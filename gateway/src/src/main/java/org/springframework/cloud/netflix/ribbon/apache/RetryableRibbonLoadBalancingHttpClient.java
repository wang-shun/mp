package org.springframework.cloud.netflix.ribbon.apache;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.feign.ribbon.FeignRetryPolicy;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.support.RetryableStatusCodeException;
import org.springframework.http.HttpRequest;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

public class RetryableRibbonLoadBalancingHttpClient extends RibbonLoadBalancingHttpClient implements ServiceInstanceChooser {
	private LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory =
			new LoadBalancedRetryPolicyFactory.NeverRetryFactory();
	public RetryableRibbonLoadBalancingHttpClient(IClientConfig config, ServerIntrospector serverIntrospector, LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory) {
		super(config, serverIntrospector);
		this.loadBalancedRetryPolicyFactory = loadBalancedRetryPolicyFactory;
	}

	@Override
	public RibbonApacheHttpResponse execute(final RibbonApacheHttpRequest request, final IClientConfig configOverride) throws Exception {
		final RequestConfig.Builder builder = RequestConfig.custom();
		IClientConfig config = configOverride != null ? configOverride : this.config;
		builder.setConnectTimeout(config.get(
				CommonClientConfigKey.ConnectTimeout, this.connectTimeout));
		builder.setSocketTimeout(config.get(
				CommonClientConfigKey.ReadTimeout, this.readTimeout));
		builder.setRedirectsEnabled(config.get(
				CommonClientConfigKey.FollowRedirects, this.followRedirects));

		final RequestConfig requestConfig = builder.build();
		final LoadBalancedRetryPolicy retryPolicy = loadBalancedRetryPolicyFactory.create(this.getClientName(), this);
		RetryCallback retryCallback = new RetryCallback() {
			@Override
			public RibbonApacheHttpResponse doWithRetry(RetryContext context) throws Exception {
				//on retries the policy will choose the server and set it in the context
				//extract the server and update the request being made
				RibbonApacheHttpRequest newRequest = request;
				ServiceInstance service = null;
				if(context instanceof LoadBalancedRetryContext) {
					service = ((LoadBalancedRetryContext)context).getServiceInstance();
					if(service != null) {
						//Reconstruct the request URI using the host and port set in the retry context
						newRequest = newRequest.withNewUri(new URI(service.getUri().getScheme(),
								newRequest.getURI().getUserInfo(), service.getHost(), service.getPort(),
								newRequest.getURI().getPath(), newRequest.getURI().getQuery(),
								newRequest.getURI().getFragment()));
					}
				}
				if (isSecure(configOverride)) {
					final URI secureUri = UriComponentsBuilder.fromUri(newRequest.getUri())
							.scheme("https").build().toUri();
					newRequest = newRequest.withNewUri(secureUri);
				}
				HttpUriRequest httpUriRequest = newRequest.toRequest(requestConfig);
				try {
					final HttpResponse httpResponse = RetryableRibbonLoadBalancingHttpClient.this.delegate.execute(httpUriRequest);
					if(retryPolicy.retryableStatusCode(httpResponse.getStatusLine().getStatusCode())) {
						throw new RetryableStatusCodeException(RetryableRibbonLoadBalancingHttpClient.this.clientName,
								httpResponse.getStatusLine().getStatusCode());
					}
					return new RibbonApacheHttpResponse(httpResponse, httpUriRequest.getURI());
				} catch (Exception ex) {
					// 请求异常，标记服务器为down，在下个轮询中会从erueka中自动加载
					ILoadBalancer loadBalancer = RetryableRibbonLoadBalancingHttpClient.this.getLoadBalancer();
					if (service != null && loadBalancer instanceof BaseLoadBalancer) {
						String serverId = service.getHost() + ":" + service.getPort();
						((BaseLoadBalancer)loadBalancer).markServerDown(serverId);
					}
					throw ex;
				}
			}
		};
		return this.executeWithRetry(request, retryPolicy, retryCallback);
	}

	private RibbonApacheHttpResponse executeWithRetry(RibbonApacheHttpRequest request, LoadBalancedRetryPolicy retryPolicy, RetryCallback<RibbonApacheHttpResponse, IOException> callback) throws Exception {
		RetryTemplate retryTemplate = new RetryTemplate();
		boolean retryable = request.getContext() == null ? true :
				BooleanUtils.toBooleanDefaultIfNull(request.getContext().getRetryable(), true);
		retryTemplate.setRetryPolicy(retryPolicy == null || !retryable ? new NeverRetryPolicy()
				: new RetryPolicy(request, retryPolicy, this, this.getClientName()));
		return retryTemplate.execute(callback);
	}

	@Override
	public ServiceInstance choose(String serviceId) {
		Server server = this.getLoadBalancer().chooseServer(serviceId);
		
		ServiceInstance service = new MyRibbonServer(serviceId, server);
		
		return service;
	}

	@Override
	public RequestSpecificRetryHandler getRequestSpecificRetryHandler(RibbonApacheHttpRequest request, IClientConfig requestConfig) {
		return new RequestSpecificRetryHandler(false, false, RetryHandler.DEFAULT, null);
	}

	static class RetryPolicy extends FeignRetryPolicy {
		public RetryPolicy(HttpRequest request, LoadBalancedRetryPolicy policy, ServiceInstanceChooser serviceInstanceChooser, String serviceName) {
			super(request, policy, serviceInstanceChooser, serviceName);
		}
	}
	
	public static class MyRibbonServer implements ServiceInstance {
		private final String serviceId;
		private final Server server;
		private final boolean secure;
		private Map<String, String> metadata;

		public MyRibbonServer(String serviceId, Server server) {
			this(serviceId, server, false, new HashMap<String, String>());
		}

		public MyRibbonServer(String serviceId, Server server, boolean secure,
				Map<String, String> metadata) {
			this.serviceId = serviceId;
			this.server = server;
			this.secure = secure;
			this.metadata = metadata;
		}

		@Override
		public String getServiceId() {
			return this.serviceId;
		}

		@Override
		public String getHost() {
			return this.server.getHost();
		}

		@Override
		public int getPort() {
			return this.server.getPort();
		}

		@Override
		public boolean isSecure() {
			return this.secure;
		}

		@Override
		public URI getUri() {
			return DefaultServiceInstance.getUri(this);
		}

		@Override
		public Map<String, String> getMetadata() {
			return this.metadata;
		}

		public Server getServer() {
			return this.server;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("RibbonServer{");
			sb.append("serviceId='").append(serviceId).append('\'');
			sb.append(", server=").append(server);
			sb.append(", secure=").append(secure);
			sb.append(", metadata=").append(metadata);
			sb.append('}');
			return sb.toString();
		}
	}
}
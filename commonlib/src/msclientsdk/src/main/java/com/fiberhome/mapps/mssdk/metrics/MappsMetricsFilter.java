package com.fiberhome.mapps.mssdk.metrics;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UrlPathHelper;


/**
 * Copy from MetricsFilter
 * @author Administrator
 *
 */
public class MappsMetricsFilter extends OncePerRequestFilter {
	private static final String ATTRIBUTE_STOP_WATCH = MappsMetricsFilter.class.getName()
			+ ".StopWatch";

	private static final int UNDEFINED_HTTP_STATUS = 999;

	private static final String UNKNOWN_PATH_SUFFIX = "/unmapped";

	private static final Log logger = LogFactory.getLog(MappsMetricsFilter.class);

	private final CustomMetricsWriter metricsWriter;

	private static final Set<PatternReplacer> STATUS_REPLACERS;

	static {
		Set<PatternReplacer> replacements = new LinkedHashSet<PatternReplacer>();
		replacements.add(new PatternReplacer("[{}]", 0, "-"));
		replacements.add(new PatternReplacer("**", Pattern.LITERAL, "-star-star-"));
		replacements.add(new PatternReplacer("*", Pattern.LITERAL, "-star-"));
		replacements.add(new PatternReplacer("/-", Pattern.LITERAL, "/"));
		replacements.add(new PatternReplacer("-/", Pattern.LITERAL, "/"));
		STATUS_REPLACERS = Collections.unmodifiableSet(replacements);
	}

	private static final Set<PatternReplacer> KEY_REPLACERS;

	static {
		Set<PatternReplacer> replacements = new LinkedHashSet<PatternReplacer>();
		replacements.add(new PatternReplacer("/", Pattern.LITERAL, "."));
		replacements.add(new PatternReplacer("..", Pattern.LITERAL, "."));
		KEY_REPLACERS = Collections.unmodifiableSet(replacements);
	}

	MappsMetricsFilter(CustomMetricsWriter metricsWriter) {
		this.metricsWriter = metricsWriter;
	}

	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
					throws ServletException, IOException {
		StopWatch stopWatch = createStopWatchIfNecessary(request);
		String path = new UrlPathHelper().getPathWithinApplication(request);
		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		try {
			chain.doFilter(request, response);
			status = getStatus(response);
		}
		finally {
			if (!request.isAsyncStarted()) {
				stopWatch.stop();
				request.removeAttribute(ATTRIBUTE_STOP_WATCH);
				recordMetrics(request, path, status, stopWatch.getTotalTimeMillis());
			}
		}
	}

	private StopWatch createStopWatchIfNecessary(HttpServletRequest request) {
		StopWatch stopWatch = (StopWatch) request.getAttribute(ATTRIBUTE_STOP_WATCH);
		if (stopWatch == null) {
			stopWatch = new StopWatch();
			stopWatch.start();
			request.setAttribute(ATTRIBUTE_STOP_WATCH, stopWatch);
		}
		return stopWatch;
	}

	private int getStatus(HttpServletResponse response) {
		try {
			return response.getStatus();
		}
		catch (Exception ex) {
			return UNDEFINED_HTTP_STATUS;
		}
	}

	private void recordMetrics(HttpServletRequest request, String path, int status,
			long time) {
		String suffix = getFinalStatus(request, path, status);
		submitToGauge(suffix, time);
		incrementCounter(status, suffix);
	}

	private String getFinalStatus(HttpServletRequest request, String path, int status) {
		Object bestMatchingPattern = request
				.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if (bestMatchingPattern != null) {
			return fixSpecialCharacters(bestMatchingPattern.toString());
		}
		Series series = getSeries(status);
		if (Series.CLIENT_ERROR.equals(series) || Series.SERVER_ERROR.equals(series)
				|| Series.REDIRECTION.equals(series)) {
			return UNKNOWN_PATH_SUFFIX;
		}
		return path;
	}

	private String fixSpecialCharacters(String value) {
		String result = value;
		for (PatternReplacer replacer : STATUS_REPLACERS) {
			result = replacer.apply(result);
		}
		if (result.endsWith("-")) {
			result = result.substring(0, result.length() - 1);
		}
		if (result.startsWith("-")) {
			result = result.substring(1);
		}
		return result;
	}

	private Series getSeries(int status) {
		try {
			return HttpStatus.valueOf(status).series();
		}
		catch (Exception ex) {
			return null;
		}

	}

	private String getKey(String string) {
		// graphite compatible metric names
		String key = string;
		for (PatternReplacer replacer : KEY_REPLACERS) {
			key = replacer.apply(key);
		}
		if (key.endsWith(".")) {
			key = key + "root";
		}
		if (key.startsWith("_")) {
			key = key.substring(1);
		}
		return key;
	}

	private void submitToGauge(String suffix, double value) {
		try {
			MetricName gauge = new MetricName("http.response");
			gauge.tag("endpoint", suffix);
			metricsWriter.submit(gauge, value);
		}
		catch (Exception ex) {
			logger.warn("Unable to submit gauge response '" + suffix + "'", ex);
		}
	}

	private void incrementCounter(int status, String suffix) {
		try {
			MetricName meter = new MetricName("http.status." + status);
			meter.tag("endpoint", suffix);
			metricsWriter.increment(meter);
		}
		catch (Exception ex) {
			logger.warn("Unable to submit counter status '" + status + "of " + suffix + "'", ex);
		}
	}

	private static class PatternReplacer {

		private final Pattern pattern;

		private final String replacement;

		PatternReplacer(String regex, int flags, String replacement) {
			this.pattern = Pattern.compile(regex, flags);
			this.replacement = replacement;
		}

		public String apply(String input) {
			return this.pattern.matcher(input)
					.replaceAll(Matcher.quoteReplacement(this.replacement));
		}

	}

}

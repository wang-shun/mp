(function(){
	$.ajaxSetup({
		// cache: false,
		error:function(jqXHR, textStatus, errorMsg){
			var responseText = jqXHR.responseText || "";
			if(responseText.indexOf("token invalid") != -1){
				new Toast("登录过期，请重新进入应用").show();
				if (NativeObj) {
					var token = NativeObj.getToken();
					var sessionId = escape(token);
	            	window.localStorage.setItem("sessionId",sessionId);
				} else {
					window.localStorage.setItem("sessionId",window.token);
				}
            	// location.href="index.html";
				return;
			}
			console.log( '发送AJAX请求到"' + this.url + '"时出错[' + jqXHR.status + ']：' + errorMsg ); 
			new Toast("请求失败！").show();
		},
		complete:function(xhr, status){
			var responseText = xhr.responseText || "";
			if(responseText.indexOf("token invalid") != -1){
				new Toast("登录过期，请重新进入应用").show();
				if (NativeObj) {
					var token = NativeObj.getToken();
					var sessionId = escape(token);
	            	window.localStorage.setItem("sessionId",sessionId);
				} else {
					window.localStorage.setItem("sessionId",window.token);
				}
				/*if(window.location.pathname !== "/index.html"){
					window.location.replace('index.html');
				}*/
			}
		}
	});

	window.serviceUrl = "http://miap.cc:9100/vt/api";
	//window.serviceUrl = "http://192.168.4.85:8080/api";
	window.accessId = "f75b6062-ec9c-4fbf-ad74-84854ed05968";
	//window.token = "mobileark#e8b05dc2-9ef7-48e0-af06-acfae88d09b8";
	window.localStorage.setItem("serviceUrl",serviceUrl);
	//window.accessId = "edcf25ab-de68-429e-8a47-e7d02b002269";
	//window.hostURL = "http://miap.cc:8384";
	// window.accessId = "4a4400ab-d8ac-401d-9350-4491957b31f4";
	// window.hostURL = "http://192.168.4.179:6060/microapp";
	//测试
	/*window.accessId = "85afb7ca-680c-44b6-8c63-0d1174b9db4e";
	window.hostURL = "http://192.168.160.89:6060";*/
	
	window.photoDownload = window.serviceUrl + "/photoupload/photoDownload.action?photoUploadId=";
	window.photoDownload_m = window.serviceUrl + "/photoupload/photoDownload.action?size=m&photoUploadId=";
	var ajaxModel = {
		/**
		 * [cacheAjaxObject description]
		 * @param  {[type]} ajaxUrl [后端接口地址]
		 * @param  {[type]} params  [参数]
		 * @param  {[type]} type    [GET or POST]
		 * @param  {[type]} custom  [ajax 更多参数配置项]
		 * @return {[type]}         [返回一个ajax对象]]
		 */
		ajaxObject: function(ajaxUrl, params, type, custom) {
			// var sessionId = "mobileark%23440f6623-4903-4b72-9f67-2c112a1fa7f8";
			var sessionId = window.localStorage.getItem("sessionId");
			var serviceUrl = window.localStorage.getItem("serviceUrl");
			// alert(sessionId);
			var defUrl = "?sessionId="+sessionId+"&reqSource=client";
			ajaxUrl = defUrl + ajaxUrl;
//			alert(serviceUrl+ajaxUrl);
			var ajaxParams = {
					url: serviceUrl+ajaxUrl,
					type: type || 'GET',
					dataType: 'json',
					data: params
				},
				ajaxObject;
			$.extend(ajaxParams,custom);
			ajaxObject = $.ajax(ajaxParams);

			return ajaxObject;
		},
		getData: function(ajaxUrl, params, custom){
			var ajaxObject = this.ajaxObject(ajaxUrl, params, 'GET', custom);
			return ajaxObject;
		},
		postData: function(ajaxUrl, params, custom){
			var ajaxObject = this.ajaxObject(ajaxUrl, params, 'POST', custom);
			return ajaxObject;
		}
	}
	return window.ajaxModel = ajaxModel;
})()
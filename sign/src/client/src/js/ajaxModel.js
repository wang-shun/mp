(function(){
	$.ajaxSetup({
		// cache: false,
		timeout : 10000, //超时时间设置，单位毫秒
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
				return;
			}
			console.log( '发送AJAX请求到"' + this.url + '"时出错[' + jqXHR.status + ']：' + errorMsg ); 
			new Toast("请求失败！").show();
		},
		complete:function(xhr, status){
			var responseText = xhr.responseText || "";
			if(status=='timeout'){//超时,status还有success,error等值的情况
		　　　　　 new Toast("请求超时！").show();
		　　　} else if(responseText.indexOf("token invalid") != -1){
				new Toast("登录过期，请重新进入应用").show();
				if (NativeObj) {
					var token = NativeObj.getToken();
					var sessionId = escape(token);
	            	window.localStorage.setItem("sessionId",sessionId);
				} else {
					window.localStorage.setItem("sessionId",window.token);
				}
    			//if(window.location.pathname !== "/index.html"){
				// 	window.location.replace('index.html');
				// }
			}
		}
	});
	//8209
	// window.accessId = "edcf25ab-de68-429e-8a47-e7d02b002269";
	// window.hostURL = "http://miap.cc:8384";
	// window.arkURL = "https://miap.cc:8209";
	
	//现场
	// window.accessId = "92f5da32-1983-4c2d-9062-6a33ee37cf3c";
	// window.hostURL = "http://www.onlysomeapps.com:6060";
	// window.arkURL = "https://www.onlysomeapps.com:7443";

	//开发
	//window.accessId = "4a4400ab-d8ac-401d-9350-4491957b31f4";
	//window.hostURL = "http://10.1.23.151:8087/microapp";
	//window.arkURL = "http://192.168.160.98:6001";// /clientdownload?

	//测试
	// window.accessId = "c61f5ad3-06f3-42cd-9eb6-fe832d1d75a5";
	// window.hostURL = "http://192.168.160.174:6060";
	// window.arkURL = "http://192.168.160.174:7001";

	//window.serviceUrl = "http://miap.cc:9100/sn/api";
	
	
	//网页测试
	/*window.serviceUrl = "http://192.168.10.210:18005/api";
	var token = "mobileark#c38a80b1-7357-4fc6-ac4c-060f57b1aa52";
	var sessionId = escape(token);
	window.localStorage.setItem("sessionId",sessionId);*/
	if(window.localStorage.getItem("serviceUrl") == ""){
		window.localStorage.setItem("serviceUrl",window.serviceUrl);
	}
	
	//window.photoDownload = window.serviceUrl + "/photoupload/photoDownload.action?photoUploadId=";
	//window.photoDownload_m = window.serviceUrl + "/photoupload/photoDownload.action?size=m&photoUploadId=";
	window.photoDownload = "";window.photoDownload_m = "";
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
			// var sessionId = escape("mobileark#5bee3ef2-0459-46a8-8c12-543eb264dab3");
			// var sessionId = "mobileark%239bd74d31-88eb-447e-b195-c1160d972e09"
			var sessionId = window.localStorage.getItem("sessionId");
			var serviceUrl = window.localStorage.getItem("serviceUrl");
			// alert(sessionId)
			var defUrl = "?sessionId="+sessionId+"&reqSource=client";
			ajaxUrl = defUrl + ajaxUrl;
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
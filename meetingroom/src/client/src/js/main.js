requirejs.config({
	waitSeconds: 0,
	config:{
		text: {
			useXhr:function(){return true;}
		}
	},
	paths: {
		"seedsui":"lib/seedsui/seedsui.min",
		"jquery":"lib/jquery/jquery-1.7.2.min",
		"underscore":"lib/underscore",
		"text":"lib/text-2.0.12",
		"domReady":"lib/domReady-2.0.1.js",
		"backbone":"lib/backbone",
		"spin":"lib/spinjs-rails/spin",
        "constants":"util/constants"
	},
	//依赖和导出配置
	shim: {
		"underscore": {
			deps: [],
			exports: "_"
		},	
		"backbone": {
			deps: ["jquery", "underscore", "text"],
			exports: "Backbone"
		},
		"spin": {
			deps: [],
			exports: "spin"
		}
	}
});

require(["jquery", "app"],
	function ($,  Application){
		document.addEventListener("clientready", onPlusReady($, Application));

		document.addEventListener("plusready", onPlusReady($, Application));

		$(document).ready(onDocReady($, Application));
	}
);

var plusReady = false;
var docReady =false;

function onPlusReady($, Application) {
	if (plusReady) return; // 避免重复执行
	plusReady = true;
	if (docReady) {
		webViewReady($, Application);
	}
}

function onDocReady($, Application) {
	docReady = true;
	if (plusReady) {
		webViewReady($, Application);
	}
}

function webViewReady($, Application) {
	//客户端通过网关请求访问相应服务端
	mplus.getServiceAddress({
	    success: function (res) {
	        var serviceAddress = res.serviceAddress; //应用服务网关地址
	        var serviceFinalUrl = serviceAddress+"/mapps-meetingroom/api";
	        window.serviceUrl = serviceFinalUrl;
	        webViewAfterReady(Application);
		},
		fail: function (res) {
			webViewAfterReady(Application);
			/*alert("未获取到应用服务网关地址:"+res.errMsg);
			return;*/
		}
	});
	//webViewAfterReady(Application);
}

function webViewAfterReady(Application){
	window.token = 'mobileark%236b216a16-1587-4f6e-82bf-e4d68c7bcf15';
	if (NativeObj) {
		window.token = NativeObj.getToken();
		console.log("mplus token:" + window.token);
	}
	Application.create({
		routeOnStart: true,
		popupDisplayMode: 'NonBlocking'
	}).start();
}
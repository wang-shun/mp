/**
 * 配置别名和依赖关系
 */
require.config({
    waitSeconds: 0,
    config:{
        text: {
            useXhr:function(){return true;}
        }
    },
    paths: {
        seedsui:"lib/seedsui/seedsui.min",
        swiper:"lib/swiper/swiper.min",
        jquery:"lib/jquery/jquery.min",
        text:"lib/require/text",
        underscore:"lib/backbone/underscore",
        backbone:"lib/backbone/backbone"
    },
    // 不是AMD的lib
    shim: {
        /*zepto: {
            exports: '$'
        },*/
        underscore:{
            exports:'_'
        },
        backbone:{
            deps:['jquery','underscore','text'],
            exports:'Backbone'
        }
    }

});

/**
 * 主应用程序初始化和初始身份验证检查
 */
require(["app","router"],
function(app, WebRouter) {
	document.addEventListener("clientready", onPlusReady(app, WebRouter));
	document.addEventListener("plusready", onPlusReady(app, WebRouter));
	$(document).ready(onDocReady(app, WebRouter));
});


var plusReady = false;
var docReady =false;

function onPlusReady(app, WebRouter) {
	if (plusReady) return; // 避免重复执行
	plusReady = true;
	if (docReady) {
		webViewReady(app, WebRouter);
	}
}

function onDocReady(app, WebRouter) {
	docReady = true;
	if (plusReady) {
		webViewReady(app, WebRouter);
	}
}

function webViewReady(app, WebRouter) {
	//客户端通过网关请求访问相应服务端
	mplus.getServiceAddress({
	    success: function (res) {
	        var serviceAddress = res.serviceAddress; //应用服务网关地址
	        var serviceFinalUrl = serviceAddress+"/mapps-dbr/api";
	        window.serviceUrl = serviceFinalUrl;
	        webViewAfterReady(app, WebRouter);
		},
		fail: function (res) {
			webViewAfterReady(app, WebRouter);
			/*alert("未获取到应用服务网关地址:"+res.errMsg);
			return;*/
		}
	});
}

function webViewAfterReady(app, WebRouter){
	window.token = 'mobileark%230c2dbab4-4c41-4d70-b091-792c73081b56';
	if (NativeObj) {
		window.token = escape(NativeObj.getToken());
		console.log("mplus token:" + window.token);
	}
	// 只需使用GET和POST支持所有浏览器
    Backbone.emulateHTTP = true;
    app.router = new WebRouter();
}
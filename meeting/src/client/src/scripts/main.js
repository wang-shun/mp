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
	document.addEventListener("clientready", onReady(app, WebRouter));
	document.addEventListener("plusready", onReady(app, WebRouter));
	$(document).ready(function() {
		onReady(app, WebRouter);
	});
});

var plusReady = false;
function onReady(app, WebRouter){
    if (plusReady) return; // 避免重复执行
    plusReady = true;
    console.log("app ready...");
    //客户端通过网关请求访问相应服务端
	mplus.getServiceAddress({
	    success: function (res) {
	        var serviceAddress = res.serviceAddress; //应用服务网关地址
	        var serviceFinalUrl = serviceAddress+"/mapps-meeting/api";
	        window.serviceUrl = serviceFinalUrl;
	        webViewAfterReady();
		},
		fail: function (res) {
			webViewAfterReady();
			/*alert("未获取到应用服务网关地址:"+res.errMsg);
			return;*/
		}
	});
}

function webViewAfterReady(){
	require(["app","router"],
	function(app, WebRouter) {
	    Backbone.emulateHTTP = true;//只需使用GET和POST支持所有浏览器
	    app.router = new WebRouter();
	});
}
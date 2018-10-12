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
	// 只需使用GET和POST支持所有浏览器
    Backbone.emulateHTTP = true;
    app.router = new WebRouter();
}


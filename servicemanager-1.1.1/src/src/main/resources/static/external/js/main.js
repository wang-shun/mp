requirejs.config({
	waitSeconds: 0,
	config:{
		text: {
			useXhr:function(){return true;}
		}
	},
	paths: {
		"seedsui":"lib/seedsui/seedsui.min",
		"seedsuipinyin":"lib/seedsui/seedsui.pinyin",
		"jquery":"lib/jquery/jquery.min",
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
		$(document).ready(function(){
			Application.create({
				routeOnStart: true,
				popupDisplayMode: 'NonBlocking'
			}).start();
		});
	}
);
requirejs.config({
	waitSeconds: 0,
	config:{
		text: {
			useXhr:function(){return true;}
		}
	},
	paths: {
		"jquery":"lib/jquery/jquery-1.7.2.min",
		"jqueryUi":"lib/jquery-ui-1.12.0.custom/jquery-ui.min",
		"underscore":"lib/underscore",
		"text":"lib/text-2.0.12",
		"domReady":"lib/domReady-2.0.1.js",
		"backbone":"lib/backbone",
		"lhgdialog":"lib/lhgdialog/lhgdialog.min",
		"fh":"lib/fh.dialog",
		"spin":"lib/spinjs-rails/spin",
		"selectbox":"lib/selectbox/jquery.selectbox-0.2",
		"datatable_lnpagination":"lib/datatable/datatable.lnpagination",
		"datatable_columresize":"lib/datatable/jquery.datatable.columresize",
		"dataTables_min":"lib/datatable/jquery.dataTables.min",
		"datatableUtil":"util/datatableUtil",
		"jquery_ztree_core":"lib/ztree/jquery.ztree.core-3.5",
		"jquery_ztree_excheck":"lib/ztree/jquery.ztree.excheck-3.5",
		"jquery_ztree_exhide":"lib/ztree/jquery.ztree.exhide-3.5",
		"plupload":"lib/upload/plupload.full.min",
		"imagesloaded":"lib/imagesloaded.pkgd.min",
		"masonry":"lib/jquery.masonry.min",
		"lightbox":"lib/lightbox",
		"jcrop": "lib/jcrop/js/jquery.Jcrop",
		"ajaxfileupload": "util/ajaxfileupload",
		"jquery.validate":"lib/validate/jquery.validate",
		"jquery.MetaData":"lib/validate/jquery.MetaData",
		"validate_methods":"lib/validate/additional-methods",
		"messages_zh":"lib/validate/messages_zh",
        "cryptoCore":"lib/crypto-core",
        "aes":"lib/aes",
        "modeEcb":"lib/mode-ecb",
        "icheck":"lib/icheck/icheck", //http://www.bootcss.com/p/icheck/
        "MD5":"lib/MD5",
        "constants":"util/constants",
        "preferenceUtil":"util/preferenceUtil",
        "echarts":"lib/echarts/echarts-all",
        "viewer":"lib/zoomify/viewer-jquery.min",
        "ckeditor":"lib/ckeditor/ckeditor"
        
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
		"fh": {
			deps: ["jquery", "lhgdialog"],
			exports: "fh"
		},		
		"spin": {
			deps: [],
			exports: "spin"
		},		
		"selectbox": {
			deps: ["jquery"],
			exports: "selectbox"
		},		
		"plupload": {
			deps: ["jquery"],
			exports: "plupload"
		},		
		"imagesloaded": {
			deps: ["jquery"],
			exports: "imagesloaded"
		},		
		"lightbox": {
			deps: ["jquery"],
			exports: "lightbox"
		},		
		"jcrop": {
			deps: ["jquery"],
			exports: "jcrop"
		},	
		"ajaxfileupload": {
			deps: ["jquery"],
			exports: "ajaxfileupload"
		},	
		"masonry": {
			deps: ["jquery"],
			exports: "masonry"
		},		
		"datatable_lnpagination": {
			deps: ["jquery","dataTables_min"],
			exports: "datatable_lnpagination"
		},		
		"datatable_columresize": {
			deps: ["jquery","dataTables_min"],
			exports: "datatable_columresize"
		},
		"jquery_ztree_core": {
			deps: ["jquery"],
			exports: "jquery_ztree_core"
		},
		"jquery_ztree_excheck": {
			deps: ["jquery","jquery_ztree_core"],
			exports: "jquery_ztree_excheck"
		},
		"jquery_ztree_exhide": {
			deps: ["jquery","jquery_ztree_core"],
			exports: "jquery_ztree_exhide"
		},
		"jquery.validate": {
			deps: ["jquery","jquery.MetaData"],
			exports: "jquery.validate"
		},
		"validate_methods": {
			deps: ["jquery","jquery.validate"],
			exports: "validate_methods"
		},
		"messages_zh": {
			deps: ["jquery","jquery.validate"],
			exports: "messages_zh"
		},
        "jqueryUi": {
            deps: ["jquery"],
            exports: "jqueryUi"
        },
        "cryptoCore": {
            deps: [],
            exports: "cryptoCore"
        },
        "aes": {
            deps: ["cryptoCore"],
            exports: "aes"
        },        
        "modeEcb": {
            deps: ["cryptoCore","aes"],
            exports: "modeEcb"
        },
        "icheck": {
            deps: ["jquery"],
            exports: "icheck"
        },
        "MD5": {
            deps: [],
            exports: "MD5"
        },
		"echarts": {
			deps: ["jquery"],
			exports: "echarts"
		},
		"viewer": {
            deps: ["jquery"],
            exports: "viewer"
        },
		"ckeditor": {
            deps: ["jquery"],
            exports: "ckeditor"
        }
	}
});

require(["jquery", "app"],
	function ($,  Application){
	    $(window).resize(function () {          //当浏览器大小变化时
	    	windowResize();
	    });
		$(document).ready(function(){
			Application.create({
				routeOnStart: true,
				popupDisplayMode: 'NonBlocking'
			}).start();
		});
	}
);
function windowResize(){
	var height = $(window).height();
	var h1 = 70+40+47+4;
	var h2 = 70+40+38+70;
	var divH1 = $(".SID-scrollbar").height()+120;
	var divH2 = $(".SID-scrollbar1").height()+120;
	if (height-h1 < divH1) {
		$(".SID-scrollbar").css("overflow","auto");
		$(".SID-scrollbar").css("height",(height-h1)+"px");
	} else {
		$(".SID-scrollbar").css("overflow","hidden");
		$(".SID-scrollbar").css("height","100%");
	}
	if (height-h2 < divH2) {
		$(".SID-scrollbar1").css("overflow","auto");
		$(".SID-scrollbar1").css("height",(height-h2)+"px");
	} else {
		$(".SID-scrollbar1").css("overflow","hidden");
		$(".SID-scrollbar1").css("height","100%");
	}
}
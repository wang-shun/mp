requirejs.config({
	waitSeconds: 0,
	config:{
		text: {
			useXhr:function(){return true;}
		}
	},
	paths: {
		"jquery":"lib/jquery/jquery-1.7.2.min",
		"editableselect":"lib/editableselect/jquery-editable-select",
		"cursorPosition":"lib/cursorPosition/cursorPosition",
		"jqueryUi":"lib/jquery/jquery-ui.min",
		"jqueryUiSlide":"lib/jquery/timepicker/jquery-ui-sliderAccess",
		"jqueryUiTimepicker":"lib/jquery/timepicker/jquery-ui-timepicker-addon",
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
        "echarts":"lib/echarts/echarts.common.min"
        
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
        "jqueryUiSlide": {
            deps: ["jquery","jqueryUi"],
            exports: "jqueryUiSlide"
        },
        "jqueryUiTimepicker": {
            deps: ["jquery","jqueryUi"],
            exports: "jqueryUiTimepicker"
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
            deps: [],
            exports: "echarts"
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
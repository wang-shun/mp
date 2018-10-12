/**
 * @全局app
 */
define([
    "jquery","underscore","backbone",
    "text!templates/global/nodataTemplate.html",
    "text!templates/global/errorTemplate.html",
    "text!templates/global/loadTemplate.html",
    "seedsui","swiper"
],function($, _, Backbone,nodataTemplate,errorTemplate,loadTemplate) {
    window.token=escape('mobileark#ab0ac017-5b14-4633-8ac4-16b528240a98');
    if (NativeObj) {
        window.token = escape(NativeObj.getToken());
        console.log("mplus token:" + window.token);
    }
    var app = {
    	routerViews:{},
        constants:{
            IMAGEPATH:"images"
        },
        pages:{
            index:"ID-PageIndex",
            meetDetail:"ID-PageMeetDetail",
            statDetail:"ID-PageStatDetail"
        },
        isBack:false,
        ropMethod:{
        	meetList:"format=json&v=1.0&appKey=&method=mapps.survey.clientquery&sessionId="+window.token,
        	preview:"format=json&v=1.0&appKey=&method=mapps.survey.clientdetail&sessionId="+window.token,
        	submit:"format=json&v=1.0&appKey=&method=mapps.survey.clientsubmit&sessionId="+window.token,
        	stat:"format=json&v=1.0&appKey=&method=mapps.survey.dataAnalysis&sessionId="+window.token
        },
        color:['#9abf88','#5698c4','#7c9fb0','#e0598b','#e279a3'
               ,'#9163b6','#4e2472','#65387d','#993767','#a34974'
               ,'#be5168','#c94a53','#e16552','#f19670','#e8975d'
               ,'#e9d78e','#e4bf80','#8e8c6d','#74c493','#447c69'
               ,'#51574a'],
       ismobile:false,
       isSubmit:false
    };
    var browser = {
        versions: function () {
            var u = navigator.userAgent, app = navigator.appVersion;
            return {
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
            }; 
        }(), 
        language: (navigator.browserLanguage || navigator.language).toLowerCase() 
    }      
    app.ismobile =   browser.versions.mobile;
    //没有数据html
    app.nodataHTML=_.template(nodataTemplate,{imgPath: app.constants.IMAGEPATH});
    //错误html
    app.errorHTML=_.template(errorTemplate,{imgPath: app.constants.IMAGEPATH});
    //加载html
    app.loadHTML=_.template(loadTemplate,{imgPath: app.constants.IMAGEPATH});
    //加载遮罩
    app.loading=new Loading({container:"#ID-Loading"});
    //提示框
    app.prompt=new Prompt("",{
        css:{backgroundColor:"#20aeff"}
    });
    //时间选择
    app.spDateTime=null;
    //控件
    app.formControls=new FormControls();
    //小提示框
    app.toast=new Toast("");
    //单页模式
    var initHistory=function(){
        app.history=new History({
            onBack:function(e){
                if(app.spDateTime){
                    app.spDateTime.scrollpicker.hide();
                    app.spDateTime=null;
                }
                if(e.discardList){
                    for(var i=0,discardLi;discardLi=e.discardList[i++];){
                        var hash=discardLi.indexOf("/")>=0?discardLi.substring(0,discardLi.indexOf("/")):discardLi;
                        document.querySelector(hash+"-Section").classList.remove("active");
                    }
                }
                //console.log("后退："+e.prevHash);
                app.isBack=true;
                if(e.prevHash){
                    var prevHash=e.prevHash.indexOf("/")>=0?e.prevHash.substring(0,e.prevHash.indexOf("/")):e.prevHash;
                    document.querySelector(prevHash+"-Section").classList.remove("active");
                }
            },
            onForward:function(e){
                //console.log("前进："+e.currentHash);
                app.isBack=false;
                var currentHash=e.currentHash.indexOf("/")>=0?e.currentHash.substring(0,e.currentHash.indexOf("/")):e.currentHash;
                document.querySelector(currentHash+"-Section").classList.add("active");
            }
        });
        app.history.clearList();
        if(location.hash){
            app.history.add(location.hash);
        }
        //初始化时，hash值就存在了，直接进入该页面
        app.isHomePage=false;
        var hash=window.location.hash;
        if(hash){
            var page=hash.indexOf("/")>=0?hash.substring(0,hash.indexOf("/")):hash;
            document.querySelector(page+"-Section").classList.add("active");
            app.isHomePage=true;
            if(app.history.list.indexOf(hash)==-1){
                app.history.clearList();
            }
        }
    };
    initHistory();
    
    return app;
});
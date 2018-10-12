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
    //window.token=escape('mobileark#c2f888db-0746-48dc-b346-a949b409ffe7');
//    if (NativeObj) {
//        window.token = escape(NativeObj.getToken());
//        console.log("mplus token:" + window.token);
//    }
    var app = {
    	routerViews:{},
        constants:{
            IMAGEPATH:"images"
        },
        pages:{
            index:"ID-PageIndex",
            meetDetail:"ID-PageMeetDetail"
        },
        isBack:false,
        ropMethod:{
        	meetList:"format=json&v=1.0&appKey=&method=mapps.survey.clientquery",//&sessionId="+window.token,
        	preview:"format=json&v=1.0&appKey=&method=mapps.survey.clientdetail",//&sessionId="+window.token,
        	submit:"format=json&v=1.0&appKey=&method=mapps.survey.clientsubmit"//&sessionId="+window.token
        }
    };
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
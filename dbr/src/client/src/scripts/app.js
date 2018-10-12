/**
 * @全局app
 */
define([
    "jquery","underscore","backbone",

    "text!templates/global/nodataTemplate.html",
    "text!templates/global/errorTemplate.html",
    
    "seedsui","swiper"
],function($, _, Backbone,nodataTemplate,errorTemplate) {
    var app = {
    	routerViews:{},
        constants:{
            IMAGEPATH:"images"
        },
        confirmMsg:{
        	'300007' : "您确认管理此设备吗？",
        	'300008' : "您确认借取此设备吗？",
        	'300009' : "您确认回收此设备吗？"
        },
        pages:{
            index:"ID-PageIndex",
            check:"ID-PageCheck",
            meetSearch:"ID-PageMeetSearch",
            
        },
        isBack:false,
        serviceUrl:"https://miap.cc:8453/dbr/api",
        ropMethod:{
        	deviceSubmit:"format=json&v=1.0&appKey=&method=mapps.dbr.submit",
        	deviceList:"format=json&v=1.0&appKey=&method=mapps.dbr.adminquery",
        	borrowDeviceList:"format=json&v=1.0&appKey=&method=mapps.dbr.borrowquery",
        	deviceCheck:"format=json&v=1.0&appKey=&method=mapps.dbr.check",
        	isAdmin:"format=json&v=1.0&appKey=&method=mapps.dbr.isadmin"
        },
        initLoginUserFlag:0,
        loginUser:{
        	username:"",
        	loginId:"",
        	deptId:"",
        	isAdmin:0
        },
        signDetail:[],
        approvalDetail:[],
        signinSequList:[]
    };
    //没有数据html
    app.nodataHTML=_.template(nodataTemplate,{imgPath: app.constants.IMAGEPATH});
    //错误html
    app.errorHTML=_.template(errorTemplate,{imgPath: app.constants.IMAGEPATH});
    //加载遮罩
    app.loading=new Loading({container:"#ID-Loading"});
    //提示框
    app.prompt=new Prompt("",{
        css:{backgroundColor:"#20aeff"}
    });
    //控件
    app.formControls=new FormControls();
    //小提示框
    app.toast=new Toast("");
    // 备份jquery的ajax方法  
    var _ajax = $.ajax;
    // 重写ajax方法，先判断登录在执行success函数 
    $.ajax = function(opt) {
        var _success = opt && opt.success || function(a, b) {};
        var _error = opt && opt.error;
        var _opt = $.extend(opt, {
            success: function(event, xhr, options) {
                try {
                    //判断对象的状态是交互完成
                    if (options.readyState == 4) {
                        //判断http的交互是否成功
                        if (options.status == 200) {
                            var responseBody = options.responseText;
                            var resultcode = 1;
                            var resultmessage = '';
                            if ("" != responseBody && null != responseBody && undefined != responseBody) {
                                responseBody = JSON.parse(responseBody);
                            }
                            if (resultcode != null && resultcode > 0) {
                                _success(responseBody, resultcode, resultmessage);
                            } else {

                            }
                        } else {
                            console.log("访问后台数据异常！");
                        }
                    }

                } catch (e) {
                    console.log(e);
                }
            },
            timeout:30000 //10s超时处理
       
        });
        _ajax(_opt);
    };
    //单页模式
    var initHistory=function(){
        app.history=new History({
            onBack:function(e){
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
        //初始化时，历史记录就有了，说明刷新过了
        /*if(app.history.list.length>0){
            for(var i=0,hashLi;hashLi=app.history.list[i++];){
                var hash=hashLi.indexOf("/")>=0?hashLi.substring(0,hashLi.indexOf("/")):hashLi;
                console.log(hash);
                document.querySelector(hash+"-Section").classList.add("active");
            }
        }*/
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
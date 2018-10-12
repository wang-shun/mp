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
    var app = {
    	routerViews:{},
        constants:{
            IMAGEPATH:"images"
        },
        pages:{
            index:"ID-PageIndex",
            search:"ID-PageSearch",
            searchHistory:"ID-PageSearchHistory",
            add:"ID-PageAdd",
            detail:"ID-PageDetail",
            enter:"ID-PageEnter",
            user:"ID-PageUser",
            email:"ID-PageEmail",

            tree:"ID-PageTree",
            treeSearch:"ID-PageTreeSearch",
        },
        isBack:false,
        workCircleFlag:false,
        imFlag:false,
        phone:"",
        serviceUrl:window.serviceUrl,
        ropMethod:{
        	indexTopFour:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&order=4&offset=1&limit=4&timestamp=0&actExpire=0",
            //indexDefaultList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&order=1",//综合排序
        	//indexBeginList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&order=2",//即将开始
            //indexNewList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&order=3",//最新发布
            //indexHotList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&order=4",//人气最高
            //indexJoinList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&order=5",//我参与的
            //indexCreateList:"format=json&v=1.0&appKey=&method=mapps.activity.list.queryMyCreate",//我创建的
            indexList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&actExpire=0",
            indexHistoryList:"format=json&v=1.0&appKey=&method=mapps.activity.list.query&actExpire=1",
            getDepartments:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getdepartments",
        	getUsers:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getusers",
        	addActivity:"format=json&v=1.0&appKey=&method=mapps.activity.one.add",//新增
            deleteActivity:"format=json&v=1.0&appKey=&method=mapps.activity.one.delete",//删除
            detailActivity:"format=json&v=1.0&appKey=&method=mapps.activity.one.detail",
            enterActivity:"format=json&v=1.0&appKey=&method=mapps.activity.one.enter",
            joinGroup:"format=json&v=1.0&appKey=&method=mapps.activity.group.join",
            sendEmail:"format=json&v=1.0&appKey=&method=mapps.activity.exportEnter",
            shareCircle:"format=json&v=1.0&appKey=&method=mapps.activity.circle.share"
        },
        errorCode:[ "300003","300004","300011","300018","300019","300020","300021","300022"]
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
        css:{backgroundColor:"#9783DB"}
    });
    //时间选择
    app.spDateTime=null;
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
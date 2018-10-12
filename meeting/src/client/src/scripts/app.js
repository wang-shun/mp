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
    window.token='mobileark%231f620ed7-3804-451d-b249-42f4ef8406e4';
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
            meetDetailForService:"ID-PageMeetDetailForService",
            meetSignList:"ID-PageMeetSignList",
            meetSearch:"ID-PageMeetSearch",
            meetAdd:"ID-PageMeetAdd",
            tree:"ID-PageTree",
            treeSearch:"ID-PageTreeSearch",
            attach:"ID-PageAttach",
        },
        isBack:false,
        ropMethod:{
            attachList:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getpersondocs&sessionId="+window.token,
            downloadAttach:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getdocurl&sessionId="+window.token,
        	meetList:"format=json&v=2.0&appKey=&method=mapps.meeting.meeting.clientquery&sessionId="+window.token,
        	meetAdd:"format=json&v=2.0&appKey=&method=mapps.meeting.meeting.client.add&sessionId="+window.token,
            meetEdit:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.client.edit&sessionId="+window.token,
        	meetDetail:"format=json&v=1.1&appKey=&method=mapps.meeting.meeting.detail&sessionId="+window.token,
        	meetDetailForService:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.detailforservice&sessionId="+window.token,
        	meetCancel:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.cancel&sessionId="+window.token,
        	meetDelete:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.delete&sessionId="+window.token,
        	meetOver:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.over&sessionId="+window.token,
        	signin:"format=json&v=2.0&appKey=&method=mapps.meeting.meeting.signin&sessionId="+window.token,
        	getDepartments:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getdepartments&sessionId="+window.token,
        	getUsers:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getusers&sessionId="+window.token,
        	querySignStatus:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.querysignstatus&sessionId="+window.token
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
        signinSequList:[],
        activeAttachData:[],//选择的附件资料
        mergeMeetingData:{}
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
            app.history.add(hash);
        }
    };
    initHistory();
    
    return app;
});
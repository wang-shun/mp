/**
 * @全局app
 */
define([
    "jquery","underscore","backbone",

    "text!templates/global/nodataTemplate.html",
    "text!templates/global/errorTemplate.html",
    "text!templates/global/meetCancelTemplate.html",
    "text!templates/global/meetWarnTemplate.html",
    "text!templates/global/noMeetTemplate.html",
    "text!templates/global/searchInitTemplate.html",
    "text!templates/global/searchNodataTemplate.html",

    "seedsui","swiper"
],function($, _, Backbone,nodataTemplate,errorTemplate,meetCancelTemplate,meetWarnTemplate,noMeetTemplate,searchInitTemplate,searchNodataTemplate) {
    window.token=escape('mobileark#2cbf6ffd-bc16-42a5-9fff-5045616094cc');
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
            meetQrcode:"ID-PageMeetQrcode",
            meetSignList:"ID-PageMeetSignList",
            meetSearch:"ID-PageMeetSearch",
            meetAdd:"ID-PageMeetAdd",
            check:"ID-PageCheck",
            checkDetail:"ID-PageCheckDetail",
            room:"ID-PageRoom",
            roomDetail:"ID-PageRoomDetail",
            tree:"ID-PageTree",
            treeSearch:"ID-PageTreeSearch",
        },
        isBack:false,
        serviceUrl:window.SERVICE_URL,
        ropMethod:{
        	inituserinfo:"format=json&v=1.0&appKey=&method=mapps.bjyh.client.userinfo",
        	meetList:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.clientquery",
        	meetRoomList:"format=json&v=1.0&appKey=&method=mapps.meetingroom.room.query",
        	meetRoomDetail:"format=json&v=2.0&appKey=&method=mapps.meetingroom.room.detail",
        	meetRoomFavorite:"format=json&v=1.0&appKey=&method=mapps.meetingroom.room.favorite",
        	meetRoomReserved:"format=json&v=2.0&appKey=&method=mapps.meetingroom.reserved.detail",
        	lockReserved:"format=json&v=1.0&appKey=&method=mapps.meeting.reserved.lock",
        	meetAdd:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.client.add",
        	meetRoomApprove:"format=json&v=1.0&appKey=&method=mapps.meetingroom.reserved.queryapprove",
        	meetDetail:"format=json&v=1.1&appKey=&method=mapps.meeting.meeting.detail",
        	meetDetailForService:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.detailforservice",
        	meetCancel:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.cancel",
        	meetDelete:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.delete",
        	meetOver:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.over",
        	updateApprove:"format=json&v=2.0&appKey=&method=mapps.meetingroom.reserved.updateapprove",
        	signin:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.signin",
        	getDepartments:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getdepartments",
        	getUsers:"format=json&v=1.0&appKey=&method=mapps.thirdpart.mobileark.getusers",
        	querySignStatus:"format=json&v=1.0&appKey=&method=mapps.meeting.meeting.querysignstatus",
        	checkCapacity:"format=json&v=1.0&appKey=&method=mapps.meeting.check.capacity"
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
    //会议已取消
    app.meetCancelHTML=_.template(meetCancelTemplate,{imgPath: app.constants.IMAGEPATH});
    //会议未添加议程
    app.meetWarnHTML=_.template(meetWarnTemplate,{imgPath: app.constants.IMAGEPATH});
    //暂无会议安排
    app.noMeetHTML=_.template(noMeetTemplate,{imgPath: app.constants.IMAGEPATH});
    //在这里可以搜索到会议
    app.searchInitHTML=_.template(searchInitTemplate,{imgPath: app.constants.IMAGEPATH});
    //没有找到相关会议
    app.searchNodataHTML=_.template(searchNodataTemplate,{imgPath: app.constants.IMAGEPATH});

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
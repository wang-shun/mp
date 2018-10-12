/**
 * 用于pushState页面路由的骨干路由器
 */
define([
    "app",

    "views/index/indexView",
    "views/meet/meetDetailView",
    "views/meet/meetQrcodeView",
    "views/meet/meetSignListView",
    "views/meet/meetSearchView",
    "views/meet/meetAddView",
    "views/tree/treeView",
    "views/tree/treeSearchView",
    "views/room/checkView",
    "views/room/checkDetailView",
    "views/room/roomView",
    "views/room/roomDetailView",
],function(app, IndexView, MeetDetailView, MeetQrcodeView, MeetSignListView, MeetSearchView, MeetAddView, TreeView, TreeSearchView, CheckView, CheckDetailView, RoomView, RoomDetailView){

    var WebRouter = Backbone.Router.extend({

        initialize: function(){
            this.views = {};
            if(!Backbone.History.started){
                Backbone.history.start();
            }
            app.routerViews = this.views;
        },
        /*routes: {
            "" : "jumpIndex",
            //"detailPage/:roomId":"jumpDetail",
        },*/
        routes:function(){
            var routes={
                "" : "jumpIndexView",
            };
            //会议详情页面
            routes[app.pages.meetDetail+"/:meetingId"]="jumpMeetDetailView";
            //服务人员推送会议详情页面
            routes[app.pages.meetDetailForService+"/:meetingId"]="jumpMeetDetailForServiceView";
            
            //会议扫码页面
            routes[app.pages.meetQrcode]="jumpMeetQrcodeView";
            //会议签到列表页面
            routes[app.pages.meetSignList]="jumpMeetSignListView";
            //会议搜索页面
            routes[app.pages.meetSearch]="jumpMeetSearchView";
            //会议添加页面
            routes[app.pages.meetAdd]="jumpMeetAddView";

            //会议室预定审批
            routes[app.pages.check]="jumpCheckView";
            //会议室预定审批详情
            routes[app.pages.checkDetail]="jumpCheckDetailView";
            routes[app.pages.checkDetail+"/:reservedId"]="jumpCheckDetailView1";
            //会议室首页
            routes[app.pages.room]="jumpRoomView";
            //会议室详情
            routes[app.pages.roomDetail+"/:roomId/:startTime/:endTime"]="jumpRoomDetailView";

            //树结构
            routes[app.pages.tree+"/:selected"]="jumpTreeView";
            routes[app.pages.treeSearch]="jumpTreeSearchView";

            return routes;
        }(),
        jumpIndexView:function(){
            //从添加页面返回
            if(app.isBack==true){
                //从新建会议页面返回
                if(app.history.prevHash=="#"+app.pages.meetAdd){
                    this.views.meetAddView.destroy();
                //从审批页面返回
                }else if(app.history.prevHash=="#"+app.pages.check){
                    this.views.checkView.destroy();
                //从搜索页面返回
                }else if(app.history.prevHash=="#"+app.pages.meetSearch){
                    this.views.meetSearchView.destroy();
                }
                return;
            }
            if(this.views.indexView)return;
            this.views.indexView = new IndexView({ el: $("#"+app.pages.index+"-Section") });
            this.views.indexView.render(this);
        },
        jumpMeetDetailView:function(meetingId){
            if(app.isBack==true)return;
            
            if(!this.views.meetDetailView){
                this.views.meetDetailView = new MeetDetailView({ el: $("#"+app.pages.meetDetail+"-Section") });
                this.views.meetDetailView.render(meetingId);
            }else{
                this.views.meetDetailView.refresh(meetingId);
            }
        },
        jumpMeetDetailForServiceView:function(meetingId){
            if(app.isBack==true)return;
            
            if(!this.views.meetDetailView){
                this.views.meetDetailView = new MeetDetailView({ el: $("#"+app.pages.meetDetailForService+"-Section") });
                this.views.meetDetailView.render(meetingId,'service');
            }else{
                this.views.meetDetailView.refresh(meetingId,'service');
            }
        },
        jumpMeetQrcodeView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.meetQrcodeView){
                this.views.meetQrcodeView = new MeetQrcodeView({ el: $("#"+app.pages.meetQrcode+"-Section") });
                this.views.meetQrcodeView.render();
            }else{
                this.views.meetQrcodeView.refresh();
            }
        },
        jumpMeetSignListView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.meetSignListView){
                this.views.meetSignListView = new MeetSignListView({ el: $("#"+app.pages.meetSignList+"-Section") });
                this.views.meetSignListView.render();
            }else{
                this.views.meetSignListView.refresh();
            }
        },
        jumpMeetSearchView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.meetSearchView){
                this.views.meetSearchView = new MeetSearchView({ el: $("#"+app.pages.meetSearch+"-Section") });
                this.views.meetSearchView.render();
            }else{
                this.views.meetSearchView.refresh();
            }
        },
        jumpMeetAddView:function(){
            if(app.isBack==true){
                //从会议室页面返回
                if(app.history.prevHash=="#"+app.pages.room){
                    this.views.roomView.destroy();
                }
                return;
            }
            //每次进入此页重新渲染
            this.views.meetAddView = new MeetAddView({ el: $("#"+app.pages.meetAdd+"-Section") });
            this.views.meetAddView.render();
        },
        jumpCheckView:function(){
            if(app.isBack==true)return;

            if(!this.views.checkView){
                this.views.checkView = new CheckView({ el: $("#"+app.pages.check+"-Section") });
                this.views.checkView.render();
            }else{
                this.views.checkView.refresh();
            }
        },
        jumpCheckDetailView:function(){
            if(app.isBack==true)return;

            if(!this.views.checkDetailView){
                this.views.checkDetailView = new CheckDetailView({ el: $("#"+app.pages.checkDetail+"-Section") });
                this.views.checkDetailView.render();
            }else{
                this.views.checkDetailView.refresh();
            }
        },
        jumpCheckDetailView1:function(reservedId){
        	app.loading.show();
        	var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomApprove+"&sessionId="+window.token;
            var pageParam = "&offset=1&limit=1&timestamp=0&status=0,1&noPage=1&reservedId="+reservedId;
            //status
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                    if(data.code!=1){//请求错误
                        app.toast.setText("链接失效");
        				app.toast.show();
                        setTimeout(function(){
                        	mplus.closeWindow();
                        }, 1000);
                        return;
                    }
                    for(var i=0,room;room=data.reserved[i++];){
        				if (room.reservedId==reservedId) {
        					app.approvalDetail=room;

                            if(app.isBack==true)return;

                            if(!self.views.checkDetailView){
                            	self.views.checkDetailView = new CheckDetailView({ el: $("#"+app.pages.checkDetail+"-Section") });
                            	self.views.checkDetailView.render(1);
                            }else{
                            	self.views.checkDetailView.refresh(1);
                            }
                            return;
        				}
        			}
                    app.toast.setText("链接失效");
    				app.toast.show();
                    setTimeout(function(){
                    	mplus.closeWindow();
                    }, 1000);
                },
                error:function(msg){
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        jumpRoomView:function(){
            if(app.isBack==true){
                var hash=app.history.prevHash;
                var page=hash.indexOf("/")>=0?hash.substring(0,hash.indexOf("/")):hash;
                //从会议室详情页面返回
                if(page==="#"+app.pages.roomDetail){
                    this.views.roomDetailView.destroy();
                }
                return;
            }
            
            if(!this.views.roomView){
                this.views.roomView = new RoomView({ el: $("#"+app.pages.room+"-Section") });
                this.views.roomView.render();
            }else{
                this.views.roomView.refresh();
            }
        },
        jumpRoomDetailView:function(roomId,startTime,endTime){
            if(app.isBack==true)return;

            if(!this.views.roomDetailView){
                this.views.roomDetailView = new RoomDetailView({ el: $("#"+app.pages.roomDetail+"-Section") });
                this.views.roomDetailView.render(roomId,startTime,endTime);
            }else{
                this.views.roomDetailView.refresh(roomId,startTime,endTime);
            }
        },
        jumpTreeView:function(selected){
            if(app.isBack==true){
                //从会议室页面返回
                if(app.history.prevHash=="#"+app.pages.treeSearch){
                    this.views.treeSearchView.destroy();
                }
                return;
            }
            
            if(!this.views.treeView){
                this.views.treeView = new TreeView({ el: $("#"+app.pages.tree+"-Section") });
                this.views.treeView.render(selected);
            }else{
                this.views.treeView.refresh(selected);
            }
        },
        jumpTreeSearchView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.treeSearchView){
                this.views.treeSearchView = new TreeSearchView({ el: $("#"+app.pages.treeSearch+"-Section") });
                this.views.treeSearchView.render();
            }else{
                this.views.treeSearchView.refresh();
            }
        },
    });
    return WebRouter;

});
/**
 * 用于pushState页面路由的骨干路由器
 */
define([
    "app",

    "views/index/indexView",
    "views/meet/meetDetailView",
    "views/meet/meetSignListView",
    "views/meet/meetSearchView",
    "views/meet/meetAddView",
    "views/tree/treeView",
    "views/tree/treeSearchView",
    "views/attach/attachView",
],function(app, IndexView, MeetDetailView, MeetSignListView, MeetSearchView, MeetAddView, TreeView, TreeSearchView, AttachView, CheckView, CheckDetailView, RoomView, RoomDetailView){

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
            
            //会议签到列表页面
            routes[app.pages.meetSignList]="jumpMeetSignListView";
            //会议搜索页面
            routes[app.pages.meetSearch]="jumpMeetSearchView";
            //会议添加页面
            routes[app.pages.meetAdd+"/:mergedata"]="jumpMeetAddView";

            /*//会议室预定审批
            routes[app.pages.check]="jumpCheckView";
            //会议室预定审批详情
            routes[app.pages.checkDetail]="jumpCheckDetailView";
            routes[app.pages.checkDetail+"/:reservedId"]="jumpCheckDetailView1";
            //会议室首页
            routes[app.pages.room]="jumpRoomView";
            //会议室详情
            routes[app.pages.roomDetail+"/:roomId/:startTime/:endTime"]="jumpRoomDetailView";*/

            //树结构
            routes[app.pages.tree+"/:selected"]="jumpTreeView";
            routes[app.pages.treeSearch]="jumpTreeSearchView";

            //附件资料
            routes[app.pages.attach]="jumpAttachView";

            return routes;
        }(),
        jumpIndexView:function(){
            //从添加页面返回
            if(app.isBack==true){
                //从新建会议页面返回
                if(app.history.prevHash.indexOf("#"+app.pages.meetAdd+"/")>=0){
                    this.views.meetAddView.destroy();
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
            //从添加页面返回
            if(app.isBack==true){
                //从新建会议页面返回
                if(app.history.prevHash.indexOf("#"+app.pages.meetAdd+"/")>=0){
                    this.views.meetAddView.destroy();
                }
                return;
            }
            
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
        jumpMeetAddView:function(mergeData){
            if(app.isBack==true)return;
            //每次进入此页重新渲染
            this.views.meetAddView = new MeetAddView({ el: $("#"+app.pages.meetAdd+"-Section") });
            this.views.meetAddView.render(mergeData);
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
        jumpAttachView:function(){
            if(app.isBack==true)return;

            if(!this.views.attachView){
                this.views.attachView = new AttachView({ el: $("#"+app.pages.attach+"-Section") });
                this.views.attachView.render();
            }else{
                this.views.attachView.refresh();
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
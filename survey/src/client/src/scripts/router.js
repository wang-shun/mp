/**
 * 用于pushState页面路由的骨干路由器
 */
define([
    "app",
    "views/index/indexView",
    "views/meet/meetDetailView",
    "views/meet/statDetailView"
],function(app, IndexView, MeetDetailView, StatDetailView){

    var WebRouter = Backbone.Router.extend({

        initialize: function(){
            this.views = {};
            if(!Backbone.History.started){
                Backbone.history.start();
            }
            app.routerViews = this.views;
        },
        routes:function(){
            var routes={
                "" : "jumpIndexView",
            };
            //会议详情页面
            routes[app.pages.meetDetail+"/:meetingId"]="jumpMeetDetailView";
            routes[app.pages.statDetail+"/:meetingId"]="jumpStatDetailView";
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
        jumpStatDetailView:function(meetingId){
        	if(app.isBack==true)return;
            
            if(!this.views.statDetailView){
                this.views.statDetailView = new StatDetailView({ el: $("#"+app.pages.statDetail+"-Section") });
                this.views.statDetailView.render(meetingId);
            }else{
                this.views.statDetailView.refresh(meetingId);
            }
        }
    });
    return WebRouter;

});
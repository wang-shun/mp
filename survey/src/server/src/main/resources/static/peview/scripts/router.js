/**
 * 用于pushState页面路由的骨干路由器
 */
define([
    "app",
    "views/index/indexView",
    "views/meet/meetDetailView",
    "views/client/meetDetailView",
    "views/add/meetDetailView"
],function(app, IndexView, MeetDetailView, ClientDetailView, AddDetailView){

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
            routes[app.pages.clientDetail+"/:meetingId"]="jumpClientDetailView";
            routes[app.pages.addDetail+"/:meetingId"]="jumpAddDetailView";
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
        	if(this.views.clientDetailView!=undefined && this.views.clientDetailView!=null){
	        	this.views.clientDetailView.destroy();
	            this.views.clientDetailView = null;
        	}
            if(!this.views.meetDetailView){
                this.views.meetDetailView = new MeetDetailView({ el: $("#"+app.pages.meetDetail+"-Section") });
                this.views.meetDetailView.render(meetingId);
            }else{
                this.views.meetDetailView.refresh(meetingId);
            }
        },
        jumpClientDetailView:function(meetingId){
            //从添加页面返回
        	if(this.views.meetDetailView!=undefined && this.views.meetDetailView!=null){
	        	this.views.meetDetailView.destroy();
	            this.views.meetDetailView = null;
        	}
            
            if(!this.views.clientDetailView){
                this.views.clientDetailView = new ClientDetailView({ el: $("#"+app.pages.clientDetail+"-Section") });
                this.views.clientDetailView.render(meetingId);
            }else{
                this.views.clientDetailView.refresh(meetingId);
            }
        },
        jumpAddDetailView:function(meetingId){
            //从添加页面返回
            if(!this.views.addDetailView){
                this.views.addDetailView = new AddDetailView({ el: $("#"+app.pages.addDetail+"-Section") });
                this.views.addDetailView.render(meetingId);
            }else{
                this.views.addDetailView.refresh(meetingId);
            }
        }
    });
    return WebRouter;

});
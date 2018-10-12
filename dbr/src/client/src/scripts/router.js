/**
 * 用于pushState页面路由的骨干路由器
 */
define([
    "app",

    "views/index/indexView",
    "views/meet/meetSearchView",
    "views/room/checkView",
],function(app, IndexView, MeetSearchView, CheckView){

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
            
            //会议搜索页面
            routes[app.pages.meetSearch]="jumpMeetSearchView";

            //会议室预定审批
            routes[app.pages.check]="jumpCheckView";


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
        jumpMeetSearchView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.meetSearchView){
                this.views.meetSearchView = new MeetSearchView({ el: $("#"+app.pages.meetSearch+"-Section") });
                this.views.meetSearchView.render();
            }else{
                this.views.meetSearchView.refresh();
            }
        },
        jumpCheckView:function(){
            if(app.isBack==true){
                //从会议室页面返回
                if(app.history.prevHash=="#"+app.pages.meetSearch){
                    this.views.meetSearchView.destroy();
                }
                return;
            }

            if(!this.views.checkView){
                this.views.checkView = new CheckView({ el: $("#"+app.pages.check+"-Section") });
                this.views.checkView.render();
            }else{
                this.views.checkView.refresh();
            }
        },
    });
    return WebRouter;

});
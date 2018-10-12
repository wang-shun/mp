/**
 * 用于pushState页面路由的骨干路由器
 */
define([
    "app",

    "views/indexView",
    "views/searchView",
    "views/searchHistoryView",
    "views/addView",
    "views/detailView",
    "views/enterView",
    "views/emailView",
    "views/userView",
    "views/tree/treeView",
    "views/tree/treeSearchView",
],function(app, IndexView, SearchView, SearchHistoryView, AddView, DetailView,EnterView,EmailView,UserView, TreeView, TreeSearchView){

    var WebRouter = Backbone.Router.extend({

        initialize: function(){
            this.views = {};
            if(!Backbone.History.started){
                Backbone.history.start();
            }
            app.routerViews = this.views;
            this.getWorkCircleId();
        },
        getWorkCircleId:function(){
        	var url = app.serviceUrl;
            var ropParam = app.ropMethod.shareCircle+"&sessionId="+window.token+"&actId=-1";
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam,
                success:function(data){
                	if (data.phone != "") 
                		app.phone = data.phone;
                	if (data.imFlag === true) 
                		app.imFlag = data.imFlag;
                	if (data.code == '1' && data.id != "") {
                		app.workCircleFlag = true;
    			    	return;
                	}
                },
                error:function(msg){
                },
                complete:function(){
                }
            });
			return;
        },
        /*routes: {
            "" : "jumpIndex",
            //"detailPage/:roomId":"jumpDetail",
        },*/
        routes:function(){
            var routes={
                "" : "jumpIndexView",
            };
            //搜索页面
            routes[app.pages.search]="jumpSearchView";
            routes[app.pages.searchHistory]="jumpSearchHistoryView";
            //添加页面
            routes[app.pages.add]="jumpAddView";
            //会议室详情
            routes[app.pages.detail+"/:id"]="jumpDetailView";
            routes[app.pages.enter+"/:id"]="jumpEnterView";
            routes[app.pages.email+"/:id"]="jumpEmailView";
            routes[app.pages.user+"/:id"]="jumpUserView";

            //树结构
            routes[app.pages.tree+"/:selected"]="jumpTreeView";
            routes[app.pages.treeSearch]="jumpTreeSearchView";

            return routes;
        }(),
        jumpIndexView:function(){
            //从添加页面返回
            if(app.isBack==true){
                //从审批页面返回
                if(app.history.prevHash=="#"+app.pages.check){
                    this.views.checkView.destroy();
                //从搜索页面返回
                }else if(app.history.prevHash=="#"+app.pages.search){
                    this.views.searchView.destroy();
                }
                return;
            }
            if(this.views.indexView)return;
            this.views.indexView = new IndexView({ el: $("#"+app.pages.index+"-Section") });
            this.views.indexView.render(this);
        },
        jumpSearchView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.searchView){
                this.views.searchView = new SearchView({ el: $("#"+app.pages.search+"-Section") });
                this.views.searchView.render();
            }else{
                this.views.searchView.refresh();
            }
        },
        jumpSearchHistoryView:function(){
            if(app.isBack==true)return;
            
            if(!this.views.searchHistoryView){
                this.views.searchHistoryView = new SearchHistoryView({ el: $("#"+app.pages.searchHistory+"-Section") });
                this.views.searchHistoryView.render();
            }else{
                this.views.searchHistoryView.refresh();
            }
        },
        jumpAddView:function(){
            if(app.isBack==true){
                //从会议室页面返回
                if(app.history.prevHash=="#"+app.pages.room){
                    this.views.roomView.destroy();
                }
                return;
            }
            this.views.meetAddView = new AddView({ el: $("#"+app.pages.add+"-Section") });
            this.views.meetAddView.render();
        },
        jumpDetailView:function(id){
            if(app.isBack==true)return;

            if(!this.views.detailView){
                this.views.detailView = new DetailView({ el: $("#"+app.pages.detail+"-Section") });
                this.views.detailView.render(id);
            }else{
                this.views.detailView.refresh(id);
            }
        },
        jumpEnterView:function(id){
            if(app.isBack==true)return;

            if(!this.views.enterView){
                this.views.enterView = new EnterView({ el: $("#"+app.pages.enter+"-Section") });
                this.views.enterView.render(id);
            }else{
                this.views.enterView.refresh(id);
            }
        },
        jumpEmailView:function(id){
            if(app.isBack==true)return;

            if(!this.views.emailView){
                this.views.emailView = new EmailView({ el: $("#"+app.pages.email+"-Section") });
                this.views.emailView.render(id);
            }else{
                this.views.emailView.refresh(id);
            }
        },
        jumpUserView:function(id){
            if(app.isBack==true)return;

            if(!this.views.userView){
                this.views.userView = new UserView({ el: $("#"+app.pages.user+"-Section") });
                this.views.userView.render(id);
            }else{
                this.views.userView.refresh(id);
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
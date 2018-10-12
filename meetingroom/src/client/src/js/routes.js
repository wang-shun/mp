define(["backbone", "views/index/index-snippet-view"
		, "views/detail/detail-snippet-view"
		, "views/detail/plandetail-snippet-view"
		, "views/detail/addplan-snippet-view"
		, "views/manager/manager-snippet-view"
		, "views/result/result-snippet-view"],function(Backbone, IndexView, DetailView, PlanDetailView, AddPlanView, ManagerView,ResultView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(app){
			this.views = {};
			this.app = app;
			this.initFlag = true; 
			this.approveFlag = "0";
			this.manageParam="";
			var appContext = this.getAppContext();
			appContext.cashUtil.saveData('servicePath',window.serviceUrl);
			appContext.cashUtil.saveData('ropParam',"format=json&v=1.0&appKey=&sessionId=" + token);
			appContext.cashUtil.saveData('ropParamNoV',"format=json&appKey=&sessionId=" + token);
			this.views.indexView = new IndexView({ el: $(".SID-index-snippet") });
			this.views.indexView.render(this);
			if(!Backbone.History.started){
				Backbone.history.start();
			}
		},
		getAppContext : function() {
			return require("app").getInstance();
		},
	  	routes: {
		    "indexPage": "indexPageJump",
		    "detailPage/:roomId/:reservedDate/:startTime/:endTime": "detailPageJump",
		    "planDetailPage": "planDetailJump",
		    "addPlanPage": "addPlanJump",
		    "managerPage": "managerPageJump",
		    "reservedDetailPage/:reservedId" : "reservedDetailJump"
		},
		reservedDetailJump:function(reservedId){
			$(".SID-rank-one").hide();
			this.views.resultView = new ResultView({ el: $(".SID-planDetail-snippet") });
			this.views.resultView.render(reservedId);
			$(".SID-planDetail-snippet").show();
		},
		initBusinessViews : function(){
			if(this.views.detailView != null){
				this.views.detailView.destroy();
			}
			if(this.views.managerView != null){
				this.views.managerView.destroy();
			}
		},
		goBack:function(){
			if(this.views.detailView != null){
				this.views.detailView.destroy();
			}
			if(this.views.managerView != null){
				this.views.managerView.destroy();
			}
			if(this.views.addPlanView != null){
				this.views.addPlanView.destroy();
			}
			$(".SID-rank-one").hide();
			$(".SID-index-snippet").show();
			history.go(-2);
		},
		indexPageJump:function(){
			$(".SID-rank-one").hide();
			$(".SID-index-snippet").show();
			this.approveFlag = "0";
			this.manageParam = "";
			this.initBusinessViews();
			this.views.indexView.refresh(false);
		},
		planDetailJump:function(){
			this.views.planDetailView = new PlanDetailView({ el: $(".SID-planDetail-snippet") });
			$(".SID-rank-one").hide();
			$(".SID-planDetail-snippet").show();
			this.views.planDetailView.render(this);
		},
		addPlanJump:function(){
			this.initBusinessViews();
			this.views.addPlanView = new AddPlanView({ el: $(".SID-addPlan-snippet") });
			$(".SID-rank-one").hide();
			$(".SID-addPlan-snippet").show();
			this.views.addPlanView.render(this);
		},
		detailPageJump:function(roomId,reservedDate,startTime,endTime){
			this.initBusinessViews();
			if (reservedDate == null) {
				reservedDate = new Date();
			}
			this.views.detailView = new DetailView({ el: $(".SID-detail-snippet") });
			if (startTime == '-1' && endTime == '-1') {
				this.views.detailView.render(roomId,this,reservedDate,"","");
			} else {
				this.views.detailView.render(roomId,this,reservedDate,startTime,endTime);
			}
			$(".SID-index-snippet").hide();
			$(".SID-planDetail-snippet").hide();
			$(".SID-addPlan-snippet").hide();
		},
		managerPageJump:function(){
			this.initBusinessViews();
			$(".SID-planDetail-snippet").hide();
			$(".SID-index-snippet").hide();
			$(".SID-manager-snippet").show();
			var adminFlag = 0;
			if (this.views.indexView && this.views.indexView.adminFlag && this.views.indexView.adminFlag == 1) {
				adminFlag = 1;
			}
			this.views.managerView = new ManagerView({ el: $(".SID-manager-snippet") });
			this.views.managerView.render(this,adminFlag);
		}
	});
	return BaseRouter;
})
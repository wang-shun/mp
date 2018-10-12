define(["backbone"
        , "views/detail/detail-snippet-view"]
	,function(Backbone,DetailView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(app){
			this.views = {};
			this.app = app;
			this.initFlag = true; 
			var appContext = this.getAppContext();
			appContext.cashUtil.saveData('ropParam',"format=json&v=1.0&appKey=");
			appContext.cashUtil.saveData('ropParamV11',"format=json&v=1.1&appKey=");
			if(!Backbone.History.started){
				Backbone.history.start();
			}
		},
		getAppContext : function() {
			return require("app").getInstance();
		},
	  	routes: {
			"external/:participantsId/:url":"detailPageJump",
		},
		initBusinessViews : function(){
			if (this.views.detailMeetingView!=null){
				this.views.detailMeetingView.destroy();
			}
		},
		removeDetailView:function(){
			delete this.views.detailMeetingView;
		},
		detailPageJump:function(participantsId,url){
			var appContext = this.getAppContext();
			appContext.cashUtil.saveData('servicePath',url+"/api");
			$(".SID-meeting-snippet").hide();
			$(".SID-detail-snippet").show();
			$("#ID-PageDetail").show();
			$("#ID-List").hide();
			$("#ID-Sign").hide();
			if (this.views.detailMeetingView == null) {
				this.views.detailMeetingView = new DetailView({ el: $(".SID-detail-snippet") });
				this.views.detailMeetingView.render(this,participantsId);
			}
		}
	});
	return BaseRouter;
})
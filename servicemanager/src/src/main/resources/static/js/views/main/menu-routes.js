define(["backbone",'views/meeting/manage-snippet-view','views/meeting/monitor-snippet-view','views/meeting/index-snippet-view','views/meeting/trace-snippet-view'],function(Backbone, ManageView, MonitorView, IndexView, TraceView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(){
			this.views = {};
		},
	  	routes: {
		  "menu": "menuJump"
		},
		menuJump: function(index) {
			this._initBusinessViews();
			if($(".SID-main-snippent").attr("data-indexid") == "main"){
				this.views.ManageView = new ManageView({el : $(".SID-manage-snippent")});
			}else if($(".SID-main-snippent").attr("data-indexid") == "monitor"){
				this.views.MonitorView = new MonitorView({el : $(".SID-monitor-snippent")});
			}else if($(".SID-main-snippent").attr("data-indexid") == "index"){
				this.views.IndexView = new IndexView({el : $(".SID-index-snippent")});
			}else if($(".SID-main-snippent").attr("data-indexid") == "trace"){
				this.views.TraceView = new TraceView({el : $(".SID-trace-snippent")});
			}
			$($(".SID-menu-snippet").find('.SID-menu').get(0)).click();
			this._renderBusinessViews();
		},
		/**
		 * init business views
		 */
		_initBusinessViews : function() {
			$.each(this.views, function(index, view) {
				view.destroy();
			});
			this.views = {};
		},
		/**
		 * Render the business views
		 */
		_renderBusinessViews : function() {
			$.each(this.views, function(index, view) {
				view.render();
			});
		}
	});
	return BaseRouter;
})
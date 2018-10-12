define(["backbone",'views/meeting/manage-snippet-view','views/meeting/monitor-snippet-view'],function(Backbone, ManageView, MonitorView){
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
define(["jquery", "backbone", "util/eventhub-backbone", "",'views/main/main-snippent-view'], 
	function($, Backbone, eventHub, LoginRouterSnippentView, MainSnippent) {
		var AppViews = function(context){
			this.context = context;
			this.context.views = {};
			initViews.call(this);
			renderViews.call(this);
		};
		
		AppViews.prototype = {
			subscribeEvents: function(){
				var self = this;
				// subscribe APPLICATION_START event.
				this.context.subscribeEvent(this.context.constants.EVENTS.PAGE_LOADED, function(){
				});

				// subscribe APPLICATION_START event.
				this.context.subscribeEvent(this.context.constants.EVENTS.APPLICATION_START, function(){
				});

				this.context.subscribeEvent(this.context.constants.EVENTS.RANDER_MAIN_PAGE, function(data){
					if (self.context.views.mainSnippent != null) {
						self.context.views.mainSnippent.destory();
					}
					self.context.views.mainSnippent = new MainSnippent({el:$(".SID-main-snippent")});
					self.context.views.mainSnippent.render(data);
				});				

			}
		};
		/**
		 * init the backbone views
		 */
		function initViews(){
			this.context.views.mainSnippent = new MainSnippent({el:$(".SID-main-snippent")});
			
			this.subscribeEvents();
		}		
		/**
		 * Render the backbone views
		 */
		function renderViews(){
			this.context.views.mainSnippent.render();
		}
		
		return AppViews;
	}
);
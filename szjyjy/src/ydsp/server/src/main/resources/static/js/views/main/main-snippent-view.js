define([ 'jquery', 'views/communication-base-view', 'text!../../templates/main/mainTemplate.html',
		'views/main/header-snippent-view', 'views/main/menu-snippent-view',
		'views/main/main-conatiner-snippent-view' ],
		function($, CommunicationBaseView, MainTemplate,
				HeaderSnippentView, MenuSnippetView,
				MainContainerSnippent) {
			var MainSnippent = CommunicationBaseView.extend({
				events : {
					'click .SI-search-toggle' : 'searchToggleDisplay'
				},
				initialize : function() {
					this.preRender();
					this.views = {};
				},
				render : function() {
					var appContext = this.getAppContext();
					var str=location.href;
					var sessionId = str.substring(str.indexOf("=")+1);
					appContext.cashUtil.saveData('servicePath',"api");
					appContext.cashUtil.saveData('ropParam','format=json&v=1.0&appKey=&sessionId='+sessionId);
					appContext.cashUtil.saveData('ropParamNoV','format=json&appKey=');
					appContext.cashUtil.saveData('ropFileParam','format=bin&v=1.0&appKey=');
					this.setUpContent();
					return this;
				},
				refresh : function() {
				},
				destroy : function() {
					this.undelegateEvents();
					this.unbind();
				},
				setUpContent : function() {
					
					var self = this;
					/**var url = this.constants.AJAX.PATH+"/login!getSignInInfo.action";
					this.showLoading();
					$.ajax({
						type : "POST",
						url : url,
						async : false,
						success : function(data) {
							self.hideLoading();
							var data_t = data;
							var imgPath = self.constants.IMAGEPATH;
							var html = _.template(
									MainTemplate, {
										'data' : data_t,
										'imgPath' : imgPath,
									});
							self.$el.append(html);					
							self.initBusinessViews();
						},
						error : function() {
							self.hideLoading();
							fh.alert("获取登录信息失败！");
						}
					});***/
					var imgPath = self.constants.IMAGEPATH;
					var html = _.template(
							MainTemplate, {
								imgPath : imgPath
							});
					self.$el.append(html);	
					self.initBusinessViews();
				},
				subscribeEvents : function() {
					var self = this;
				},
				/**
				 * init business views
				 */
				initBusinessViews : function() {
					$.each(this.views, function(index, view) {
						view.destroy();
					});
					this.views = {};
					this.views.menuSnippetView = new MenuSnippetView({el:$(".SID-menu-snippet")});
					this.views.headerSnippentView = new HeaderSnippentView({el:$(".SID-header-snippent")});
					//this.views.navbarSnippentView = new NavbarSnippentView({el:$(".SID-nav-bar-snippet")});
					this.views.mainContainerSnippent = new MainContainerSnippent({el:$(".SID-main-container-snippet")});
					this.renderBusinessViews();
				},
				/**
				 * Render the business views
				 */
				renderBusinessViews : function() {
					$.each(this.views, function(index, view) {
						view.render();
					});
				},
				
				clearBusinessViews : function() {
					$.each(this.views, function(index, view) {
						view.destroy();
					});
					this.views = {};
				},

				searchToggleDisplay : function(Event){
					if($(Event.currentTarget).find("span.fhicon-arrowD2").length==1){
						this.$el.find(".search-toggle span:eq(1)").text("关闭查询条件");
						this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
						
						this.$el.find(".search-modle").removeClass("hide");
					}else{
						this.$el.find(".search-toggle span:eq(1)").text("展开查询条件");
						this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
						
						this.$el.find(".search-modle").addClass("hide");
					}
				}
				
			});

			return MainSnippent;
		});
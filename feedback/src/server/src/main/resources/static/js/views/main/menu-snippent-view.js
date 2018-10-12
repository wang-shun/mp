define(['jquery', 'views/communication-base-view', ''], function($,CommunicationBaseView, TemplateMenu) {
	var MenuSnippetView = CommunicationBaseView.extend({
		events: {
			'click .SID-menu' : 'menuClickHandler'
		},
		initialize: function() {
			this.preRender();
		},
		render: function() {
			this.setUpContent();
			return this;
		},
		refresh: function() {},
		destroy: function() {
			this.undelegateEvents();
			this.unbind();
			this.$el.empty();
		},
		defauleMenu : function(){
			this.$el.find('.SID-menu').first().click();
		},
		menuGroupClickHandler :function(target){
		},
		menuClickHandler :function(target){
			this.$el.find('.SID-menu').parent().removeClass('active');
			$(target.currentTarget).parent().addClass('active');
			var menuName = $(target.currentTarget).attr('data-name');
			var msg = {
					menuName : menuName
			};
			this.eventHub.publishEvent('MENU_CLICKL', msg);	
			return false;
		},
		setUpContent: function() {
			var self = this;
			var appContext = self.getAppContext();
			var servicePath = appContext.cashUtil.getData('servicePath');
			var ropParam = appContext.cashUtil.getData('ropParam');
			$.ajax({
				type:"POST",
				url:servicePath+"?"+ropParam+ "&method=mapps.feedback.isadmin",
				success:function(ajax){
					if(ajax.code == "1"){
						if (ajax.adminFlag) {
							var html = '<li class="nav-category-list"><a class="SID-menu" data-name="会议室维护"><span class="line"></span><span class="fhicon-meeting-set"></span>会议室维护</a></li>';
							self.$el.append(html);
							self.defauleMenu();
							$(".SID-welcome").hide();
						} else {
							var html = '';	
							self.$el.append(html);
							//self.defauleMenu();
							$(".SID-welcome").hide();
						}
					}
				},
				error:function(){
					fh.alert("权限失败！");
				}
			});
		}
	});

	return MenuSnippetView;
});
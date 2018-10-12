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
			var html = '';
			html += '<li class="nav-category-list"><a class="SID-menu" data-name="我的问卷"><span class="line"></span><span class="questionnaire-wenjuan"></span>我的问卷</a></li>';
			html += '<li class="nav-category-list"><a class="SID-menu" data-name="问卷列表"><span class="line"></span><span class="fhicon-rule"></span>问卷列表</a></li>';
			html += '<li class="nav-category-list"><a class="SID-menu" data-name="问卷模板"><span class="line"></span><span class="fhicon-namelist"></span>问卷模板</a></li>';
			self.$el.append(html);
			self.defauleMenu();
			$(".SID-welcome").hide();
		}
	});

	return MenuSnippetView;
});
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
			var html = '<li class="nav-category-list"><a class="SID-menu" data-name="现场查验"><span class="line"></span><span class="fhicon-meeting-statistics"></span>现场查验</a></li>';	

			this.$el.append(html);
			this.defauleMenu();
		}
	});

	return MenuSnippetView;
});
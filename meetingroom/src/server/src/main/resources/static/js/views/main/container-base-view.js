define(['jquery', 'views/communication-base-view', 'text!../templates/baseContainerTemplate.html'], 
	function($, CommunicationBaseView, TemplateBaseContainer) {
	var ContainerBaseView = CommunicationBaseView.extend({
		viewType: 'ContainerBaseView',
		preRender: function() {
			this.subscribeEvents();
			this.initContetnt();
		},
		initContetnt : function(){
			var html = _.template(TemplateBaseContainer, {});
			this.$el.append(html);
			var nav = this.attributes.parentMenu.menuName + '&gt;' + this.attributes.currentMenu.menuName;
			this.$el.find('.SID-nav-bar').html(nav);
		},
	});
	return ContainerBaseView;
});
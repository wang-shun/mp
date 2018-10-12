define(['jquery', 'views/base-view', 'util/eventhub-backbone'], function($, BaseView, EventHub) {
	var CommunicationBaseView = BaseView.extend({
		viewType: 'CommunicationBaseView',
		eventHub: EventHub,
		subscribeEvents: function() {
			return this;
		},
		preRender: function() {
			this.subscribeEvents();
		}
	});
	return CommunicationBaseView;
});
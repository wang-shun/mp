define(["backbone", "underscore"], 
	function(Backbone, _){
		var EventHub = function() {
			this._eventHub = _.extend({}, Backbone.Events);
		};
	
		EventHub.prototype.publishEvent = function(subject, data) {
			this._eventHub.trigger(subject, data);
		};
	
		EventHub.prototype.subscribeEvent = function(subject, callback, context){
			this._eventHub.on(subject, callback, context);
			return {
				subject: subject,
				callback: callback,
				context: context
			};
		};
	
		EventHub.prototype.unsubscribeEvent = function(subscription) {
			subscription = subscription || {};
			this._eventHub.off(subscription.subject, subscription.callback, subscription.context);
		};
		
		EventHub.prototype.unsubscribeEventByName = function(subject) {
			this._eventHub.off(subject);
		};
		
		var _instance = null;
		EventHub.getInstance = function(){
			if(_instance == null){
				_instance = new EventHub();
			}
			
			return _instance;
		};
	
		return EventHub.getInstance();
});
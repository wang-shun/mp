define(["backbone", "underscore"], 
	function(Backbone, _){
		var CashUtil = function() {
			this._cashData = {};
		};
	
		CashUtil.prototype.saveData = function(key, data) {
			this._cashData[key] = data;
		};
		CashUtil.prototype.getData = function(key, data) {
			return this._cashData[key];
		};
		var _instance = null;
		CashUtil.getInstance = function(){
			if(_instance == null){
				_instance = new CashUtil();
			}
			
			return _instance;
		};
	
		return CashUtil.getInstance();
});
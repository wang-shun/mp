define(["spin", "underscore", "jqueryUi"], 
	function(Spinner, _){
		var _uiRanderUtilInstances = {};
		var UiRanderUtil = {};
		UiRanderUtil.randerIcheckbox = function(view){
			
			view.$el.find('.skin-minimal input').iCheck({
				checkboxClass: 'icheckbox_minimal-blue',
				radioClass: 'iradio_minimal-blue',
				increaseArea: '20%'
			});
		};
		UiRanderUtil.randerJQueryUI_Sortable = function(view){
			view.$el.find(".rankUl").sortable();
			view.$el.find(".rankUl").disableSelection(); 
		};	
		UiRanderUtil._getDate = function(dateFormat, element) {
			var date;
			try {
				date = $.datepicker.parseDate(dateFormat, element.value);
			} catch (error) {
				date = null;
			}

			return date;
		};
		UiRanderUtil._setDate = function(Sid,date) {
			$(Sid).datepicker( "setDate", date );
		};
		UiRanderUtil._setDateByType = function(fromSid,toSid,type) {
			var currentDate = new Date();
			var week = currentDate.getDay();
			var month = currentDate.getDate();
			var millisecond = 1000 * 60 * 60 * 24;
			var minusDay = week != 0 ? week - 1 : 6;
			var monday = new Date(currentDate.getTime() - (minusDay * millisecond));
			var firstDayOfMonth = new Date(currentDate.getTime() - ((month-1) * millisecond));
			//var sunday = new Date(monday.getTime() + (6 * millisecond));
			if (type == "day") {
				$(fromSid).datepicker( "setDate", currentDate );
			} else if (type == "week") {
				$(fromSid).datepicker( "setDate", monday );
			} else if (type == "month") {
				$(fromSid).datepicker( "setDate", firstDayOfMonth );
			}
			$(toSid).datepicker( "setDate", currentDate );
		};
		UiRanderUtil.randerJQueryUI_DateRange = function(view, fromSid, toSid,dateFormat) {
			var self = this;
			var from = $(fromSid).datepicker({});
			var to = $(toSid).datepicker({});
			from.on("change", function() {
					//to.datepicker("option", "minDate", self._getDate(dateFormat, this));
			});
				
			to.on("change", function() {
				//from.datepicker("option", "maxDate", self._getDate(dateFormat, this));
			});
		};
		return UiRanderUtil;
});
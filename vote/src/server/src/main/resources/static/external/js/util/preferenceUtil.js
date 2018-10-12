define(["jquery","util/constants"],function($,constants){
	var PreferenceUtil = {};
	var that = this;
	PreferenceUtil.customPreferenceLoad = function(key){
  //   	var param = "{\"cKey\":\""+key+"\"}";
		// var url = constants.AJAX.PATH +"/access/globalUtilManage/preferenceLoadOperate.htm";
		// $.ajax({
		// 	url : url,
		// 	data: param,
		// 	async: false,
		// 	success: function(data,resultcode,resultmessage) {
		// 		return data.cValue;
		// 	},
		// 	error : function(resultcode,resultmessage) {
		// 		fh.alert(resultmessage);
		// 	}
		// });
    }
    
	PreferenceUtil.customPreferenceSet = function(key,value){
    }
	
	return PreferenceUtil;
});
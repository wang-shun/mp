define(["jquery","util/constants"],function($,constants){
	var PreferenceUtil = {};
	var that = this;
	PreferenceUtil.customPreferenceLoad = function(key){
    	var param = "{\"cKey\":\""+key+"\"}";
		var url = constants.AJAX.PATH +"/access/globalUtilManage/preferenceLoadOperate.htm";
		var dataJson;
		$.ajax({
			url : url,
			data: param,
			async: false,
			success: function(data,resultcode,resultmessage) {
				dataJson = data.cValue;
			},
			error : function(resultcode,resultmessage) {
				fh.alert(resultmessage);
			}
		});
		
		return dataJson;
    }
    
	PreferenceUtil.customPreferenceSet = function(key,value){
    	var param = "{\"cKey\":\""+key+"\",\"cValue\":\""+value+"\"}";
		var url = constants.AJAX.PATH +"/access/globalUtilManage/preferenceSetOperate.htm";
		var dataJson;
		$.ajax({
			url : url,
			data: param,
			async: false,
			success: function(data,resultcode,resultmessage) {
				dataJson = data.cValue;
			},
			error : function(resultcode,resultmessage) {
				fh.alert(resultmessage);
			}
		});
		
		return dataJson;
    }
	
	PreferenceUtil.querySystemAbility = function(orgUUID,loginId,handsetUUID,userUUID){
    	var param = "{\"orgUUID\":\""+orgUUID+"\",\"loginId\":\""+loginId+"\",\"handsetUUID\":\""+handsetUUID+"\",\"userUUID\":\""+userUUID+"\"}";
		var url = constants.AJAX.PATH +"/access/globalUtilManage/querySystemAbility.htm";
		var dataJson;
		$.ajax({
			url : url,
			data: param,
			async: false,
			success: function(data,resultcode,resultmessage) {
				dataJson = data;
			},
			error : function(resultcode,resultmessage) {
				fh.alert(resultmessage);
			}
		});
		return dataJson;
    }
	
	return PreferenceUtil;
});
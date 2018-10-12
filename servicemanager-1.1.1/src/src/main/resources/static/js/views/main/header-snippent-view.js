define(['jquery', 'views/base-view'], function($, BaseView) {
	var HeaderSnippentView = BaseView.extend({
		events: {
			'click .SID-logout' : 'logoutHandler',
			'click .SID-test' : 'test'
		},
		test : function(){
			var self = this;
			var appContext = self.getAppContext();
			var servicePath = appContext.cashUtil.getData('servicePath');
			var ropParam = appContext.cashUtil.getData('ropParam');
			$.ajax({
				type:"POST",
				url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.redis.testByList&redisId=456',//&appId=1&resCode=datasource&resId=1&config=[{"key":"type","value":"com.zaxxer.hikari.HikariDataSource"},{"key":"url","value":"jdbc:postgresql://192.168.160.103:5432/mr_test"},{"key":"username","value":"mr"},{"key":"password","value":"123456"}]',
				success:function(ajax){
					if(ajax.code == "1"){
						fh.alert("test成功！",false,null);
					}
					else{
						fh.alert("test失败！",false,null);
					}
				},
				error:function(){
					fh.alert("发布会议失败！");
				}
			});
		},
		initialize: function() {
			this.preRender();
		},
		logoutHandler: function(){
			var self = this;
			fh.confirm('您确定退出吗？',function(){
				var url = "logSessionOut.action?loginType="+self.constants.LOGIN_TYPE
				window.top.location = url;
			},false);
		},
		render: function() {
			this.setUpHeader();
			return this;
		},
		refresh: function() {},
		destroy: function() {
			this.undelegateEvents();
			this.unbind();
			//this.$el.empty();
		},
		setUpHeader: function() {
		//	this.$el.find('.SID-hellow-word').append("&nbsp;&nbsp;").append(this.getHellowWord());
		},
		getHellowWord: function() {
			var now = new Date();
			var hour = now.getHours();
            if(hour < 6){return "凌晨好！";} 
            else if (hour < 9){return "早上好！";} 
            else if (hour < 12){return "上午好！";} 
            else if (hour < 14){return "中午好！";} 
            else if (hour < 17){return "下午好！";} 
            else if (hour < 19){return "傍晚好！";} 
            else if (hour < 22){return "晚上好！";} 
            else {return "夜里好！";}			
		}
	});

	return HeaderSnippentView;
});
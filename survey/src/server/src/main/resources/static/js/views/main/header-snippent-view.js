define(['jquery', 'views/base-view'], function($, BaseView) {
	var HeaderSnippentView = BaseView.extend({
		events: {
			'click .SID-logout' : 'logoutHandler'
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
define([ 'jquery', 'views/communication-base-view','text!../../templates/reserved/leftTemplate.html','text!../../templates/reserved/leftTableTemplate.html'],
		function($, CommunicationBaseView,TemplateLeft,TemplateLeftTable) {
			var LeftsideSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-room-search' : '_onClickSearch',
							'click .SID-td-room' : '_onClickRoom'
						},
						initialize : function() {
							this.parentObj;
						},
						render : function(parentObj) {
							this.parentObj = parentObj;
							this.setContentHTML();
							return this;
						},
						refresh: function() {
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var param = "";
							param = this.$el.find("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.room.query&offset=1&limit=100&timestamp=0" + param;
							$.ajax({
								type : "POST",
								url : url,
								data : "",
								success : function(data) {
									self.$el.find(".SID-roomTable").empty();
									if (data.code == "1") {
										var html = _.template(TemplateLeftTable, {
											'imgPath': self.constants.IMAGEPATH,
											'room' : data.room
										});
										self.$el.find(".SID-roomTable").append(html);
									} else if(data.code == "1020"){
										fh.alert("会话失效，请重新登录！",false,function(){
							        		window.location = window.location;
						    			});
									}else {
										fh.alert(data.code + ":" + data.message);
									}
								},
								error : function(){
									fh.alert("数据获取失败");
								}
							});
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						_onClickSearch : function() {
							this.refresh();
						},
						_onClickRoom : function(e) {
							this.$el.find(".SID-roomTable>tr>td").removeClass("active");
							var target=e.target;
							var roomId = $(target).attr("data-roomId");
							$(target).addClass("active");
							this.parentObj.reloadRightView(roomId);
						},
						setContentHTML : function (){
							var template = _.template(TemplateLeft);
							var html = template({});
							this.$el.append(html);
							this.refresh();
						}
					});
			return LeftsideSnippetView;
		});
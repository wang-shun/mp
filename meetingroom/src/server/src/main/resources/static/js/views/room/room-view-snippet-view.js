define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/room/roomDetailTemplate.html'],
		function($, CommunicationBaseView,Template) {
			var treeObj;
			var datatable;
			var RoomViewSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
						},
						render : function(ajax) {
							this._setContentHTML(ajax);
							return this;
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						_setContentHTML : function (ajax){
							var self = this;
							var template = _.template(Template);
							var equipment = "";
							if(ajax.projector == "1"){
								equipment += "投影、";
							}
							if(ajax.display == "1"){
								equipment += "显示、";
							}
							if(ajax.stereo == "1"){
								equipment += "音响、";
							}
							if(ajax.microphone == "1"){
								equipment += "麦克风、";
							}
							if(ajax.wifi == "1"){
								equipment += "无线网络、";
							}
							if(ajax.projector == "0" && ajax.display == "0" && ajax.stereo == "0"
								&& ajax.microphone == "0" && ajax.wifi == "0"){
								equipment ="无";
							}
							if(ajax.remarks == null || ajax.remarks == ""){
								ajax.remarks = "无";
							}
							if(equipment != "" && equipment !="无")
								ajax.equipment =equipment.substring(0,equipment.length-1);
							var html = template({
								'imgPath' : ajax.imagePath,
								'room' :ajax
							});
							this.$el.append(html);
							var height = document.documentElement.clientHeight/1.2>485 ? 485 : document.documentElement.clientHeight/1.2;
							var commonDialog = fh.commonOpenDialog('roomDetailDialog', "会议室详情", 750, height, this.el);
							if(ajax.imagePath == null)
								$(".SID-img-hide").hide();
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
						}
					});
			return RoomViewSnippetView;
		});
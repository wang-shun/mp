define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/room/userDetailTemplate.html','viewer'],
		function($, CommunicationBaseView,Template) {
			var treeObj;
			var datatable;
			var RoomViewSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.data = {};
							this.parentView;
						},
						render : function(ajax,parentView) {
							this.data = ajax;
							this.parentView = parentView;
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
							console.log(ajax);
							var self = this;
							var template = _.template(Template);
							var html = template({
								'data' :ajax
							});
							this.$el.append(html);
							var commonDialog = fh.commonOpenDialog('roomDetailDialog', "用户详情", 700, 300, this.el);
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
						}
					});
			return RoomViewSnippetView;
		});
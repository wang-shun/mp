define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/law/goodsDetailTemplate.html'],
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
							var	html = template({
								'goodsDetails' :ajax.goodsDetails,
								'containerDetails' :ajax.containerDetails
							});
							
							this.$el.append(html);
							var	commonDialog = fh.commonOpenDialog('DetailDialog', "货物+集装箱信息", 750, 600, this.el);
							commonDialog.addBtn("cannel","关闭",function(){
									self.destroy();
									commonDialog.cancel();
							});
							
							
						},
						
					});
			return RoomViewSnippetView;
		});
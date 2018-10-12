define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/hotel/orderDetailTemplate.html'],
		function($, CommunicationBaseView,Template) {
			var treeObj;
			var datatable;
			var OrderViewSnippetView = CommunicationBaseView
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
							//console.log(ajax);
							var html = template({
								'order' :ajax.data
							});
							this.$el.append(html);
							var height = document.documentElement.clientHeight/1.2>485 ? 485 : document.documentElement.clientHeight/1.2;
							var commonDialog = fh.commonOpenDialog('orderDetailDialog', "订单详情", 750, height, this.el);
							if(ajax.imagePath == null)
								$(".SID-img-hide").hide();
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
						}
					});
			return OrderViewSnippetView;
		});
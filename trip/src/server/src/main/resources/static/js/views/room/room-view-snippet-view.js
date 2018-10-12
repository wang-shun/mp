define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/room/roomDetailTemplate.html','viewer'],
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
							var images = ajax.feedback.images;
							var imageArr = [];
							if (images && images.length > 0) {
								imageArr = images.split(",");
							}
							var self = this;
							var template = _.template(Template);
							var html = template({
								'imgPath' : ajax.imagePath,
								'room' :ajax.feedback,
								'webRoot' : ajax.webRoot,
								'imageArr' : imageArr
							});
							this.$el.append(html);
							var commonDialog = fh.commonOpenDialog('roomDetailDialog', "意见反馈详情", 700, 550, this.el);
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("ok","保存",function(){
								self._sumbitForm(commonDialog);
							});
							self.previewImage();
						},
						previewImage: function() {
			                $('.SID-image').viewer({
			                	url: 'data-original'
			                });
			            },
						_sumbitForm : function(commonDialog){
							var param = "";
							var _self = this;
						    this.validateForm("ID-confirmForm");
						    var appContext = this.getAppContext();
						    var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var msg_success,msg_faild,data;
							data = ropParam + "&method=mapps.feedback.update&feedbackId="+this.data.feedback.id;
							msg_success = "保存成功！";
							msg_faild = "保存失败！";
							param = this.$el.find("#ID-confirmForm").serialize();
				            
				            if (param!="") {
								param = "&"+param;
							}
						    if(this.validateResult()){
						    	$.ajax({
						    		type:"POST",
							    	url:servicePath +"?"+data+param,
							    	success :function(response){
							    		if(response.code == "1"){
							    			fh.alert(msg_success,false,function(){
							    				commonDialog.cancel();
							    				_self.parentView.datatable._fnReDraw();
							    				_self.destroy();
											});
							    		}else{
							    			fh.alert(msg_faild);
							    		}
							    	},
							    	error :function(){
							    		fh.alert(msg_faild);
							    	}
							    });
						    }
						}
					});
			return RoomViewSnippetView;
		});
define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/room/roomAddTemplate.html','views/room/upload-snippet-view'],
		function($, CommunicationBaseView,Template,UploadSnippetView) {
			var treeObj;
			var datatable;
			var RoomAddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .upload-img':'_onClickUpload',
							'click .parentClose':'_closePreview'
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.imagePath = "";
							this.room = {};
						},
						render : function(parentView,room) {
							this.room = room;
							this.parentView = parentView;
							this._setContentHTML(room);
							return this;
						},
						destroy : function() {
							this._destroyBusinessViews();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						_destroyBusinessViews : function() {
							$.each(this.childView, function(index, view) {
								view.destroy();
							});
						},
						refresh : function() {
						},
						_setContentHTML : function (room){
							var self = this;
							var html = _.template(Template, {
								'imgPath': self.constants.IMAGEPATH
							});
							this.$el.append(html);
							var commonDialog;
							var height = document.documentElement.clientHeight/1.2>485?485:document.documentElement.clientHeight/1.2;
							if(room)
								commonDialog=fh.commonOpenDialog('roomAddDialog', "编辑会议室", 750, height, this.el);
							else
								commonDialog=fh.commonOpenDialog('roomAddDialog', "新增会议室", 750, height, this.el)
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("ok","保存",function(){
								self._sumbitForm(commonDialog,room);
							});
							this._initData(room);
						},
						_initData:function(room){
							var self = this;
							if(room){// 修改
								self.$el.find("#roomId").val(room.roomId);
								self.$el.find("#SID-roomName").val(room.roomName);
								self.$el.find("#SID-area").val(room.area);
								self.$el.find("#SID-capacity").val(room.capacity);
								self.$el.find("#SID-address").val(room.address);
								if(room.projector == "1")
									self.$el.find("#SID-projector1").prop("checked",true);
								else
									self.$el.find("#SID-projector1").prop("checked",false);
								if(room.display == "1")
									self.$el.find("#SID-display1").prop("checked",true);
								else
									self.$el.find("#SID-display1").prop("checked",false);
								if(room.microphone == "1")
									self.$el.find("#SID-microphone1").prop("checked",true);
								if(room.stereo == "1")
									self.$el.find("#SID-stereo1").prop("checked",true);
								if(room.wifi == "1")
									self.$el.find("#SID-wifi1").prop("checked",true);
								if(room.remarks == "")
									self.$el.find("#SID-remarks").val("无");
								else
									self.$el.find("#SID-remarks").val(room.remarks);
								if(room.imagePath == null){
									self.$el.find(".img-show").css({"display":"none"});
									self.$el.find(".upload-img").show();
								}else{
									self.$el.find(".img-show").css({"display":"block","background-image":"url("+room.imagePath+")"});
									self.$el.find(".upload-img").hide();
								}
							}else {// 新增
								self.$el.find(".img-show").css({"display":"none"});
								self.$el.find(".upload-img").show();
							}
						},
						_onClickUpload:function(){
							var self = this;
							self.childView.uploadSnippetView = new UploadSnippetView({el:$(".SID-upload")});
							self.childView.uploadSnippetView.render(self,'(支持bmp、jpg、gif、png格式的图片)');
						},
						_closePreview :function(){
							this.$el.find(".img-show").css({"display":"none","background-image":"url()"});
							if(this.room)
								this.room.layout="";
							else
								this.imagePath = "";
							this.$el.find(".upload-img").show();
						},
						_sumbitForm : function(commonDialog,room){
							var param = "";
							var _self = this;
						    this.validateForm("formMsg");
						    var appContext = this.getAppContext();
						    var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var msg_success,msg_faild,data;
							if(room){
								data = ropParam + "&method=mapps.meetingroom.room.edit";
								msg_success = "编辑会议室成功！";
								msg_faild = "编辑会议室失败！";
							}else{
								data = ropParam + "&method=mapps.meetingroom.room.add";
								msg_success = "新建会议室成功！";
								msg_faild = "新建会议室失败！";
							}
							if(room && _self.imagePath == "")
								this.$el.find("#imagePath").val(room.layout);
							else 
								this.$el.find("#imagePath").val(_self.imagePath);
							
							param = $("#formMsg").serialize();
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
							    				_self.parentView.refreshTable();
							    				_self.destroy();
											});
							    		}else if(response.code == "300004"){
							    			fh.alert(response.message);
							    		}else if(response.code == "300005"){
							    			fh.alert(response.message,false,function(){
							    				commonDialog.cancel();
							    				_self.destroy();
							    			});
							    		}else{
							    			fh.alert(response.message);
							    		}
							    	},
							    	error :function(){
							    		fh.alert(msg_faild);
							    	}
							    });
						    }
						}
					});
			return RoomAddSnippetView;
		});
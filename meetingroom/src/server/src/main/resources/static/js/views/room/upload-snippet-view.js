define(
		['jquery','views/communication-base-view','text!../../templates/room/uploadTemplate.html'],
		function($,CommunicationBaseView,uploadTemplate){
			var UploadSnippetView = CommunicationBaseView.extend({
				events : {
					'click #layer-close':'destroy',
					'change .file-upload':'_browseFile',
					'click .upload-cont':'_uploadFile'
				},
				initialize : function(){
					this.parentView = {};
				},
				render : function(parentView,fileType){
					this.parentView = parentView;
					this._setContentHTML(fileType);
					return this;
				},
				_browseFile : function(){
					var fileName = this.$el.find(".file-upload").val();
					var nameSplit = fileName.split("\\");
					this.$el.find(".pop-text").val(nameSplit[nameSplit.length-1]);
				},
				refresh : function() {
				},
				destroy : function() {
					this.undelegateEvents();
					this.unbind();
					this.$el.empty();
				},
				_setContentHTML : function (fileType){
					var self = this;
					var template = _.template(uploadTemplate);
					var html = template({
						'fileType' : fileType,
						'imagePath' : self.constants.IMAGEPATH
					});
					this.$el.append(html);
					this.$el.find(".layer-opt").show();
					this.$el.find(".img-shadow").show();
				},
				_uploadFile:function(){
					var self= this;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
				    var ropParam = appContext.cashUtil.getData('ropParam');
					var importFile = self.$el.find(".file-upload").val();
					if (importFile == null || importFile == undefined || importFile == "") {
						fh.alert("请选择要上传的文件！");
							return;
						}
					if(importFile){
						var url = servicePath + "?" + ropParam + "&method=mapps.fileservice.file.upload";
						$.ajaxFileUpload({
							url: url,
							secureuri: false,
							fileElementId: 'file',
							dataType: 'json',
							success: function(data, resultcode, resultmessage) {
								if(data.code == "1" && resultcode == "success"){
									self.parentView.imagePath = data.path;
									self.destroy();
									self.parentView.$el.find(".img-show").css({"display":"block","background-image":"url("+data.url+")","background-size":"contain","background-repeat":"no-repeat","background-position":"center"});
									self.parentView.$el.find(".upload-img").hide();
								}else{
									fh.alert(data.message,false,function(){
										self.$el.find(".pop-text").val("");
									});
								}
							},
							error: function(data, status, e) {
								fh.alert(data.message);
							}
						});
					}
				}
			});
			return UploadSnippetView;
		});
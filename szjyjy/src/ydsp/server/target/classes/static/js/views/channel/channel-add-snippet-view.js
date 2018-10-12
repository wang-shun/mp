define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/channel/channelAddTemplate.html'],
		function($, CommunicationBaseView,Template) {
			var treeObj;
			var datatable;
			var ChannelAddSnippetView = CommunicationBaseView
					.extend({
						events : {
							
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
						},
						render : function(parentView,hostId) {
							this.parentView = parentView;
							this._setContentHTML(hostId);
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
						_setContentHTML : function (hostId){
							var self = this;
							var html = _.template(Template, {
							});
							this.$el.append(html);
							var commonDialog;
							var height = document.documentElement.clientHeight/1.2>485?485:document.documentElement.clientHeight/1.2;
							if(hostId)
								commonDialog=fh.commonOpenDialog('channelAddDialog', "编辑视频通道", 750, height, this.el);
							else
								commonDialog=fh.commonOpenDialog('channelAddDialog', "新增视频通道", 750, height, this.el)
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("ok","保存",function(){
								self._sumbitForm(commonDialog,hostId);
							});
							this._initData(hostId);
						},
						_initData:function(hostId){
							var self = this;
							if(hostId){// 修改
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.channel.detail&hostId="+hostId,
									success :function(ajax){
										self.$el.find("#SID-channel").val(ajax.channel);
										self.$el.find("#SID-hostId").val(ajax.hostId);
										self.$el.find("#SID-hostPass").val(ajax.hostPass);
										self.$el.find("#SID-hostEmail").val(ajax.hostEmail);
										self.$el.find("#SID-capacity").val(ajax.capacity);
										document.getElementById("SID-hostId").readOnly=true;
										
									}
								});
								
							}else{
								self.$el.find("#SID-hostId").readOnly = "";
							}
						},
						_sumbitForm : function(commonDialog,hostId){
							var param = "";
							var _self = this;
						    this.validateForm("formMsg");
						    var appContext = this.getAppContext();
						    var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var msg_success,msg_faild,data;
							if(hostId){
								data = ropParam + "&method=mapps.channel.edit";
								msg_success = "编辑成功！";
								msg_faild = "编辑失败！";
							}else{
								data = ropParam + "&method=mapps.channel.add";
								msg_success = "新增成功！";
								msg_faild = "新增失败！";
							}
							
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
			return ChannelAddSnippetView;
		});
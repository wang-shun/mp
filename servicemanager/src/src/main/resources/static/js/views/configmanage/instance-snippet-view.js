define(
    [ 'jquery', 'views/communication-base-view',
      'text!../../templates/configmanage/InstanceTemplate.html',
      'text!../../templates/configmanage/InstanceListTemplate.html'],
    function($, CommunicationBaseView,Template,InstanceListTemplate) {
        var InstanceSnippetView = CommunicationBaseView
            .extend({
                events : {
                },
                initialize : function() {
                    this.childView = {};
                    this.parentView = {};
                    this.data={};
                    this.dialog;
                },
                render : function(parentView,data) {
                    this.parentView = parentView;
                    this.data = data;
                    this._setContentHTML();
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
                _setContentHTML : function (){
                    var self = this;
                    var html = _.template(Template, {});
                    this.$el.append(html);
                    var instanceHtml = _.template(InstanceListTemplate, {
						'instanceList': self.data.instanceList,
					});
            		self.$el.find(".SID-instanceBox").html(instanceHtml);
                    var commonDialog;
                    commonDialog =fh.servicemanagerOpenDialog('instanceDialog', this.data.title, 800, 500, this.el);
                    commonDialog.addBtn("ok","确定",function(){
						commonDialog.cancel();
					});
                    this.dialog = commonDialog;
                },
				keyQueryRestartStatus:function(){
					var self = this;
					var beginDate= new Date();
					if(self.timecount==0) {
                        self.timecount = beginDate.getTime();
                    }
					window.setTimeout(function(){
						var appContext = self.getAppContext();
						var servicePath = appContext.cashUtil.getData('servicePath');
						var ropParam = appContext.cashUtil.getData('ropParam');
						$.ajax({
							type:"POST",
							url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.service.restartstatus&appId='+self.data.appId,
							success:function(ajax){
								if(ajax.code == "1"){
								    var currentDate=new Date().getTime();
								    console.log(currentDate-self.timecount);
								    if ((currentDate-self.timecount)>=60000){
								    	var restartstatusBoxList = self.$el.find(".SID-restartstatusBox").find(".restartStatus");
								    	var restartStatusImgList = self.$el.find(".SID-restartstatusBox").find(".restartStatusImg");
										for(var i=0;i<restartstatusBoxList.length;i++){
											if($(restartstatusBoxList[i]).html() == "重启中"){
												$(restartstatusBoxList[i]).css("color","red");
												$(restartstatusBoxList[i]).html("重启失败");
												$(restartstatusBoxList[i]).parent().append('<br/><span style="color:red">服务重启失败,请检查服务运行状态.</span>');
												$(restartStatusImgList[i]).attr("src","images/main/close_on.png");
											}
										}
										self.dialog.addBtn("ok","确定",function(){
	                                        self.destroy();
	                                        self.dialog.cancel();
	                                    });
                                    }else{
                                    	if(ajax.restartStatusList.length > 0){
    										var statusHtml = _.template(restartStatusTemplate, {
    		    								'restartStatusList': ajax.restartStatusList,
    		    							});
    		                        		self.$el.find(".SID-restartstatusBox").html(statusHtml);
    		                        		var allUpFlag = true;
    		                        		for(var i=0;i<ajax.restartStatusList.length;i++){
    		                        			if(ajax.restartStatusList[i].status == "down"){
    		                        				allUpFlag = false;
    		                        			}
    		                        		}
    		                        		if(!allUpFlag){
    		                        			self.keyQueryRestartStatus();
    		                        		}else{
    		                        			self.dialog.addBtn("ok","确定",function(){
    		                                        self.destroy();
    		                                        self.dialog.cancel();
    		                                    });
    		                        		}
    									}else{
    										self.keyQueryRestartStatus();
    									}
                                    }
								}
								else{
									self.destroy();
                                    self.dialog.cancel();
									fh.alert(ajax.message);
								}
							}
						});
					},5000);
				},
            });
        return InstanceSnippetView;
    });

define(
    [ 'jquery', 'views/communication-base-view',
      'text!../../templates/configmanage/restartTemplate.html',
      'text!../../templates/configmanage/restartStatusDefaultTemplate.html',
      'text!../../templates/configmanage/restartStatusTemplate.html',
      'text!../../templates/configmanage/restartStatusDisconnectTemplate.html'],
    function($, CommunicationBaseView,Template,restartStatusDefaultTemplate,restartStatusTemplate,restartStatusDisconnectTemplate) {
        var RestartSnippetView = CommunicationBaseView
            .extend({
                events : {
                },
                initialize : function() {
                    this.childView = {};
                    this.parentView = {};
                    this.data={};
                    //时间计数器
                    this.timecount=0;
                    // 附件id
                    this.dialog;
                    this.recentlyOpenedfileList = [];
                    this.disconnectedList = [];
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
                    self.timecount = new Date().getTime();
                    var html = _.template(Template, {});
                    this.$el.append(html);
                    for(var k=0;k<self.data.restartStatusList.length;k++){
                    	if(self.data.restartStatusList[k].status == "disconnected"){
                    		self.disconnectedList.push(self.data.restartStatusList[k]);
                    		self.data.restartStatusList.splice(k,1);
                    	}
                    }
                    var statusDisconnectHtml = _.template(restartStatusDisconnectTemplate, {
						'restartStatusList': self.disconnectedList,
					});
            		self.$el.find(".SID-disconnectedBox").html(statusDisconnectHtml);
                    var statusHtml = _.template(restartStatusDefaultTemplate, {
						'restartStatusList': self.data.restartStatusList,
					});
            		self.$el.find(".SID-restartstatusBox").html(statusHtml);
                    var commonDialog;
                    commonDialog =fh.servicemanagerOpenDialog('dbAddDialog', this.data.title, 700, 500, this.el);
                    this.dialog = commonDialog;
                    if(self.data.restartStatusList.length > 0){
                    	self.keyQueryRestartStatus();
                    }else{
                    	self.dialog.addBtn("ok","确定",function(){
                            self.destroy();
                            self.dialog.cancel();
                        });
                    }
                },
                reQueryStatus:function(){
                	var self = this;
                	var preFix = isIframe?iframePrefix:"";
                	var restartstatusBoxList = self.$el.find(".SID-restartstatusBox").find(".restartStatus");
			    	var restartStatusImgList = self.$el.find(".SID-restartstatusBox").find(".restartStatusImg");
                	var hasOverTimeFlag = false;
					for(var i=0;i<restartstatusBoxList.length;i++){
						if($(restartstatusBoxList[i]).html() == "重启超时"){
							hasOverTimeFlag = true;
							$(restartstatusBoxList[i]).parent().html('<img class="restartStatusImg" src="'+preFix+"images/icon/loading.gif"+'"/><label class="restartStatus">获取中</label>');
						}
					}
					if(hasOverTimeFlag){
						self.timecount = 0;
						self.keyQueryRestartStatus();
					}
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
								    	var preFix = isIframe?iframePrefix:"";
										for(var i=0;i<restartstatusBoxList.length;i++){
											if($(restartstatusBoxList[i]).html() == "重启中"){
												$(restartstatusBoxList[i]).css("color","red");
												$(restartstatusBoxList[i]).html("重启超时");
												$(restartstatusBoxList[i]).parent().append('<br/><span style="color:red">服务重启时间超时,请检查服务运行状态.</span>');
												$(restartStatusImgList[i]).attr("src",preFix+"images/main/close_on.png");
											}
										}
										self.dialog.addBtn("ok","确定",function(){
	                                        self.destroy();
	                                        self.dialog.cancel();
	                                    });
										self.dialog.addBtn("refresh","重新获取",function(){
											self.reQueryStatus();
	                                    });
                                    }else{
                                    	if(ajax.restartStatusList.length > 0){
    		                        		var allUpFlag = true;
    		                        		for(var i=0;i<ajax.restartStatusList.length;i++){
    		                        			for(var k=0;k<self.disconnectedList.length;k++){
    		                        				console.log(self.disconnectedList[k].ip == ajax.restartStatusList[i].ip && self.disconnectedList[k].port == ajax.restartStatusList[i].port);
    		                        				console.log(self.disconnectedList[k].ip == ajax.restartStatusList[i].ip);
    		                        				console.log(self.disconnectedList[k].port == ajax.restartStatusList[i].port);
    		                        				if(self.disconnectedList[k].ip == ajax.restartStatusList[i].ip && self.disconnectedList[k].port == ajax.restartStatusList[i].port){
    		                        					ajax.restartStatusList.splice(i,1);
    		                        				}
    		                        			}
    		                        			if(ajax.restartStatusList[i].status == "down"){
    		                        				allUpFlag = false;
    		                        			}
    		                        		}
    		                        		var statusHtml = _.template(restartStatusTemplate, {
    		    								'restartStatusList': ajax.restartStatusList,
    		    							});
    		                        		self.$el.find(".SID-restartstatusBox").html(statusHtml);
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
        return RestartSnippetView;
    });

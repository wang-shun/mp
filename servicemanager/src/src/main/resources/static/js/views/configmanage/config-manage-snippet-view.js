define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/configmanage/configManageTemplate.html'
		  ,'text!../../templates/configmanage/configBlockTemplate.html'
		  ,'text!../../templates/configmanage/configParamsTemplate.html'
		  ,'text!../../templates/configmanage/configServicesTemplate.html'
		  ,'text!../../templates/configmanage/configResourcesTemplate.html'
		  , 'views/configmanage/restart-snippet-view'
		  , 'views/configmanage/instance-snippet-view'],
		function($, CommunicationBaseView,Template,ConfigBlockTemplate,ConfigParamsTemplate,ConfigServicesTemplate,ConfigResourcesTemplate, RestartView,InstanceView) {
			var treeObj; 
			var timeTable = new Array();
			var ConfigManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-block':'_onClickBlock',
							'click .SID-detailSave-btn':'_onClickDetailSave',
							'click .SID-detailSave-restart-btn':'_onClickDetailRestart',
							'click .SID-detailBack-btn':'_onClickDetailBack',
							'click .SID-blockDelete-btn':'_onClickBlockDelete',
							'click .SID-cancel-btn':'_onClickCancelBtn',
                            'click .SID-bind-btn': '_onClickBind',
                            'click .SID-toggleBox': '_onClickToggleBox',
                            'keyup .form-item-text' : '_changetext',
                            'click .SID-serviceId-a' : '_onGetInstance',
                            'click .SID-toggle-name-key-btn' : '_toggleNameKey',
                            'change .paramchange' : '_paramChange',
                            'keyup .paramchange' : '_paramChange',
						},
						initialize : function() {
							this.views = {};
							this.appId = "";
							this.instanceList = [];
							this.configCheckrst = "";
							this.resourceCheckrst = "";
							this.serviceCheckrst = "";
							this.dorestart = 0;
							this.isalive = 0;
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							return this;
						},
						refresh : function() {
						},
						destroyBusinessViews : function(){
							$.each(this.views, function(index, view) {
								 view.destroy();
							});		
							this.views = {};
						},		
						destroy : function() {
							this.destroyBusinessViews()
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						setContentHTML : function (){
							showCover("正在加载数据······");
							var self = this;
							var template = _.template(Template);
							var html = template({});
							self.$el.append(html);
							self.uiRanderUtil.randerJQueryUI_DateRange(self,"#from","#to","yy-mm-dd");
                            //var databaseId = $(e.target).attr("data-databaseId");
                            var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
                            $.ajax({
                                type:"POST",
                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.app.list",//+databaseId,
                                success:function(ajax){
                                	if(ajax.code=="1"){
                                		if(ajax.list != undefined){
                                			var blockHtml = _.template(ConfigBlockTemplate, {
    	        								'list': ajax.list,
    	        							});
    										self.$el.find(".SID-blockBox").append(blockHtml);
                                		}
                                		if(ajax.offLineList != undefined){
                                			var blockHtml = _.template(ConfigBlockTemplate, {
    	        								'list': ajax.offLineList,
    	        							});
    										self.$el.find(".SID-offLineBlockBox").append(blockHtml);
                                		}
                                	}else{
						    			fh.alert(ajax.message);
						    		}
                                	hideCover();
                                }
                            });
						},
						_onClickBlock :function(e){
							var self = this;
							self._emptyAll();
							$(e.currentTarget).addClass("config-block-active");
							//var path = $(e.currentTarget).attr("data-appId");
							
							var appId = $(e.currentTarget).attr("data-appId");
							if(appId == ""){
								return;
							}
							self.appId = $(e.currentTarget).attr("data-appId");
							var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
                            showCover("正在加载数据······");
                            
                    		var dependencyGetMethod = "";
                    		if($(e.currentTarget).parent()[0].className == "SID-blockBox"){
                    			dependencyGetMethod = "mapps.servicemanager.dependecies.get";
                    			self.$el.find(".SID-detailSave-btn").html("应用");
                    			self.$el.find(".SID-detailSave-restart-btn").show();
                    			self.$el.find(".SID-blockDelete-btn").hide();
                    			self.isalive = 1;
                    		}else if($(e.currentTarget).parent()[0].className == "SID-offLineBlockBox"){
                    			dependencyGetMethod = "mapps.servicemanager.dependecies.offget";
                    			self.$el.find(".SID-detailSave-btn").html("保存");
                    			self.$el.find(".SID-detailSave-restart-btn").hide();
                    			self.$el.find(".SID-blockDelete-btn").show();
                    			self.isalive = 0;
                    		}
                            $.ajax({
                                type:"POST",
                                url:servicePath+"?"+ropParam+ "&method="+dependencyGetMethod+"&appId="+appId,
                                success:function(ajax){
                                	if(ajax.code=="1"){
                                		self.instanceList = ajax.instanceList;
                                		var instanceList = ajax.instanceList;
                                		var instanceNum = instanceList.length;
                                		var instanceUpNum = 0;
                                		var instanceDownNum = 0;
                                		for(var k=0;k<instanceNum;k++){
                                			if(instanceList[k].status == "UP"){
                                				instanceUpNum++;
                                			}else if(instanceList[k].status == "DOWN"){
                                				instanceDownNum++;
                                			}
                                		}
                                		var instanceUpNumStr = " "+(instanceUpNum == 0 ? "" : ("UP:"+instanceUpNum));
                                		var instanceDownNumStr = " "+(instanceDownNum == 0 ? "" : ("DOWN:"+instanceDownNum));
                                		if(instanceNum > 0){
                                			self.$el.find(".SID-serviceId-label").html('<a class="SID-serviceId-a">'+appId+'('+instanceUpNumStr+instanceDownNumStr+')'+'</a>');
                							self.$el.find(".SID-serviceId-a").css("color","#1788fb");
                                		}else{
                                			self.$el.find(".SID-serviceId-label").html(appId);
                                		}
            							
                                		if(ajax.configProperties == undefined){
                                			self.$el.find(".SID-paramsBox").append('<br/><div style="text-align:center;color:gray;width:90%;width:90%;">暂无配置数据</div>');
                                		}else if(ajax.configProperties.length == 0){
                                			self.$el.find(".SID-paramsBox").append('<br/><div style="text-align:center;color:gray;width:90%;width:90%;">暂无配置数据</div>');
                                		}else{
                                			var paramsHtml = _.template(ConfigParamsTemplate, {
		        								'configProperties': ajax.configProperties,
		        							});
	                                		self.$el.find(".SID-paramsBox").append(paramsHtml);
                                		}
                                		if(ajax.services == undefined){
                                			self.$el.find(".SID-servicesBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
                                		}else if(ajax.services.length == 0){
                                			self.$el.find(".SID-servicesBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
                                		}else{
	                                		var servicesHtml = _.template(ConfigServicesTemplate, {
		        								'services': ajax.services,
		        								'appId': appId,
		        							});
	                                		self.$el.find(".SID-servicesBox").append(servicesHtml);
                                		}
                                		if(ajax.resources == undefined){
                                			self.$el.find(".SID-resourcesBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
                                		}else if(ajax.resources.length == 0){
                                			self.$el.find(".SID-resourcesBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
                                		}else{
                                			 $.ajax({
                                				 type:"POST",
                                				 url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.resourcelist.get&resources="+encodeURI(JSON.stringify(ajax.resources)),
                                				 success:function(data){
                                					 if(ajax.code=="1"){
                                						 var resourcesHtml = _.template(ConfigResourcesTemplate, {
             		        								'resources': ajax.resources,
             		        								'resMap': data.resMap,
             		        							});
             	                                		self.$el.find(".SID-resourcesBox").html(resourcesHtml);
                                					 }else{
                                						 fh.alert(ajax.message);
                                					 }
                                					 var checkrlt = self._submitCheck(false);
                                             		 self.configCheckrst = JSON.stringify(checkrlt.config);
                                             		 self.resourceCheckrst = JSON.stringify(checkrlt.resourceList);
                                             		 self.serviceCheckrst = JSON.stringify(checkrlt.serviceList);
                                					 hideCover();
                                				 }
                                			 });
                                		}
                                	}else{
						    			fh.alert(ajax.message);
						    		}
                                	hideCover();
                                }
                            });
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
//                            $.ajax({
//                                type:"POST",
//                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.resourcelist.get&offset=1&limit=100&isenabled=1",
//                                success:function(ajax){
//                                	if(ajax.code=="1"){
//                                		var databases =ajax.databases;
//                                		var redisList =ajax.redisList;
//                                		var dependencyGetMethod = "";
//                                		if($(e.currentTarget).parent()[0].className == "SID-blockBox"){
//                                			dependencyGetMethod = "mapps.servicemanager.dependecies.get";
//                                		}else if($(e.currentTarget).parent()[0].className == "SID-offLineBlockBox"){
//                                			dependencyGetMethod = "mapps.servicemanager.dependecies.offget";
//                                		}
//                                		$.ajax({
//			                                type:"POST",
//			                                url:servicePath+"?"+ropParam+ "&method="+dependencyGetMethod+"&appId="+appId,
//			                                success:function(ajax){
//			                                	if(ajax.code=="1"){
//			                                		if(ajax.configProperties.length == 0){
//			                                			self.$el.find(".SID-paramsBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
//			                                		}else{
//			                                			var paramsHtml = _.template(ConfigParamsTemplate, {
//					        								'configProperties': ajax.configProperties,
//					        							});
//				                                		self.$el.find(".SID-paramsBox").append(paramsHtml);
//			                                		}
//			                                		if(ajax.services.length == 0){
//			                                			self.$el.find(".SID-servicesBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
//			                                		}else{
//				                                		var servicesHtml = _.template(ConfigServicesTemplate, {
//					        								'services': ajax.services,
//					        								'appId': appId,
//					        							});
//				                                		self.$el.find(".SID-servicesBox").append(servicesHtml);
//			                                		}
//			                                		if(ajax.resources.length == 0){
//			                                			self.$el.find(".SID-resourcesBox").append('<br/><div style="text-align:center;color:gray;width:90%;">暂无配置数据</div>');
//			                                		}else{
//				                                		var resourcesHtml = _.template(ConfigResourcesTemplate, {
//					        								'resources': ajax.resources,
//					        								'databases': databases,
//					        								'redisList': redisList,
//					        							});
//				                                		self.$el.find(".SID-resourcesBox").append(resourcesHtml);
//			                                		}
//			                                		var checkrlt = self._submitCheck(false);
//			                                		self.configCheckrst = JSON.stringify(checkrlt.config);
//			                                		self.resourceCheckrst = JSON.stringify(checkrlt.resourceList);
//			                                		self.serviceCheckrst = JSON.stringify(checkrlt.serviceList);
//			                                	}else{
//									    			fh.alert(ajax.message);
//									    		}
//			                                	hideCover();
//			                                }
//			                            });
//                                	}else{
//						    			fh.alert(ajax.message);
//						    		}
//                                	hideCover();
//                                }
//                            });
							//self.$el.find(".SID-blockBox").hide();
							self.$el.find(".SID-detailBox").show();
						},
						_onClickDetailBack:function(){
							var self = this;
							var checkrlt = self._submitCheck(false);
							if(self.configCheckrst != JSON.stringify(checkrlt.config) || self.resourceCheckrst != JSON.stringify(checkrlt.resourceList) || self.serviceCheckrst != JSON.stringify(checkrlt.serviceList)){
								fh.confirm('配置信息已被修改,是否不保存返回?',function(){
									//self.$el.find(".SID-detailBox").hide();
									self.$el.find(".SID-blockBox").show();
								});
							}else{
								//self.$el.find(".SID-detailBox").hide();
								self.$el.find(".SID-blockBox").show();
							}
						},
						_onClickBlockDelete:function(e){
							var self = this;
							var appId = self.appId;
							var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
							fh.confirm('<span style="color:red">是否删除该离线服务?(路由信息及相关配置信息将同步移除)</span>',function(){
								$.ajax({
	                                type:"POST",
	                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dependecies.offdelete&appId="+appId,
	                                success:function(ajax){
	                                	hideCover();
	                                	if(ajax.code=="1"){
	                                		fh.alert('删除离线服务成功');
	                                		self.render();
	                                	}else{
							    			fh.alert(ajax.message);
							    		}
	                                }
	                            });
							});
						},
						_paramChange:function(){
							var self = this;
							var checkrlt = self._submitCheck(false);
							if(self.configCheckrst != JSON.stringify(checkrlt.config) || self.resourceCheckrst != JSON.stringify(checkrlt.resourceList) || self.serviceCheckrst != JSON.stringify(checkrlt.serviceList)){
								self.$el.find(".SID-detailSave-btn").removeClass("configaddbtndisable");
								self.$el.find(".SID-detailSave-btn").addClass("configaddbtn");
								self.$el.find(".SID-detailSave-btn").addClass("meet-add");
								self.dorestart = 1;
							}else{
								self.$el.find(".SID-detailSave-btn").removeClass("configaddbtn");
								self.$el.find(".SID-detailSave-btn").removeClass("meet-add");
								self.$el.find(".SID-detailSave-btn").addClass("configaddbtndisable");
								self.dorestart = 0;
							}
						},
						_onClickDetailRestart :function(e){
							var self = this;
							var checkrlt = self._submitCheck(false);
							if(self.configCheckrst != JSON.stringify(checkrlt.config) || self.resourceCheckrst != JSON.stringify(checkrlt.resourceList) || self.serviceCheckrst != JSON.stringify(checkrlt.serviceList)){
								fh.alert('配置信息已被修改,请点击应用按钮保存并重启。');
								return;
							}
							var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
                            var appId = self.appId;
                            fh.confirm('是否重启服务?',function(){	
	                            $.ajax({
	                                type:"POST",
	                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.server.restart&application="+appId,
	                                success:function(ajax){
	                                	if(ajax.code=="1"){
	        								var data = {
	        										title:"重启结果",
	        										appId: appId,
	        										restartStatusList: ajax.restartStatusList,
	        								}
	        								self.views.RestartView = new RestartView();
	        								self.views.RestartView.render(self,data);
//	                                        fh.alert("保存并重启成功");
//	                                        self.$el.find(".SID-detailBox").hide();
	                                        self.$el.find(".SID-blockBox").show();
	                                	}else{
							    			fh.alert(ajax.message);
							    		}
	                                	hideCover();
	                                }
	                            });
                            });
						},
						_onClickDetailSave :function(e){
							//var restartFlag = ($(e.currentTarget).attr("class").indexOf("restart") > -1);
							var self = this;
							if(self.dorestart == 0){
								return;
							}
							showCover("正在操作······");
							//self.validateForm("formMsg");
							var checkResult = self._submitCheck(true);
							if(!(checkResult.checkOk)){
								hideCover();
								return;
							}
							var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
                            var appId = self.appId;
                            var config = JSON.stringify(checkResult.config);
                            var resourceList = JSON.stringify(checkResult.resourceList);
                            var serviceList = JSON.stringify(checkResult.serviceList);
                            var configChange = false;
                            var resourceChange = false;
                            var serviceChange = false;
                            var isAllDef = false;
                            if(self.configCheckrst != JSON.stringify(checkResult.config)){
                            	configChange = true;
                            }
                    		if(self.resourceCheckrst != JSON.stringify(checkResult.resourceList)){
                    			resourceChange = true;
                    		}
                    		if(self.serviceCheckrst != JSON.stringify(checkResult.serviceList)){
                    			serviceChange = true;
                    		}
                    		var cfgParamDefNum = 0;
                    		for(var n=0;n<checkResult.config.length;n++){
                    			if(checkResult.config[n].isDef == "1"){
                    				cfgParamDefNum++;
                    			}
                    		}
                        	$.ajax({
                                type:"POST",
                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.app.saveform&appId="+appId+"&config="+config+"&resourceList="+resourceList+"&serviceList="+serviceList+"&configChange="+configChange+"&resourceChange="+resourceChange+"&serviceChange="+serviceChange),
                                success:function(ajax){
                                	if(ajax.code=="1"){
        								self.configCheckrst = JSON.stringify(checkResult.config);
        								self.resourceCheckrst = JSON.stringify(checkResult.resourceList);
        								self.serviceCheckrst = JSON.stringify(checkResult.serviceList);
        								self._paramChange();
                                		if((configChange || resourceChange || serviceChange || (cfgParamDefNum != checkResult.config.length)) && self.isalive == "1"){
                                			fh.confirm('配置保存成功,是否重启服务?',function(){	
	                                			$.ajax({
	                                                type:"POST",
	                                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.server.restart&application="+appId,
	                                                success:function(ajax){
	                                                	if(ajax.code=="1"){
	                        								var data = {
	                        										title:"重启结果",
	                        										appId: appId,
	                        										restartStatusList: ajax.restartStatusList,
	                        								}
	                        								self.views.RestartView = new RestartView();
	                        								self.views.RestartView.render(self,data);
//	                                                        fh.alert("保存并重启成功");
//	                                                        self.$el.find(".SID-detailBox").hide();
	                                                        self.$el.find(".SID-blockBox").show();
	                                                	}else{
	                						    			fh.alert(ajax.message);
	                						    		}
	                                                	hideCover();
	                                                }
	                                            });
	                                		});
                                		}else{
                                			fh.alert("保存成功");
                                    		//self.$el.find(".SID-detailBox").hide();
            								self.$el.find(".SID-blockBox").show();
                                		}
                                	}else{
						    			fh.alert(ajax.message);
						    		}
                                	hideCover();
                                }
                            });
							
						},
						_toggleNameKey:function(e){
							var self = this;
							var labelList = self.$el.find(".SID-paramsBox").find(".SID-toggle-name-key");
							for(var i=0;i<labelList.length;i++){
								if($(labelList[i]).html() == $(labelList[i]).attr("data-name")){
									$(labelList[i]).html($(labelList[i]).attr("data-key"));
								}else{
									$(labelList[i]).html($(labelList[i]).attr("data-name"));
								}
							}
						},
						_onGetInstance :function(){
							var self = this;
							var data = {
									title:"实例详情",
									instanceList: self.instanceList,
							}
							self.views.InstanceView = new InstanceView();
							self.views.InstanceView.render(self,data);
						},
						_changetext :function(){
							this._submitCheck(true);
						},
						_submitCheck :function(regexflag){
							var self = this;
							var checkResult = {
									'checkOk': true,
									'config': [],
									'resourceList': [],
									'serviceList': [],
							};
							var checkReguList = [];
							var inputList = self.$el.find(".SID-paramsBox").find("input");
							var checkBoxkeys = "";
							for(var i=0;i<inputList.length;i++){
								var checkObj = {};
								var regu = $(inputList[i]).parent().attr("data-regex");
								var isDef = $(inputList[i]).parent().attr("data-isDef");
								checkObj.isDef = isDef;
								if($(inputList[i]).attr("type") == "text"){
									checkObj.key = $(inputList[i]).attr("name");
									checkObj.value = $(inputList[i]).val();
									checkReguList.push(regu);
									checkResult.config.push(checkObj);
								}else if($(inputList[i]).attr("type") == "radio"){
									if($(inputList[i]).attr("checked") == "checked"){
										checkObj.key = $(inputList[i]).attr("name");
										checkObj.value = $(inputList[i]).val();
										checkReguList.push(regu);
										checkResult.config.push(checkObj);
									}
								}else if($(inputList[i]).attr("type") == "checkbox"){
									if($(inputList[i]).attr("checked") == "checked"){
										if(checkBoxkeys.indexOf($(inputList[i]).attr("name")) > -1){
											var tempvalue = "";
											for(var m=0;m<checkResult.config.length;m++){
												if(checkResult.config[m].key == $(inputList[i]).attr("name")){
													tempvalue = checkResult.config[m].value;
													checkResult.config[m].value = tempvalue+","+$(inputList[i]).val();
												}
											}
										}else{
											checkBoxkeys += "@#@"+$(inputList[i]).attr("name");
											checkObj.key = $(inputList[i]).attr("name");
											checkObj.value = $(inputList[i]).val();
											checkReguList.push(regu);
											checkResult.config.push(checkObj);
										}
									}
								}
							}
							if(regexflag){
								checkResult.checkOk = self._reguCheck(checkResult.config,checkReguList);
							}
							
							var serviceBoxList = self.$el.find(".SID-servicesBox").find(".form-item-editbox");
							for(var i=0;i<serviceBoxList.length;i++){
								var service = {};
								service.svcId = $(serviceBoxList[i]).parent().attr("data-svcId");
								service.appkey = $(serviceBoxList[i]).find(".service-appkey").html();
								service.secret = $(serviceBoxList[i]).find(".service-secret").html();
								checkResult.serviceList.push(service);
							}
							
							var selectList = self.$el.find(".SID-resourcesBox").find(".form-item-select");
							for(var i=0;i<selectList.length;i++){
								var resource = {};
								resource.resId = $(selectList[i]).attr("data-resId");
								resource.resCode = $(selectList[i]).attr("data-resCode");
								resource.resName = $(selectList[i]).attr("data-resName");
								resource.value = $(selectList[i]).val();
								checkResult.resourceList.push(resource);
							}
							
							return checkResult;
						},
						_reguCheck :function(configList,checkReguList){
							var self = this;
							var checktf = true;
							for(var i=0;i<configList.length;i++){
								if(new RegExp("("+ checkReguList[i] +")").test(configList[i].value)){
									$($("input[name='"+configList[i].key+"']").parents(".form-item").find('.wrong-text')).html("");
									$($("input[name='"+configList[i].key+"']").parents(".form-item").find('.form-item-tip')).hide();
								}else{
									$($("input[name='"+configList[i].key+"']").parents(".form-item").find('.wrong-text')).html($($("input[name='"+configList[i].key+"']").parents(".form-item").find('.form-item-label')).html()+"不符合校验规则");
									$($("input[name='"+configList[i].key+"']").parents(".form-item").find('.form-item-tip')).show();
									checktf = false;
								}
							}
							return checktf;
						},
						_emptyAll :function(){
							var self = this;
							self.$el.find(".SID-detailSave-btn").removeClass("configaddbtn");
							self.$el.find(".SID-detailSave-btn").removeClass("meet-add");
							self.$el.find(".SID-detailSave-btn").addClass("configaddbtndisable");
							self.$el.find(".fhicon-arrowD2").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
							self.$el.find(".SID-servicePath").val("");
							self.$el.find(".SID-baseInfoBox").show()
							self.$el.find(".SID-paramsBox").html("").show();
							self.$el.find(".SID-servicesBox").html("").show();
							self.$el.find(".SID-resourcesBox").html("").show();
							var configBlockList = self.$el.find(".config-block");
							for(var i=0;i<configBlockList.length;i++){
								$(configBlockList[i]).removeClass("config-block-active");
							}
						},
						_onClickToggleBox :function(e){
							var boxId = $(e.currentTarget).attr("data-box");
							var self = this;
							if(self.$el.find("."+boxId).css("display") == "block"){
								self.$el.find("."+boxId).parent().find(".fhicon-arrowU2").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
								self.$el.find("."+boxId).hide();
							}else{
								self.$el.find("."+boxId).parent().find(".fhicon-arrowD2").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
								self.$el.find("."+boxId).show();
							}
						},
						_onFormChanged :function(){
							var self = this;
							self.$el.find(".SID-paramsBox").attr("data-changed","true");
						},
						_onClickCancelBtn :function(e){
							var self = this;
							fh.confirm('是否取消绑定?',function(){
								//showCover("正在操作······");
								var appId = $(e.currentTarget).parent().parent().attr("data-appId");
								var svcId = $(e.currentTarget).parent().parent().attr("data-svcId");
								var rankNo = $(e.currentTarget).parent().parent().attr("rankNo");
								var appContext = self.getAppContext();
	                            var servicePath = appContext.cashUtil.getData('servicePath');
	                            var ropParam = appContext.cashUtil.getData('ropParam');
	                            self.$el.find(".SID-appkey-sercet-"+rankNo).html('<label>未绑定路由</label>');
                        		self.$el.find(".SID-optBox-"+rankNo).html('<a class="meet-add configaddbtn SID-bind-btn"></span>绑定</a>');
                        		self._paramChange();
//	                            $.ajax({
//	                                type:"POST",
//	                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.svcauth.delete&appId="+appId+"&svcId="+svcId,
//	                                success:function(ajax){
//	                                	if(ajax.code=="1"){
//	                                		
//	                                	}else{
//							    			fh.alert(ajax.message);
//							    		}
//	                                	hideCover();
//	                                }
//	                            });
							});
						},

                        _onClickBind: function (e) {
                            var self=this;
                            showCover("正在操作······");
                            var appId = $(e.currentTarget).parent().parent().attr("data-appId");
							var svcId = $(e.currentTarget).parent().parent().attr("data-svcId");
							var rankNo = $(e.currentTarget).parent().parent().attr("rankNo");
							var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
                            $.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.svcauth.createappkey&serviceId="+svcId,
								success:function(ajax){
									if(ajax.code=="1"){
										if(ajax.appkey != undefined){
											var appkeysecretcontent = '<label>appkey: </label>'+
							                '<label class="service-appkey" style="width: 270px;display:inline-block">'+ajax.appkey+'</label>'+
							                '<label>密钥: </label>'+
							                '<label class="service-secret" style="width: 150px;display:inline-block">'+ajax.secret+'</label>';
		                            		self.$el.find(".SID-appkey-sercet-"+rankNo).html(appkeysecretcontent);
										}else{
											var appkeysecretcontent = '<label class="service-appkey">已绑定</label>';
											self.$el.find(".SID-appkey-sercet-"+rankNo).html(appkeysecretcontent);
										}
										self.$el.find(".SID-optBox-"+rankNo).html('<a class="meet-add configaddbtn SID-cancel-btn"></span>取消</a>');
										self._paramChange();
//										$.ajax({
//											type:"POST",
//											url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.svcauth.get&svcId="+svcId+"&appId="+appId,
//											success:function(ajax){
//												if(ajax.code=="1"){
//													
//			                                	}else{
//									    			fh.alert(ajax.message);
//									    		}
//			                                	hideCover();
//											}
//										});
                                	}else{
						    			fh.alert(ajax.message);
						    		}
									hideCover();
								}
							});
                        },
					});
			return ConfigManageSnippetView;
		});
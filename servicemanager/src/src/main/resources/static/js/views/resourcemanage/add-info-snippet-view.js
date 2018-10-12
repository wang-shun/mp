define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/resourcemanage/addResourceInfoTemplate.html'
		  , 'text!../../templates/resourcemanage/resourceParamsTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,ConfigParamsTemplate
				,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'change .SID-resources' : '_onChangeType',
							'change .form-item-text' : '_submitCheck',
							'keyup .form-item-text' : '_submitCheck',
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							//计数器
							this.count=0;
							// 附件id
							this.fileIds=[];
							this.dialog;
							this.recentlyOpenedfileList = [];
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
                            this.passedTest=0;
							this._setContentHTML();
							this._queryResources();
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
						_queryResources:function(){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.resource.type.list',
								success:function(ajax){
									if(ajax.code == "1"){
										var resourceTypeStr = '<option value="">请选择</option>';
										var resourceType =ajax.resourceType;
										for(var i=0;i<resourceType.length;i++){
											resourceTypeStr += '<option value="'+resourceType[i].id+'">'+resourceType[i].name+'</option>';
										}
										self.$el.find(".SID-resources").html(resourceTypeStr);
										if(self.data.resourceDetail != null){
											self._fillData(self.data);
										}
									}
									else{
										fh.alert(ajax.message);
									}
								},
								error:function(){
									fh.alert("链接失败！");
								}
							});
						},
						_setContentHTML : function (){
							var self = this;
							var html = _.template(Template, {});
							this.$el.append(html);
							var commonDialog;

//							if(this.data.redisDetail != null){
								commonDialog =fh.servicemanagerOpenDialog('resourceInfoDialog', this.data.title, 780, 500, this.el);
//                                this._fillData(this.data);
//							}else{
//								commonDialog =fh.servicemanagerOpenDialog('dbAddDialog', this.data.title, 780, 500, this.el);
//							}
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							if(this.data.resourceDetail != null){
								commonDialog.addBtn("save","保存",function(){
									self._saveForm(commonDialog);
								});
							}else{
	                            commonDialog.addBtn("save","新增",function(){
	                                self._addForm(commonDialog);
	                            });
							}
							this.dialog = commonDialog;
						},
		                _fillData : function(data){
		                	var self = this;
		                	//填充基本信息
		                	self.$el.find(".SID-id").val(data.resourceDetail.id);
		                	self.$el.find(".SID-name").val(data.resourceDetail.name);
		                	self.$el.find(".SID-resources").val(data.resourceDetail.resId);
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.resource.type.item'+"&infoId="+data.resourceDetail.resId,
								success:function(ajax){
									if(ajax.code == "1"){
										if(ajax.resourceTypeItemList.length != 0){
											for(var n=0;n<ajax.resourceTypeItemList.length;n++){
												for(m=0;m<data.configList.length;m++){
													if(ajax.resourceTypeItemList[n].key == data.configList[m].key){
														ajax.resourceTypeItemList[n].defaultValue = data.configList[m].value;
													}
												}
											}
											
											var paramsHtml = _.template(ConfigParamsTemplate, {
		        								'configProperties': ajax.resourceTypeItemList,
		        							});
	                                		self.$el.find(".SID-resourceParamsBox").html(paramsHtml);
										}else{
											self.$el.find(".SID-resourceParamsBox").html("");
										}
									}
									else{
										fh.alert(ajax.message);
									}
								},
								error:function(){
									fh.alert("链接失败！");
								}
							});
		                },
		                _onChangeType:function(e){
							var self = this;
							var obj = $(e.currentTarget);
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.resource.type.item'+"&infoId="+obj.val(),
								success:function(ajax){
									if(ajax.code == "1"){
										if(ajax.resourceTypeItemList.length != 0){
											var paramsHtml = _.template(ConfigParamsTemplate, {
		        								'configProperties': ajax.resourceTypeItemList,
		        							});
	                                		self.$el.find(".SID-resourceParamsBox").html(paramsHtml);
										}else{
											self.$el.find(".SID-resourceParamsBox").html("");
										}
									}
									else{
										fh.alert(ajax.message);
									}
								},
								error:function(){
									fh.alert("链接失败！");
								}
							});
						},
						_submitCheck :function(regexflag){
							var self = this;
							var checkResult = {
									'checkOk': true,
									'config': [],
							};
							var checkReguList = [];
							var inputList = self.$el.find(".SID-resourceParamsBox").find("input");
							var checkBoxkeys = "";
							for(var i=0;i<inputList.length;i++){
								var checkObj = {};
								var regu = $(inputList[i]).parent().attr("data-regex");
//								var isDef = $(inputList[i]).parent().attr("data-isDef");
//								checkObj.isDef = isDef;
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
							if(regexflag != false){
								checkResult.checkOk = self._reguCheck(checkResult.config,checkReguList);
							}
							return checkResult;
						},
						_reguCheck :function(configList,checkReguList){
							var self = this;
							var checktf = true;
							for(var i=0;i<configList.length;i++){
								if(new RegExp("("+ checkReguList[i] +")").test(configList[i].value)){
									self.$el.find(self.$el.find("input[name='"+configList[i].key+"']").parents(".form-item").find('.wrong-text')).html("");
								}else{
									self.$el.find(self.$el.find("input[name='"+configList[i].key+"']").parents(".form-item").find('.wrong-text')).html(self.$el.find(self.$el.find("input[name='"+configList[i].key+"']").parents(".form-item").find('.form-item-label')).html()+"不符合校验规则");
									checktf = false;
								}
							}
							return checktf;
						},
                        _saveForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.resource.info.edit";
				        	this._sumbitForm(commonDialog,method,"保存服务",self._closeMethod);
				        },
				        _addForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.resource.info.add";
				        	this._sumbitForm(commonDialog,method,"新增资源",self._closeMethod);
				        },
				        // _createDb:function(commonDialog){
				        // 	var self = this;
				        // 	var method = "mapps.servicemanager.database.create";
				        // 	this._sumbitForm(commonDialog,method,"创建服务",self._closeMethod);
				        // },
				        _testDb:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.redis.test";
				        	this._sumbitForm(commonDialog,method,"测试",self._closeMethodTest);
				        },
				        _closeMethodTest:function(title,commonDialog,_self){
			    			_self._exalert(title+"成功",false,function(){
							});
				        },
                        _closeMethod:function(title,commonDialog,_self){
							_self._exalert(title+"成功",false,function(){
								commonDialog.cancel();
								_self.parentView.holdList();
								_self.destroy();
							});
                        },
                        _exalert:function(info,ischild,handler){
                        	var _self = this;
							fh.alert(info,true,handler,_self.dialog,null);
                        },
						_sumbitForm : function(commonDialog,method,title,closeMethod){
							var param = "";
							var _self = this;
							if(_self.$el.find(".SID-resourceParamsBox").html() == ""){
								return;
							}
							_self.validateForm("formMsg");
						    if(_self.validateResult()){
								var submitCheck = _self._submitCheck(true);
							    if(submitCheck.checkOk){
					                var appContext = this.getAppContext();
									var servicePath = appContext.cashUtil.getData('servicePath');
									var ropParam = appContext.cashUtil.getData('ropParam');
									var param = JSON.stringify(submitCheck.config);
									var id = _self.$el.find(".SID-id").val();
									var name = _self.$el.find(".SID-name").val();
									var resId = _self.$el.find(".SID-resources").val();
	                                this.showLoading();
	                                $.ajax({
	                                    type : "POST",
	                                    url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&configList="+param+"&id="+id+"&name="+name+"&resId="+resId),
	                                    success : function(response) {
	                                        _self.hideLoading();
	                                        if(response.code == "1"){
	                                            closeMethod(title,commonDialog,_self);
	                                        }else{
	                                            _self._exalert(response.message);
	                                        }
	                                    },
	                                    error : function(){
	                                        _self.hideLoading();
	                                        _self._exalert("数据处理失败");
	                                    }
	                                });
							    }
						    }
						}
					});
			return AddSnippetView;
		});
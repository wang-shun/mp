define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/routemanage/addExTemplate.html'
		  , 'views/databasemanage/create-snippet-view'
		  , 'views/doc/doc-snippet-view'
		  ,'text!../../templates/meeting/addAgendaTemplate.html'
		  ,'text!../../templates/meeting/addSignTemplate.html'
		  ,'text!../../templates/meeting/addRemarkTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,CreateView,DocView
				,agendaTemp,signTemp,remarkTemp
				,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-tab-btn' : '_onClickTab',
							'click .fhicon-close3' : '_onClickDelLi',
							'click .SID-clear' : '_onClickClear',
							'keyup .SID-search-user' : '_searchUser',
							'click .SID-user-li':'_chooseUser',
							'click .SID-dept-tree' : '_onClickDeptTree',
							'click .SID-dept-tree1' : '_onClickDeptTree1',
							'click .SID-outerOk' : '_onClickOuterOk',
							'click .add-schedule' : '_initAgenda',
							'click .SID-module-del':'_delModuleDiv',
							'click .add-sign':'_initSign',
							'click .add-remark' : '_initRemark',
							'click .SID-word-btn' : '_openWord',
							'click .SID-attach-remove' : '_removeAttach',
 							'click' : '_datepickerCheckExternalClick',
							'change .SID-dbType' : '_onChangedbType',
							'change .SID-needAuth' : '_onNeedAuthchange',
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
							var commonDialog;
							if(this.data.routeDetail!= null){
								commonDialog =fh.commonOpenDialog('dbEditDialog', this.data.title, 780, 500, this.el);
								this._fillData(this.data);
							}else{
								commonDialog =fh.commonOpenDialog('dbAddDialog', this.data.title, 780, 500, this.el);
							}
                            commonDialog.addBtn("cannel","取消",function(){
                                self.destroy();
                                commonDialog.cancel();
                            });
                            if(this.data.routeDetail != null){
                                commonDialog.addBtn("save","保存",function(){
                                    self._saveForm(commonDialog);
                                });
                            }else{
                                commonDialog.addBtn("save","保存",function(){
                                    self._addForm(commonDialog);
                                });
                            }
//							commonDialog.addBtn("test","测试",function(){
//								self._testDb(commonDialog);
//							});
							this.dialog = commonDialog;
						},
						_datepickerCheckExternalClick : function(Event){
		                    $.datepicker._checkExternalClick(Event);
		                },
		                _fillData : function(data){
                            //填充基本信息
                            this.$el.find(".SID-id").val(data.routeDetail.id);
                            this.$el.find(".SID-serviceName").val(data.routeDetail.serviceId);
                            this.$el.find(".SID-url").val(data.routeDetail.url);
                            this.$el.find(".SID-path").val(data.routeDetail.path);
                            this.$el.find(".SID-retryable").attr("checked",(data.routeDetail.retryable == "1"));
                            this.$el.find(".SID-stripPrefix").attr("checked",(data.routeDetail.stripPrefix == "1"));
                            /*this.$el.find(".SID-needAuth").attr("checked",(data.routeDetail.needAuth == "1"));
                            if(data.routeDetail.needAuth == "1"){
		                		this.$el.find(".SID-authResource").val(data.routeDetail.authResource);
		                	}else{
		                		this.$el.find(".authResourceBox").hide();
		                	}*/
                            this.$el.find(".SID-sensitiveHeaders").val(data.routeDetail.sensitiveHeaders);
                            this.$el.find(".SID-enabled").val(data.routeDetail.enabled);

                        },
                        _onNeedAuthchange:function(e){
							var self = this;
							var obj = $(e.currentTarget);
			            	var checked = obj.attr("checked");
			            	if(checked == "checked"){
			            		self.$el.find(".authResourceBox").show();
			            	}else{
			            		self.$el.find(".authResourceBox").hide();
			            	}
						},
						_delModuleDiv:function(e){
							var obj = $(e.currentTarget);
							var signObj = obj.parents(".SID-signs");
							obj.parents(".SID-module-div").remove();
							signObj.find(".SID-module-div:first").addClass("first");
							var divs = signObj.find(".SID-module-div");
							for(var i=0,div;div=divs[i++];){
								$(div).find(".sign-name").html("签到" + i);
							}
						},
                        _saveForm:function(commonDialog){
                            var self = this;
                            var method = "mapps.servicemanager.route.saveEx";
                            this._submitForm(commonDialog,method,"保存服务",self._closeMethod);
                        },
                        _addForm:function(commonDialog){
                            var self = this;
                            var method = "mapps.servicemanager.route.addEx";
                            this._submitForm(commonDialog,method,"保存服务",self._closeMethod);
                        },
				        _createDb:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.database.create";
				        	this._submitForm(commonDialog,method,"创建服务",self._closeMethod);
				        },
				        _testDb:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.database.test";
				        	this._submitForm(commonDialog,method,"测试",self._closeMethodTest);
				        },
				        _closeMethodTest:function(title,commonDialog,_self){
			    			fh.alert(title+"成功",false,function(){
							});
				        },
                        _closeMethod:function(title,commonDialog,_self){
							fh.alert(title+"成功",false,function(){
								commonDialog.cancel();
								_self.parentView.holdList();
								_self.destroy();
							});
                        },
						_submitForm : function(commonDialog,method,title,closeMethod){
							var param = "";
							var _self = this;
						    this.validateForm("formMsg");
						    if(this.validateResult()){
						    	var data = {};
						    	// 路由基本信息
                                var paramstr = "";
                                paramstr += "&id=" + this.$el.find(".SID-id").val();
                                paramstr += "&serviceName=" + this.$el.find(".SID-serviceName").val();
                                paramstr += "&url=" + this.$el.find(".SID-url").val();
                                paramstr += "&path=" + this.$el.find(".SID-path").val();
                                paramstr += "&retryable=" + ((this.$el.find(".SID-retryable").attr("checked") == "checked")?"1":"0");
                                paramstr += "&stripPrefix=" + ((this.$el.find(".SID-stripPrefix").attr("checked") == "checked")?"1":"0");
                                /*paramstr += "&needAuth=" + ((this.$el.find(".SID-needAuth").attr("checked") == "checked")?"1":"0");
                                if(this.$el.find(".SID-needAuth").attr("checked") == "checked"){
				                	paramstr += "&authResource=" + this.$el.find(".SID-authResource").val();
				                }else{
				                	paramstr += "&authResource=";
				                }*/
                                paramstr += "&needAuth=" + "0";
                                paramstr += "&authResource=";
                                
                                paramstr += "&ssHeaders=" + this.$el.find(".SID-sensitiveHeaders").val();
                                if(this.$el.find(".SID-enabled").val() == ""){
                                    paramstr += "&enabled=1";
                                }
				                
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								var param = JSON.stringify(data);
								this.showLoading();
								$.ajax({
									type : "POST",
                                    url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+paramstr),
									success : function(response) {
										_self.hideLoading();
										if(response.code == "1"){
											closeMethod(title,commonDialog,_self);
   							    		}else{
							    			fh.alert(response.message);
							    		}
									},
									error : function(){
										_self.hideLoading();
										fh.alert("数据处理失败");
									}
								});
						    }
						}
					});
			return AddSnippetView;
		});
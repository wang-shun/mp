define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/redisdatabasemanage/addTemplate.html'
		  , 'views/redisdatabasemanage/create-snippet-view'
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
							'change .form-item-text' : '_onChangedValue',
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

							if(this.data.redisDetail != null){
								commonDialog =fh.commonOpenDialog('dbEditDialog', this.data.title, 780, 500, this.el);
                                this._fillData(this.data);
							}else{
								commonDialog =fh.commonOpenDialog('dbAddDialog', this.data.title, 780, 500, this.el);
							}
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							if(this.data.redisDetail != null){
								commonDialog.addBtn("save","保存",function(){
									self._saveForm(commonDialog);
								});
							}else{
                            commonDialog.addBtn("save","新增",function(){
                                self._addForm(commonDialog);
                            });
							}
							// commonDialog.addBtn("create","创建",function(){
							// 	self.validateForm("formMsg");
							//     if(self.validateResult()){
							// 		self.childView.CreateView = new CreateView();
							// 		var data = {
                             //            title: "创建redis"
                             //        }
							// 		data.host = self.$el.find(".SID-host").val();
							// 		data.port = self.$el.find(".SID-port").val();
							// 		data.sidordbname = self.$el.find(".SID-sidordbname").val();
							// 		data.userName = self.$el.find(".SID-username").val();
							// 		data.password = self.$el.find(".SID-password").val();
							// 		self.childView.CreateView.render(self,data);
							//     }
							// });
							commonDialog.addBtn("test","测试",function(){
								self._testDb(commonDialog);
							});
							this.dialog = commonDialog;
							//this._dialogBtnInit("base-info");
							//this.uiRanderUtil.randerJQueryUI_DateTimeRange(this,".SID-startdate",".SID-enddate");
						},
						_datepickerCheckExternalClick : function(Event){
		                    $.datepicker._checkExternalClick(Event);
		                },
		                _fillData : function(data){
		                	//填充基本信息
		                	this.$el.find(".SID-host").val(data.redisDetail.host);
		                	this.$el.find(".SID-port").val(data.redisDetail.port);
                            this.$el.find(".SID-dbIndex").val(data.redisDetail.dbIndex);
		                	this.$el.find(".SID-username").val(data.redisDetail.userName);
		                	this.$el.find(".SID-password").val(data.redisDetail.password);
		                	this.$el.find(".SID-enabled").val(data.redisDetail.enabled);
		                	this.$el.find(".SID-id").val(data.redisDetail.id);
		                	this.$el.find(".SID-remarks").val(unescape(data.redisDetail.remarks));
		                },
		                _onChangedValue:function(e){
							var self = this;
							self.passedTest = 0;
						},
						_openWord:function(){
							this.childView.DocView= new DocView();
							this.childView.DocView.render(this,this.fileIds,this.recentlyOpenedfileList);
						},
                        _saveForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.redis.edit";
				        	this._sumbitForm(commonDialog,method,"保存redis",self._closeMethod);
				        },
				        _addForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.redis.add";
				        	this._sumbitForm(commonDialog,method,"新增redis",self._closeMethod);
				        },
				        // _createDb:function(commonDialog){
				        // 	var self = this;
				        // 	var method = "mapps.servicemanager.database.create";
				        // 	this._sumbitForm(commonDialog,method,"创建redis",self._closeMethod);
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
						    this.validateForm("formMsg");
						    if(this.validateResult()){
						    	var data = {};
						    	// 数据库基本信息
				                var databaseInfo = new Object();
				                databaseInfo.id = this.$el.find(".SID-id").val();
				                databaseInfo.host = this.$el.find(".SID-host").val();
				                databaseInfo.port = this.$el.find(".SID-port").val();
				                databaseInfo.dbIndex = this.$el.find(".SID-dbIndex").val();
				                databaseInfo.userName = this.$el.find(".SID-username").val();
				                databaseInfo.password = this.$el.find(".SID-password").val();
				                var tempenabled = (this.$el.find(".SID-enabled").val() == "")?"1":this.$el.find(".SID-enabled").val();
				                databaseInfo.enabled = tempenabled;
				                databaseInfo.remarks = escape(this.$el.find(".SID-remarks").val());
				                data.redis = databaseInfo;
				                
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								var param = JSON.stringify(data);
								if(title=="测试" || _self.passedTest=="1"){
                                    this.showLoading();
                                    $.ajax({
                                        type : "POST",
                                        url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&redisJson="+param),
                                        success : function(response) {
                                            _self.hideLoading();
                                            if(response.code == "1"){
                                            	if(title=="测试"){
                                                    _self.passedTest=1;
												}
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
								}else{
									_self._exalert("未测试或测试未通过，请通过测试后再继续");
								}

						    }
						}
					});
			return AddSnippetView;
		});
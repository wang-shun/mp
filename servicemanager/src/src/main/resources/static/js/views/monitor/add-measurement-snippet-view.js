define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/addMeasurementTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination', 'editableselect'],
		function($, CommunicationBaseView,Template
				,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'keyup .SID-retainTime' : '_onCheckRetainTime',
							'change .retainTimeUnit' : '_onCheckRetainTime',
							'change .SID-retentionpolicy' : '_onChangePolicy',
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.dialog;
							this.measurements = [];
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
							this._setContentHTML();
							this.queryMeasurements();
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
								commonDialog =fh.servicemanagerOpenDialog('measurementEditDialog', this.data.title, 780, 500, this.el);
                                this._fillData(this.data);
							}else{
								commonDialog =fh.servicemanagerOpenDialog('measurementAddDialog', this.data.title, 780, 500, this.el);
							}
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							if(this.data.measurementDetail != null){
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
						queryMeasurements:function(){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.measurement.list',
								success:function(ajax){
									if(ajax.code == "1"){
										var measurementsStr = '<option value="" selected></option>';
										if(ajax.queryResult[0].series != undefined){
											var measurements = ajax.queryResult[0].series[0].values;
											self.measurements = measurements;
											for(var i=0;i<measurements.length;i++){
												measurementsStr += '<option value="'+measurements[i]+'">'+measurements[i]+'</option>';
											}
											self.$el.find(".SID-measurement").append(measurementsStr).editableSelect({ effects: 'fade' });
											$.ajax({
												type:"POST",
												url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.retention.query&keyword=&limit=100&offset=1&sort=',
												success:function(ajax){
													if(ajax.code == "1"){
														var retentionStr = '<option value="" selected>自定义</option>';
														var retentionList = ajax.retentionList;
														for(var i=0;i<retentionList.length;i++){
															var selected = "";
															if(retentionList[i].isDefault == "1" && self.data.measurementDetail == null){
																selected = "selected";
															}
															retentionStr += '<option value="'+retentionList[i].retainTime+'" '+selected+'>'+unescape(retentionList[i].rp).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];})+":"+unescape(retentionList[i].rpName).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];})+'</option>';
														}
														self.$el.find(".SID-retentionpolicy").append(retentionStr);
														
														if(self.data.measurementDetail != null){
															self._fillData(self.data);
														}else{
															self._onChangePolicy();
														}
														hideCover();
													}
													else{
														hideCover();
														self._exalert("无法获取保留策略列表",true,function(){
															self.destroy();
															self.dialog.cancel();
														});
													}
												}
											});
										}
										hideCover();
									}
									else{
										hideCover();
										self._exalert("无法获取指标列表",true,function(){
											self.destroy();
											self.dialog.cancel();
										});
									}
								}
							});
						},
		                _fillData : function(data){
		                	//填充基本信息
		                	this.$el.find(".SID-id").val(data.measurementDetail.id);
		                	this.$el.find(".SID-measurement").val(data.measurementDetail.measurement);
		                	this.$el.find(".SID-measurement").attr("disabled",true);
		                	this.$el.find(".SID-name").val(data.measurementDetail.name);
		                	var retainTime = data.measurementDetail.retainTime.substr(0,data.measurementDetail.retainTime.length-1);
		                	var retainTimeUnit = data.measurementDetail.retainTime.substr(data.measurementDetail.retainTime.length-1,data.measurementDetail.retainTime.length);
		                	var _self = this;
							var retainTimeUnitList = _self.$el.find("input[name='retainTimeUnit']");
		                	for(var i=0;i<retainTimeUnitList.length;i++){
								if($(retainTimeUnitList[i]).val() == retainTimeUnit){
									$(retainTimeUnitList[i]).click();
								}
							}
		                	this.$el.find(".SID-retainTime").val(retainTime);
		                },
		                _onCheckRetainTime:function(e){
		                	var _self = this;
		                	var retainTime = _self.$el.find(".SID-retainTime").val();
							var retainTimeUnitList = _self.$el.find("input[name='retainTimeUnit']");
							var retainTimeUnit = '';
							for(var i=0;i<retainTimeUnitList.length;i++){
								if($(retainTimeUnitList[i]).attr("checked") == "checked"){
									retainTimeUnit = $(retainTimeUnitList[i]).val();
								}
							}
							var flag = true;
							if(retainTime < 0){
								_self.$el.find(_self.$el.find(".SID-retainTime").parents(".form-item").find('.wrong-text')).html("不能为负数");
								flag = false;
							}else{
								_self.$el.find(_self.$el.find(".SID-retainTime").parents(".form-item").find('.wrong-text')).html("");
							}
							return flag;
						},
						_onChangePolicy:function(){
		                	var self = this;
		                	var policyRetainTime = self.$el.find(".SID-retentionpolicy").val();
		                	var retainTime = policyRetainTime.substr(0,policyRetainTime.length-1);
		                	var retainTimeUnit = policyRetainTime.substr(policyRetainTime.length-1,policyRetainTime.length);
		                	var retainTimeUnitList = self.$el.find("input[name='retainTimeUnit']");
		                	for(var i=0;i<retainTimeUnitList.length;i++){
								if($(retainTimeUnitList[i]).val() == retainTimeUnit){
									$(retainTimeUnitList[i]).click();
								}
							}
		                	self.$el.find(".SID-retainTime").val(retainTime);
						},
                        _saveForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.measurement.edit";
				        	this._sumbitForm(commonDialog,method,"保存指标",self._closeMethod);
				        },
				        _addForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.measurement.add";
				        	this._sumbitForm(commonDialog,method,"新增指标",self._closeMethod);
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
						    if(this.validateResult() && _self._onCheckRetainTime()){
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								var id = _self.$el.find(".SID-id").val();
								var measurement = _self.$el.find(".SID-measurement").val();
								var ismeasurementexit = 0;
								var measurements = _self.measurements;
								for(var k=0;k<measurements.length;k++){
									if(measurements[k] == measurement){
										ismeasurementexit = 1;
									}
								}
								if(ismeasurementexit == 0){
									_self._exalert("指标:"+measurement+"不存在<br/>请选择下拉菜单中的指标");
									return;
								}
								var name = escape(_self.$el.find(".SID-name").val());
								var retainTime = _self.$el.find(".SID-retainTime").val();
								var retainTimeUnitList = _self.$el.find("input[name='retainTimeUnit']");
								var retainTimeUnit = '';
								for(var i=0;i<retainTimeUnitList.length;i++){
									if($(retainTimeUnitList[i]).attr("checked") == "checked"){
										retainTimeUnit = $(retainTimeUnitList[i]).val();
									}
								}
								
                                this.showLoading();
                                $.ajax({
                                    type : "POST",
                                    url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&id="+id+"&measurement="+measurement+"&name="+name+"&retainTime="+retainTime+"&retainTimeUnit="+retainTimeUnit),
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
						    }
						}
					});
			return AddSnippetView;
		});
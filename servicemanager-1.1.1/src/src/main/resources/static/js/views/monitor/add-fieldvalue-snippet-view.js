define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/addFieldValueTemplate.html'
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
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.dialog;
							this.fieldList = [];
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
							this._setContentHTML();
							this.queryFieldValue();
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
							commonDialog =fh.commonOpenDialog('fieldAddDialog', this.data.title, 680, 400, this.el,null,null,null,self.parentView.dialog);
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
                            commonDialog.addBtn("save","新增",function(){
                                self._addForm(commonDialog);
                            });
							this.dialog = commonDialog;
						},
						queryFieldValue:function(){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.field.value&id='+self.data.id,
								success:function(ajax){
									if(ajax.code == "1"){
										var optionsStr = '<option value="" selected></option>';
										var optionList =ajax.queryResult[0].series[0].values;
										self.fieldList = optionList;
										for(var i=0;i<optionList.length;i++){
											optionsStr += '<option value="'+optionList[i][0]+'">'+optionList[i][0]+'</option>';
										}
										self.$el.find(".SID-field").append(optionsStr).editableSelect({ effects: 'fade' });
										var unitStr = '<option value="" selected></option>';
										var unitList = self.data.unitList;
										for(var i=0;i<unitList.length;i++){
											unitStr += '<option value="'+unitList[i].unit+'">'+unitList[i].unit+'</option>';
										}
										self.$el.find(".SID-unit").append(unitStr).editableSelect({ effects: 'fade' });
//										if(self.data.measurementDetail != null){
//											self._fillData(self.data);
//										}
									}
									else{
										self._exalert(ajax.message);
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
								$($(".SID-retainTime").parents(".form-item").find('.wrong-text')).html("不能为负数");
								flag = false;
							}else{
								$($(".SID-retainTime").parents(".form-item").find('.wrong-text')).html("");
							}
							return flag;
						},
                        _saveForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.measurement.edit";
				        	this._sumbitForm(commonDialog,method,"保存指标",self._closeMethod);
				        },
				        _addForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.field.add";
				        	this._sumbitForm(commonDialog,method,"新增指标定义",self._closeMethod);
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
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								
								var field = _self.$el.find(".SID-field").val();
								var isfieldexit = 0;
								var fieldList = _self.fieldList;
								for(var k=0;k<fieldList.length;k++){
									if(fieldList[k][0] == field){
										isfieldexit = 1;
									}
								}
								if(isfieldexit == 0){
									_self._exalert("值域:"+field+"不存在<br/>请选择下拉菜单中的值域");
									return;
								}
								var name = escape(_self.$el.find(".SID-name").val());
								var unit = escape(_self.$el.find(".SID-unit").val());
								var originunit = _self.$el.find(".SID-unit").val();
								var isunitexit = 0;
								var unitList = _self.data.unitList;
								for(var k=0;k<unitList.length;k++){
									if(unitList[k].unit == originunit){
										isunitexit = 1;
									}
								}
								if(isunitexit == 0){
									_self._exalert("单位:\""+originunit+"\"不存在<br/>请选择下拉菜单中的单位");
									return;
								}
								
                                this.showLoading();
                                $.ajax({
                                    type : "POST",
                                    url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&field="+field+"&name="+name+"&unit="+unit+"&measurementId="+_self.data.id),
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
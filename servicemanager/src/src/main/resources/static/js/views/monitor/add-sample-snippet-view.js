define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/addSampleTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'change .SID-dbType' : '_onChangedbType',
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

							if(this.data.sampleDetail != null){
								commonDialog =fh.servicemanagerOpenDialog('dbEditDialog', this.data.title, 780, 500, this.el);
                                this._fillData(this.data);
							}else{
								commonDialog =fh.servicemanagerOpenDialog('dbAddDialog', this.data.title, 780, 500, this.el);
							}
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							if(this.data.sampleDetail != null){
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
		                	//填充基本信息
		                	this.$el.find(".SID-measurement").val(data.sampleDetail.cqName);
		                	this.$el.find(".SID-sampleSql").val(data.sampleDetail.sampleSql);
		                	this.$el.find(".SID-id").val(data.sampleDetail.id);
		                	this.$el.find(".SID-remarks").val(unescape(data.sampleDetail.remarks));
		                },
                        _saveForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.sample.edit";
				        	this._sumbitForm(commonDialog,method,"保存采样设置",self._closeMethod);
				        },
				        _addForm:function(commonDialog){
				        	var self = this;
				        	var method = "mapps.servicemanager.sample.add";
				        	this._sumbitForm(commonDialog,method,"新增采样设置",self._closeMethod);
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
				                var id = this.$el.find(".SID-id").val();
				                var measurement = escape(this.$el.find(".SID-measurement").val());
				                var sampleSql = escape(this.$el.find(".SID-sampleSql").val());
				                var remarks = escape(this.$el.find(".SID-remarks").val());
				                
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
                                this.showLoading();
                                $.ajax({
                                    type : "POST",
                                    url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&id="+id+"&measurement="+measurement+"&sampleSql="+sampleSql+"&remarks="+remarks),
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
					});
			return AddSnippetView;
		});
define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/ruleManageTemplate.html'
		  ,'views/monitor/add-rule-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,AddView,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var RuleManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-add-btn': '_openAddDialog',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-retention-start':'_onClickStart',
							'click .SID-retention-stop':'_onClickStop',
							'click .SID-retention-edit':'_onClickEdit',
							'click .SID-save-btn':'_onClickSave',
							'click .SID-cancel-btn':'_onClickCancel',
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
							this.parentView = {};
							this.conditionData = {};
						},
						render : function(parentView) {
							this.parentView = parentView;
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
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							this.holdList();
						},
						_onClickSave:function(e){
							var self = this;
							var databaseId = $(e.target).attr("data-databaseId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.retention.detail&id="+databaseId,
								success:function(ajax){
									self.views.AddView = new AddView();
									ajax.title = "保留策略详情";
									self.views.AddView.render(self,ajax);
								}
							});
						},
						_onClickCancel:function(e){
							this.$el.find(".SID-addrule").hide();
							this.$el.find(".SID-addrule").html("");
							this.views.AddView.undelegateEvents();
							this.$el.find(".SID-rulemanage").show();
						},
						_onClickEdit:function(e){
							var self = this;
							var id = $(e.target).attr("data-id");
							self._openAddDialog(id);
						},
						_onClickStop:function(e){
							var self = this;
							fh.confirm('禁用此预警规则？',function(){
								var id = $(e.target).attr("data-id");
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.rule.disable&id="+id,
									success:function(ajax){
										if(ajax.code=="1"){
											self.holdList();
	                                	}else{
							    			fh.alert(ajax.message);
							    		}
									}
								});
							});
						},
                        _onClickStart:function(e){
                        	var self = this;
                        	fh.confirm('启用此预警规则？',function(){
                        		var id = $(e.target).attr("data-id");
                                var appContext = self.getAppContext();
                                var servicePath = appContext.cashUtil.getData('servicePath');
                                var ropParam = appContext.cashUtil.getData('ropParam');
                                $.ajax({
                                    type:"POST",
                                    url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.rule.enable&id="+id,
                                    success:function(ajax){
                                    	if(ajax.code=="1"){
    										self.holdList();
                                    	}else{
    						    			fh.alert(ajax.message);
    						    		}
                                    }
                                });
							});
                        },
						search : function(){
							var keyword = this.$el.find(".SID-keyword").val();
							this.$el.find(".SID-keyword").val(keyword.trim());
							this.holdList();
						},
						clearSearch : function(){
							this.$el.find(".SID-keyword").val("");
						},
						_openAddDialog : function(editId){
							var self = this;
							self.$el.find(".SID-rulemanage").hide();
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.retention.getfrominflux',
								success:function(ajax){
									if(ajax.code == "1"){
										self.conditionData.retentionList = ajax.queryResult;
										$.ajax({
											type:"POST",
											url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.measurement.query&keyword=&limit=100&offset=1&sort=',
											success:function(ajax){
												if(ajax.code == "1"){
													self.conditionData.measurementList = ajax.measurementList;
													if(typeof(editId) == "string"){
														$.ajax({
															type:"POST",
															url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.rule.detail&id="+editId,
															success:function(ajax){
																if(ajax.code == "1"){
																	var data = {
																			title:"预警规则详情",
																			measurementList:self.conditionData.measurementList,
																			retentionList:self.conditionData.retentionList,
																			ruleDetail:ajax,
																	}
																	self.views.AddView = new AddView({el:$(".SID-addrule")});
																	self.views.AddView.render(self,data);
																	self.$el.find(".SID-addrule").show();
																	hideCover();
																}else{
																	hideCover();
																	fh.alert(ajax.message);
																}
															}
														});
													}else{
														var data = {
																title:"新增预警规则",
																measurementList:self.conditionData.measurementList,
																retentionList:self.conditionData.retentionList,
														}
														self.views.AddView = new AddView({el:$(".SID-addrule")});
														self.views.AddView.render(self,data);
														self.$el.find(".SID-addrule").show();
														hideCover();
													}
												}
												else{
													hideCover();
													fh.alert(ajax.message);
												}
											}
										});
									}
									else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						initSearchParam:function(){
							var param = "";
							param = $("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							return param;
						},
						holdList : function(){
							var tableIndex = 0;
							var tableObj={};
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.rule.query" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"指标key","sWidth":"20%","mDataProp":"measurement","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"规则名称","sWidth":"20%","mDataProp":"name","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"时间范围","sWidth":"20%","mDataProp":"pastTime","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"值域","sWidth":"20%","mDataProp":"valueField","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"状态","sWidth":"10%","mDataProp":"enabled","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var str = "";
								if(val == 1)
									str="<span>可用</span>";
								if(val == 0)
									str="<span style=\"color:red\">禁用</span>";
								return str;
							}},
							{"sTitle":"操作","sWidth":"10%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(o.aData.enabled =='<span>可用</span>')
									str += '<a href="javascript:void(0)" class="SID-retention-stop" data-id="'+o.aData.id+'"><span class="fhicon-delete"></span>禁用</a>';
								if(o.aData.enabled =='<span style=\"color:red\">禁用</span>')
									str += '<a href="javascript:void(0)" class="SID-retention-start" data-id="'+o.aData.id+'"><span class="fhicon-active"></span>启用</a>';
								str += '<a href="javascript:void(0)" class="SID-retention-edit" data-id="'+o.aData.id+'"><span class="fhicon-pencil"></span>修改</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.ruleList ? data.ruleList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return RuleManageSnippetView;
		});
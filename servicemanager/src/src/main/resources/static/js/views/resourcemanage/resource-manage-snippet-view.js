define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/resourcemanage/resourceManageTemplate.html'
		  ,'views/resourcemanage/add-info-snippet-view','views/resourcemanage/add-type-snippet-view'
		  ,'views/monitor/view-json-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,AddInfoView,AddTypeView,viewJSONView,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var RedisManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-addInfo-btn': '_onClickAddInfo',
							'click .SID-addType-btn': '_onClickAddType',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-database-start':'_onClickDatabaseStart',
							'click .SID-database-stop':'_onClickDatabaseStop',
							'click .SID-database-edit':'_onClickDatabaseEdit',
							'click .SID-json-view':'_viewJSON',
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
							this.datajson = [];
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
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							this.holdList();
						},
						_viewJSON:function(e){
                        	var self = this;
                        	var id = $(e.currentTarget).attr("data-id");
                        	var resultJson = {};
                        	for(var i=0;i<self.datajson.length;i++){
                        		if(self.datajson[i].id == id){
                        			if(typeof(self.datajson[i].configList) == "string"){
                        				resultJson = JSON.parse(self.datajson[i].configList);
                        			}else if(typeof(self.datajson[i].configList) == "object"){
                        				resultJson = self.datajson[i].configList;
                        			}
                        		}
                        	}
                        	var data = {
									title:"查看配置项",
									jsonData:resultJson
							}
                        	self.views.viewJSONView = new viewJSONView();
                        	self.views.viewJSONView.render(self,data);
                        },
						_onClickDatabaseEdit:function(e){
							var self = this;
							var databaseId = $(e.target).attr("data-databaseId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.resource.info.detail&resourceId="+databaseId,
								success:function(ajax){
									self.views.AddInfoView = new AddInfoView();
									ajax.title = "资源详情";
									self.views.AddInfoView.render(self,ajax);
								}
							});
						},
						_onClickDatabaseStop:function(e){
							var self = this;
							fh.confirm('禁用此资源？',function(){
								var databaseId = $(e.target).attr("data-databaseId");
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.resource.disable&resourceId="+databaseId,
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
                        _onClickDatabaseStart:function(e){
                        	var self = this;
                        	fh.confirm('启用此资源？',function(){
                                var databaseId = $(e.target).attr("data-databaseId");
                                var appContext = self.getAppContext();
                                var servicePath = appContext.cashUtil.getData('servicePath');
                                var ropParam = appContext.cashUtil.getData('ropParam');
                                $.ajax({
                                    type:"POST",
                                    url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.resource.enable&resourceId="+databaseId,
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
                        
                        _onClickDatabaseTest:function(e){
                            var self = this;
                            var databaseId = $(e.target).attr("data-databaseId");
                            var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
                            this.showLoading();
                            $.ajax({
                                type:"POST",
                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.redis.testByList&redisId="+databaseId,
                                success:function(ajax){
                                	if(ajax.code=="1"){
                                		fh.alert("Redis连接测试成功");
                                	}else{
						    			fh.alert(ajax.message);
						    		}
                                	self.hideLoading();
                                },
                                error : function(){
									self.hideLoading();
									fh.alert("数据处理失败");
								}
                            });
                        },
                        
						search : function(){
							this.holdList();
						},
						clearSearch : function(){
							this.$el.find(".SID-keyword").val("");
						},
						_onClickAddInfo : function(){
							var data = {
									title:"新增资源信息",
									op:"新增"
							}
							this.views.AddInfoView = new AddInfoView();
							this.views.AddInfoView.render(this,data);
						},
						_onClickAddType : function(){
//							var data = {
//									title:"新增资源类型",
//									op:"新增"
//							}
//							this.views.AddTypeView = new AddTypeView();
//							this.views.AddTypeView.render(this,data);
							
							var self = this;
                            var appContext = self.getAppContext();
                            var servicePath = appContext.cashUtil.getData('servicePath');
                            var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
                                type:"POST",
                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.influxdb.test",
                                success:function(ajax){
                                	if(ajax.code=="1"){
										
                                	}else{
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
							var self = this;
							var tableIndex = 0;
							var tableObj={};
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.resource.info.list" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"资源名称","sWidth":"20%","mDataProp":"name","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"资源类型","sWidth":"20%","mDataProp":"resId","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								return o.aData.resName;
							}},
							//{"sTitle":"说明","sWidth":"20%","mDataProp":"remarks","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"配置项","sWidth":"8%","mDataProp":"dbIndex","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" style="text-decoration:none" class="SID-json-view" data-id="'+o.aData.id+'"><span class="fhicon-eye"></span>查看</a>';
								return str;
							}},
							{"sTitle":"状态","sWidth":"8%","mDataProp":"enabled","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var str = "";
								if(val == 1)
									str="<span>可用</span>";
								if(val == 0)
									str="<span style=\"color:red\">禁用</span>";
								return str;
							}},
							{"sTitle":"操作","sWidth":"8%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(o.aData.enabled =='<span>可用</span>')
									str += '<a href="javascript:void(0)" class="SID-database-stop" data-databaseId="'+o.aData.id+'"><span class="fhicon-delete"></span>禁用</a>';
								if(o.aData.enabled =='<span style=\"color:red\">禁用</span>')
									str += '<a href="javascript:void(0)" class="SID-database-start" data-databaseId="'+o.aData.id+'"><span class="fhicon-active"></span>启用</a>';
								str += '<a href="javascript:void(0)" class="SID-database-edit" data-databaseId="'+o.aData.id+'"><span class="fhicon-pencil"></span>修改</a>';// +
								//	'<a href="javascript:void(0)" class="SID-database-test" data-databaseId="'+o.aData.id+'"><span class="fhicon-eye"></span>测试</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.resourceList ? data.resourceList : ''
								};
								self.datajson = jsonData.aaData;
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return RedisManageSnippetView;
		});
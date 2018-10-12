define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/databasemanage/dataBaseManageTemplate.html'
		  ,'views/databasemanage/add-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,AddView,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var HoldSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-add-btn': '_openAddDialog',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-meet-delete':'_onClickDeleteMeeting',
							'click .SID-meet-detail':'_onClickMeetingDetail',
							'click .SID-meet-cancel':'_onClickMeetingCancel',
							'click .SID-meet-apply':'_onClickMeetingApply',
							'click .SID-meet-edit':'_onClickMeetingEdit',
							'click .SID-database-start':'_onClickDatabaseStart',
							'click .SID-database-stop':'_onClickDatabaseStop',
							'click .SID-database-edit':'_onClickDatabaseEdit',
							'click .SID-database-test':'_onClickDatabaseTest'
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
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
							this.uiRanderUtil.randerJQueryUI_DateRange(this,"#from","#to","yy-mm-dd");
							this.holdList();
						},
						_onClickMeetingEdit:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.detailForWeb&meetingId="+meetingId,
								success:function(ajax){
									if(ajax.code == '1'){
										self._openEditDialog(ajax);
									}
								},
								error:function(){}
							});
						},

						_onClickDatabaseEdit:function(e){
							var self = this;
							var databaseId = $(e.target).attr("data-databaseId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.database.detail&databaseId="+databaseId,
								success:function(ajax){
									self.views.AddView = new AddView();
									ajax.title = "数据库详情";
									ajax.operation = 1;
									self.views.AddView.render(self,ajax);
								}
							});
						},
						_onClickDatabaseStop:function(e){
							var self = this;
							fh.confirm('禁用此数据库？',function(){
								var databaseId = $(e.target).attr("data-databaseId");
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.database.stop&databaseId="+databaseId,
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
                        	fh.confirm('启用此数据库？',function(){
                                var databaseId = $(e.target).attr("data-databaseId");
                                var appContext = self.getAppContext();
                                var servicePath = appContext.cashUtil.getData('servicePath');
                                var ropParam = appContext.cashUtil.getData('ropParam');
                                $.ajax({
                                    type:"POST",
                                    url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.database.start&databaseId="+databaseId,
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
                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.database.testByList&databaseId="+databaseId,
                                success:function(ajax){
                                	if(ajax.code=="1"){
                                		fh.alert("数据库连接测试成功");
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
							var keyword = this.$el.find(".SID-keyword").val();
							this.$el.find(".SID-keyword").val(keyword.trim());
							this.holdList();
						},
						clearSearch : function(){
							this.$el.find(".SID-keyword").val("");
						},
						_openAddDialog : function(){
							var data = {
									title:"新增数据库",
									op:"新增"
							}
							this.views.AddView = new AddView();
							this.views.AddView.render(this,data);
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
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.database.list" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"数据库类型","sWidth":"10%","mDataProp":"dbType","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								var str = "";
								if(val == 'postgresql')
									str = '<img src="images/icon/postgres.png" style="vertical-align: middle"/><span style="padding:0 5px;vertical-align: middle">PostgreSQL</span>';
								if(val == 'oracle')
									str = '<img src="images/icon/oracle.png" style="vertical-align: middle"/><span style="padding:0 5px;vertical-align: middle">Oracle</span>';
								return str;
							}},
							{"sTitle":"地址","sWidth":"15%","mDataProp":"host","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"端口","sWidth":"10%","mDataProp":"port","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"SID/数据库","sWidth":"15%","mDataProp":"sid/dbname","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(o.aData.dbType == '<img src="images/icon/postgres.png" style="vertical-align: middle"/><span style="padding:0 5px;vertical-align: middle">PostgreSQL</span>')
									str = o.aData.dbName;
								if(o.aData.dbType == '<img src="images/icon/oracle.png" style="vertical-align: middle"/><span style="padding:0 5px;vertical-align: middle">Oracle</span>')
									str = o.aData.sid;
								return str;
							}},
							{"sTitle":"用户名","sWidth":"15%","mDataProp":"userName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"说明","sWidth":"20%","mDataProp":"remarks","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								//return val.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"状态","sWidth":"8%","mDataProp":"enabled","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var str = "";
								var test = o.aData.dbType;
								var test1 = o.aData.sid;
								var test2 = o.aData.dbName;
								if(val == 1)
									str="<span>可用</span>";
								if(val == 0)
									str="<span style=\"color:red\">禁用</span>";
								return str;
							}},
							{"sTitle":"分配应用数","sWidth":"8%","mDataProp":"appnums","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作","sWidth":"8%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(o.aData.enabled =='<span>可用</span>')
									str += '<a href="javascript:void(0)" class="SID-database-stop" data-databaseId="'+o.aData.id+'"><span class="fhicon-delete"></span>禁用</a>';
								if(o.aData.enabled =='<span style=\"color:red\">禁用</span>')
									str += '<a href="javascript:void(0)" class="SID-database-start" data-databaseId="'+o.aData.id+'"><span class="fhicon-active"></span>启用</a>';
								str += '<a href="javascript:void(0)" class="SID-database-edit" data-databaseId="'+o.aData.id+'"><span class="fhicon-pencil"></span>修改</a>' +
									'<a href="javascript:void(0)" class="SID-database-test" data-databaseId="'+o.aData.id+'"><span class="fhicon-eye"></span>测试</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.databases ? data.databases : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return HoldSnippetView;
		});
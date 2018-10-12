define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/routemanage/routeManageTemplate.html'
		  ,'views/routemanage/addex-snippet-view','views/routemanage/addreg-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination','views/meeting/meet-detail-snippet-view'],
		function($, CommunicationBaseView,Template,AddExView,AddRegView,datatableUtil,datatableLnpagination,MeetingDetailView) {
			var treeObj; 
			var timeTable = new Array();
			var HoldSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-addEx-btn': '_onClickAddEx',
							'click .SID-addReg-btn': '_onClickAddReg',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-route-start':'_onClickRouteStart',
							'click .SID-route-stop':'_onClickRouteStop',
							'click .SID-route-edit':'_onClickRouteEdit',
							'click .SID-meet-apply':'_onClickMeetingApply',
							'click .SID-meet-edit':'_onClickMeetingEdit'
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
						_onClickRouteStart:function(e){
							var self = this;
							fh.confirm('启用此路由？',function(){
	                            var routeId = $(e.target).attr("data-routeId");
	                            var appContext = self.getAppContext();
	                            var servicePath = appContext.cashUtil.getData('servicePath');
	                            var ropParam = appContext.cashUtil.getData('ropParam');
	                            $.ajax({
	                                type:"POST",
	                                url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.route.enable&routeId="+routeId,
	                                success:function(ajax){
	                                	self.holdList();
	                                }
	                            });
							});
                        },
                        _onClickRouteStop:function(e){
                        	var self = this;
                        	fh.confirm('禁用此路由？',function(){
    							var routeId = $(e.target).attr("data-routeId");
    							var appContext = self.getAppContext();
    							var servicePath = appContext.cashUtil.getData('servicePath');
    							var ropParam = appContext.cashUtil.getData('ropParam');
    							$.ajax({
    								type:"POST",
    								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.route.disable&routeId="+routeId,
    								success:function(ajax){
    									self.holdList();
    								}
    							});
							});
						},
						_onClickRouteEdit:function(e){
							var self = this;
							var routeId = $(e.target).attr("data-routeId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.route.detail&routeId="+routeId,
								success:function(ajax){
									if(ajax.routeDetail.url == undefined){
										self.views.AddRegView = new AddRegView();
										ajax.title = "注册路由详情";
										self.views.AddRegView.render(self,ajax);
									}else{
                                        self.views.AddExView = new AddExView();
                                        ajax.title = "外部路由详情";
                                        self.views.AddExView.render(self,ajax);
									}
								}
							});
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
						_onClickMeetingDetail:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.detailForWeb&meetingId="+meetingId,
								success:function(ajax){
									self.views.meetingDetailView = new MeetingDetailView();
									self.views.meetingDetailView.render(ajax);
								}
							});
						},
						_onClickDeleteMeeting:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							fh.confirm('删除此会议？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.delete&meetingId="+meetingId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("删除会议成功！",false,function(){
												self.holdList();
											});
										}else{
											fh.alert("删除会议失败！",false,null);
										}
									},
									error:function(){
										fh.alert("删除会议失败！");
									}
								});
							});
						},
						_onClickMeetingCancel:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							fh.confirm('取消此会议？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.cancel&meetingId="+meetingId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("取消会议成功！",false,function(){
												self.holdList();
											});
										}else{
											fh.alert("取消会议失败！",false,null);
										}
									},
									error:function(){
										fh.alert("取消会议失败！");
									}
								});
							});
						},
						_onClickMeetingApply:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							var localtime= new Date();
							var meetingstime;
							var meetingetime;
							for (var i = 0,length = timeTable.length;i<length;i++){
								var currentMeeting = timeTable[i];
								if (currentMeeting[0]==meetingId){
									meetingstime=currentMeeting[1];
									meetingetime=currentMeeting[2];
								}
							}
							fh.confirm('发布此会议？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								if(meetingstime< localtime){
									fh.alert("发布会议失败！开始时间已过期",false,null);
								}
								else if(meetingetime< localtime){
									fh.alert("发布会议失败！结束时间已过期",false,null);
								}
								else{
									$.ajax({
										type:"POST",
										url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.apply&meetingId="+meetingId,
										success:function(ajax){
											if(ajax.code == "1"){
												fh.alert("发布会议成功！",false,function(){
													self.holdList();
												});
											}
											else{
												fh.alert("发布会议失败！",false,null);
											}
										},
										error:function(){
											fh.alert("发布会议失败！");
										}
									});
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
						_openEditDialog : function(data){
							data.title = '编辑会议';
							data.op = '编辑';
							data.operation = 1;
							this.views.AddView = new AddView();
							this.views.AddView.render(this,data);
						},
						_onClickAddEx : function(){
							var data = {
									title:"新增外部路由",
							}
							this.views.AddExView = new AddExView();
							this.views.AddExView.render(this,data);
						},
						_onClickAddReg : function(){
							var data = {
									title:"新增注册路由",
							}
							data.services = this.services;
							this.views.AddRegView = new AddRegView();
							this.views.AddRegView.render(this,data);
						},
						_openAddDialog : function(){
							var data = {
									title:"新增会议",
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
							var self = this;
							var tableIndex = 0;
							var tableObj={};
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.route.query" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"服务名称","sWidth":"15%","mDataProp":"serviceName","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								var portalLint = o.aData.portalLink;
								val = val.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
								if(portalLint==""){
									return val;
								}else{
									return val+'<img src="images/icon/portal.png" title="该服务含有门户入口"/>';
								}
								
							}},
							{"sTitle":"服务(服务名或者URL)","sWidth":"20%","mDataProp":"url","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								var serviceId = o.aData.serviceId;
								if(val == ""){
									return serviceId;
								}else{
									return val;
								}
							}},
							{"sTitle":"路由路径","sWidth":"16%","mDataProp":"path","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"失败重试","sWidth":"8%","mDataProp":"retryable","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								if(val == true){
									return "是";
								}else{
									return "否";
								}
							}},
							{"sTitle":"是否鉴权","sWidth":"8%","mDataProp":"needAuth","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								if(val == true){
									return "是";
								}else{
									return "否";
								}
							}},
							{"sTitle":"敏感的HTTP头","sWidth":"20%","mDataProp":"sensitiveHeaders","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								var list = val;
								var resultstr = ""
								for(var i=0;i<list.length;i++){
									list[i] = list[i].replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
									if(resultstr == ""){
										resultstr += list[i];
									}else{
										resultstr += ","+list[i];
									}
								}
								return resultstr;
							}},
							{"sTitle":"服务节点数","sWidth":"9%","mDataProp":"serviceNodeNumber","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"状态","sWidth":"8%","mDataProp":"enabled","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								if(val == true){
									return "<span>可用</span>";
								}else{
									return "<span style=\"color:red\">禁用</span>";
								}
							}},
							{"sTitle":"操作","sWidth":"8%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(o.aData.enabled =='<span>可用</span>')
									str += '<a href="javascript:void(0)" class="SID-route-stop" data-routeId="'+o.aData.id+'"><span class="fhicon-delete"></span>禁用</a>';
								if(o.aData.enabled =='<span style=\"color:red\">禁用</span>')
									str += '<a href="javascript:void(0)" class="SID-route-start" data-routeId="'+o.aData.id+'"><span class="fhicon-active"></span>启用</a>';
								str += '<a href="javascript:void(0)" class="SID-route-edit" data-routeId="'+o.aData.id+'"><span class="fhicon-pencil"></span>修改</a>';
									//+ '<a href="javascript:void(0)" class="SID-route-bind" data-routeId="'+o.aData.id+'"><span class="fhicon-eye"></span>服务绑定</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.routes ? data.routes : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
					});
			return HoldSnippetView;
		});
define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/holdTemplate.html'
		  ,'views/meeting/add-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination','views/meeting/meet-detail-snippet-view'],
		function($, CommunicationBaseView,Template,AddView,datatableUtil,datatableLnpagination,MeetingDetailView) {
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
							this.holdList();
						},
						clearSearch : function(){
							this.$el.find(".SID-meetingName").val("");
							this.$el.find(".SID-address").val("");
							this.$el.find(".SID-meetingStatus").val(0);
							this.$el.find(".SID-create-beginDate").val("");
							this.$el.find(".SID-create-endDate").val("");
							this.$el.find(".SID-create-beginDate").change();
							this.$el.find(".SID-create-endDate").change();
						},
						_openEditDialog : function(data){
							data.title = '编辑会议';
							data.op = '编辑';
							data.operation = 1;
							this.views.AddView = new AddView();
							this.views.AddView.render(this,data);
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
							var tableIndex = 0;
							var tableObj={};
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.meeting.meeting.webquery&order=2&selfStatus=1" + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"会议名称","sWidth":"15%","mDataProp":"meetingName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"会议创建时间","sWidth":"16%","mDataProp":"createTimeStr","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"会议开始时间","sWidth":"16%","mDataProp":"beginTimeStr","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"会议结束时间","sWidth":"16%","mDataProp":"endTimeStr","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"会议地址","sWidth":"21%","mDataProp":"address","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"会议状态","sWidth":"9%","mDataProp":"status","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var str = "";
								if(val == 10)
									str="未发布";
								if(val == 20)
									str="未进行";
								if(val == 30)
									str="进行中";
								if(val == 40)
									str="已取消";
								if(val == 50)
									str="已结束";
								return str;
							}},
							{"sTitle":"操作","sWidth":"7%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								//储存会议时间为全局变量用于发布时验证时间
								var currentMeetingBeginTime= o.aData.beginTimeStr.replace(/-/g, "/");
								var currentMeetingEndTime= o.aData.endTimeStr.replace(/-/g, "/")
								var currentMeeting= [o.aData.meetingId, new Date(currentMeetingBeginTime), new Date(currentMeetingEndTime)];
								timeTable[tableIndex]= currentMeeting;
								tableIndex++;
								if(o.aData.status =='未进行' || o.aData.status =='进行中')
									str = '<a href="javascript:void(0)" class="SID-meet-detail" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-eye"></span>查看</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-pencil"></span>编辑</a><a href="javascript:void(0)" class="SID-meet-cancel" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-delete"></span>取消</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-trash"></span>删除</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-active"></span>发布</a>';
								if(o.aData.status =='已取消' || o.aData.status =='已结束')
									str = '<a href="javascript:void(0)" class="SID-meet-detail" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-eye"></span>查看</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-pencil"></span>编辑</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-delete"></span>取消</a><a href="javascript:void(0)" class="SID-meet-delete" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-trash"></span>删除</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-active"></span>发布</a>';
								if(o.aData.status =='未发布')	
									str = '<a href="javascript:void(0)" class="SID-meet-detail" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-eye"></span>查看</a><a href="javascript:void(0)" class="SID-meet-edit" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-pencil"></span>编辑</a><a class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-delete"></span>取消</a><a href="javascript:void(0)" class="SID-meet-delete" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-trash"></span>删除</a><a href="javascript:void(0)" class="SID-meet-apply" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-active"></span>发布</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.meeting ? data.meeting : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return HoldSnippetView;
		});
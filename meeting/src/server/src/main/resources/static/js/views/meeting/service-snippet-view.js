define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/serviceTemplate.html','util/datatableUtil','datatable_lnpagination'
		  ,'views/meeting/meet-detail-snippet-view'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination,MeetingDetailView) {
			var treeObj;
			var ServiceSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search' : 'search',
							'click .SID-meeting-delete':'_onClickDeleteMeeting',
							'click .SID-meeting-detail':'_onClickMeetingDetail',
							'click .SID-clearSearch' : 'clearSearch'
						},
						initialize : function() {
							this.childView = {};
							this.datatable = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							this.meetingList();
							return this;
						},
						refresh : function() {
						},
						refreshTable : function() {
							this.meetingList();
						},
						destroyBusinessViews : function(){
							$.each(this.childView, function(index, view) {
								 view.destroy();
							});		
							this.childView = {};
						},		
						destroy : function() {
							this.destroyBusinessViews()
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						subscribeEvents : function() {
						},
						setContentHTML : function (){
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							this.uiRanderUtil.randerJQueryUI_DateRange(this,"#from","#to","yy-mm-dd");
							this.meetingList();
						},
						search : function(){
							this.meetingList(this,'search');
						},
						clearSearch : function(){
							this.$el.find(".SID-meetingName").val("");
							this.$el.find(".SID-meetingHolder").val("");
							this.$el.find(".SID-meetingStatus").val(0);
							this.$el.find(".SID-beginDate").val("");
							this.$el.find(".SID-endDate").val("");
							this.$el.find(".SID-beginDate").change();
							this.$el.find(".SID-endDate").change();
						},
						initSearchParam:function(){
							var param = "";
							param = $("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							return param;
						},
						_onClickDeleteMeeting:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							fh.confirm('确定删除会议吗？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.delete&meetingId="+meetingId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("删除会议成功！",false,function(){
												self.meetingList();
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
									self.childView.meetingDetailView = new MeetingDetailView();
									self.childView.meetingDetailView.render(ajax);
								}
							});
						},
						meetingList : function(that,operate){
							var tableObj={};
							
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.meeting.meeting.webquery&order=1&selfStatus=3" + param;
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							tableObj.aoColumns=[
							{"sTitle":"会议名称","sWidth":"19%","mDataProp":"meetingName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"会议召开人","sWidth":"9%","mDataProp":"holderName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"会议开始时间","sWidth":"16%","mDataProp":"beginTimeStr","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"会议结束时间","sWidth":"16%","mDataProp":"endTimeStr","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"会议地址","sWidth":"24%","mDataProp":"address","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"会议状态","sWidth":"9%","mDataProp":"status","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var str = "";
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
								var str = '';
								if(o.aData.status =='未进行' || o.aData.status =='进行中')
									str = '<a href="javascript:void(0)" class="grey" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-trash"></span>删除</a><a href="javascript:void(0)" class="SID-meeting-detail" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-eye"></span>查看</a>';
								else	
									str = '<a href="javascript:void(0)" class="SID-meeting-delete" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-trash"></span>删除</a><a href="javascript:void(0)" class="SID-meeting-detail" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-eye"></span>查看</a>';
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
			return ServiceSnippetView;
		});
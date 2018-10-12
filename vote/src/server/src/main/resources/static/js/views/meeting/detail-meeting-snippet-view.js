define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/detailMeetingTemplate.html','util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var DetailMeeting = CommunicationBaseView
					.extend({
						events : {
							'click .SID-attach':'_onClickUrl'
						},
						initialize : function() {
							this.datatable={};
							this.dataTip = [];
							this.data;
						},
						render : function(data) {
							this.data = data;
							this._setUpContent(data);
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						_onClickUrl:function(e){
							var url = $(e.target).attr("data-url");
							location.href = url;
						},
						_initTable : function(){
							var tableObj={};
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.meeting.meeting.queryAttach&meetingId=" +self.data.meeting.meetingId ;
							tableObj.tbID = "grid-attach";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"文档名称","sWidth":"10%","mDataProp":"fileName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"文档类型","sWidth":"8%","mDataProp":"contentType","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"文档公开权限","sWidth":"8%","mDataProp":"privilege","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(val == 1)
									str = "查看";
								if(val == 3)
									str = "下载";
								return str;
							}},
							{"sTitle":"上传时间","sWidth":"15%","mDataProp":"uploadTimeStr","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作","sWidth":"7%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '';
								if(o.aData.privilege == '查看')
									str = '<a href="javascript:void(0)" class="SID-attach" data-url="'+o.aData.viewUrl+'"><span class="fhicon-eye"></span>查看</a><a href="javascript:void(0)" class="grey" data-url="'+o.aData.downloadUrl+'"><span class="fhicon-set2"></span>下载</a>';
								else if(o.aData.privilege == '下载')
									str = '<a href="javascript:void(0)" class="grey" data-url="'+o.aData.viewUrl+'"><span class="fhicon-eye"></span>查看</a><a href="javascript:void(0)" class="SID-attach" data-url="'+o.aData.downloadUrl+'"><span class="fhicon-set2"></span>下载</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.attachList ? data.attachList : ''
								};
								self.dataTip = data.total ? data.total : 0
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_setUpContent: function(data) { 
							var self = this;
							var template = _.template(Template);
							var html = template({
								'meeting':data.meeting,
								'agendasInfo':data.agendasInfo,
								'remarks':data.remarksList
							})
							this.$el.append(html);
							this._initTable();
						},
					});
			return DetailMeeting;
		});
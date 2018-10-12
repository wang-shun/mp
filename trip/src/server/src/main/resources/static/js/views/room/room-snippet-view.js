define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/room/roomTemplate.html',
		  'views/room/room-view-snippet-view','views/room/user-snippet-view','util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,RoomViewSnippetView,UserSnippetView,datatableUtil,datatableLnpagination) {
			var treeObj;
			var RoomSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-meet-add': 'addInit',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-meet-detail': 'roomDetail',
							'click .SID-meet-delete': 'roomDelete',
							'click .SID-user-detail':'userDetail'
						},
						initialize : function() {
							this.chaildView = {};
							this.datatable = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							this.roomList();
							return this;
						},
						refresh : function() {
						},
						refreshTable : function() {
							this.roomList();
						},
						destroyBusinessViews : function(){
							$.each(this.chaildView, function(index, view) {
								 view.destroy();
							});		
							this.chaildView = {};
						},		
						destroy : function() {
							this.destroyBusinessViews()
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						subscribeEvents : function() {
							var self = this;
							this.eventHub.subscribeEvent('REFRESH_MEETINGLIST', function(operate) {
								self.meetingList(self, operate);
							});
						},
						setContentHTML : function (){
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);

							this.uiRanderUtil.randerJQueryUI_DateRange(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
							//this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","month");
						},
						search : function(){
							this.roomList(this,'search');
						},
						clearSearch : function(){
							this.$el.find(".SID-userName").val("");
							this.$el.find(".SID-beginDate").val("");
							this.$el.find(".SID-endDate").val("");
						},
						roomDetail : function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.feedback.detail&feedbackId="+roomId,
								success:function(ajax){
									self.chaildView.roomViewSnippetView = new RoomViewSnippetView();
									self.chaildView.roomViewSnippetView.render(ajax,self);
								}
							});
						},
						userDetail : function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.feedback.user&feedbackId="+roomId,
								success:function(ajax){
									self.chaildView.userSnippetView = new UserSnippetView();
									self.chaildView.userSnippetView.render(ajax,self);
								}
							});
						},
						roomDelete:function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							fh.confirm('确定删除吗？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.feedback.delete&feedbackId="+roomId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("删除成功！",false,function(){
												self.roomList();
											});
										}else{
											fh.alert(ajax.message,false,function(){
												self.roomList();
											});
										}
									},
									error:function(){
										fh.alert("删除失败！");
									}
								});
							});
						},
						initSearchParam:function(){
							var param = "";
							var userName = this.$el.find(".SID-userName").val().trim();
							var beginDate = this.$el.find(".SID-beginDate").val();
							var endDate = this.$el.find(".SID-endDate").val();
							if (userName!="") {
								param += "&userName="+userName;
							}
							if (beginDate!="") {
								param += "&beginDate="+beginDate;
							}
							if (endDate!="") {
								param += "&endDate="+endDate;
							}
							return param;
						},
						roomList : function(that,operate){
							var tableObj={};
							
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.feedback.query" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"姓名","sWidth":"80px","mDataProp":"userName","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-user-detail" data-roomId="'+o.aData.id+'">'+val+'</a>';
								return str;
							}},
							{"sTitle":"设备型号","sWidth":"80px","mDataProp":"deviceName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"反馈应用","sWidth":"80px","mDataProp":"appName","sDefaultContent": "" ,"sClass":"center"},
							{"sTitle":"反馈内容","sWidth":"20%","mDataProp":"feedback","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"反馈时间","sWidth":"150px","mDataProp":"submitTime","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return o.aData.submitTimeStr;
							}},
							{"sTitle":"联系方式","sWidth":"80px","mDataProp":"contack","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"问题状态","sWidth":"60px","mDataProp":"confirm","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								if (val=="0") {
									return "待处理";
								} else if (val=="1") {
									return "处理中";
								} else if (val=="2") {
									return "已处理";
								}
							}},
							{"sTitle":"操作","sWidth":"60px","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-meet-detail" data-roomId="'+o.aData.id+'"><span class="fhicon-eye"></span>查看</a><a href="javascript:void(0)" class="SID-meet-delete" data-roomId="'+o.aData.id+'"><span class="fhicon-delete"></span>删除</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.feedbackList ? data.feedbackList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						}
			
					});
			return RoomSnippetView;
		});
define(
		[ 'jquery', 'views/communication-base-view'
		  ,'text!../../templates/statistics/userRecordTemplate.html'
		  ,'views/meet/answerDetailView'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,DetailView
				,datatableUtil,datatableLnpagination) {
			var StatisticsSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search' : '_onClickSearch',
							'click .SID-clear' : '_onClickClear',
							'click .SID-personop-peview' : '_onClickPersonPeviewBtn',
							'click .userRecord-export':'_onClickExport',
							'click .SID-data-user-export':'exportUser'
						},
						initialize : function() {
							this.childrenView={};
							this.datatable={};
							this.surveyId = "";
							this.orgId = "";
						},
						render : function(surveyId) {
							this.surveyId = surveyId;
							this._setUpContent();
							return this;
						},
						refresh : function() {
						},
						destroyViews : function(){
							$.each(this.childrenView, function(index, view) {
								 view.destroy();
							});		
							this.childrenView = {};
						},	
						destroy : function() {
							this.destroyViews();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						_onClickExport:function(){
							var self = this;
							if(self.dataTip == 0){
								fh.alert("当前无数据导出！");
								return;
							}
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropFileParam = appContext.cashUtil.getData('ropFileParam');
							var beginDate = this.$el.find(".SID-beginDate").val();
							var endDate = this.$el.find(".SID-endDate").val();
							if (beginDate == "" || endDate == "") {
								return;
							}
				            var param = this._initParam();
							var url = servicePath + "?" + ropFileParam + "&method=mapps.meetingroom.oplog.export" + param+"&pageFlag=2";
							location.href = url;
						},
						_onClickClear:function(){
							this.$el.find(".SID-userName").val("");
							this.$el.find(".SID-depName").val("");
							this.$el.find(".SID-opType").val("");
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","week");
						},
						_onClickSearch:function(){this._initTable();},
						_initTable : function(){
							var tableObj={};
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath;
							var url = servicePath + "?" + ropParam + "&method=mapps.survey.answerPeople&surveyId=" + this.surveyId;
							
							tableObj.url = url;
							tableObj.tbID = "grid-table2";
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"答卷者","sWidth":"30%","mDataProp":"personName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"提交时间","sWidth":"30%","mDataProp":"submitTime","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								return o.aData.submitTimeStr;
							}},
							{"sTitle":"答卷时长","sWidth":"30%","mDataProp":"durationStr","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"操作","sWidth":"10%","mDataProp":"result","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-personop-peview" data-roomId="'+self.surveyId+'" data-personId="'+o.aData.personId+'">查看</a>';
								return str;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								self.orgId = data.orgId;
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.answerPeople ? data.answerPeople : ''
								};
								self.dataTip = data.total ? data.total : 0
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_onClickPersonPeviewBtn:function(e){
				        	var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							var personId =obj.attr("data-personId");
							if(roomId == "" || personId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.survey.webdetail&surveyId="+roomId+"&personId="+personId,
				                success:function(data){
				                	console.log(data);
				                	if (data.code == "1") {
										self.childrenView.detailView = new DetailView();
										self.childrenView.detailView.render(data);
				                	}
				                }
							});
				        },
						_setUpContent: function() { 
							var self = this;
							var html = _.template(Template);
							this.$el.append(html);
							this._initTable();
						},
						exportUser : function()
						{
							var url = "exportCVS?surveyId="+this.surveyId+"&orgId="+this.orgId;
							window.location.href = url;
						}
					});
			return StatisticsSnippetView;
		});
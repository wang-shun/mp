define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/statistics/userRecordTemplate.html','util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var StatisticsSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-day' : '_onClickDay',
							'click .SID-week' : '_onClickWeek',
							'click .SID-month' : '_onClickMonth',
							'click .SID-custom' : '_onClickCustom',
							'click .SID-search' : '_onClickSearch',
							'click .SID-clear' : '_onClickClear',
							'click .userRecord-export':'_onClickExport'
						},
						initialize : function() {
							this.datatable={};
							this.dataTip = [];
						},
						render : function() {
							this._setUpContent();
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
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
							this.$el.find(".SID-week").click();
						},
						_onClickSearch:function(){
							var beginDate = this.$el.find(".SID-beginDate").val();
							var endDate = this.$el.find(".SID-endDate").val();
							if (beginDate == "" || endDate == "") {
								fh.alert("请选择查询日期");
								return;
							}
							if(beginDate>endDate){
								fh.alert("开始日期不能大于结束日期");
								return;
							}
							if(new Date(Date.parse(endDate)) > new Date()){
								fh.alert("结束日期不能大于当前日期");
								return;
							}
							this._initTable();
						},
						_initParam:function(){
							var userName = this.$el.find(".SID-userName").val();
							var depName = this.$el.find(".SID-depName").val();
							var op = this.$el.find(".SID-opType").val();
							var beginDate = this.$el.find(".SID-beginDate").val();
							var endDate = this.$el.find(".SID-endDate").val();
							var param = "";
							if (userName != "") 
								param += "&userName=" + encodeURIComponent(userName);
							if (depName != "") 
								param += "&depName=" + encodeURIComponent(depName);
							if (op != "") 
								param += "&op=" + encodeURIComponent(op);
							if (beginDate != "" && endDate != "")
					            param += "&statBeginTime="+beginDate+"&statEndTime="+endDate;
					        return param;
						},
						_initTable : function(){
							var tableObj={};
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath;

							var beginDate = this.$el.find(".SID-beginDate").val();
							var endDate = this.$el.find(".SID-endDate").val();
							if (beginDate == "" || endDate == "") {
								return;
							}
							if(beginDate>endDate){
								fh.alert("开始日期不能大于结束日期");
								return;
							}
							if(new Date(Date.parse(endDate)) > new Date()){
								fh.alert("结束日期不能大于当前日期");
								return;
							}
				            var param = this._initParam();
							var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.oplog.query&pageFlag=1" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"预定人","sWidth":"10%","mDataProp":"userName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"所属部门","sWidth":"10%","mDataProp":"depName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"操作类型","sWidth":"10%","mDataProp":"op","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作时间","sWidth":"18%","mDataProp":"opTime","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"会议室名称","sWidth":"15%","mDataProp":"roomName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"预定时间","sWidth":"18%","mDataProp":"reservedTime","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"会议名称","sWidth":"15%","mDataProp":"meetingName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"操作结果","sWidth":"10%","mDataProp":"result","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								return val == "1" ? "成功" : "失败";
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.opLogList ? data.opLogList : ''
								};
								self.dataTip = data.total ? data.total : 0
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_setUpContent: function() { 
							var self = this;
							var html = _.template(Template);
							this.$el.append(html);
							this.$el.find('.SID-week').attr("checked","true");  
							this.uiRanderUtil.randerJQueryUI_DateRange(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","week");
							this._initTable();
						},
						_onClickDay:function(){
							this.$el.find('#ID-date-input').hide();
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","day");
						},
						_onClickWeek:function(){
							this.$el.find('#ID-date-input').hide();
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","week");
						},
						_onClickMonth:function(){
							this.$el.find('#ID-date-input').hide();
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","month");
						},
						_onClickCustom:function(){
							this.$el.find('#ID-date-input').show();
							$(".SID-beginDate").datepicker( "setDate", "" );
							$(".SID-endDate").datepicker( "setDate", "" );
						}
					});
			return StatisticsSnippetView;
		});
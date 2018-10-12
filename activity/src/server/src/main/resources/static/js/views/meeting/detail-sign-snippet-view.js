define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/detailSignTemplate.html','util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var DetailSign = CommunicationBaseView
					.extend({
						events : {
							'click .SID-sign-detail':'_onClickSign',
							'click .SID-sign-export':'_onClickExport'
						},
						initialize : function() {
							this.childView={};
							this.datatable={};
							this.dataTip = [];
							this.data;
							this.colSize;
							this.exportFlag=false;
						},
						render : function(data) {
							this.data = data;
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
							if (self.exportFlag) {
								var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropFileParam = appContext.cashUtil.getData('ropFileParam');
								var param = "&meetingId="+self.data.meeting.meetingId+"";
								var url = servicePath + "?" + ropFileParam + "&method=mapps.meeting.meeting.exportSignInfo" + param;
								location.href=url;
							}
						},
						_initTable : function(){
							var tableObj={};
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.meeting.meeting.querySign&meetingId=" +self.data.meeting.meetingId ;
							tableObj.tbID = "grid-sign";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							$.ajax({
								type:"POST",
								async:false,
								url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.querySequ&meetingId=" +self.data.meeting.meetingId,
								success:function(ajax){
									self.colSize = ajax.size;
								}
							});
							tableObj.aoColumns=[
							{"sTitle":"姓名","sWidth":"80px","mDataProp":"personName","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"部门","sWidth":"100px","mDataProp":"deptName","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"手机号","sWidth":"100px","mDataProp":"phone","sDefaultContent": "" ,"sClass":"center","bSortable":false}
							];
							for(var i = 1;i<=self.colSize;i++){
								var json = {"sTitle":"第"+i+"次签到时间","sWidth":"150px","mDataProp":"sequ"+i+"","sDefaultContent": "" ,"sClass":"center"}
								tableObj.aoColumns.push(json);
							}
							var param="";
							var jsonProc = function(data) {
								var aData = data.signRecordList;
								for(var i=0;i<aData.length;i++){
									var array = aData[i].signInfo;
									$.each(array, function (n, value) {
										aData[i][n] = value;
									}); 
								}
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.signRecordList ? data.signRecordList : ''
								};
								if (data.total ? data.total : 0 > 0) {
									self.exportFlag = true;
								} else {
									self.$el.find(".SID-sign-export").hide();
								}
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_setUpContent: function() { 
							var self = this;
							var html = _.template(Template);
							this.$el.append(html);
							this._initTable();
						},
					});
			return DetailSign;
		});
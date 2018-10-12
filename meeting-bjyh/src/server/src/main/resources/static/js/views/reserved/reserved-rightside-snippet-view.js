define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/reserved/rightTemplate.html','util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,TemplateRight,datatableUtil,datatableLnpagination) {
			var RightsideSnippetView = CommunicationBaseView
			.extend({
				events : {
					'click .SID-search' : '_onClickSearch',
					'click .SID-clearSearch' : '_onClickClearSearch',
					'click .SID-cancelReserved' : '_onClickCancelReserved',
					'click .SID-overReserved' : '_onClickOverReserved',
					'click .SID-delReserved' : '_onClickDelReserved'
				},
				initialize : function() {
					this.parentObj;
					this.datatable = {};
					this.roomId = "";
				},
				render : function(parentObj,roomId) {
					this.parentObj = parentObj;
					this.roomId = roomId;
					this.setContentHTML();
					this.reservedList()
					return this;
				},
				refresh : function() {
				},
				destroy : function() {
					this.undelegateEvents();
					this.unbind();
					this.$el.empty();
				},
				setContentHTML : function (){
					var template = _.template(TemplateRight);
					var html = template({});
					this.$el.append(html);
					$(".SID-date").datepicker({});
				},
				_onClickSearch : function(){
					this.reservedList();
				},
				_onClickClearSearch : function(){
					this.$el.find(".SID-displayName").val("");
					this.$el.find(".SID-depName").val("");
					this.$el.find(".SID-date").val("");
				},
				_onClickCancelReserved:function(e){
					var self = this;
					var obj = $(e.target);
					var reservedId = obj.attr("data-reservedId");
					if (reservedId == "") 
						return;
					fh.confirm('确定取消预定吗？', function() {
						var appContext = self.getAppContext();
						var servicePath = appContext.cashUtil.getData('servicePath');
						var ropParam = appContext.cashUtil.getData('ropParamNoV');
						var url = servicePath + "?" + ropParam + "&v=2.0&method=mapps.meetingroom.reserved.delete&reservedId="+reservedId+"&operationType=1";
						self.showLoading();
						$.ajax({
							type : "POST",
							url : url,
							data : "",
							success : function(data) {
								self.hideLoading();
								if (data.code == "1") {
									fh.alert("取消预定成功！",false,function(){
										self.reservedList();
									});
								} else if (data.code == "300010" || data.code == "300013") {
									fh.alert(data.message,false,function(){
										self.reservedList();
									});
								} else {
									fh.alert("取消预定失败！");
								}
							},
							error : function() {
								self.hideLoading();
								fh.alert("取消预定失败！");
							}
						});
					});
				},
				_onClickOverReserved:function(e){
					var self = this;
					var obj = $(e.target);
					var reservedId = obj.attr("data-reservedId");
					if (reservedId == "") 
						return;
					fh.confirm('确定结束预定吗？', function() {
						var appContext = self.getAppContext();
						var servicePath = appContext.cashUtil.getData('servicePath');
						var ropParam = appContext.cashUtil.getData('ropParamNoV');
						var url = servicePath + "?" + ropParam + "&v=2.0&method=mapps.meetingroom.reserved.delete&reservedId="+reservedId+"&operationType=2";
						self.showLoading();
						$.ajax({
							type : "POST",
							url : url,
							data : "",
							success : function(data) {
								self.hideLoading();
								if (data.code == "1") {
									fh.alert("结束预定成功！",false,function(){
										self.reservedList();
									});
								} else if (data.code == "300010" || data.code == "300013") {
									fh.alert(data.message,false,function(){
										self.reservedList();
									});
								} else {
									fh.alert("结束预定失败！");
								}
							},
							error : function() {
								self.hideLoading();
								fh.alert("结束预定失败！");
							}
						});
					});
				},
				_onClickDelReserved:function(e){
					var self = this;
					var obj = $(e.target);
					var reservedId = obj.attr("data-reservedId");
					if (reservedId == "") 
						return;
					var status = obj.attr("data-status");
					var statusname = "";
					if (status == "3") {
						statusname = "已结束的会议";
					} else if (status == "4") {
						statusname = "已取消的会议";
					} else if (status == "r") {
						statusname = "不批准的会议";
					}
					fh.confirm('确定删除'+statusname+'预定吗？', function() {
						var appContext = self.getAppContext();
						var servicePath = appContext.cashUtil.getData('servicePath');
						var ropParam = appContext.cashUtil.getData('ropParamNoV');
						var url = servicePath + "?" + ropParam + "&v=2.0&method=mapps.meetingroom.reserved.delete&reservedId="+reservedId+"&operationType=3";
						self.showLoading();
						$.ajax({
							type : "POST",
							url : url,
							data : "",
							success : function(data) {
								self.hideLoading();
								if (data.code == "1") {
									fh.alert("删除预定成功！",false,function(){
										self.reservedList();
									});
								} else if (data.code == "300010" || data.code == "300013") {
									fh.alert(data.message,false,function(){
										self.reservedList();
									});
								} else {
									fh.alert("删除预定失败！");
								}
							},
							error : function() {
								self.hideLoading();
								fh.alert("删除预定失败！");
							}
						});
					});
				},
				_initSearchParam:function(){
					var param = "";
					var displayName = this.$el.find(".SID-displayName").val();
					var depName = this.$el.find(".SID-depName").val();
					var date = this.$el.find(".SID-date").val();
					if (displayName != "") 
						param += "&displayName=" + encodeURIComponent(displayName);
					if (depName != "") 
						param += "&depName=" + encodeURIComponent(depName);
					if (date != "") 
						param += "&reservedStartDate=" + date + "&reservedEndDate=" + date;
					return param;
				},
				reservedList : function(){
					var tableObj={};
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var param = this._initSearchParam();
					var url = servicePath + "?" + ropParam + "&v=2.0&method=mapps.meetingroom.reserved.query&status=1,2,3,4,a,r&order=3&roomId=" + this.roomId + param;
					tableObj.url = url;
					//控制是否可分页
					tableObj.bPaginate = true;
					// tableObj.aaSorting = [[2,'asc']];
					tableObj.aoColumns=[
					{"sTitle":"预定人","sWidth":"10%","mDataProp":"displayName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
					{"sTitle":"所属部门","sWidth":"20%","mDataProp":"depName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
					//{"sTitle":"预定日期","sWidth":"15%","mDataProp":"reservedDate","sDefaultContent": "" ,"sClass":"center"},
					{"sTitle":"开始时间","sWidth":"150px","mDataProp":"reservedStartTime","sDefaultContent": "","bSortable":false ,"sClass":"center"},
					{"sTitle":"结束时间","sWidth":"150px","mDataProp":"reservedEndTime","sDefaultContent": "" ,"bSortable":false,"sClass":"center"},
					{"sTitle":"会议名称","sWidth":"20%","mDataProp":"meetingName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
					{"sTitle":"预定状态","sWidth":"80px","mDataProp":"statusStr","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
						if (o.aData.status == "1") {
							return "准备中";
						} else if (o.aData.status == "2") {
							return "使用中";
						} else if (o.aData.status == "3") {
							return "已结束";
						} else if (o.aData.status == "4") {
							return "已取消";
						} else if (o.aData.status == "0") {
							return "已删除";
						} else if (o.aData.status == "a") {
							return "待审批";
						} else if (o.aData.status == "r") {
							return "不批准";
						}
						return "";
					}},
					{"sTitle":"操作","sWidth":"80px","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
						var str = "";
						if (o.aData.status == "1" || o.aData.status == "a") {
							str = '<a href="javascript:void(0)" class="SID-cancelReserved" data-reservedId="'+o.aData.reservedId+'"><span class="fhicon-eye"></span>取消</a>';
						} else if (o.aData.status == "2") {
							str = '<a href="javascript:void(0)" class="SID-overReserved" data-reservedId="'+o.aData.reservedId+'"><span class="fhicon-eye"></span>结束</a>';
						} else if (o.aData.status == "3" || o.aData.status == "4" || o.aData.status == "r") {
							str = '<a href="javascript:void(0)" class="SID-delReserved" data-reservedId="'+o.aData.reservedId+'" data-status="'+o.aData.status+'"><span class="fhicon-eye"></span>删除</a>';
						}
						var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
						return retBox;
					}}
					];
					var param="";
					var jsonProc = function(data) {
						var jsonData = {
								"iTotalDisplayRecords" : data.total ? data.total : 0,
										"iTotalRecords" : data.total ? data.total : 0,
												"aaData" : data.reserved ? data.reserved : ''
						};
						return jsonData;
					}
					datatable=datatableUtil(tableObj,param,jsonProc);
				},
				refreshTable : function() {
					this.reservedList();
				}
	
			});
			return RightsideSnippetView;
		});
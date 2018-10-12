define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/statistics/roomStatTemplate.html','util/datatableUtil','datatable_lnpagination', 'ajaxfileupload', 'echarts'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var echarts = require('echarts');
			var RoomStatSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search' : '_onClickSearch',
							'click .SID-day' : '_onClickDay',
							'click .SID-week' : '_onClickWeek',
							'click .SID-month' : '_onClickMonth',
							'click .SID-export' : '_onClickExport'
						},
						initialize : function() {
							this.datatable={};
							this.myChart={};
							this.option={};
							this.dataTip=[];
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
							if(self.dataTip.length ==0){
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
							var param = "&statBeginTime="+beginDate+"&statEndTime="+endDate+"&pageFlag=2";
							var url = servicePath + "?" + ropFileParam + "&method=mapps.meetingroom.stat.export" + param;
							location.href=url;
						},
						_onClickSearch:function(){
							//this.myChart = echarts.init(document.getElementById('analysis-Chart'));
							this._getOption();
							this._initTable();
						},
						_chart:function(){
							this.myChart = echarts.init(document.getElementById('analysis-Chart'));
							this._getOption();
						},
						_getOption:function(){
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');

							var beginDate = this.$el.find(".SID-beginDate").val();
							var endDate = this.$el.find(".SID-endDate").val();
							if (beginDate == "" || endDate == "") {
								return;
							}
							if(beginDate>endDate){
								fh.alert("开始日期不能大于结束日期");
								return;
							}
//							if(new Date(Date.parse(endDate)) > new Date()){
//								fh.alert("结束日期不能大于当前日期");
//								return;
//							}
				            var param = "&statBeginTime="+beginDate+"&statEndTime="+endDate+"&pageFlag=2";
							var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.stat.query" + param;
							$.ajax({
								type : "POST",
								url : url,
								data : "",
								success : function(data) {
									if (data.code == "1") {
										self._setOption(data.statList);
										self.dataTip = data.statList;
									} else {
										fh.alert(data.code + ":" + data.message);
									}
								},
								error : function(){
									fh.alert("数据获取失败");
								}
							});
						},
						_setOption:function(data){
							var self = this;
							var show = false;
							var end = 100;
							if (data.length > 10) {
								show = true;
								end = Math.round(10/data.length*100);
							}
							// 指定图表的配置项和数据
							this.option = {
								    tooltip : {
								        trigger: 'axis'
								    },
								    toolbox: {
								        show : true,
								        showTitle : false,
								        feature : {
								            magicType: {show: true, type: ['line', 'bar']},
								            saveAsImage : {show: true}
								        }
								    },
								    calculable : true,
								    legend: {
								        data:['预定次数','预定时长']
								    },
								    dataZoom : {
								        show : show,
								        realtime: true,
								        start : 0,
								        end : end
								    },
								    xAxis : [
								        {
								            type : 'category',
								            data : self._toArray(data, "roomName")
								        }
								    ],
								    yAxis : [
								        {
								            type : 'value',
								            name : '预定次数',
								            axisLabel : {
								                formatter: '{value}'
								            }
								        },
								        {
								            type : 'value',
								            name : '预定时长',
								            axisLabel : {
								                formatter: '{value} h'
								            }
								        }
								    ],
								    series : [

								        {
								            name:'预定次数',
								            type:'bar',
								            data:self._toArray(data, "reservedNum")
								        },
								        {
								            name:'预定时长',
								            type:'bar',
								            yAxisIndex: 1,
								            data:self._toArray(data, "durationSum")
								        }
								    ]
								};
					        // 使用刚指定的配置项和数据显示图表。
					        this.myChart.setOption(this.option);
						},
						_toArray:function(jsonArray, property) {
						    var result = new Array();
						    $.each(jsonArray || [],function(index,d){
						        result[index] = jsonArray[index][property];
						    });
						    return result;
						},
						_initTable : function(){
							var tableObj={};

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
//							if(new Date(Date.parse(endDate)) > new Date()){
//								fh.alert("结束日期不能大于当前日期");
//								return;
//							}
				            var param = "&statBeginTime="+beginDate+"&statEndTime="+endDate+"&pageFlag=1";
							var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.stat.query" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"会议室名称","sWidth":"40%","mDataProp":"roomName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"预定次数","sWidth":"15%","mDataProp":"reservedNum","sDefaultContent": "" ,"sClass":"right","bSortable":true},
							{"sTitle":"预定总时长","sWidth":"15%","mDataProp":"durationSum","sDefaultContent": "" ,"sClass":"right","bSortable":true,"fnRender":function(o,val){
								return val + "h";
							}},
							{"sTitle":"每天预定时长","sWidth":"15%","mDataProp":"durationAvgDay","sDefaultContent": "" ,"sClass":"right","bSortable":true,"fnRender":function(o,val){
								return val + "h";
							}},
							{"sTitle":"每次预定时长","sWidth":"15%","mDataProp":"durationAvgNum","sDefaultContent": "" ,"sClass":"right","bSortable":true,"fnRender":function(o,val){
								return val + "h";
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.statList ? data.statList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_setUpContent: function() { 
							var self = this;
							var html = _.template(Template);
							this.$el.append(html);
							this.uiRanderUtil.randerJQueryUI_DateRange(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","month");
							this._chart();
							this._initTable();
						},
						_onClickDay:function(){this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","day");},
						_onClickWeek:function(){this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","week");},
						_onClickMonth:function(){this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","month");}
					});
			return RoomStatSnippetView;
		});
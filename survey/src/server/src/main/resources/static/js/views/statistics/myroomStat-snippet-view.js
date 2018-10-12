define(
		[ 'jquery', 'views/communication-base-view'
		  ,'text!../../templates/statistics/myRoomStatTemplate.html'
		  ,'text!../../templates/statistics/chartTemplate.html'
		  ,'text!../../templates/statistics/selectTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination', 'ajaxfileupload', 'echarts'],
		function($, CommunicationBaseView,Template,chartTemplate,selectTemplate
				,datatableUtil,datatableLnpagination) {
			var echarts = require('echarts');
			var RoomStatSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-data-stat-search' : '_onClickSearch',
							'click .SID-data-stat-reset' : '_onClickReset',
							'click .SID-export' : '_onClickExport',
							'change .SID-chart-type' : '_changeChartType',
							'click .SID-select-where-add' : '_onClickWhereAdd',
							'click .SID-select-where-del' : '_onClickWhereDel',
							'change .SID-select-where-question' : '_changeOption',
							'click .SID-data-stat-export' : 'exportData'
						},
						initialize : function() {
							this.datatable={};
							this.chartArr=[];
//							this.myChart=[];
//							this.option={};
//							this.dataTip=[];
							this.surveyId = "";
						},
						render : function(surveyId) {
							this.surveyId = surveyId;
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
							this._initSurveyData();
						},
						_onClickReset:function(){
							var self = this;
							fh.confirm('重置后将清除所有筛选，是否继续？',function(){
								self.$el.find(".SID-select-where-div").remove();
								self._initSurveyData();
							});
						},
						_getParam:function(){
							var seldata = {
									surveyId : this.surveyId,
									option : [],
									errorFlag:false
							}
							var divObj = this.$el.find(".SID-select-where-div");
							divObj.find(".SID-error").hide();
							for(var i=0,obj; obj=divObj[i++];) {
								var questionId = $(obj).find(".SID-select-where-question").val();
								var type = $(obj).find(".SID-select-where-type").val();
								var optionSequ = $(obj).find(".SID-select-where-option").val();
								if (questionId != "" && optionSequ != "" && type != "") {
									var option = {
											questionId:questionId,
											isSelect:type,
											optionSequ:optionSequ
									}
									seldata.option.push(option);
								} else {
									$(obj).find(".SID-error").show();
									seldata.errorFlag = true;
								}
							}
							return seldata;
						},
						_initSurveyData:function(){
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							// 查询条件
							var seldata = this._getParam()
							if (seldata.errorFlag) {
								return;
							}
							var param = "&jsonStr=" + JSON.stringify(seldata);
							var url = ropParam + "&method=mapps.survey.dataAnalysis" + param;
							self.$el.find(".SID-chart-question-div").empty();
							$.ajax({
								type : "POST",
								contentType:"application/x-www-form-urlencoded; charset=UTF-8",
								url : servicePath,
								data : url,
								success : function(data) {
									if (data.code == "1") {
										// 头部问卷信息初始化
										var statusStr = "";
										if (data.status == "1") {
											statusStr = "发布中"
										} else if (data.status == "2") {
											statusStr = "已完成"
										} 
										$(".SID-survey-info").html('<span>当前状态：<span>'+statusStr+'</span>  问卷在线时长：<span>'+data.durationStr+'</span>  已收集有效问卷：<span>'+data.answerPersons+'份</span></span>');
										// 筛选条件初始化
										// 图表初始化
										console.log(data);
										self.chartArr = [];
										for(var i=0,question; question=data.analysisQuestions[i++];) {
											// 生成数据展现区域
											var chartData = {
													sequ:i,
													id:question.id,
													total:question.total,
													titleText:question.question,
													type:question.type,
													legendData:[],
													seriesData:[],
													seriesBarData:[],
													tableData:[]
											}
											for(var j=0,option; option=question.suQustionOptions[j++];) {
												var seriesData = {
														value:option.selected,
														name:option.option
												}
												chartData.legendData.push(option.option);
												chartData.seriesData.push(seriesData);
												chartData.seriesBarData.push(option.selected);
												var tableData = {
														td1:option.option,
														td2:option.selected,
														td3:option.selectedPercent,
														id:option.id,
														sequ:option.sequ
												}
												chartData.tableData.push(tableData);
											}
											var questionHtml = _.template(chartTemplate,{data:chartData});
											self.$el.find(".SID-chart-question-div").append(questionHtml);
											var chart = {
												myChart:echarts.init(document.getElementById('analysis-Chart-'+i)),
												option:chartData
											}
											self.chartArr.push(chart);
										}
										self._setPieOption();
										for(var i=0,chart; chart=self.chartArr[i++];) {
											if (chart.option.type == "3") {
												self._setBarOption("update",chart);
											}
										}
									} else {
										fh.alert(data.code + ":" + data.message);
									}
								},
								error : function(){
									fh.alert("数据获取失败");
								}
							});
						},
						_setPieOption:function(updateFlag,data){
							var self = this;
							var arr = self.chartArr;
							if (updateFlag == "update") {
								arr = [data];
							}
							for(var i=0,chart; chart=arr[i++];) {
								var legendArr=[];
								for(var j=0,legendData; legendData=chart.option.legendData[j++];) {
									var str = legendData;
									if (legendData.length > 30) {
										str = legendData.substring(0,30)+"...";
									}
									legendArr.push(str);
								}
								var seriesArr=[];
								for(var j=0,seriesData; seriesData=chart.option.seriesData[j++];) {
									var str = {
											name:seriesData.name,
											value:seriesData.value
									};
									if (seriesData.name.length > 30) {
										str.name = seriesData.name.substring(0,30)+"...";
									}
									seriesArr.push(str);
								}
								// 指定图表的配置项和数据
								var option = {
									    tooltip : {
									        trigger: 'item'
									    },
									    toolbox: {
									        show : true,
									        feature : {
									            magicType: {show: false, type: ['pie']},
									            saveAsImage : {show: true}
									        }
									    },
									    calculable : false,
									    legend: {
									        orient : 'vertical',
									        x : 'left',
									        data:legendArr
									    },
									    series : [
									        {
									            type:'pie',
									            data:seriesArr
									        }
									    ]
									};
						        // 使用刚指定的配置项和数据显示图表。
								chart.myChart.setOption(option,true);
							}
						},
						_setBarOption:function(updateFlag,data){
							var self = this;
							var arr = self.chartArr;
							if (updateFlag == "update") {
								arr = [data];
							}
							for(var i=0,chart; chart=arr[i++];) {
								var legendArr=[];
								for(var j=0,legendData; legendData=chart.option.legendData[j++];) {
									var str = legendData;
									if (legendData.length > 10) {
										str = legendData.substring(0,10)+"...";
									}
									legendArr.push(str);
								}
								var titleArr=[];
								for(var j=0,titleData; titleData=chart.option.titleText[j++];) {
									var str = titleData;
									if (titleData.length > 10) {
										str = titleData.substring(0,10)+"...";
									}
									titleArr.push(str);
								}
								// 指定图表的配置项和数据
								var option = {
									    tooltip : {
									        trigger: 'axis'
									    },
									    toolbox: {
									        show : true,
									        showTitle : false,
									        feature : {
									            magicType: {show: false, type: ['bar']},
									            saveAsImage : {show: true}
									        }
									    },
									    calculable : false,
									    grid: { // 控制图的大小，调整下面这些值就可以，
									    	x: 150,
									    	x2: 150,
									    },
									    xAxis : [
									        {
									            type : 'value'
									        }
									    ],
									    yAxis : [
									        {
									        	type : 'category',
									        	data : legendArr
									        }
									    ],
									    series : [
									        {
									            tooltip : {           
									                formatter: "{c}"
									            },
									            type:'bar',
									            data:chart.option.seriesBarData
									        }
									    ]
									};
						        // 使用刚指定的配置项和数据显示图表。
								chart.myChart.setOption(option,true);
							}
						},
						_changeChartType:function(e){
							var sequ = $(e.currentTarget).attr("data-sequ")-1;
							var type = $(e.currentTarget).val();
							if (type == "pie") {
								this._setPieOption("update",this.chartArr[sequ]);
							} else {
								this._setBarOption("update",this.chartArr[sequ]);
							}
						},
						_onClickWhereAdd:function(e){
							var data = []
							for(var i=0,chart; chart=this.chartArr[i++];) {
								var option = {
										value:chart.option.id,
										text:chart.option.sequ+"."+chart.option.titleText
								};
								data.push(option);
							}
							var html = _.template(selectTemplate,{data:data});
							$(e.currentTarget).parents(".search-list").nextAll(".SID-search-btn-div").before(html);
						},
						_onClickWhereDel:function(e){
							var divObj = $(e.currentTarget).parents(".SID-select-where-div")
							fh.confirm('删除规则后将不可恢复，是否继续？',function(){
								divObj.remove();
							});
						},
						_changeOption:function(e){
							var questionSelected = $(e.currentTarget).val();
							var whereDivObj = $(e.currentTarget).parents(".SID-select-where-div");
							whereDivObj.find(".SID-select-where-option>option[value!='']").remove();
							if (questionSelected == "") {
								return;
							}
							for(var i=0,chart; chart=this.chartArr[i++];) {
								if (chart.option.id == questionSelected) {
									for(var j=0,op; op=chart.option.tableData[j++];) {
										whereDivObj.find(".SID-select-where-option").append("<option value='"+op.sequ+"'>"+op.td1+"</option>");
									}
									return;
								}
							}
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
							var data = {
									surveyId : this.surveyId,
									option : []
							}
							var param = "&jsonStr=" + JSON.stringify(data);
							var url = servicePath + "?" + ropParam + "&method=mapps.survey.dataAnalysis" + param;
							tableObj.tbID = "grid-table1";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = false;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"选项","sWidth":"40%","mDataProp":"option","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"计数","sWidth":"15%","mDataProp":"selected","sDefaultContent": "" ,"sClass":"right","bSortable":true},
							{"sTitle":"占比","sWidth":"15%","mDataProp":"selectedPercent","sDefaultContent": "" ,"sClass":"right","bSortable":true}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.analysisQuestions[0].suQustionOptions ? data.analysisQuestions[0].suQustionOptions : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_setUpContent: function() { 
							var self = this;
							var html = _.template(Template);
							this.$el.append(html);
							this._initSurveyData();
							//this._initTable();
						},
						exportData : function()
						{
							var jsonData = this._getParam()
							var url = "exportChartData?jsonData="+encodeURIComponent(JSON.stringify(jsonData));
							window.location.href = url;
						}
						
					});
			return RoomStatSnippetView;
		});
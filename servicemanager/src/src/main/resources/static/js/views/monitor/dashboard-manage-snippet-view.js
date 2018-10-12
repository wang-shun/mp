define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/dashboardManageTemplate.html'
		  ,'text!../../templates/monitor/dashboardBoxTemplate.html'
		  ,'text!../../templates/monitor/dashboardEditTemplate.html'
		  ,'views/monitor/add-dashboardwidget-snippet-view'
		  ,'views/monitor/gaugeLocation'
		  ,'util/datatableUtil','datatable_lnpagination','echarts','gridstack','gridstack_jQueryUI','jqueryUi'],
		function($, CommunicationBaseView,Template,DashboardBoxTemplate,DashboardEditTemplate,AddView,gaugeLocationModel,datatableUtil,datatableLnpagination,echarts) {
			var treeObj; 
			var timeTable = new Array();
			var HoldSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-add-btn': '_openEditWidget', //_addGridStackWidget
							'click .SID-editWidget': '_openEditWidget',
							'click .SID-deleteWidget':'_deleteGridStackWidget',
							'click .SID-renameWidget':'_renameGridStackWidget',
							'blur .widgetNameInput':'_blurWidgetNameInput',
							'keydown .widgetNameInput':'_keydownWidgetNameInput',
							'click .SID-createDashboard-btn':'_createDashboard',//_createGridStack
							'click .widget-operation':'_toggleOptionBox',
							'gsresizestop .grid-stack':'_resizeGridStackWidget',
							'click .SID-save-btn':'_saveDashboard',
							'click .SID-cancel-btn':'_cancelsaveDashboard',
							'click .SID-dashboard-edit':'_editDashboard',
							'click .SID-dashboard-delete':'_deleteDashboard',
							'change .SID-refreshTime':'_displayEcharts',
							'change .SID-timeRange':'_displayEcharts',
							'mouseleave .blurhide':'_blurHide'
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
							this.data = {};
							this.dashboardId = '';
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
							var self = this;
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
//							this.uiRanderUtil.randerJQueryUI_DateRange(this,"#from","#to","yy-mm-dd");
							self.getDashboardList();
						},
						_blurHide:function(e){
							$(e.currentTarget).find(".widget-operation").removeClass("active");
							$(e.currentTarget).find(".tb-opt-main").hide();
						},
						search : function(){
							var keyword = this.$el.find(".SID-keyword").val();
							this.$el.find(".SID-keyword").val(keyword.trim());
							this.getDashboardList();
						},
						clearSearch : function(){
							this.$el.find(".SID-keyword").val("");
						},
//						getDashboardList:function(){
//							var self = this;
//							var appContext = self.getAppContext();
//							var servicePath = appContext.cashUtil.getData('servicePath');
//							var ropParam = appContext.cashUtil.getData('ropParam');
//							$.ajax({
//                                type : "POST",
//                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.list"),
//                                success : function(response) {
//                                	hideCover();
//                                    if(response.code == "1"){
//            							if(response.dashboardList.length > 0){
//            								var template = _.template(DashboardListTemplate);
//            								var html = template({
//            									dashboardList: response.dashboardList
//            								});
//            								self.$el.find("#dashboardListBox").html(html);
//            							}
//                                    }else{
//                                        fh.alert(response.message);
//                                    }
//                                },
//                                error : function(){
//                                	hideCover();
//                                    fh.alert("数据处理失败");
//                                }
//                            });
//						},
						getDashboardList : function(){
							var tableIndex = 0;
							var tableObj={};
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.dashboard.list&keyword=" + this.$el.find(".SID-keyword").val();
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"dashboard","sWidth":"30%","mDataProp":"name","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"刷新时间","sWidth":"30%","mDataProp":"refreshTime","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"时间范围","sWidth":"30%","mDataProp":"timeRange","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"操作","sWidth":"10%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-dashboard-edit" data-dashboardId="'+o.aData.id+'"><span class="fhicon-pencil"></span>编辑</a>' +
									'<a href="javascript:void(0)" class="SID-dashboard-delete" data-dashboardId="'+o.aData.id+'"><span class="fhicon-delete"></span>删除</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.dashboardList ? data.dashboardList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						},
						_fillData:function(data){
							var self = this;
							var grid = $('.grid-stack').data('gridstack');
							var dashboard = data.dashboard;
							self.$el.find(".SID-dashboardid").val(dashboard.id);
							self.$el.find(".SID-dashboardname").val(dashboard.name);
							self.$el.find(".SID-timeRange").val(dashboard.timeRange);
							self.$el.find(".SID-refreshTime").val(dashboard.refreshTime);
							if(dashboard.layout != undefined){
								if(dashboard.layout != ''){
									var layout = JSON.parse(dashboard.layout);
									for(var i=0;i<layout.length;i++){
										var node = layout[i];
										var DashboardBoxHtml = _.template(DashboardBoxTemplate, {
											'widgetid': node.id,
											'name': node.name,
											'opt': true
										});
										grid.addWidget($(DashboardBoxHtml),node.x, node.y, node.width, node.height);
									}
									self._displayEcharts();
								}else{
									hideCover();
									return;
								}
							}else{
								hideCover();
								return;
							}
						},
						_formatTime:function(timeStr){
							var timeMiliSec = 0;
							switch(timeStr){
								case '1m':
									timeMiliSec = 1000*60;
									break;
								case '5m':
									timeMiliSec = 1000*60*5;
									break;
								case '15m':
									timeMiliSec = 1000*60*15;
									break;
								case '1h':
									timeMiliSec = 1000*60*60;
									break;
								case '6h':
									timeMiliSec = 1000*60*60*6;
									break;
								case '12h':
									timeMiliSec = 1000*60*60*12;
									break;
								case '1d':
									timeMiliSec = 1000*60*60*24;
									break;
								case '2d':
									timeMiliSec = 1000*60*60*24*2;
									break;
								case '7d':
									timeMiliSec = 1000*60*60*24*7;
									break;
								case '30d':
									timeMiliSec = 1000*60*60*24*30;
									break;
							}
							return timeMiliSec;
						},
						_displayEcharts:function(){
							var self = this;
							
							//添加刷新时间应该小于等于时间范围的校验
							var refreshTime = self.$el.find(".SID-refreshTime").val();
							var timeRange = self.$el.find(".SID-timeRange").val();
							if(self._formatTime(refreshTime) > self._formatTime(timeRange)){
								fh.alert("刷新时间不能大于时间范围");
								return;
							}
							
							var dashboardIdList = _.map($('.grid-stack > .grid-stack-item:visible'), function (el) {
		                        el = $(el);
		                        var node = el.data('_gridstack_node');
		                        return el.attr("id");
		                    }, this);
							
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.displayecharts&idList="+JSON.stringify(dashboardIdList)+"&refreshTime="+self.$el.find(".SID-refreshTime").val()+"&timeRange="+self.$el.find(".SID-timeRange").val(),
								success:function(ajax){
									if(ajax.code == "1"){
										var displayEchartsList = ajax.displayEchartsList;
										for(var i=0;i<displayEchartsList.length;i++){
											self._drawDashboard(displayEchartsList[i].series,displayEchartsList[i].panel);
										}
										hideCover();
									}else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						_drawDashboard:function(seriesList,panel){
							var self = this;
							
							var tempname = unescape(panel.name).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							self.$el.find("#"+panel.id).find(".widgetName").html(tempname);
							
							var myChart = echarts.getInstanceByDom(self.$el.find("#"+panel.id+"Content")[0]);
							//不存在则重新初始化
							if(myChart == undefined){
								// 基于准备好的dom，初始化echarts实例
								myChart = echarts.init(self.$el.find("#"+panel.id+"Content")[0]);
							}
							//清空以重绘
							myChart.clear();
							
							if(seriesList.length == 0){
								return;
							}
							
							//var titletext = seriesList[0].name;
							var titletext = "";
							
							var legendData = [];
							var xAxisData = [];
							var chartType = 'line';
							switch(panel.chartType*1){
								case 1:
									chartType = 'line';
									break;
								case 2:
									chartType = 'linesmooth';
									break;
								case 3:
									chartType = 'bar';
									break;
								case 4:
									chartType = 'pie';
									break;
								case 5:
									chartType = 'gauge';
									break;
							}
							var seriesData = [];
							var isSmooth = false;
							var dataZoom = [];
							var option = {};
							//处理数据
							
							if(chartType == "line" || chartType == "bar" || chartType == "linesmooth"){
								if(chartType == "linesmooth"){
									chartType = "line";
									isSmooth = true;
								}
								dataZoom = [{
					                start: 0
					            }, {
					                type: 'inside'
					            }];
								
								
								for(var i=0;i<seriesList[0].values.length;i++){
									var tempTime = seriesList[0].values[i][0];//.replaceAll("T"," ").replaceAll("Z","");
									var tempdate = new Date(tempTime);
									//tempdate.setHours(tempdate.getHours()+8);
									var s = "-";
									var ss = ":";
									var h = (tempdate.getHours()<10)?("0"+tempdate.getHours()):tempdate.getHours();
									var m = (tempdate.getMinutes()<10)?("0"+tempdate.getMinutes()):tempdate.getMinutes();
									var sc = (tempdate.getSeconds()<10)?("0"+tempdate.getSeconds()):tempdate.getSeconds();
									var finaldate = tempdate.getFullYear()+s+((tempdate.getMonth()<9)?("0"+(tempdate.getMonth()+1)):tempdate.getMonth()+1)+s+((tempdate.getDate()<10)?("0"+tempdate.getDate()):tempdate.getDate())+" "+h+ss+m+ss+sc;
									xAxisData.push(finaldate);
								}
								for(var i=0;i<seriesList.length;i++){
									for(var k=1;k<seriesList[i].columns.length;k++){
										var columnName = seriesList[i].columns[k];
										var tags = columnName;
										for(var key in seriesList[i].tags){
											tags += "\n"+seriesList[i].tags[key];
										}
										legendData.push(tags);
										
										var oneData = [];
										for(var j=0;j<seriesList[i].values.length;j++){
											oneData.push(seriesList[i].values[j][k]); 
										}
										var oneSeries = {
												smooth: isSmooth,
												name: tags,
								                type: chartType,
								                data: oneData
										};
										seriesData.push(oneSeries);
									}
								}
								
								option = {
							        	backgroundColor: '#FFFFFF',
							            title: {
							                text: titletext
							            },
							            tooltip: {
							                trigger: 'axis'
							            },
							            legend: {
							            	right: '20',
							            	top: '5',
							                data:legendData
							            },
							            xAxis: {
							                data: xAxisData
							            },
							            yAxis: {
							            	axisLabel: {
							            		formatter: function (value, index) {
							            			if(value > 999){
							            				return value/1000 + 'k';
							            			}else{
							            				return value;
							            			}
							            		}
							            	}
							            },
//							            dataZoom: dataZoom,
							            series: seriesData
							        };
							}else if(chartType == "pie"){
								var oneData = [];
								for(var i=0;i<seriesList.length;i++){
									for(var k=1;k<seriesList[i].columns.length;k++){
										var columnName = seriesList[i].columns[k];
										var tags = columnName;
										for(var key in seriesList[i].tags){
											tags += "\n"+seriesList[i].tags[key];
										}
										legendData.push(tags);
										
										var pieData = {
												value: seriesList[i].values[seriesList[i].values.length-1][k],
												name: tags
										};
										oneData.push(pieData); 
									}
								}
								var oneSeries = {
										name: 'data',
						                type: chartType,
						                radius : '55%',
						                center: ['40%', '50%'],
						                data: oneData.sort(function (a, b) { return a.value - b.value; }),
						                roseType: 'radius',
						                animationType: 'scale',
						                animationEasing: 'elasticOut',
						                animationDelay: function (idx) {
						                    return Math.random() * 200;
						                }
								};
								seriesData.push(oneSeries);
								
								option = {
							        	backgroundColor: '#FFFFFF',
							            title: {
							                text: titletext
							            },
							            tooltip: {
							            	trigger: 'item',
							                formatter: "{b}<br/>{c} ({d}%)"
							            },
							            legend: {
							            	right: '20',
							            	top: '5',
							                data:legendData,
							                orient: 'vertical'
							            },
							            series: seriesData
							        };
								
							}else if(chartType == "gauge"){
								for(var i=0;i<seriesList.length;i++){
									for(var k=1;k<seriesList[i].columns.length;k++){
										var columnName = seriesList[i].columns[k];
										var tags = columnName;
										for(var key in seriesList[i].tags){
											tags += "\n"+seriesList[i].tags[key];
										}
										
										var oneDataOfOneSeries = seriesList[i].values[seriesList[i].values.length-1][k];
										if(oneDataOfOneSeries != null && oneDataOfOneSeries != ""){
											oneDataOfOneSeries = oneDataOfOneSeries.toFixed(2)*100;
										}
										var oneSeries = {
												name: tags,
								                type: chartType,
								                min:0,
								                max:100,
								                detail: {
								                    formatter: '{value}',
								                    offsetCenter: [0, '10%'],
								                    textStyle: {
								                        color: 'auto',
								                        fontSize: 20
								                    }
								                },
								                title : {
								                    fontWeight: 'bolder',
								                },
								                tooltip: {
								                	formatter: "{a} <br/>比例 : {c}%"
								                },
								                axisLine: {            // 坐标轴线
								                    lineStyle: {       // 属性lineStyle控制线条样式
								                        width: 8
								                    }
								                },
								                axisTick: {            // 坐标轴小标记
								                    length:12,        // 属性length控制线长
								                    lineStyle: {       // 属性lineStyle控制线条样式
								                        color: 'auto'
								                    }
								                },
								                splitLine: {           // 分隔线
								                    length:20,         // 属性length控制线长
								                    lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
								                        color: 'auto'
								                    }
								                },
								                pointer: {
								                    width:5
								                },
								                detail: {
								                	show: false
								                },
								                data: [
								                       {
								                    	   value: oneDataOfOneSeries,
								                    	   name: tags
								                    	   }
								                       ]
								        };
										seriesData.push(oneSeries);
									}
								}
								
								var seriesNum = seriesData.length;
								if(seriesNum > 9){
									return;
								}
								for(var j=0;j<seriesNum;j++){
									seriesData[j].radius = gaugeLocation[seriesNum][j].radius;
									seriesData[j].center = gaugeLocation[seriesNum][j].center;
								}
								
								option = {
										backgroundColor: '#FFFFFF',
							            title: {
							                text: titletext
							            },
							            tooltip: { 
							                formatter: "{a} <br/>{b} : {c}%"
							            },
							            series: seriesData
							        };
							}
							
							// 使用刚指定的配置项和数据显示图表。
					        myChart.setOption(option);
						},
						_openEditWidget:function(e){
							var self = this;
							$('#'+$(e.currentTarget).attr("data-widgetid")).find(".widget-operation").click();
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.retention.getfrominflux',
								success:function(ajax){
									if(ajax.code == "1"){
										self.data.retentionList = ajax.queryResult;
										self.data.dbName = ajax.dbName;
										$.ajax({
											type:"POST",
											url:servicePath+"?"+ropParam+ '&method=mapps.servicemanager.measurement.query&keyword=&limit=100&offset=1&sort=',
											success:function(ajax){
												if(ajax.code == "1"){
													self.data.measurementList = ajax.measurementList;
													if($(e.currentTarget).hasClass("SID-editWidget")){
														$.ajax({
															type:"POST",
															url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.panel.detail&id="+$(e.currentTarget).attr("data-widgetid"),
															success:function(ajax){
																if(ajax.code == "1"){
																	var data = {
																			title:"编辑图表",
																			measurementList:self.data.measurementList,
																			retentionList:self.data.retentionList,
																			dashboardId: self.$el.find(".SID-dashboardid").val(),
																			dbName:self.data.dbName,
																			timeRange:self.$el.find(".SID-timeRange").val(),
																			refreshTime:self.$el.find(".SID-refreshTime").val(),
																			panelDetail:ajax,
																	}
																	self.views.AddView = new AddView();
																	self.views.AddView.render(self,data);
																	hideCover();
																}else{
																	hideCover();
																	fh.alert(ajax.message);
																}
															}
														});
													}else{
														var data = {
																title:"新增图表",
																measurementList:self.data.measurementList,
																retentionList:self.data.retentionList,
																dashboardId: self.$el.find(".SID-dashboardid").val(),
																dbName:self.data.dbName,
																timeRange:self.$el.find(".SID-timeRange").val(),
																refreshTime:self.$el.find(".SID-refreshTime").val(),
														}
														self.views.AddView = new AddView();
														self.views.AddView.render(self,data);
														hideCover();
													}
												}
												else{
													hideCover();
													fh.alert(ajax.message);
												}
											}
										});
									}
									else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						_addGridStackWidget:function(dashboardPanel){
							var self = this;
							var grid = $('.grid-stack').data('gridstack');
							var node = {
	                                x: 0,
	                                y: 0,
	                                width: 6,
	                                height: 4,
	                            };
							var widgetid = dashboardPanel.id;
							var DashboardBoxHtml = _.template(DashboardBoxTemplate, {
								'widgetid': widgetid,
								'name': dashboardPanel.name,
								'opt': true
							});
							grid.addWidget($(DashboardBoxHtml),node.x, node.y, node.width, node.height);
//							grid.resizable(self.$el.find(".SID-WidgetBox"),false);
//							grid.movable(self.$el.find(".SID-WidgetBox"),false);
							
							self._displayEcharts();
					        //self._drawEcharts(widgetid+'Content',option);
						},
						_drawEcharts:function(widgetid,option){
							var self = this;
							// 基于准备好的dom，初始化echarts实例
							var myChart = echarts.init(self.$el.find("#"+widgetid)[0]);
							// 使用刚指定的配置项和数据显示图表。
					        myChart.setOption(option);
						},
						_deleteGridStackWidget:function(e){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							fh.confirm('视图数据将一并删除,是否删除此视图?',function(){
								showCover();
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.panel.delete&id="+$(e.currentTarget).attr("data-widgetid"),
									success:function(ajax){
										if(ajax.code == "1"){
											var grid = $('.grid-stack').data('gridstack');
											grid._updateElement($('#'+$(e.currentTarget).attr("data-widgetid")), function(el, node) {
												grid.removeWidgetNode(node);
									        });
											self._saveDashboard(true);
											hideCover();
										}else{
											hideCover();
											fh.alert(ajax.message);
										}
									}
								});
								
							});
						},
						_renameGridStackWidget:function(e){
							var self = this;
							var nameSpan = $('#'+$(e.currentTarget).attr("data-widgetid")).find(".widgetName");
							nameSpan.css("padding","0");
							nameSpan.html('<input class="widgetNameInput" style="border-radius: 4px;width:100%;height:34px;font-size:20px;" value="'+nameSpan.html()+'"/>');
							$('#'+$(e.currentTarget).attr("data-widgetid")).find(".widget-operation").click();
							nameSpan.find(".widgetNameInput").focus();
						},
						_blurWidgetNameInput:function(e){
							var self = this;
							var nameSpan = $(e.currentTarget).parent();
							var nameInputValue = $(e.currentTarget).val();
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.panel.rename&id="+nameSpan.parents(".SID-WidgetBox").attr("id")+"&name="+nameInputValue,
								success:function(ajax){
									if(ajax.code == "1"){
										nameSpan.css("padding","5px");
										nameSpan.html(unescape(nameInputValue).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];}));
										hideCover();
									}else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						_keydownWidgetNameInput:function(e){
							var self = this;
							if(e.keyCode==13)
								self._blurWidgetNameInput(e);
						},
						_createGridStack:function(){
							var self = this;
							var DashboardEdittemplate = _.template(DashboardEditTemplate);
							var DashboardEdithtml = DashboardEdittemplate({});
							self.$el.find("#dashboardEditBox").html(DashboardEdithtml);
							var options = {
					                float: false,
					                animate: true,
					            };
					        $('.grid-stack').gridstack(options);
							self.$el.find("#dashboardMngBox").hide();
							self.$el.find("#dashboardEditBox").show();
						},
						_createDashboard:function(){
							var self = this;
							var dashboard = {
		                    		id: '',
		                    		name: "Dashboard"+Math.floor(5000*Math.random()),
		                    		layout: '',
		                    		refreshTime: '1m',
		                    		timeRange: '1m',
		                    		isDefault: ''
		                    };
		                    var data = {
		                    	dashboard: dashboard	
		                    };
		                    var param = JSON.stringify(data);
		                    var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
		                    showCover();
                            $.ajax({
                                type : "POST",
                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.save&dashboardSaveJson="+param),
                                success : function(response) {
                                	hideCover();
                                    if(response.code == "1"){
                                    	data.dashboard.id = response.id;
                                    	self._createGridStack();
                                    	self._fillData(data);
                                    	hideCover();
                                    }else{
                                    	hideCover();
                                        fh.alert(response.message);
                                    }
                                },
                                error : function(){
                                	hideCover();
                                    fh.alert("数据处理失败");
                                }
                            });
						},
						_toggleOptionBox:function(e){
							var self = this;
							if($(e.currentTarget).hasClass("active")){
								$(e.currentTarget).removeClass("active");
								$(e.currentTarget).parent().find(".tb-opt-main").hide();
							}else{
								$(e.currentTarget).addClass("active");
								$(e.currentTarget).parent().find(".tb-opt-main").show();
							}
						},
						_resizeGridStackWidget:function(e,w){
							var self = this;
							// 获取echarts实例
							var myChart = echarts.getInstanceByDom(self.$el.find("#"+self.$el.find(w).attr("id")+'Content')[0]);
							//不存在则重新初始化
							if(myChart == undefined){
								// 基于准备好的dom，初始化echarts实例
								myChart = echarts.init(self.$el.find("#"+self.$el.find(w).attr("id")+'Content')[0]);
							}
							
							
							// 重新设定大小
							window.setTimeout(function(){
								myChart.resize();
							},100);
						},
						_saveDashboard:function(isFromDelete){
							var self = this;
							var name = escape(self.$el.find(".SID-dashboardname").val());
							if(name == ""){
								fh.alert("请输入dashboard名称");
								return;
							}
							var id = self.$el.find(".SID-dashboardid").val();
							var timeRange = self.$el.find(".SID-timeRange").val();
							var refreshTime = self.$el.find(".SID-refreshTime").val();
							var serializedData = _.map($('.grid-stack > .grid-stack-item:visible'), function (el) {
		                        el = $(el);
		                        var node = el.data('_gridstack_node');
		                        return {
		                        	id: el.attr("id"),
		                        	name: escape(el.find(".widgetName").html()),
		                            x: node.x,
		                            y: node.y,
		                            width: node.width,
		                            height: node.height
		                        };
		                    }, this);
		                    var layout = JSON.stringify(serializedData);
		                    if(serializedData.length == 0){
		                    	fh.alert("请添加一张图表");
								return;
		                    }
		                    var dashboard = {
		                    		id: id,
		                    		name: name,
		                    		layout: layout,
		                    		refreshTime: refreshTime,
		                    		timeRange: timeRange,
		                    		isDefault: ''
		                    };
		                    var data = {
		                    	dashboard: dashboard	
		                    };
		                    var param = JSON.stringify(data);
		                    var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
		                    showCover();
                            $.ajax({
                                type : "POST",
                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.save&dashboardSaveJson="+param),
                                success : function(response) {
                                	hideCover();
                                    if(response.code == "1"){
                                    	if(isFromDelete != true){
                                    		self.$el.find("#dashboardEditBox").hide();
                                        	self.$el.find("#dashboardMngBox").show();
                                        	self.getDashboardList();
                                    	}
                                    }else{
                                        fh.alert(response.message);
                                    }
                                },
                                error : function(){
                                	hideCover();
                                    fh.alert("数据处理失败");
                                }
                            });
						},
						_cancelsaveDashboard:function(){
							var self = this;
							fh.confirm('当前新增或修改的图表将无法保存,是否取消保存?',function(){
								self.$el.find("#dashboardEditBox").hide();
	                        	self.$el.find("#dashboardMngBox").show();
	                        	self.getDashboardList();
							});
						},
						_editDashboard:function(e){
							var self = this;
							var id = $(e.currentTarget).attr("data-dashboardId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
                                type : "POST",
                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.detail&id="+id),
                                success : function(response) {
                                    if(response.code == "1"){
                                    	self._createGridStack();
                                    	self._fillData(response);
                                    	hideCover();
                                    }else{
                                    	hideCover();
                                        fh.alert(response.message);
                                    }
                                }
                            });
						},
						_deleteDashboard:function(e){
							var self = this;
							var id = $(e.currentTarget).attr("data-dashboardId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							fh.confirm('视图数据将一并删除,<br/>是否删除此dashboard?',function(){
								showCover();
	                            $.ajax({
	                                type : "POST",
	                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.delete&id="+id),
	                                success : function(response) {
	                                	hideCover();
	                                    if(response.code == "1"){
	                                    	self.getDashboardList();
	                                    }else{
	                                        fh.alert(response.message);
	                                    }
	                                }
	                            });
							});
						}
					});
			return HoldSnippetView;
		});
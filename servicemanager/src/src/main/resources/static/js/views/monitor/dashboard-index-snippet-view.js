define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/dashboardIndexTemplate.html'
		  ,'text!../../templates/monitor/dashboardBoxTemplate.html'
		  ,'views/monitor/gaugeLocation'
		  ,'util/datatableUtil','datatable_lnpagination','echarts','gridstack','gridstack_jQueryUI','jqueryUi'],
		function($, CommunicationBaseView,Template,DashboardBoxTemplate,gaugeLocationModel,datatableUtil,datatableLnpagination,echarts) {
			var treeObj; 
			var timeTable = new Array();
			var HoldSnippetView = CommunicationBaseView
					.extend({
						events : {
							'change .SID-defaultDashboard':'_ListDashboard',
							'click .SID-refresh-btn':'_refreshData',
							'click .SID-fullscreen-btn':'_clickFullScreen',
							'click .SID-cancelfullscreen-btn':'_cancelFullScreen',
							'click .SID-setdefault-btn':'_setDefault',
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
							this.data = {};
							this.refreshInterval = null;
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
							showCover();
							var self = this;
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							var options = {
				                float: false,
				                animate: true,
				            };
				            $('.grid-stack').gridstack(options);
				            if(document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || false){
				            	self._clickFullScreen();
				            }else{
				            	self._cancelFullScreen();
				            }
				            document.onkeydown = self.screenkeydown;
				            
				            self._getDashboardList();
						},
						_getDashboardList:function(){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.list&keyword=&limit=50&offset=1&sort=",
								success:function(ajax){
									if(ajax.code == "1"){
										var dashboardList = ajax.dashboardList;
										if(dashboardList.length > 0){
											var optionStr = '';
											for(var i=0;i<dashboardList.length;i++){
												var selected = (dashboardList[i].isDefault == "1")?"selected":"";
												optionStr += '<option value="'+dashboardList[i].id+'" '+selected+'>'+unescape(dashboardList[i].name).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];})+'</option>';
											}
											self.$el.find(".SID-defaultDashboard").html(optionStr);
											
											self._ListDashboard();
										}else{
											hideCover();
										}
									}else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						_setDefault:function(){
							var self = this;
							var id = self.$el.find(".SID-defaultDashboard").val();
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							showCover();
							$.ajax({
                                type : "POST",
                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.setdefault&id="+id),
                                success : function(response) {
                                    if(response.code == "1"){
                                    	
                                    	hideCover();
                                    }else{
                                    	hideCover();
                                        fh.alert(response.message);
                                    }
                                }
                            });
						},
						_ListDashboard:function(){
							showCover();
							var self = this;
							var id = self.$el.find(".SID-defaultDashboard").val();
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
                                type : "POST",
                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.detail&id="+id),
                                success : function(response) {
                                    if(response.code == "1"){
                                    	//self._createGridStack();
                                    	self._fillData(response);
                                    	self.data = response;
                                    }else{
                                    	hideCover();
                                        fh.alert(response.message);
                                    }
                                }
                            });
						},
						_refreshData:function(){
							var self = this;
							self._displayEcharts(self.data.dashboard);
						},
						_fillData:function(data){
							var self = this;
							var grid = $('.grid-stack').data('gridstack');
							grid.removeAll();
							var dashboard = data.dashboard;
							if(dashboard == undefined){
								hideCover();
								return;
							}
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
											'opt': false
										});
										grid.addWidget($(DashboardBoxHtml),node.x, node.y, node.width, node.height);
										grid.resizable(self.$el.find(".SID-WidgetBox"),false);
										grid.movable(self.$el.find(".SID-WidgetBox"),false);
									}
									self._displayEcharts(dashboard);
									
								}else{
									hideCover();
									return;
								}
							}else{
								hideCover();
								return;
							}
							var refreshMiliSec = 1000*60;
							switch(dashboard.refreshTime){
								case '1m':
									refreshMiliSec = 1000*60;
									break;
								case '5m':
									refreshMiliSec = 1000*60*5;
									break;
								case '15m':
									refreshMiliSec = 1000*60*15;
									break;
								case '1h':
									refreshMiliSec = 1000*60*60;
									break;
								case '6h':
									refreshMiliSec = 1000*60*60*6;
									break;
								case '12h':
									refreshMiliSec = 1000*60*60*12;
									break;
								case '1d':
									refreshMiliSec = 1000*60*60*24;
									break;
								case '2d':
									refreshMiliSec = 1000*60*60*24*2;
									break;
								case '7d':
									refreshMiliSec = 1000*60*60*24*7;
									break;
								case '30d':
									refreshMiliSec = 1000*60*60*24*30;
									break;
							}
							if(self.refreshInterval != null){
								window.clearInterval(self.refreshInterval);
							}
							self.refreshInterval = window.setInterval(function(){
								self._displayEcharts(self.data.dashboard);
							},refreshMiliSec);
						},
						_displayEcharts:function(dashboard){
							var self = this;
							var dashboardIdList = _.map($('.grid-stack > .grid-stack-item:visible'), function (el) {
		                        el = $(el);
		                        var node = el.data('_gridstack_node');
		                        return el.attr("id");
		                    }, this);
							
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.displayecharts&idList="+JSON.stringify(dashboardIdList)+"&refreshTime="+dashboard.refreshTime+"&timeRange="+dashboard.timeRange,
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
							var myChart = echarts.getInstanceByDom(self.$el.find("#"+panel.id+"Content")[0]);
							//不存在则重新初始化
							if(myChart == undefined){
								// 基于准备好的dom，初始化echarts实例
								myChart = echarts.init(self.$el.find("#"+panel.id+"Content")[0]);
							}
							//清空以重绘
							//myChart.clear();
							// 使用刚指定的配置项和数据显示图表。
					        myChart.setOption(option);
						},
						screenkeydown:function(e){
							var currKey = e.keyCode||e.which||e.charCode;
							if(currKey == 122){
								return false;
							}
						},
						_clickFullScreen:function(e){
							var self = this;
							var docElm = document.documentElement;   
							//W3C    
							if (docElm.requestFullscreen) 
							{      
								docElm.requestFullscreen();    
							}   
							//FireFox    
							else if (docElm.mozRequestFullScreen) 
							{      
								docElm.mozRequestFullScreen();    
							}   
							//Chrome等    
							else if (docElm.webkitRequestFullScreen) 
							{      
								docElm.webkitRequestFullScreen();    
							}   
							//IE11   
							else if (elem.msRequestFullscreen) 
							{    
								elem.msRequestFullscreen();   
							} 
							document.addEventListener("webkitfullscreenchange", function(ew) {
								  if(!ew.currentTarget.webkitIsFullScreen){
									  self._cancelFullScreen();
								  }
								});
							var fullscreenbtn = $(".SID-fullscreen-btn");
							//旧版
							fullscreenbtn.find("span").html("取消全屏");
							fullscreenbtn.addClass("SID-cancelfullscreen-btn");
							fullscreenbtn.removeClass("SID-fullscreen-btn");
							//新版
							fullscreenbtn.attr("title","取消全屏");
							fullscreenbtn.find("img").attr("src","images/icon/cancelal.png");
							$(".SID-monitorGeneralBox").css("margin","0");
						},
						_cancelFullScreen:function(e){
							if (document.exitFullscreen) 
							{  
								document.exitFullscreen();  
							}  else if (document.mozCancelFullScreen) 
							{  
								document.mozCancelFullScreen();  
							}  else if (document.webkitCancelFullScreen) 
							{  
								document.webkitCancelFullScreen(); 
							}  else if (document.msExitFullscreen) 
							{  
								document.msExitFullscreen();  
							} 
							var fullscreenbtn = $(".SID-cancelfullscreen-btn");
							//旧版
							fullscreenbtn.find("span").html("全屏");
							fullscreenbtn.addClass("SID-fullscreen-btn");
							fullscreenbtn.removeClass("SID-cancelfullscreen-btn");
							//新版
							fullscreenbtn.attr("title","全屏");
							fullscreenbtn.find("img").attr("src","images/icon/al.png");
							$(".SID-monitorGeneralBox").css("margin","0 20px 38px 20px");
						}
					});
			return HoldSnippetView;
		});
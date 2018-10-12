define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/trace/traceDependancyTemplate.html'
		  ,'views/trace/trace-detail-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination','echarts','jqueryUi','jqueryUiTimepicker'],
		function($, CommunicationBaseView,Template,TraceDetailView,datatableUtil,datatableLnpagination,echarts) {
			var treeObj; 
			var timeTable = new Array();
			var TraceManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search': 'getDependancy',
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
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
						getDateStr : function(date){
                    		//date.setHours(date.getHours()+8);
							var s = "-";
							var ss = ":";
							var h = (date.getHours()<10)?("0"+date.getHours()):date.getHours();
							var m = (date.getMinutes()<10)?("0"+date.getMinutes()):date.getMinutes();
							var sc = (date.getSeconds()<10)?("0"+date.getSeconds()):date.getSeconds();
							var resolvedtime = date.getFullYear()+s+((date.getMonth()<9)?("0"+(date.getMonth()+1)):date.getMonth()+1)+s+((date.getDate()<10)?("0"+date.getDate()):date.getDate())+" "+h+ss+m;//+ss+sc;
							return resolvedtime;
						},
						setContentHTML : function (){
							var self = this;
							var template = _.template(Template);
							var html = template({});
							self.$el.append(html);
							
							//设置时间范围
							var curDate = new Date();
							$(".SID-timeEnd").val(self.getDateStr(curDate)).datetimepicker();
							curDate.setDate(curDate.getDate()-1);
							$(".SID-timeStart").val(self.getDateStr(curDate)).datetimepicker();
							
							// 基于准备好的dom，初始化echarts实例
							echarts.init(self.$el.find(".SID-echartBox")[0]);
						},
						getDependancy:function(){
							var self = this;
							//处理zipkin其他请求参数
							var endStr = self.$el.find(".SID-timeEnd").val();
							var endTime = new Date(endStr);
							var endTs = endTime.getTime();
							var startStr = self.$el.find(".SID-timeStart").val();
							var startTime = new Date(startStr);
							var startTs = startTime.getTime();
							var lookback = endTs - startTs;
							
							//判断时间是否合法
							var curTime = new Date();
							var curTs = curTime.getTime();
							if(startTs > curTs || endTs > curTs){
								fh.alert("开始时间或结束时间不能晚于当前时间");
								return;
							}
							if(startTs > endTs){
								fh.alert("开始时间不能晚于结束时间");
								return;
							}
							showCover("获取数据中");
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							console.log("ajax start==="+new Date());
							$.ajax({
								type:"POST",
								url:servicePath + "?" + ropParam + "&method=mapps.servicemanager.trace.dependancy&endTs=" + endTs + "&lookback=" + lookback,
								success:function(ajax){
									var rs = ajax.jsonArray;
									//console.log(rs);
									console.log("get data==="+new Date());
									//测试数据
									//rs = [{"child":"mapps-meetingroom","callCount":7,"parent":"mapps-gateway"},{"callCount":2,"parent":"mapps-gateway","child":"mapps-fileservice"},{"callCount":2,"parent":"mapps-meetingroom","child":"mapps-fileservice"},{"callCount":2,"parent":"mapps-fileservice","child":"mapps-contacts"},{"callCount":2,"parent":"mapps-meetingroom","child":"mapps-contacts"},{"callCount":2,"parent":"mapps-meetingroom","child":"mapps-imsg"},{"callCount":2,"parent":"mapps-imsg","child":"mapps-dss"},{"callCount":2,"parent":"mapps-meetingroom","child":"mapps-quanzi"}];
									var seriesData = [];
									var legendData = [];
									var linksData = [];
									var categoriesData = [];
									var svcNameStr = "";
									var isParentObj = {};
									//拼装echarts所需数据结构
									if(rs.length > 0){
										for(var i=0;i<rs.length;i++){
											//统计为parent的次数
											if(isParentObj[rs[i].parent] == undefined){
												isParentObj[rs[i].parent] = 1;
											}else{
												isParentObj[rs[i].parent] = isParentObj[rs[i].parent]+1;
											}
											
											//构建linkData
											var oneLink = {
								                    source: rs[i].parent,
								                    target: rs[i].child,
								                    lineStyle: {
									                    normal: {
									                        color: 'sourceTotarget',
									                        curveness: 0.1
									                    }
									                },
								                    customformatter: rs[i].parent+" > "+rs[i].child
								                };
											linksData.push(oneLink);
											
											//构建seriesData
											if(svcNameStr.indexOf(rs[i].parent+",") < 0){
												var oneSeries = {
														"id":rs[i].parent,
														"name":rs[i].parent,
														"itemStyle":null,
														"symbolSize":30,
														"x":null,
														"y":null,
														"value":0,
														"category":rs[i].parent,
														"label":{"normal":{"show":true}},
												}
												seriesData.push(oneSeries);
												svcNameStr += rs[i].parent+",";
												legendData.push(rs[i].parent);
												var oneCategories = {"name":rs[i].parent};
												categoriesData.push(oneCategories);
											}
											if(svcNameStr.indexOf(rs[i].child+",") < 0){
												var oneSeries = {
														"id":rs[i].child,
														"name":rs[i].child,
														"itemStyle":null,
														"symbolSize":30,
														"x":null,
														"y":null,
														"value":0,
														"category":rs[i].child,
														"label":{"normal":{"show":true}},
												}
												seriesData.push(oneSeries);
												svcNameStr += rs[i].child+",";
												legendData.push(rs[i].child);
												var oneCategories = {"name":rs[i].child};
												categoriesData.push(oneCategories);
											}
										}
										
										//处理服务级别  && 处理调用关系
										var hasLevel0Flag = false;
										for(var i=0;i<seriesData.length;i++){
											var svcName = seriesData[i].id;
											var sourceFlag = false;
											var targetFlag = false;
											var parentNum = 0;
											var parentList = [];
											var childNum = 0;
											var childList = [];
											var pointLevel = 0;
											for(var j=0;j<linksData.length;j++){
												if(linksData[j].target == svcName){
													targetFlag = true;
													parentNum++;
													parentList.push(linksData[j].source);
												}
												if(linksData[j].source == svcName){
													sourceFlag = true;
													childNum++;
													childList.push(linksData[j].target);
												}
											}
											if(sourceFlag && !targetFlag){
												seriesData[i].attributes = {"pointLevel":0};
												hasLevel0Flag = true;
											}
											if(sourceFlag && targetFlag){
												seriesData[i].attributes = {"pointLevel":"x"};
											}
											if(!sourceFlag && targetFlag){
												seriesData[i].attributes = {"pointLevel":"x"};
											}
											seriesData[i].attributes.parentNum = parentNum;
											seriesData[i].attributes.parentList = parentList;
											seriesData[i].attributes.childNum = childNum;
											seriesData[i].attributes.childList = childList;
										}
										
										//默认只含有child没有parent的为级别0,如果级别0不存在,则选取为parent次数最多的为级别0
										if(!hasLevel0Flag){
											var maxParentNum = 0;
											var maxParentName = "mapps-gateway";
											for(var key in isParentObj){
												if (isParentObj[key] > maxParentNum){
													maxParentNum = isParentObj[key];
													maxParentName = key;
												}
											}
											for(var i=0;i<seriesData.length;i++){
												var svcName = seriesData[i].id;
												if(maxParentName == svcName){
													seriesData[i].attributes.pointLevel = 0;
												}
											}
										}
										
										//计算x级别
										for(var level=0;level<seriesData.length;level++){//最多深度为seriesData.length,level为深度
											for(var i=0;i<seriesData.length;i++){
												if(seriesData[i].attributes.pointLevel == level){
													var svcName = seriesData[i].id;
													var targetList = [];
													for(var j=0;j<linksData.length;j++){//计算targetList
														if(linksData[j].source == svcName){
															targetList.push(linksData[j].target);
														}
													}
													for(var j=0;j<targetList.length;j++){
														for(var k=0;k<seriesData.length;k++){
															if(seriesData[k].id == targetList[j]){
																if(seriesData[k].attributes.pointLevel == "x" || seriesData[k].attributes.pointLevel > (level+1)){
																	seriesData[k].attributes.pointLevel = (level+1);
																}
															}
														}
													}
												}
											}
										}
										
										//统计单级别宽度/个数
										var levelObj = {};
										var maxLevel = 0;//级别深度
										for(var i=0;i<seriesData.length;i++){
											var pointlevel = seriesData[i].attributes.pointLevel;
											if(levelObj["level"+pointlevel] == undefined){
												levelObj["level"+pointlevel] = 1;
												seriesData[i].attributes.pointnumber = 1;
											}else{
												levelObj["level"+pointlevel] = levelObj["level"+pointlevel]+1;
												seriesData[i].attributes.pointnumber = levelObj["level"+pointlevel];
											}
											if(pointlevel > maxLevel){
												maxLevel = pointlevel;
											}
										}
										
										var h = self.$el.find(".SID-echartBox").height();
										var w = self.$el.find(".SID-echartBox").width();
										var widthUnit = 1/(maxLevel+2);
										//根据级别深度 和 级别宽度生成x,y && 生成显示框
										for(var i=0;i<seriesData.length;i++){
											var pointlevel = seriesData[i].attributes.pointLevel;
											var pointnumber = seriesData[i].attributes.pointnumber;
											var heightUnit = 1/(levelObj["level"+pointlevel]+1);
											seriesData[i].x = (pointlevel+1)*widthUnit*w;
											seriesData[i].y = (pointnumber)*heightUnit*h;
											
											//生成显示框
											var parentNum = seriesData[i].attributes.parentNum;
											var parentList = seriesData[i].attributes.parentList;
											var childNum = seriesData[i].attributes.childNum;
											var childList = seriesData[i].attributes.childList;
											var formatterStr = ''+seriesData[i].id+"<br />Used by: "+parentNum;
											for(var j=0;j<parentList.length;j++){
												formatterStr += "<br />- "+parentList[j];
											}
											formatterStr += "<br />Uses: "+childNum;
											for(var j=0;j<childList.length;j++){
												formatterStr += "<br />- "+childList[j];
											}
											seriesData[i].customformatter = formatterStr;
										}
										
										//比较箭头曲线起始点,计算渐变起点/终点
										for(var j=0;j<linksData.length;j++){
											var source = linksData[j].source;
											var target = linksData[j].target;
											var sourcex = 0;
											var targetx = 0;
											var sourcey = 0;
											var targety = 0;
											for(var i=0;i<seriesData.length;i++){
												if(seriesData[i].id == source){
													sourcex = seriesData[i].x;
													sourcey = seriesData[i].y;
												}
												if(seriesData[i].id == target){
													targetx = seriesData[i].x;
													targety = seriesData[i].y;
												}
											}
											var sourceToTargetNeedReverse = false;
											if(sourcex > targetx){
												sourceToTargetNeedReverse = true;
											}else if(sourcey > targety && sourcex >= targetx){
												sourceToTargetNeedReverse = true;
											}
											linksData[j].sourceToTargetNeedReverse = sourceToTargetNeedReverse;
										}
										console.log("calc finish==="+new Date());
										self.draw(seriesData,legendData,linksData,categoriesData);
									}else{
										self.drawNoData();
									}
								}
							});
						},
						draw:function(seriesData,legendData,linksData,categoriesData){
							var self = this;
							var myChart = echarts.getInstanceByDom(self.$el.find(".SID-echartBox")[0]);
							//清空以重绘
							myChart.clear();
							option = {
							        title: {
							            text: '调用依赖图',
							            top: 'bottom',
							            left: 'right'
							        },
							        tooltip: {
							        	formatter:function (params, ticket, callback) {
							        		var dataType = params.dataType;
							        		var dataIndex = params.dataIndex;
							        	    if(dataType == "node"){
							        	    	return seriesData[dataIndex].customformatter;
							        	    }else if(dataType == "edge"){
							        	    	return linksData[dataIndex].customformatter;
							        	    }else{
							        	    	return '';
							        	    }
							        	}
							        },
							        legend: [{
							            // selectedMode: 'single',
							            data: legendData
							        }],
							        animationDuration: 1500,
							        animationEasingUpdate: 'quinticInOut',
							        series : [
							            {
							                name: 'Les Miserables',
							                type: 'graph',
							                layout: 'none',
							                data: seriesData,
							                links: linksData,
							                categories: categoriesData,
							                roam: true,
							                edgeSymbol: ['none', 'arrow'],
							                label: {
							                    normal: {
							                        position: 'bottom',
							                        formatter: '{b}'
							                    }
							                }
							            }
							        ]
							    };
							// 使用刚指定的配置项和数据显示图表。
					        myChart.setOption(option);
					        hideCover();
						},
						drawNoData:function(){
							var self = this;
							var myChart = echarts.getInstanceByDom(self.$el.find(".SID-echartBox")[0]);
							//清空以重绘
							myChart.clear();
							option = {
							        title: {
							            text: '当前时间段内无数据',
							            top: 'middle',
							            left: 'center'
							        },
							        tooltip: {},
							        legend: [{
							            // selectedMode: 'single',
							            data: []
							        }],
							        animationDuration: 1500,
							        animationEasingUpdate: 'quinticInOut',
							        series : [
							            {
							                name: 'Les Miserables',
							                type: 'graph',
							                layout: 'none',
							                data: [],
							                links: [],
							                categories: [],
							                roam: true,
							                edgeSymbol: ['none', 'arrow'],
							                label: {
							                    normal: {
							                        position: 'bottom',
							                        formatter: '{b}'
							                    }
							                }
							            }
							        ]
							    };
							// 使用刚指定的配置项和数据显示图表。
					        myChart.setOption(option);
							hideCover();
						}
					});
			return TraceManageSnippetView;
		});
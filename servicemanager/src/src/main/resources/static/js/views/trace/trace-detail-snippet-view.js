define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/trace/traceDetailTemplate.html'
		  ,'text!../../templates/trace/traceDivTemplate.html'
		  ,'text!../../templates/trace/traceTreeTemplate.html'
		  ,'text!../../templates/trace/traceAnnotationTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,traceDivTemplate,traceTreeTemplate,traceAnnotationTemplate,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var TraceDetailView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-traceDiv' : '_onClickTrace',
							'click .SID-treetoggle' : '_onTreeToggle',
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.dialog;
							this.displayMode = "timeline";
							this.traceList = [];
							this.rootIndex = 0;
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
                            this.passedTest=0;
							this._setContentHTML();
							return this;
						},
						destroy : function() {
							this._destroyBusinessViews();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						_destroyBusinessViews : function() {
							$.each(this.childView, function(index, view) {
								view.destroy();
							});
						},
						refresh : function() {
						},
						_setContentHTML : function (){
							var self = this;
							var html = _.template(Template, {});
							self.$el.append(html);
							var commonDialog = fh.servicemanagerOpenDialog('traceDetailDialog', self.data.title, 960, 700, self.el);
							self._fillData(self.data);
							commonDialog.addBtn("cannel","关闭",function(){
								self.displayMode = "timeline";
								commonDialog.cancel();
							});
							commonDialog.addBtn("sortBytree","按节点排序",function(){
								self.displayMode = "treeline";
								self.generateTraceList();
							});
							commonDialog.addBtn("sortBytime","按时间排序",function(){
								self.displayMode = "timeline";
								self.generateTraceList();
							});
							self.dialog = commonDialog;
						},
		                _fillData : function(data){
		                	var self = this;
		                	var jsonArray = data.jsonArray;
		                	
		                	//处理排序并生成排序字段
		                	for (var j = 1; j < jsonArray.length; j++) {
		                		if(jsonArray[j].annotations == undefined){
		                			jsonArray[j].sortTimestamp = jsonArray[j].timestamp;
		                		}else{
		                			jsonArray[j].sortTimestamp = jsonArray[j].annotations[0].timestamp;
		                		}
		    				}
		                	
		                	//根据排序字段进行排序
		                	function sortBySortTimestamp(a,b){
		                		return (a.sortTimestamp - b.sortTimestamp);
		                	}
		                	jsonArray.sort(sortBySortTimestamp);
		                	
		                	var duration = jsonArray[0].duration*1;
		                	var timestamp = jsonArray[0].timestamp*1;
		                	//计算最终duration
		                	var maxEndTimestamp = timestamp + duration;
		    				var minStartTimestamp = timestamp;
		    				for (var j = 1; j < jsonArray.length; j++) {
		    					var oneEndTimestamp = jsonArray[j].timestamp + jsonArray[j].duration;
		    					if(maxEndTimestamp < oneEndTimestamp){
		    						maxEndTimestamp = oneEndTimestamp;
		    					}
		    					if(minStartTimestamp > jsonArray[j].timestamp){
		    						minStartTimestamp = jsonArray[j].timestamp;
		    					}
		    				}
		    				duration = maxEndTimestamp - minStartTimestamp;
		                	
		                	var finalduration = "";
		                	if(duration > 999999){
								finalduration += Math.round(duration/1000)/1000+"s";
							}else if(duration > 999){
								finalduration += duration/1000+"ms";
							}else{
								finalduration += duration+"μs"
							}
							//处理数据
							var traceList = [];
							var serviceNameListStr = "";
							var serviceNum = 0;
							var depth = 0;
							for(var i=0;i<jsonArray.length;i++){
								var binaryAnotationList = jsonArray[i].binaryAnnotations;
								for(var j=0;j<binaryAnotationList.length;j++){
									if(serviceNameListStr.indexOf(binaryAnotationList[j].endpoint.serviceName+",") < 0){
										serviceNameListStr += binaryAnotationList[j].endpoint.serviceName+",";
										serviceNum++;
									}
								}
								
								//计算深度
								if(jsonArray[i].id == jsonArray[i].traceId){
									jsonArray[i].depth = 1;
								}else{
									for(var j=0;j<jsonArray.length;j++){
										if(jsonArray[j].id == jsonArray[i].parentId){
											jsonArray[i].depth = jsonArray[j].depth+1;
										}
									}
								}
								if(depth < jsonArray[i].depth){
									depth = jsonArray[i].depth;
								}
								
								var oneTrace = {
										id: jsonArray[i].id,
										traceId: jsonArray[i].traceId,
										parentId: jsonArray[i].parentId,
										timestamp: jsonArray[i].timestamp,
								};
								if(jsonArray[i].id == jsonArray[i].traceId){
									self.rootIndex = i;
								}
								oneTrace.left = (jsonArray[i].timestamp*1-jsonArray[0].timestamp*1)/duration*100+"%";
								var colorIndex = (jsonArray[i].depth-1);
								var colorStr = "traceColor";
								var bA = jsonArray[i].binaryAnnotations;
								for(var k=0;k<bA.length;k++){
									if(bA[k].key == "error"){
										colorStr = "traceRed";
									}
								}
								if(colorIndex<6){
									oneTrace.color = colorStr+colorIndex;
								}else{
									oneTrace.color = colorStr+"5";
								}
								var tempduration = jsonArray[i].duration;
								if(tempduration > 999999){
									oneTrace.duration = Math.round(tempduration/1000)/1000+"s";
								}else if(tempduration > 999){
									oneTrace.duration = tempduration/1000+"ms";
								}else{
									oneTrace.duration = tempduration+"μs"
								}
								oneTrace.name = jsonArray[i].name;
								oneTrace.width = (jsonArray[i].duration*1)/duration*100;
								if(oneTrace.width < 0.2){
									oneTrace.width = "4px";
								}else{
									oneTrace.width += "%";
								}
								var anotationList = jsonArray[i].annotations;
								if(anotationList == undefined){
									oneTrace.serviceName = jsonArray[i].binaryAnnotations[0].endpoint.serviceName;
								}else{
									for(var j=0;j<anotationList.length;j++){
										if(anotationList[j].value == "sr"){
											oneTrace.serviceName = anotationList[j].endpoint.serviceName;
										}
									}
									if(oneTrace.serviceName == undefined){
										oneTrace.serviceName = anotationList[0].endpoint.serviceName;
									}
								}
								traceList.push(oneTrace);
							}
		                	//填充基本信息
							self.$el.find(".SID-duration").html(finalduration);
							self.$el.find(".SID-services").html(serviceNum);
							self.$el.find(".SID-depth").html(depth);
							self.$el.find(".SID-spans").html(jsonArray.length);
							
							self.traceList = traceList;
							//制造时间对比段
							self.generateTraceList();
		                },
		                generateTraceList:function(){
		                	var self = this;
		                	self.$el.find(".SID-annotationBox").html("");
							if(self.displayMode == "timeline"){//按照时间排序
								var traceDivHtml = _.template(traceDivTemplate, {
									'traceList': self.traceList,
								});
			            		self.$el.find(".SID-traceBox").html(traceDivHtml);
							}else if(self.displayMode == "treeline"){//按照节点关系排序
								var traceDivHtml = _.template(traceTreeTemplate, {
									'trace': self.traceList[self.rootIndex],
									'traceIndex': self.rootIndex,
									'traceLevel': 1,
								});
			            		self.$el.find(".SID-traceBox").html(traceDivHtml);
							}
		                },
		                _onTreeToggle:function(e){
		                	var self = this;
		                	
		                	function sortByTimestamp(a,b){
		                		return (a.trace.timestamp - b.trace.timestamp);
		                	}
		                	
		                	var flag = ($(e.currentTarget).html() == "+");
		                	var parentLevel = $(e.currentTarget).attr("data-parentLevel")*1;
		                	var parentIndex = $(e.currentTarget).attr("data-parentIndex")*1;
		                	var traceList = self.traceList;
		                	if(flag){
		                		var traceTemplateList = [];
		                		if(self.$el.find(".SID-childBox-"+traceList[parentIndex].id).html() == ""){
		                			self.$el.find(".SID-childBox-"+traceList[parentIndex].id).html("");
			                		for(var i=0;i<traceList.length;i++){
				                		if(traceList[i].parentId == traceList[parentIndex].id){
				                			var traceTemplateObj = {
												'trace': traceList[i],
												'traceIndex': i,
												'traceLevel': (parentLevel+1),
											};
				                			traceTemplateList.push(traceTemplateObj);
				                		}
				                	}
			                		if(traceTemplateList.length > 0){
			                			traceTemplateList.sort(sortByTimestamp);
				                		for(var i=0;i<traceTemplateList.length;i++){
				                			var traceDivHtml = _.template(traceTreeTemplate, traceTemplateList[i]);
						            		self.$el.find(".SID-childBox-"+traceList[parentIndex].id).append(traceDivHtml);
				                		}
				                		self.$el.find(".SID-childBox-"+traceList[parentIndex].id).show();
				                		$(e.currentTarget).html("-");
			                		}else{
			                			$(e.currentTarget).css("border","0px");
			                			$(e.currentTarget).css("width","16px");
			                			$(e.currentTarget).css("height","16px");
			                			$(e.currentTarget).html(" ");
			                			$(e.currentTarget).removeClass("SID-treetoggle");
			                		}
		                		}else{
		                			self.$el.find(".SID-childBox-"+traceList[parentIndex].id).show();
		                			$(e.currentTarget).html("-");
		                		}
		                	}else{
		                		self.$el.find(".SID-childBox-"+traceList[parentIndex].id).hide();
		                		$(e.currentTarget).html("+");
		                	}
		                	
		                },
                        _exalert:function(info,ischild,handler){
                        	var _self = this;
							fh.alert(info,true,handler,_self.dialog,null);
                        },
                        _onClickTrace:function(e){
                        	var self = this;
                        	var index = $(e.currentTarget).attr("data-index");
                        	var jsonArray = self.data.jsonArray;
                        	var traceInfo = jsonArray[(index*1)];
                        	var traceAnnotationHtml = _.template(traceAnnotationTemplate, {
								'traceInfo': traceInfo,
								'zerotimepoint':jsonArray[0].timestamp,
							});
                        	self.$el.find(".SID-annotationBox").html(traceAnnotationHtml);
                        }
					});
			return TraceDetailView;
		});
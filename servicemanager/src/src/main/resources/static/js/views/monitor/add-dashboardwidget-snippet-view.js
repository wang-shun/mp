define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/addDashboardWidgetTemplate.html'
		  ,'text!../../templates/monitor/dashboardSeriesBoxTemplate.html'
		  ,'text!../../templates/monitor/kapacitorFilterTemplate.html'
		  ,'text!../../templates/monitor/fieldFilterTemplate.html'
		  ,'views/monitor/edit-selfdefine-snippet-view'
		  ,'views/monitor/gaugeLocation'
		  ,'util/datatableUtil','datatable_lnpagination','echarts','editableselect'],
		function($, CommunicationBaseView,Template,dashboardSeriesBoxTemplate,kapacitorFilterTemplate,fieldFilterTemplate,editSelfdefineView
				,gaugeLocationModel,datatableUtil,datatableLnpagination,echarts) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-smalltab' : '_selectTab',
							'click .SID-addtab' : '_addSeries',
							'click .seriesLabelDelete': '_deleteSeries',
							'click .SID-kapacitorFilter' : '_showFilter',
							'click .SID-fieldFilter' : '_showFieldFilter',
							'click .SID-closeFilter' : '_closeFilter',
							'change .SID-filter-tagvalue' : '_clickFilter',
							'change .SID-filter-groupby' : '_clickFilter',
							'click .SID-toggleFilter' : '_toggleFilter',
							'change .definecheck' : '_defineCheck',
							'change .SID-func' : '_defineCheck',
							'change .selfdefinecheck' : '_defineCheck',
							'change .SID-selffunc' : '_defineCheck',
							'click .fieldtextarea' : '_clickfieldTextarea',
							'change .fieldinput' : '_defineCheck',
							'change .SID-rp' : '_changeRp',
							'click .SID-chartType' : '_clickChartType',
							'mouseleave .blurhide':'_blurHide'
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.dialog;
							this.measurements = [];
							this.tabIndex = 1;
							this.clickTextArea = {};
							this.chartType = "line";
							this.isInit = false;
							this.needGroupBy = true;
							this.customMap = {};
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
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
							this.$el.append(html);
							var seriesTemplate = _.template(dashboardSeriesBoxTemplate);
							var seriesHtml = seriesTemplate({
								num: self.tabIndex,
							});
							self.$el.find("#seriesOptionBox").html(seriesHtml);
							
							if(self.data.retentionList[0].series != undefined){
								var retentionList = self.data.retentionList[0].series[0].values;
								var retentionStr = '<option value="" selected></option>';
								for(var i=0;i<retentionList.length;i++){
									var selected = "";
									retentionStr += '<option value="'+retentionList[i][0]+'">'+retentionList[i][0]+'</option>';
								}
								self.$el.find(".SID-rp").append(retentionStr);
							}
							var measurementStr = '<option value="" selected></option>';
							var measurementList = self.data.measurementList;
							for(var i=0;i<measurementList.length;i++){
								var selected = "";
								measurementStr += '<option value="'+measurementList[i].id+'" '+selected+'>'+measurementList[i].measurement+"-"+measurementList[i].name+'</option>';
							}
							var seriesBox = self.$el.find(".SID-measurement").parent().parent().parent();
							self.$el.find(".SID-measurement").append(measurementStr).editableSelect({ 
								effects: 'fade' ,
								onSelect:function (li) {
									self.measurementId = li.value;
									self._onChangeMeasurement(seriesBox);
								},
							});
							
							var commonDialog;
							if(self.data.panelDetail != null){
								commonDialog =fh.servicemanagerOpenDialog('panelEditDialog', this.data.title, 960, 720, this.el);
                                self._fillData(self.data);
							}else{
								commonDialog =fh.servicemanagerOpenDialog('panelAddDialog', this.data.title, 960, 720, this.el);
							}
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("save","保存",function(){
								self._sumbitForm(commonDialog);
							});
							this.dialog = commonDialog;
							
							// 基于准备好的dom，初始化echarts实例
							echarts.init(self.$el.find("#echartsBox")[0]);
						},
						_blurHide:function(e){
							if(!(e.screenX==0))
								$(e.currentTarget).hide();
						},
						_selectTab :function(e){
							var self = this;
							var clickTab = $(e.currentTarget);
							self.$el.find(".SID-smallmenu li").removeClass("active");
							clickTab.addClass("active");
							self.$el.find("#seriesOptionBox").find(".SID-seriesBox").hide();
							self.$el.find("#series"+clickTab.attr("data-name")).show();
						},
		                _fillData : function(data){
		                	var self = this;
		                	self.isInit = true;
		                	var dashboardPanel = data.panelDetail.dashboardPanel;
		                	var dashboardPanelSeries = data.panelDetail.dashboardPanelSeries;
		                	//填充基本信息
		                	var chartType = 'line';
							switch(dashboardPanel.chartType*1){
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
		                	self.chartType = chartType;
		                	var preFix = isIframe?iframePrefix:"";
		                	self.$el.find(".chartType-line").attr("src",preFix+"images/icon/line.png");
		                	self.$el.find(".chartType-"+chartType).attr("src",preFix+"images/icon/"+chartType+"blue.png");
		                	self.$el.find(".SID-id").val(dashboardPanel.id);
		                	self.$el.find(".SID-panelName").val(dashboardPanel.name);
		                	
		                	//根据series数量填充
		                	if(dashboardPanelSeries.length > 1){
		                		for(var i=1;i<dashboardPanelSeries.length;i++){
		                			self._addSeries();
		                		}
		                	}
		                	
		                	//处理填充单个series详情
	                		for(var i=0;i<dashboardPanelSeries.length;i++){
	                			var series = self.$el.find("#seriesOptionBox").find("#series"+(i+1));
	                			series.find(".SID-rp").val(dashboardPanelSeries[i].retentionPolicy);
	                			series.find(".SID-measurement").editableSelect('select', series.find(".es-list").find("li[value='"+dashboardPanelSeries[i].measurement+"']")).blur();
	                		}
		                	
		                },
		                _dataHandle:function(series){
		                	var self = this;
		                	var dashboardPanelSeries = self.data.panelDetail.dashboardPanelSeries;
		                	var seriesIndex = series.attr("id").split("series")[1]*1-1;
		                	//进行数据处理
                			var whereSetting = dashboardPanelSeries[seriesIndex].whereSetting;
                			if(whereSetting != ""){
                				var whereToJson = JSON.parse(whereSetting);
                				var groupBy = whereToJson.groupby;
                				var where = whereToJson.where;
	                			if(groupBy.length > 0){
			                		for(var k=0;k<groupBy.length;k++){
			                			series.find(".SID-filterBox").find("#"+series.attr("id")+groupBy[k].key+"groupby").attr("checked","checked");
			                		}
		                		}
		                		if(where.length > 0){
			                		for(var k=0;k<where.length;k++){
			                			series.find(".SID-filterBox").find("#"+series.attr("id")+where[k].key+"tag"+where[k].value).attr("checked","checked");
			                		}
		                		}
                			}
		                	self._clickFilterBase(series);
		                	
		                	var fieldsSetting = dashboardPanelSeries[seriesIndex].fieldsSetting;
		                	var fieldToJson = JSON.parse(fieldsSetting);
		                	var fieldList = fieldToJson.field;
		                	for(var i=0;i<fieldList.length;i++){
		                		var fieldTd = series.find("#fieldvalueBox").find("td[title='"+fieldList[i].key+"']");
		                		fieldTd.parent().find(".definecheck").attr("checked","checked");
		                		fieldTd.parent().find(".SID-func").val(fieldList[i].func);
		                	}
		                	if(fieldToJson.customField != ""){
		                		series.find("#selfdefineBox").find(".selfdefinecheck").attr("checked","checked");
		                	}
		                	series.find("#selfdefineBox").find(".fieldinput").val(fieldToJson.customName);
		                	series.find("#selfdefineBox").find(".fieldtextarea").val(fieldToJson.customField);
		                	self._defineCheckBase(series);
		                	
		                	var sqlList = self._seriesCheck();
		                	if(sqlList.length == dashboardPanelSeries.length){
		                		self.isInit = false;
			                	//最后统一获取echarts绘图数据
								self._getEchartsSeries();
		                	}
		                },
		                _addSeries:function(e){
		                	var self = this;
		                	var addNo = self.$el.find(".SID-smallmenu").find("#tabBox").find("li").length;
		                	self.tabIndex++;
		                	self.$el.find(".SID-smallmenu li").removeClass("active");
		                	self.$el.find("#seriesOptionBox").find(".SID-seriesBox").hide();
		                	var preFix = isIframe?iframePrefix:"";
		                	var addtabhtml = '<li class="active SID-smalltab" data-name="'+self.tabIndex+'"><a style="padding: 0 0 0 25px">系列'+(addNo+1)+'<img src="'+preFix+'images/icon/seriesoff.png" class="seriesLabelDelete"/></a></li>';
		                	self.$el.find("#tabBox").append(addtabhtml);
		                	var seriesTemplate = _.template(dashboardSeriesBoxTemplate);
							var seriesHtml = seriesTemplate({
								num: self.tabIndex,
							});
							self.$el.find("#seriesOptionBox").append(seriesHtml);
							
							
							if(self.data.retentionList[0].series != undefined){
								var retentionList = self.data.retentionList[0].series[0].values;
								var retentionStr = '<option value="" selected></option>';
								for(var i=0;i<retentionList.length;i++){
									var selected = "";
									retentionStr += '<option value="'+retentionList[i][0]+'">'+retentionList[i][0]+'</option>';
								}
								self.$el.find(".SID-rp:visible").append(retentionStr);
							}
							var measurementStr = '<option value="" selected></option>';
							var measurementList = self.data.measurementList;
							for(var i=0;i<measurementList.length;i++){
								var selected = "";
//								if(measurementList[i].isDefault == "1" && self.data.measurementDetail == null){
//									selected = "selected";
//								}
								measurementStr += '<option value="'+measurementList[i].id+'" '+selected+'>'+measurementList[i].measurement+"-"+measurementList[i].name+'</option>';
							}
							var seriesBox = self.$el.find(".SID-measurement:visible").parent().parent().parent();
							self.$el.find(".SID-measurement:visible").append(measurementStr).editableSelect({ 
								effects: 'fade' ,
								onSelect:function (li) {
									self.measurementId = li.value;
									self._onChangeMeasurement(seriesBox);
								},
							});
		                },
		                _deleteSeries:function(e){
		                	var self = this;
		                	fh.confirm('是否删除此系列?',function(){
		                		$(e.currentTarget).prev("li").click();
		                		self.$el.find("#seriesOptionBox").find("#series"+$(e.currentTarget).attr("data-name")).remove();
		                		$(e.currentTarget).remove();
		                		var seriesTabList = self.$el.find("#tabBox li");
		                		var preFix = isIframe?iframePrefix:"";
		                		for(var i=0;i<seriesTabList.length;i++){
		                			if(i != 0){
		                				$(seriesTabList[i]).find("a").html('系列'+(i+1)+'<img src="'+preFix+'images/icon/seriesoff.png" class="seriesLabelDelete"/>');
		                			}
		                		}
		                		self._getEchartsSeries();
							},true,"",self.dialog,function(){},null);
		                },
		                _showFilter:function(e){
							var self = this;
							var kapacitorFilterBox = $(e.currentTarget).parent();
							var bigbox = kapacitorFilterBox.parent().parent().find("#bigbox");
							//过滤模块宽度调整
							bigbox.width(kapacitorFilterBox.width()-1);
							bigbox.show();
							bigbox.parent().find("#fieldbox").hide();
						},
						_showFieldFilter:function(e){
								var self = this;
								var fieldFilterBox = $(e.currentTarget).parent();
								var fieldbox = fieldFilterBox.parent().parent().find("#fieldbox");
								//过滤模块宽度调整
								fieldbox.width(fieldFilterBox.width()-1);
								fieldbox.show();
							},
						_closeFilter:function(e){
							$(e.currentTarget).parent().parent().hide();
						},
						_onChangeMeasurement:function(seriesBox){
		                	var self = this;
		                	var selectmeasurementId = self.measurementId;
		                	var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
		                	var measurement = seriesBox.find('.SID-measurement').val().split("-")[0];
							if(measurement != ""){
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.field.list&id="+selectmeasurementId,
									success:function(ajax){
										self.data.valuedefineList = ajax.list;
										var fieldFilterHtml = _.template(fieldFilterTemplate, {
											'fieldList': self.data.valuedefineList,
										});
										seriesBox.find(".fieldFilterBox").parent().parent().find(".SID-fieldBox").html(fieldFilterHtml);
										seriesBox.find(".fieldFilterBox").parent().parent().find(".SID-fieldBox").find(".SID-valuedefineListInput").val(JSON.stringify(ajax.list));
									}
								});
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.filter.get&id="+measurement,
									success:function(ajax){
										if(ajax.filterList != undefined){
											var kapacitorFilterHtml = _.template(kapacitorFilterTemplate, {
												'filterList': ajax.filterList,
												'labelFor': seriesBox.attr("id")
											});
											seriesBox.find(".kapacitorFilterBox").parent().parent().find(".SID-filterBox").html(kapacitorFilterHtml);
										}else{
											seriesBox.find(".kapacitorFilterBox").parent().parent().find(".SID-filterBox").html("");
										}
										if(self.isInit){
											self._dataHandle(seriesBox);
										}
									}
								});
							}
		                },
		                _toggleFilter:function(e){
							var self = this;
							if($(e.currentTarget).find("span").attr("class").indexOf("fhicon-arrowU2") > -1){
								$(e.currentTarget).find("span").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
								$(e.currentTarget).parent().parent().find(".tagvalueBox").hide();
							}else{
								$(e.currentTarget).find("span").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
								$(e.currentTarget).parent().parent().find(".tagvalueBox").show();
							}
						},
						_clickFilter:function(e){
							var self = this;
							var seriesBox = $(e.currentTarget).parents('.SID-seriesBox');
							self._clickFilterBase(seriesBox);
						},
						_clickFilterBase:function(seriesBox){
							var self = this;
							var tagvalueStr = "";
							var groupbyStr = "";
							var connectStr = "";
							var whereJsonStr = {
									where: [],
									groupby: []
							};
							var tagvalueList = seriesBox.find('.SID-filterBox').find(".SID-filter-tagvalue");
							for(var i=0;i<tagvalueList.length;i++){
								if($(tagvalueList[i]).attr("checked") == "checked"){
									var tag = $(tagvalueList[i]).attr("data-tag");
									var onestr = "\""+tag+"\"='"+$(tagvalueList[i]).val()+"'";
									if(tagvalueStr == ""){
										tagvalueStr += onestr;
									}else{
										tagvalueStr += " or "+onestr;
									}
									var oneWhereJson = {
											key: tag,
											value: $(tagvalueList[i]).val()
									};
									whereJsonStr.where.push(oneWhereJson);
								}
							}
							var groupbyList = seriesBox.find('.SID-filterBox').find(".SID-filter-groupby");
							
							for(var i=0;i<groupbyList.length;i++){
								if($(groupbyList[i]).attr("checked") == "checked"){
									//var tag = "'"+$(groupbyList[i]).attr("data-tag")+"'";
									var tag = $(groupbyList[i]).attr("data-tag");
									if(groupbyStr == ""){
										groupbyStr += tag;
									}else{
										groupbyStr += ","+tag;
									}
									var oneGroupByJson = {
											key: tag
									};
									whereJsonStr.groupby.push(oneGroupByJson);
								}
							}
							if(tagvalueStr != "" && groupbyStr != ""){
								connectStr = " | ";
							}
							var filterStr = tagvalueStr+connectStr+((groupbyStr == "")?"":("group by "+groupbyStr));
							seriesBox.find('.SID-kapacitorFilter').val(filterStr);
							seriesBox.find(".finalwhere").val(tagvalueStr);
							seriesBox.find('.finalgroupby').val(groupbyStr);
							seriesBox.find('.wherejson').val(JSON.stringify(whereJsonStr));
							self._createStatement(seriesBox);
						},
						_defineCheck:function(e){
							var self = this;
							var seriesBox = $(e.currentTarget).parents('.SID-seriesBox');
							self._defineCheckBase(seriesBox);
						},
						_defineCheckBase:function(seriesBox){
							var self = this;
							var fieldStr = "";
							var fieldJson = {
									field: [],
									customField: '',
									customName: '',
							};
							//fieldfilter拼接
							var trStr = "";
							var trdataList = seriesBox.find("#fieldvalueBox").find('.datatr');
							var checkedNum = 0;
							var noFuncNum = 0;
							for(var i=0;i<trdataList.length;i++){
								if($(trdataList[i]).find(".definecheck").attr("checked") == "checked"){
									checkedNum++;
									var func = $(trdataList[i]).find(".SID-func").val();
									var field = $(trdataList[i]).find(".textinoneline").html();
									var s = "";
									if(trStr != ""){
										s = ",";
									}
									if(func != ""){
										trStr += s+func+"(\""+field+"\") as \""+func+"_"+field+"\"";
									}else{
										noFuncNum++;
										trStr += s+"\""+field+"\"";
									}
									var oneFieldJson = {
											key: field,
											func: func
									};
									fieldJson.field.push(oneFieldJson);
								}
							}
							
							if(noFuncNum != 0 && noFuncNum != checkedNum){
								self._exalert("请统一字段格式:全部使用统计函数或全部不使用统计函数");
								seriesBox.find(".SID-statement").val("");
								self._getEchartsSeries();
								return;
							}
							if(noFuncNum == 0 && checkedNum != 0){
								self.needGroupBy = true;
							}else{
								self.needGroupBy = false;
							}
							fieldStr += trStr;
							var textareaStr = "";
							var selfdefineBox = seriesBox.find("#selfdefineBox");
							var regexStr = "(mean|min|max|count|sum|stddev)\\((";
							var fieldList = JSON.parse(seriesBox.find(".fieldFilterBox").parent().parent().find(".SID-fieldBox").find(".SID-valuedefineListInput").val());
							for(var i=0;i<fieldList.length;i++){
								if(i != 0){
									regexStr += "|";
								}
								regexStr += fieldList[i].field;
							}
							regexStr += ")\\)";
							if(selfdefineBox.find('.selfdefinecheck').attr("checked") == "checked" && selfdefineBox.find('.fieldtextarea').val() != ""){
								if(selfdefineBox.find('.fieldtextarea').val().match(regexStr) == null && noFuncNum == 0 && checkedNum != 0){
									self._exalert("请统一字段格式:全部使用统计函数或全部不使用统计函数");
									seriesBox.find(".SID-statement").val("");
									self._getEchartsSeries();
									return;
								}else if(selfdefineBox.find('.fieldtextarea').val().match(regexStr) != null && noFuncNum != 0){
									self._exalert("请统一字段格式:全部使用统计函数或全部不使用统计函数");
									seriesBox.find(".SID-statement").val("");
									self._getEchartsSeries();
									return;
								}
								
								if(selfdefineBox.find('.fieldtextarea').val().match(regexStr) == null){
									self.needGroupBy = false;
								}else{
									self.needGroupBy = true;
								}
								if(fieldStr != ""){
									textareaStr += ",";
								}
								var customId = seriesBox.attr("id");
								textareaStr += "("+selfdefineBox.find('.fieldtextarea').val()+") as customValue"+customId;
								fieldJson.customField = selfdefineBox.find('.fieldtextarea').val().replace(/\+/g, '%2B');
								fieldJson.customName = (selfdefineBox.find('.fieldinput').val() == "")?"自定义字段":selfdefineBox.find('.fieldinput').val();
								self.customMap["customValue"+customId] = fieldJson.customName;
							}
							fieldStr += textareaStr;
							seriesBox.find('.SID-fieldFilter').val(fieldStr);
							fieldJson.needGroupBy = self.needGroupBy;
							seriesBox.find('.fieldjson').val(JSON.stringify(fieldJson));
							self._createStatement(seriesBox);
						},
						_clickfieldTextarea:function(e){
							var self = this;
							self.clickTextArea = $(e.currentTarget);
							var data = {
									title: "编辑自定义字段",
									fieldList: self.data.valuedefineList,
									filledvalue: $(e.currentTarget).val(),
									measurement: self.$el.find('.SID-measurement:visible').val().split("-")[0],
							}
							self.childView.editSelfdefineView = new editSelfdefineView();
							self.childView.editSelfdefineView.render(self,data);
						},
						_fillfieldTextarea:function(str){
							var self = this;
							self.clickTextArea.val(str);
							var seriesBox = self.clickTextArea.parents('.SID-seriesBox');
							self._defineCheckBase(seriesBox);
						},
						_changeRp:function(e){
							var self = this;
							var seriesBox = $(e.currentTarget).parents('.SID-seriesBox');
							self._createStatement(seriesBox);
						},
						_createStatement:function(seriesBox){
							var self = this;
							if(seriesBox.find('.SID-rp').val() == "" || seriesBox.find('.SID-measurement').val() == "" || seriesBox.find('.SID-fieldFilter').val() == ""){
								seriesBox.find(".SID-statement").val("");
							}else{
								var statementStr = "select "+seriesBox.find('.SID-fieldFilter').val()+" from \""+self.data.dbName+"\".\""+seriesBox.find('.SID-rp').val()+"\".\""+seriesBox.find('.SID-measurement').val().split("-")[0]+"\"";
								statementStr += " where time > now() - "+self.data.timeRange;
								if(seriesBox.find('.finalwhere').val() != ""){
									statementStr += " and ("+seriesBox.find('.finalwhere').val()+")";
								}
								var fieldJson = JSON.parse(seriesBox.find('.fieldjson').val());
								if(fieldJson.needGroupBy){
									statementStr += " group by time("+self.data.refreshTime+")";
									if(seriesBox.find('.finalgroupby').val() != ""){
										statementStr += ","+seriesBox.find('.finalgroupby').val();
									}
								}else{
									if(seriesBox.find('.finalgroupby').val() != ""){
										statementStr += " group by "+seriesBox.find('.finalgroupby').val();
									}
								}
								seriesBox.find(".SID-statement").val(statementStr);
							}
							
							//获取echarts绘图数据
							self._getEchartsSeries();
						},
						_clickChartType:function(e){
							var self = this;
							var preFix = isIframe?iframePrefix:"";
							//取消所有蓝色图表类型按钮样式
							var chartTypebtnList = self.$el.find(".SID-chartType");
							for(var i=0;i<chartTypebtnList.length;i++){
								$(chartTypebtnList[i]).find("img").attr("src",preFix+"images/icon/"+$(chartTypebtnList[i]).attr("data-chartType")+".png");
							}
							
							var chartType = $(e.currentTarget).attr("data-chartType");
							$(e.currentTarget).find("img").attr("src",preFix+"images/icon/"+chartType+"blue.png");
							self.chartType = chartType;
							//获取echarts绘图数据
							self._getEchartsSeries();
						},
						_seriesCheck:function(){
							var self = this;
							var seriesOptionBox = self.$el.find("#seriesOptionBox");
							var seriesList = seriesOptionBox.find(".SID-seriesBox");
							var sqlList = [];
							for(var i=0;i<seriesList.length;i++){
								var seriesSql = $(seriesList[i]).find(".SID-statement").val();
								if(seriesSql != ""){
									sqlList.push(seriesSql);
								}
							}
							return sqlList;
						},
						_getEchartsSeries:function(){
							var self = this;
							
							if(self.isInit){
								return;
							}
							
							var sqlList = self._seriesCheck();
							if(sqlList.length == 0){
								var myChart = echarts.getInstanceByDom(self.$el.find("#echartsBox")[0]);
								if(myChart != undefined){
									//清空
									myChart.clear();
								}
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.echartseries.get&sql="+encodeURI(JSON.stringify(sqlList)).replace(/\+/g, '%2B'),
								success:function(res){
									if(res.code == "1"){
										//if(res.series.length > 0){
											self._drawDashboard(res.series);
										//}
									}else{
	                                	self._exalert(res.message);
	                                }
								}
							});
						},
						_drawDashboard:function(seriesList){
							var self = this;
							
							var myChart = echarts.getInstanceByDom(self.$el.find("#echartsBox")[0]);
							//清空以重绘
							myChart.clear();
							
							if(seriesList.length == 0){
								return;
							}
							
							//var titletext = seriesList[0].name;
							var titletext = "";
							
							var legendData = [];
							var xAxisData = [];
							var chartType = self.chartType;
							var seriesData = [];
							var isSmooth = false;
							var dataZoom = [];
							var seriesOptionBox = self.$el.find("#seriesOptionBox");
							var seriesBoxList = seriesOptionBox.find(".SID-seriesBox");
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
										if(columnName.indexOf("customValue") > -1){
											var fieldName = self.customMap[columnName];
											if(fieldName != undefined){
												columnName = fieldName;
											}else{
												columnName = "自定义字段";
											}
										}
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
							        	backgroundColor: '#F0F0F0',
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
							            yAxis: {},
//							            dataZoom: dataZoom,
							            series: seriesData
							        };
							}else if(chartType == "pie"){
								var oneData = [];
								for(var i=0;i<seriesList.length;i++){
									for(var k=1;k<seriesList[i].columns.length;k++){
										var columnName = seriesList[i].columns[k];
										if(columnName.indexOf("customValue") > -1){
											var fieldName = self.customMap[columnName];
											if(fieldName != undefined){
												columnName = fieldName;
											}else{
												columnName = "自定义字段";
											}
										}
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
							        	backgroundColor: '#F0F0F0',
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
										if(columnName.indexOf("customValue") > -1){
											var fieldName = self.customMap[columnName];
											if(fieldName != undefined){
												columnName = fieldName;
											}else{
												columnName = "自定义字段";
											}
										}
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
									self._exalert("当前仪表盘个数已大于9个");
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
                        _exalert:function(info,ischild,handler){
                        	var _self = this;
							fh.alert(info,true,handler,_self.dialog,null);
                        },
                        _exvalidate:function(){
                        	var self = this;
                        	var flag = true;
                        	if(self.$el.find(".SID-panelName").val().trim() == ""){
                        		self._exalert('图表名称不能为空');
                        		flag = false;
                        	}
                        	return flag;
                        },
						_sumbitForm : function(commonDialog){
							var param = "";
							var self = this;
							if(self._exvalidate()){
								var sqlList = self._seriesCheck();
								if(sqlList.length == 0){
									var myChart = echarts.getInstanceByDom(self.$el.find("#echartsBox")[0]);
									//清空
									myChart.clear();
									self._exalert('当前图表无任何系列');
									return;
								}
								//组织参数
								var chartType = 1;
								switch(self.chartType){
									case 'line':
										chartType = 1;
										break;
									case 'linesmooth':
										chartType = 2;
										break;
									case 'bar':
										chartType = 3;
										break;
									case 'pie':
										chartType = 4;
										break;
									case 'gauge':
										chartType = 5;
										break;
								}
								var dashboardPanel = {
										id: self.$el.find(".SID-id").val(),
										dashboardId: self.data.dashboardId,
										name: escape(self.$el.find(".SID-panelName").val().trim()),
										sequ: 0,
										chartType: chartType
								};
								
								var dashboardPanelSeries = [];
								var seriesOptionBox = self.$el.find("#seriesOptionBox");
								var seriesList = seriesOptionBox.find(".SID-seriesBox");
								for(var m=0;m<seriesList.length;m++){
									var oneSeries = {
											id: '',
											panelId: '',
											dashboardId: self.data.dashboardId,
											retentionPolicy: $(seriesList[m]).find(".SID-rp").val(),
											measurement: $(seriesList[m]).find(".SID-measurement").val().split("-")[0],
											whereSetting: $(seriesList[m]).find(".wherejson").val(),
											fieldsSetting: $(seriesList[m]).find(".fieldjson").val(),
											sql: $(seriesList[m]).find(".SID-statement").val()
									};
									dashboardPanelSeries.push(oneSeries);
								}
								var data = {
										dashboardPanel: dashboardPanel,
										dashboardPanelSeries: dashboardPanelSeries
								}
								var param = JSON.stringify(data);
								
				                var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								showCover();
	                            $.ajax({
	                                type : "POST",
	                                url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.dashboard.panel.save&dashboardPanelSaveJson="+param),
	                                success : function(response) {
	                                    if(response.code == "1"){
	                                    	hideCover();
	                                    	var opt = (self.$el.find(".SID-id").val() == "")?"新增":"编辑";
	                                    	self._exalert(opt+"图表成功",true,function(){
	                                    		dashboardPanel.id = response.id;
	                                    		if(self.$el.find(".SID-id").val() == ""){
	                                    			self.parentView._addGridStackWidget(dashboardPanel);
	                                    		}else{
	                                    			self.parentView._displayEcharts();
	                                    		}
	                                    		self.destroy();
		                                    	self.dialog.cancel();
	                                    	});
	                                    }else{
	                                    	hideCover();
	                                        self._exalert(response.message);
	                                    }
	                                },
	                                error : function(){
	                                	hideCover();
	                                    self._exalert("数据处理失败");
	                                }
	                            });
						    }
						}
					});
			return AddSnippetView;
		});
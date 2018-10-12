define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/trace/traceManageTemplate.html'
		  ,'views/trace/trace-detail-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination','jqueryUi','jqueryUiTimepicker'],
		function($, CommunicationBaseView,Template,TraceDetailView,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var TraceManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'change .SID-services': '_getSpans',
							'click .SID-search': 'holdList',
							'click .SID-trace-detail': 'openDetail',
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
							showCover("正在加载数据");
							var self = this;
							var template = _.template(Template);
							var html = template({});
							self.$el.append(html);
							
							//设置时间范围
							var curDate = new Date();
							$(".SID-timeEnd").val(self.getDateStr(curDate)).datetimepicker();
							curDate.setDate(curDate.getDate()-7);
							$(".SID-timeStart").val(self.getDateStr(curDate)).datetimepicker();
							
							
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.trace.servicelist",
								success:function(ajax){
									if(ajax.code == "1"){
										var servicelist = ajax.jsonArray;
										var servicelistStr = "";
										for(var i=0;i<servicelist.length;i++){
											servicelistStr += '<option value="'+servicelist[i]+'">'+servicelist[i]+'</option>';
										}
										self.$el.find(".SID-services").html(servicelistStr);
										hideCover();
										self._getSpans();
									}else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						_getSpans:function(){
							//showCover();
							var self = this;
							var serviceId = self.$el.find(".SID-services").val();
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.trace.getspans&id="+serviceId,
								success:function(ajax){
									if(ajax.code == "1"){
										var spanlist = ajax.jsonArray;
										var spanlistStr = '<option value="all">all</option>';
										for(var i=0;i<spanlist.length;i++){
											spanlistStr += '<option value="'+spanlist[i]+'">'+spanlist[i]+'</option>';
										}
										self.$el.find(".SID-spans").html(spanlistStr);
										hideCover();
									}else{
										hideCover();
										fh.alert(ajax.message);
									}
								}
							});
						},
						openDetail:function(e){
							var self = this;
							var traceId = $(e.currentTarget).attr("data-traceId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.trace.detail&id="+traceId,
								success:function(ajax){
									self.views.TraceDetailView = new TraceDetailView();
									ajax.title = "跟踪调用详情";
									self.views.TraceDetailView.render(self,ajax);
								}
							});
						},
						search : function(){
							var keyword = this.$el.find(".SID-keyword").val();
							this.$el.find(".SID-keyword").val(keyword.trim());
							this.holdList();
						},
						clearSearch : function(){
							this.$el.find(".SID-keyword").val("");
						},
						_openAddDialog : function(){
							var data = {
									title:"新增保留策略",
							}
							this.views.AddView = new AddView();
							this.views.AddView.render(this,data);
						},
						initSearchParam:function(){
							var param = "";
							param = $("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							return param;
						},
						holdList : function(){
							var self = this;
							var tableIndex = 0;
							var tableObj={};
							var param = this.initSearchParam();
							
							//处理zipkin其他请求参数
							var endStr = self.$el.find(".SID-timeEnd").val();
							var endTime = new Date(endStr);
							var endTs = endTime.getTime();
							var startStr = self.$el.find(".SID-timeStart").val();
							var startTime = new Date(startStr);
							var startTs = startTime.getTime();
							
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
							
							var lookback = endTs - startTs;
							var tracelimit = self.$el.find(".SID-limit").val();
							var minDuration = self.$el.find(".SID-duration").val();
							var serviceName = self.$el.find(".SID-services").val();
							var sortOrder = self.$el.find(".SID-sort").val();
							var spanName = self.$el.find(".SID-spans").val();

							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.trace.gettraceList&annotationQuery=" +
									"&endTs=" + endTs + "&tracelimit=" + tracelimit + "&lookback=" + lookback
								+ "&minDuration=" + minDuration + "&serviceName=" + serviceName + "&sortOrder="
								+ sortOrder + "&spanName=" + spanName;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"请求时长","sWidth":"10%","mDataProp":"duration","sDefaultContent": "" ,"sClass":"center","bSortable":true,"fnRender":function(o,val){
								var duration = o.aData.duration;
								var finalduration = "";
								if(duration > 999999){
									finalduration += Math.round(duration/1000)/1000+"s";
								}else if(duration > 999){
									finalduration += duration/1000+"ms";
								}else{
									finalduration += duration+"μs"
								}
								return finalduration;
							}},
							{"sTitle":"请求占比","sWidth":"10%","mDataProp":"percent","sDefaultContent": "" ,"sClass":"center","bSortable":true,"fnRender":function(o,val){
								return o.aData.percent;
							}},
							{"sTitle":"请求信息个数","sWidth":"10%","mDataProp":"spans","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								
								return o.aData.spans;
							}},
							{"sTitle":"发起时间","sWidth":"20%","mDataProp":"time","sDefaultContent": "" ,"sClass":"center","bSortable":true,"fnRender":function(o,val){
								
								return o.aData.time;
							}},
							{"sTitle":"操作","sWidth":"10%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
//								var str = '<a href="javascript:void(0)" class="SID-trace-detail" data-traceId="'+o.aData.traceId+'"><span class="fhicon-eye"></span>查看</a>';
//								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
//								return retBox;
								var str = '<a href="javascript:void(0)" class="SID-trace-detail" data-traceId="'+o.aData.traceId+'" style="text-decoration: none;"><span class="fhicon-eye"></span>查看</a>';
								return str;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.traceInfoList ? data.traceInfoList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return TraceManageSnippetView;
		});
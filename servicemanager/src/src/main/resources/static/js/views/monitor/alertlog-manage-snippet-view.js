define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/alertlogManageTemplate.html'
		  ,'views/monitor/view-json-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,viewJSONView,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var RetentionManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-alertlog-view':'_viewAlertLog',
						},
						initialize : function() {
							this.views = {};
							this.datatable = {};
							this.datajson = [];
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
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							this.holdList();
						},
                        _viewAlertLog:function(e){
                        	var self = this;
                        	var id = $(e.currentTarget).attr("data-id");
                        	var resultJson = {};
                        	for(var i=0;i<self.datajson.length;i++){
                        		if(self.datajson[i].id == id){
                        			resultJson = JSON.parse(self.datajson[i].alertData);
                        		}
                        	}
                        	
                        	//处理时区问题
                        	var tmptime = resultJson.time;
							resultJson.time = self._resolvetime(tmptime);
                        	
                        	var tempvalues = resultJson.data.series[0].values;
                        	for(var i=0;i<tempvalues.length;i++){
                        		var temptime = tempvalues[i][0];//.replaceAll("T"," ").replaceAll("Z","").split(".")[0];
								tempvalues[i][0] = self._resolvetime(temptime);
                        	}
                        	resultJson.data.series[0].values = tempvalues;
                        	
                        	var data = {
									title:"查看详细数据",
									jsonData:resultJson,
									emphasizeList:["id","message","level","data"]
							}
                        	self.views.viewJSONView = new viewJSONView();
                        	self.views.viewJSONView.render(self,data);
                        },
                        _resolvetime : function(time){
                        	var date = new Date(time);
                    		//date.setHours(date.getHours()+8);
							var s = "-";
							var ss = ":";
							var h = (date.getHours()<10)?("0"+date.getHours()):date.getHours();
							var m = (date.getMinutes()<10)?("0"+date.getMinutes()):date.getMinutes();
							var sc = (date.getSeconds()<10)?("0"+date.getSeconds()):date.getSeconds();
							var resolvedtime = date.getFullYear()+s+((date.getMonth()<9)?("0"+(date.getMonth()+1)):date.getMonth()+1)+s+((date.getDate()<10)?("0"+date.getDate()):date.getDate())+" "+h+ss+m+ss+sc;
							return resolvedtime;
                        },
						search : function(){
							var keyword = this.$el.find(".SID-keyword").val();
							this.$el.find(".SID-keyword").val(keyword.trim());
							this.holdList();
						},
						clearSearch : function(){
							this.$el.find(".SID-keyword").val("");
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
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.alertlog.query" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"规则名称","sWidth":"20%","mDataProp":"ruleName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"告警级别","sWidth":"10%","mDataProp":"alertLevel","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var result = "提示";
								switch(val){
									case 'INFO':
										result = '提示';
										break;
									case 'WARNING':
										result = '警告';
										break;
									
								}
								return result;
							}},
							{"sTitle":"告警消息","sWidth":"35%","mDataProp":"message","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"告警时间","sWidth":"10%","mDataProp":"alertTime","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return self._resolvetime(val);
							}},
							{"sTitle":"详细数据","sWidth":"8%","mDataProp":"alertData","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" style="text-decoration:none" class="SID-alertlog-view" data-id="'+o.aData.id+'"><span class="fhicon-eye"></span>查看</a>';
								return str;
							}},
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.alertlogsList ? data.alertlogsList : ''
								};
								self.datajson = jsonData.aaData;
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return RetentionManageSnippetView;
		});
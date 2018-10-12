define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/law/rightTemplate.html','util/datatableUtil','views/law/law-detail-snippet-view','datatable_lnpagination','views/law/law-goodsdetail-snippet-view'],
		function($, CommunicationBaseView,TemplateRight,datatableUtil,LawDetailSnippetView,datatableLnpagination,GoodsDetailSnippetView) {
			var RightsideSnippetView = CommunicationBaseView
			.extend({
				events : {
					'click .SID-search' : '_onClickSearch',
					'click .SID-clearSearch' : '_onClickClearSearch',
					'click .SID-lawDetail' : '_onClickLawDetail',
					'click .SID-goodsDetail' : '_onClickGoodsDetail'
				},
				initialize : function() {
					this.parentObj;
					this.datatable = {};
					this.state = "";
				},
				render : function(parentObj,state) {
					this.parentObj = parentObj;
					this.state = state;
					this.setContentHTML();
					this.lawList()
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
					this.uiRanderUtil.randerJQueryUI_DateRange(this,".SID-startTime",".SID-endTime","yy-mm-dd");
				},
				_onClickSearch : function(){
					this.lawList();
				},
				_onClickClearSearch : function(){
					this.$el.find(".SID-declNo").val("");
					this.$el.find(".SID-name").val("");
					this.$el.find(".SID-startTime").val("");
					this.$el.find(".SID-endTime").val("");
				},
				_onClickLawDetail:function(e){
					var self = this;
					var obj = $(e.target);
					var declNo =obj.attr("data-declNo");
					if(declNo == ""){
						return;
					}
					var appContext = self.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					$.ajax({
						type:"POST",
						url:servicePath+"?"+ropParam+ "&method=mapps.ydzf.query.detail&declNo="+declNo,
						success:function(ajax){
							self.lawDetailSnippetView = new LawDetailSnippetView();
							self.lawDetailSnippetView.render(ajax);
						}
					});
				},
				_onClickGoodsDetail:function(e){
					var self = this;
					var obj = $(e.target);
					var declNo =obj.attr("data-declNo");
					if(declNo == ""){
						return;
					}
					var appContext = self.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					$.ajax({
						type:"POST",
						url:servicePath+"?"+ropParam+ "&method=mapps.ydzf.query.goodsdetail&declNo="+declNo,
						success:function(ajax){
							self.goodsDetailSnippetView = new GoodsDetailSnippetView();
							self.goodsDetailSnippetView.render(ajax);
						}
					});
				},
				_initSearchParam:function(){
					var param = "";
					var declNo = this.$el.find(".SID-declNo").val();
					var name = this.$el.find(".SID-name").val();
					var startTime = this.$el.find(".SID-startTime").val();
					var endTime = this.$el.find(".SID-endTime").val();
					if (declNo != "") 
						param += "&declNo=" + encodeURIComponent(declNo);
					if (name != "") 
						param += "&name=" + encodeURIComponent(name);
					if (startTime != "") 
						param += "&startTime=" + startTime;
					if (endTime != "") 
						param += "&endTime=" + endTime;
					return param;
				},
				lawList : function(){
					var tableObj={};
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var param = this._initSearchParam();
					var url = servicePath + "?" + ropParam + "&v=1.0&method=mapps.ydzf.query.list&status="+this.state+ param;
					tableObj.url = url;
					//控制是否可分页
					tableObj.bPaginate = true;
					// tableObj.aaSorting = [[2,'asc']];
					tableObj.aoColumns=[
					{"sTitle":"报检号","sWidth":"15%","mDataProp":"declNo","sDefaultContent": "" ,"sClass":"center","bSortable":false},
					{"sTitle":"第一检查人","sWidth":"10%","mDataProp":"receiverDocName","sDefaultContent": "" ,"sClass":"center","bSortable":false},
					{"sTitle":"第二检查人","sWidth":"10%","mDataProp":"inspectorName","sDefaultContent": "" ,"sClass":"center","bSortable":false},
					{"sTitle":"报检单位","sWidth":"20%","mDataProp":"declRegName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
					{"sTitle":"生产商","sWidth":"20%","mDataProp":"mnufctrRegName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
					{"sTitle":"查验时间","sWidth":"15%","mDataProp":"confmTimeStr","sDefaultContent": "","sClass":"center"},
					{"sTitle":"操作","sWidth":"10%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
						var str = "";
						str = '<a href="javascript:void(0)" class="SID-lawDetail"  data-declNo="'+o.aData.declNo+'"><span class="fhicon-eye"></span>查看详情</a>'+
							  '<a href="javascript:void(0)" class="SID-goodsDetail"  data-declNo="'+o.aData.declNo+'"><span class="fhicon-eye"></span>货集信息</a>';
						var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
						return retBox;
					}}
					];
					var param="";
					var jsonProc = function(data) {
						var jsonData = {
								"iTotalDisplayRecords" : data.total ? data.total : 0,
										"iTotalRecords" : data.total ? data.total : 0,
												"aaData" : data.lawList ? data.lawList : ''
						};
						return jsonData;
					}
					datatable=datatableUtil(tableObj,param,jsonProc);
				},
				refreshTable : function() {
					this.lawList();
				}
	
			});
			return RightsideSnippetView;
		});
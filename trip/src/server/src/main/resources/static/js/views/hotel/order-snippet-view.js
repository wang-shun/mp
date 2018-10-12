define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/hotel/orderTemplate.html',
		  'util/datatableUtil','datatable_lnpagination','views/hotel/order-view-snippet-view'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination,OrderViewSnippetView) {
			var treeObj;
			var OrderSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-order-detail': 'orderDetail',
							'click .SID-order-delete': 'orderDelete'
						},
						initialize : function() {
							this.chaildView = {};
							this.datatable = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							this.orderList();
							return this;
						},
						refresh : function() {
						},
						refreshTable : function() {
							this.orderList();
						},
						destroyBusinessViews : function(){
							$.each(this.chaildView, function(index, view) {
								 view.destroy();
							});		
							this.chaildView = {};
						},		
						destroy : function() {
							this.destroyBusinessViews()
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						subscribeEvents : function() {
							var self = this;
							this.eventHub.subscribeEvent('REFRESH_MEETINGLIST', function(operate) {
								self.meetingList(self, operate);
							});
						},
						setContentHTML : function (){
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);

							this.uiRanderUtil.randerJQueryUI_DateRange(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
							//this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate","month");
						},
						search : function(){
							this.orderList(this,'search');
						},
						clearSearch : function(){
							this.$el.find(".SID-userName").val("");
							this.$el.find(".SID-beginDate").val("");
							this.$el.find(".SID-endDate").val("");
						},
						initSearchParam:function(){
							var param = "";
							var userName = $(".SID-userName").val();
							var beginDate = $(".SID-beginDate").val();
							var endDate = $(".SID-endDate").val();
							var orderState = $(".SID-orderState").val();
							if (userName!="") {
								param += "&orderCode="+userName;
							}
							if (beginDate!="") {
								param += "&arrivalStart="+beginDate;
							}
							if (endDate!="") {
								param += "&arrivalEnd="+endDate;
							}
							if (orderState!="") {
								param += "&orderStatus="+orderState;
							}
							return param;
						},
						orderDetail : function(e){
							var self = this;
							var obj = $(e.target);
							var orderId =obj.attr("data-orderId");
							if(orderId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.trip.orderdetail.bt&orderId="+orderId,
								success:function(ajax){
									self.chaildView.orderViewSnippetView = new OrderViewSnippetView();
									self.chaildView.orderViewSnippetView.render(ajax);
								}
							});
						},
						orderDelete : function(e){
							var self = this;
							var obj = $(e.target);
							var orderId =obj.attr("data-orderId");
							if(orderId == ""){
								return;
							}
							
							fh.confirm("是否确定取消订单？",function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.trip.ordercancel&orderId="+orderId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("取消成功",false,function(){
												self.orderList();
											});
										}else{
											fh.alert("取消失败："+ajax.message);
										}
										
									}
								});
							},false);
							
							
						},
						orderList : function(that,operate){
							var tableObj={};
							
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.trip.orderlistall.bt" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"订单编号","sWidth":"120px","mDataProp":"orderCode","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-order-detail" data-orderId="'+o.aData.orderID+'">'+val+'</a>';
								return str;
							}},
							{"sTitle":"酒店名称","sWidth":"180px","mDataProp":"hotelName","sDefaultContent": "" ,"bSortable":false,"sClass":"left"},
							{"sTitle":"入住日期","sWidth":"100px","mDataProp":"checkInTimeString","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"离店日期","sWidth":"100px","mDataProp":"checkOutTimeString","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"入住人","sWidth":"80px","mDataProp":"guestsName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"联系电话","sWidth":"60px","mDataProp":"contactPhone","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"订单状态","sWidth":"100px","mDataProp":"orderStatus","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "未知"+ o.aData.orderStatus;
								var state = o.aData.orderStatus;
								if(state == 1){
							    	str = "待付款"　;
							    }else if(state == 2){
							    	str = "本地取消"　;
							    }else if(state == 3){
							    	str = "待酒店确认"　;
							    }else if(state == 4){
							    	str = "预定成功"　;
							    }else if(state == 5){
							    	str = "预定失败"　;
							    }else if(state == 100){
							    	str = "酒店已取消"　;
							    }else if(state == 101){
							    	str = "noshow"　;
							    }else if(state == 102){
							    	str = "在住"　;
							    }else if(state == 103){
							    	str = "离店"　;
							    }else if(state == 104){
							    	str = "已评论"　;
							    }
								return str;
							}},
							{"sTitle":"操作","sWidth":"60px","mDataProp":"hotelID","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								
								
								var str = '<a href="javascript:void(0)" class="SID-order-detail" data-orderId="'+o.aData.orderID+'"><span class="fhicon-eye"></span>查看</a>';
								//console.log(o.aData.orderStatus);
								if(o.aData.orderStatus == '预定成功' ){
									str+='<a href="javascript:void(0)" class="SID-order-delete" data-orderId="'+o.aData.orderID+'"><span class="fhicon-delete"></span>取消</a>' ;
								}
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.list ? data.list : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							//console.log(this.datatable);
						}
			
					});
			return OrderSnippetView;
		});
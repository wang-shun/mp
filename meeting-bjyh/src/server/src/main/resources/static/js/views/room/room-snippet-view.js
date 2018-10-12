define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/room/roomTemplate.html','views/room/room-add-snippet-view',
		  'views/room/room-view-snippet-view','util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,RoomAddSnippetView,RoomViewSnippetView,datatableUtil,datatableLnpagination) {
			var treeObj;
			var RoomSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-meet-add': 'addInit',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-meet-detail': 'roomDetail',
							'click .SID-meet-delete': 'roomDelete'
						},
						initialize : function() {
							this.chaildView = {};
							this.datatable = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							this.roomList();
							return this;
						},
						refresh : function() {
						},
						refreshTable : function() {
							this.roomList();
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
						},
						search : function(){
							this.roomList(this,'search');
						},
						clearSearch : function(){
							this.$el.find(".SID-roomName").val("");
							this.$el.find(".SID-address").val("");
							this.$el.find(".SID-projector").removeAttr("checked");
							this.$el.find(".SID-display").removeAttr("checked");
							this.$el.find(".SID-microphone").removeAttr("checked");
							this.$el.find(".SID-stereo").removeAttr("checked");
							this.$el.find(".SID-wifi").removeAttr("checked");
							this.$el.find(".SID-roomType").val("");
						},
						addInit : function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.meetingroom.room.detail&roomId="+roomId,
									success:function(ajax){
										self.chaildView.roomAddSnippetView = new RoomAddSnippetView();
										self.chaildView.roomAddSnippetView.render(self,ajax);
									}
								});
							}else{
								this.chaildView.roomAddSnippetView = new RoomAddSnippetView();
								this.chaildView.roomAddSnippetView.render(this,roomId);
							}
						},
						roomDetail : function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.meetingroom.room.detail&roomId="+roomId,
								success:function(ajax){
									self.chaildView.roomViewSnippetView = new RoomViewSnippetView();
									self.chaildView.roomViewSnippetView.render(ajax);
								}
							});
						},
						roomDelete:function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							fh.confirm('确定删除会议室吗？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.meetingroom.room.delete&roomId="+roomId,
									success:function(ajax){
										if(ajax.code == "300005"){
											fh.alert("会议室处于预定中！");
										}else if(ajax.code == "1"){
											fh.alert("删除会议室成功！",false,function(){
												self.roomList();
											});
										}else{
											fh.alert(ajax.message,false,function(){
												self.roomList();
											});
										}
									},
									error:function(){
										fh.alert("删除会议室失败！");
									}
								});
							});
						},
						initSearchParam:function(){
							var param = "";
							param = $("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							return param;
						},
						roomList : function(that,operate){
							var tableObj={};
							
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var url = servicePath + "?format=json&v=1.0&appKey=&method=mapps.meetingroom.room.queryweb" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"会议室名称","sWidth":"15%","mDataProp":"roomName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"会议室地址","sWidth":"15%","mDataProp":"address","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"面积","sWidth":"9%","mDataProp":"area","sDefaultContent": "" ,"sClass":"right"},
							{"sTitle":"容量","sWidth":"9%","mDataProp":"capacity","sDefaultContent": "" ,"sClass":"right"},
							{"sTitle":"投影","sWidth":"9%","mDataProp":"projector","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return val=="1"?"有":"无";
							}},
							{"sTitle":"显示","sWidth":"9%","mDataProp":"display","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return val=="1"?"有":"无";
							}},
							{"sTitle":"麦克风","sWidth":"9%","mDataProp":"microphone","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return val=="1"?"有":"无";
							}},
							{"sTitle":"音响","sWidth":"9%","mDataProp":"stereo","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return val=="1"?"有":"无";
							}},
							{"sTitle":"无线网络","sWidth":"9%","mDataProp":"wifi","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return val=="1"?"有":"无";
							}},
							{"sTitle":"类型","sWidth":"9%","mDataProp":"roomType","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return val=="1"?"特殊":"普通";
							}},
							{"sTitle":"操作","sWidth":"7%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-meet-detail" data-roomId="'+o.aData.roomId+'"><span class="fhicon-eye"></span>查看</a><a href="javascript:void(0)" class="SID-meet-add" data-roomId="'+o.aData.roomId+'"><span class="fhicon-pencil"></span>编辑</a><a href="javascript:void(0)" class="SID-meet-delete" data-roomId="'+o.aData.roomId+'"><span class="fhicon-delete"></span>删除</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.room ? data.room : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						}
			
					});
			return RoomSnippetView;
		});
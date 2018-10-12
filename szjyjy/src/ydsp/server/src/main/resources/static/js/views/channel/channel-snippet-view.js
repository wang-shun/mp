define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/channel/channelTemplate.html','views/channel/channel-add-snippet-view',
		  'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,ChannelAddSnippetView,datatableUtil,datatableLnpagination) {
			var treeObj;
			var ChannelSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-channel-add': 'addInit',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-channel-delete': 'channelDelete',
							'click .SID-end-meeting': 'endMeeting'
								
						},
						initialize : function() {
							this.chaildView = {};
							this.datatable = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							this.channelList();
							return this;
						},
						refresh : function() {
						},
						refreshTable : function() {
							this.channelList();
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
							this.channelList(this,'search');
						},
						clearSearch : function(){
							this.$el.find(".SID-channel").val("");
						},
						addInit : function(e){
							var self = this;
							var obj = $(e.target);
							var hostId =obj.attr("data-hostId");
							this.chaildView.channelAddSnippetView = new ChannelAddSnippetView();
							this.chaildView.channelAddSnippetView.render(this,hostId);
								
						},
						channelDelete:function(e){
							var self = this;
							var obj = $(e.target);
							var hostId =obj.attr("data-hostId");
							if(hostId == ""){
								return;
							}
							fh.confirm('确定删除视频通道吗？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.channel.delete&hostId="+hostId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("删除视频通道成功！",false,function(){
												self.channelList();
											});
										}else{
											fh.alert(ajax.message,false,function(){
												self.channelList();
											});
										}
									},
									error:function(){
										fh.alert("删除视频通道失败！");
									}
								});
							});
						},
						endMeeting:function(e){
							var self = this;
							var obj = $(e.target);
							var hostId =obj.attr("data-hostId");
							var meetingId =obj.attr("data-meetingId");
							if(hostId == ""){
								return;
							}
							if(meetingId == ""){
								return;
							}
							fh.confirm('确定强制中断会议吗？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.channel.endMeeting&hostId="+hostId+"&meetingId="+meetingId,
									success:function(ajax){
										if(ajax.code == "1"){
											fh.alert("强制中断成功！",false,function(){
												self.channelList();
											});
										}else{
											fh.alert(ajax.message,false,function(){
												self.channelList();
											});
										}
									},
									error:function(){
										fh.alert("强制中断失败！");
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
						channelList : function(that,operate){
							var tableObj={};
							
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var url = servicePath + "?format=json&v=1.0&appKey=&method=mapps.channel.query" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"视频通道名称","sWidth":"15%","mDataProp":"channel","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"帐号","sWidth":"15%","mDataProp":"hostId","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"容量","sWidth":"9%","mDataProp":"capacity","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"使用次数","sWidth":"9%","mDataProp":"meetings","sDefaultContent": "0" ,"sClass":"center","bSortable":false},
							{"sTitle":"使用时长","sWidth":"9%","mDataProp":"meetingMinutes","sDefaultContent": "0" ,"sClass":"center","bSortable":false},
							{"sTitle":"创建时间","sWidth":"9%","mDataProp":"createTimeString","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"当前状态","sWidth":"9%","mDataProp":"status","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var status = "闲置";
								if(o.aData.status=="1"){
									status = "进行中";
								}
								return status;
							}},
							{"sTitle":"最后使用人","sWidth":"9%","mDataProp":"userCode","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"操作","sWidth":"7%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str ="";
								if(o.aData.status=="进行中"){
									str = '<a href="javascript:void(0)" class="SID-end-meeting" data-hostId="'+o.aData.hostId+'" data-meetingId="'+o.aData.meetingId+'"><span class="fhicon-eye"></span>强制中断</a><a href="javascript:void(0)" class="SID-channel-add" data-hostId="'+o.aData.hostId+'"><span class="fhicon-pencil"></span>编辑</a><a href="javascript:void(0)" class="SID-channel-delete" data-hostId="'+o.aData.hostId+'"><span class="fhicon-delete"></span>删除</a>';
								}else{
									str = '<a href="javascript:void(0)" class="SID-channel-add" data-hostId="'+o.aData.hostId+'"><span class="fhicon-pencil"></span>编辑</a><a href="javascript:void(0)" class="SID-channel-delete" data-hostId="'+o.aData.hostId+'"><span class="fhicon-delete"></span>删除</a>';
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
														"aaData" : data.channelList ? data.channelList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						}
			
					});
			return ChannelSnippetView;
		});
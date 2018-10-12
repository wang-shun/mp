define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/tagManageTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var TagManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-add-btn': '_openAddDialog',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-tag-save':'_onSaveTag',
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
						setContentHTML : function (){
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							this.holdList();
						},
						_onClickMeetingEdit:function(e){
							var self = this;
							var meetingId = $(e.target).attr("data-meetingId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.detailForWeb&meetingId="+meetingId,
								success:function(ajax){
									if(ajax.code == '1'){
										self._openEditDialog(ajax);
									}
								},
								error:function(){}
							});
						},
						_onSaveTag:function(e){
							var self = this;
							var tagId = $(e.currentTarget).attr("data-id");
							var systemId = $(e.currentTarget).attr("data-systemId");
							var tag = $(e.currentTarget).attr("data-tag");
							var tagName = escape($(e.currentTarget).parent().parent().find(".SID-tag-name").val());
							var tagNameTarget = $(e.currentTarget).parent().parent().find(".SID-tag-name");
							if(tagName == ""){
								fh.alert("tag名称字段为必填项",false,function(){
									$(tagNameTarget[0]).focus();
								});
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.tag.save&id="+tagId+"&name="+tagName+"&systemId="+systemId+"&tag="+tag),
								success:function(ajax){
									if(ajax.code=="1"){
										self.holdList();
                                	}else{
						    			fh.alert(ajax.message);
						    		}
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
									title:"新增指标",
									op:"新增"
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
							var tableIndex = 0;
							var tableObj={};
							var param = this.initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.tag.query" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"tag","sWidth":"40%","mDataProp":"tag","sDefaultContent": "" ,"sClass":"left"},
							{"sTitle":"tag名称","sWidth":"40%","mDataProp":"name","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								var str = '<input class="SID-tag-name" style="border-width:0;padding:0;margin:0;width:100%;background-color:transparent;" maxlength="80" value="'+val+'"></input>';
								return str;
							}},
							{"sTitle":"操作","sWidth":"10%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" style="text-decoration:none" class="SID-tag-save" data-systemId="'+o.aData.systemId+'" data-tag="'+o.aData.tag+'" data-id="'+o.aData.id+'"><span class="fhicon-active"></span>保存</a>';
								return str;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.tagList ? data.tagList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return TagManageSnippetView;
		});
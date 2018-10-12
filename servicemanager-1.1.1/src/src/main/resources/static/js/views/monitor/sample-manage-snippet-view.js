define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/sampleManageTemplate.html'
		  ,'views/monitor/add-sample-snippet-view'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,AddView,datatableUtil,datatableLnpagination) {
			var treeObj; 
			var timeTable = new Array();
			var SampleManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-add-btn': '_openAddDialog',
							'click .SID-search' : 'search',
							'click .SID-clearSearch' : 'clearSearch',
							'click .SID-sample-start':'_onClickStart',
							'click .SID-sample-stop':'_onClickStop',
							'click .SID-sample-edit':'_onClickEdit',
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
						_onClickEdit:function(e){
							var self = this;
							var databaseId = $(e.target).attr("data-databaseId");
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.sample.detail&id="+databaseId,
								success:function(ajax){
									self.views.AddView = new AddView();
									ajax.title = "采样设置详情";
									self.views.AddView.render(self,ajax);
								}
							});
						},
						_onClickStop:function(e){
							var self = this;
							fh.confirm('禁用此采样设置？',function(){
								var id = $(e.target).attr("data-id");
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.sample.disable&id="+id,
									success:function(ajax){
										if(ajax.code=="1"){
											self.holdList();
	                                	}else{
							    			fh.alert(ajax.message);
							    		}
									}
								});
							});
						},
                        _onClickStart:function(e){
                        	var self = this;
                        	fh.confirm('启用此采样设置？',function(){
                        		var id = $(e.target).attr("data-id");
                                var appContext = self.getAppContext();
                                var servicePath = appContext.cashUtil.getData('servicePath');
                                var ropParam = appContext.cashUtil.getData('ropParam');
                                $.ajax({
                                    type:"POST",
                                    url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.sample.enable&id="+id,
                                    success:function(ajax){
                                    	if(ajax.code=="1"){
    										self.holdList();
                                    	}else{
    						    			fh.alert(ajax.message);
    						    		}
                                    }
                                });
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
									title:"新增采样设置",
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
							var url = servicePath + "?" + ropParam + "&method=mapps.servicemanager.sample.query" + param;// + param;
							tableObj.tbID="meet-hold";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"采样生成指标","sWidth":"40%","mDataProp":"cqName","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"采样sql","sWidth":"40%","mDataProp":"sampleSql","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"采样说明","sWidth":"40%","mDataProp":"remarks","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								return unescape(val).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
							}},
							{"sTitle":"状态","sWidth":"8%","mDataProp":"enabled","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								var str = "";
								if(val == 1)
									str="<span>可用</span>";
								if(val == 0)
									str="<span style=\"color:red\">禁用</span>";
								return str;
							}},
							{"sTitle":"操作","sWidth":"10%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = "";
								if(o.aData.enabled =='<span>可用</span>')
									str += '<a href="javascript:void(0)" class="SID-sample-stop" data-id="'+o.aData.id+'"><span class="fhicon-delete"></span>禁用</a>';
								if(o.aData.enabled =='<span style=\"color:red\">禁用</span>')
									str += '<a href="javascript:void(0)" class="SID-sample-start" data-id="'+o.aData.id+'"><span class="fhicon-active"></span>启用</a>';
								str += '<a href="javascript:void(0)" class="SID-sample-edit" data-databaseId="'+o.aData.id+'"><span class="fhicon-pencil"></span>修改</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.sampleList ? data.sampleList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						}
			
					});
			return SampleManageSnippetView;
		});
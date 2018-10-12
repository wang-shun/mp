define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/tree/deptTreeTemplate.html',"jquery_ztree_core", 'jquery_ztree_excheck',
			'jquery_ztree_exhide'],
		function($, CommunicationBaseView,Template,ZtreeCore) {
			var treeObj;
			var datatable;
			var DeptTreeSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.parentObj;
							this.depUuids=[];
							this.domObj;
						},
						render : function(parentObj,depUuids,obj) {
							this.parentObj = parentObj;
							this.depUuids = depUuids;
							this.domObj = obj
							this.setContentHTML();
							return this;
						},
						sumbitForm : function(commonDialog){
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							var nodes = treeObj.getCheckedNodes();
							this.parentObj._setDep(nodes,this.domObj);
							commonDialog.cancel();
						},
						refresh : function() {
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						setContentHTML : function (){
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var url = servicePath + "?" + ropParam + "&method=mapps.thirdpart.mobileark.getdepartments";
				            $.ajax({
								type : "POST",
								url : url,
								success : function(data) {
									self.parentObj.hideLoading();
									if (data.code == "1") {
										var template = _.template(Template);
										var html = template({});
										var commonDialog = fh.servicemanagerOpenDialog('orgAddDialog', "选择部门", 500, 600, html);
										commonDialog.addBtn("cannel","关闭",function(){
											commonDialog.cancel();
										});
										commonDialog.addBtn("ok","确定",function(){
											self.sumbitForm(commonDialog);
										});
										self._drawTree(data.depList);
									} else {
										fh.alert(data.message,false,function(){
											self.destroy();
										});
									}
								},
								error : function(){
									self.parentObj.hideLoading();
									fh.alert("数据获取失败",false,function(){
										self.destroy();
									});
								}
							});
						},
						_drawTree : function(data){
							var self = this;
							var setting = {
								view: {
									showLine: false,
									showIcon: true,
									selectedMulti: false,
									dblClickExpand: false
								},					
								data: {
									simpleData: {
										enable: true,
										idKey: "depUuid",
										pIdKey: "parentId",
										rootPId: -1
									},
									key: {
										name: "depName"
									}						
								},
								check : {
									enable : true,
									chkStyle : "checkbox",
									autoCheckTrigger : true,
									chkboxType: { "Y": "", "N": "" }
								},
								callback : {
									onClick : this._zTreeOnClick
								}				
							}
							
							var zNodes =data;
							$.fn.zTree.init($("#treeDemo"), setting, zNodes);
							this._initChecked();
						},
						_initChecked:function(){
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							for(var i=0,depUuid;depUuid=this.depUuids[i++];){
								var selectNode = treeObj.getNodeByParam("depUuid",depUuid, null);
								treeObj.checkNode(selectNode,true, true);
							}
						},
						_zTreeOnClick :function(event, treeId, treeNode){
						}
			
					});
			return DeptTreeSnippetView;
		});
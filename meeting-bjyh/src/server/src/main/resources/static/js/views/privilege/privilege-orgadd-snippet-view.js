define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/privilege/orgAddTemplate.html',"jquery_ztree_core", 'jquery_ztree_excheck',
			'jquery_ztree_exhide'],
		function($, CommunicationBaseView,Template,ZtreeCore) {
			var treeObj;
			var datatable;
			var RoomAddSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.parentObj;
							this.depUuids=[];
						},
						render : function(parentObj,depUuids) {
							this.parentObj = parentObj;
							this.depUuids = depUuids;
							this.setContentHTML();
							return this;
						},
						sumbitForm : function(commonDialog){
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							var nodes = treeObj.getCheckedNodes();
							this.parentObj._setDep(nodes);
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
				            var url = servicePath + "?" + ropParam + "&method=mapps.mr.thirdpart.mobileark.getdepartments";
				            $.ajax({
								type : "POST",
								url : url,
								success : function(data) {
									if (data.code == "1") {
										var template = _.template(Template);
										var html = template({});
										var commonDialog = fh.commonOpenDialog('orgAddDialog', "组织架构选择", 500, 600, html);
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
									onCheck : zTreeChkDisabled
								}				
							}
							//节点选中回调方法
							function zTreeChkDisabled(event, treeId, treeNode) {
								var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								//当前节点是否选中
								var curNodeCheck = treeNode.checked;
								//将所有子节点灰掉不可操作，并取消原来选中的节点
								chkChildrenDisabled(treeObj, treeNode, curNodeCheck);
							};
							//递归设置子节点不可操作，并取消已经选中的节点
							function chkChildrenDisabled(treeObj, treeNode, curNodeCheck) {
								var childSize = 0;
								if (treeNode.children != null) {
									childSize = treeNode.children.length;
								}
								if (null != treeNode && childSize > 0) {
									for (var i = 0; i < childSize; i++) {
										var child = treeNode.children[i];
										if (curNodeCheck) {
											//取消原来选中的节点
											child.checked = false;
											//设置为不可选
											treeObj.setChkDisabled(child, true);
										} else {
											//设置为可选
											treeObj.setChkDisabled(child, false);
										}
										if (child.children != null) {
											chkChildrenDisabled(treeObj, child, curNodeCheck);
										}
									}
								}
							};
							var zNodes =data;
							$.fn.zTree.init($("#treeDemo"), setting, zNodes);
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							for(var i=0,depUuid;depUuid=this.depUuids[i++];){
								var selectNode = treeObj.getNodeByParam("depUuid",depUuid, null);
								treeObj.checkNode(selectNode,true, true);
								//将子节点置灰不可操作
								zTreeChkDisabled(null, null, selectNode);
							}
						},
						_initChecked:function(){
							var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							for(var i=0,depUuid;depUuid=this.depUuids[i++];){
								var selectNode = treeObj.getNodeByParam("depUuid",depUuid, null);
								treeObj.checkNode(selectNode,true, true);
								//将子节点置灰不可操作
								zTreeChkDisabled(null, null, selectNode);
							}
						}
			
					});
			return RoomAddSnippetView;
		});
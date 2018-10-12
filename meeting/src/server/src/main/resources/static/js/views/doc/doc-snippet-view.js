define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/doc/docTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template
				,datatableUtil,datatableLnpagination) {
			var datatable;
			var DocSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-refresh':'refresh',
							'click .SID-search-btn':'_searchDoc',
							'click .SID-folder':'_reviewFolder',
							'click .SID-toFolder':'_linkData',
							'click .SID-back':'_upper',
							'click .SID-go':'_down'
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.fileIds=[];
							this.historyIds=[];
							this.historyNames=[];
							this.historyNamesGo=[];
							this.recentlyOpenedfileList = [];
							this.recentlyOpenedfolderList = [];
						},
						render : function(parentView,fileIds,recentlyOpenedfileList) {
							this.parentView = parentView;
							this.fileIds = fileIds;
							this.recentlyOpenedfileList = recentlyOpenedfileList;
							this._setContentHTML();
							return this;
						},
						destroy : function() {
							this._destroyBusinessViews();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						_destroyBusinessViews : function() {
							$.each(this.childView, function(index, view) {
								view.destroy();
							});
						},
						refresh : function() {
							if (this.$el.find(".SID-param").val()==""){
								var folderid = this.$el.find(".SID-crumbs a:last").attr("data-folderid");
								this._initDocTable(folderid);
							} else {
								this._searchDoc();
							}
						},
						_searchDoc:function(){
							if (this.$el.find(".SID-param").val()==""){
								this.$el.find(".upload-content").removeClass("hide");
							} else {
								this.$el.find(".upload-content").addClass("hide");
							}
							this._initDocTable();
							this.historyNames=[];
							this.historyNamesGo=[];
							this._setGoBackBtn();
						},
						_setContentHTML : function (){
							var self = this;
							var html = _.template(Template, {
								'imgPath': self.constants.IMAGEPATH
							});
							this.$el.append(html);
							var commonDialog =fh.commonOpenDialog('roomAddDialog1', '会议文档', 780, 500, this.el);
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("ok","确定",function(){
								self._sumbitForm(commonDialog);
							});
							this._initDocTable();
						},
						_saveFile:function(){
							var tableObj={};
							tableObj.tableObjID = "grid-table1";
							// 当前页全部数据
							var allArr = [];
							var allObjs = getAllObjs(this.datatable,tableObj);
							for ( var i = 0; i < allObjs.length; i++) {
								allArr.push(allObjs[i].docId);
							}
							// 当前页选中数据
							var selArr=[];
							var objs = getSeleteObjs(this.datatable,tableObj);
							for ( var i = 0; i < objs.length; i++) {
								this._setFileList(objs[i],1);
								selArr.push(objs[i].docId);
							}
							// 当前页未选择数据
							var diffArr = _.difference(allArr, selArr);
							this._setFileList(diffArr,0);
							// 合并选中值
							this.fileIds = _.union(this.fileIds, selArr);
							// 去除取消项
							this.fileIds = _.difference(this.fileIds, diffArr);
						},
						_setFileList:function(objs,op){
							if (op==1) {
								var flag = true;
								for (var i=0;i<this.recentlyOpenedfileList.length;i++) {
									if (this.recentlyOpenedfileList[i].filePath == objs.docId) {
										flag = false;
									}
								}
								if (flag) {
									var object = {};
									object.filePath = objs.docId;
									object.fileName = objs.name;
									object.size = objs.size;
									object.fileType = objs.docType;
									object.uploadTime = objs.uploadTime;
									object.privilege = '3';
									this.recentlyOpenedfileList.push(object);
								}
							} else {
								for (var i=0;i<objs.length;i++) {
									for (var j=0;j<this.recentlyOpenedfileList.length;j++) {
										if (this.recentlyOpenedfileList[j].filePath == objs[i]) {
											this.recentlyOpenedfileList.splice(j,1);
											break;
										}
									}
								}
							}
						},
						_reviewFolder:function(e){
							var docId = $(e.target).attr('data-id');
							var name = $(e.target).text();
							var html = this.$el.find(".SID-crumbs").html();
							this.historyNames.push(html);
							this.$el.find(".SID-crumbs a").addClass('docbtn');
							this.$el.find(".SID-crumbs").append("<a class='SID-toFolder' data-folderid='"+docId+"' data-folderName='"+name+"'>&gt;&nbsp&nbsp"+name+"</a>");
							this._initDocTable(docId);
							this._setGoBackBtn();
						},
						_linkData:function(e){
							var id = $(e.currentTarget).attr("data-folderid");
							var name = $(e.currentTarget).attr("data-folderName");
							if ($(e.currentTarget).hasClass("docbtn")) {
								var html = this.$el.find(".SID-crumbs").html();
								this.historyNames.push(html);
								if (id=="") {
									this.$el.find(".SID-crumbs a:gt(0)").remove();
								} else {
									var aObj = this.$el.find(".SID-crumbs>a");
									var flag = false;
									for(var i=0,a; a=aObj[i++];){
				        				if ($(a).attr("data-folderid")==id) {
				        					flag = true;
				        					continue;
				        				}
				        				if (flag) {
				        					$(a).remove();
				        				}
				        			}
								}
								this.$el.find(".SID-crumbs a:last").removeClass('docbtn');
								this._initDocTable(id);
								this._setGoBackBtn();
							}
						},
						_upper:function(e){
							if (this.historyNames.length > 0) {
								var html = this.$el.find(".SID-crumbs").html();
								// 绘画面包屑
								this.$el.find(".SID-crumbs").html(this.historyNames[this.historyNames.length-1]);
								// 放进前进历史
								this.historyNamesGo.push(html);
								// 删除回退历史
								this.historyNames.splice(this.historyNames.length-1,1);
								var skipId = this.$el.find(".SID-crumbs a:last").attr("data-folderid");
								this._initDocTable(skipId);
								this._setGoBackBtn();
							}
						},
						_down:function(e){
							if (this.historyNamesGo.length > 0) {
								var html = this.$el.find(".SID-crumbs").html();
								// 绘画面包屑
								this.$el.find(".SID-crumbs").html(this.historyNamesGo[this.historyNamesGo.length-1]);
								// 放进回退历史
								this.historyNames.push(html);
								// 删除前进历史
								this.historyNamesGo.splice(this.historyNamesGo.length-1,1);
								var skipId = this.$el.find(".SID-crumbs a:last").attr("data-folderid");
								this._initDocTable(skipId);
								this._setGoBackBtn();
							}
						},
						_setGoBackBtn:function(){
							if (this.historyNames.length > 0) {
								this.$el.find(".SID-back").addClass("docbtn");
								this.$el.find(".SID-back").css("color","#1788fb");
							} else {
								this.$el.find(".SID-back").removeClass("docbtn");
								this.$el.find(".SID-back").css("color","#e8e8e8");
							}
							if (this.historyNamesGo.length > 0){
								this.$el.find(".SID-go").addClass("docbtn");
								this.$el.find(".SID-go").css("color","#1788fb");
							} else {
								this.$el.find(".SID-go").removeClass("docbtn");
								this.$el.find(".SID-go").css("color","#e8e8e8");
							}
						},
						_initDocTable : function(docId){
							this._saveFile();
							var self = this;
							var tableObj={};
							var name = $(".SID-param").val();
							var param = "&searchParam="+encodeURIComponent(name);
							if(docId)
								param= param + "&folderId="+docId;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.thirdpart.mobileark.getpersondocs"+param;
							tableObj.tbID = "grid-table1";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = false;
							tableObj.nopage = "hidepage";
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{
								"sTitle" : "<input type='checkbox' class='SID-cBox' data-id='all'/>",
								"mDataProp" : "",
								"sDefaultContent" : "<input type='checkbox' />",
								"bSortable" : false,
								"sWidth" : "15px",
								"sClass" : "center",
								"fnRender" : function(o, val) {
									var str = "";
									if(o.aData.type != 'folder')
										str = "<input type='checkbox' class='SID-cBox' data-id='"+o.aData.docId+"' />";
									var id = o.aData.docId;
									if($.inArray(id,self.fileIds)!=-1) {
										str = "<input type='checkbox' checked='checked' class='SID-cBox' data-id='"+o.aData.docId+"'/>";
									}
									return str;
								}
							},
							{"sTitle":"名称","sWidth":"30%","mDataProp":"name","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								var str = val;
								if(o.aData.type == 'folder'){
									str = '<a class ="SID-folder docbtn" data-id ='+o.aData.docId+'>'+val+'</a>';
								}
								return str;
							}},
							{"sTitle":"大小","sWidth":"10%","mDataProp":"size1","sDefaultContent": "" ,"sClass":"right","bSortable":false,"fnRender":function(o,val){
								if(o.aData.type == 'folder'){
									return o.aData.size;
								} else {
									return o.aData.size+" KB";
								}
							}},
							{"sTitle":"文档类型","sWidth":"10%","mDataProp":"docType","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								return self._getStr(o.aData.docType);
							}},
							{"sTitle":"上传时间","sWidth":"20%","mDataProp":"uploadTime","sDefaultContent": "" ,"sClass":"center","bSortable":false}
							];
							var param="";
							var jsonProc = function(data) {
								var docList = data.documentList;
								var folderList = data.folderList;
								var list = [];
								if(folderList != null){
									for(var i=0;i<folderList.length;i++){
										var object = {};
										object.docId = folderList[i].folderId;
										object.name = folderList[i].folderName;
										object.size = '-';
										object.docType = '普通文件夹';
										object.uploadTime = folderList[i].createTime;
										object.type = 'folder';
										list.push(object);
									}
								}
								if(docList != null){
									for(var i=0;i<docList.length;i++){
										var object = {};
										object.docId = docList[i].fileId;
										object.name = docList[i].fileName;
										object.size = docList[i].fileSize;
										object.docType = '.'+docList[i].fileType;
										object.uploadTime = docList[i].uploadTime;
										object.type = 'document';
										list.push(object);
									}
								}
								var jsonData = {
												"aaData" : list ? list : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_getStr:function(str) {  
				            return str.replace(/\./g, "");  
				        },
						_sumbitForm : function(commonDialog){
							this._saveFile();
							this.parentView._reloadAttachment(this.fileIds,this.recentlyOpenedfileList);
							this.destroy();
							commonDialog.cancel();
						}
					});
			return DocSnippetView;
		});
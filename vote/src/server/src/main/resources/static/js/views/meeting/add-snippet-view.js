define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/addTemplate.html'
		  , 'views/tree/depttree-snippet-view'
		  , 'views/doc/doc-snippet-view'
		  ,'text!../../templates/meeting/addAgendaTemplate.html'
		  ,'text!../../templates/meeting/addSignTemplate.html'
		  ,'text!../../templates/meeting/addRemarkTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination'],
		function($, CommunicationBaseView,Template,DeptTreeView,DocView
				,agendaTemp,signTemp,remarkTemp
				,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-tab-btn' : '_onClickTab',
							'click .fhicon-close3' : '_onClickDelLi',
							'click .SID-clear' : '_onClickClear',
							'keyup .SID-search-user' : '_searchUser',
							'click .SID-user-li':'_chooseUser',
							'click .SID-dept-tree' : '_onClickDeptTree',
							'click .SID-dept-tree1' : '_onClickDeptTree1',
							'click .SID-outerOk' : '_onClickOuterOk',
							'click .add-schedule' : '_initAgenda',
							'click .SID-module-del':'_delModuleDiv',
							'click .add-sign':'_initSign',
							'click .add-remark' : '_initRemark',
							'click .SID-word-btn' : '_openWord',
							'click .SID-attach-remove' : '_removeAttach',
							'click' : '_datepickerCheckExternalClick'
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							//计数器
							this.count=0;
							// 附件id
							this.fileIds=[];
							this.dialog;
							this.recentlyOpenedfileList = [];
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
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
						},
						_setContentHTML : function (){
							var self = this;
							var html = _.template(Template, {
								'imgPath': self.constants.IMAGEPATH,
							});
							this.$el.append(html);
							var commonDialog;
							if(this.data.operation == 1){
								commonDialog =fh.commonOpenDialog('roomEditDialog', this.data.title, 780, 500, this.el);
								this._fillData(this.data);
							}else{
								commonDialog =fh.commonOpenDialog('roomAddDialog', this.data.title, 780, 500, this.el);
								this._initAttachment("-1");
							}
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("ok","保存",function(){
								self._sumbitForm(commonDialog);
							});
							commonDialog.addBtn("next","下一步",function(){
								commonDialog.hideBtn("next");
								//self._next(commonDialog);
							});
							commonDialog.addBtn("prev","上一步",function(){
								commonDialog.showBtn("next");
								//self._prev(commonDialog);
							});
							this.dialog = commonDialog;
							this._dialogBtnInit("base-info");
							this.uiRanderUtil.randerJQueryUI_DateTimeRange(this,".SID-startdate",".SID-enddate");
						},
						_dialogBtnInit:function(tab){
							var self = this;
							this.dialog.removeBtn("next");
							this.dialog.removeBtn("prev");
							if (tab == "base-info") {
								this.dialog.addBtn("next","下一步",function(){
									$(".SID-tab-btn[data-name='meeting-people']").click();
								});
							} else if (tab == "meeting-people") {
								this.dialog.addBtn("next","下一步",function(){
									$(".SID-tab-btn[data-name='meeting-schedule']").click();
								});
								this.dialog.addBtn("prev","上一步",function(){
									$(".SID-tab-btn[data-name='base-info']").click();
								});
							} else if (tab == "meeting-schedule") {
								this.dialog.addBtn("next","下一步",function(){
									$(".SID-tab-btn[data-name='annex-information']").click();
								});
								this.dialog.addBtn("prev","上一步",function(){
									$(".SID-tab-btn[data-name='meeting-people']").click();
								});
							} else if (tab == "annex-information") {
								this.dialog.addBtn("next","下一步",function(){
									$(".SID-tab-btn[data-name='meeting-sign']").click();
								});
								this.dialog.addBtn("prev","上一步",function(){
									$(".SID-tab-btn[data-name='meeting-schedule']").click();
								});
							} else if (tab == "meeting-sign") {
								this.dialog.addBtn("next","下一步",function(){
									$(".SID-tab-btn[data-name='meeting-remark']").click();
								});
								this.dialog.addBtn("prev","上一步",function(){
									$(".SID-tab-btn[data-name='annex-information']").click();
								});
							} else if (tab == "meeting-remark") {
								this.dialog.addBtn("prev","上一步",function(){
									$(".SID-tab-btn[data-name='meeting-sign']").click();
								});
							}
						},
						_datepickerCheckExternalClick : function(Event){
		                    $.datepicker._checkExternalClick(Event);
		                },
		                _fillData : function(data){
		                	//填充基本信息
		                	this.$el.find(".SID-id").val(data.meeting.meetingId);
		                	this.$el.find(".SID-name").val(data.meeting.meetingName);
		                	this.$el.find(".SID-address").val(data.meeting.address);
		                	this.$el.find(".SID-startdate").val(this._getDateStr(data.meeting.beginTimeStr));
		                	this.$el.find(".SID-enddate").val(this._getDateStr(data.meeting.endTimeStr));
		                	if(data.meeting.hasGroup == 1)
		                		this.$el.find(".sel_yes").attr('checked','checked');
		                	//填充会议议程
		                	var agendasInfo = data.agendasInfo;
		                	for(var i = 0,length=agendasInfo.length;i<length;i++){
		                		this._loadAgenda(agendasInfo[i]);
		                	}
		                	this.$el.find(".SID-schedule > .meeting-schedule").hide();//初始化时隐藏基本信息下的议程信息
		                	//填充参会人员信息
		                	var mdList = data.mdList;
		                	var depNodes = [];
		                	var depServiceNodes = [];
		                	for(var i=0;i<mdList.length;i++){
		                		if(mdList[i].type == 'inner' && mdList[i].entityType == 'dept'){
		                			var node = {depUuid:mdList[i].entityId,depOrder:mdList[i].deptOrder,depName:mdList[i].entityName};
		                			depNodes.push(node);
		                		}else if(mdList[i].type == 'service' && mdList[i].entityType == 'dept'){
		                			var node = {depUuid:mdList[i].entityId,depOrder:mdList[i].deptOrder,depName:mdList[i].entityName};
		                			depServiceNodes.push(node);
		                		}else if(mdList[i].type == 'inner' && mdList[i].entityType == 'user'){
		                			var userBox = this.$el.find(".meeting-people").find(".SID-userbox");
		                			var text = "<span class='name'>"+mdList[i].entityName+"</span><span class='job'>"+mdList[i].deptName+"</span><span class='num'>"+mdList[i].phone+"</span>";
		                			userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+mdList[i].entityId+"'><i class='fhicon-close3 SID-del-li'></i>"+text+"</li>");
		                		}else if(mdList[i].type == 'outer' && mdList[i].entityType == 'user'){
		                			var userBox = this.$el.find(".SID-outerbox");
		                			var text = "<span class='name'>"+mdList[i].entityName+"</span><span class='num'>"+mdList[i].entityId+"</span>";
		                			userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+mdList[i].entityId+"'><i class='fhicon-close3 SID-del-li'></i>"+text+"</li>");
		                		}else if(mdList[i].type == 'service' && mdList[i].entityType == 'user'){
		                			var userBox = this.$el.find(".meeting-sign").find(".SID-userbox");
		                			var text = "<span class='name'>"+mdList[i].entityName+"</span><span class='job'>"+mdList[i].deptName+"</span><span class='num'>"+mdList[i].phone+"</span>";
		                			userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+mdList[i].entityId+"'><i class='fhicon-close3 SID-del-li'></i>"+text+"</li>");
		                		}
		                	}
		                	this._setDep(depNodes,$(".meeting-people"));
		                	this._setDep(depServiceNodes,$(".meeting-sign"));
		                	//填充会议签到信息
		                	var signInfo = data.signinSequList;
		                	for(var i = 0,length=signInfo.length;i<length;i++){
		                		this._initSign(signInfo[i]);
		                	}
		                	this.$el.find(".SID-signs > .form-sign").show();//初始化时隐藏基本信息下的签到信息
		                	//填充会议备注信息
		                	var remarkInfo = data.remarksList;
		                	for(var i=0,length=remarkInfo.length;i<length;i++){
		                		this._initRemark(remarkInfo[i]);
		                	}
		                	this.$el.find(".SID-remarks > .meeting-remark").hide();//初始化时隐藏基本信息下的备注信息
		                	// 附件初始化
		                	var attInfo = data.attachmentList;
		                	for(var i=0,length=attInfo.length;i<length;i++){
		                		this.fileIds.push(attInfo[i].filePath);
		                	}
		                	this._initAttachment(data.meeting.meetingId);
		                },
						_onClickTab:function(e){
							var btnObj = $(e.currentTarget);
							this.validateForm("formMsg");
						    if(!this.validateResult()){
						    	return;
						    }
							this.$el.find(".tablist li").removeClass("active");
							btnObj.addClass("active");
					        if(btnObj.data("name")=="base-info"){
					            $(".add-sign").prev().addClass("hide");
					            $(".add-schedule").hide();
					            $(".add-remark").hide();
					            $(".formBox").hide();
					            $(".base-info").show();
					        }
					        if(btnObj.data("name")=="meeting-schedule"){
					            $(".add-sign").prev().addClass("hide");
					            $(".formBox").hide();
					            $(".add-remark").hide();
					            $(".add-schedule").show();
					            $(".meeting-schedule").show();
					        }
					        if(btnObj.data("name")=="meeting-sign"){
					            $(".add-schedule").hide();
					            $(".add-remark").hide();
					            $(".formBox").hide();
					            $(".meeting-sign").show();
					            $(".SID-signs").show();
					        }
					        if(btnObj.data("name")=="meeting-people"){
					            $(".add-sign").prev().addClass("hide");
					            $(".add-schedule").hide();
					            $(".add-remark").hide();
					            $(".formBox").hide();
					            $(".meeting-people").show();
					        }
					        if(btnObj.data("name")=="annex-information"){
					            $(".add-sign").prev().addClass("hide");
					            $(".add-schedule").hide();
					            $(".add-remark").hide();
					            $(".formBox").hide();
					            $(".annex-information").show();
					        }
					        if(btnObj.data("name")=="meeting-remark"){
					            $(".add-sign").prev().addClass("hide");
					            $(".add-schedule").hide();
					            $(".formBox").hide();
					            $(".add-remark").show();
					            $(".meeting-remark").show();
					        }
					        this._dialogBtnInit(btnObj.data("name"));
						},
						_openWord:function(){
							this.childView.DocView= new DocView();
							this.childView.DocView.render(this,this.fileIds,this.recentlyOpenedfileList);
						},
						_loadAgenda:function(data){
							var self = this;
							if(data == null){
								data.address='';
								data.beginTimeStr='';
								data.endTimeStr='';
								data.remarks='';
							} else {
								data.beginTimeStr = self._getDateStr(data.beginTimeStr);
								data.endTimeStr = self._getDateStr(data.endTimeStr);
							}
							var html = _.template(agendaTemp, {
								'imgPath': self.constants.IMAGEPATH,
								'sequName' : self.count,
								'agenda':data
							});
							self.count++;
							this.$el.find(".SID-schedule").append(html);
							var s = this.$el.find(".SID-schedule > .SID-agenda-div:last").find(".SID-agenda-start");
							var e = this.$el.find(".SID-schedule > .SID-agenda-div:last").find(".SID-agenda-end");
							this.uiRanderUtil.randerDateTime(s);
							this.uiRanderUtil.randerDateTime(e);
							this.$el.find(".SID-schedule > .meeting-schedule").show();
						},
						_initAgenda:function(e){
							var self = this;
							var data = {}
							data.address='';
							data.beginTimeStr='';
							data.endTimeStr='';
							data.remarks='';
							var html = _.template(agendaTemp, {
								'imgPath': self.constants.IMAGEPATH,
								'sequName' : self.count,
								'agenda':data
							});
							self.count++;
							this.$el.find(".SID-schedule").append(html);
							var s = this.$el.find(".SID-schedule > .SID-agenda-div:last").find(".SID-agenda-start");
							var e = this.$el.find(".SID-schedule > .SID-agenda-div:last").find(".SID-agenda-end");
							this.uiRanderUtil.randerDateTime(s);
							this.uiRanderUtil.randerDateTime(e);
							this.$el.find(".SID-schedule > .meeting-schedule").show();
						},
						_delModuleDiv:function(e){
							var obj = $(e.currentTarget);
							var signObj = obj.parents(".SID-signs");
							obj.parents(".SID-module-div").remove();
							signObj.find(".SID-module-div:first").addClass("first");
							var divs = signObj.find(".SID-module-div");
							for(var i=0,div;div=divs[i++];){
								$(div).find(".sign-name").html("签到" + i);
							}
						},
						_initSign:function(data){
							var self = this;
							if(data == null)
								data.remarks='';
							var html = _.template(signTemp, {
								'imgPath': self.constants.IMAGEPATH,
								'sequName' : self.count,
								'signInfo':data
							});
							self.count++;
							this.$el.find(".SID-signs").append(html);
							var divs = this.$el.find(".SID-signs > .SID-module-div");
							for(var i=0,div;div=divs[i++];){
								$(div).find(".sign-name").html("签到" + i);
							}
							divs.removeClass("first");
							this.$el.find(".SID-signs > .SID-module-div:first").addClass("first");
							this.$el.find(".SID-signs > .form-sign").show();
						},
						_initRemark:function(data){
							var self = this;
							if(data == null)
								data.remarks='';
							var html = _.template(remarkTemp, {
								'imgPath': self.constants.IMAGEPATH,
								'sequName' : self.count,
								'remark':data
							});
							self.count++;
							this.$el.find(".SID-remarks").append(html);
							this.$el.find(".SID-remarks > .meeting-remark").show();
						},
						_initAttachment:function(meetingId){
							var self = this;
							var tableObj={};
							var param = "&meetingId="+meetingId;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.meeting.meeting.getattachmentfromWeb"+param;
							tableObj.tbID = "grid-table2";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = false;
							tableObj.nopage = "hidepage";
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"文档名称","sWidth":"30%","mDataProp":"fileName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"文档类型","sWidth":"10%","mDataProp":"contentType","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"权限","sWidth":"10%","mDataProp":"privilege","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								if (o.aData.privilege == "1") {
									return '<select class="SID-privilege"><option value="1" selected>查看</option><option value="3">下载</option></select>';
								} else {
									return '<select class="SID-privilege"><option value="1">查看</option><option value="3" selected>下载</option></select>';
								}
							}},
							{"sTitle":"上传时间","sWidth":"20%","mDataProp":"uploadTimeStr","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作","sWidth":"20%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								return '<div class="tb-opt-box">'
                                +'<a class="table-operation"><span class="fhicon-set2"></span></a>'
                                +'<div class="tb-opt-main" style="display: none">'
                                +'<a class="SID-attach-remove" data-id="'+o.aData.filePath+'"><span class="fhicon-delete"></span>删除</a>'
                                +'</div>'
                                +'</div>';
							}}
							];
							var param="";
							var jsonProc = function(data) {
								for (var i=0;i<data.attachmentList.length;i++) {
									var obj = data.attachmentList[i];
									var object = {};
									object.filePath = obj.filePath;
									object.fileName = obj.fileName;
									object.size = obj.size;
									object.fileType = obj.contentType;
									object.uploadTime = obj.uploadTimeStr;
									object.privilege = obj.privilege;
									self.recentlyOpenedfileList.push(object);
								}
								console.log(self.recentlyOpenedfileList);
								var jsonData = {
										"iTotalDisplayRecords" : data.attachmentList ? data.attachmentList.length : 0,
												"iTotalRecords" : data.attachmentList ? data.attachmentList.length : 0,
														"aaData" : data.attachmentList ? data.attachmentList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						},
						_reloadAttachment:function(fileIds,recentlyOpenedfileList){
							this.fileIds = fileIds;
							this.recentlyOpenedfileList = recentlyOpenedfileList;
							var ids = "";
							for(var i=0,fileId; fileId=fileIds[i++];) {
								ids += fileId + ",";
							}
							var self = this;
							var tableObj={};
							var param = "&searchExistFlag=1&fileIds="+ids;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.meeting.meeting.virtual.request"+param;
							tableObj.tbID = "grid-table2";
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = false;
							tableObj.nopage = "hidepage";
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"文档名称","sWidth":"30%","mDataProp":"fileName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"文档类型","sWidth":"10%","mDataProp":"fileType","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"权限","sWidth":"10%","mDataProp":"privilege","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								if (o.aData.privilege == "1") {
									return '<select class="SID-privilege"><option value="1" selected>查看</option><option value="3">下载</option></select>';
								} else {
									return '<select class="SID-privilege"><option value="1">查看</option><option value="3" selected>下载</option></select>';
								}
							}},
							{"sTitle":"上传时间","sWidth":"20%","mDataProp":"uploadTime","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作","sWidth":"20%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								return '<div class="tb-opt-box">'
                                +'<a class="table-operation"><span class="fhicon-set2"></span></a>'
                                +'<div class="tb-opt-main" style="display: none">'
                                +'<a class="SID-attach-remove" data-id="'+o.aData.filePath+'"><span class="fhicon-delete"></span>删除</a>'
                                +'</div>'
                                +'</div>';
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : 0,
												"iTotalRecords" : 0,
														"aaData" : self.recentlyOpenedfileList ? self.recentlyOpenedfileList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
						},
						_removeAttach:function(e){
							var obj = $(e.currentTarget);
							var fileId = obj.attr("data-id");
							this.fileIds = _.difference(this.fileIds, [fileId]);
							for (var j=0;j<this.recentlyOpenedfileList.length;j++) {
								if (this.recentlyOpenedfileList[j].filePath == fileId) {
									this.recentlyOpenedfileList.splice(j,1);
									break;
								}
							}
							this._reloadAttachment(this.fileIds,this.recentlyOpenedfileList);
						},
						_searchUser:function(e){
							var obj = $(e.currentTarget);
							var _self = this;
							var username = obj.val();
							var pObj = obj.parents(".form-item");
							if (username == ""){
								pObj.find(".pop-rankbox-maskColor").css("display","none");
								return;
							}
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var param = "&userName="+encodeURIComponent(username);
							var url = servicePath + "?" +ropParam + "&method=mapps.thirdpart.mobileark.getusers"+param;
				            $.ajax({
								type : "POST",
								url : url,
								success : function(data) {
									if (data.code == "1") {
										_self._setUser(data.userList,pObj);
									}
								},
								error : function(){
								}
							});
						},
						_setUser:function(users,pObj){
							if (users.length == 0){
								return;
							}
							var userList = pObj.find(".SID-userlist");
							var userSearch = pObj.find(".pop-searchbar");
							userList.empty();
							var liObjs = pObj.find(".SID-userbox>li");
							var userIdArr = new Array();
							for(var i=0,liObj;liObj=liObjs[i++];){
								userIdArr.push($(liObj).attr("data-depUuid"));
							}
							var pushCount=0;
							for(var i=0,user;user=users[i++];){
								var flag = true;
								for(var j=0,userId;userId=userIdArr[j++];){
									if (user.userUuid == userId)
										flag = false;
								}
								if (flag) {
									userList.append("<li class='SID-priv-li rank-item SID-user-li' data-type='user' data-depUuid='"+user.userUuid+"'><span class='name'>"+user.userName+"</span><span class='job'>"+user.deptName+"</span><span class='num'>"+user.phoneNum+"</span></li>");
									pushCount ++;
								}
							}
							if (pushCount == 0) {
								return ;
							}
							pObj.find(".pop-rankbox-maskColor").css("display","block");
						},
						_chooseUser:function(e){
							var obj = $(e.currentTarget);
							var pObj = obj.parents(".form-item");
							var type = obj.attr("data-type");
			            	var id = obj.attr("data-depUuid");
			            	var text = obj.html();
			            	var userBox = pObj.find(".SID-userbox");
			            	userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+id+"'><i class='fhicon-close3 SID-del-li'></i>"+text+"</li>");
			            	pObj.find(".SID-search-user").val("");
			            	pObj.find(".pop-rankbox-maskColor").css("display","none");
						},
						_onClickOuterOk:function(){
							var name = this.$el.find(".SID-outerName").val();
							var phone = this.$el.find(".SID-outerPhone").val();
							if (name == "" || phone == "") {
								fh.alert("请填写外部人员信息！");
							} else {
								var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
								if (mobile.test(phone)) {
									var userBox = this.$el.find(".SID-outerbox");
									var liList = userBox.find("li");
									for(var i=0,li;li=liList[i++];){
										var num = $(li).find(".num").text();
										if (num == phone) {
											fh.alert("电话号码重复！");
											return;
										}
									}
				            		userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+phone+"'><i class='fhicon-close3 SID-del-li'></i><span class='name'>"+name+"</span><span class='num'>"+phone+"</span></li>");
				            		this.$el.find(".SID-outerName").val("");
				            		this.$el.find(".SID-outerPhone").val("");
								} else {
									fh.alert("电话格式错误！");
								}
							}
						},
						_onClickDeptTree : function(e){
							var depLis = this.$el.find(".meeting-people").find(".SID-deplist > li");
							var depUuids = [];
							for(var i=0,li;li=depLis[i++];){
								depUuids.push($(li).attr("data-depUuid"));
							}
							this.childView.DeptTreeView= new DeptTreeView();
							this.childView.DeptTreeView.render(this,depUuids,$(".meeting-people"));
						},
						_onClickDeptTree1 : function(e){
							var depLis = this.$el.find(".meeting-sign").find(".SID-deplist > li");
							var depUuids = [];
							for(var i=0,li;li=depLis[i++];){
								depUuids.push($(li).attr("data-depUuid"));
							}
							this.childView.DeptTreeView= new DeptTreeView();
							this.childView.DeptTreeView.render(this,depUuids,$(".meeting-sign"));
						},
						_setDep:function(nodes,obj){
							var deplist = obj.find(".SID-deplist");
							deplist.empty();
							for(var i=0,node;node=nodes[i++];){
								deplist.append("<li class='SID-priv-li rank-item' data-type='dept' data-depOrder='"+node.depOrder+"' data-depUuid='"+node.depUuid+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+node.depName+"</span></li>");
				            }
						},
						_onClickDelLi:function(e){
							var obj = $(e.currentTarget);
							obj.parent().remove();
						},
						_onClickClear:function(e){
							var btnObj = $(e.currentTarget);
							btnObj.parent().find(".rank-list").empty();
						},
						format:function(data,fmt) {   
						  var o = {   
						    "M+" : data.getMonth()+1,                 //月份   
						    "d+" : data.getDate(),                    //日   
						    "h+" : data.getHours(),                   //小时   
						    "m+" : data.getMinutes(),                 //分   
						    "s+" : data.getSeconds(),                 //秒   
						    "q+" : Math.floor((data.getMonth()+3)/3), //季度   
						    "S"  : data.getMilliseconds()             //毫秒   
						  };   
						  if(/(y+)/.test(fmt))   
						    fmt=fmt.replace(RegExp.$1, (data.getFullYear()+"").substr(4 - RegExp.$1.length));   
						  for(var k in o)   
						    if(new RegExp("("+ k +")").test(fmt))   
						  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
						  return fmt;
						},
						_validateTime:function(stime,etime,tab){
							var title = "会议";
							if (tab == "meeting-schedule") {
								title = "议程";
								var meetingstime = this.$el.find(".SID-startdate");
								var meetingetime = this.$el.find(".SID-enddate");
								if (!(new Date(stime.val()) >= new Date(meetingstime.val()) && new Date(stime.val()) <= new Date(meetingetime.val()))) {
									this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
				                	fh.alert(title+"开始时间超出会议时间",false,function(){
				                		stime.parents(".SID-module-div").find(".SID-agenda-remarks").focus();
				                		stime.focus();
				                	});
				                	return false;
				                }
								if (!(new Date(etime.val()) >= new Date(meetingstime.val()) && new Date(etime.val()) <= new Date(meetingetime.val()))) {
									this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
				                	fh.alert(title+"结束时间超出会议时间",false,function(){
				                		etime.parents(".SID-module-div").find(".SID-agenda-remarks").focus();
				                		etime.focus();
				                	});
				                	return false;
				                }
							}
							if (new Date(stime.val()) < new Date()) {
								this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
			                	fh.alert(title+"开始时间不能早于当前时间",false,function(){
			                		stime.focus();
			                	});
			                	return false;
			                } else if (new Date(etime.val()) < new Date()) {
								this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
			                	fh.alert(title+"结束时间不能早于当前时间",false,function(){
			                		etime.focus();
			                	});
			                	return false;
			                } else if (new Date(stime.val()) >= new Date(etime.val())) {
								this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
			                	fh.alert(title+"开始时间不能晚于结束时间",false,function(){
			                		stime.focus();
			                	});
			                	return false;
			                }
							return true;
						},
				        _getDateStr:function(startDiffTime) {  
				            //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式   
				            return startDiffTime.replace(/\./g, "-");  
				        },
				        _getDate:function(startDiffTime) {  
				            //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式   
				            return new Date(startDiffTime.replace(/\./g, "/"));  
				        },
						_sumbitForm : function(commonDialog){
							var param = "";
							var _self = this;
						    this.validateForm("formMsg");
						    if(this.validateResult()){
						    	var data = {
				                		agendaFlag : 0,
				                		attachmentFlag : 0,
				                		createGroupFlag : 0,
				                		signinFlag : 0,
				                		remarksFlag : 0,
				                		meeting : null,
				                		inParticipantsList : [],
				                		outParticipantsList : [],
				                		serviceParticipantsList : [],
				                		agendaList : [],
				                		attachmentList : [],
				                		signinSequList : [],
				                		remarksList : []
				                };
						    	// 会议主信息
				                var meetingInfo = new Object();
				                meetingInfo.id = this.$el.find(".SID-id").val();
				                meetingInfo.name = $.trim(this.$el.find(".SID-name").val());
				                meetingInfo.address = $.trim(this.$el.find(".SID-address").val());
				                meetingInfo.beginTime = this.$el.find(".SID-startdate").val()+":00";
				                meetingInfo.endTime = this.$el.find(".SID-enddate").val()+":00";
				                if (!_self._validateTime(this.$el.find(".SID-startdate"),this.$el.find(".SID-enddate"),"base-info")) {
				                	return;
				                }
				                data.meeting = meetingInfo;
				                // 创建群组
				                data.createGroupFlag = $('input[name="sel"]:checked').val();
				                // 内部人员
				                var innerDepli = this.$el.find(".meeting-people").find(".SID-deplist > li");
				                for(var i=0,li; li=innerDepli[i++];) {
		    		                var inInfo = new Object();
		    		                inInfo.entityId = $(li).attr("data-depuuid");
		    		                inInfo.entityType = "dept";
		    		                inInfo.entityName = $(li).find("span").html();
		    		                inInfo.deptOrder = $(li).attr("data-deporder");
		    		                data.inParticipantsList.push(inInfo);
			                	}
				                var innerUserli = this.$el.find(".meeting-people").find(".SID-userbox > li");
				                for(var i=0,li; li=innerUserli[i++];) {
		    		                var inInfo = new Object();
		    		                inInfo.entityId = $(li).attr("data-depuuid");
		    		                inInfo.entityType = "user";
		    		                inInfo.entityName = $(li).find("span[class='name']").html();
		    		                data.inParticipantsList.push(inInfo);
			                	}
				                // 外部人员
				                var outerLi = this.$el.find(".meeting-people").find(".SID-outerbox > li");
				                for(var i=0,li;li=outerLi[i++];){
					                var inInfo2 = new Object();
					                inInfo2.type = "outer";
					                inInfo2.entityType = "user";
					                inInfo2.entityId = $(li).attr("data-depuuid");
					                inInfo2.entityName = $(li).find("span[class='name']").html();
					                data.outParticipantsList.push(inInfo2);
				                }
				                if (data.inParticipantsList.length == 0 && data.outParticipantsList.length == 0){
				                	this.$el.find(".SID-tab-btn[data-name='meeting-people']").click();
				                	fh.alert("请设置参会人员",false,function(){ });
				                	return;
				                }
				                // 会议议程
				                var agendaDivs = this.$el.find(".SID-agenda-div");
				                for(var i=0,div;div=agendaDivs[i++];){
				                	var agendaInfo = new Object();
				                	
				                	if (!_self._validateTime($(div).find(".SID-agenda-start"),$(div).find(".SID-agenda-end"),"meeting-schedule")) {
					                	return;
					                }
				                	
				                	agendaInfo.beginTime = $(div).find(".SID-agenda-start").val()+":00";
				                	agendaInfo.endTime = $(div).find(".SID-agenda-end").val()+":00";
				                	agendaInfo.address = $.trim($(div).find(".SID-agenda-address").val());
				                	agendaInfo.remarks = $.trim($(div).find(".SID-agenda-remarks").val());
				                	data.agendaList.push(agendaInfo);
				                }
				                if (data.agendaList.length > 0){
				                	data.agendaFlag=1;
				                }
				                // 附件资料.
			                	for(var i =0,div;div=this.datatable.fnGetData()[i++];){
			                		var valObj = $(div).find(".SID-attach-attr");
			                		var attachInfo = new Object();
			                		attachInfo.filePath = div.filePath;
			                		attachInfo.fileName = div.fileName;
			                		attachInfo.contentType = div.fileType;
			                		attachInfo.size = div.size;
			                		attachInfo.uploadTime = div.uploadTime;
			                		attachInfo.privilege = $($(".SID-privilege")[i-1]).find("option:selected").val();
			                		data.attachmentList.push(attachInfo);
			                	}
				                if (this.datatable.fnGetData().length > 0){
				                	data.attachmentFlag=1;
				                }
				                // 会议签到
			                	var innerDepli = this.$el.find(".meeting-sign").find(".SID-deplist > li");
				                for(var i=0,li; li=innerDepli[i++];) {
		    		                var inInfo = new Object();
		    		                inInfo.entityId = $(li).attr("data-depuuid");
		    		                inInfo.entityType = "dept";
		    		                inInfo.entityName = $(li).find("span").html();
		    		                inInfo.deptOrder = $(li).attr("data-deporder");
		    		                data.serviceParticipantsList.push(inInfo);
			                	}
				                var innerUserli = this.$el.find(".meeting-sign").find(".SID-userbox > li");
				                for(var i=0,li; li=innerUserli[i++];) {
		    		                var inInfo = new Object();
		    		                inInfo.entityId = $(li).attr("data-depuuid");
		    		                inInfo.entityType = "user";
		    		                inInfo.entityName = $(li).find("span[class='name']").html();
		    		                data.serviceParticipantsList.push(inInfo);
			                	}
				                var signins = this.$el.find(".meeting-sign").find(".SID-signin-remarks");
				                for(var i=0,div;div=signins[i++];){
				                	if ($(div).val()==""){
				                		continue;
				                	}
				                	var signinSequInfo = new Object();
				                	signinSequInfo.remarks = $.trim($(div).val());
				                	signinSequInfo.sequ = i + 1;
				                	data.signinSequList.push(signinSequInfo);
				                }
				                if (data.serviceParticipantsList.length > 0 || data.signinSequList.length > 0){
				                	data.signinFlag=1;
				                }
				                // 会议备注
				                var remarksDivs = this.$el.find(".SID-remarks-div").find(".SID-remarks-remarks");
				                for(var i=0,div;div=remarksDivs[i++];){
				                	var remarksInfo = new Object();
				                	remarksInfo.remarks = $.trim($(div).val());
				                	data.remarksList.push(remarksInfo);
				                }
				                if (data.remarksList.length > 0){
				                	data.remarksFlag=1;
				                }
				                console.log(JSON.stringify(data));
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								var param = JSON.stringify(data);
								$.ajax({
									type : "POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.meeting.meeting.service.add&meetingJson="+param,
									success : function(response) {
										if(response.code == "1"){
							    			fh.alert(_self.data.title+"成功",false,function(){
							    				commonDialog.cancel();
							    				_self.parentView.holdList();
							    				_self.destroy();
											});
							    		}else{
							    			fh.alert("数据处理失败");
							    		}
									},
									error : function(){
										fh.alert("数据处理失败");
									}
								});
						    }
						}
					});
			return AddSnippetView;
		});
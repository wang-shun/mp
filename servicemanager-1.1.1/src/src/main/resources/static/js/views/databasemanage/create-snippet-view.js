define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/databasemanage/createTemplate.html'
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
							'click' : '_datepickerCheckExternalClick',
							'change .SID-dbType' : '_onChangedbType'
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
							var html = _.template(Template, {});
							this.$el.append(html);
							var commonDialog;
							commonDialog =fh.commonOpenDialog('dbCreateDialog', this.data.title, 390, 280, this.el,null,null,null,self.parentView.dialog);
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("create","创建",function(){
								self.$el.find(".SID-wrongtext").html("");
								if(self._checkForm()){
									self._createDb(commonDialog);
								}
							});
							this.dialog = commonDialog;
							//this._dialogBtnInit("base-info");
							//this.uiRanderUtil.randerJQueryUI_DateTimeRange(this,".SID-startdate",".SID-enddate");
						},
						_datepickerCheckExternalClick : function(Event){
		                    $.datepicker._checkExternalClick(Event);
		                },
		                _checkForm : function(){
		                	//检测是否为空
		                	if(this.$el.find(".SID-admin").val() == "" || this.$el.find(".SID-pass").val() == ""){
		                		this.$el.find(".SID-wrongtext").html("管理员用户或密码不能为空");
		                		return false;
		                	}else{
		                		return true;
		                	}
		                },
		                _fillData : function(data){
		                	//填充基本信息
		                	this.$el.find(".SID-dbType").val(data.databaseDetail.dbType);
		                	this.$el.find(".SID-host").val(data.databaseDetail.host);
		                	this.$el.find(".SID-port").val(data.databaseDetail.port);
		                	if(data.databaseDetail.dbType == "postgresql"){
		                		document.getElementById("sidordbnamelabel").innerHTML = "<span>*</span>数据库：";
		                		this.$el.find(".SID-sidordbname").val(data.databaseDetail.dbName);
		                	}else if(data.databaseDetail.dbType == "oracle"){
		                		document.getElementById("sidordbnamelabel").innerHTML = "<span>*</span>SID：";
		                		this.$el.find(".SID-sidordbname").val(data.databaseDetail.sid);
		                	}
		                	this.$el.find(".SID-username").val(data.databaseDetail.userName);
		                	this.$el.find(".SID-password").val(data.databaseDetail.password);
		                	this.$el.find(".SID-enabled").val(data.databaseDetail.enabled);
		                	this.$el.find(".SID-id").val(data.databaseDetail.id);
		                },
		                _onChangedbType:function(e){
							var obj = $(e.currentTarget);
			            	var dbtype = obj.val();
			            	if(dbtype == "postgresql")
			            		document.getElementById("sidordbnamelabel").innerHTML = "<span>*</span>数据库：";
			            	if(dbtype == "oracle")
			            		document.getElementById("sidordbnamelabel").innerHTML = "<span>*</span>SID：";
						},
						_onClickTab:function(e){
							var self = this;
							var btnObj = $(e.currentTarget);
							this.validateForm("formMsg");
						    if(!this.validateResult()){
						    	return;
						    }
						    var srcKey = this.$el.find(".tablist .active").data("name");
						    if((srcKey=="base-info" && btnObj.data("name")!="meeting-people" && btnObj.data("name")!="base-info") 
						    		|| (srcKey=="meeting-people" && btnObj.data("name")!="meeting-people" && btnObj.data("name")!="base-info")){
							    // 内部人员
				                var innerDepli = self.$el.find(".meeting-people").find(".SID-deplist > li");
				                var innerUserli = self.$el.find(".meeting-people").find(".SID-userbox > li");
				                // 外部人员
				                var outerLi = self.$el.find(".meeting-people").find(".SID-outerbox > li");
				                if (innerDepli.length == 0 && innerUserli.length == 0 && outerLi.length == 0){
				                	this.$el.find(".SID-tab-btn[data-name='meeting-people']").click();
				                	self._exalert("请设置参会人员",false,function(){ });
				                	return;
				                }
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
							{"sTitle":"文档名称","sWidth":"40%","mDataProp":"fileName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"文档类型","sWidth":"15%","mDataProp":"fileType","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender" : function(o, val) {
								return o.aData.contentType;
							}},
							{"sTitle":"上传时间","sWidth":"25%","mDataProp":"uploadTimeStr","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作","sWidth":"20%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								return '<div class="tb-opt-box">'
                                +'<a class="table-operation"><span class="fhicon-set2"></span></a>'
                                +'<div class="tb-opt-main" style="display: none">'
                                +'<a class="SID-attach-remove" data-id="'+o.aData.filePath+'"><span class="fhicon-delete"></span>删除</a>'
                                +'</div>'
                                +'</div>';
							}}
							];
							/*
							 * 
							{"sTitle":"权限","sWidth":"10%","mDataProp":"privilege","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								if (o.aData.privilege == "1") {
									return '<select class="SID-privilege"><option value="1" selected>查看</option><option value="3">下载</option></select>';
								} else {
									return '<select class="SID-privilege"><option value="1">查看</option><option value="3" selected>下载</option></select>';
								}
							}},
							 * */
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
							{"sTitle":"文档名称","sWidth":"40%","mDataProp":"fileName","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"文档类型","sWidth":"15%","mDataProp":"fileType","sDefaultContent": "" ,"sClass":"left","bSortable":false},
							{"sTitle":"上传时间","sWidth":"25%","mDataProp":"uploadTime","sDefaultContent": "" ,"sClass":"center","bSortable":false},
							{"sTitle":"操作","sWidth":"20%","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
								return '<div class="tb-opt-box">'
                                +'<a class="table-operation"><span class="fhicon-set2"></span></a>'
                                +'<div class="tb-opt-main" style="display: none">'
                                +'<a class="SID-attach-remove" data-id="'+o.aData.filePath+'"><span class="fhicon-delete"></span>删除</a>'
                                +'</div>'
                                +'</div>';
							}}
							];
							/*{"sTitle":"权限","sWidth":"10%","mDataProp":"privilege","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender" : function(o, val) {
							if (o.aData.privilege == "1") {
								return '<select class="SID-privilege"><option value="1" selected>查看</option><option value="3">下载</option></select>';
							} else {
								return '<select class="SID-privilege"><option value="1">查看</option><option value="3" selected>下载</option></select>';
							}
						}},*/
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
							var self = this;
							var obj = $(e.currentTarget);
							fh.confirm('删除此附件？',function(){
								var fileId = obj.attr("data-id");
								self.fileIds = _.difference(self.fileIds, [fileId]);
								for (var j=0;j<self.recentlyOpenedfileList.length;j++) {
									if (self.recentlyOpenedfileList[j].filePath == fileId) {
										self.recentlyOpenedfileList.splice(j,1);
										break;
									}
								}
								self._reloadAttachment(self.fileIds,self.recentlyOpenedfileList);
							});
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
							var _self = this;
							var name = this.$el.find(".SID-outerName").val();
							var phone = this.$el.find(".SID-outerPhone").val();
							if (name == "" || phone == "") {
								_self._exalert("请填写外部人员信息！");
							} else {
								var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
								if (mobile.test(phone)) {
									var userBox = this.$el.find(".SID-outerbox");
									var liList = userBox.find("li");
									for(var i=0,li;li=liList[i++];){
										var num = $(li).find(".num").text();
										if (num == phone) {
											_self._exalert("电话号码重复！");
											return;
										}
									}
				            		userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+phone+"'><i class='fhicon-close3 SID-del-li'></i><span class='name'>"+name+"</span><span class='num'>"+phone+"</span></li>");
				            		this.$el.find(".SID-outerName").val("");
				            		this.$el.find(".SID-outerPhone").val("");
								} else {
									_self._exalert("电话格式错误！");
								}
							}
						},
						_onClickDeptTree : function(e){
							var depLis = this.$el.find(".meeting-people").find(".SID-deplist > li");
							var depUuids = [];
							for(var i=0,li;li=depLis[i++];){
								depUuids.push($(li).attr("data-depUuid"));
							}
							this.showLoading();
							this.childView.DeptTreeView= new DeptTreeView();
							this.childView.DeptTreeView.render(this,depUuids,$(".meeting-people"));
						},
						_onClickDeptTree1 : function(e){
							var depLis = this.$el.find(".meeting-sign").find(".SID-deplist > li");
							var depUuids = [];
							for(var i=0,li;li=depLis[i++];){
								depUuids.push($(li).attr("data-depUuid"));
							}
							this.showLoading();
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
							var _self = this;
							var title = "会议";
							if (tab == "meeting-schedule") {
								title = "议程";
								var meetingstime = this.$el.find(".SID-startdate");
								var meetingetime = this.$el.find(".SID-enddate");
								if (!(this._getDate(stime.val()) >= this._getDate(meetingstime.val()) && this._getDate(stime.val()) <= this._getDate(meetingetime.val()))) {
									this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
				                	_self._exalert(title+"开始时间超出会议时间",false,function(){
				                		stime.parents(".SID-module-div").find(".SID-agenda-remarks").focus();
				                		stime.focus();
				                	});
				                	return false;
				                }
								if (!(this._getDate(etime.val()) >= this._getDate(meetingstime.val()) && this._getDate(etime.val()) <= this._getDate(meetingetime.val()))) {
									this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
				                	_self._exalert(title+"结束时间超出会议时间",false,function(){
				                		etime.parents(".SID-module-div").find(".SID-agenda-remarks").focus();
				                		etime.focus();
				                	});
				                	return false;
				                }
							}
							if (this._getDate(stime.val()) < new Date()) {
								this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
			                	_self._exalert(title+"开始时间不能早于当前时间",false,function(){
			                		stime.focus();
			                	});
			                	return false;
			                } else if (this._getDate(etime.val()) < new Date()) {
								this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
			                	_self._exalert(title+"结束时间不能早于当前时间",false,function(){
			                		etime.focus();
			                	});
			                	return false;
			                } else if (this._getDate(stime.val()) >= this._getDate(etime.val())) {
								this.$el.find(".SID-tab-btn[data-name='"+tab+"']").click();
			                	_self._exalert(title+"开始时间不能晚于结束时间",false,function(){
			                		stime.focus();
			                	});
			                	return false;
			                }
							return true;
						},
						_exalert:function(info,ischild,handler){
                        	var _self = this;
							fh.alert(info,true,handler,_self.dialog,null);
                        },
				        _getDateStr:function(startDiffTime) {  
				            //将xxxx/xx/xx的时间格式，转换为 xxxx-xx-xx的格式作为String对象输出   
				            return startDiffTime.replace(/\./g, "-");  
				        },
				        _getDate:function(startDiffTime) {  
				            //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式 并作为Date对象输出  
				            return new Date(startDiffTime.replace(/-/g, "/"));  
				        },
				        _saveForm:function(commonDialog){
				        	var method = "mapps.servicemanager.database.save";
				        	this._sumbitForm(commonDialog,method,"保存数据库");
				        },
				        _addForm:function(commonDialog){
				        	var method = "mapps.servicemanager.database.add";
				        	this._sumbitForm(commonDialog,method,"新增数据库");
				        },
				        _createDb:function(commonDialog){
				        	var method = "mapps.servicemanager.database.create";
				        	this._sumbitForm(commonDialog,method,"创建数据库");
				        },
				        _testDb:function(commonDialog){
				        	var method = "mapps.servicemanager.database.test";
				        	this._sumbitForm(commonDialog,method,"测试");
				        },
				        _publishForm:function(commonDialog){
				        	var method = "mapps.meeting.meeting.service.publish";
				        	this._sumbitForm(commonDialog,method,"发布会议");
				        },
						_sumbitForm : function(commonDialog,method,title){
							var param = "";
							var _self = this;
						    this.validateForm("formMsg");
						    if(this.validateResult()){
						    	var data = {};
						    	// 数据库基本信息
				                var databaseInfo = new Object();
				                databaseInfo.dbType = this.data.dbType;
				                databaseInfo.host = this.data.host;
				                databaseInfo.port = this.data.port;
				                var sidordbname = this.data.sidordbname;this.$el.find(".SID-password").val();
				                var adminuser = this.$el.find(".SID-admin").val();
				                var adminpass = this.$el.find(".SID-pass").val();
				                databaseInfo.userName = this.data.userName;
				                databaseInfo.password = this.data.password;
				                var tempenabled = (this.$el.find(".SID-enabled").val() == "")?"1":this.$el.find(".SID-enabled").val();
				                databaseInfo.enabled = tempenabled;
				                data.database = databaseInfo;
				                
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								var param = JSON.stringify(data);
								this.showLoading();
								$.ajax({
									type : "POST",
									url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&databaseJson="+param+"&sidordbname="+sidordbname+"&adminuser="+adminuser+"&adminpass="+adminpass),
									success : function(response) {
										_self.hideLoading();
										if(response.code == "1"){
							    			_self._exalert(title+"成功",false,function(){
							    				commonDialog.cancel();
							    				//_self.parentView.holdList();
							    				_self.destroy();
											});
							    		}else{
							    			_self._exalert(response.message);
							    		}
									},
									error : function(){
										_self.hideLoading();
										_self._exalert("数据处理失败");
									}
								});
						    }
						}
					});
			return AddSnippetView;
		});
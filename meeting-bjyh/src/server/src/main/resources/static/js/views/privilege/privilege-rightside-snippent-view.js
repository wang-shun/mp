define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/privilege/rightTemplate.html','views/privilege/privilege-orgadd-snippet-view'],
		function($, CommunicationBaseView,TemplateRight,OrgAddSnippetView) {
			var treeObj;
			var datatable;
			var RightsideSnippentView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-tab' : '_selectTab',
							"click .SID-add" : "_orgAdd",
							'click .SID-del-li' : '_onClickDelLi',
							'click .SID-depclear' : '_onClickDepClear',
							'click .SID-priv-cancel' : '_onClickCancel',
							'click .SID-priv-save' : '_onClickSave',
							'click .SID-adminpriv-cancel' : '_onClickAdminCancel',
							'click .SID-adminpriv-save' : '_onClickAdminSave',
							'keyup .SID-search-user1' : '_searchUser',
							'keyup .SID-search-user2' : '_searchUser',
							'keyup .SID-search-user3' : '_searchUser',
							'click .SID-user-li':'_chooseUser'
						},
						initialize : function() {
							this.parentObj;
							this.roomId;
							this.openDlg = {};
							this.adminMaxNum = 1;
							this.serviceMaxNum = 1;
						},
						render : function(parentObj,roomId,tabId) {
							this.roomId = roomId;
							this.parentObj = parentObj;
							this.setContentHTML(tabId);
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						setContentHTML : function(tabId){
							var template = _.template(TemplateRight);
							var html = template({});
							this.$el.append(html);
							if (tabId == 1) {
								this.$el.find(".SID-tab[data-tab-id='1']").click();
							} else {
								this._initPage(0);
							}
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							_this.parent('li').siblings('li').removeClass('active');
							_this.parent("li").addClass('active');
							var tabId = _this.attr("data-tab-id");
							this._initPage(tabId);
							this.$el.find('.SID-tab-container').hide();
							var currentTabContainer = this.$el.find('.SID-tab-container').filter(function( index ) {
								return $( this ).attr("tab-content-id") === tabId;
							});
							currentTabContainer.show();
							return this;
						},
						_onClickSave:function(){
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.privilege.add&roomId="+this.roomId;
				            var lis = this.$el.find(".SID-visible").find(".rank-list > .rank-item");
				            var arrPriv = [];
				            for(var i=0,li;li=lis[i++];){
				            	var type = $(li).attr("data-type");
				            	var id = $(li).attr("data-depUuid");
				            	if (id == "undefined" || id == "")  
				            	{  
				            		fh.alert("服务异常，请刷新后重试");
				            	    return;
				            	}  
				            	var deptOrder = $(li).attr("data-depOrder");
				            	var name = $(li).find("span").text();
				            	var privObj = new Object();
				            	privObj.type = type;
				            	privObj.entityId = id;
				            	privObj.entityName = name;
				            	if (type == "dept")
				            		privObj.deptOrder = deptOrder;
				            	arrPriv.push(privObj);
				            }
				            var param = "{\"list\":"+JSON.stringify(arrPriv)+"}";
				            $.ajax({
								type : "GET",
								url : url,
								data : {"jsonData":param},
								dataType:"json",
								success : function(data) {
									if (data.code == "1") {
										fh.alert("保存成功");
									} else {
										fh.alert(data.code + ":" + data.message);
									}
								},
								error : function(){
									fh.alert("数据保存失败");
								}
							});
						},
						_onClickAdminSave:function(){
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.privilege.addadmin&roomId="+this.roomId;
				            var adminlis = this.$el.find(".SID-adminbox > .rank-item");
				            var adminNum = adminlis.length;
				            if (adminNum > this.adminMaxNum) {
				            	fh.alert("会议室管理员设置超过上限了");
				            	return;
				            }
				            var adminPriv = this._setArr(adminlis);
				            var servicelis = this.$el.find(".SID-servicebox > .rank-item");
				            var serviceNum = servicelis.length;
				            if (serviceNum > this.serviceMaxNum){
				            	fh.alert("会议室服务员设置超过上限了");
				            	return;
				            }
				            var servicePriv = this._setArr(servicelis);
				            var param = "{\"adminList\":"+JSON.stringify(adminPriv)+",\"serviceList\":"+JSON.stringify(servicePriv)+"}";
				            var sear=new RegExp('undefined');
				            if(sear.test(param))
			            	{  
			            		fh.alert("服务异常，请刷新后重试");
			            	    return;
			            	}
				            $.ajax({
								type : "GET",
								url : url,
								data : {"jsonData":param},
								dataType:"json",
								success : function(data) {
									if (data.code == "1") {
										fh.alert("保存成功");
									} else {
										fh.alert(data.code + ":" + data.message);
									}
								},
								error : function(){
									fh.alert("数据保存失败");
								}
							});
						},
						_setArr:function(lis){
							var arrPriv = [];
				            for(var i=0,li;li=lis[i++];){
				            	var type = $(li).attr("data-type");
				            	var id = $(li).attr("data-depUuid");
				            	var deptOrder = $(li).attr("data-depOrder");
				            	var name = $(li).find("span").text();
				            	var privObj = new Object();
				            	privObj.type = type;
				            	privObj.entityId = id;
				            	privObj.entityName = name;
				            	if (type == "dept")
				            		privObj.deptOrder = deptOrder;
				            	arrPriv.push(privObj);
				            }
				            return arrPriv
						},
						_initPage:function(tabId){
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.privilege.query&roomId="+this.roomId;
				            if (tabId == 1) {
				            	url = servicePath + "?" + ropParam + "&method=mapps.meetingroom.privilege.queryadmin&roomId="+this.roomId;
				            }
				            $.ajax({
								type : "POST",
								url : url,
								success : function(data) {
									if (data.code == "1") {
										if (tabId == 1) {
											var depObj = "";
											var userObj2 = self.$el.find("#subForm2").find(".SID-userbox");
											var userObj3 = self.$el.find("#subForm3").find(".SID-userbox");
											userObj2.empty();
											userObj3.empty();
											self._initLi(data.adminList,depObj,userObj2);
											self._initLi(data.serviceList,depObj,userObj3);
										} else if (tabId == 0) {
											var depObj = self.$el.find(".SID-deplist");
											var userObj = self.$el.find("#subForm1").find(".SID-userbox");
											depObj.empty();
											userObj.empty();
											self._initLi(data.privileges,depObj,userObj);
										}
									} else {
										fh.alert(data.code + ":" + data.message);
									}
								},
								error : function(){
									fh.alert("数据获取失败");
								}
							});
						},
						_initLi:function(list,depObj,userObj){
							for(var i=0,privilege;privilege=list[i++];){
								if (privilege.type=='dept') {
									depObj.append("<li class='SID-priv-li rank-item' data-type='dept' data-depOrder='"+privilege.deptOrder+"'  data-depUuid='"+privilege.entityId+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+privilege.entityName+"</span></li>");
								} else if (privilege.type=='user') {
									userObj.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+privilege.entityId+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+privilege.entityName+"</span></li>");
								}
							}
						},
						_searchUser:function(event){
							var _self = this;
							var formObj = $(event.currentTarget).parents("form");
							var username = formObj.find(".SID-suInput").val();
							if (username == ""){
								formObj.find(".pop-rankbox-maskColor").css("display","none");
								return;
							}
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var param = "";
							param = formObj.serialize();
							//this.$el.find("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							var url = servicePath + "?" +ropParam + "&method=mapps.mr.thirdpart.mobileark.getusers"+param;
				            $.ajax({
								type : "POST",
								url : url,
								success : function(data) {
									if (data.code == "1") {
										_self._setUser(formObj,data.userList);
									}
								},
								error : function(){
								}
							});
						},
						_onClickCancel:function(){
							var self = this;
							fh.confirm('确定放弃此次编辑吗？', function() {
								self.parentObj.reloadRightView(self.roomId);
							});
						},
						_onClickAdminCancel:function(){
							var self = this;
							fh.confirm('确定放弃此次编辑吗？', function() {
								self.parentObj.reloadRightView(self.roomId,1);
							});
						},
						_orgAdd : function(){
							var depLis = this.$el.find(".SID-deplist > li");
							var depUuids = [];
							for(var i=0,li;li=depLis[i++];){
								depUuids.push($(li).attr("data-depUuid"));
							}
							this.openDlg.orgAddSnippetView= new OrgAddSnippetView();
							this.openDlg.orgAddSnippetView.render(this,depUuids);
						},
						_setDep:function(nodes){
							var deplist = $(".SID-deplist");
							deplist.empty();
							for(var i=0,node;node=nodes[i++];){
								deplist.append("<li class='SID-priv-li rank-item' data-type='dept' data-depOrder='"+node.depOrder+"' data-depUuid='"+node.depUuid+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+node.depName+"</span></li>");
				            }
						},
						_setUser:function(formObj,users){
							if (users.length == 0){
								return;
							}
							var userList = formObj.find(".SID-userlist");
							var userSearch = formObj.find(".pop-searchbar");
							userList.empty();
							var liObjs = formObj.find(".SID-userbox>li");
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
									userList.append("<li class='SID-priv-li rank-item SID-user-li' data-type='user' data-depUuid='"+user.userUuid+"'><span style='pointer-events:none;'>"+user.userName+"</span></li>");
									pushCount ++;
								}
							}
							if (pushCount == 0) {
								return ;
							}
							formObj.find(".pop-rankbox-maskColor").css("display","block");
						},
						_chooseUser:function(event){
							var formObj = $(event.currentTarget).parents("form");
							var type = $(event.target).attr("data-type");
			            	var id = $(event.target).attr("data-depUuid");
			            	var name = $(event.target).text();
			            	var userBox = formObj.find(".SID-userbox");
			            	formObj.find(".SID-suInput").val("");
			            	formObj.find(".pop-rankbox-maskColor").css("display","none");
			            	if (userBox.hasClass("SID-adminbox")) {
			            		var adminNum = this.$el.find(".SID-adminbox > .rank-item").length;
			            		if (adminNum >= this.adminMaxNum) {
			            			fh.alert("会议室管理员设置超上限");
			            			return;
			            		}
			            	} else if (userBox.hasClass("SID-servicebox")) {
			            		var serviceNum = this.$el.find(".SID-servicebox > .rank-item").length;
			            		if (serviceNum >= this.serviceMaxNum){
			            			fh.alert("会议室服务员设置超上限");
			            			return;
			            		}
			            	}
			            	userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+id+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+name+"</span></li>");
						},
						_onClickDelLi:function(e){
							var obj = $(e.target);
							obj.parent().remove();
						},
						_onClickDepClear:function(){
							$(".SID-deplist").empty();
						}
					});
			return RightsideSnippentView;
		});
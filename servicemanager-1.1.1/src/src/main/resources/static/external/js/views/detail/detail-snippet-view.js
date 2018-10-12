define(
	['jquery', 'views/communication-base-view', 'seedsui'
	, 'text!../../templates/detail/detailTemplate.html', 'text!../../templates/detail/template-default-base.html'
	, 'text!../../templates/detail/template-default-meet.html', 'text!../../templates/detail/template-default-attach.html'
	, 'text!../../templates/detail/detailInitTemplate.html'
	, 'text!../../templates/detail/noDataTemplate.html'],
	function($, CommunicationBaseView,seedsui,Template, baseTemplate, meetTemplate, attachTemplate,initTemplate,noDataTemplate) {
		var IndexRouterSnippentView = CommunicationBaseView
			.extend({
				events: {
					'click .SID-getDoc':'_onClickGetDocUrl'
				},
				initialize: function() {
					this.preRender();
					this.views = {};
					this.parentObj;
					this.meetingId="";
		        	//Data
		            //DOM
		            this.detailPage;this.detailPageId="ID-PageDetail";
		            this.rmenu,this.rmenuId="ID-Rmenu";//右上角菜单图标
		            this.rmenuPopover,this.rmenuPopoverId="ID-RmenuPopover";//右侧弹出菜单
		            this.tabbar,this.tabbarClass="SID-detail-tabbar";
		            this.tabs={};
		            this.pages={};
		            this.detailBaseId="ID-DetailBase";
		            this.detailMeetId="ID-DetailMeet";
		            this.detailAttachId="ID-DetailAttach";
		            this.detailSignId="ID-DetailSign";
		            
		            //Plugin
		            this.data;
		            this.toast=new Toast("提示");
		            this.refreshFlag=true;
				},
				render: function(parentObj,meetingId) {
					this.meetingId = meetingId;
					this.parentObj = parentObj;
					var html = _.template( initTemplate, { });
					this.$el.html(html);
					this.refresh(true);
					return this;
				},
				refresh: function(flag) {
					var self = this;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamV11');
					var url = servicePath;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&method=mapps.meeting.meeting.outerdetail&participantsId="+self.meetingId,
						success : function(data) {
							self.refreshFlag=false;
							$(".SID-loading").hide();
							if (data.code == "1") {
								self.data = data;
								self._setUpContent(data);
							} else {
								self.toast.setText(data.message);
								self.toast.show();
							}
						},
						error : function(){
							$(".SID-loading").hide();
							self.toast.setText("数据获取失败");
							self.toast.show();
						}
					});
				},
		        /*=========================
		          Method
		          ===========================*/
		        _activePage:function(id){
		        	//设置显隐与选中效果
					for(var pageId in this.pages){
						this.pages[pageId].classList.remove("active");
						this.tabs[pageId].classList.remove("active");
					}
					this.pages[id].classList.add("active");
					this.tabs[id].classList.add("active");
				},
				_onClickGetDocUrl:function(e){
					var self = this;
					var target = e.target;
					var documentId = target.getAttribute("data-id");
					var type = target.getAttribute("data-value");
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam')
		        	$.ajax({
		        		type:'post',
		        		url:servicePath,
		        		data : ropParam + "&method=mapps.thirdpart.mobileark.getdocurl&documentId="+documentId+"&privilege="+type+"",//3-获取文档下载地址，1-获取文档预览地址
		        		success : function(data) {
		        			//mPlus接口暂未开放预览
		        			if(data.docUrls.length>0 && type==1){
		        			}
		        			if(data.docUrls.length>0 && type==0){
		        				var docUrl = data.docUrls[0];
		        				window.open(docUrl);
	        				};
	        			},
						error : function(){
						}
		        	});
				},
				_loadDetailBase:function(){//baseTemplate, meetTemplate, attachTemplate, signTemplate
					var self=this;
					var html = _.template(
						baseTemplate, {
						'imgPath': self.constants.IMAGEPATH,
						'imgQrcode': self.data.qrcode,
						'meetingInfo' : self.data.meeting,
						'remarksList' : self.data.remarksList
					});
					self.$el.find("#"+self.detailBaseId).html(html);
				},
				_loadDetailMeet:function(){
					var self=this;
					var temp = meetTemplate;
					if (self.data.agendasInfo.length==0){
						temp = noDataTemplate;
					}
					var day = new Date();
					var month = day.getMonth()+1;
					month = month<10 ? "0"+month:month;
					var str = month + "." + day.getDate();
					var html = _.template(
							temp, {
						'imgPath': self.constants.IMAGEPATH,
						'agendasInfo' : self.data.agendasInfo,
						'day' : str,
						'status' : self.data.meeting.status
					});
					self.$el.find("#"+self.detailMeetId).html(html);
				},
				_loadDetailAttach:function(){
		            var self=this;
					var temp = attachTemplate;
					if (self.data.attachmentList.length==0){
						temp = noDataTemplate;
					}
		            var html = _.template(
		            		temp,{
	            		'imgPath': self.constants.IMAGEPATH,
						'attachmentList' : self.data.attachmentList
		            	});
		            self.$el.find("#"+self.detailAttachId).html(html); 
				},
				_loadData:function(){
					this._loadDetailBase();
					this._loadDetailMeet();
					this._loadDetailAttach();
				},
				/*=========================
		          Events
		          ===========================*/
		        _attach:function(){
		        	var self=this;
		        	//Events
		            this.rmenu.addEventListener("click",function(e){
						self._onClickRmenu(e);
					},false);
		            this.tabbar.addEventListener("click",function(e){
						self._onClickTabbar(e);
					},false);
					this.detailPage.addEventListener("click",function(e){
						self._onClickPage(e);
					},false);
		        },
		        /*=========================
		          Event Handler
		          ===========================*/
		        _onClickPage:function(e){
		        	var el=null;
		        	if(e.target.tagName=="LI"){
		        		el=e.target.parentNode.parentNode;
		        	}
		        	if(e.target.tagName=="UL"){
		        		el=e.target.parentNode;
		        	}
		        	
		        	if(!el || !el.classList.contains("app-popover")){
		        		this.rmenuPopover.classList.remove("active");
		        	}
		        },
		        _onClickTabbar:function(e){
					var target=e.target;
					if(target.classList.contains("tab")){
						var id=target.getAttribute('data-id');
		        		this._activePage(id);
					}
				},
				_onClickRmenu:function(e){
					//设置右上角菜单
					this.rmenuPopover.classList.toggle("active");
					e.stopPropagation();
				},
		        /*=========================
		          OnLoad
		          ===========================*/
		        _onLoad:function(){
		            var self=this;
		            //DOM
		            this.detailPage=document.getElementById(this.detailPageId);
		            this.rmenu=document.getElementById(this.rmenuId);
		            this.rmenuPopover=document.getElementById(this.rmenuPopoverId);

		            this.tabbar=document.querySelector("."+this.tabbarClass);
					this.tabs[this.detailBaseId]=document.querySelector("[data-id='"+this.detailBaseId+"']");
					this.tabs[this.detailMeetId]=document.querySelector("[data-id='"+this.detailMeetId+"']");
					this.tabs[this.detailAttachId]=document.querySelector("[data-id='"+this.detailAttachId+"']");

					this.pages[this.detailBaseId]=document.getElementById(this.detailBaseId);
					this.pages[this.detailMeetId]=document.getElementById(this.detailMeetId);
					this.pages[this.detailAttachId]=document.getElementById(this.detailAttachId);
					
					//Data
		            this._loadData();
		            //Attach
		            this._attach();

		        },
				_setUpContent: function(data) {
					var self = this;
					var html = _.template(
						Template, {
							'imgPath': self.constants.IMAGEPATH,
							'meetInfo': data.meeting
						});
					this.$el.html(html);
					//初始化
					this._onLoad();
				}
			});

		return IndexRouterSnippentView;
	});
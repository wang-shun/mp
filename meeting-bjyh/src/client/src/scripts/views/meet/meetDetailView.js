define([
    "app",

    "text!../../templates/meet/meetDetailTemplate.html",
    "text!../../templates/meet/meetDetailTabbarTemplate.html",
    "text!../../templates/meet/meetDetailSliderTemplate.html",
    "text!../../templates/meet/meetDetailForServiceTabbarTemplate.html",
    "text!../../templates/meet/meetDetailForServiceSliderTemplate.html"
], function(app, meetDetailTemplate, meetDetailTabbarTemplate, meetDetailSliderTemplate,meetDetailForServiceTabbarTemplate, meetDetailForServiceSliderTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.meetingId="";
        	this.meetingData;
        	this.isservice;
        	this.curtotalnum=0;
        	this.cursignnum=0;
        	this.prompt;
        	this.clickSequ="";
        },
        render:function(meetingId,isservice){
        	this.meetingId = meetingId;
            console.log("会议详情页meetDetailView：渲染");
            var self = this;
            //渲染页面
            var html=_.template(
            meetDetailTemplate,{
                noDataHTML:app.meetWarnHTML,
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            /*DOM*/
            this.tabbarContainer=document.getElementById("ID-MeetDetail-Tabbar");
            this.sliderContainer=document.getElementById("ID-MeetDetail-Pages");
            //this.sliderContainer.style.height=(window.innerHeight-96)+"px";
            this.sliderContainer.style.height="100%";
            //菜单弹出框
            this.menupop=new Dialog("#ID-MeetDetail-MenuPop",{
                position:"top-right",
                animation:"zoom",
                css:{overflow:"visible",top:"54px",right:"6px"}
            });
            //对话框
            this.confirm=new Alert("我是Confirm框",{
                onClickCancel:function(e){
                    e.hide();
                }
            });
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            /*加载数据*/
            if(isservice == "service"){
            	this.loadDataForService();
            }else{
            	this.loadData();
            }
        },
        refresh:function(meetingId,isservice){
        	this.meetingId = meetingId;
            app.loading.show();
            console.log("会议详情页meetDetailView：刷新");
            if(isservice == "service"){
            	this.loadDataForService();
            }else{
            	this.loadData();
            }
        },
        destroy:function(){
        	app.meetingDetail = {};
            console.log("会议详情页meetDetailView：移除");
        },
        loadData:function(){
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetDetail+"&sessionId="+window.token;
            var pageParam = "&meetingId="+self.meetingId+"&refreshFlag=true";
            app.loading.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	console.log(data);
                    if(data.code!=1){//请求错误
                    	if(app.isHomePage){
                    		self.$el.find("#ID-MeetDetail-Menu").hide();
	                        app.toast.setText("链接失效");
	        				app.toast.show();
	                        setTimeout(function(){
	                        	mplus.closeWindow();
	                        }, 1000);
                    	} else if (data.code == 0 && data.message == 'hasDelete') {
                    		self.$el.find("#ID-MeetDetail-Menu").hide();
                    	}
                        return;
                    }
                    self.errorPanel.removeClass("active");
                    if(data.meeting.status=="4"){//会议已取消
                    	self.$el.find("#ID-MeetDetail-Menu").hide();
                        self.sliderContainer.innerHTML=app.meetCancelHTML;
                        return;
                    }
            		self.meetingData = data;
                    //没有数据
                    if(data.meeting.length<=0){
                        self.sliderContainer.innerHTML=app.meetWarnHTML;
                        return;
                    }
                    // 按钮权限控制
                    var meetInfo = data.meeting;
                    self.$el.find("#ID-MeetDetail-Menu").show();
                    self.$el.find("#ID-MeetDetail-BtnCancel").show();
                    self.$el.find("#ID-MeetDetail-BtnQrcode").show();
                    self.$el.find("#ID-MeetDetail-BtnGroupChat").show();
                    self.$el.find("#ID-MeetDetail-BtnDel").show();
                    self.$el.find("#ID-MeetDetail-BtnFinish").show();
                    var countnum = 0;
                    var btnObj;
                    if (data.signinSequList.length<=0) {
                    	self.$el.find("#ID-MeetDetail-BtnQrcode").hide();
                    	countnum++;
                    } else {
                    	if (!((meetInfo.isSelfCreate == '1' || meetInfo.isSelfService == '1') && (meetInfo.status==1 || meetInfo.status==2))) {
                    		self.$el.find("#ID-MeetDetail-BtnQrcode").hide();
                    		countnum++;
	                    } else {
	                    	btnObj = self.$el.find("#ID-MeetDetail-BtnQrcode");
	                    }
                    }
                    if (!(meetInfo.hasGroup == 1 && meetInfo.status!=4)) {
                    	self.$el.find("#ID-MeetDetail-BtnGroupChat").hide();
                    	countnum++;
                    } else {
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnGroupChat");
                    }
                    if (((meetInfo.isSelfCreate == '1') && (meetInfo.status==1)) || meetInfo.status=='a'){
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnCancel");
                    } else {
                    	self.$el.find("#ID-MeetDetail-BtnCancel").hide();
                    	countnum++;
                    }
                    if (((meetInfo.isSelfCreate == '1') && (meetInfo.status==2))){
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnFinish");
                    } else {
                    	self.$el.find("#ID-MeetDetail-BtnFinish").hide();
                    	countnum++;
                    }
                    if (meetInfo.status==1 || meetInfo.status==2 || meetInfo.status=='a') {
                    	self.$el.find("#ID-MeetDetail-BtnDel").hide();
                    	countnum++;
                    } else {
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnDel");
                    }
                    if(countnum>=5){
                    	self.$el.find("#ID-MeetDetail-Menu").hide();
                    }else{
                    	btnObj.removeClass("underline");
                    }
                    //编译渲染
                    var tabbarHtml=_.template(meetDetailTabbarTemplate,{
                        noDataHTML:app.meetWarnHTML,
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    var sliderHtml=_.template(meetDetailSliderTemplate,{
                        noDataHTML:app.meetWarnHTML,
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.tabbarContainer.innerHTML=tabbarHtml;
                    self.sliderContainer.innerHTML=sliderHtml;
                    self.tabs=self.tabbarContainer.querySelectorAll(".tab");
                    //初始化插件
                    if(self.slider){
                        self.slider.destroy();
                    }
                    self.slider=self._initSlider();
                },
                error:function(msg){
                	self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
			//轮询签到情况
			self._querySignStatus();
        },
        
        loadDataForService:function(){
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetDetailForService+"&sessionId="+window.token;
            var pageParam = "&meetingId="+self.meetingId+"&refreshFlag=true";
            app.loading.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	console.log(data);
                    if(data.code!=1){//请求错误
                    	if(app.isHomePage){
                    		self.$el.find("#ID-MeetDetail-Menu").hide();
	                        app.toast.setText("链接失效");
	        				app.toast.show();
	                        setTimeout(function(){
	                        	mplus.closeWindow();
	                        }, 1000);
                    	} else if (data.code == 0 && data.message == 'hasDelete') {
                    		self.$el.find("#ID-MeetDetail-Menu").hide();
                    	}
                        return;
                    }
                    self.errorPanel.removeClass("active");

            		self.meetingData = data;
                    //没有数据
                    if(data.meeting.length<=0){
                        self.sliderContainer.innerHTML=app.meetWarnHTML;
                        return;
                    }
                    // 按钮权限控制
                    var meetInfo = data.meeting;
                    self.$el.find("#ID-MeetDetail-Menu").hide();
                    /*self.$el.find("#ID-MeetDetail-BtnCancel").show();
                    self.$el.find("#ID-MeetDetail-BtnQrcode").show();
                    self.$el.find("#ID-MeetDetail-BtnGroupChat").show();
                    self.$el.find("#ID-MeetDetail-BtnDel").show();
                    var countnum = 0;
                    var btnObj;
                    if (data.signinSequList.length<=0) {
                    	self.$el.find("#ID-MeetDetail-BtnQrcode").hide();
                    	countnum++;
                    } else {
                    	if (!((meetInfo.isSelfCreate == '1' || meetInfo.isSelfService == '1') && (meetInfo.status==1 || meetInfo.status==2))) {
                    		self.$el.find("#ID-MeetDetail-BtnQrcode").hide();
                    		countnum++;
	                    } else {
	                    	btnObj = self.$el.find("#ID-MeetDetail-BtnQrcode");
	                    }
                    }
                    if (!(meetInfo.hasGroup == 1 && meetInfo.status!=4)) {
                    	self.$el.find("#ID-MeetDetail-BtnGroupChat").hide();
                    	countnum++;
                    } else {
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnGroupChat");
                    }
                    if (!((meetInfo.isSelfCreate == '1') && (meetInfo.status==1 || meetInfo.status==2))){
                    	self.$el.find("#ID-MeetDetail-BtnCancel").hide();
                    	countnum++;
                    } else {
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnCancel");
                    }
                    if (meetInfo.status==1 || meetInfo.status==2) {
                    	self.$el.find("#ID-MeetDetail-BtnDel").hide();
                    	countnum++;
                    } else {
                    	btnObj = self.$el.find("#ID-MeetDetail-BtnDel");
                    }
                    if(countnum>=4){
                    	self.$el.find("#ID-MeetDetail-Menu").hide();
                    }else{
                    	btnObj.removeClass("underline");
                    }*/
                    //编译渲染
                    var tabbarHtml=_.template(meetDetailForServiceTabbarTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    var sliderHtml=_.template(meetDetailForServiceSliderTemplate,{
                        noDataHTML:app.meetWarnHTML,
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.tabbarContainer.innerHTML=tabbarHtml;
                    self.sliderContainer.innerHTML=sliderHtml;
                    self.tabs=self.tabbarContainer.querySelectorAll(".tab");
                    //初始化插件
                    if(self.slider){
                        self.slider.destroy();
                    }
                    //self.slider=self._initSlider();
                },
                error:function(msg){
                	self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },

        /*=========================
          Method
          ===========================*/
        _querySignStatus:function(){//轮询获取实时签到情况
        	var self=this;
            console.log(window.location.href);
        	if(window.location.href.indexOf("#ID-PageMeetDetail") == -1){
        		return;
        	}
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.querySignStatus+"&sessionId="+window.token;
            var pageParam = "&meetingId="+self.meetingId;
            var querysignstatus_second = 2000;
        	$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	console.log(data);
                    if(data.code!=1){//请求错误
                        return;
                    }
                    var totalnum = data.totalnum;
                    var signnum = data.signnum;
                    if(totalnum != 0){
                    	if(self.curtotalnum != 0){
                    		if(totalnum == self.curtotalnum){
                    			if(signnum>self.cursignnum){
                    				//弹窗
                        			self._signstatusprompt(signnum);
                        			
                        			self.curtotalnum=totalnum;
                                    self.cursignnum=signnum;
                                    //连续扫描
                        			setTimeout(function(){
                        				self.refresh(self.meetingId);
                        				//self._querySignStatus();
                    				},querysignstatus_second);
                    			}else if(signnum == self.cursignnum){
                    				if(signnum<totalnum){
                            			self.curtotalnum=totalnum;
                                        self.cursignnum=signnum;
                                        //连续扫描
                            			setTimeout(function(){
                            				self._querySignStatus();
                        				},querysignstatus_second);
                    				}else{
                    					
                    				}
                    			}else{
                    				
                    			}
                    		}else{
                    			self.curtotalnum=totalnum;
                                self.cursignnum=signnum;
                                //连续扫描
                    			setTimeout(function(){
                    				self._querySignStatus();
                				},querysignstatus_second);
                    		}
                    	}else{
                    		self.curtotalnum=totalnum;
                            self.cursignnum=signnum;
                            //连续扫描
                			setTimeout(function(){
                				self._querySignStatus();
            				},querysignstatus_second);
                    	}
                    }
                    
                    
//                    self.curtotalnum=totalnum;
//                    self.cursignnum=signnum;
//                    //弹窗
//        			self._signstatusprompt(signnum);
//        			//连续扫描
//        			setTimeout(function(){
//        				self._querySignStatus();
//    				},querysignstatus_second);
                },
                error:function(msg){
                	
                },
                complete:function(){
                	
                }
            });
        },
        _signstatusprompt:function(num){
        	//弹出框
//            this.prompt=new Prompt("您已签到"+num+"次",{
//                parent:this.$el[0],
//                css:{
//                    top:"340px",
//                    backgroundColor:"#20aeff"
//                }
//            });
//            this.prompt.show();
        	app.toast.setText("您已签到"+num+"次");
	    	app.toast.show();
        },
        
        _eachBreak:function(arr,fn){
            for(var i=0;i<arr.length;i++){
                if(fn(arr[i],i)==false)break;
            }
        },
        _tabActive:function(index){
            for(var i=0,t;t=this.tabs[i++];){
                t.classList.remove("active");
            }
            this.tabs[index].classList.add("active");
        },
        _initSlider:function(){
            var self=this;
            return new Swiper("#ID-MeetDetail-Pages",{
                freeModeMomentumBounceRatio:0,
                freeModeMomentumBounce:false,
                onSlideChangeEnd:function(e){
                    self._tabActive(e.activeIndex);
                }
            });
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back':'_onBack',
            'click #ID-MeetDetail-Menu' : '_onClickMenu',//点击菜单
            //'click #ID-MeetDetail-MenuPopMask' : '_onClickMenuPopMask',//点击遮罩
            'click #ID-MeetDetail-Tabbar .tab' : '_onClickTab',//点击tab
            'click .SID-MeetDetail-SignLi' : '_onClickSignLi',//点击签到详情
            'click .SID-MeetDetail-SignList' : '_onClickSignList',//点击签到列表
            'click #ID-MeetDetail-BtnQrcode':'_onClickBtnQrcode',//点击扫码签到
            'click #ID-MeetDetail-BtnGroupChat':'_onClickBtnGroupChat',//点击会议群聊
            'click #ID-MeetDetail-BtnCancel':'_onClickBtnCancel',//点击取消会议
            'click #ID-MeetDetail-BtnFinish':'_onClickBtnFinish',//点击结束会议
            'click #ID-MeetDetail-BtnDel':'_onClickBtnDel',//点击删除会议
            'click .SID-phone':'_onClickPhone'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh(this.meetingId);
        },
        _onBack:function(){
            if(app.isHomePage){
            	mplus.closeWindow();
                return;
            }
            history.go(-1)
        },
        _onClickPhone:function(e){
        	var phone = e.currentTarget.getAttribute("data-phone");
        	if (phone!="") {
        		mplus.callPhone({
        			phones: [phone], // 号码数组
        			success: function (res) {
    			    },
    			    fail: function (res) {
    				}
    			});
        	}
        },
        _onClickMenu:function(e){
            this.menupop.show();
            //this.menupop.classList.toggle("active");
        },
        _onClickTab:function(e){
            var index=0;
            this._eachBreak(this.tabs,function(n,i){
                if(n==e.target){
                    index=i;
                    return false;
                }
            });
            this._tabActive(index);
            this.slider.slideTo(index);
        },
        _onClickSignLi:function(e){
        	app.signDetail = [];
        	var signId = e.currentTarget.getAttribute("data-signId");
        	for(var i=0,signinData; signinData=this.meetingData.signinRecordList[i++];) {
        		if (signinData.sequId == signId) {
        			app.signDetail.push(signinData);
        		}
        	}
        	console.log(app.signDetail);
            var hash="#"+app.pages.meetSignList;//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
            e.stopPropagation();
        },
        _onClickSignList:function(e){
        	var self = this;
            var groupSign=e.currentTarget.getAttribute("data-groupSign");
            var groupColor=$(e.currentTarget).find(".progress-bar").css("background-color");//e.currentTarget.getAttribute("data-groupColor");
            
            if (this.clickSequ == groupSign) {
            	if(!e.currentTarget.classList.contains("active")){
                	for(var i=0,signinData; signinData=self.meetingData.signinRecordList[i++];) {
                		if (signinData.sequId == groupSign && signinData.signed == "Y") {
                			this.$el.find("[data-loginId="+signinData.personId+"]").css({backgroundColor:groupColor,borderColor:groupColor,color:"white"});
                		}
                	}
                    e.currentTarget.classList.add("active");
                }else{
                    this.$el.find("[data-groupForSign=1]").attr("style","");
                    e.currentTarget.classList.remove("active");
                }
            } else {
                $(".SID-MeetDetail-SignList").removeClass("active");
            	this.$el.find("[data-groupForSign=1]").attr("style","");
                e.currentTarget.classList.remove("active");
                for(var i=0,signinData; signinData=self.meetingData.signinRecordList[i++];) {
            		if (signinData.sequId == groupSign && signinData.signed == "Y") {
            			this.$el.find("[data-loginId="+signinData.personId+"]").css({backgroundColor:groupColor,borderColor:groupColor,color:"white"});
            		}
            	}
                e.currentTarget.classList.add("active");
            }
        	this.clickSequ = groupSign;
        },
        _onClickBtnQrcode:function(e){
        	app.signinSequList = this.meetingData.signinSequList;
            this.menupop.hide();
            var hash="#"+app.pages.meetQrcode;//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnGroupChat:function(e){
        	this.menupop.hide();
        	var self = this;
			var id = this.meetingData.meeting.groupId;
			var name = this.meetingData.meeting.meetingName;
			mplus.showGroupMessage({
				imaccount: id, // im群组账号
				name: name,
			    fail: function (res) {
			    	app.toast.setText("会议群聊打开失败");
			    	app.toast.show();
				}
			});
        },
        _onClickBtnCancel:function(e){
        	var self = this;
            this.confirm.setText("您确定取消此会议吗？");
            this.confirm.setOnClickOk(function(e){
            	e.hide();
                var url = app.serviceUrl;
                var ropParam = app.ropMethod.meetCancel+"&sessionId="+window.token;
                var pageParam = "&meetingId="+self.meetingId;
                app.loading.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			app.loading.hide();
	        			if(data.code == "1"){
					    	app.toast.setText("取消会议成功");
					    	app.toast.show();
					    	self._back();
	        			}else{
					    	app.toast.setText("取消会议失败");
					    	app.toast.show();
	        			}
					},
					error : function(){
						app.loading.hide();
				    	app.toast.setText("取消会议失败");
				    	app.toast.show();
					}
	        	});
            });
            this.confirm.show();
            this.menupop.hide();
        },
        _onClickBtnFinish:function(e){
        	var self = this;
        	this.confirm.setText("您确定结束此会议吗？");
            this.confirm.setOnClickOk(function(e){
            	e.hide();
                var url = app.serviceUrl;
                var ropParam = app.ropMethod.meetOver+"&sessionId="+window.token;
                var pageParam = "&meetingId="+self.meetingId;
                app.loading.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			app.loading.hide();
	        			if(data.code == "1"){
					    	app.toast.setText("结束会议成功");
					    	app.toast.show();
					    	self._back();
	        			}else{
					    	app.toast.setText("结束会议失败");
					    	app.toast.show();
	        			}
					},
					error : function(){
						app.loading.hide();
				    	app.toast.setText("结束会议失败");
				    	app.toast.show();
					}
	        	});
            });
            this.confirm.show();
            this.menupop.hide();
        },
        _onClickBtnDel:function(e){
        	var self = this;
            this.confirm.setText("您确定删除此会议吗？");
            this.confirm.setOnClickOk(function(e){
            	e.hide();
                var url = app.serviceUrl;
                var ropParam = app.ropMethod.meetDelete+"&sessionId="+window.token;
                var pageParam = "&meetingId="+self.meetingId;
                app.loading.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			app.loading.hide();
	        			if(data.code == "1"){
					    	app.toast.setText("删除会议成功");
					    	app.toast.show();
					    	self._back();
	        			}else{
					    	app.toast.setText("删除会议失败");
					    	app.toast.show();
	        			}
					},
					error : function(){
						app.loading.hide();
				    	app.toast.setText("删除会议失败");
				    	app.toast.show();
					}
	        	});
            });
            this.confirm.show();
            this.menupop.hide();
        },
        _back:function(){
        	if (app.routerViews.indexView) {
        		app.routerViews.indexView.loadData(1);
        	}
			setTimeout(function(){
				if(app.isHomePage){
	            	mplus.closeWindow();
	                return;
	            }
				history.go(-1);
			}, 1000);
        }
    });

    return view;
});
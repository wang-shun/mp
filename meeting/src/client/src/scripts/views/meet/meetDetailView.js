define([
    "app",

    "text!../../templates/meet/meetDetailTemplate.html",
    "text!../../templates/meet/meetDetailPopTemplate.html",
    "text!../../templates/meet/meetDetailTabbarTemplate.html",
    "text!../../templates/meet/meetDetailSliderTemplate.html"
], function(app, meetDetailTemplate, meetDetailPopTemplate, meetDetailTabbarTemplate, meetDetailSliderTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.meetingId="";
        	this.meetingData;
        	this.curtotalnum=0;
        	this.cursignnum=0;
        	this.prompt;
            this.data={};
        },
        render:function(meetingId){
        	this.meetingId = meetingId;
            console.log("会议详情页meetDetailView：渲染");
            var self = this;
            //渲染页面
            var html=_.template(
            meetDetailTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            /*DOM*/
            this.tabbarContainer=document.getElementById("ID-MeetDetail-Tabbar");
            this.sliderContainer=document.getElementById("ID-MeetDetail-Pages");
            this.sliderContainer.style.height=(window.innerHeight-96)+"px";
            this.popContainer=document.getElementById("ID-MeetDetail-MenuPop");
            //菜单弹出框
            this.menupop=new Dialog(this.popContainer,{
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
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
            /*加载数据*/
            this.loadData();
        },
        refresh:function(meetingId){
        	this.meetingId = meetingId;
            
            console.log("会议详情页meetDetailView：刷新");
            this.loadData();
        },
        destroy:function(){
        	app.meetingDetail = {};
            console.log("会议详情页meetDetailView：移除");
        },
        loadData:function(){
            var self=this;
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.meetDetail;
            var pageParam = "&meetingId="+self.meetingId+"&refreshFlag=true";
            
            self.loadPanel.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
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
                        self.data={};
                        return;
                    }
                    self.data=data;
                    self.errorPanel.removeClass("active");

            		self.meetingData = data;
                    //没有数据
                    if(data.meeting.length<=0){
                        self.sliderContainer.innerHTML=app.nodataHTML;
                        return;
                    }
                    //编译渲染
                    var tabbarHtml=_.template(meetDetailTabbarTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    var sliderHtml=_.template(meetDetailSliderTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        nodataHTML:app.nodataHTML,
                        data:data
                    });
                    var popHtml=_.template(meetDetailPopTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.tabbarContainer.innerHTML=tabbarHtml;
                    self.sliderContainer.innerHTML=sliderHtml;
                    self.popContainer.innerHTML=popHtml;
                    self.tabs=self.tabbarContainer.querySelectorAll(".tab");
                    //初始化插件
                    if(self.slider){
                        self.slider.destroy();
                    }
                    //滑动页
                    self.slider=new Swiper("#ID-MeetDetail-Pages",{
                        freeModeMomentumBounceRatio:0,
                        freeModeMomentumBounce:false,
                        onSlideChangeEnd:function(e){
                            self._tabActive(e.activeIndex);
                        }
                    });
                    //滑动二维码
                    self.sliderCode=new Swiper("#ID-MeetDetail-Qrcodes",{
                        nextButton: '#ID-MeetDetail-Qrcodes .swiper-button-next',
                        prevButton: '#ID-MeetDetail-Qrcodes .swiper-button-prev',
                        //自定义小点点
                        /*pagination: '#ID-MeetDetail-Qrcodes .swiper-pagination',
                        paginationBulletRender: function (e, index) {
                            var className="swiper-pagination-bullet";
                            if(e.activeIndex===index){
                                className+=" swiper-pagination-bullet-active";
                            }
                            return '<span class="' + className + '">' + (index + 1) + '</span>';
                        },*/
                        onInit:function(e){
                            e.container.find(".swiper-pagination").html(eval(e.activeIndex+1)+"/"+e.container.find(".swiper-slide").length);
                        },
                        onSlideChangeEnd:function(e){
                            e.container.find(".swiper-pagination").html(eval(e.activeIndex+1)+"/"+e.container.find(".swiper-slide").length);
                        }
                    });
                },
                error:function(msg){
                    self.data={};
                	self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    	self.loadPanel.hide();
                    }, 300);
                }
            });
			//轮询签到情况
			self._querySignStatus();
        },
        /*=========================
          Method
          ===========================*/
        _querySignStatus:function(){//轮询获取实时签到情况
        	var self=this;
        	if(app.history.currentHash.indexOf("ID-PageMeetDetail") == -1){
        		return;
        	}
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.querySignStatus;
            var pageParam = "&meetingId="+self.meetingId;
            var querysignstatus_second = 2000;
        	$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
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
                        				self._querySignStatus();
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
            'click #ID-MeetDetail-BtnEdit':'_onClickBtnEdit',//点击编辑会议
            'click #ID-MeetDetail-BtnCreatorChat':'_onClickBtnCreatorChat',//点击与创建人聊天
            'click #ID-MeetDetail-BtnGroupChat':'_onClickBtnGroupChat',//点击会议群聊
            'click #ID-MeetDetail-BtnCancel':'_onClickBtnCancel',//点击取消会议
            'click #ID-MeetDetail-BtnFinish':'_onClickBtnFinish',//点击结束会议
            'click #ID-MeetDetail-BtnDel':'_onClickBtnDel',//点击删除会议
            'click .SID-phone':'_onClickPhone',//点击创建人电话
            'click .SID-MeetDetail-ImgQrcode':'_onClickImgQrcode',//点击二维码图片
            'click .SID-MeetDetail-Download':'_onClickDownload'//点击附件下载
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
            history.go(-1);
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
        _onClickImgQrcode:function(e){
            var self=this;
            var target=e.target;
            var current=target.src;
            var urls=(function(){
                var arr=[];
                for(var i=0,sign;sign=self.data.signinSequList[i++];){
                    arr.push(sign.qrcode);
                }
                return arr;
            })();
            console.log(current,urls);
            mplus.previewImage({
                current:current, // 当前显示图片的http链接
                urls:urls // 需要预览的图片http链接列表
            });
        },
        _onClickBtnCreatorChat:function(e){
        	var self = this;
			var id = this.meetingData.meeting.sponsor;
			var name = this.meetingData.meeting.person;
			mplus.searchOrgMembers({
				condition: id, // 模糊查询条件
				limit: '20',// 分页查询每页返回数据条数，如20
				from: '0',// 分页查询从第几页开始，如0，第一页以此类推
				success: function (res) {
					var orgMemberArr = res.orgMembers; // 返回部门成员数组
					var tempIMId = "";
					for(var i=0;i<orgMemberArr.length;i++){
						if(id == orgMemberArr[i].loginId){
							tempIMId = orgMemberArr[i].imAccount;
						}
					}
					if(tempIMId == ""){
						app.toast.setText("获取im账号失败");
				    	app.toast.show();
				    	return;
					}
					mplus.showChatMessage({
						imaccount: tempIMId, // im群组账号
						name: name,
					    fail: function (res) {
					    	app.toast.setText("与"+name+"("+id+")聊天打开失败");
					    	app.toast.show();
						}
					});
			    },
			    fail: function (res) {
			    	app.toast.setText("获取im账号失败");
			    	app.toast.show();
				}
			});
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
			    	app.toast.setText("会议群聊不存在或已解散");
			    	app.toast.show();
				}
			});
        },
        _onClickBtnEdit:function(e){
            app.mergeMeetingData=this.data;
            this.menupop.hide();
            var hash="#"+app.pages.meetAdd+"/1";//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnCancel:function(e){
        	var self = this;
            this.confirm.setText("您确定取消此会议吗？");
            this.confirm.setOnClickOk(function(e){
            	e.hide();
                var url = window.serviceUrl;
                var ropParam = app.ropMethod.meetCancel;
                var pageParam = "&meetingId="+self.meetingId;
                
                self.loadPanel.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			
	        			self.loadPanel.hide();
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
						
						self.loadPanel.hide();
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
                var url = window.serviceUrl;
                var ropParam = app.ropMethod.meetOver;
                var pageParam = "&meetingId="+self.meetingId;
                
                self.loadPanel.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			
	        			self.loadPanel.hide();
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
						self.loadPanel.hide();
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
                var url = window.serviceUrl;
                var ropParam = app.ropMethod.meetDelete;
                var pageParam = "&meetingId="+self.meetingId;
                self.loadPanel.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			self.loadPanel.hide();
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
						self.loadPanel.hide();
				    	app.toast.setText("删除会议失败");
				    	app.toast.show();
					}
	        	});
            });
            this.confirm.show();
            this.menupop.hide();
        },
        _onClickDownload:function(e){
            var self = this;
            var target = e.currentTarget;
            var documentId = target.getAttribute("data-id");
            var type = target.getAttribute("data-value");
            var url=window.serviceUrl;
            var ropParam = app.ropMethod.downloadAttach;
            $.ajax({
                type:'post',
                url:url,
                data : ropParam + "&documentId="+documentId+"&privilege="+type+"",//3-获取文档下载地址，1-获取文档预览地址
                success : function(data) {
                    //mPlus接口暂未开放预览
                    if(data.docUrls.length>0 && type==1){
                        mplus.previewImage({
                            current: '', // 当前显示图片的http链接
                            urls: [data.docUrls[0]] // 需要预览的图片http链接列表
                        });
                    }
                    if(data.docUrls.length>0 && type==0){
                        var docUrl = data.docUrls[0];
                        mplus.openWithBrowser({
                            url: docUrl, // url地址
                            success: function (res) {
                                   // 发送成功回调
                            },
                            fail: function (res) {
                                self.toast.setText(res.errMsg);
                                self.toast.show();
                            }
                        });
                    };
                },
                error : function(){
                }
            });
        },
        _back:function(){
			app.routerViews.indexView.loadData();
			setTimeout(function(){
				history.go(-1);
			}, 1000);
        }
    });

    return view;
});
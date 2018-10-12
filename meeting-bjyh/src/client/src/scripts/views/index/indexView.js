define([
    "app",

    "text!../../templates/indexV20/indexTemplate.html",
    "text!../../templates/index/indexListTemplate.html",
    "text!../../templates/indexV20/roomListTemplate.html",
    "text!../../templates/indexV20/checkListTemplate.html"
], function(app, indexTemplate, indexListTemplate,roomListTemplate,checkListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.offset = 1;
        	this.limit = 10;
        	this.timestamp = 0;
        	this.endflag = 0;
        	this.initFlag = 0;
            this.dragFlag=0;
			this.drag = [];
			this.pagination=[
 				{
                     hasData:false,
                     offset:1,//当前页数
                     limit:10,//每页条数
                     timestamp:0,//时间戳
                     endflag:0//结束标识
 	            },
 	            {
 	                hasData:false,
 	                offset:1,
 	                limit:10,
 	                timestamp:0,
 	                endflag:0
 	            },
 	            {
 	                hasData:false,
 	                offset:1,
 	                limit:10,
 	                timestamp:0,
 	                endflag:0
 	            }
 			];
			// 会议室查询条件参数
        	this.timeStart = "";
            this.timeEnd = "";
            this.peopleNum = 0;
            this.roomLiDom;
            // 审批列表信息用于传入详情页面
        	this.meetingList=[];
            //
            this.promptHeight = ["44px",'156px','44px'];
        },
        render:function(){
            var self = this;
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            this.initLoginUserInfo();
        },
        initLoginUserInfo:function(){
        	var self = this;
			app.loading.show();
        	// 登录人信息初始化
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.inituserinfo+"&sessionId="+window.token;
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                    if(data.code!=1){//请求错误
                    	self.errorPanel.addClass("active");
                        return;
                    }

                	app.loginUser.username = data.user.userName;
                	app.loginUser.loginId = data.user.loginId;
                	app.loginUser.deptId = data.user.deptUuid;
                	// 管理员标识处理
                	app.loginUser.isAdmin = data.adminFlag;
                	
					self.$el.html("");
					//数据请求错误
					self.errorPanel=self.$el.find(".SID-Error");
					if(!self.errorPanel[0]){
						self.$el.append(app.errorHTML);
						self.errorPanel=self.$el.find(".SID-Error");
					}
                    //渲染页面
                    var html=_.template(
                    indexTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        isAdmin: app.loginUser.isAdmin
                    });
                    self.$el.append(html);
                    
                	self.$el.find(".SID-meeting-page").hide();
                	self.$el.find(".SID-check-page").hide();
                	if (app.loginUser.isAdmin != 1) {
                		self.$el.find(".SID-check-topbar").remove();
                	} else {
                		self.$el.find(".SID-check-topbar").show();
                	}
                	self.initPlug();
                	self.initFlag = 1;
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
        initPlug:function(){
            var self = this;
            this._setPageHeight(0);
            //滑动页面
			this.sliderContainers=document.querySelectorAll("#ID-Pages > .slider-wrapper > .slider-slide");
            this.sliderContainer=document.getElementById("ID-Pages");
            //this.sliderContainer.style.height=(window.innerHeight-this.topPx)+"px";

            this.tabbarContainer=document.getElementById("ID-Tabbar");
            this.tabs=this.tabbarContainer.querySelectorAll(".SID-Tabbar");

            this.activeIndex=0;
			this.slider=new Slider("#ID-Pages",{
                onSlideChangeEnd:function(e){
                	if (e.activeIndex==self.activeIndex){
                		return;
                	}
                	self.prompt.hide();
                    self._tabActive(e.activeIndex);
                    if(!self.pagination[e.activeIndex].hasData){
                    	self.$el.find(".slider-slide").eq(e.activeIndex).scrollTop(0);
						self.loadData(e.activeIndex);
					}
                    self.activeIndex = e.activeIndex;
                }
            });

            //header
            this.header=this.$el.find("header")[0];
            
            //内容区域
            this.article=this.$el.find("article");

            //列表
            this.listContainer=this.$el.find(".SID-Index-List");
            // -------------会议室 start-------------- 
            //时间控件分钟数据
            this.minutesData=function(){
                var data=[];
                for(var i=0;i<60;i=i+10){
                    var temp=i<10?"0"+i:i;
                    data.push({key:temp,value:temp+"分"});
                }
                return data;
            }();
            //时间控件小时数据
            this.hoursData=function(){
                var startData=[];
                for(var i=7;i<=21;i++){
                    var temp=i<10?"0"+i:i;
                    startData.push({key:temp,value:temp+"时"});
                }
                var endData=startData.slice(0);
                endData.push({key:"22",value:"22时"});
                return {
                    startHours:startData,
                    endHours:endData
                };
            }();
            //筛选框
            this.dialog=new Dialog("#ID-Room-DialogFilter",
                {
                    overflowContainer:self.$el[0],
                    position:"top",
                    animation:"slideDown",
                    css:{width:"100%",paddingTop:"44px"},
                    onHid:function(e){
                        self.header.style.zIndex="2";
                    }
                }
            );
            // -------------会议室 end-------------- 
            // -------------会议 start-------------- 
            //日历
            this.divCalendar=this.$el.find("#ID-Index-Calendar");
            this.calendar=new Calendar(this.divCalendar[0],{
                viewType:"week",
                isShowDayNum:true,
                //disableBeforeDate:new Date(),
                activeDate:new Date(),
                prevHTML:'<i class="icon-arrowleft"></i>',
                nextHTML:'<i class="icon-arrowright"></i>',
                onChange:function(e){
                    self.dragFlag=0;
                	if (self.initFlag == 1) {
                        self.$el.find(".slider-slide").eq(1).scrollTop(0);
                		self.loadData(1);
                	}
                }
            });
            // -------------会议 end-------------- 
            // -------------审核 start-------------- 
            //筛选框
            this.dialogCheck=new Dialog("#ID-Check-DialogFilter",
                {
                    overflowContainer:self.$el[0],
                    position:"top",
                    animation:"slideDown",
                    css:{width:"100%",paddingTop:"44px"},
                    onHid:function(e){
                        self.header.style.zIndex="2";
                        self.$el.find("#ID-Check-BtnFilter").removeClass("active");
                    },
                    onClickMask:function(e){
                        e.hide();
                    }
                }
            );
            // -------------审核 end-------------- 
            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.$el[0],
                css:{
                    top:self.promptHeight[0],
                    backgroundColor:"#20aeff"
                }
            });

            //下拉刷新
            for(var i=0;i < this.sliderContainers.length;i++) {
            	this._initDrag(i);
            }
            
            this.imglazy=new ImgLazy(document.querySelector("#ID-PageIndex-Section > article"));
            //加载数据
            this.loadData(0);
        },
        _initDrag:function(index){
        	var self = this;
        	this.drag[index]=DfCircle({
                overflowContainer:this.sliderContainers[index],
                parent:this.sliderContainers[index],
                topParent:this.sliderContainers[index],
                bottomParent:this.sliderContainers[index],
                onTopRefresh:function(e){
                    console.log("头部刷新");
                    self.dragFlag=1;
                    self.loadData($(".SID-Tabbar.active").attr("data-index"));
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomRefresh:function(e){
                    console.log("底部刷新");
                    self.loadMoreData($(".SID-Tabbar.active").attr("data-index"));
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
        	this.drag[index].bottomNoData();
            //this.drag[index].bottomContainer.style.display="block";
        },
        _setPageHeight:function(index){
        	var docHeight = document.documentElement.clientHeight;
            var bottomHeight = 61;
            var topHeight = 44;
            var calendarHeight = 112;
            var height = docHeight-topHeight-bottomHeight;
            if (index == 1) {
            	height = docHeight-topHeight-bottomHeight-calendarHeight;
            }
        	this.$el.find("#ID-Pages").css("height",height+"px");
        	this.$el.find(".slider-container").css("height",height+"px");
        	this.$el.find(".slider-wrapper").css("height",height+"px");
        	this.$el.find(".slider-slide").css("height",height+"px");
        },
        _tabActive:function(index){
            this.prompt.hide();
            for(var i=0,t;t=this.tabs[i++];){
                t.classList.remove("active");
            }
            this.tabs[index].classList.add("active");
            this._setPageHeight(index);
            if (index == 0) {
            	this.$el.find(".SID-room-page").show();
	            this.$el.find(".SID-meeting-page").hide();
	            this.$el.find(".SID-check-page").hide();
            } else if (index == 1) {
            	this.$el.find(".SID-room-page").hide();
	            this.$el.find(".SID-meeting-page").show();
	            this.$el.find(".SID-check-page").hide();
            } else if (index == 2) {
            	this.$el.find(".SID-room-page").hide();
	            this.$el.find(".SID-meeting-page").hide();
	            this.$el.find(".SID-check-page").show();
            }
        },
        refresh:function(){
        	if (this.initFlag==0) {
        		this.initLoginUserInfo();
        		return;
        	}
            this.dragFlag=0;
            console.log("首页：刷新");
            this.$el.find("article").scrollTop(0);
            this.pagination[0].hasData=false;
            this.pagination[1].hasData=false;
            this.pagination[2].hasData=false;
            this.$el.find(".slider-slide").scrollTop(0);
            if ($(".SID-Tabbar.active").attr("data-index")) {
            	this.loadData($(".SID-Tabbar.active").attr("data-index"));
            } else {
            	this.loadData(0);
            }
        },
        refreshByIndex:function(activeIndex){
        	if (this.initFlag==0) {
        		this.initLoginUserInfo();
        		return;
        	}
            this.dragFlag=0;
            console.log("首页：刷新");
            this.$el.find("article").scrollTop(0);
            this.pagination[0].hasData=false;
            this.pagination[1].hasData=false;
            this.pagination[2].hasData=false;
            this.$el.find(".slider-slide").scrollTop(0);
            this.loadData(activeIndex);
        },
        destroy:function(){
            console.log("首页：移除");
        },
        loadData:function(activeIndex){
        	if (activeIndex==2){
            	this.meetingList = [];
        	}
        	this.reloadPage(false,activeIndex);
        },
        loadMoreData:function(activeIndex){
        	this.reloadPage(true,activeIndex);
        },
        _getParam:function(activeIndex){
			// rop 接口参数
            var ropParam = app.ropMethod.meetList+"&sessionId="+window.token;
            // 分页参数
            var pageParam = "&offset="+this.pagination[activeIndex].offset
            +"&limit="+this.pagination[activeIndex].limit
            +"&timestamp="+this.pagination[activeIndex].timestamp;
            // 查询条件参数
        	var businessParam = "";
            if (activeIndex == 0) {
        		ropParam = app.ropMethod.meetRoomList+"&sessionId="+window.token;
            	businessParam = "";
            	if (this.timeStart != "" && this.timeEnd != "") {
            		businessParam += "&reservedStartTime=" + this.timeStart + ":00";
            		businessParam += "&reservedEndTime=" + this.timeEnd + ":00";
	            }
	            if (this.peopleNum != "") {
	            	businessParam += "&capacity=" + this.peopleNum;
	            }
        		return ropParam+pageParam+businessParam;
            } else if (activeIndex == 1) {
            	// 查询条件参数
                var year = this.calendar.activeDate.year();
                var month = this.calendar.activeDate.month();
                var day = this.calendar.activeDate.date();
                var selectDate = year + "-" + month + "-" + day;
            	businessParam = "&selectDate="+selectDate;
        		return ropParam+pageParam+businessParam;
	        } else if (activeIndex == 2) {
	        	ropParam = app.ropMethod.meetRoomApprove+"&sessionId="+window.token;
	        	businessParam = "";
	            var deviceList = this.$el.find(".SID-Check-State.active");
	            $.each(deviceList, function (i, n) {
	            	if (i==0) {
	            		businessParam += "&status=" + $(n).attr("data-status");
	            	} else {
	            		businessParam += "," + $(n).attr("data-status");
	            	}
	            });
    			return ropParam+pageParam+businessParam;
	        }
        	return false;
        },
        reloadPage:function(nextPageFlag,activeIndex){
            if(this.dragFlag==0)app.loading.show();
            var self = this;
            // 是否为加载下一页  true 是
			if (!nextPageFlag) {
				this.pagination[activeIndex].offset = 1;
				this.pagination[activeIndex].timestamp = 0;
            }
            var url = app.serviceUrl;
            var param = this._getParam(activeIndex);
            if (!param) {
            	return false;
            }
            $.ajax({
            	url : url,
				data : param,
                success:function(data){
                    if(data.code!=1){//请求错误
                        return;
                    }
                    self.errorPanel.removeClass("active");
                    self.pagination[activeIndex].offset = self.offset + 1;
					self.pagination[activeIndex].timestamp = data.timestamp;
					self.pagination[activeIndex].endflag = data.endflag;

                    //编译渲染
					if (activeIndex == 0) {
						var listHTML=_.template(roomListTemplate,{
	                        noDataHTML:app.nodataHTML,
	                        imgPath: app.constants.IMAGEPATH,
	                        data:data.room
	                    });
						var listData = data.room;
					} else if (activeIndex == 1) {
	                    var listHTML=_.template(indexListTemplate,{
	                        noDataHTML:app.noMeetHTML,
	                        imgPath: app.constants.IMAGEPATH,
	                        data:data.meeting
	                    });
						var listData = data.meeting;
					} else if (activeIndex == 2) {
						//编译渲染
	                    var listHTML=_.template(checkListTemplate,{
	                        noDataHTML:app.nodataHTML,
	                        imgPath: app.constants.IMAGEPATH,
	                        data:data.reserved
	                    });
						var listData = data.reserved;
						self.meetingList = listData;
					}
                    //没有数据
                    if(listData.length<=0){
                        //标识头部与尾部状态
                        self.drag[activeIndex].topNoData();
                        self.drag[activeIndex].bottomNoData();
                        self.listContainer[activeIndex].innerHTML=listHTML;
                        self.article[0].style.overflow="hidden";
                        return;
                    }

                    self.article[0].style.overflow="auto";

                    if (!nextPageFlag) {//头部刷新
                        self.listContainer[activeIndex].innerHTML=listHTML;
                        setTimeout(function(){
                            if (self.pagination[activeIndex].endflag) {
                                self.drag[activeIndex].topNoData();
                                self.drag[activeIndex].bottomNoData();
                            }else{
                                self.drag[activeIndex].topComplete();
                            }
                            if(self.dragFlag){
                            	self.prompt.setCssTop(self.promptHeight[activeIndex]);
                                self.prompt.show();
                            }
                            self.dragFlag=1;
                        }, 300);
                    }else{//底部刷新
                        self.drag[activeIndex].bottomContainer.style.display="block";
                        self.listContainer[activeIndex].innerHTML+=listHTML;
                        if (self.pagination[activeIndex].endflag) {
                            self.drag[activeIndex].bottomNoData();
                        }else{
                            self.drag[activeIndex].bottomComplete();
                        }
                    }
                    if (activeIndex == 0) {
                    	self.imglazy.update();
                    }
                    self.pagination[activeIndex].hasData=true;
                },
                error:function(msg){
                    //标识头部与尾部状态
                    self.drag[activeIndex].topNoData();
                    self.drag[activeIndex].bottomNoData();

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
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Back':'_onBack',
            'click .SID-Error' : '_onClickPanelError',
            'click #ID-Index-BtnSearch':'_onClickBtnSearch',
            'click #ID-Index-BtnAdd':'_onClickBtnAdd',
            'click #ID-Index-BtnCheck':'_onClickBtnCheck',
            'click .SID-Index-Li': '_onClickLi',
            
            'click #ID-Tabbar .tab' : '_onClickTab',//点击tab
            
            'click #ID-Room-BtnFilter' : '_onClickBtnFilter',
            'click .SID-DivDateTime' : '_onClickDivDateTime',
            'click #ID-Room-BtnReset' : '_onReset',
            'click #ID-Room-BtnSubmit' : '_onSubmit',
            'click .SID-Room-Li' : '_onClickRoomLi',

            'click #ID-Check-BtnFilter' : '_onClickBtnFilterCheck',
            'click .SID-Check-Li' : '_onClickCheckLi',
            'click .SID-Check-State' : '_onClickState'
        },

        /*=========================
          Event Handler
          ===========================*/
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
		_eachBreak:function(arr,fn){
            for(var i=0;i<arr.length;i++){
                if(fn(arr[i],i)==false)break;
            }
        },
        /* -------------审批 start--------------  */
        _onClickBtnFilterCheck:function(e){
            this.header.style.zIndex="11";
            this.dialogCheck.toggle();
            if(this.dialogCheck.isHid){
                e.currentTarget.classList.remove("active");
            }else{
                e.currentTarget.classList.add("active");
            }
        },
        _onClickState:function(e){
            var target=e.currentTarget;
            var state=target.getAttribute("data-state");
            e.currentTarget.classList.toggle("active");
            this.$el.find(".slider-slide").eq(2).scrollTop(0);
            this.loadData(2);
        },
        _onClickCheckLi:function(e){
        	var reservedId = e.currentTarget.getAttribute("data-reservedId");
        	for(var i=0,room;room=this.meetingList[i++];){
				if (room.reservedId==reservedId)
					app.approvalDetail=room;
			}
            var hash="#"+app.pages.checkDetail;//会议室预定详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        /* -------------审批 end--------------  */
        /* -------------会议室 start--------------  */
        _onClickRoomLi:function(e){
        	this.roomLiDom = $(e.currentTarget);
        	var roomId=e.currentTarget.getAttribute("data-roomId");
        	var startTime = escape(this.timeStart==""?"-1":this.timeStart);
        	var endTime = escape(this.timeEnd==""?"-1":this.timeEnd);	
            var hash="#"+app.pages.roomDetail+"/"+roomId+"/"+startTime+"/"+endTime;//会议室详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _changeRoomCollection:function(flag){
        	if (flag) {
        		this.roomLiDom.find(".SID-collection-div").show();
        	} else {
        		this.roomLiDom.find(".SID-collection-div").hide();
        	}
        },
        _onClickBtnFilter:function(e){
            this.header.style.zIndex="11";
            this.dialog.toggle();
        },
        _onSubmit:function(e){
            this.timeStart = this.$el.find("[name='NID-Room-TimeStart']").val();
            this.timeEnd = this.$el.find("[name='NID-Room-TimeEnd']").val();
            var num = this.$el.find("[name='NID-Room-PeopleNum']").val();
            var reg = /^\+?[1-9]\d{0,3}(\.\d*)?$/;
            if (num != "" && !reg.test(num)) {
            	app.toast.setText("容量请设置在1-9999的范围");
                app.toast.show();
            	return;
            }
            this.peopleNum = num;
            this.dialog.hide();
            this.$el.find(".slider-slide").eq(0).scrollTop(0);
            this.loadData(0);
        },
        _onReset:function(e){
            this.$el.find("[name='NID-Room-TimeStart']").val("").parent().removeClass("inputbox-error");
            this.$el.find("[name='NID-Room-TimeEnd']").val("").parent().removeClass("inputbox-error");
            this.$el.find("[name='NID-Room-PeopleNum']").val("").parent().removeClass("inputbox-error");
        },
        //点击日期选择
        _onClickDivDateTime:function(e){
            var self=this;
            var defaults=[];
            var input=e.target.querySelector(".input-text");
            if(input.value!=""){
                var split=input.value.split(" ");
                var split1=split[0].split("-");
                var split2=split[1].split(":");
                defaults[0]=split1[0];
                defaults[1]=split1[1];
                defaults[2]=split1[2];
                defaults[3]=split2[0];
                defaults[4]=split2[1];
            }else{
                var date=new Date().setMinuteCeil(10);
                if(input.classList.contains("SID-DateTimeEnd")){
                    date.plusMinute(20);
                }
                defaults[0]=date.getFullYear();
                defaults[1]=date.getMonth()+1;
                defaults[2]=date.getDate();
                defaults[3]=date.getHours();
                defaults[4]=date.getMinutes();
            }
            this._newSpDateTime(defaults,input);
            setTimeout(function(){
                app.spDateTime.show();
            },10);
        },
        //时间控件-动态更新分钟
        _newSpMinutes:function(hourKey,minuteKey){
            var self=this;
            var minuteKey=minuteKey;
            var minutes=this.minutesData.slice(0);
            if(hourKey==="22"){
                minutes=[{key:"00",value:"00分"}];
                minuteKey="00";
            }else if(hourKey==="07"){
                minutes.shift();
            }
            app.spDateTime.scrollpicker.replaceSlot(4,minutes,minuteKey,'text-center');//修改第五项
        },
        //时间控件
        _newSpDateTime:function(defaults,input){
            var self=this;
            app.spDateTime=new SpDate({
                //parent:self.$el[0],
                viewType:"datetime",
                //hoursData:self.hoursData.startHours,
                minutesData:self.minutesData,
                isSimpleYear:true,
                minYear:new Date().getFullYear()-5,
                maxYear:new Date().getFullYear()+5,
                yearClass:"text-center",
                monthClass:"text-center",
                dayClass:"text-center",
                hourClass:"text-center",
                minuteClass:"text-center",
                onInit:function(e){
                    //点击开始时间
                    if(input.classList.contains("SID-DateTimeStart")){
                        e.hours=self.hoursData.startHours;
                    }else if(input.classList.contains("SID-DateTimeEnd")){//点击结束时间
                        e.hours=self.hoursData.endHours;
                    }
                },
                onScrollEnd:function(e){
                    //点击结束时间
                    if(!input.classList.contains("SID-DateTimeEnd"))return;
                    //小时算分钟
                    if(e.scrollpicker.activeSlot.index==3){
                        hourKey=e.scrollpicker.activeOptions[3]["key"];
                        minuteKey=e.scrollpicker.activeOptions[4]["key"];
                        self._newSpMinutes(hourKey,minuteKey);//更新总天数
                    }
                },
                onClickDone:function(e){
                    var activeDate=new Date(e.activeText.replace(/-/g,"/"));
                    
                    //点击开始时间
                    if(input.classList.contains("SID-DateTimeStart")){
                        //不合法的时间
                        if(activeDate<=new Date()){
                            app.toast.setText("时间不能早于当前时间");
                            app.toast.show();
                            return;
                        }

                        var endInput=input.parentNode.nextElementSibling.querySelector(".SID-DateTimeEnd");
                        //如果结束时间为空或者开始时间大于结束时间
                        var endInputDate=new Date(endInput.value.replace(/-/g,"/"));
                        if((endInput.value=="") || (activeDate >= endInputDate)){
                            //设置结束时间
                            var endTime=new Date(activeDate).plusMinute(10);
                            endInput.value=endTime.format("yyyy-MM-dd hh:mm");
                        }
                    //点击结束时间
                    }else if(input.classList.contains("SID-DateTimeEnd")){
                        //不合法的时间
                        if(new Date(activeDate)<=new Date().setMinuteCeil(10)){
                            app.toast.setText("无效的时间，请重新设置");
                            app.toast.show();
                            return;
                        }

                        var prevInput=input.parentNode.previousElementSibling.querySelector(".SID-DateTimeStart");
                        //如果开始时间为空或者结束时间小于开始时间
                        var prevInputDate=new Date(prevInput.value.replace(/-/g,"/"));
                        if((prevInput.value=="") || (activeDate <= prevInputDate)){
                            //设置开始时间
                            var startTime=activeDate.minusMinute(10);
                            prevInput.value=startTime.format("yyyy-MM-dd hh:mm");
                        }
                    }
                    input.value=e.activeText;
                    e.scrollpicker.hide();
                },
                onShowed:function(e){
                    if(input.classList.contains("SID-DateTimeEnd")){//点击结束时间
                        var hourKey=e.scrollpicker.activeOptions[3]["key"];
                        var minuteKey=e.scrollpicker.activeOptions[4]["key"];
                        self._newSpMinutes(hourKey,minuteKey);
                    }
                },
                onHid:function(e){
                    e.scrollpicker.destroy();
                }
            });
            if(defaults && defaults.length>0){
                app.spDateTime.setDefaultYear(defaults[0]);
                app.spDateTime.setDefaultMonth(defaults[1]);
                app.spDateTime.setDefaultDay(defaults[2]);
                app.spDateTime.setDefaultHour(defaults[3]);
                app.spDateTime.setDefaultMinute(defaults[4]);
                app.spDateTime.update();
            }
        },
        /* -------------会议室 end--------------  */
        _onBack:function(e){
            mplus.closeWindow();
        },
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onClickBtnSearch:function(e){
            var hash="#"+app.pages.meetSearch;//会议搜索页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnAdd:function(e){
            var hash="#"+app.pages.meetAdd;//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnCheck:function(e){
            var hash="#"+app.pages.check;//审批页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickLi:function(e){
        	var meetingId = $(e.currentTarget).attr("data-meetingId");
        	if (meetingId==""){
        		return;
        	}
            var hash="#"+app.pages.meetDetail+"/"+meetingId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
    });

    return view;
});
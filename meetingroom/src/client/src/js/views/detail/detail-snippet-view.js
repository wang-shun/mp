define(
	['jquery', 'views/communication-base-view'
	, 'text!../../templates/detail/detailTemplate.html', 'text!../../templates/detail/detailContentTemplate.html','text!../../templates/detail/dayList.html','text!../../templates/detail/planDetail.html'],
	function($, CommunicationBaseView, Template, contentTemplate , dayListTemplate) {
		var IndexRouterSnippentView = CommunicationBaseView
			.extend({
				events: {
					'click .SID-Favorite': '_onClickFavorite',
					'click .SID-MoreDepute':'_onClickMoreDepute',
					'click .SID-BtnDetailSubmit':'_onSubmit',
					'click .SID-detailback':'back',
					'click .SID-BtnPlanDetail':'_onClickBtnPlanDetail',
					'click #ID-Detail-BtnDetail':'_onClickBtnDetail'
				},
				initialize: function() {
					this.preRender();
					this.childView = {};
					this.parentObj;
					this.roomId="";
					this.userInfo={userName:"",userPhone:""};
					this.toast;//弹出框
					//DOM
					this.favorite;
					this.moreDepute;
					this.moreHandler;
					this.chooseTime;
					/*this.times;*/
					/*this.submitPop;*/
					this.midDialog;
					this.elMeetname;
					this.elMeetdate;
					this.formSubmitPop;
					/*this.meetName;*/
					//Data
					this.clickCount=0;
					this.selectTimes=[];
					this.selectDate=new Date();
					this.reservedDatesJson;
					this.activeDate=new Date();
					this.startTime="";
					this.endTime="";
					/*this.popConfirm;*/
					this.changeDate="";
					//Plugin
					this.timepart;
					this.detailCalendar;
					this.needApprove = "0";
					
				},
				render: function(roomId,parentObj,activeDate,startTime,endTime) {
					this.roomId = roomId;
					this.parentObj = parentObj;
					this.startTime = startTime;
					if (endTime == "22:30") {
						this.endTime = "22:00"
					} else {
						this.endTime = endTime;
					}
					if (activeDate)
						this.activeDate = activeDate;
					this._setUpContent();
					return false;
				},
				refresh: function() {
					this.$el.empty();
					this.clickCount=0;
					this.selectTimes=[];
					this.render(this.roomId,this.parentObj,this.activeDate,"","");
				},
				destroy: function() {
					this.undelegateEvents();
					this.unbind();
					$(".SID-detail-snippet article").scrollTop(0);
					this.$el.empty();
				},
				//Method
				_onClickShowImg:function(e){
					var target=e.target;
					if(target.getAttribute("data-path")){
						var path = $(e.currentTarget).attr("data-path");
						if (path==""){
							return;
						}
						mplus.previewImage({
	    				    current: '', // 当前显示图片的http链接
	    				    urls: [path] // 需要预览的图片http链接列表
	    				});
					}
				},
				_getReservedDetail:function(activeDate){
					var self = this;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					// 获取日历上选择的日期  格式必须是2016-11-15
					var reservedDate = activeDate;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&v=2.0&method=mapps.meetingroom.reserved.detail&roomId="+self.roomId+"&reservedDate="+reservedDate,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								var listHtml = _.template(
									dayListTemplate, {
									'imgPath': self.constants.IMAGEPATH,
									'data' : data
								});
								if(data.reservedDetailList.length===0){
									$("#ID-DivDayList").css("display","none");
								}else{
									$("#ID-DivDayList").css("display","block");
									$("#ID-DivDayList").html(listHtml);
								}
								data.date=activeDate;
								self.dayListData=data;
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
				_resetCalendar:function(){
					this.detailCalendar.reset();
				},
				//Events Handler
				back:function(e){
					this.parentObj.detailData=null;
                	this.destroy();
        			$(".SID-rank-one").hide();
                	$(".SID-index-snippet").show();
                    history.go(-1);
					return false;
				},
				_onClickFavorite:function(e){
					var self = this;
					var type = $(e.target).attr("data-collection")=="1"? 0 : 1;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					var url = servicePath;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&method=mapps.meetingroom.room.favorite&roomId="+self.roomId
						      +"&operationType=" + type,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								if (type == 0) {
									self.toast.setText("取消收藏");
									self.favorite.innerHTML="收藏";
								} else {
									self.toast.setText("已收藏");
									self.favorite.innerHTML="已收藏";
									$(e.target).attr("data-collection")
								}
								$(e.target).attr("data-collection",type);
								self.toast.show();
								self.parentObj.views.indexView.refresh(false);
							} else {
								self.toast.setText(data.message);
								self.toast.show();
							}
						},
						error : function(){
							$(".SID-loading").hide();
							self.toast.setText("操作失败");
							self.toast.show();
						}
					});
				},
				_onClickMoreDepute:function(e){
					$(".icon",e.target).toggleClass("icon-arrowup");
					$(".icon",e.target).toggleClass("icon-arrowdown");
					this.moreHandler.classList.toggle("hide")
				},
				_onSubmit:function(e){
					var timerange=this.timepart.getActiveTimes();
					var startTime=timerange.startTimeString;
					var endTime=timerange.endTimeString;
					if(!startTime || endTime==""){
						this.toast.setText("未选择预定时间");
						this.toast.show();
						return;
					}
					var dateStr=this.detailCalendar.container.querySelector(".calendar-title").innerHTML.split("&nbsp;")[0];
					
					//获得值
					var meetRoomName=document.getElementById("forMeetname").innerHTML;
					//生成planData对象
					this.parentObj.planData={};
					this.parentObj.planData.roomName=meetRoomName;
					this.parentObj.planData.name=this.userInfo.userName;
					this.parentObj.planData.phoneNum=this.userInfo.userPhone;
					this.parentObj.planData.date=dateStr;
					this.parentObj.planData.starttime=startTime;
					this.parentObj.planData.endtime=endTime;
					this.parentObj.planData.needApprove=this.needApprove;
					this.parentObj.roomId=this.roomId;
					location.href="#addPlanPage";
					/*this.elMeetname.innerHTML=meetRoomName;
					this.elMeetdate.innerHTML=meetRoomDate;
					this.midDialog.show();*/
				},
				//初始化
				_onLoad:function(){
					var self=this;
					this.header=self.$el.find("header")[0];
					this.toast=new Prompt("2",{
						"parent":self.$el[0]
					});
					//是否可选弹出框
		            this.dialog=new Dialog("#ID-MeetDetatil-Pop",
		                {
		                    overflowContainer:self.$el[0],
		                    position:"top",
		                    animation:"slideDown",
		                    css:{width:"100%",paddingTop:"44px",position:"absolute"},
		                    onHid:function(e){
		                        self.header.style.zIndex="2";
		                    }
		                }
		            );
		            this.popOk=this.dialog.container.querySelector("#ID-MeetDetatil-PopOk");
		            this.popCancel=this.dialog.container.querySelector("#ID-MeetDetatil-PopCancel");
		            this.popContent=this.dialog.container.querySelector("#ID-MeetDetatil-PopContent");
					//日历
					this.timepart=new Timepart("#ID-Timepart",{
						onConflictOver:function(e){
	                        if(e.target.status=="active"){
	                            self.toast.setText("不能跨选禁用时间段");
	                            self.toast.show();
	                            //移除所有选中效果
	                            e.removeAllActive();
	                        }
	                    },
	                    onConflictContain:function(e){
	                        /*if(e.target.status=="disabled"){
	                            self.toast.setText("时间段冲突");
	                            self.toast.show();
	                        }*/
	                    },
	                    onClickActive:function(e){
	                        console.log(e.target);
	                        e.removeAllActive();
	                    },
	                    onClickDisabled:function(e){
	                    	self.header.style.zIndex="11";
	                    	if(e.target.classList.contains("SID-DisabledInfo")){
		                        self.popOk.style.display="-webkit-box";
		                        self.popContent.style.display="none";
		                        self.popCancel.style.display="none";
		                        
		                        self.dialog.show();
		                    }else if(e.target.classList.contains("SID-DisabledBefore")){
		                        self.popOk.style.display="none";
		                        self.popContent.style.display="none";
		                        self.popCancel.style.display="-webkit-box";

		                        self.dialog.show();
		                    }
	                        /*var data=JSON.parse(e.target.getAttribute("data-progress"));
	                        if(!data){

	                        	return;
	                        }
	                        self.toast.setText("开始时间:"+data.startTime+",结束时间:"+data.endTime);
	                        self.toast.show();*/
	                    },
	                    onClickValid:function(s){//点击有效区域
	                        if(s.clickCount==1){//如果点击了一次
	                            self.part1=s.target;
	                            self.part1.classList.add(s.params.activeClass);
	                        }else if(s.clickCount==2){//如果点击了两次
	                            self.part2=s.target;
	                            //选中
	                            var times=s.getTimesByParts(self.part1,self.part2);
	                            s.activeTimes(times.startTime,times.endTime);
	                        }else if(s.clickCount==3){//如果点击了三次
	                            s.removeAllActive();
	                            self.activeStartTime=null;
	                            self.activeEndTime=null;
	                        }
	                    },
					});
					var activeDate=new Date();
					if(this.parentObj.detailData){
						activeDate=new Date(this.parentObj.detailData.date.replace(/-/g,"/"));
					}
					this.detailCalendar=new Calendar("#ID-DetailCalendar",{
						disableBeforeDate:new Date(),
						isShowDayNum:true,
						prevHTML:'<i class="icon-arrowleft"></i>',
            			nextHTML:'<i class="icon-arrowright"></i>',
						isYTouch:false,
						activeDate:activeDate,
						viewType:"week",
						onChange:function(e){
							self.timepart.removeAllActive();
							self._setTimeDisable(e);
							//加载列表
							var activeDate=e.activeDate.year()+"-"+e.activeDate.month()+"-"+e.activeDate.date();
							self._getReservedDetail(activeDate);
						}
					});
					if(this.parentObj.detailData){
						this.timepart.activeTimes(this.parentObj.detailData.begin,this.parentObj.detailData.end);
					}
					this._initDetail();
				},
				_bindClick:function(){
					var self = this;
					self.$el.find(".SID-showimg").bind("click", function(e){
						self._onClickShowImg(e);
					});
				},
				_onClickBtnPlanDetail:function(e){
					var self=this;
					if(!self.dayListData)self.dayListData={};
					var planId=e.target.id;

					var data={};
					for(var i=0,rowData;rowData=self.dayListData.reservedDetailList[i++];){
						if(rowData['reservedId']==planId){
							data=rowData;
						}
					}
					var year=this.detailCalendar.activeDate.year();
					var month=this.detailCalendar.activeDate.month();
					var day=this.detailCalendar.activeDate.date();
					data.date=year+"-"+month+"-"+day;
					data.roomName=document.getElementById("forMeetname").innerHTML;

					self.parentObj.planDetailData=data;
					location.href="#planDetailPage";
				},
				_onClickBtnDetail:function(){
					var self=this;
					self.header.style.zIndex="11";
					self.popOk.style.display="none";
                    self.popContent.style.display="block";
                    self.popCancel.style.display="none";
					self.dialog.show();
				},
				//加载数据
				_initDetail: function() {
					if(this.startTime != "" && this.endTime != ""){
						this.timepart.activeTimes(this.startTime,this.endTime);
					}
					var self = this;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&v=2.0&method=mapps.meetingroom.room.detail&roomId="+self.roomId,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								var html = _.template(
									contentTemplate, {
									'imgPath': self.constants.IMAGEPATH,
									'roomDetailData' : data
								});
								self.$el.find("#ID-Title-RoomName").text(data.roomName);
								self.needApprove = data.needApprove;
								self.userInfo.userName = data.userName;
								self.userInfo.userPhone = data.userPhone;
								self.$el.find("#ID-MeetDetatil-PopContent").html(html);

								self.favorite=document.querySelector(".SID-Favorite");
								self.moreDepute=document.querySelector(".SID-MoreDepute");
								if(data.collection==1){
									self.favorite.innerHTML="已收藏";
								} else {
									self.favorite.innerHTML="收藏";
								}
								self.favorite.setAttribute("data-collection",data.collection);
								self.favorite.style.display="inline-block";
								// 详情高度设置
								self.moreHandler=document.querySelector(".SID-MoreHandler");
								self.reservedDatesJson = data.reservedDates;
								// 已预约出去的时间初始化
								self._setTimeDisable();
								self._setTimeVal();
								$(".SID-detail-snippet").show();
								self.detailCalendar.updateContainerSize();
								self._bindClick();
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
				_setTimeVal:function(){
					var sTimeObj = this.$el.find(".SID-ChooseTime").find("label[class=active]").first();
					var eTimeObj = this.$el.find(".SID-ChooseTime").find("label[class=active]").last();
					if (sTimeObj.length > 0 && eTimeObj.length > 0) {
						this.clickCount=2;
						this.selectTimes[0] = sTimeObj.attr("data-num");
						this.selectTimes[1] = eTimeObj.attr("data-num");
					}

				},
				_setTimeDisable:function(arg){
					var self=this;
					var activeTimes=self.timepart.getActiveTimes();
					console.log(activeTimes);
					if(activeTimes.startTime && activeTimes.endTime){
						self.activeStartTime=activeTimes.startTime;
						self.activeEndTime=activeTimes.endTime;
					}else{
						self.activeStartTime=null;
						self.activeEndTime=null;
					}

					var reservedDate="";
					if(arg){
						reservedDate=arg.activeDate;
					}else{
						reservedDate=this.detailCalendar.activeDate;
					}

					var currentDate=new Date();
					this.timepart.removeAllActive();
					this.timepart.removeAllDisabled();

					/*if(reservedDate){
						reservedDate=reservedDate.format("yyyy-MM-dd");
					}*/
					if(reservedDate.compareDate(new Date())===0){
						this._setDisable();
					}
					for(var n in this.reservedDatesJson){
						var date=this.reservedDatesJson[n].reservedDate;
						if (date == reservedDate.format("yyyy-MM-dd")) {
							var times=this.reservedDatesJson[n].reservedTimes;
							for(var i=0,time;time=times[i++];){
								var startTime=time.starttime;
								var endTime=time.endtime;
								this.timepart.disableTimes(startTime,endTime,["SID-DisabledInfo"]);
							}
						}
					}
					if(self.activeStartTime && self.activeEndTime){
						var isConfilt=this.timepart.hasProgress(self.activeStartTime,self.activeEndTime);
						if(isConfilt){
							self.activeStartTime=null;
							self.activeEndTime=null;
						}else{
							this.timepart.activeTimes(self.activeStartTime,self.activeEndTime);
						}
					}
				},
				_setDisable:function(){
					/*var currentDate = new Date();
					currentDate.setMinuteCeil(30);
					var currentTimeStr=currentDate.hour()+":"+currentDate.minute();*/
					this.timepart.disableTimes(null,new Date().setMinuteCeil(30),["SID-DisabledBefore"]);
				},
				_setUpContent: function(data_t) {
					var self=this;
					var html = _.template(
						Template, {
							imgPath: self.constants.IMAGEPATH,
							data: data_t
						});
					this.$el.html(html);
					//初始化
					this._onLoad();
				}
			});

		return IndexRouterSnippentView;
	});
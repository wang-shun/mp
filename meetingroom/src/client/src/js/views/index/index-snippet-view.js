define(
	['jquery', 'views/communication-base-view','seedsui'
	, "views/detail/detail-snippet-view"
	, "views/manager/manager-snippet-view"
	, 'text!../../templates/index/indexTemplate.html', 'text!../../templates/index/indexListTemplate.html'],
	function($, CommunicationBaseView,seedsui,DetailSnippetView,ManagerSnippetView, Template, listTemplate) {
		var IndexRouterSnippentView = CommunicationBaseView
			.extend({
				events: {
					'click .SID-TabFilter': '_onClickTabFilter',
					'click .SID-DialogFilterBack':'_onClickDialogFilterBack',
					'click .SID-Time1':'_onClickTime1',
					'click .SID-Time2':'_onClickTime2',
					'click .SID-DeviceBlock':'_onClickDeviceBlock',
					'click .SID-FilterSubmit':'_onClickSubmit',
					'click .SID-FilterReset':'_onClickReset',
					'click .SID-order' : '_onClickOrder',
					'click .SID-back' : '_onClickBack',
					'click .SID-manager' : '_onClickToManager',
					'blur .SID-peopleNum' : '_onFocusNum'
				},
				initialize: function() {
					this.preRender();
					this.parentObj;
					//DOM
					this.indexPage;
					this.elNodata;
					this.header,this.tabFilter;
					this.dialogFilter;
					//Data
					this.devices=[];
					//数据校验DOM
					this.time1,this.time2;
					this.filterSubmit;//提交按钮
			        this.filterReset;//重置按钮
			        this.peopleNum;//人数
			        this.deviceBlock;//设备容器
			        this.dbs;//设备
			        this.toast;//弹出框
			        this.formFilter;//表单
			        //日历控件
			        this.activeDate;
			        // 分页
			        /*this.offset = 1;
			        this.limit = 10;*/
			        this.timestamp = 0;
			        this.childViews = {};
			        this.endflag = 0;
			        //下拉刷新
			        this.drag;
			        this.param="";
			        this.startTime="";
			        this.endTime="";
			        this.adminFlag=0;
			        this.peoplenumFlag=0;
				},
				_scroll : function(Event){
					return this;
				},
				_onFocusNum : function(e){
					var target=e.target;
					var val = $(target).val();
					var reg = /^\+?[1-9]\d{0,3}?$/;
					if (val != "" && !reg.test(this.peopleNum.value)) {
						this.peoplenumFlag=1;
						this.toast.setText("容量填写不符合规范");
	                    this.toast.show();
						$(target).val("");
					}else{
						this.peoplenumFlag=0;
					}
				},
				initBusinessViews : function(){
					$.each(this.childViews, function(index, view) {
						 view.destroy();
					});		
					this.childViews = {};
				},
				subscribeEvents : function() {
					var self = this;
					this.eventHub.subscribeEvent('REFRESH_INDEX_TABLE', function(msg) {
						self.refresh(false);
					});
				},
				render: function(parentObj) {
					this.parentObj = parentObj;
					this._setUpContent();
					return this;
				},
				setParam:function(){
					var reservedDate="";
					if(this.calendar.activeDate){
						this.activeDate=this.calendar.activeDate.format("yyyy-MM-dd");
						reservedDate = this.activeDate;
					}
					var reservedStartTime = this.time1.value;
					var reservedEndTime = this.time2.value;
					var capacity = this.peopleNum.value;
					var param = "";
		            if (reservedDate != "") {
			            if (reservedStartTime != "" && reservedEndTime != "") {
			            	param += "&reservedStartTime=" + reservedDate + " " + reservedStartTime + ":00";
			            	param += "&reservedEndTime=" + reservedDate + " " + reservedEndTime + ":00";

					        this.startTime=reservedStartTime;
					        this.endTime=reservedEndTime;
			            } else {
			            	param += "&reservedDate=" + reservedDate;
			            }
		            }
		            if (capacity != "") {
		            	param += "&capacity=" + capacity;
		            }
		            var deviceList = this.$el.find(".SID-DeviceBlock").find("div");
		            $.each(deviceList, function (n, key) {
						if ($(key).hasClass("active")) {
							param += "&" + $(key).attr("key") + "=1";
						}
					});
		            this.param = param;
				},
				refresh: function(isNext) {
					var self = this;
					//是否为加载下一页  true 是
					if(!isNext){
						this.timestamp = 0;
						self.pagination.current=1;
						self.$el.find("article").scrollTop(0);
					}
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					var url = servicePath;
		            var param = this.param;
		            param += "&order=1";
		            //如果是最后一页
					if(isNext && self.pagination.current>self.pagination.max){
						if(self.pagination.current>1)self.pagination.current--;
                        //drag标识底部状态
                        self.drag.bottomNoData();
                        return;
                    }
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&method=mapps.meetingroom.room.query&offset="+self.pagination.current
						      +"&limit=" + self.pagination.limit
							  +"&timestamp=" + self.timestamp + param,
						success : function(data) {
							self.pagination.total=data.total;
							self.pagination.max=Math.ceil(self.pagination.total/self.pagination.limit);
							if (data.code == "1") {
								self.timestamp = data.timestamp;
								/*self.endflag = data.endflag;
								console.log("endFlag无数据:"+self.endflag);*/
								//此处注入数据
								var html = _.template(
									listTemplate, {
									'imgPath': self.constants.IMAGEPATH,
									'room' : data.room
								});

								//头部刷新或者下一页
		                        if(!isNext){
		                            self.$el.find(".SID-meetList").html(html);
		                            //drag标识头部状态
		                            self.drag.topComplete();
		                        }else{
		                            self.$el.find(".SID-meetList").append(html);
		                            //drag标识底部状态
		                            self.drag.bottomComplete();
		                        }
		                        //如果没有超过一页
								if(self.pagination.total <= self.pagination.limit){
		                            //drag标识头部状态
		                            self.drag.topComplete();
		                            //drag标识底部状态
		                            self.drag.bottomNoData();
		                        }

								//self.$el.find(".SID-meetList").append(html);
								if (data.reservedFlag == 1) {
									self.$el.find(".SID-reddot").show();
								} else {
									self.$el.find(".SID-reddot").hide();
								}
								if (data.adminFlag == 1) {
									self.adminFlag = 1;
								} else {
									self.adminFlag = 0;
								}
								if(data.room.length<=0){
									self._showNoData();
								}else{
									self._hideNoData();
								}
								self.imglazy.update();
							} else {
								if (data.code=='300017'){
							        self.startTime="";
							        self.endTime="";
								}
								self.toast.setText(data.message);
								self.toast.show();
							}
						},
						error : function(){
							if(isNext)self.pagination.current--;
							self.toast.setText("数据获取失败");
							self.drag.topComplete();
							self.drag.bottomNoData();
						},
						complete:function(){
							$(".SID-loading").hide();
						}
					});
				},
				destroy: function() {
					this.undelegateEvents();
					this.unbind();
					this.$el.empty();
				},
				_showNoData:function(){
					this.elNodata.style.display="-webkit-box";
				},
				_hideNoData:function(){
					this.elNodata.style.display="none";
				},
				_onClickToManager:function(){
					this.parentObj.app.router.navigate("#managerPage", {trigger: true});
				},
				_onClickBack:function(){
					mplus.closeWindow();
				},
				_onClickOrder:function(e){
					var target=e.target;
					$(target).parent().find(".SID-order").removeClass("active");
					target.classList.add("active");
					this.refresh(false);
				},
				//Method
				_initCalendar:function(){
					this.calendar=new Calendar("#calender_select_container",{
						dayHeight:"40",
						prevHTML:'<i class="icon-arrowleft"></i>',
            			nextHTML:'<i class="icon-arrowright"></i>',
						isYTouch:false,
						viewType:"week",
						disableBeforeDate:new Date()
					});
				},
				_resetCalendar:function(){
					this.calendar.reset();
				},
				//Events Handler
				_onClickTabFilter:function(e){
		            this.dialogFilter.show();
				},
				_onClickDialogFilterBack:function(e){
					this.dialogFilter.hide();

				},
				_onClickTime1:function(e){
					if(!this.calendar.activeDate){
						this.toast.setText("未选择日期");
	                    this.toast.show();
	                    return;
					}
					var input=e.target;
		            this.spTime1.setOnClickDone(function(e){
		                input.value=e.activeText;
		                input.classList.remove("active-error");
		                input.classList.add("active");
		                e.scrollpicker.hide();
		            });
		            this.spTime1.show();
				},
				_onClickTime2:function(e){
					if(!this.calendar.activeDate){
						this.toast.setText("未选择日期");
	                    this.toast.show();
	                    return;
					}
					var input=e.target;
		            this.spTime2.setOnClickDone(function(e){
		                input.value=e.activeText;
		                input.classList.remove("active-error");
		                input.classList.add("active");
		                e.scrollpicker.hide();
		            });
		            this.spTime2.show();
				},
				_onClickDeviceBlock:function(e){
					var target=e.target;
		            if(target.tagName=="DIV"){
		                if(!target.classList.contains("active")){
		                    target.classList.add("active");
		                }else{
		                    target.classList.remove("active");
		                }
		            }
				},
				_onClickSubmit:function(e){
					if(this.peoplenumFlag==1){
						this.peoplenumFlag=0;
						return;
					} 
					var date=new Date();
					if(this.time1.value.length>0 && this.time2.value.length==0){
						this.toast.setText("未选择结束时间");
	                    this.toast.show();
	                    return;
					}
					if(this.time1.value.length==0 && this.time2.value.length>0){
						this.toast.setText("未选择开始时间");
	                    this.toast.show();
	                    return;
					}
					if(this.time1.value.length>0 || this.time2.value.length>0){
						var sttime = this.time1.value;
						var edtime = this.time2.value;
						var sthour = sttime.split(":")[0]*1;
						var stmin = sttime.split(":")[1]*1;
						var edhour = edtime.split(":")[0]*1;
						var edmin = edtime.split(":")[1]*1;
						if(sthour == edhour){
							if(stmin > edmin){
								this.toast.setText("结束时间早于开始时间");
			                    this.toast.show();
			                    return;
							}
						}else if(sthour > edhour){
							this.toast.setText("结束时间早于开始时间");
		                    this.toast.show();
		                    return;
						}
						var compareResult=date.compareTime(this.time2.value,this.time1.value);
						if(compareResult==0)compareResult=false;
		                if(!compareResult || this.time1.value.length<=0 || this.time2.value.length<=0){
		                    if(!this.time1.classList.contains("active-error")){
		                        this.time1.classList.remove("active");
		                        this.time1.classList.add("active-error");
		                    }
		                    if(!this.time2.classList.contains("active-error")){
		                        this.time2.classList.remove("active");
		                        this.time2.classList.add("active-error");
		                    }
		                    this.toast.setText("时间选择错误");
		                    this.toast.show();
		                    return;
		                }
		                //提交成功
		                this.time1.classList.remove("active");
		                this.time1.classList.remove("active-error");
		                this.time2.classList.remove("active");
		                this.time2.classList.remove("active-error");
		                this.time1.classList.add("active");
		                this.time2.classList.add("active");
		            }
					if (this.peopleNum.value!="") {
						var reg = /^\+?[1-9]\d{0,3}?$/;
						if (!reg.test(this.peopleNum.value)) {
							this.toast.setText("请输入正确的会场容量");
		                    this.toast.show();
		                    return;
						}
					}
					this.dialogFilter.hide();
		            this.setParam();
		            this.refresh(false);
				},
				_onClickReset:function(e){
					//初始化时间
		            this.time1.value="";
		            this.time1.classList.remove("active");
		            this.time1.classList.remove("active-error");
		            this.time2.value="";
		            this.time2.classList.remove("active");
		            this.time2.classList.remove("active-error");
		            //容量
		            this.peopleNum.value="";
		            //设备
		            for(var i=0,db;db=this.dbs[i++];){
		                db.classList.remove("active");
		            }
		            this.devices=[];
		            //清空日历
		            this._resetCalendar();
		            this.activeDate="";

			        this.param="";
			        this.startTime="";
			        this.endTime="";
				},
				//初始化
				_onLoad:function(){
					this.article=this.$el.find("article");
					this.indexPage=document.querySelector(".SID-index-snippet");
					this.header=document.getElementsByTagName("header")[0];
			        this.tabFilter=document.querySelector(".SID-TabFilter");
			        this.elNodata=document.querySelector(".SID-Nodata");
			        var self=this;
			        //数据过滤弹出框
			        this.dialogFilter=new Dialog("#dialogFilter",{
		                overflowContainer:self.article[0],
		                position:"right",
		                animation:"slideLeft",
		                css:{width:"85%",height:"100%"},
		                onClickMask:function(e){
		                    self.tabFilter.classList.remove("active");
		                },
		                onShowed:function(){
		                	self.indexPage.style.overflow="hidden";
		                },
		                onHid:function(e){
		                    self.indexPage.style.overflow="auto";
		                }
		            });
		            this.hoursData=(function(){
		            	var arr=[];
		            	for(var i=7;i<=22;i++){
		            		arr.push({
		            			key:i<10?"0"+i:i,
		            			value:i<10?"0"+i+"时":i+"时"
		            		});
		            	}
		            	return arr;
		            })();
			        //时间弹出框
			        this.spTime1=new SpDate({
			        	"parent":self.$el[0],
			            "viewType":"time",
			            "hourClass":"text-center",
			            "hoursData":this.hoursData,
			            "minuteClass":"text-center",
			            "minutesData":[{"key":"00","value":"0分"},{"key":"30","value":"30分"}]
			        });
			        this.spTime2=new SpDate({
			        	"parent":self.$el[0],
			            "viewType":"time",
			            "hourClass":"text-center",
			            "hoursData":this.hoursData,
			            "minuteClass":"text-center",
			            "minutesData":[{"key":"00","value":"0分"},{"key":"30","value":"30分"}]
			        });
			        //初始化日历
			        this._initCalendar();
			        var isHasScroll=false;
			        var indexSnippetContainer=document.querySelector(".SID-index-snippet");
		            this.pagination={
	                    hasData:null,
	                    current:1,//当前页数
	                    max:0,//总页数
	                    limit:10,//每页条数
	                    total:0//总条数
	                };
		            this.drag=DfPull({
	                    overflowContainer:this.article[0],
	                    parent:this.article[0],
	                    onTopRefresh:function(e){
	                        console.log("头部刷新");
	                        self.pagination.current=1;
	                        self.refresh(false);
	                    },
	                    onTopComplete:function(e){
	                        console.log("头部完成");
	                    },
	                    onBottomRefresh:function(e){
	                        console.log("底部刷新");
	                        self.pagination.current++;
	                        self.refresh(true);
	                    },
	                    onBottomComplete:function(e){
	                        console.log("底部完成");
	                    },
	                    onBottomNoData:function(e){
	                        console.log("底部无数据了");
	                    }
	                });


			        //过滤弹出框表单DOM
			        this.time1=document.querySelector(".SID-Time1");
			        this.time2=document.querySelector(".SID-Time2");
					this.filterSubmit=document.querySelector(".SID-FilterSubmit");
			        this.filterReset=document.querySelector(".SID-FilterReset");
			        this.peopleNum=document.getElementById("peopleNum");
			        this.deviceBlock=document.querySelector(".SID-DeviceBlock");
			        this.dbs=this.deviceBlock.getElementsByTagName("div");
			        this.toast=new Prompt("4",{"parent":self.$el[0]});
			        this.formFilter=new Form("#formFilter",{
			        	"toastParent":self.$el[0]
			        });
			        
			        var indexPage=document.querySelector(".SID-index-snippet");
			        
	            	this.imglazy=new ImgLazy(document.querySelector(".SID-index-snippet > article"));
			        $(indexPage).bind("click", function(e){
			        //EventUtil.addHandler(indexPage,"click",function(e){
						var target=e.target;
						if(target.classList.contains("SID-roomdetail")){
							var roomId=target.getAttribute("data-roomid");
							var reservedDate = new Date();
							if(self.calendar.activeDate){
								reservedDate = self.calendar.activeDate;
							}
							var startTime = self.startTime;
							var endTime = self.endTime;
							if (startTime == "" || endTime == "") {
								location.href="#detailPage/"+roomId+"/"+reservedDate+"/-1/-1";
//								self.parentObj.app.router.navigate("#detailPage/"+roomId+"/"+reservedDate, {trigger: true});
							} else {
								location.href="#detailPage/"+roomId+"/"+reservedDate+"/"+startTime+"/"+endTime;
//								self.parentObj.app.router.navigate("#detailPage/"+roomId+"/"+reservedDate+"/"+startTime+"/"+endTime, {trigger: true});
							}
						}
						return false;
					})
			        //this.refresh(false);
				},
				//加载数据
				_setUpContent: function() { 
					var self = this;
					var html = _.template(
						Template, {
							'imgPath': self.constants.IMAGEPATH
						});
					this.$el.append(html);
					//初始化
					this._onLoad();
				}
			});

		return IndexRouterSnippentView;
	});
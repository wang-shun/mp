define(
	['jquery', 'views/communication-base-view','seedsui', 'text!../../templates/manager/managerTemplate.html', 'text!../../templates/manager/managerListTemplate.html', 'text!../../templates/manager/managerApprovalTemplate.html'],
	function($, CommunicationBaseView, seedsui, Template, listTemplate , approvalTemplate) {
		var ManagerSnippentView = CommunicationBaseView
			.extend({
				events: {
					'click .SID-BtnFinsh':'_onClickBtnFinsh',
					'click .SID-BtnCancel':'_onClickBtnCancel',
					'click .SID-BtnDel':'_onClickBtnDel',
					'click .SID-managerBack':'back',
					'click .SID-BtnPlanDetail':'_onClickBtnPlanDetail',
					'click .SID-Tabbar':'_onClickTab'
				},
				initialize: function() {
					this.preRender();
					this.childView = {};
					this.parentObj;
					this.toast;//弹出框
					//Data
					this.ajaxUrl;
					this.ajaxData;
					//DOM
					this.popConfirm;
					this.elNodata;
					this.tabReserve,this.tabApprove;
			        // 分页
			        this.timestamp = 0;
			        this.childViews = {};
			        this.status="";
			        this.reservedId="";
			        this.endflag = 0;
			        //下拉刷新
			        this.drag;
			        this.temp = listTemplate;
			        this.reserveRoomList=[];
			        this.adminFlag = 0;
				},
				render: function(parentObj,adminFlag) {
					this.parentObj = parentObj;
					this.adminFlag = adminFlag;
					this._setUpContent();
					return this;
				},
				refresh: function(isNext) {
					this._loadManager(isNext);
				},
				_loadManager:function(isNext){
					this.parentObj.managerTab=0;
					var self = this;
					//是否为加载下一页  true 是
					if(!isNext){
						this.timestamp = 0;
						this.reserveRoomList=[];
					}

					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					var param = "&status=a,r,1,2,4,3";
					//如果是最后一页
					if(isNext && self.pagination[0].current>self.pagination[0].max){
						self.pagination[0].current--;
                        //drag标识底部状态
                        self.drag[0].bottomNoData();
                        return;
                    }
					$.ajax({
						type : "POST",
						url : url,
						async : false,
						data : ropParam + "&v=2.0&method=mapps.meetingroom.reserved.query&offset="+self.pagination[0].current
						      +"&limit=" + self.pagination[0].limit
							  +"&timestamp=" + self.timestamp + param,
						success : function(data) {
							self.pagination[0].total=data.total;
							self.pagination[0].max=Math.ceil(self.pagination[0].total/self.pagination[0].limit);
							self.pagination[0].hasData=true;

							console.log(self.pagination[0]);

							if (data.code == "1") {
								self.timestamp = data.timestamp;
								var html = _.template(
									listTemplate, {
									'imgPath': self.constants.IMAGEPATH,
									'listData' : data.reserved
								});

								//头部刷新或者下一页
		                        if(!isNext){
		                        	self.$el.find(".SID-managerList").html(html);
		                            //drag标识头部状态
		                            self.drag[0].topComplete();
		                        }else{
		                            self.$el.find(".SID-managerList").append(html);
		                            //drag标识底部状态
		                            self.drag[0].bottomComplete();
		                        }
		                        //如果没有超过一页
								if(self.pagination[0].total <= self.pagination[0].limit){
									//drag标识头部状态
		                            self.drag[0].topComplete();
									//drag标识底部状态
			                        self.drag[0].bottomNoData();
								}
								//保存数据
								self.reserveRoomList = self.reserveRoomList.concat(data.reserved);
		                        
							} else {
								self.toast.setText(data.message);
								self.toast.show();
							}
							if(data.reserved.length<=0){
								self._showNoData();
							}else{
								self._hideNoData();
							}
						},
						error : function(){
							if(isNext)self.pagination[0].current--;
							self.toast.setText("数据获取失败");
							self.drag[0].topComplete();
							self.drag[0].bottomNoData();
						},
						complete:function(){
							$(".SID-loading").hide();
						}
					});
				},
				_loadApproval:function(isNext){
					this.parentObj.managerTab=1;
					var self = this;
					//是否为加载下一页  true 是
					if(!isNext){
						this.timestamp = 0;
						this.approveRoomList=[];
					}

					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					var param = "&status=0,1";
					//如果是最后一页
					if(isNext && self.pagination[1].current>self.pagination[1].max){
						self.pagination[1].current--;
                        //drag标识底部状态
                        self.drag[1].bottomNoData();
                        return;
                    }
					$.ajax({
						type : "POST",
						url : url,
						async : false,
						data : ropParam + "&v=1.0&method=mapps.meetingroom.reserved.queryapprove&offset="+self.pagination[1].current
						      +"&limit=" + self.pagination[1].limit
							  +"&timestamp=" + self.timestamp + param,
						success : function(data) {
							console.log(data);
							self.pagination[1].total=data.total;
							self.pagination[1].max=Math.ceil(self.pagination[1].total/self.pagination[1].limit);
							self.pagination[1].hasData=true;
							
							if (data.code == "1") {
								self.timestamp = data.timestamp;
								var html = _.template(
									approvalTemplate, {
									'imgPath': self.constants.IMAGEPATH,
									'listData' : data.reserved
								});
								
								//头部刷新或者下一页
		                        if(!isNext){
		                        	self.$el.find(".SID-DivApproval").html(html);
		                            //drag标识头部状态
		                            self.drag[1].topComplete();
		                        }else{
		                            self.$el.find(".SID-DivApproval").append(html);
		                            //drag标识底部状态
		                            self.drag[1].bottomComplete();
		                        }

		                        //如果没有超过一页
								if(self.pagination[1].total <= self.pagination[1].limit){
									//drag标识头部状态
		                            self.drag[1].topComplete();
									//drag标识底部状态
			                        self.drag[1].bottomNoData();
								}
								//保存数据
		                        self.approveRoomList = self.approveRoomList.concat(data.reserved);
							} else {
								self.toast.setText(data.message);
								self.toast.show();
							}
							if(data.reserved.length<=0){
								self._showNoData();
							}else{
								self._hideNoData();
							}
						},
						error : function(){
							if(isNext)self.pagination[1].current--;
							self.toast.setText("数据获取失败");
							self.drag[1].topComplete();
							self.drag[1].bottomNoData();
						},
						complete:function(){
							$(".SID-loading").hide();
						}
					});
				},
				destroy: function() {
					$(".SID-manager-snippet article").scrollTop(0);
					$(".SID-rank-one").hide();
					$(".SID-index-snippet").show();
					this.undelegateEvents();
					this.unbind();
					this.$el.empty();
				},
				//Method
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
				_showNoData:function(){
					this.elNodata.style.display="-webkit-box";
				},
				_hideNoData:function(){
					this.elNodata.style.display="none";
				},
				//Events Handler
				changeStatus:function(){
					var self = this;
					if (this.reservedId == "")
						return;
					var type = this.status;
					var text = "";
					if (type == "4") {
						type = "3";
						text = "删除会议室预定";
					} else if (type == "1") {
						text = "取消会议室预定";
					} else if (type == "2") {
						text = "结束会议室预定";
					} else if (type == "3") {
						text = "删除会议室预定";
					}
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&v=2.0&method=mapps.meetingroom.reserved.delete&reservedId="+self.reservedId
						      +"&operationType=" + type,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								self.toast.setText(text+"成功");
								self.toast.show();
							} else {
								self.toast.setText(text+"失败");
								self.toast.show();
							}
							self.popConfirm.hide();
							self.refresh(false);
						},
						error : function(){
							$(".SID-loading").hide();
							self.toast.setText(text+"失败");
							self.toast.show();
						}
					});
				},
				back:function(e){
					this.parentObj.managerTab=0;
                	this.destroy();
                    history.go(-1);
					return false;
				},
				_onClickBtnFinsh:function(e){
					var self = this;
					this.status = $(e.target).attr("data-status");
					this.reservedId = $(e.target).attr("data-reservedId");
					//设置提示语
	                this.popConfirm.setText("结束此会议室使用？");
	                //设置执行函数
	                this.popConfirm.setOnClickOk(function(e){
	                	self.changeStatus();
	                })
	                //显示弹出框
	                this.popConfirm.show();
	                e.stopPropagation();
				},
				_onClickBtnCancel:function(e){
					var self = this;
					this.status = $(e.target).attr("data-status");
					this.reservedId = $(e.target).attr("data-reservedId");
					//设置提示语
	                this.popConfirm.setText("取消此会议室预定？");
	                //设置执行函数
	                this.popConfirm.setOnClickOk(function(e){
	                	self.changeStatus();
	                })
	                //显示弹出框
	                this.popConfirm.show();
	                e.stopPropagation();
				},
				_onClickBtnDel:function(e){
					var self = this;
					this.status = $(e.target).attr("data-status");
					this.reservedId = $(e.target).attr("data-reservedId");
					//设置提示语
	                this.popConfirm.setText("删除此会议室的预定？");
	                //设置执行函数
	                this.popConfirm.setOnClickOk(function(e){
	                	self.changeStatus();
	                })
	                //显示弹出框
	                this.popConfirm.show();
	                e.stopPropagation();
				},
				getParam:function(){
					var managerStauts="a,r,1,2,4,3";//待审批,未批准,准备中,使用中,已取消,已结束
					var approvalStauts="0,1";//待审批,已审批
		            return managerStauts;
				},
				_onClickBtnPlanDetail:function(e){
					var target=e.currentTarget;
					var reservedId=target.id;
					var data={};

					var activeTab=$(".SID-Tabbar.active");
					var activeArr=[];
					if(activeTab.attr("id")=="ID-TabReserve"){
						this.parentObj.managerTab=0;
						activeArr=this.reserveRoomList;
					}else{
						this.parentObj.managerTab=1;
						activeArr=this.approveRoomList;
					}
					for(var i=0,room;room=activeArr[i++];){
						if (room.reservedId==reservedId)
							data=room;
					}
					this.parentObj.planDetailData = {};
					//this.parentObj.planDetailData=this.parentObj.planDetailData?this.parentObj.planDetailData:{};
					this.parentObj.planDetailData.roomName=data.roomName;
					this.parentObj.planDetailData.reservedUserName=data.displayName;
					this.parentObj.planDetailData.phoneNum=data.phone;
					this.parentObj.planDetailData.date=data.reservedDate;
					this.parentObj.planDetailData.starttime=data.reservedStartTime;
					this.parentObj.planDetailData.endtime=data.reservedEndTime;
					this.parentObj.planDetailData.meetingName=data.meetingName;
					this.parentObj.planDetailData.reservedRemark=data.reservedRemark;
					this.parentObj.planDetailData.reservedId=data.reservedId;
					this.parentObj.planDetailData.approved=data.approved;
					this.parentObj.planDetailData.approveResult=data.approveResult;
					this.parentObj.planDetailData.participantsNum=data.participantsNum;
					if(target.getAttribute("data-isApprove") && target.getAttribute("data-isApprove")=="true"){
						this.parentObj.planDetailData.isApprove="1";
					} else if(target.getAttribute("data-isApprove") && target.getAttribute("data-isApprove")=="false"){
						this.parentObj.planDetailData.isApprove="2";
					}
					this.destroy();
					location.href="#planDetailPage";
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
				//初始化
				_onLoad:function(){
					var self=this;
					this.article=this.$el.find("article");
					this.elNodata=document.querySelector(".SID-ManagerNodata");
					this.tabReserve=document.getElementById("ID-TabReserve");
					this.tabApprove=document.getElementById("ID-TabApprove");
					//滑动页面
					this.sliderContainers=document.querySelectorAll("#ID-Pages > .slider-wrapper > .slider-slide");
	                this.sliderContainer=document.getElementById("ID-Pages");
	                this.sliderContainer.style.height=(window.innerHeight-106)+"px";

	                this.tabbarContainer=document.getElementById("ID-Tabbar");
	                this.tabs=this.tabbarContainer.querySelectorAll(".SID-Tabbar");

					this.slider=new Slider("#ID-Pages",{
	                    onSlideChangeEnd:function(e){
	                        self._tabActive(e.activeIndex);
	                        if(!self.pagination[e.activeIndex].hasData){
								if(e.activeIndex==0){
									self._loadManager();
								}else if(e.activeIndex==1){
									self._loadApproval();
								}
							}
	                    }
	                });
					var paddingTop = "136px";
					if (this.adminFlag != 1) {
						paddingTop = "84px";
					}
			        //提示框
			        this.popConfirm=new Alert("我是Confirm框",{
			        	parent:self.$el[0],
			            title:false,
			            onClickCancel:function(e){
			                e.hide();
			            }
			        });
			        var managerSnippetContainer=document.querySelector(".SID-manager-snippet");
			        this.pagination=[],this.drag=[];
			        this.pagination[0]={
	                    hasData:null,
	                    current:1,//当前页数
	                    max:0,//总页数
	                    limit:10,//每页条数
	                    total:0//总条数
	                };
			        this.drag[0]=DfPull({
			        	overflowContainer:this.sliderContainers[0],
	                    parent:this.sliderContainers[0],
		                onTopRefresh:function(e){
		                	console.log("头部刷新");
	                        self.pagination[0].current=1;
		                	self._loadManager(false);
		                },
		                onTopComplete:function(e){
	                        console.log("头部完成");
	                    },
		                onBottomRefresh:function(e){
		                	console.log("底部刷新");
	                        self.pagination[0].current++;
	                        self._loadManager(true);
		                },
		                onBottomComplete:function(e){
	                        console.log("底部完成");
	                    },
	                    onBottomNoData:function(e){
	                        console.log("底部无数据了");
	                    }
		            });
		            this.pagination[1]={
	                    hasData:null,
	                    current:1,//当前页数
	                    max:0,//总页数
	                    limit:10,//每页条数
	                    total:0//总条数
	                };
			        this.drag[1]=DfPull({
			        	overflowContainer:this.sliderContainers[1],
	                    parent:this.sliderContainers[1],
		                onTopRefresh:function(e){
		                	console.log("头部刷新");
	                        self.pagination[1].current=1;
		                	self._loadApproval(false);
		                },
		                onTopComplete:function(e){
	                        console.log("头部完成");
	                    },
		                onBottomRefresh:function(e){
		                	console.log("底部刷新");
	                        self.pagination[1].current++;
	                        self._loadApproval(true);
		                },
		                onBottomComplete:function(e){
	                        console.log("底部完成");
	                    },
	                    onBottomNoData:function(e){
	                        console.log("底部无数据了");
	                    }
		            });
		            this.toast=new Prompt("5",{"parent":self.$el[0]});
		            if(this.parentObj.managerTab===1){
		            	this._tabActive(1);
		            	this.slider.slideTo(1);
		            }else{
		            	this._loadManager();		            	
		            }
				},
				_setUpContent: function(data_t) {
					var self = this;
					var html = _.template(
						Template, {
							'imgPath': self.constants.IMAGEPATH,
							'adminFlag':self.adminFlag
						});
					this.$el.append(html);
					//初始化
					this._onLoad();
				}
			});

		return ManagerSnippentView;
	});
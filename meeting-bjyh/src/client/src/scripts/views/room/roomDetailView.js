define([
    "app",

    "text!../../templates/room/roomDetailTemplate.html"
], function(app, roomDetailTemplate){

    var view = Backbone.View.extend({
        initialize:function(){
            this.roomName="";
        	this.roomId = "";
        	this.startTime = "-1";
        	this.endTime = "-1";
            this.data={};
            this.selectMonth="";
            this.hasDate=[];
            this.reservedInfos=[];
            this.submintObj={};
        },
        render:function(roomId,startTime,endTime){
        	this.roomId = roomId;
        	this.startTime = startTime;
        	this.endTime = endTime;
            var self = this;

            //渲染页面
            var html=_.template(
            roomDetailTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //header
            this.header=this.$el.find("header")[0];

            //会议室详情弹出框
            this.roomdialog=new Dialog("#ID-MeetDetatil-RoomPop",
                {
                    overflowContainer:self.$el[0],
                    position:"top",
                    animation:"slideDown",
                    css:{width:"100%",paddingTop:"44px",position:"absolute"},
                    onHid:function(e){
                        self.header.style.zIndex="11";
                    }
                }
            );
            
            //是否可选弹出框
            this.dialog=new Dialog("#ID-MeetDetatil-Pop",
                {
                    overflowContainer:self.$el[0],
                    position:"top",
                    animation:"slideDown",
                    css:{width:"100%",paddingTop:"44px",position:"absolute"},
                    onHid:function(e){
                        self.header.style.zIndex="11";
                    }
                }
            );
            var popOk=this.dialog.container.querySelector("#ID-MeetDetatil-PopOk");
            var popCancel=this.dialog.container.querySelector("#ID-MeetDetatil-PopCancel");
            var popContent=this.dialog.container.querySelector("#ID-MeetDetatil-PopContent");

            //时间段
            this.timepart=new Timepart("#ID-RoomDetail-Timepart",{
                onClick:function(e){
                    self.header.style.zIndex="11";
                    if(e.target.classList.contains("SID-DisabledInfo")){//占用
                    	self.roomdialog.hide();
                        var dataPart=JSON.parse(e.target.getAttribute("data-progress"));
                        if(dataPart.userName)self.$el.find("#ID-RoomDetail-ReservedPeople").html(dataPart.userName);
                        self.$el.find("#ID-RoomDetail-ReservedPhone").html(dataPart.tel);
                        self.$el.find("#ID-RoomDetail-ReservedStart").html(dataPart.beginTimeStr);
                        self.$el.find("#ID-RoomDetail-ReservedEnd").html(dataPart.endTimeStr);
                        popOk.style.display="none";
                        popContent.style.display="block";
                        popCancel.style.display="none";
                        self.dialog.show();
                    }else if(e.target.classList.contains("disabled")){//过期
                        popOk.style.display="none";
                        popContent.style.display="none";
                        popCancel.style.display="-webkit-box";
                        self.dialog.show();
                    }else{//可预定
                        popOk.style.display="-webkit-box";
                        popContent.style.display="none";
                        popCancel.style.display="none";
                        self.dialog.show();
                    }
                }
            });
            //日历
            this.calendar=new Calendar("#ID-RoomDetail-Calendar",{
                viewType:"week",
                isShowDayNum:true,
                //isYTouch:false,//是否允许竖向滑动
                activeDate:new Date(),
                prevHTML:'<i class="icon-arrowleft"></i>',
                nextHTML:'<i class="icon-arrowright"></i>',
                onChange:function(e){
                	self.selectMonth = e.activeDate.format("yyyy-MM");
                    self._getReservedDetail(e);
                }
            });
            //时间间隔
            this.minutesSpace=60;
            //最大最小时间
            this.minSpTime=new Date();
            this.minSpTime.setHours(7,0,0,0);
            this.maxSpTime=new Date();
            this.maxSpTime.setHours(22,0,0,0);
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
            //设置默认时间
            this._setDefaults();
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            //加载数据
            this.loadData();
        },
        refresh:function(roomId,startTime,endTime){
        	this.$el.find(".SID-RoomDetail-Title").text("");
        	this.roomId = roomId;
        	this.startTime = startTime;
        	this.endTime = endTime;
            app.loading.show();
            console.log("会议室预定详情roomDetailView：刷新");
            this.selectMonth="";
            this.hasDate=[];
            this.reservedInfos=[];
            //还原时间段
            this.timepart.reset();
            this.calendar.reset();
            //设置默认时间
            this._setDefaults();

            this.loadData();
        },
        destroy:function(){
            console.log("会议室预定详情roomDetailView：移除");
            this.selectMonth="";
            this.hasDate=[];
            this.reservedInfos=[];
            /*this.undelegateEvents();
            this.unbind();
            this.$el.empty();*/
            this.dialog.hide();
            this.roomdialog.hide();
            //this._setReset();
        },
        loadData:function(){
            app.loading.show();
            //if(this.startTime != "" && this.endTime != ""){
			//	this.timepart.activePartsByRange(this.startTime,this.endTime);
			//}
			var self = this;
			var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomDetail+"&sessionId="+window.token;
            var pageParam = "&roomId="+this.roomId;
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
				success : function(data) {
					self.data=data;
                    self.calendar.draw();
					self.errorPanel.removeClass("active");
					if (data.code == "1") {
                        self.roomName=data.roomName;
						self.$el.find(".SID-RoomDetail-Title").text(self.roomName);
						if(data.collection==1){
							self.$el.find("#ID-RoomDetail-BtnFav").text("已收藏");
							self.$el.find("#ID-RoomDetail-BtnFav").addClass("active");
						} else {
							self.$el.find("#ID-RoomDetail-BtnFav").text("收藏");
							self.$el.find("#ID-RoomDetail-BtnFav").removeClass("active");
						}
						if (data.needApprove == 1) {
							self.$el.find(".SID-needApprove").html("需审批");
                            self.$el.find(".SID-needApprove").removeClass("disabled").addClass("success");
						} else {
							self.$el.find(".SID-needApprove").html("无需审批");
                            self.$el.find(".SID-needApprove").removeClass("success").addClass("disabled");
						}
						self.$el.find(".SID-capacity").text(data.capacity+"人");
						self.$el.find(".SID-area").text(data.area+"平方");
						var deviceText = "";
						var deviceName = "";
						if (data.projector==1) {
							deviceName = "投影";
							deviceText += deviceText==""?deviceName:"、" + deviceName;
						}
						if (data.display==1) {
							deviceName = "显示";
							deviceText += deviceText==""?deviceName:"、" + deviceName;
						}
						if (data.microphone==1) {
							deviceName = "麦克风";
							deviceText += deviceText==""?deviceName:"、" + deviceName;
						}
						if (data.stereo==1) {
							deviceName = "音响";
							deviceText += deviceText==""?deviceName:"、" + deviceName;
						}
						if (data.wifi==1) {
							deviceName = "无线网";
							deviceText += deviceText==""?deviceName:"、" + deviceName;
						}
						self.$el.find(".SID-device").text(deviceText);
						if (deviceText == "") {
							self.$el.find(".SID-device").parent().hide();
						} else {
							self.$el.find(".SID-device").parent().show();
						}
						// 会议室管理员
						self.$el.find(".SID-adminName").text(data.adminName);
						if (data.adminName == "") {
							self.$el.find(".SID-adminName").parent().hide();
						} else {
							self.$el.find(".SID-adminName").parent().show();
						}
						// 会议室服务人员
						self.$el.find(".SID-serviceName").text(data.serviceName);
						if (data.serviceName == "") {
							self.$el.find(".SID-serviceName").parent().hide();
						} else {
							self.$el.find(".SID-serviceName").parent().show();
						}
						self.$el.find(".SID-remarks").text(data.remarks);
						if (data.remarks == "") {
                            self.$el.find(".SID-remarks").parent().hide();
                        } else {
                        	self.$el.find(".SID-remarks").parent().show();
                        }
						
			            return;
					} else {
						app.toast.setText(data.message);
						app.toast.show();
					}
				},
				error : function(){
					self.errorPanel.addClass("active");
				},
                complete:function(){
                    /*setTimeout(function(){
                        app.loading.hide();
                    }, 300);*/
                }
			});
        },
		_getReservedDetail:function(e){
			var self = this;
			if ($.inArray(self.selectMonth,self.hasDate) > -1) {
				self._changeDate(e);
				app.loading.hide();
				return;
			}
			var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomDetail+"&sessionId="+window.token;
            var pageParam = "&roomId="+this.roomId+"&selectMonth="+self.selectMonth;
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
				success : function(data) {
					app.loading.hide();
					self.errorPanel.removeClass("active");
					if (data.code == "1") {
						if ($.inArray(self.selectMonth,self.hasDate) > -1) {
							self._changeDate(e);
							app.loading.hide();
							return;
						}
						if (data.reservedInfos) {
							self.hasDate.push(self.selectMonth);
							self.reservedInfos = self.reservedInfos.concat(data.reservedInfos);
						}
					}
					self._changeDate(e);
				},
				error : function(){
					self.errorPanel.addClass("active");
				},
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
			});
		},
		_changeDate:function(e){
			var self = this;
			var today=new Date();
            //还原
            self.timepart.reset();

            /*//今天
            var todayStartTimes=[];
            if(today.compareDate(e.activeDate)==0){
                var date=new Date();
                date.setMinuteCeil(10);
                if(firstStartTime && today.compareTime(firstStartTime)==1){
                    date=firstStartTime.setMinuteFloor(10);
                }
                self.timepart.disableTimes(null,date,["SID-DisabledBefore"]);
            }else if(today.compareDate(e.activeDate)==1){
                self.timepart.disableTimes(null,null,["SID-DisabledBefore"]);
            }*/

            //禁用-小于等于当前时间的 | 修改时间框的值
            if(today.compareDate(e.activeDate)==0){
                self.timepart.disableTimes(null,today.setMinuteCeil(10));
                //设置日期
                self._setDay(e.activeDate);
            }else if(today.compareDate(e.activeDate)>0){
                self.timepart.disableTimes(null,null);
            }else{
                //设置日期
                self._setDay(e.activeDate);   
            }
            //标出占用-已预定的时间
            if(self.reservedInfos){
                var infos=self.reservedInfos;
                for(n in infos){
                    var info=infos[n];
                    var startTime=new Date(info.beginTime);
                    var endTime=new Date(info.endTime);
                    /*//如果是今天则提取，为下面计算firstStartTime
                    if(startTime.compareDate(today)==0){
                        todayStartTimes.push(startTime);
                    }*/
                    //不跨天
                    if(startTime.compareDate(endTime)==0){
                        if(startTime.compareDate(e.activeDate)==0){
                            //console.log("不跨天");
                            self.timepart.chooseTimes(startTime,endTime,["SID-DisabledInfo"],JSON.stringify(info));
                        }
                    //跨天
                    }else{
                        if(startTime.compareDate(e.activeDate)==0){//开始时间是当前天
                            //console.log("开始天");
                            self.timepart.chooseTimes(startTime,null,["SID-DisabledInfo"],JSON.stringify(info));   
                        }else if(endTime.compareDate(e.activeDate)==0){//结束时间是当前天
                            //console.log("结束天");
                            self.timepart.chooseTimes(null,endTime,["SID-DisabledInfo"],JSON.stringify(info));   
                        }else if(startTime < e.activeDate &&  e.activeDate< endTime){//中间天
                            //console.log("中间天");
                            self.timepart.chooseTimes(null,null,["SID-DisabledInfo"],JSON.stringify(info));   
                        }else{
                            //console.log("其它天");
                        }
                    }
                }
            }
            /*var firstStartTime=null;
            if(todayStartTimes.length>0){
                firstStartTime=todayStartTimes.sort().shift();
            }*/
		},
        /*=========================
          Model
          ===========================*/
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
                        //如果结束时间为空，或者开始时间大于结束时间，则设置结束时间
                        if((endInput.value=="") || (activeDate >= endInputDate)){
                            var endTime=new Date(activeDate).plusMinute(self.minutesSpace);
                            //如果结束时间大于最大时间，则结束时间设置为最大时间
                            if(endTime.compareTime(self.maxSpTime)===1){
                                endTime.setHours(self.maxSpTime.getHours(),self.maxSpTime.getMinutes(),0,0);
                            }
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
                        //如果开始时间为空，或者结束时间小于开始时间，设置开始时间
                        var prevInputDate=new Date(prevInput.value.replace(/-/g,"/"));
                        if((prevInput.value=="") || (activeDate <= prevInputDate)){
                            var startTime=activeDate.minusMinute(self.minutesSpace);
                            //如果结束时间大于最大时间，则结束时间设置为最大时间
                            if(startTime.compareTime(self.minSpTime)===-1){
                                startTime.setHours(self.minSpTime.getHours(),self.minSpTime.getMinutes(),0,0);
                            }
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
        //表单验证
        _validateForm:function(){
            $(".inputbox-error").removeClass("inputbox-error");
            var validator=new Validator();
            this.$el.find('[name="NID-RoomDetail-TimeStart"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'开始时间不能为空'
                    }
                ]);
            });
            this.$el.find('[name="NID-RoomDetail-TimeEnd"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'结束时间不能为空'
                    }
                ]);
            });
            var error=validator.start();
            return error;
        },
        _setReset:function(){
            this.$el.find("[name='NID-RoomDetail-TimeStart']").val("").parent().removeClass("inputbox-error");
            this.$el.find("[name='NID-RoomDetail-TimeEnd']").val("").parent().removeClass("inputbox-error");
        },
        _setDefaults:function(){
            //开始时间
            var startTime=new Date().setMinuteCeil(10);
            var startTimeStr=startTime.format("yyyy-MM-dd hh:mm");
            //结束时间
            var endTime=new Date(startTime).plusMinute(this.minutesSpace);
            var endTimeStr=endTime.format("yyyy-MM-dd hh:mm");

            this.$el.find("[name='NID-RoomDetail-TimeStart']").val(startTimeStr);
            this.$el.find("[name='NID-RoomDetail-TimeEnd']").val(endTimeStr);
        },
        _setDay:function(date){
            var self=this;
            var inputStartVal=self.$el.find("[name='NID-RoomDetail-TimeStart']").val();
            var inputEndVal=self.$el.find("[name='NID-RoomDetail-TimeEnd']").val();
            if(inputStartVal=="" || inputEndVal==""){
                self._setDefaults();
            }
            
            //开始时间
            var startTimeStr=date.year()+"-"+date.month()+"-"+date.date()+inputStartVal.substr(10,inputStartVal.length);
            self.$el.find("[name='NID-RoomDetail-TimeStart']").val(startTimeStr);
            //结束时间
            var endTimeStr=date.year()+"-"+date.month()+"-"+date.date()+inputEndVal.substr(10,inputEndVal.length);
            self.$el.find("[name='NID-RoomDetail-TimeEnd']").val(endTimeStr);
        },
        /*=========================
          Events
          ===========================*/
        events: {
        	'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back' : '_onBack',
            'click #ID-RoomDetail-BtnFav':'_onClickBtnFav',
            'click #ID-RoomDetail-BtnToggle':'_onClickBtnToggle',
            'click .SID-DivDateTime' : '_onClickDivDateTime',
            'click #ID-RoomDetail-BtnSubmit' : '_onSubmit'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.loadData();
        },
        _onBack:function(){
            if(!this.dialog.isHid){
                this.dialog.hide();
                return;
            }
            if(!this.roomdialog.isHid){
                this.roomdialog.hide();
                return;
            }
            this.destroy();
            history.go(-1);
            return false;
        },
        _onClickBtnFav:function(e){
        	var type = 0;
            if(e.target.classList.contains("active")){
            	type=0;
            }else{
            	type=1;
            }
            var self = this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomFavorite+"&sessionId="+window.token;
            var pageParam = "&roomId="+self.roomId+"&operationType=" + type;
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
				success : function(data) {
					$(".SID-loading").hide();
					if (data.code == "1") {
						if (type == 0) {
							self.$el.find("#ID-RoomDetail-BtnFav").removeClass("active");
							self.$el.find("#ID-RoomDetail-BtnFav").text("收藏");
			                app.toast.setText("取消收藏");
                            app.router.views.indexView._changeRoomCollection(false);
						} else {
							self.$el.find("#ID-RoomDetail-BtnFav").addClass("active");
							self.$el.find("#ID-RoomDetail-BtnFav").text("已收藏");
							app.toast.setText("收藏成功");
                            app.router.views.indexView._changeRoomCollection(true);
						}
						app.toast.show();
					} else {
						app.toast.setText(data.message);
						app.toast.show();
					}
				},
				error : function(){
					$(".SID-loading").hide();
					app.toast.setText("操作失败");
					app.toast.show();
				}
			});
        },
        _onClickBtnToggle:function(e){
        	this.header.style.zIndex="11";
        	this.dialog.hide();
            if(!this.roomdialog.isHid){
                this.roomdialog.hide();
            }else{
                this.roomdialog.show();
            }
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
        //提交预定
        _onSubmit:function(e){
            var error=this._validateForm();
            if(error){
                error.field.parentNode.classList.add("inputbox-error");
                app.prompt.setText(error.msg);
                app.prompt.show();
                return;
            }
            //验证时间是否已经被选中
            var startTime=new Date(this.$el.find("[name='NID-RoomDetail-TimeStart']").val().replace(/-/g,"/"));
            var endTime=new Date(this.$el.find("[name='NID-RoomDetail-TimeEnd']").val().replace(/-/g,"/"));
            var isConflict=false;
            if(this.reservedInfos){
                var infos=this.reservedInfos;
                for(n in infos){
                    var info=infos[n];
                    var beginTime=new Date(info.beginTime);
                    var finishTime=new Date(info.endTime);
                    //相交
                    if((startTime > beginTime && startTime < finishTime)||(endTime > beginTime && endTime < finishTime)){
                        isConflict=true;
                        break;
                    }
                    //包含
                    if((beginTime > startTime && beginTime < endTime)||(finishTime > startTime && finishTime < endTime)){
                        isConflict=true;
                        break;
                    }
                }
            }
            if(isConflict){
                app.toast.setText("您要预定的时间已经被占用，请重新选择");
                app.toast.show();
                return;
            }
            var meetTime=this.$el.find("[name='NID-RoomDetail-TimeStart']").val()+" ~ "+this.$el.find("[name='NID-RoomDetail-TimeEnd']").val();
//            $(".SID-MeetAdd-Room").eq(0).removeClass("hide").find("[name='NID-MeetAddress']").val(this.roomName);
//            $(".SID-MeetAdd-Room").eq(0).removeClass("hide").find("[name='NID-MeetTime']").val(meetTime);
//            $(".SID-MeetAdd-Room").eq(1).addClass("hide").find("[name='NID-MeetRoom']").val(this.roomId);
//            app.routerViews.meetAddView.needApprove = this.data.needApprove;
//            this.destroy();
//            history.go(-2);
            this.submintObj = {};
            this.submintObj.roomName = this.roomName;
            this.submintObj.roomId = this.roomId;
            this.submintObj.meetTime = meetTime;
            this.submintObj.needApprove = this.data.needApprove;
            var hash="#"+app.pages.meetAdd;//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
        }
    });

    return view;
});
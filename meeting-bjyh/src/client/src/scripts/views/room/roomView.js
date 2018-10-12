define([
    "app",

    "text!../../templates/room/roomTemplate.html",
    "text!../../templates/room/roomListTemplate.html"
], function(app, roomTemplate, roomListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.offset = 1;
        	this.limit = 10;
        	this.timestamp = 0;
        	this.endflag = 0;
        	this.timeStart = "";
            this.timeEnd = "";
            this.peopleNum = 0;
            this.dragFlag=0;
        },
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            roomTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
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
            //header
            this.header=this.$el.find("header")[0];
            this.article=self.$el.find("article")[0];
            //列表
            this.listContainer=this.$el.find("#ID-Room-List");
            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.article,
                css:{top:"50px",backgroundColor:"#20aeff"}
            });
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
            //下拉刷新
            this.drag=DfPull({
                overflowContainer:self.article,
                topParent:self.article,
                bottomParent:self.article,
                onTopRefresh:function(e){
                    console.log("头部刷新");
                	self.loadData();
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomRefresh:function(e){
                    console.log("底部刷新");
                    self.loadMoreData();
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            //加载数据
            this.loadData();
        },
        refresh:function(){
            console.log("会议室预定roomView：刷新");
            this.dragFlag=0;
            this.$el.find("article").scrollTop(0);
            this._onSubmit();
        },
        destroy:function(){
            console.log("会议室预定roomView：移除");
            this.$el.find("article").scrollTop(0);
            /*this.undelegateEvents();
            this.unbind();
            this.$el.empty();*/
            this.dialog.hide();
            $("#ID-Room-BtnReset").trigger("click");
            this._onReset();
        },
        loadData:function(){
        	this.reloadPage(false);
        },
        loadMoreData:function(){
        	this.reloadPage(true);
        },
        reloadPage:function(nextPageFlag){
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomList+"&sessionId="+window.token;

			if (nextPageFlag) {
			} else {
				this.offset = 1;
				this.timestamp = 0;
			}
            var pageParam = "&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            if (this.timeStart != "" && this.timeEnd != "") {
            	pageParam += "&reservedStartTime=" + this.timeStart + ":00";
            	pageParam += "&reservedEndTime=" + this.timeEnd + ":00";
            }
            if (this.peopleNum != "") {
            	pageParam += "&capacity=" + this.peopleNum;
            }
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                	self.errorPanel.removeClass("active");
                    self.offset = self.offset + 1;
					self.timestamp = data.timestamp;
					self.endflag = data.endflag;
                    //没有数据
                    if(data.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        //标识头部与尾部状态
                        self.drag.topNoData();
                        self.drag.bottomNoData();
                        return;
                    }
                    //编译渲染
                    var listHTML=_.template(roomListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data.room
                    });
                    if (!nextPageFlag) {//头部刷新
                        self.listContainer[0].innerHTML=listHTML;
                        setTimeout(function(){
                            if (self.endflag) {
                                self.drag.topNoData();
                                self.drag.bottomNoData();
                            }else{
                                self.drag.topComplete();
                            }
                            if(self.dragFlag){
                                self.prompt.show();
                            }
                            self.dragFlag=1;
                        }, 300);
                    }else{//底部刷新
                        self.listContainer[0].innerHTML+=listHTML;
                        if (self.endflag) {
                            self.drag.bottomNoData();
                        }else{
                            self.drag.bottomComplete();
                        }
                    }
                },
                error:function(msg){
                    //标识头部与尾部状态
                    self.drag.topNoData();
                    self.drag.bottomNoData();
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
        //表单验证
        _validateForm:function(){
            $(".inputbox-error").removeClass("inputbox-error");
            var validator=new Validator();
            $('[name="NID-Room-TimeStart"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'开始时间不能为空'
                    }
                ]);
            });
            $('[name="NID-Room-TimeEnd"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'结束时间不能为空'
                    }
                ]);
            });
            $('[name="NID-Room-PeopleNum"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'会场人数不能为空'
                    },
                    {
                        rule:'minNumber:1',
                        errorMsg:'会场人数不能小于1'
                    },
                    {
                        rule:'maxNumber:9999',
                        errorMsg:'会场人数不能大于9999'
                    }
                ]);
            });

            var error=validator.start();
            return error;
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back' : '_onBack',
            'click #ID-Room-BtnFilter' : '_onClickBtnFilter',
            'click .SID-DivDateTime' : '_onClickDivDateTime',
            'click #ID-Room-BtnReset' : '_onReset',
            'click #ID-Room-BtnSubmit' : '_onSubmit',
            'click .SID-Room-Li' : '_onClickLi'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onBack:function(){
            if(!this.dialog.isHid){
                this.dialog.hide();
                return;
            }
            this.destroy();
            history.go(-1);
        },
        _onClickBtnFilter:function(e){
            this.header.style.zIndex="11";
            this.dialog.toggle();
        },
        _onSubmit:function(e){
            /*var error=this._validateForm();
            if(error){
                error.field.parentNode.classList.add("inputbox-error");
                app.toast.setText(error.msg);
                app.toast.show();
                return;
            }*/
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
            this.loadData();
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
        _onClickLi:function(e){
        	var roomId=e.currentTarget.getAttribute("data-roomId");
        	var startTime = escape(this.timeStart==""?"-1":this.timeStart);
        	var endTime = escape(this.timeEnd==""?"-1":this.timeEnd);	
            var hash="#"+app.pages.roomDetail+"/"+roomId+"/"+startTime+"/"+endTime;//会议室详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
    });

    return view;
});
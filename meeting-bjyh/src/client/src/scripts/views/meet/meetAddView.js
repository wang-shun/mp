define([
    "app",

    "text!../../templates/meet/meetAddTemplate.html",
    "text!../../templates/meet/add/meetAddAgendaTemplate.html",
    "text!../../templates/meet/add/meetAddRemarkTemplate.html",
    "text!../../templates/meet/add/meetAddSignTemplate.html",
], function(app, meetAddTemplate, meetAddAgendaTemplate, meetAddRemarkTemplate, meetAddSignTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.needApprove = "0";
        },
        _initRoomInfo:function(){
        	if (app.routerViews.roomDetailView) {
	//          $(".SID-MeetAdd-Room").eq(0).removeClass("hide").find("[name='NID-MeetAddress']").val(this.roomName);
	//          $(".SID-MeetAdd-Room").eq(0).removeClass("hide").find("[name='NID-MeetTime']").val(meetTime);
	//          $(".SID-MeetAdd-Room").eq(1).addClass("hide").find("[name='NID-MeetRoom']").val(this.roomId);
	//          app.routerViews.meetAddView.needApprove = this.data.needApprove;
	        	this.$el.find("[name='NID-MeetAddress']").val(app.routerViews.roomDetailView.submintObj.roomName);
	        	this.$el.find("[name='NID-MeetTime']").val(app.routerViews.roomDetailView.submintObj.meetTime);
	        	this.$el.find("[name='NID-MeetRoom']").val(app.routerViews.roomDetailView.submintObj.roomId);
	        	this.needApprove = app.routerViews.roomDetailView.submintObj.needApprove;
        	}
        },
        render:function(){
            var self = this;
            setTimeout(function(){
                app.loading.hide();
            }, 300);
            //渲染页面
            var html=_.template(
            meetAddTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //提示框
            this.confirm=new Alert("您确定要放弃此次编辑吗？",{
                overflowContainer:self.$el[0],
                parent:self.$el[0],
                onClickOk:function(e){
                    e.hide();
                    self.destroy();
                    history.go(-1);
                },
                onClickCancel:function(e){
                    e.hide();
                }
            });
            this.confirm1=new Alert("我是Confirm框",{
                onClickCancel:function(e){
                    e.hide();
                }
            });
            app.formControls.update();
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
            // 默认参会人员为登录人
            this.$el.find("#ID-MeetAdd-People").val(app.loginUser.username);
            this.$el.find("[name='NID-MeetAdd-People']").val(app.loginUser.loginId+":1:"+app.loginUser.username+":0:"+app.loginUser.deptId);
            // 初始化会议室选定信息
            this._initRoomInfo();
            //返回控制
            Mplus.BackMonitor.addHandler(function(){
                self._CallBack();
            });
        },
        _CallBack:function (){
        	var hash = window.location.hash;
        	var page=hash.indexOf("/")>=0?hash.substring(0,hash.indexOf("/")):hash;
        	app.routerViews.meetAddView.confirm1.hide();
    		if ("#ID-PageMeetAdd" == page) {
    			app.routerViews.meetAddView.confirm.show();
    		} else if ("#ID-PageTree" == page) {
    			app.routerViews.treeView._onSubmit();
    		} else {
    			history.go(-1);
    		}
    	},
        refresh:function(){
            console.log("新建会议meetAddView：刷新");
            this.render();
        },
        destroy:function(){
            var self=this;
            Mplus.BackMonitor.removeHandler(function(){
		    	self._CallBack();
		    });
            console.log("新建会议meetAddView：移除");
            this.undelegateEvents();
            this.unbind();
            setTimeout(function() {
                self.$el.empty();
            }, 300);
        },

        /*=========================
          Method
          ===========================*/
        //取消确认删除
        _reConfirmDel:function(){
            $(".SID-BtnDel").html("删除").removeClass("active");
            $(".SID-IconDelConfirm").removeClass("active");
        },
        //签到号码排序
        _sortLblSign:function(){
            var lblSigns=document.querySelectorAll(".SID-LblSign");
            for(var i=0,lbl;lbl=lblSigns[i++];){
                lbl.value="签到"+i;
            }
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
                    if ($("[name='NID-MeetTime']").val() == "") {
                    	app.toast.setText("请先预定会议室 ");
                        app.toast.show();
                        e.scrollpicker.hide();
                        return;
                    }
                    var roomDate=$("[name='NID-MeetTime']").val().split("~");
                    var startRoomDate,endRoomDate;
                    if(roomDate){
                        startRoomDate=new Date(roomDate[0].replace(/-/g,"/"));
                        endRoomDate=new Date(roomDate[1].replace(/-/g,"/"));
                    }
                    
                    //点击开始时间
                    if(input.classList.contains("SID-DateTimeStart")){
                        //时间不能早于当前时间
                        if(activeDate.compareDateTime(new Date())==-1){
                            app.toast.setText("议程时间需在会议室预定的有效时间范围 ");
                            app.toast.show();
                            return;
                        }
                        //开始时间不能早于会议室时间
                        if(startRoomDate && activeDate.compareDateTime(startRoomDate)==-1){
                            app.toast.setText("议程时间需在会议室预定的有效时间范围");
                            app.toast.show();
                            return;
                        }
                        //开始时间不能晚于会议室时间
                        if(endRoomDate && activeDate.compareDateTime(endRoomDate)>=0){
                            app.toast.setText("议程时间需在会议室预定的有效时间范围");
                            app.toast.show();
                            return;
                        }

                        var endInput=input.parentNode.nextElementSibling.querySelector(".SID-DateTimeEnd");
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
                        //结束时间不能早于当前时间
                        if(activeDate.compareDateTime(new Date().setMinuteCeil(10))<=0){
                            app.toast.setText("议程时间需在会议室预定的有效时间范围");
                            app.toast.show();
                            return;
                        }

                        //结束时间不能晚于会议室时间
                        if(endRoomDate && activeDate.compareDateTime(endRoomDate)>0){
                            app.toast.setText("议程时间需在会议室预定的有效时间范围");
                            app.toast.show();
                            return;
                        }

                        //结束时间不能早于会议室时间
                        if(startRoomDate && activeDate.compareDateTime(startRoomDate)<=0){
                            app.toast.setText("议程时间需在会议室预定的有效时间范围");
                            app.toast.show();
                            return;
                        }

                        var prevInput=input.parentNode.previousElementSibling.querySelector(".SID-DateTimeStart");
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
            //基本信息
            $('[name="NID-MeetName"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'会议名称不能为空'
                    },
                    {
                        rule:'maxLength:29',
                        errorMsg:'会议名称不能超过30位'
                    }
                ]);
            });
            $('[name="NID-MeetRoom"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'请先预定会议室'
                    }
                ]);
            });
            $('[name="NID-MeetAdd-PeopleName"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'参会人员不能为空'
                    }
                ]);
            });
            //会议议程
            $('[name="NID-AgendaStart"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                    validator.add(n,[
                        {
                            rule:'required',
                            errorMsg:'开始时间不能为空'
                        }
                    ]);
                }
            });
            $('[name="NID-AgendaEnd"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                    validator.add(n,[
                        {
                            rule:'required',
                            errorMsg:'结束时间不能为空'
                        }
                    ]);
                }
            });
            $('[name="NID-AgendaAddress"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                    validator.add(n,[
                        {
                            rule:'required',
                            errorMsg:'议程地址不能为空'
                        },
                        {
                            rule:'maxLength:29',
                            errorMsg:'议程地址不能超过30位'
                        }
                    ]);
                }
            });
            $('[name="NID-AgendaDes"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                    validator.add(n,[
                        {
                            rule:'required',
                            errorMsg:'议程说明不能为空'
                        },
                        {
                            rule:'maxLength:49',
                            errorMsg:'议程说明不能超过50位'
                        }
                    ]);
                }
            });
            //会议签到
            $('[name="NID-SignDes"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                    validator.add(n,[
                        {
                            rule:'required',
                            errorMsg:'签到说明不能为空'
                        },
                        {
                            rule:'maxLength:29',
                            errorMsg:'签到说明不能超过30位'
                        }
                    ]);
                }
            });
            //会议提醒
//            $('[name="NID-Alarm"]').each(function(i,n){
//                if(!n.classList.contains("form-ignore")){
//                    validator.add(n,[
//                        {
//                            rule:'required',
//                            errorMsg:'会议提醒不能为空'
//                        },
//                        {
//                            rule:'minNumber:1',
//                            errorMsg:'提醒时间不合法'
//                        }
//                    ]);
//                }
//            });
            //会议备注
            $('[name="NID-Remarks"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                    validator.add(n,[
                        {
                            rule:'required',
                            errorMsg:'会议备注不能为空'
                        },
                        {
                            rule:'maxLength:49',
                            errorMsg:'会议备注不能超过50位'
                        }
                    ]);
                }
            });

            var error=validator.start();
            return error;
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Back' : '_onBack',
            'click article' : '_onClickBody',

            'click #ID-MeetAdd-BtnAddAgenda' : '_onClickBtnAddAgenda',
            'click #ID-MeetAdd-BtnAddSign' : '_onClickBtnAddSign',
            'click #ID-MeetAdd-BtnAddRemark' : '_onClickBtnAddRemark',

            'click #ID-MeetAdd-SwitchAgenda' : '_onClickSwitchAgenda',
            'click #ID-MeetAdd-SwitchSign' : '_onClickSwitchSign',
            'click #ID-MeetAdd-SwitchAlarm' : '_onClickSwitchAlarm',
            'click #ID-MeetAdd-SwitchRemark' : '_onClickSwitchRemark',
            'click #ID-MeetAdd-SwitchCreate' : '_onClickSwitchCreate',
            
            'click .SID-IconDel' : '_onClickIconDel',
            'click .SID-IconDelConfirm' : '_onClickIconDelConfirm',
            'click .SID-BtnDel' : '_onClickBtnDel',

            'click .SID-DivDateTime' : '_onClickDivDateTime',

            //表单验证
            'input [name="NID-MeetName"]' : '_onChangeMeetName',
            'input [name="NID-AgendaAddress"]' : '_onChangeAgendaAddress',
            'input [name="NID-AgendaDes"]' : '_onChangeAgendaDes',
            'input [name="NID-SignDes"]' : '_onChangeSignDes',
            'input [name="NID-Remarks"]' : '_onChangeRemarks',

            //提交表单
            'click #ID-MeetAdd-BtnSubmit' : '_onSubmit',

            //会议室预定
            'click .SID-MeetAdd-Room' : '_onClickMeetAddRoom',

            //树结构
            'click #ID-MeetAdd-Tree' : '_onClickMeetAddTree'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(e){
        	this.confirm1.hide();
            this.confirm.show();
        },
        _onClickBody:function(e){
            if(!e.target.classList.contains("SID-BtnDel") && !e.target.classList.contains("SID-IconDel") && !e.target.classList.contains("SID-IconDelConfirm")){
                this._reConfirmDel();
            }
        },

        _onClickBtnAddAgenda:function(e){
            $(e.target.previousElementSibling).append(meetAddAgendaTemplate);
            app.formControls.update();
            console.log("添加会议议程");
        },
        _onClickBtnAddSign:function(e){
            $(e.target.previousElementSibling).append(meetAddSignTemplate);
            this._sortLblSign();
            app.formControls.update();
            console.log("添加会议签到");
        },
        _onClickBtnAddRemark:function(e){
            $(e.target.previousElementSibling).append(meetAddRemarkTemplate);
            app.formControls.update();
            console.log("添加会议备注");
        },

        _onClickIconDel:function(e){
            $(e.target.nextElementSibling).addClass("active");
            console.log("删除");
        },
        _onClickIconDelConfirm:function(e){
            var self=this;
            $(e.target.parentNode).slideUp("200",function(){
                $(this).remove();
                self._sortLblSign();
            });
            
            console.log("确认删除");
        },
        _onClickBtnDel:function(e){
            if(e.target.classList.contains("active")){
                $(e.target.parentNode).slideUp("200",function(){
                    $(this).remove();
                });
            }else{
                $(e.target).addClass("active").html("确认删除");
            }
        },

        _onClickSwitchAgenda:function(e){
            var opts=$(e.target.parentNode.parentNode).find(".SID-SwitchOptions");
            var dynamicOpts=opts.find(".SID-DynamicOptions");
            
            //开关
            if(e.target.classList.contains("active")){
                //默认添加一条
                if(!dynamicOpts.children()[0]){
                    $("#ID-MeetAdd-BtnAddAgenda").trigger("click");
                }
                //标题选中
                e.target.previousElementSibling.classList.add("active");
                //展开
                opts.find(".input-text,.input-pre").removeClass("form-ignore");
                opts.slideDown("200");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
                //收缩
                opts.find(".input-text,.input-pre").addClass("form-ignore");
                opts.slideUp("200");
            }
        },
        _onClickSwitchSign:function(e){
            var opts=$(e.target.parentNode.parentNode).find(".SID-SwitchOptions");
            var dynamicOpts=opts.find(".SID-DynamicOptions");
            
            //关闭时忽略元素
            if(e.target.classList.contains("active")){
                //默认添加一条
                if(!dynamicOpts.children()[0]){
                    $("#ID-MeetAdd-BtnAddSign").trigger("click");
                }
                //标题选中
                e.target.previousElementSibling.classList.add("active");
                //展开
                opts.find(".input-text,.input-pre").removeClass("form-ignore");
                opts.slideDown("200");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
                //收缩
                opts.find(".input-text,.input-pre").addClass("form-ignore");
                opts.slideUp("200");
            }
        },
        _onClickSwitchAlarm:function(e){
            var opts=$(e.target.parentNode.parentNode).find(".SID-SwitchOptions");
            //开关
            if(e.target.classList.contains("active")){
                //标题选中
                e.target.previousElementSibling.classList.add("active");
                //展开
                opts.find(".input-text,.input-pre").removeClass("form-ignore");
                opts.slideDown("200");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
                //收缩
                opts.find(".input-text,.input-pre").addClass("form-ignore");
                opts.slideUp("200");
            }
        },
        _onClickSwitchRemark:function(e){
            var opts=$(e.target.parentNode.parentNode).find(".SID-SwitchOptions");
            var dynamicOpts=opts.find(".SID-DynamicOptions");
            
            //开关
            if(e.target.classList.contains("active")){
                //默认添加一条
                if(!dynamicOpts.children()[0]){
                    $("#ID-MeetAdd-BtnAddRemark").trigger("click");
                }
                //标题选中
                e.target.previousElementSibling.classList.add("active");
                //展开
                opts.find(".input-text,.input-pre").removeClass("form-ignore");
                opts.slideDown("200");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
                //收缩
                opts.find(".input-text,.input-pre").addClass("form-ignore");
                opts.slideUp("200");
            }
        },
        _onClickSwitchCreate:function(e){
            console.log("创建群组");
            //开关
            if(e.target.classList.contains("active")){
                //标题选中
                e.target.previousElementSibling.classList.add("active");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
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

        //表单验证
        _onChangeMeetName:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:29',
                    errorMsg:'会议名称不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
            }
        },
        _onChangeAgendaAddress:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:29',
                    errorMsg:'议程地址不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
            }
        },
        _onChangeAgendaDes:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:49',
                    errorMsg:'议程说明不能超过50位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
            }
        },
        _onChangeSignDes:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:29',
                    errorMsg:'签到说明不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
            }
        },
        _onChangeRemarks:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:49',
                    errorMsg:'会议备注不能超过50位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
            }
        },
        _getDate:function(startDiffTime) {
            //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式   
            return new Date(startDiffTime.replace(/\-/g, "/"));  
        },
        _onSubmit:function(e){
            var self = this;
            var error=this._validateForm();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                error.field.parentNode.classList.add("inputbox-error");
                return;
            }
            var form=new Form("#ID-FormMeetAdd",{
                ignoreClass:"form-ignore"
            }).serializeJson();
            // 会前提醒验证
            var alarmData=form['NID-Alarm'];
            if (this.$el.find("#ID-MeetAdd-SwitchAlarm").hasClass("active")) {
	            if(!/^(?!0)\d{1,3}$/.test(alarmData)) {
	            	app.toast.setText("会议提醒时间只能设置1-999");
	                app.toast.show();
	                $("[name='NID-Alarm']").parent().parent().addClass("inputbox-error");
	                return;
	            }
	            if(alarmData && alarmData.length>0){
	                var now=new Date();
	                var startTime=new Date(form['NID-MeetTime'][0].split(" ~ ")[0].replace(/-/g,"/"));
	                var diffMinute=now.diff(startTime).minutesAll;
	                var alarmMinute=form['NID-AlarmUnit'][0]*alarmData[0];
	                if(diffMinute<alarmMinute){
	                    app.toast.setText("无效的提醒时间，请重新设置");
	                    app.toast.show();
	                    $("[name='NID-Alarm']").parent().parent().addClass("inputbox-error");
	                    return;
	                }
	            }
            }
            $("[name='NID-Alarm']").parent().parent().removeClass("inputbox-error");
            
            //提交的参数
            var data = {
            		agendaFlag : 0,//会议议程
            		attachmentFlag : 0,//附件
            		createGroupFlag : 0,//创建会议
            		signinFlag : 0,//会议签到
                    alarmDataFlag: 0,//会议提醒
            		remarksFlag : 0,//会议备注
            		meeting : null,
            		inParticipantsList : [],
            		outParticipantsList : [],
            		serviceParticipantsList : [],
            		agendaList : [],
            		attachmentList : [],
            		signinSequList : [],
            		remarksList : []
            };
            data.roomId = this.$el.find("[name='NID-MeetRoom']").val();
            // 会议主信息
            var meetingInfo = new Object();
            meetingInfo.name = form['NID-MeetName'][0].replace(/(^\s*)|(\s*$)/g,"");
            meetingInfo.address = form['NID-MeetAddress'][0].replace(/(^\s*)|(\s*$)/g,"");
            meetingInfo.beginTime = form['NID-MeetTime'][0].split(" ~ ")[0]+":00";
            meetingInfo.endTime = form['NID-MeetTime'][0].split(" ~ ")[1]+":00";
            // 会前提醒
            var alarmData=form['NID-Alarm'];
            if(alarmData && alarmData.length>0){
            	meetingInfo.noticeType="1";
                if ($("[name='NID-AlarmUnit']").val()=="60"){
                	meetingInfo.noticeType="2";
                }
                meetingInfo.noticeSet=$("[name='NID-Alarm']").val();
            } else {
            	meetingInfo.noticeType = "0";
            	meetingInfo.noticeSet="0";
            }
            data.meeting = meetingInfo;
            // 内部人员
            var inner = form['NID-MeetAdd-People'][0].replace(/(^\s*)|(\s*$)/g,"");
            if (inner != "") {
            	var personArr = inner.split(",");
            	for(var i=0,person; person=personArr[i++];) {
            		var arr = person.split(":");
	                var inInfo = new Object();
	                inInfo.entityId = arr[0];
	                inInfo.entityType = arr[3] == "0"?"user":"dept";
	                inInfo.entityName = arr[2];
	                inInfo.deptOrder = arr[3];
	                data.inParticipantsList.push(inInfo);
            	}
            }

            // 会议议程
            var sdate = self._getDate(meetingInfo.beginTime);
            var edate = self._getDate(meetingInfo.endTime);
            var errorFlag = false;
            var agendaData=form['NID-AgendaStart'];
            if(agendaData && agendaData.length>0){
                data.agendaFlag=1;
                for(var i=0;i<agendaData.length;i++){
                    var agendaInfo = new Object();
                    agendaInfo.beginTime = form['NID-AgendaStart'][i]+":00";
                    agendaInfo.endTime = form['NID-AgendaEnd'][i]+":00";
                    agendaInfo.address = form['NID-AgendaAddress'][i];
                    agendaInfo.remarks = form['NID-AgendaDes'][i];
                    data.agendaList.push(agendaInfo);
                    
                    var agendaObj = $(this.$el.find("#ID-MeetAdd-GroupAgenda").find(".SID-DynamicOptions")[i]);
                    if (sdate > self._getDate(agendaInfo.beginTime) || edate < self._getDate(agendaInfo.beginTime)) {
                    	agendaObj.find(".SID-DateTimeStart").parent().addClass("inputbox-error");
                    	//$(div).find(".SID-agenda-start").parent().addClass("app-active-error");
                        errorFlag = true;
                	}
                	if (sdate > self._getDate(agendaInfo.endTime) || edate < self._getDate(agendaInfo.endTime)) {
                		agendaObj.find(".SID-DateTimeEnd").parent().addClass("inputbox-error");
                		//$(div).find(".SID-agenda-end").parent().addClass("app-active-error");
                        errorFlag = true;
                	}
                	if (errorFlag) {
                    	app.toast.setText("议程时间超出会议时间范围");
                        app.toast.show();
                    	return;
                    }
                	agendaObj.find(".SID-DateTimeEnd").parent().removeClass("inputbox-error");
                }
                
            }

            // 会议签到
            var signData=form['NID-SignSequ'];
            if(signData && signData.length>0){
                data.signinFlag=1;
                for(var i=0;i<signData.length;i++){
                    var signInfo = new Object();
                    signInfo.remarks = form['NID-SignDes'][i];
                    signInfo.sequ = i + 1;
                    data.signinSequList.push(signInfo);
                }
            }

            // 会议备注
            var remarksData=form['NID-Remarks'];
            if(remarksData && remarksData.length>0){
                data.remarksFlag=1;
                for(var i=0;i<remarksData.length;i++){
                    var remarksInfo = new Object();
                    remarksInfo.remarks = remarksData[i].replace(/(^\s*)|(\s*$)/g,"");
                    data.remarksList.push(remarksInfo);
                }
            }

            //创建群组
            data.createGroupFlag=$("[name='NID-IsCreateGroup']").val();
            //功能开启，无添加
            if($("#ID-MeetAdd-SwitchAgenda").hasClass("active") && $("#ID-MeetAdd-GroupAgenda .SID-DynamicOptions").html()==""){
                app.toast.setText("请添加会议议程");
                app.toast.show();
                return;
            }
            if($("#ID-MeetAdd-SwitchSign").hasClass("active") && $("#ID-MeetAdd-GroupSign .SID-DynamicOptions").html()==""){
                app.toast.setText("请添加会议签到");
                app.toast.show();
                return;
            }
            if($("#ID-MeetAdd-SwitchRemark").hasClass("active") && $("#ID-MeetAdd-GroupRemark .SID-DynamicOptions").html()==""){
                app.toast.setText("请添加会议备注");
                app.toast.show();
                return;
            }
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.checkCapacity+"&sessionId="+window.token;
            var param = JSON.stringify(data);
            var pageParam = "&meetingJson="+param+"&lockId=-1";
            app.loading.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
				success : function(res) {
					if(res.code=="900001") {
						self.confirm1.setText(res.message);
						self.confirm1.setOnClickOk(function(e){
			            	e.hide();
			                self._onSubmitAfter(data);
			            });
			            self.confirm1.show();
					} else if (res.code =='300001') {
						app.toast.setText(res.message);
						app.toast.show();
					} else if (res.code =='300007') {
						app.toast.setText("会议室已被预定");
						app.toast.show();
					} else if (res.code =='300006') {
						app.toast.setText("预定时间无效");
						app.toast.show();
					}  else if (res.code =='300018') {
						app.toast.setText("创建群组失败");
						app.toast.show();
					} else if (res.code == '1') {
						self._onSubmitAfter(data);
					}
				},
				error : function(){
					app.toast.setText("发布会议失败");
					app.toast.show();
				},
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
			});
        },
        _onSubmitAfter:function(data){
        	var self = this;
			var url = app.serviceUrl;
			var lockParam = app.ropMethod.lockReserved+"&sessionId="+window.token 
						+ "&roomId="+data.roomId 
						+ "&timeStart="+data.meeting.beginTime 
						+ "&timeEnd="+data.meeting.endTime;
            app.loading.show();
			$.ajax({
				type : "POST",
				url : url,
				data : lockParam,
				success : function(res) {
					if (res.code == "1") {
						var lockId = res.lockId;
			            var ropParam = app.ropMethod.meetAdd+"&sessionId="+window.token;
			            var param = JSON.stringify(data);
			            var pageParam = "&meetingJson="+param+"&lockId="+lockId;
			            app.loading.show();
						$.ajax({
							type : "POST",
							url : url,
							data : ropParam + pageParam,
							success : function(data) {
								if(data.code=="1") {
									//self.$el.find(".SID-BtnWriteSubmit").hide();
									if (self.needApprove == "1") {
										app.toast.setText("已提交管理员审批！");
										app.toast.show();
									} else {
										app.toast.setText("会议发布成功！");
										app.toast.show();
									}
									app.routerViews.indexView.refresh();
									setTimeout(function(){
										self.destroy();
							            history.go(-2);
						            },1000);
								} else if (data.code =='300001') {
									app.toast.setText(data.message);
									app.toast.show();
								} else if (data.code =='300007') {
									app.toast.setText("会议室已被预定");
									app.toast.show();
								} else if (data.code =='300006') {
									app.toast.setText("预定时间无效");
									app.toast.show();
								}  else if (data.code =='300018') {
									app.toast.setText("创建群组失败");
									app.toast.show();
								} else {
									app.toast.setText("发布会议失败");
									app.toast.show();
								}
							},
							error : function(){
								app.toast.setText("发布会议失败");
								app.toast.show();
							},
			                complete:function(){
			                    setTimeout(function(){
			                        app.loading.hide();
			                    }, 300);
			                }
						});
					} else if (res.code =='300005') {
						app.toast.setText("时间段已被占用");
						app.toast.show();
		                app.loading.hide();
					} else {
						app.toast.setText("发布会议失败");
						app.toast.show();
		                app.loading.hide();
					}
		        },
				error : function(){
					app.toast.setText("发布会议失败");
					app.toast.show();
	                app.loading.hide();
				},
		        complete:function(){
		        }
			});
            //e.preventDefault();
        },
        //会议室预定
        _onClickMeetAddRoom:function(e){
            var hash="#"+app.pages.room;
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        //树结构选择
        _onClickMeetAddTree:function(e){
        	var val = $("[name='NID-MeetAdd-People']").val();
        	if (val == "") {
        		val = "-1";
        	}
            var hash="#"+app.pages.tree+"/"+val;
            app.router.navigate(hash, { trigger : true, replace : false });
        }
    });

    return view;
});
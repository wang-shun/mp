define([
    "app",

    "text!../../templates/meet/meetAddTemplate.html",
    "text!../../templates/meet/add/meetAddAgendaTemplate.html",
    "text!../../templates/meet/add/meetAddRemarkTemplate.html",
    "text!../../templates/meet/add/meetAddSignTemplate.html",
    "text!../../templates/attach/attachActiveListTemplate.html"
], function(app, meetAddTemplate, meetAddAgendaTemplate, meetAddRemarkTemplate, meetAddSignTemplate,attachActiveListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(mergeFlag){
            this.mergeFlag=mergeFlag;
            var self = this;
            //渲染页面
            var html=_.template(
            meetAddTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
            this.loadPanel.find("header").remove();
            this.loadPanel.css({"top":"49px"});
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
            app.formControls.update();

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
            this.$el.find("[name='NID-MeetAdd-PeopleName']").val(app.loginUser.username);
            this.$el.find("[name='NID-MeetAdd-People']").val(app.loginUser.loginId+":1:"+app.loginUser.username+":0:"+app.loginUser.deptId);
        	/*if (NativeObj) {
            	var phoneType = NativeObj.getPhoneType();
        		// 安卓
        		if (phoneType == "0") {
        			mplus.setBackListener({
        			    active: '1', // 是否要监听返回键 0 否 1是 ，如若监听返回键，则ARK不再处理返回键事件，	        // 如需ARK处理，需解除监听
        			});
        	        document.addEventListener("backpressed", self._CallBack, false);
        		}
            }*/
            console.log(this.mergeFlag);
            if(this.mergeFlag!="0"){
                this.$el.find(".titlebar-title").html("编辑会议");
                this._loadData();
            }else{
                this.$el.find(".titlebar-title").html("新建会议");
            }
        },
        _CallBack:function (){
        	var hash = window.location.hash;
        	var page=hash.indexOf("/")>=0?hash.substring(0,hash.indexOf("/")):hash;
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
        	/*if (NativeObj) {
            	var phoneType = NativeObj.getPhoneType();
        		// 安卓
        		if (phoneType == "0") {
        			mplus.setBackListener({
        			    active: '0', // 是否要监听返回键 0 否 1是 ，如若监听返回键，则ARK不再处理返回键事件，	        // 如需ARK处理，需解除监听
        			});
        	        document.removeEventListener("backpressed", self._CallBack, false);
        		}
            }*/
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
        _loadData:function(){
            var data=app.mergeMeetingData;
            var mergeData={};
            //基本信息
            mergeData.meetName=data.meeting.meetingName;
            $("[name='NID-MeetName']").val(mergeData.meetName);
            $("[name='NID-MeetName']").attr("readonly","true");
            mergeData.startTime=data.meeting.beginTimeStr;
            $("[name='NID-MeetBegin']").val(mergeData.startTime);
            mergeData.endTime=data.meeting.endTimeStr;
            $("[name='NID-MeetEnd']").val(mergeData.endTime);
            mergeData.address=data.meeting.address;
            $("[name='NID-MeetAddress']").val(mergeData.address);

            //参会人员与服务人员
            var personName="";
            var personHidden="";
            var serviceName="";
            var serviceHidden="";
            for(var i=0,person;person=data.originParticipantsList[i++];){
                var type=person.entityType=="user"?"1":"0";
                if(person.type=="inner"){
                    personName+=person.entityName+",";
                    personHidden+=person.entityId+":"+type+":"+person.entityName+":"+person.deptOrder+":"+person.parentId+",";
                }else if(person.type=="service"){
                    serviceName+=person.entityName+",";
                    serviceHidden+=person.entityId+":"+type+":"+person.entityName+":"+person.deptOrder+":"+person.parentId+",";
                }
            }
            if(personName!="")personName=personName.substr(0,personName.length-1);
            if(personHidden!="")personHidden=personHidden.substr(0,personHidden.length-1);
            if(serviceName!="")serviceName=serviceName.substr(0,serviceName.length-1);
            if(serviceHidden!="")serviceHidden=serviceHidden.substr(0,serviceHidden.length-1);
            mergeData.personName=personName;
            $("[name='NID-MeetAdd-PeopleName']").val(mergeData.personName);
            mergeData.personHidden=personHidden;
            $("[name='NID-MeetAdd-People']").val(mergeData.personHidden);
            mergeData.serviceName=serviceName;
            $("[name='NID-MeetAdd-ServicePeopleName']").val(mergeData.serviceName);
            mergeData.serviceHidden=serviceHidden;
            $("[name='NID-MeetAdd-ServicePeople']").val(mergeData.serviceHidden);

            //会议议程
            mergeData.agendas=data.agendasInfo;
            if(mergeData.agendas.length>0){
                //注入内容
                for(var i=0,agenda;agenda=mergeData.agendas[i++];){
                    $("#ID-MeetAdd-GroupAgenda .SID-DynamicOptions").append(meetAddAgendaTemplate);
                    $("#ID-MeetAdd-GroupAgenda .SID-DynamicOptions > div").eq(i-1).find("[name='NID-AgendaStart']").val(new Date(agenda.beginTime).format("yyyy-MM-dd hh:mm"));
                    $("#ID-MeetAdd-GroupAgenda .SID-DynamicOptions > div").eq(i-1).find("[name='NID-AgendaEnd']").val(new Date(agenda.endTime).format("yyyy-MM-dd hh:mm"));
                    $("#ID-MeetAdd-GroupAgenda .SID-DynamicOptions > div").eq(i-1).find("[name='NID-AgendaAddress']").val(agenda.address);
                    $("#ID-MeetAdd-GroupAgenda .SID-DynamicOptions > div").eq(i-1).find("[name='NID-AgendaDes']").val(agenda.remarks);
                }
                app.formControls.update();
                //显示内容
                $("#ID-MeetAdd-GroupAgenda .SID-SwitchOptions").css("display","block");
                //激活开关
                $("#ID-MeetAdd-GroupAgenda .sliver-title").addClass("active");
                $("#ID-MeetAdd-GroupAgenda .switch").addClass("active");
            }

            //附件资料
            mergeData.attachs=data.attachmentList;
            if(mergeData.attachs.length>0){
                //注入内容
                for(var i=0,attach;attach=mergeData.attachs[i++];){
                    app.activeAttachData[i-1]={};
                    app.activeAttachData[i-1].contentType=attach.contentType;
                    app.activeAttachData[i-1].fileName=attach.fileName;
                    app.activeAttachData[i-1].filePath=attach.filePath;
                    app.activeAttachData[i-1].size=attach.size;
                    app.activeAttachData[i-1].uploadTime=attach.uploadTime;
                }
                var html = _.template(
                    attachActiveListTemplate, {
                    'imgPath': app.constants.IMAGEPATH,
                    'attach' : app.activeAttachData
                });
                $("#ID-MeetAdd-DivAttach").html(html);
                app.formControls.update();
                //显示内容
                $("#ID-MeetAdd-GroupAttach .SID-SwitchOptions").css("display","block");
                //激活开关
                $("#ID-MeetAdd-GroupAttach .sliver-title").addClass("active");
                $("#ID-MeetAdd-GroupAttach .switch").addClass("active");
            }

            //会议签到
            mergeData.signList=data.signinSequList;
            mergeData.signType=data.meeting.signType;
            if(mergeData.signList.length>0){
                //注入内容
                if(mergeData.signType=="1"){
                    $("#ID-MeetAdd-TurnSign").addClass("active");
                    $("[name='NID-SignMode']").val("1");
                    $("#ID-SignModeDescription").html("召开人/服务员扫描参会者个人二维码签到。");
                }
                
                for(var i=0,sign;sign=mergeData.signList[i++];){
                    $("#ID-MeetAdd-GroupSign .SID-DynamicOptions").append(meetAddSignTemplate);
                    $("#ID-MeetAdd-GroupSign .SID-DynamicOptions > div").eq(i-1).find("[name='NID-SignDes']").val(sign.remarks);
                }
                this._sortLblSign();
                app.formControls.update();
                //显示内容
                $("#ID-MeetAdd-GroupSign .SID-SwitchOptions").css("display","block");
                //激活开关
                $("#ID-MeetAdd-GroupSign .sliver-title").addClass("active");
                $("#ID-MeetAdd-GroupSign .switch").addClass("active");
            }
            
            //会议提醒
            mergeData.alarmKey=data.meeting.noticeType;//0-未设置 1-分钟  2-小时
            mergeData.alarmValue=data.meeting.noticeSet;
            if(mergeData.alarmKey!="0"){
                $("#ID-MeetAdd-GroupAlarm [name='NID-Alarm']").removeClass("form-ignore");
                $("#ID-MeetAdd-GroupAlarm [name='NID-AlarmUnit']").removeClass("form-ignore");
                //注入内容
                $("#ID-MeetAdd-GroupAlarm [name='NID-Alarm']").val(mergeData.alarmValue);
                if(mergeData.alarmKey=="2"){
                    $("#ID-MeetAdd-GroupAlarm .turn").addClass("active");
                    $("#ID-MeetAdd-GroupAlarm [name='NID-AlarmUnit']").val("60");
                }
                //显示内容
                $("#ID-MeetAdd-GroupAlarm .SID-SwitchOptions").css("display","block");
                //激活开关
                $("#ID-MeetAdd-GroupAlarm .sliver-title").addClass("active");
                $("#ID-MeetAdd-GroupAlarm .switch").addClass("active");
            }
            //会议备注
            mergeData.remarks=data.remarksList;
            if(mergeData.remarks.length>0){
                //注入内容
                for(var i=0,remark;remark=mergeData.remarks[i++];){
                    $("#ID-MeetAdd-GroupRemark .SID-DynamicOptions").append(meetAddRemarkTemplate);
                    $("#ID-MeetAdd-GroupRemark .SID-DynamicOptions > div").eq(i-1).find("[name='NID-Remarks']").val(remark.remarks);
                }
                app.formControls.update();
                //显示内容
                $("#ID-MeetAdd-GroupRemark .SID-SwitchOptions").css("display","block");
                //激活开关
                $("#ID-MeetAdd-GroupRemark .sliver-title").addClass("active");
                $("#ID-MeetAdd-GroupRemark .switch").addClass("active");
            }

            //创建讨论组
            mergeData.isGroup=data.meeting.hasGroup;
            $("[name='NID-IsCreateGroup']").val(mergeData.isGroup);
            $("#ID-MeetAdd-GroupCreate").hide();
        },
        //取消确认删除
        _reConfirmDel:function(){
            $(".SID-BtnDel").html("删除").removeClass("active");
            $(".SID-IconDelConfirm").removeClass("active");
        },
        //签到号码排序
        _sortLblSign:function(){
            var lblSigns=document.querySelectorAll(".SID-LblSign");
            for(var i=0,lbl;lbl=lblSigns[i++];){
                lbl.value="第"+i+"次签到";
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
                            app.toast.setText("结束时间过早");
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
            //基本信息
            $('[name="NID-MeetName"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'会议名称不能为空'
                    },
                    {
                        rule:'maxLength:30',
                        errorMsg:'会议名称不能超过30位'
                    }
                ]);
            });
            //会议基本信息
            $('[name="NID-MeetBegin"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'会议开始时间不能为空'
                    }
                ]);
            });
            $('[name="NID-MeetEnd"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'会议结束时间不能为空'
                    }
                ]);
            });
            $('[name="NID-MeetAddress"]').each(function(i,n){
                validator.add(n,[
                    {
                        rule:'required',
                        errorMsg:'会议地址不能为空'
                    },
                    {
                        rule:'maxLength:30',
                        errorMsg:'会议地址不能超过30位'
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
                            rule:'maxLength:30',
                            errorMsg:'签到说明不能超过30位'
                        }
                    ]);
                }
            });
            //会议提醒
            $('[name="NID-Alarm"]').each(function(i,n){
                if(!n.classList.contains("form-ignore")){
                	if($('[name="NID-AlarmUnit"]').val()=="1"){
                		validator.add(n,[
	                        {
	                            rule:'required',
	                            errorMsg:'会议提醒不能为空'
	                        },
	                        {
	                            rule:'minNumber:5',
	                            errorMsg:'提醒时间不能小于5分钟'
	                        }
	                    ]);
                	}else{
                		validator.add(n,[
	                        {
	                            rule:'required',
	                            errorMsg:'会议提醒不能为空'
	                        },
	                        {
	                            rule:'minNumber:1',
	                            errorMsg:'提醒时间不合法'
	                        }
	                    ]);
                	}
                }
            });
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
            'click #ID-MeetAdd-BtnAddAttach' : '_onClickBtnAddAttach',
            'click #ID-MeetAdd-BtnAddSign' : '_onClickBtnAddSign',
            'click #ID-MeetAdd-BtnAddRemark' : '_onClickBtnAddRemark',

            'click #ID-MeetAdd-SwitchAgenda' : '_onClickSwitchAgenda',
            'click #ID-MeetAdd-SwitchAttach' : '_onClickSwitchAttach',
            'click #ID-MeetAdd-SwitchSign' : '_onClickSwitchSign',
            'click #ID-MeetAdd-TurnSign' : '_onClickTurnSign',
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
            'input [name="NID-MeetAddress"]' : '_onChangeMeetAddress',
            'input [name="NID-AgendaDes"]' : '_onChangeAgendaDes',
            'input [name="NID-SignDes"]' : '_onChangeSignDes',
            'input [name="NID-Remarks"]' : '_onChangeRemarks',

            //提交表单
            'click #ID-MeetAdd-BtnSubmit' : '_onSubmit',

            //会议室预定
            'click .SID-MeetAdd-Room' : '_onClickMeetAddRoom',

            //参会人员
            'click #ID-MeetAdd-Tree' : '_onClickMeetAddTree',
            //服务人员
            'click #ID-MeetAdd-ServiceTree' : '_onClickMeetAddServiceTree',
        },

        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(e){
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
        _onClickBtnAddAttach:function(e){
            var hash="#"+app.pages.attach;//附件资料页面
            app.router.navigate(hash, { trigger : true, replace : false });
            console.log("添加附件资料");
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
        //删除选中附件
        _removeActiveAttachById:function(id){
            for(var i=0;i<app.activeAttachData.length;i++){
                if (app.activeAttachData[i].filePath == id) {
                    app.activeAttachData.splice(i,1);
                    var attachLi=document.querySelector(".SID-AttachList-Cbo[data-path='"+id+"']");
                    if(attachLi)attachLi.checked=false;
                    break;
                }
            }
        },
        _onClickIconDelConfirm:function(e){
            var self=this;
            var inputLine=e.target.parentNode;
            //删除附件列表
            if(inputLine.classList.contains("SID-AttachActiveList-Li")){
                this._removeActiveAttachById(inputLine.getAttribute("data-id"));
            }
            $(inputLine).slideUp("200",function(){
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
        _onClickSwitchAttach:function(e){
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
                //开关时修改描述文字
                $("#ID-SignModeDescription").html("参会者扫描召开人/服务员的会议二维码签到。");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
                //收缩
                opts.find(".input-text,.input-pre").addClass("form-ignore");
                opts.slideUp("200");
                //开关时修改描述文字
                $("#ID-SignModeDescription").html("召开人/服务员扫描参会者个人二维码签到。");
            }

        },
        _onClickTurnSign:function(e){
            //关闭时忽略元素
            if(e.currentTarget.classList.contains("active")){
                //开关时修改描述文字
                $("#ID-SignModeDescription").html("召开人/服务员扫描参会者个人二维码签到。");
            }else{
                //开关时修改描述文字
                $("#ID-SignModeDescription").html("参会者扫描召开人/服务员的会议二维码签到。");
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
                    rule:'maxLength:30',
                    errorMsg:'会议名称不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                e.target.value=e.target.value.substring(0,30);
            }
        },
        _onChangeMeetAddress:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:30',
                    errorMsg:'会议地址不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                e.target.value=e.target.value.substring(0,30);
            }
        },
        _onChangeAgendaAddress:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:30',
                    errorMsg:'议程地址不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                e.target.value=e.target.value.substring(0,30);
            }
        },
        _onChangeAgendaDes:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:50',
                    errorMsg:'议程说明不能超过50位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                e.target.value=e.target.value.substring(0,50);
            }
        },
        _onChangeSignDes:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:30',
                    errorMsg:'签到说明不能超过30位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                e.target.value=e.target.value.substring(0,30);
            }
        },
        _onChangeRemarks:function(e){
            var validator=new Validator();
            validator.add(e.target,[
                {
                    rule:'maxLength:50',
                    errorMsg:'备注说明不能超过50位'
                }
            ]);
            var error=validator.start();
            if(error){
                app.toast.setText(error.msg);
                app.toast.show();
                e.target.value=e.target.value.substring(0,50);
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
            if(alarmData && alarmData.length>0){
                var now=new Date();
                var startTime=new Date(form['NID-MeetBegin'][0].replace(/-/g,"/"));
                var diffMinute=now.diff(startTime).minutesAll;
                var alarmMinute=form['NID-AlarmUnit'][0]*alarmData[0];
                if(diffMinute<alarmMinute){
                    app.toast.setText("无效的提醒时间，请重新设置");
                    app.toast.show();
                    $("[name='NID-Alarm']").parent().parent().addClass("inputbox-error");
                    return;
                }
            }
            $("[name='NID-Alarm']").parent().parent().removeClass("inputbox-error");
            //提交的参数
            var agendaFlag = $("#ID-MeetAdd-SwitchAgenda").hasClass("active")?"1":"0";
            var attachmentFlag =  $("#ID-MeetAdd-SwitchAttach").hasClass("active")?"1":"0";;
            var signinFlag = $("#ID-MeetAdd-SwitchSign").hasClass("active")?"1":"0";
            var alarmFlag = $("#ID-MeetAdd-SwitchAlarm").hasClass("active")?"1":"0";
            var remarksFlag = $("#ID-MeetAdd-SwitchRemark").hasClass("active")?"1":"0";
            var createGroupFlag = $("#ID-MeetAdd-SwitchCreate").hasClass("active")?"1":"0";
            var data = {
            		agendaFlag : agendaFlag,//会议议程
            		attachmentFlag : attachmentFlag,//附件
            		signinFlag : signinFlag,//会议签到
            		alarmFlag : alarmFlag,//会议提醒
            		remarksFlag : remarksFlag,//会议备注
            		createGroupFlag : createGroupFlag,//创建会议
            		signinMode:0,//签到模式
            		meeting : null,
            		inParticipantsList : [],
            		outParticipantsList : [],
            		serviceParticipantsList : [],
            		agendaList : [],
            		attachmentList : [],
            		signinSequList : [],
            		remarksList : []
            };
            // 会议主信息
            var meetingInfo = new Object();
            meetingInfo.name = form['NID-MeetName'][0].replace(/(^\s*)|(\s*$)/g,"");
            meetingInfo.address = form['NID-MeetAddress'][0].replace(/(^\s*)|(\s*$)/g,"");
            meetingInfo.beginTime = form['NID-MeetBegin'][0]+":00";
            meetingInfo.endTime = form['NID-MeetEnd'][0]+":00";
            meetingInfo.signType = $("[name='NID-SignMode']").val();
            // 会议提醒
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
                    inInfo.parentId = arr[4];
	                data.inParticipantsList.push(inInfo);
            	}
            }
            // 服务人员
            var service = form['NID-MeetAdd-ServicePeople'][0].replace(/(^\s*)|(\s*$)/g,"");
            if (service != "") {
            	var personArr = service.split(",");
            	for(var i=0,person; person=personArr[i++];) {
            		var arr = person.split(":");
	                var inInfo = new Object();
	                inInfo.entityId = arr[0];
	                inInfo.entityType = arr[3] == "0"?"user":"dept";
	                inInfo.entityName = arr[2];
	                inInfo.deptOrder = arr[3];
                    inInfo.parentId = arr[4];
	                data.serviceParticipantsList.push(inInfo);
            	}
            }

            // 会议议程
            if ("1" == agendaFlag) {
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
	            } else {
	            	app.toast.setText("请添加议程");
					app.toast.show();
					return;
	            }
            }
            // 附件
            if ("1" == attachmentFlag) {
	        	var attachDivs = this.$el.find(".SID-AttachActiveList-Li");
	        	if (attachDivs.length<1){
	            	app.toast.setText("请添加附件");
					app.toast.show();
					return;
	            }
	        	for(var i =0,div;div=attachDivs[i++];){
	        		var valObj = $(div);
	        		var attachInfo = new Object();
	        		attachInfo.filePath = valObj.attr("data-id");
	        		attachInfo.fileName = valObj.attr("data-name");
	        		attachInfo.contentType = valObj.attr("data-type");
	        		attachInfo.size = valObj.attr("data-size");
	        		attachInfo.uploadTime = valObj.attr('data-uploadTime');
	        		attachInfo.privilege = "3";//$(div).find("input[type='radio']:checked").val();
	        		data.attachmentList.push(attachInfo);
	        	}
            }
            // 签到模式
            if ("1" == signinFlag) {
	            data.signinMode=$("[name='NID-SignMode']").val();
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
            }
            // 会议备注
            if ("1" == remarksFlag) {
	            var remarksData=form['NID-Remarks'];
	            if(remarksData && remarksData.length>0){
	                data.remarksFlag=1;
	                for(var i=0;i<remarksData.length;i++){
	                    var remarksInfo = new Object();
	                    remarksInfo.remarks = remarksData[i].replace(/(^\s*)|(\s*$)/g,"");
	                    data.remarksList.push(remarksInfo);
	                }
	            }
            }
            //创建群组
            data.createGroupFlag=$("[name='NID-IsCreateGroup']").val();
            //功能开启，无添加时报错
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

            var ropParam = app.ropMethod.meetAdd;
            //更新
            if(this.mergeFlag!="0"){
                data.signinPersonList=app.mergeMeetingData.signinPersonList;
                data.meeting.id=app.mergeMeetingData.meeting.meetingId;
                data.meeting.groupId=app.mergeMeetingData.meeting.groupId;
                ropParam = app.ropMethod.meetEdit;
            }
			var url = window.serviceUrl;
            console.log(data);
            var param = JSON.stringify(data);
            
            var pageParam = "&meetingJson="+param;
            self.loadPanel.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
				success : function(data) {
                    console.log("成功"+this.code);
					if(data.code=="1") {
						//self.$el.find(".SID-BtnWriteSubmit").hide();
						app.toast.setText("会议发布成功！");
						app.toast.show();
                        if(self.mergeFlag=="0"){
                            app.routerViews.indexView.refresh();
                        }else{
                            app.routerViews.indexView.refresh();
                            app.routerViews.meetDetailView.refresh(app.mergeMeetingData.meeting.meetingId);
                        }
						setTimeout(function(){
                            self.destroy();
                            history.go(-1);
                        },1000);
					} else if (data.code =='300001') {
						app.toast.setText(data.message);
						app.toast.show();
					} else if (data.code =='300007') {
						app.toast.setText("操作期间该会议室已被预定!");
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
                    self.loadPanel.hide();
					app.toast.setText("发布会议失败");
					app.toast.show();
				},
                complete:function(){
                    /*setTimeout(function(){
                        self.loadPanel.hide();
                    }, 300);*/
                }
			});
            //e.preventDefault();
        },
        //会议室预定
        _onClickMeetAddRoom:function(e){
            var hash="#"+app.pages.room;
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        //参会人员选择
        _onClickMeetAddTree:function(e){
        	var val = $("[name='NID-MeetAdd-People']").val();
            app.peopleNameInput="NID-MeetAdd-PeopleName";
            app.peopleInput="NID-MeetAdd-People";
        	if (val == "") {
        		val = "-1";
        	}
            var hash="#"+app.pages.tree+"/"+val;
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        //服务人员选择
        _onClickMeetAddServiceTree:function(e){
            var val = $("[name='NID-MeetAdd-ServicePeople']").val();
            app.peopleNameInput="NID-MeetAdd-ServicePeopleName";
            app.peopleInput="NID-MeetAdd-ServicePeople";
            if (val == "") {
                val = "-1";
            }
            var hash="#"+app.pages.tree+"/"+val;
            app.router.navigate(hash, { trigger : true, replace : false });
        }
    });

    return view;
});
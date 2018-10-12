define([
    "app",
    "text!../templates/add/addTemplate.html",
    "text!../templates/add/addPhotoTemplate.html",
], function(app, addTemplate, addPhotoTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            addTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            this.$el.find("#ID-Add-Phone").val(app.phone);
            //更新表单控件
            app.formControls.update();
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
            //计数控件
            this.countValues=new CountValues({
                onInput:function(e){
                    var input=e.target;
                    input.nextElementSibling.innerHTML=input.value.length+"/1500";
                    //console.log("输入中");
                },
                onInputOut:function(e){
                    //console.log("超过数字限制时");
                },
                onInputIn:function(e){
                    //console.log("没有超过数字限制时");
                }
            });
            //表单
            this.formContainer=document.getElementById("ID-Add-Form");
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
                for(var i=0;i<=23;i++){
                    var temp=i<10?"0"+i:i;
                    startData.push({key:temp,value:temp+"时"});
                }
                var endData=startData.slice(0);
                //endData.push({key:"23",value:"23时"});
                return {
                    startHours:startData,
                    endHours:endData
                };
            }();
            //退出应用弹出框
            this.popConfirm=new Alert("放弃编辑此页面？",{
                onClickCancel:function(e){
                    e.hide();
                }
            });

            this.gridContainer=document.getElementById("ID-Grid");
            //图片上传
            this.picImage=new Mplus.Image({
                onChooseSuccess:function(res){//确定选择
                    if(res.target==="1"){
                        self._addPoster(res);
                    }else if(res.target=="2"){
                        self._addPhoto(res);
                    }
                },
                onChooseError:function(res){//取消选择
                },
                onUploadSuccess:function(e){//上传成功
                    //渲染到页面
                	if (e.target == "1") {//main
                        self._updatedPoster(e.localId,e.serverId);
                	} else if (e.target == "2") {
                		self._updatedPhoto(e.localId,e.serverId);
                	}
                },
                onUploadsSuccess:function(){//上传完成(指全部图片上传完成)
                },
                onUploadError:function(e){//上传失败
                    app.toast.setText("图片上传失败,请重试");
                    app.toast.show();
                    if(e.target=="1"){
                        self._resetPoster();
                    }else if(e.target=="2"){
                        $(".SID-BtnPhoto:first").parent().remove();
                    }
                    $(".SID-mainImageBtn").removeClass("disabled");
                    $(".SID-BtnUpload").removeClass("disabled");
                },
                onDownloadSuccess:function(e){//下载成功
                },
                onDownloadsSuccess:function(e){//下载完成(指全部图片下载完成)
                	var mainServerId = self.$el.find("#ID-mainImageId").val();
                	var hiddenTxts=document.querySelectorAll(".SID-TxtPhoto");
                    //提交操作，修改隐藏表单域
                	var downList=e.downObjList;
                	for (var i=0,j=0;i<downList.length;i++) {
                		if (downList[i].serverId==mainServerId) {
                			self.$el.find("#ID-actPhotoUrl").val(downList[i].downloadUrl);
                		} else {
                			hiddenTxts[j].value=downList[i].downloadUrl;
                			j++;
                		}
                	}
                    //提交
                	self._afterSubmit();
                },
                onDownloadError:function(res){//下载失败
                    app.toast.setText("图片上载失败,请重试");
                    app.toast.show();
                    self.loadPanel.hide();
                }
            });
            //返回控制
            Mplus.BackMonitor.addHandler(function(){
                self._CallBack();
            });
        },
        _addPoster:function(){
            $(".SID-mainImageBtn").addClass("disabled");
            $(".SID-BtnUpload").addClass("disabled");

            $("#ID-Add-PosterNone").addClass("hide");
            $("#ID-Add-PosterExist").addClass("hide");
            $("#ID-mainImageId").val("");
            $("#ID-Add-PosterLoad").removeClass("hide");
        },
        _resetPoster:function(){
            $("#ID-Add-PosterNone").removeClass("hide");
            $("#ID-Add-PosterLoad").addClass("hide");
            $("#ID-Add-PosterExist").css({"background-image":""});
            $("#ID-Add-PosterExist").addClass("hide");
            $("#ID-mainImageId").val("");
        },
        _updatedPoster:function(url,serverid){
            $("#ID-Add-PosterNone").addClass("hide");
            $("#ID-Add-PosterLoad").addClass("hide");

            $("#ID-Add-PosterExist").css({"background-image":"url("+url+")"});
            $("#ID-Add-PosterExist").removeClass("hide");
            $("#ID-mainImageId").val(serverid);

            $(".SID-mainImageBtn").removeClass("disabled");
            $(".SID-BtnUpload").removeClass("disabled");
        },
        _addPhoto:function(res){
            $(".SID-mainImageBtn").addClass("disabled");
            $(".SID-BtnUpload").addClass("disabled");

            for(var i=0,id;id=res.localIds[i++];){
                //渲染页面
                var html=_.template(
                addPhotoTemplate,{
                    url:id,
                    imgPath: app.constants.IMAGEPATH
                });
                $(this.gridContainer).prepend(html);
            }
        },
        _updatedPhoto:function(url,serverid){
        	var photoList = $(".SID-BtnPhoto");
        	var fillIndex = 0;
        	for(var r=0;r<photoList.length;r++){
        		var curphotoList = $(".SID-BtnPhoto");
        		if($(curphotoList[r]).attr("data-serverid") == undefined){
        			fillIndex = r;
        			break;
        		}
        	}
        	$(photoList[fillIndex]).css({"background-image":"url("+url+")"}).attr("data-serverid",serverid).find(".SID-PhotoLoading").hide();
        	$(photoList[fillIndex]).find(".SID-BtnDelImage").removeClass("hide");
//            $(".SID-BtnPhoto:first").css({"background-image":"url("+url+")"}).attr("data-serverid",serverid).find(".SID-PhotoLoading").hide();
//            $(".SID-BtnPhoto:first").find(".SID-BtnDelImage").removeClass("hide");

            $(".SID-mainImageBtn").removeClass("disabled");
            $(".SID-BtnUpload").removeClass("disabled");
        },
        refresh:function(){
            console.log("发布活动addView：刷新");
            //this.render();
        },
        destroy:function(){
            var self=this;
        	Mplus.BackMonitor.removeHandler(function(){
                self._CallBack();
            });
        	self.loadPanel.hide();
            console.log("发布活动addView：移除");
            this.undelegateEvents();
            this.unbind();
            setTimeout(function() {
                self.$el.empty();
            }, 300);
        },
        /*=========================
          Method
          ===========================*/
        //设置截止日期
        _setOffTime:function(timeText){
            var self=this;
            var offTime=self.$el.find(".SID-DateTimeOff").val();
            if(!offTime){
                self.$el.find(".SID-DateTimeOff").val(timeText);
            }

            var startTime=new Date(timeText.replace(/-/g,"/"));
            offTime=new Date(offTime.replace(/-/g,"/"));
            if(offTime>startTime){
                self.$el.find(".SID-DateTimeOff").val(timeText);
            }
        },
        //时间控件-动态更新分钟
        _newSpMinutes:function(hourKey,minuteKey){
            var self=this;
            var minuteKey=minuteKey;
            var minutes=this.minutesData.slice(0);
            if(hourKey==="24"){
                minutes=[{key:"00",value:"00分"}];
                minuteKey="00";
            }else if(hourKey==="00"){
                minutes.shift();
            }
            self.spDateTime.scrollpicker.replaceSlot(4,minutes,minuteKey,'text-center');//修改第五项
        },
        //时间控件
        _newSpDateTime:function(defaults,input){
            var self=this;
            this.spDateTime=new SpDate({
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

                        var endInput=input.parentNode.parentNode.nextElementSibling.querySelector(".SID-DateTimeEnd");
                        if(endInput){
                            //如果结束时间为空或者开始时间大于结束时间
                            var endInputDate=new Date(endInput.value.replace(/-/g,"/"));
                            if((endInput.value=="") || (activeDate >= endInputDate)){
                                //设置结束时间
                                var endTime=new Date(activeDate).plusMinute(10);
                                endInput.value=endTime.format("yyyy-MM-dd hh:mm");
                            }
                        }
                        self._setOffTime(e.activeText);
                    //点击结束时间
                    }else if(input.classList.contains("SID-DateTimeEnd")){
                        //不合法的时间
                        if(new Date(activeDate)<=new Date().setMinuteCeil(10)){
                            app.toast.setText("结束时间过早");
                            app.toast.show();
                            return;
                        }

                        var prevInput=input.parentNode.parentNode.previousElementSibling.querySelector(".SID-DateTimeStart");
                        if(prevInput){
                            //如果开始时间为空或者结束时间小于开始时间
                            var prevInputDate=new Date(prevInput.value.replace(/-/g,"/"));
                            if((prevInput.value=="") || (activeDate <= prevInputDate)){
                                //设置开始时间
                                var startTime=activeDate.minusMinute(10);
                                prevInput.value=startTime.format("yyyy-MM-dd hh:mm");
                                self._setOffTime(startTime.format("yyyy-MM-dd hh:mm"));
                            }
                        }
                    //点击截止时间
                    }else if(input.classList.contains("SID-DateTimeOff")){
                        //不合法的时间
                        if(activeDate<=new Date()){
                            app.toast.setText("截止时间过早");
                            app.toast.show();
                            return;
                        }
                        if(activeDate>new Date(self.$el.find(".SID-DateTimeStart").val().replace(/-/g,"/"))){
                            app.toast.setText("截止时间不能晚于开始时间");
                            app.toast.show();
                            return;
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
                this.spDateTime.setDefaultYear(defaults[0]);
                this.spDateTime.setDefaultMonth(defaults[1]);
                this.spDateTime.setDefaultDay(defaults[2]);
                this.spDateTime.setDefaultHour(defaults[3]);
                this.spDateTime.setDefaultMinute(defaults[4]);
                this.spDateTime.update();
            }
        },
        _validate:function(){
            var validator=new Validator();
            validator.add(this.formContainer["NID-Add-Title"],[{
                rule:'required',
                errorMsg:'请输入活动标题'
            }]);
            validator.add(this.formContainer["NID-Add-Node"],[{
                rule:'required',
                errorMsg:'请输入活动详情'
            }]);
            validator.add(this.formContainer["NID-Add-StartTime"],[{
                rule:'required',
                errorMsg:'请选择开始时间'
            }]);
            validator.add(this.formContainer["NID-Add-EndTime"],[{
                rule:'required',
                errorMsg:'请选择结束时间'
            }]);
            validator.add(this.formContainer["NID-latitude"],[{
                rule:'required',
                errorMsg:'请选择地址'
            }]);
            validator.add(this.formContainer["NID-Add-OffTime"],[{
                rule:'required',
                errorMsg:'请选择报名截止时间'
            }]);
            validator.add(this.formContainer["NID-Add-Phone"],[{
                rule:'required',
                errorMsg:'请输入活动咨询电话'
            }]);
            validator.add(this.formContainer["NID-Add-Phone"],[{
                rule:'phone',
                errorMsg:'手机号码不正确'
            }]);
            validator.add(this.formContainer["NID-Add-Count"],[{
            	rule:'minNumber:1',
                errorMsg:'最小人数为1'
            }]);
            validator.add(this.formContainer["NID-Add-Count"],[{
            	rule:'maxNumber:9999',
                errorMsg:'最大人数为9999'
            }]);
//            validator.add(this.formContainer["NID-MeetAdd-People"],[{
//                rule:'required',
//                errorMsg:'请邀请参与人员'
//            }]);
            var error=validator.start();
            return error;
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Back' : '_onBack',
            'click #ID-Add-Submit' : '_onSubmit',
            'click .SID-DivDateTime' : '_onClickDivDateTime',
            'click .SID-DivOffDateTime' : '_onClickDivDateTime',
            'click .SID-Add-Required' : '_onClickRequired',
            'click .SID-mainImageBtn' : '_onClickMainImageBtn',
            'click .SID-BtnDelMain' : '_onClickDelMainImage',
            'click .SID-BtnDelImage':'_onClickDelImage',
            'click .SID-BtnUpload' : '_onClickUploadBtn',
            'click #ID-MeetAdd-SwitchCreate' : '_onClickSwitchCreate',
            //'click #ID-choose-address' : '_onClickChooseAddress',
            //树结构
            'click #ID-MeetAdd-Tree' : '_onClickMeetAddTree'
        },
        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(e){
        	var self = this;
            this.popConfirm.setText("您确定要放弃此次编辑吗？");
            this.popConfirm.setOnClickOk(function(pop){
            	pop.hide();
            	self.destroy();
            	history.go(-1);
            });
            this.popConfirm.show();
        },
        _CallBack:function (){
        	var self=this;
        	self.loadPanel.hide();
        	var hash = window.location.hash;
        	var page=hash.indexOf("/")>=0?hash.substring(0,hash.indexOf("/")):hash;
    		if ("#ID-PageAdd" == page) {
    			app.routerViews.meetAddView._onBack();
    		} else if ("#ID-PageTree" == page) {
    			app.routerViews.treeView._onSubmit();
    		} else {
    			history.go(-1);
    		}
    	},
        _onClickSwitchCreate:function(e){
            console.log("推送开关");
            //开关
            if(e.target.classList.contains("active")){
                //标题选中
                e.target.previousElementSibling.classList.add("active");
            }else{
                //标题去除选中
                e.target.previousElementSibling.classList.remove("active");
            }
        },
        _onClickChooseAddress:function(){
        	var self = this;
            Mplus.chooseLocation(function(data){
                	var latitude = data.latitude; // 纬度，浮点数，范围为90 ~ -90
                    var longitude = data.longitude; // 经度，浮点数，范围为180 ~ -180。
                    var address = data.name||data.address; //地址详情说明
                    if(address == "null"){
                        address = "定位失败";
                        self.$el.find("#ID-address").val("选择地址");
                        self.$el.find("#ID-latitude").val("");
                        self.$el.find("#ID-longitude").val("");
                        return;
                    }
                    if(address == "[位置]"){
                        address = data.address;
                    }
                    self.$el.find("#ID-latitude").val(latitude);
                    self.$el.find("#ID-longitude").val(longitude);
                    self.$el.find("#ID-address").val(address);
                },
                function(res){
                    //app.toast.setText(res.errMsg);
                    //app.toast.show();
                }
            );
        },
        _onClickMainImageBtn:function(e){
            if(e.currentTarget.classList.contains("disabled")){
                app.toast.setText("前图片尚未上传完成");
                app.toast.show();
                return;
            }
            this.picImage.params.max = '1';
        	this.picImage.choose("1");
        },
        _onClickDelMainImage:function(){
        	var self = this;
        	this.popConfirm.setText("您确定要删除活动海报吗？");
            this.popConfirm.setOnClickOk(function(pop){
            	pop.hide();
	    		self._resetPoster();
            });
            this.popConfirm.show();
        },
        _onClickUploadBtn:function(e){
            if(e.currentTarget.classList.contains("disabled")){
                app.toast.setText("前图片尚未上传完成");
                app.toast.show();
                return;
            }
        	var currentPhotoCount=$("#ID-Grid > li").length;
            if(currentPhotoCount>=10){
                app.toast.setText("最多上传9张图片");
                app.toast.show();
                return;
            }
            var validPhotoCount=10-currentPhotoCount;
            this.picImage.params.max = validPhotoCount+"";
            this.picImage.choose("2");
        },
        _onClickDelImage:function(e){
            this.popConfirm.setText("您确定要删除此照片吗？");
            this.popConfirm.setOnClickOk(function(pop){
                pop.hide();
                var target=e.target;
                var delTarget=target.parentNode.parentNode;
                $(delTarget).remove();
            });
            this.popConfirm.show();
        },
        _onClickPhoto:function(e){
            var target=e.target;
            var currentUrl=target.style.backgroundImage;
            currentUrl=currentUrl.substring(5,currentUrl.lastIndexOf(")")-1);
            var urls=[];
            $('[name="NID-Photo"]').each(function(i,n){
                urls.push(n.value);
            });
            mplus.previewImage({
                current: currentUrl, // 当前显示图片的http链接
                urls: urls // 需要预览的图片http链接列表
            });
        },
        _onSubmit:function(e){
            e.preventDefault();
            var self=this;
            //获得所有图片服务器地址
            var downIdList=[];
            var mainId = self.$el.find("#ID-mainImageId").val();
            if (mainId != "") {
            	downIdList.push(self.$el.find("#ID-mainImageId").val());
            }
            $(".SID-BtnPhoto").each(function(i,n){
                var serverid=n.getAttribute("data-serverid");
                downIdList.push(serverid);
            });
            if(downIdList.length>0){
                self.loadPanel.show();
                self.picImage.downAll(downIdList);
            }else{
                self._afterSubmit();
            }
        },
        _checkTime:function(formData){
        	var actStartTime = new Date(formData["NID-Add-StartTime"][0].replace(/-/g,"/"));// 开始时间
        	var actEndTime = new Date(formData["NID-Add-EndTime"][0].replace(/-/g,"/"));// 结束时间
        	var enterEndTime = new Date(formData["NID-Add-OffTime"][0].replace(/-/g,"/"));// 报名截止
            //点击开始时间
            //不合法的时间
            if(actStartTime<=new Date()){
                app.toast.setText("时间不能早于当前时间");
                app.toast.show();
                return false;
            }
            //点击结束时间
            //不合法的时间
            if(actEndTime<=new Date().setMinuteCeil(10)){
                app.toast.setText("结束时间过早");
                app.toast.show();
                return false;
            }
            //点击截止时间
            //不合法的时间
            if(enterEndTime<=new Date()){
                app.toast.setText("截止时间过早");
                app.toast.show();
                return false;
            }
            if(enterEndTime>actStartTime){
                app.toast.setText("截止时间不能晚于开始时间");
                app.toast.show();
                return false;
            }
            return true;
        },
        _afterSubmit:function(){
        	var self = this;
        	self.loadPanel.hide();
            var error=this._validate();
            if(error){
            	self.loadPanel.hide();
                app.toast.setText(error.msg);
                app.toast.show();
                //error.field.parentNode.classList.add("inputbox-error");
                return;
            }
            var formData=new Form("#ID-Add-Form").serializeJson();
            console.log(formData);
            if (!self._checkTime(formData)){
            	return;
            }
            var data={};
            data["actTitle"] = formData["NID-Add-Title"][0];
            data["actPosterUrl"] = self.$el.find("#ID-actPhotoUrl").val();// 海报
            data["content"] = formData["NID-Add-Node"][0];// 内容
            // 主题9图
            if(formData["NID-Photo"]){
                formData["NID-Photo"].forEach(function(n,i){
                    data["imageItems["+i+"]"] = n;
                });
            }
            data["actStartTime"] = formData["NID-Add-StartTime"][0]+":00";// 开始时间
            data["actEndTime"] = formData["NID-Add-EndTime"][0]+":00";// 结束时间
            data["address"] = formData["NID-Add-Address"][0];// 地址
            data["actCoordinate"] = formData["NID-latitude"][0]+","+formData["NID-longitude"][0];// 纬度,经度
            data["enterEndTime"] = formData["NID-Add-OffTime"][0]+":00";// 报名截止
            data["conTel"] = formData["NID-Add-Phone"][0];// 咨询电话
            data["numLimit"] = Number(formData["NID-Add-Count"][0]);// 人数限制
            data["pushFlag"]=formData["NID-IsCreateGroup"][0];
            // 报名必填项
            data["phone"] = formData["NID-Add-RePhone"][0];
            data["name"] = formData["NID-Add-ReName"][0];
            data["idCard"] = formData["NID-Add-ReCard"][0];
            data["sex"] = formData["NID-Add-ReSex"][0];
            data["remark"] = formData["NID-Add-ReMark"][0];
            // 邀请人员
            var dataPriv={
            		privileges:[]
            }
            var inner = formData['NID-MeetAdd-People'][0].replace(/(^\s*)|(\s*$)/g,"");
            if (inner != "") {
            	var personArr = inner.split(",");
            	for(var i=0,person; person=personArr[i++];) {
            		var arr = person.split(":");
	                var inInfo = new Object();
	                inInfo.entityId = arr[0];
	                inInfo.type = arr[3] == "0"?"user":"dept";
	                inInfo.entityName = arr[2];
	                inInfo.deptOrder = arr[3] == "0"?"":arr[3];
	                dataPriv.privileges.push(inInfo);
            	}
            }
            var param = JSON.stringify(dataPriv);
            data["privilegesJson"] = param;
//            //图片
//            if(formData["NID-Photo"]){
//                formData["NID-Photo"].forEach(function(n,i){
//                    data["imageItems["+i+"]"] = n;
//                });
//            }
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.addActivity+"&sessionId="+window.token;
            self.loadPanel.show();
            $.ajax({
            	url : app.serviceUrl+"?"+ropParam,
                data : data,
				type : "POST",
				dataType: 'json',
				success : function(data) {
					if(data.code=="1") {
						app.toast.setText("活动发布成功");
						app.toast.show();
						app.routerViews.indexView.refresh();
						setTimeout(function(){
							self.destroy();
				            history.go(-1);
			            },1000);
					} else if(_.intersection([data.code],app.errorCode)) {
						app.toast.setText(data.message);
						app.toast.show();
					} else {
						app.toast.setText("活动发布失败");
						app.toast.show();
					}
				},
				error : function(){
					app.toast.setText("活动发布失败");
					app.toast.show();
				},
                complete:function(){
                	self.loadPanel.hide();
                }
			});
        },
        //点击日期选择
        _onClickDivDateTime:function(e){
            var self=this;
            var defaults=[];
            var input=e.currentTarget.querySelector(".input-text");
            if(input.classList.contains("SID-DateTimeOff")){
                var startTime=this.$el.find(".SID-DateTimeStart").val();
                if(!startTime){
                    app.toast.setText("请先选择开始时间");
                    app.toast.show();
                    return;
                }
            }
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
                    date.plusMinute(10);
                }
                defaults[0]=date.getFullYear();
                defaults[1]=date.getMonth()+1;
                defaults[2]=date.getDate();
                defaults[3]=date.getHours();
                defaults[4]=date.getMinutes();
            }
            this._newSpDateTime(defaults,input);
            setTimeout(function(){
                self.spDateTime.scrollpicker.show();
            },10);
        },
        //点击必填项
        _onClickRequired:function(e){
            var target=e.currentTarget;
            if(target.classList.contains("active")){
                target.classList.remove("active");
                target.nextElementSibling.value="0";
            }else{
                target.classList.add("active");
                target.nextElementSibling.value="1";
            }
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
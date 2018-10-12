define(
	['jquery', 'views/communication-base-view'
	, 'text!../../templates/detail/addPlan.html'],
	function($, CommunicationBaseView, addPlanTemplate) {
		var IndexRouterSnippentView = CommunicationBaseView
			.extend({
				events: {
					'click .SID-BtnAddPlanBack':'back',
					'click .SID-BtnAddPlanSubmit':'_onClickSubmit',
					//表单验证
					'input .SID-InputMeetName':'_onInputMeetName',
					'input .SID-InputMeetRemark':'_onInputMeetRemark',
				},
				initialize: function() {
					var self = this;
					this.preRender();
					this.parentObj;
					//DOM
					this.formAddPlan;
					//Data
					this.roomId;
					//Plugin
					this.countValues={};
					this.toast=new Prompt("1",{
						parent:self.$el[0]
					});
					this.needApprove="0";
				},
				render: function(parentObj) {
					var self=this;
					this.parentObj = parentObj;
					if(this.parentObj.roomId)this.roomId=this.parentObj.roomId;
					if(this.parentObj.planData.needApprove)this.needApprove=this.parentObj.planData.needApprove;
					var data={};
					if(this.parentObj.planData)data=this.parentObj.planData;
					var html = _.template(
						addPlanTemplate,{
							data:data
						}
					);
					$(".SID-addPlan-snippet").append(html);
					//获得表单
					this.formContainer=document.getElementById("ID-FormAddPlan");
					return false;
				},
				refresh: function() {
					this.$el.empty();
					this.render(this.planId,this.parentObj);
				},
				destroy: function() {
					this.undelegateEvents();
					this.unbind();
					this.$el.empty();
				},
				/*=========================
	              Method
	              ===========================*/
				back:function(e){
					var self=this;
					this.parentObj.detailData={
						date:self.$el.find(".SID-InputDate").val(),
						begin:self.$el.find(".SID-InputStartTime").val(),
						end:self.$el.find(".SID-InputEndTime").val()
					};
                	this.destroy();
                    history.go(-1);
					return false;
				},
				_onInputMeetName:function(e){
					var self=this;
		            var validator=new Validator();
		            validator.add(e.target,[
		                {
		                    rule:'maxLength:19',
		                    errorMsg:'会议名称不能超过20位'
		                }
		            ]);
		            var error=validator.start();
		            if(error){
		                self.toast.setText(error.msg);
		                self.toast.show();
		            }
		        },
		        _onInputMeetRemark:function(e){
		        	var self=this;
		            var validator=new Validator();
		            validator.add(e.target,[
		                {
		                    rule:'maxLength:49',
		                    errorMsg:'会议备注不能超过50位'
		                }
		            ]);
		            var error=validator.start();
		            if(error){
		                self.toast.setText(error.msg);
		                self.toast.show();
		            }
		        },
				_validateForm:function(){
	                var validator=new Validator();
	                validator.add(this.formContainer["inputMeetName"],[
	                    {
	                        rule:'maxLength:20',
	                        errorMsg:'会议名称不能超过20位'
	                    },{
	                        rule:'required',
	                        errorMsg:'会议名称不能为空'
	                    }
	                ]);
	                validator.add(this.formContainer["inputMeetRemark"],[
	                    {
	                        rule:'maxLength:50',
	                        errorMsg:'会议备注不能超过50位'
	                    }
	                ]);
	                
	                var error=validator.start();
	                return error;
	            },
	            /*=========================
	              Event Handler
	              ===========================*/
				//点击提交按钮
				_onClickSubmit:function(e){
					var self = this;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					var roomId=this.roomId;
					var startTime = $(".SID-InputStartTime").val();
					var endTime = $(".SID-InputEndTime").val();
					var date = $(".SID-InputDate").val();
					var meetName = $.trim($(".SID-InputMeetName").val());
					var remark = $.trim($(".SID-InputMeetRemark").val());
					var meetingPeopleCount = $.trim($("#ID-AddPlan-PeopleCount").val());

					var error=this._validateForm();
	                if(error){
	                    self.toast.setText(error.msg);
	                    self.toast.show();
	                    return;
	                }
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&v=2.0&method=mapps.meetingroom.reserved.add&roomId="+self.roomId
						     + "&meetingName=" + meetName +"&participantsNum=" + meetingPeopleCount +"&reservedDate=" + date 
						     + "&reservedStartTime=" + startTime +"&reservedEndTime=" + endTime+"&reservedRemark=" + remark,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								if (self.needApprove == "0") {
									self.toast.setText("会议室预定成功");
								} else {
									self.toast.setText("已提交管理员审批");
								}
								self.toast.show();
								self.parentObj.views.indexView._onClickReset();
								self.parentObj.views.indexView.refresh(false);
								setTimeout(function(){
									self.parentObj.goBack();
								}, 1000);
							} else if (data.code =='300007') {
								//设置提示语
								self.toast.setText("操作期间该会议室已被预定!");
				                //显示弹出框
				                self.toast.show();
							} else if (data.code =='300006') {
								self.toast.setText("预定时间无效");
								self.toast.show();
							} else {
								self.toast.setText("预约失败");
								self.toast.show();
							}
						},
						error : function(){
							$(".SID-loading").hide();
							self.toast.setText("预约失败");
							self.toast.show();
						}
					});
				},
				//初始化
				_onLoad:function(){
					var self=this;
				}
			});

		return IndexRouterSnippentView;
	});
define([
    "app",

    "text!../../templates/meet/meetDetailTemplate.html",
    "text!../../templates/meet/surveyQuestionTemplate.html",
    "text!../../templates/meet/surveyAnswerTemplate.html"
], function(app, meetDetailTemplate, surveyQuestionTemplate, surveyAnswerTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.surveyId="";
            this.surveyData={};
            this.showFlag = "show";
        },
        render:function(surveyId){
        	var self = this;
        	this.surveyId = surveyId;
            //渲染页面
            var html=_.template( meetDetailTemplate , {
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //对话框
//            this.confirm=new Alert("您确定要放弃此次填写吗？",{
//                onClickCancel:function(e){
//                    e.hide();
//                }
//            });
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
//            if(!this.errorPanel[0]){
//                self.$el.append(app.errorHTML);
//                this.errorPanel=self.$el.find(".SID-Error");
//            }
            self.$el.find(".SID-Error").css("display",'none');
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
            self.$el.find(".SID-Load").css("display",'none');
            /*加载数据*/
            this.loadData();
            //返回控制
            Mplus.BackMonitor.addHandler(function(){
                self._onBack();
            });
        },
        refresh:function(surveyId){
        	this.surveyId = surveyId;
            console.log("detailView：刷新");
            this.loadData();
            //返回控制
            Mplus.BackMonitor.addHandler(function(){
                self._onBack();
            });
        },
        destroy:function(){
            var self=this;
        	self.loadPanel.hide();
            this.undelegateEvents();
            this.unbind();
            self.$el.empty();
        },
        loadData:function(){
            var self=this;
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.preview;
            var pageParam = "&surveyId="+self.surveyId;
            self.$el.find("#ID-submit-Btn").hide();
            self.$el.find(".SID-submitTime").hide();
            self.loadPanel.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	console.log(data);
                    if(data.code!=1){//请求错误
                    	if(app.isHomePage){
	                        app.toast.setText("链接失效");
	        				app.toast.show();
	                        setTimeout(function(){
	                        	mplus.closeWindow();
	                        }, 1000);
                    	}
                        self.surveyData={};
                        return;
                    }
                    self.surveyData=data.survey;
//                    self.errorPanel.removeClass("active");
                    //没有数据
                    if(data.survey.length<=0){
                        self.sliderContainer.innerHTML=app.nodataHTML;
                        return;
                    }
                    self.$el.find(".SID-survey-title").html(data.survey.titleHtml);
                    self.showFlag = "show";
                    if (data.survey.status == "1" && data.survey.submitTime == "") {
                        self.$el.find("#ID-submit-Btn").show();
                        self.showFlag = "answer";
                    } else if (typeof(data.survey.submitTime) != "undefined" && data.survey.submitTime != "") {
                    	self.$el.find(".SID-submitTime").html("完成时间: "+data.survey.submitTime);
                    	self.$el.find(".SID-submitTime").show();
                    } else if (data.survey.status == "0" && data.survey.submitTime == "") {
                        self.$el.find("#ID-submit-Btn").hide();
                        self.showFlag = "answer";
                    }
                    //题目绘制
                    self._initQuestion(data.survey.suQuestion);
                },
                error:function(msg){
                    self.surveyData={};
//                	self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    	self.loadPanel.hide();
                    }, 300);
                }
            });
        },
        /*=========================
          Method
          ===========================*/
        _initQuestion:function(questions){
        	var self = this;
        	var template = surveyQuestionTemplate;
        	if (self.showFlag == "show") {
        		template = surveyAnswerTemplate;
        	}
			var self = this;
			var divHtml = "";
			var pageNum = 1;
			var questionArr = [];
			for(var i=0,question; question=questions[i++];) {
				if (question.type == "7") {
					// 分页
					if (questionArr.length > 0) {
						var pageHtml=_.template(template,{
	                        imgPath: app.constants.IMAGEPATH,
	                        questionArr:questionArr,
	                        pageNum:pageNum
	                    });
						divHtml += pageHtml;
						pageNum++;
						questionArr = [];
					}
				} else {
					questionArr.push(question);
				}
				if (i >= questions.length && questionArr.length > 0) {
					var pageHtml=_.template(template,{
                        imgPath: app.constants.IMAGEPATH,
                        questionArr:questionArr,
                        pageNum:pageNum
                    });
					divHtml += pageHtml;
					pageNum++;
					questionArr = [];
				}
			}
			this.$el.find("#ID-question-div").html(divHtml);
			if (pageNum > 2) {
				this.$el.find(".SID-prev-btn").hide();
				this.$el.find(".SID-next-btn").show();
			}
			this._updateQuestionSequ();
//			if (self.showFlag == "show") {
//				self.$el.find("#ID-question-div").find("input,select,textarea").attr("disabled","disabled");
//			}
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back':'_onBack',
            
            'click .SID-prev-btn':'_onClickPrevBtn',
            'click .SID-next-btn':'_onClickNextBtn',
            'click #ID-submit-Btn':'_onSubmit',
            'click .SID-client-btn':'_onClickToClient'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickToClient:function(e){
        	this.destroy();
        	var hash="#"+app.pages.clientDetail+"/"+this.surveyId;//会议详情页面
        	app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickPanelError:function(e){
            this.refresh(this.surveyId);
        },
        _onBack:function(){
        	var self = this;
        	if (self.showFlag == "show") {
        		if(app.isHomePage){
                	mplus.closeWindow();
                    return;
                }
            	Mplus.BackMonitor.removeHandler(function(){
    		    	self._CallBack();
    		    });
            	self._resetDom();
                history.go(-1);
                return;
        	}
        	this.confirm.setOnClickOk(function(e){
        		e.hide();
                if(app.isHomePage){
                	mplus.closeWindow();
                    return;
                }
            	Mplus.BackMonitor.removeHandler(function(){
    		    	self._CallBack();
    		    });
            	self._resetDom();
                history.go(-1);
            });
        	this.confirm.show();
        },
        _submitBack:function(){
        	var self = this;
        	window.close();
            if(app.isHomePage){
            	mplus.closeWindow();
                return;
            }
        	Mplus.BackMonitor.removeHandler(function(){
		    	self._CallBack();
		    });
        	self._resetDom();
            history.go(-1);
        },
        _resetDom:function(){
        	var self = this;
        	self.$el.find(".SID-survey-title").html("");
            self.$el.find("#ID-submit-Btn").hide();
            self.$el.find(".SID-submitTime").hide();
            self.$el.find(".SID-prev-btn").hide();
            self.$el.find(".SID-next-btn").hide();
            self.$el.find("#ID-question-div").html("");
            self.surveyId="";
            self.surveyData={};
            self.showFlag = "show";
        },
        _onClickPrevBtn:function(e){
        	// 当前页
        	var currentPageObj = this.$el.find(".SID-question-ul[class$='active']");
        	var pageNum = currentPageObj.attr("data-pageNum");
        	// 恢复默认
        	currentPageObj.removeClass("active");
        	currentPageObj.hide();
        	// 按钮控制
        	if (currentPageObj.prev().prev().length>0) {
        		this.$el.find(".SID-prev-btn").show();
        	} else {
        		this.$el.find(".SID-prev-btn").hide();
        	}
        	// 翻页
        	this.$el.find(".SID-next-btn").show();
        	currentPageObj.prev(".SID-question-ul").addClass("active");
        	currentPageObj.prev(".SID-question-ul").show();
        	this.$el.find(".SID-detail-article").scrollTop(0);
        },
        _onClickNextBtn:function(e){
        	var currentPageObj = this.$el.find(".SID-question-ul[class$='active']");
        	var errorData=this._validateForm(currentPageObj);
        	if(errorData.allErrorNum>0){
                app.toast.setText("请填写必选题");
                if (errorData.requiredErrorNum > 0) {
            		app.toast.setText("请填写必选题");
            	} else if (errorData.minMaxErrorNum > 0) {
            		app.toast.setText("请按要求作答");
            	}
                app.toast.show();
                return;
            }
        	var pageNum = currentPageObj.attr("data-pageNum");
        	currentPageObj.removeClass("active");
        	currentPageObj.hide();
        	if (currentPageObj.next().next().length>0){
        		this.$el.find(".SID-next-btn").show();
        	} else {
        		this.$el.find(".SID-next-btn").hide();
        	}
        	this.$el.find(".SID-prev-btn").show();
        	currentPageObj.next(".SID-question-ul").addClass("active");
        	currentPageObj.next(".SID-question-ul").show();
        	this.$el.find(".SID-detail-article").scrollTop(0);
        },
		_updateQuestionSequ:function(){
			var allLiObj = this.$el.find(".SID-question-ul>.SID-question-li").find(".SID-question-sequ");
			for(var i=0,sequObj; sequObj=allLiObj[i++];) {
				$(sequObj).attr("data-sequ",i);
				$(sequObj).html("Q"+i+".");
			}
			return;
		},
		_validateForm:function(pageObj){
			$(".inputbox-error").removeClass("inputbox-error");
			$(".inputbox-error-required").removeClass("inputbox-error-required");
			$(".inputbox-error-min-max").removeClass("inputbox-error-min-max");
			var validateRangeObj = pageObj;
			// 数据序列化
			var form=new Form("#ID-FormMeetAdd",{
                ignoreClass:"form-ignore"
            }).serializeJson();
			// 校验问题
			var questions = validateRangeObj.find(".SID-question-li");
			for(var i=0,question; question=questions[i++];) {
				var required = $(question).attr("data-required");
				var id = $(question).attr("data-question-id");
				var type = $(question).attr("data-question-type");
				if (required == "1") {
					if (typeof(form["NID-"+id])=="undefined" || form["NID-"+id][0] == "") {
						// 问题必填校验
						$(question).find("dd").addClass("inputbox-error");
						$(question).find("dd").addClass("inputbox-error-required");
					} else {
						if (type == "1" || type == "3") {
							var checkObjs = $(question).find("[name='NID-"+id+"']:checked");
							for(var j=0,checkObj; checkObj=checkObjs[j++];) {
								var customInput = $(checkObj).attr("data-customInput");
								var optionId = $(checkObj).val();
								// 选项后文本框必填校验
								if (customInput == "1") {
									if (typeof(form["NID-"+optionId])=="undefined" || form["NID-"+optionId][0]=="") {
										$(checkObj).parent().parent("dd").find("[name='NID-"+optionId+"']").addClass("inputbox-error");
										$(checkObj).parent().parent("dd").find("[name='NID-"+optionId+"']").addClass("inputbox-error-required");
									}
								}
							}
						}
					}
					if (type == "3") {
						var selmin = $(question).attr("data-question-selmin");
						var selmax = $(question).attr("data-question-selmax");
						var length = $(question).find("[name='NID-"+id+"']:checked").length;
						console.log(length);
						if (selmin != "0" && selmin > length) {
							$(question).find("dd").addClass("inputbox-error");
							$(question).find("dd").addClass("inputbox-error-min-max");
						} else if (selmax != "0" && selmax < length) {
							$(question).find("dd").addClass("inputbox-error");
							$(question).find("dd").addClass("inputbox-error-min-max");
						}
					}
				}
			}
			var errorData = {
					allErrorNum : $(".inputbox-error").length,
					requiredErrorNum : $(".inputbox-error-required").length,
					minMaxErrorNum : $(".inputbox-error-min-max").length,
			}
			return errorData;
        },
        _onSubmit:function(e){
        	var self = this;
        	if (self.showFlag == "show") {
        		return;
        	}
            var pageObjs = this.$el.find(".SID-question-ul");
            for(var i=0,pageObj; pageObj=pageObjs[i++];) {
            	var currentPageObj = $(pageObj);
	            var errorData=this._validateForm(currentPageObj);
	            if(errorData.allErrorNum>0){
	            	pageObjs.removeClass("active");
	            	pageObjs.hide();
	            	if (currentPageObj.next().length>0){
	            		this.$el.find(".SID-next-btn").show();
	            	} else {
	            		this.$el.find(".SID-next-btn").hide();
	            	}
	            	if (currentPageObj.prev().length>0) {
	            		this.$el.find(".SID-prev-btn").show();
	            	} else {
	            		this.$el.find(".SID-prev-btn").hide();
	            	}
	            	currentPageObj.addClass("active");
	            	currentPageObj.show();
	            	app.toast.setText("请填写必选题");
	            	if (errorData.requiredErrorNum > 0) {
	            		app.toast.setText("请填写必选题");
	            	} else if (errorData.minMaxErrorNum > 0) {
	            		app.toast.setText("请按要求作答");
	            	}
	                app.toast.show();
	                return;
	            }
            }
			var form=new Form("#ID-FormMeetAdd",{
                ignoreClass:"form-ignore"
            }).serializeJson();
            // 获取参数
            var data = {
            		beginTime:self.surveyData.createTime,
            		surveyId:self.surveyId,
            		surveyAnswer:[]
            };
            var questions = this.$el.find(".SID-question-li[data-question-type!='6']");
            for(var i=0,question; question=questions[i++];) {
            	var id = $(question).attr("data-question-id");
            	var type = $(question).attr("data-question-type");
            	
				if (typeof(form["NID-"+id])!="undefined" && form["NID-"+id][0] != "") {
					
					if (type == "1" || type == "3") {
						var checkObjs = $(question).find("[name='NID-"+id+"']:checked");
						for(var j=0,checkObj; checkObj=checkObjs[j++];) {
							var optionId = $(checkObj).val();
								var questionData = {
				            			questionId:id,
				            			optionId:optionId,
				            			answer:""
				            	}
								if (typeof(form["NID-"+optionId])!="undefined" && form["NID-"+optionId][0]!="") {
									questionData.answer = form["NID-"+optionId][0];
								}
								data.surveyAnswer.push(questionData);
						}
					} else if (type == "2") {
						var questionData = {
		            			questionId:id,
		            			optionId:form["NID-"+id][0],
		            			answer:""
		            	}
						data.surveyAnswer.push(questionData);
					} else {
						var questionData = {
		            			questionId:id,
		            			optionId:"",
		            			answer:form["NID-"+id][0]
		            	}
						data.surveyAnswer.push(questionData);
					}
				}
            }
            
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.submit;
            var param = JSON.stringify(data);
            var pageParam = "&jsonStr="+encodeURIComponent(param);
            app.loading.show();
            self.$el.find("#ID-submit-Btn").hide();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
				success : function(data) {
					console.log(data);
					if (data.code == '1') {
						app.toast.setText("问卷提交成功");
						app.toast.show();
						if (app.routerViews.indexView) {
							app.routerViews.indexView.refresh();
						}
						setTimeout(function(){
							self._submitBack();
						},1000);
					} else if (data.code == '300001' || data.code == '300002' || data.code == '300003') {
						app.toast.setText(data.message);
						app.toast.show();
					} else {
						app.toast.setText("问卷提交失败");
						app.toast.show();
			            self.$el.find("#ID-submit-Btn").show();
					}
				},
				error : function(){
					app.toast.setText("问卷提交失败");
					app.toast.show();
		            self.$el.find("#ID-submit-Btn").show();
				},
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
			});
        }
    });

    return view;
});
define([
    "app",

    "text!../../templates/meet/surveyStatDetailTemplate.html",
    "text!../../templates/meet/surveyQuestionTemplate.html",
    "text!../../templates/meet/surveyStatTemplate.html", 'echarts'
], function(app, meetDetailTemplate, surveyQuestionTemplate, surveyAnswerTemplate){

	var echarts = require('echarts');
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
            this.confirm=new Alert("您确定要放弃此次填写吗？",{
                onClickCancel:function(e){
                    e.hide();
                }
            });
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
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
            setTimeout(function() {
                self.$el.empty();
            }, 300);
        },
        loadData:function(){
            var self=this;
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.preview;
            var pageParam = "&surveyId="+self.surveyId;
            self.$el.find("#ID-submit-Btn").hide();
            self.$el.find("#ID-stat-Btn").hide();
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
                    self.errorPanel.removeClass("active");
                    //没有数据
                    if(data.survey.length<=0){
                        self.sliderContainer.innerHTML=app.nodataHTML;
                        return;
                    }
                    self.$el.find(".SID-survey-title").html(data.survey.title);
                    self.showFlag = "show";
                    if (data.survey.status == "1" && data.survey.submitTime == "") {
                        self.$el.find("#ID-submit-Btn").show();
                        self.showFlag = "answer";
                    } else if (typeof(data.survey.submitTime) != "undefined" && data.survey.submitTime != "") {
                    	self.$el.find(".SID-submitTime").html("完成时间: "+data.survey.submitTime);
                    	self.$el.find(".SID-submitTime").show();
                        self.$el.find("#ID-stat-Btn").show();
                    }
                    //题目绘制
                    self._initQuestion(data.survey.suQuestion);
                    self._initSurveyData();
                },
                error:function(msg){
                    self.surveyData={};
                	self.errorPanel.addClass("active");
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
        	var template = surveyAnswerTemplate;//surveyQuestionTemplate;
        	if (self.showFlag == "show") {
        		template = surveyAnswerTemplate;
        	}
			var self = this;
			var divHtml = "";
			var pageNum = 1;
			var questionArr = [];
			for(var i=0,question; question=questions[i++];) {
//				if (question.type == "7") {
//					// 分页
//					if (questionArr.length > 0) {
//						var pageHtml=_.template(template,{
//	                        imgPath: app.constants.IMAGEPATH,
//	                        questionArr:questionArr,
//	                        pageNum:pageNum
//	                    });
//						divHtml += pageHtml;
//						pageNum++;
//						questionArr = [];
//					}
//				} else {
					questionArr.push(question);
//				}
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
			if (self.showFlag == "show") {
				self.$el.find("#ID-question-div").find("input,select,textarea").attr("disabled","disabled");
			}
        },
        _getParam:function(){
			var seldata = {
					surveyId : this.surveyId,
					option : [],
					errorFlag:false
			}
			return seldata;
		},
		_initSurveyData:function(){
			console.log(app.ismobile);
			var self = this;
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.stat;
			// 查询条件
			var seldata = this._getParam()
			var param = "&jsonStr=" + JSON.stringify(seldata);
			//self.$el.find(".SID-chart-question-div").empty();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + param,
				success : function(data) {
					if (data.code == "1") {
						// 头部问卷信息初始化
						var statusStr = "";
						if (data.status == "1") {
							statusStr = "发布中"
						} else if (data.status == "2") {
							statusStr = "已完成"
						} 
						//$(".SID-survey-info").html('<span>当前状态：<span>'+statusStr+'</span>  问卷在线时长：<span>'+data.durationStr+'</span>  已收集有效问卷：<span>'+data.answerPersons+'份</span></span>');
						// 筛选条件初始化
						// 图表初始化
						console.log(data);
						self.chartArr = [];
						for(var i=0,question; question=data.analysisQuestions[i++];) {
							// 生成数据展现区域
							var chartData = {
									sequ:i,
									id:question.id,
									total:question.total,
									titleText:question.question,
									type:question.type,
									legendData:[],
									seriesData:[],
									seriesBarData:[],
									tableData:[]
							}
							for(var j=0,option; option=question.suQustionOptions[j++];) {
								var seriesData = {
										value:option.selected,
										name:option.option
								}
								chartData.legendData.push(option.option);
								chartData.seriesData.push(seriesData);
								chartData.seriesBarData.push(option.selected);
								var tableData = {
										td1:option.option,
										td2:option.selected,
										td3:option.selectedPercent,
										id:option.id,
										sequ:option.sequ
								}
								chartData.tableData.push(tableData);
							}
//							var questionHtml = _.template(chartTemplate,{data:chartData});
//							self.$el.find(".SID-chart-question-div").append(questionHtml);
							var chart = {
								myChart:echarts.init(document.getElementById('ID-chart-'+question.id)),
								option:chartData
							}
							self.chartArr.push(chart);
						}
						self._setPieOption();
						for(var i=0,chart; chart=self.chartArr[i++];) {
							if (chart.option.type == "3") {
								self._setBarOption("update",chart);
							}
						}
					} else {
						fh.alert(data.code + ":" + data.message);
					}
				},
				error : function(){
					fh.alert("数据获取失败");
				}
			});
		},
		_setPieOption:function(updateFlag,data){
			var self = this;
			var arr = self.chartArr;
			if (updateFlag == "update") {
				arr = [data];
			}
			var labelTop = {
			    normal : {
			        label : {
			            show : true,
			            position : 'center',
			            formatter : function (params){
			                return params.value;
			            },
			            textStyle: {
			                baseline : 'bottom'
			            }
			        },
			        labelLine : {
			            show : false
			        }
			    }
			};
			var labelFromatter = {
			    normal : {
			        label : {
			        	show : false,
			            formatter : function (params){
			                return params.value;
			            },
			            textStyle: {
			                baseline : 'top'
			            }
			        },
			        labelLine : {
			            show : false
			        }
			    },
			}
			for(var i=0,chart; chart=arr[i++];) {
				var legendArr=[];
//				for(var j=0,legendData; legendData=chart.option.legendData[j++];) {
//					var str = legendData;
//					if (legendData.length > 3) {
//						str = legendData.substring(0,3)+"...";
//					}
//					legendArr.push(str);
//				}
				var seriesArr=[];
				for(var j=0,seriesData; seriesData=chart.option.seriesData[j++];) {
//					if (seriesData.value == 0) {
//						continue;
//					}
					var str = {
							name:seriesData.name,
							value:seriesData.value,
							itemStyle : labelTop
					};
					if (seriesData.name.length > 3) {
						str.name = seriesData.name.substring(0,3)+"...";
					}
					seriesArr.push(str);
					if (legendArr.length < 12) {
						legendArr.push(str.name);
					}
				}
				console.log(seriesArr);
				// 指定图表的配置项和数据
				var option = {
						//color:app.color,
					    tooltip : {
					        trigger: 'item'
					    },
					    toolbox: {
					        show : true,
					        feature : {
					            magicType: {show: false, type: ['pie']},
					        }
					    },
					    calculable : false,
					    legend: {
					        x : 'center',
					        y : 'bottom',
					        data:legendArr
					    },
					    series : [
					        {
					            type:'pie',
					            radius : [70, 100],
					            itemStyle : labelFromatter,
					            data:seriesArr
					        }
					    ]
					};
		        // 使用刚指定的配置项和数据显示图表。
				chart.myChart.setOption(option,true);
			}
		},
		_setBarOption:function(updateFlag,data){
			var self = this;
			var arr = self.chartArr;
			if (updateFlag == "update") {
				arr = [data];
			}
			for(var i=0,chart; chart=arr[i++];) {
				var legendArr=[];
				for(var j=0,legendData; legendData=chart.option.legendData[j++];) {
					var str = legendData;
					if (legendData.length > 3) {
						str = legendData.substring(0,3)+"...";
					}
					legendArr.push(str);
				}
				console.log(legendArr);
				var titleArr=[];
				for(var j=0,titleData; titleData=chart.option.titleText[j++];) {
					var str = titleData;
					if (titleData.length > 3) {
						str = titleData.substring(0,3)+"...";
					}
					titleArr.push(str);
				}
				// 指定图表的配置项和数据
				var option = {
						//color:app.color,
					    tooltip : {
					        trigger: 'axis'
					    },
					    toolbox: {
					        show : true,
					        showTitle : false,
					        feature : {
					            magicType: {show: false, type: ['bar']},
					        }
					    },
					    calculable : false,
					    grid: { // 控制图的大小，调整下面这些值就可以，
					    	x: 57,
					    	x2:5
					    },
					    xAxis : [
					        {
					            type : 'value',
					            boundaryGap : [0,1]
					        }
					    ],
					    yAxis : [
					        {
					        	type : 'category',
					        	data : legendArr
					        }
					    ],
					    series : [
					        {
					            tooltip : {           
					                formatter: "{c}"
					            },
					            type:'bar',
					            data:chart.option.seriesBarData
					        }
					    ]
					};
		        // 使用刚指定的配置项和数据显示图表。
				chart.myChart.setOption(option,true);
			}
		},
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back':'_onBack',
            
            'click .SID-prev-btn':'_onClickPrevBtn',
            'click .SID-next-btn':'_onClickNextBtn',
            'click #ID-submit-Btn':'_onSubmit'
        },

        /*=========================
          Event Handler
          ===========================*/
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
            self.$el.find("#ID-stat-Btn").hide();
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
										$(checkObj).siblings("[name='NID-"+optionId+"']").addClass("inputbox-error");
										$(checkObj).siblings("[name='NID-"+optionId+"']").addClass("inputbox-error-required");
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
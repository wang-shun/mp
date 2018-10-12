define(
		[ 'jquery' , 'views/communication-base-view','util/datatableUtil','datatable_lnpagination'
		  ,'text!../../templates/room/roomTemplate.html'
		  ,'text!../../templates/room/surveySeeTemplate.html'
		  ,'text!../../templates/room/surveyAddTemplate.html'
		  ,'text!../../templates/room/question/radioTemplate.html'
		  ,'text!../../templates/room/question/selectTemplate.html'
		  ,'text!../../templates/room/question/checkboxTemplate.html'
		  ,'text!../../templates/room/question/inputTemplate.html'
		  ,'text!../../templates/room/question/textareaTemplate.html'
		  ,'text!../../templates/room/question/remeakTemplate.html'
		  ,'text!../../templates/room/question/pageTemplate.html'
		  ,'text!../../templates/room/question/optionTemplate.html'
		  ,'text!../../templates/room/question/questionSetTemplate.html'
		  ,'text!../../templates/room/question/optionSetTemplate.html'
		  ,'text!../../templates/room/question/commonQuestionsTemplate.html'
		  ,'text!../../templates/room/setSurveyTemplate.html'
		  ,'views/room/more-view'
		  ,'views/privilege/privilege-orgadd-snippet-view'
		  ,'views/meet/meetDetailView'
		  ,'views/statistics/statistics-snippet-view','ckeditor'],
		function($, CommunicationBaseView,datatableUtil,datatableLnpagination
				,Template,detailTemplate,addTemplate
				,radioTemplate,selectTemplate,checkboxTemplate,inputTemplate,textareaTemplate,remeakTemplate,pageTemplate
				,optionTemplate,questionSetTemplate,optionSetTemplate,commonQuestionsTemplate
				,setSurveyTemplate
				,MoreView,OrgAddSnippetView,RoomViewSnippetView,StatisticsView) {
			var treeObj;
			var RoomSnippetView = CommunicationBaseView
					.extend({
						initialize : function() {
							this.childrenView = {};
							this.datatable = {};
							this.ckeditorFlag=true;
							this.ckeditorInitFlag = true;
						},
						render : function() {
							this._initSurveyListHtml();
							this._sessionForever();
							return this;
						},	
						destroyViews : function(){
							$.each(this.childrenView, function(index, view) {
								 view.destroy();
							});		
							this.childrenView = {};
						},	
						destroy : function() {
							this.destroyCkeditor();
							this.destroyViews();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						// 列表页面处理
						_initSurveyListHtml : function() {
							this.$el.empty();
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
							this._initSurveyList();
						},
						// 清空搜索框
						_clearSearchParam : function(){
							this.$el.find(".SID-status").val("");
							this.$el.find(".SID-title").val("");
						},
						// 获取搜索信息
						_initSearchParam:function(){
							var param = "";
							var title = this.$el.find(".SID-title").val().trim();
							var status = this.$el.find(".SID-status").val();
							if (title!="") {
								param += "&title="+encodeURIComponent(title);
							}
							if (status!="") {
								param += "&status="+status;
							}
							return param;
						},
						// 初始化列表
						_initSurveyList : function(){
							var tableObj={};
							var param = this._initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.survey.webget" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"问卷名称/问卷ID","mDataProp":"title1","sDefaultContent": "" ,"sClass":"left","fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-listop-peview" data-roomId="'+o.aData.id+'">'+o.aData.title+'/'+o.aData.surveyCode+'</a>';
								return str;
							}},
							{"sTitle":"创建时间","sWidth":"150px","mDataProp":"createTime","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								return o.aData.createTimeStr;
							}},
							{"sTitle":"完成情况","sWidth":"150px","mDataProp":"deviceName","sDefaultContent": "" ,"sClass":"left","bSortable":false,"fnRender":function(o,val){
								return o.aData.answerPersons+"/"+o.aData.targetPersons;
							}},
							{"sTitle":"项目状态","sWidth":"100px","mDataProp":"status","sDefaultContent": "" ,"sClass":"center","fnRender":function(o,val){
								if (o.aData.status=="0") {
									return "创建中";
								} else if (o.aData.status=="1") {
									return "发布中";
								} else if (o.aData.status=="2") {
									return "已完成";
								}
							}},
							{"sTitle":"操作","sWidth":"80px","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-listop-detail" data-roomId="'+o.aData.id+'"><span class="fhicon-eye"></span>查看</a>';
								if (o.aData.status == "创建中") {
									str += '<a href="javascript:void(0)" class="SID-listop-edit" data-roomId="'+o.aData.id+'"><span class="fhicon-edit"></span>编辑</a>';
									str += '<a href="javascript:void(0)" class="SID-listop-submit" data-roomId="'+o.aData.id+'"><span class="fhicon-active"></span>提交</a>';
									str += '<a href="javascript:void(0)" class="SID-listop-copy" data-roomId="'+o.aData.id+'"><span class="fhicon-meeting-jurisdiction"></span>复制</a>';
									str += '<a href="javascript:void(0)" class="SID-listop-delete" data-roomId="'+o.aData.id+'"><span class="fhicon-delete"></span>删除</a>';
								} else if (o.aData.status == "发布中") {
									str += '<a href="javascript:void(0)" class="SID-listop-statistics" data-roomId="'+o.aData.id+'" data-title="'+o.aData.title+'"><span class="fhicon-data"></span>答卷数据</a>';
									str += '<a href="javascript:void(0)" class="SID-listop-copy" data-roomId="'+o.aData.id+'"><span class="fhicon-meeting-jurisdiction"></span>复制</a>';
									str += '<a href="javascript:void(0)" class="SID-listop-close" data-roomId="'+o.aData.id+'"><span class="fhicon-close"></span>关闭</a>';
								} else if (o.aData.status == "已完成") {
									str += '<a href="javascript:void(0)" class="SID-listop-statistics" data-roomId="'+o.aData.id+'" data-title="'+o.aData.title+'"><span class="fhicon-data"></span>答卷数据</a>';
									str += '<a href="javascript:void(0)" class="SID-listop-copy" data-roomId="'+o.aData.id+'"><span class="fhicon-meeting-jurisdiction"></span>复制</a>';
								}
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.surveyList ? data.surveyList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						events : {
							'click .SID-search' : '_initSurveyList',
							'click .SID-clearSearch' : '_clearSearchParam',
							'click .SID-listop-detail' : '_onClickDetailBtn',
							'click .SID-show-toSet':'_toSetSurveyByShow',
							'click .SID-show-toQuestion':'_toQuestionSurvey',
							'click .SID-listop-edit' : '_onClickEditBtn',
							'click .SID-listop-statistics' : '_onClickStatisticsBtn',
							'click .SID-listop-submit' : '_onClickSubmitBtn',
							'click .SID-listop-copy' : '_onClickCopyBtn',
							'click .SID-listop-delete' : '_onClickDeleteBtn',
							'click .SID-listop-close' : '_onClickCloseBtn',
							'click .SID-listop-peview' : '_onClickPeviewBtn',
							
							'click .SID-to-surveyList':'_initSurveyListHtml',
							
							'click .SID-blank-survey-init':'_onClickSurveyInit',
							'click .SID-quote-survey-btn':'_onClickQuoteBtn',
							'click .SID-add-problem':'_onClickAddProblem',
							'click .SID-add-option':'_addOption',
							'click .SID-del-question':'_delQuestion',
							'click .SID-copy-question':'_copyQuestion',
							'click .SID-up-option':'_upOption',
							'click .SID-down-option':'_downOption',
							'click .SID-del-option':'_delOption',
							'click .SID-more-option':'_moreOption',
							'click .SID-more-question':'_moreQuestion',
							'click .SID-toEditSel-option':'_toEditSelOption',
							'click .SID-edit-ok-option':'_finishEditSel',
							'focusin .SID-validate-input':'_validateInputFocusin',
							'focusout .SID-validate-input':'_validateInputFocusout',
							'focusin .SID-validate-div':'_validateDivFocusin',
							'focusout .SID-validate-div':'_validateDivFocusout',
							'click .SID-submit-btn':'_publishSubmit',
							
							'click .SID-submit-view-btn':'_submitAndPeview',
							'click .SID-next-btn':'_toSetSurvey',
							'click .SID-prev-btn':'_toQuestionSurvey',
							'click .SID-cancel-btn':'_initSurveyListHtml',
							"click .SID-add" : "_orgAdd",
							'click .SID-del-li' : '_onClickDelLi',
							'click .SID-depclear' : '_onClickDepClear',
							'keyup .SID-search-user1' : '_searchUser',
							'click .SID-user-li':'_chooseUser',
							'click .SID-begin-checkbox':'_toggleCheckBox',
							'click .SID-end-checkbox':'_toggleCheckBox',
							
							'click .SID-commonQuestion-btn':'_addCommonQuestion'
						},
						// 答卷数据跳转
						_onClickStatisticsBtn:function(e){
							var self = this;
							var obj = $(e.target);
							var id =obj.attr("data-roomId");
							var title = obj.attr("data-title");
							if(id == ""){
								return;
							}
							this.$el.find(".SID-survey-list").hide();
							this.childrenView.statisticsView = new StatisticsView({el : $(".SID-survey-stat")});
							this.childrenView.statisticsView.render(id,title);
						},
						// 点击引入模板
						_onClickQuoteBtn:function(e){
							var msg = {
									menuName : "问卷模板"
							};
							this.eventHub.publishEvent('MENU_CLICKL', msg);	
						},
						// 常用问题点击事件
						_addCommonQuestion:function(e){
							var type = $(e.currentTarget).attr("data-common-question-type");
							var data = [];
							data.push(commonQuestions[type]);
							this._initQuestion(data);
							this._updateQuestionSequ(null,"reset");
							// 滚动到底部
							var obj = this.$el.find(".questionnaire-right");
							var height = this.$el.find(".questionnaire-list").height();
							obj.animate({scrollTop:height+'px'},"slow"); 
							this._initSort();
						},
						// 初始化常用问题html
				        _initCommonQuestions:function(){
				        	var html = _.template(commonQuestionsTemplate,{});
							this.$el.find(".SID-commonQuestion-div").append(html);
				        },
				        // 预览
				        _onClickPeviewBtn:function(e){
				        	var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							window.open("peview.html#ID-PageMeetDetail/"+roomId);
							return;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.survey.preview&surveyId="+roomId,
				                success:function(data){
				                	if (data.code == "1") {
										self.childrenView.roomViewSnippetView = new RoomViewSnippetView();
										self.childrenView.roomViewSnippetView.render(data);
				                	}
				                }
							});
				        },
				        // 查看按钮事件
						_onClickDetailBtn:function(e){
							var self = this;
							var obj = $(e.target);
							var id =obj.attr("data-roomId");
							if(id == ""){
								return;
							}
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							self.showLoading();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.survey.preview&surveyId="+id,
								success:function(data){
									if (data.code == "1") {
										self.$el.empty();
										var html = _.template(detailTemplate,{data:data.survey});
										self.$el.append(html);
										self.ckeditorFlag = false;
										//return;
										// 题目,选项
										self._initQuestion(data.survey.suQuestion,'1');
										self.$el.find(".SID-survey-page1").find(".questionnaire-right").css("left","220px");
										self.$el.find(".SID-survey-page1").find(".questionnaire-list").find(".SID-question-li[data-type='6']").find("textarea").removeClass("SID-validate-input").attr("readonly","readonly");
										self.$el.find(".SID-survey-page1").find(".questionnaire-list").find(".SID-validate-input").attr("readonly","readonly").removeClass("SID-validate-input");
										self.$el.find(".SID-survey-page1").find(".questionnaire-list").find("dd").removeClass("choose");
										self._updateQuestionSequ(null,"reset");
										// 发布设置
										var page2Obj = self.$el.find(".SID-survey-page2");
										//self.uiRanderUtil.randerJQueryUI_DateRange(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
										if (typeof(data.survey.effectiveTime)==="undefined") {
											page2Obj.find(".SID-begin-label").html("提交后立即发布");
										} else {
											//self.uiRanderUtil._setDate(".SID-beginDate",new Date(data.survey.effectiveTime));
											//page2Obj.find(".SID-begin-label").html(page2Obj.find(".SID-beginDate").val());
											page2Obj.find(".SID-begin-label").html(data.survey.effectiveTimeStr);
										}
										if (typeof(data.survey.endTime)==="undefined") {
											page2Obj.find(".SID-end-label").html("不限制截止日期");
										} else {
											//self.uiRanderUtil._setDate(".SID-endDate",new Date(data.survey.endTime));
											//page2Obj.find(".SID-end-label").html(page2Obj.find(".SID-endDate").val());
											page2Obj.find(".SID-end-label").html(data.survey.endTimeStr);
										}
							            if (data.survey.pushTo == "0")
							            	page2Obj.find(".SID-pushTo").removeAttr("checked");
							            self.ckeditorFlag = true;
									}
								},
				                complete:function(){
				                    setTimeout(function(){
				                    	self.hideLoading();
				                    }, 300);
				                }
							});
						},
						// 编辑按钮事件
						_onClickEditBtn:function(e){
							var self = this;
							var obj = $(e.target);
							var id =obj.attr("data-roomId");
							if(id == ""){
								return;
							}
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							self.showLoading();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.survey.preview&surveyId="+id,
								success:function(data){
									if (data.code == "1") {
										self.$el.empty();
										var html = _.template(addTemplate,{data:data.survey});
										self.$el.append(html);
										self.ckeditorInitFlag = false;
										self.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
										self._initCommonQuestions();
										// 题目,选项
										self._initQuestion(data.survey.suQuestion);
										self._updateQuestionSequ(null,"reset");
										// 人员设置
										if (data.survey.suParticipants.length > 0) {
											self._initSurveySet(data.survey.suParticipants);
											// 发布设置
											var page2Obj = self.$el.find(".SID-survey-page2");
											if (typeof(data.survey.effectiveTime)==="undefined") {
												page2Obj.find(".SID-begin-checkbox").removeAttr("checked");
												page2Obj.find(".SID-begin-label").html("提交后立即发布");
												page2Obj.find(".SID-beginDate").hide();
											} else {
												self.uiRanderUtil._setDate(".SID-beginDate",new Date(data.survey.effectiveTime));
											}
											if (typeof(data.survey.endTime)==="undefined") {
												page2Obj.find(".SID-end-checkbox").removeAttr("checked");
												page2Obj.find(".SID-end-label").html("不限制截止日期");
												page2Obj.find(".SID-endDate").hide();
											} else {
												self.uiRanderUtil._setDate(".SID-endDate",new Date(data.survey.endTime));
											}
								            if (data.survey.pushTo == "0")
								            	page2Obj.find(".SID-pushTo").removeAttr("checked");
										} else {
											self.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
											self.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate",7);
										}
							            self._initSort();
							            self.ckeditorInitFlag = true;
							            self.reloadCkeditor();
									}
								},
				                complete:function(){
				                    setTimeout(function(){
				                    	self.hideLoading();
				                    }, 300);
				                }
							});
						},
						// 编辑页面初始化
						_toEditInit:function(id){
							var self = this;
							var id = id;
							if(id == ""){
								return;
							}
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							self.showLoading();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.preview&surveyTemplateId="+id,
								success:function(data){
									if (data.code == "1") {
										data.survey.id = "";
										self.$el.empty();
										var html = _.template(addTemplate,{data:data.survey});
										self.$el.append(html);
										self.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
										self._initCommonQuestions();
										// 题目,选项
										self._initQuestion(data.survey.suQuestion);
										self._updateQuestionSequ(null,"reset");
										// 人员设置
//										if (data.survey.suParticipants.length > 0) {
//											self._initSurveySet(data.survey.suParticipants);
//											// 发布设置
//											var page2Obj = self.$el.find(".SID-survey-page2");
//											if (typeof(data.survey.effectiveTime)==="undefined") {
//												page2Obj.find(".SID-begin-checkbox").removeAttr("checked");
//												page2Obj.find(".SID-begin-label").html("提交后立即发布");
//												page2Obj.find(".SID-beginDate").hide();
//											} else {
//												self.uiRanderUtil._setDate(".SID-beginDate",new Date(data.survey.effectiveTime));
//											}
//											if (typeof(data.survey.endTime)==="undefined") {
//												page2Obj.find(".SID-end-checkbox").removeAttr("checked");
//												page2Obj.find(".SID-end-label").html("不限制截止日期");
//												page2Obj.find(".SID-endDate").hide();
//											} else {
//												self.uiRanderUtil._setDate(".SID-endDate",new Date(data.survey.endTime));
//											}
//								            if (data.survey.pushTo == "0")
//								            	page2Obj.find(".SID-pushTo").removeAttr("checked");
//										} else {
											self.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
											self.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate",7);
//										}
							            self._initSort();
							            self.reloadCkeditor();
									}
								},
				                complete:function(){
				                    setTimeout(function(){
				                    	self.hideLoading();
				                    }, 300);
				                }
							});
						},
						// 提交按钮事件
						_onClickSubmitBtn:function(e){
							var self = this;
							var obj = $(e.target);
							var id =obj.attr("data-roomId");
							if(id == ""){
								return;
							}
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							self.showLoading();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.survey.preview&surveyId="+id,
								success:function(data){
									if (data.code == "1") {
										self.$el.empty();
										var html = _.template(addTemplate,{data:data.survey});
										self.$el.append(html);
										self.ckeditorInitFlag = false;
										self.$el.find(".SID-survey-page1").hide();
										self.$el.find(".SID-survey-page2").show();
										self.$el.find(".SID-survey-page2").find(".SID-prev-btn").hide();
										self.$el.find(".SID-survey-page2").find(".SID-cancel-btn").show();
										self.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
										// 题目,选项
										self._initQuestion(data.survey.suQuestion);
										// 人员设置
										if (data.survey.suParticipants.length > 0) {
											self._initSurveySet(data.survey.suParticipants);
											// 发布设置
											var page2Obj = self.$el.find(".SID-survey-page2");
											if (typeof(data.survey.effectiveTime)==="undefined") {
												page2Obj.find(".SID-begin-checkbox").removeAttr("checked");
												page2Obj.find(".SID-begin-label").html("提交后立即发布");
												page2Obj.find(".SID-beginDate").hide();
											} else {
												self.uiRanderUtil._setDate(".SID-beginDate",new Date(data.survey.effectiveTime));
											}
											if (typeof(data.survey.endTime)==="undefined") {
												page2Obj.find(".SID-end-checkbox").removeAttr("checked");
												page2Obj.find(".SID-end-label").html("不限制截止日期");
												page2Obj.find(".SID-endDate").hide();
											} else {
												self.uiRanderUtil._setDate(".SID-endDate",new Date(data.survey.endTime));
											}
								            if (data.survey.pushTo == "0")
								            	page2Obj.find(".SID-pushTo").removeAttr("checked");
										} else {
											self.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
											self.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate",7);
										}

										self.ckeditorInitFlag = true;
									}
								},
				                complete:function(){
				                    setTimeout(function(){
				                    	self.hideLoading();
				                    }, 300);
				                }
							});
						},
						// 初始化问题 questions问题对象，showFlag是否是查看功能
						_initQuestion:function(questions,showFlag){
							var self = this;
							for(var i=0,question; question=questions[i++];) {
								var data = {
										sequ:i,
										blankFlag:0,
										showFlag:showFlag,
										type:question.type,
										required : question.required,
										title:question.questionHtml,
										options:question.suQustionOptions
								}
								switch(question.type)
								{
							        case "1":
										self._addRadio(data);
							        break;
							        case "2":
										self._addSelect(data);
							        break;
							        case "3":
										data.selMin = question.selMin;
										data.selMax = question.selMax;
										self._addCheckbox(data);
							        break;
							        case "4":
										self._addInput(data);
							        break;
							        case "5":
										self._addTextarea(data);
							        break;
							        case "6":
							        	data.sequ=0;
							        	self._addRemark(data);
							        break;
							        case "7":
							        	data.sequ=0;
							        	self._addPage(data);
							        break;
							        default:return;
							        break;
							    }
							}
						},
						// 初始化问卷设置页面
						_initSurveySet:function(data){
							var page2Obj = this.$el.find(".SID-survey-page2");
							var depObj = page2Obj.find(".SID-deplist");
							var userObj = page2Obj.find("#subForm1").find(".SID-userbox");
							depObj.empty();
							userObj.empty();
							this._initLi(data,depObj,userObj);
						},
						// 问卷范围初始化
						_initLi:function(list,depObj,userObj){
							for(var i=0,privilege;privilege=list[i++];){
								if (privilege.entityType=='dept') {
									depObj.append("<li class='SID-priv-li rank-item' data-type='dept' data-depOrder='"+privilege.deptOrder+"'  data-depUuid='"+privilege.entityId+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+privilege.entityName+"</span></li>");
								} else if (privilege.entityType=='user') {
									userObj.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+privilege.entityId+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+privilege.entityName+"</span></li>");
								}
							}
						},
						// 拷贝功能
						_onClickCopyBtn : function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							fh.confirm('确定复制该问卷？',function(){
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.survey.copy&surveyId="+roomId,
									success:function(data){
										if(data.code == "1"){
											self._initSurveyList();
										}else{
											fh.alert("复制失败！",false,function(){
												self._initSurveyList();
											});
										}
									},
									error:function(){
										fh.alert("复制失败！");
									}
								});
							});
						},
						// 删除功能
						_onClickDeleteBtn:function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							fh.confirm('问卷删除后不可恢复，确定删除？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.survey.delete&surveyId="+roomId,
									success:function(data){
										if(data.code == "1"){
											self._initSurveyList();
										}else{
											fh.alert("删除失败！",false,function(){
												self._initSurveyList();
											});
										}
									},
									error:function(){
										fh.alert("删除失败！");
									}
								});
							});
						},
						// 关闭功能
						_onClickCloseBtn:function(e){
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							fh.confirm('确定关闭问卷？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.survey.close&surveyId="+roomId,
									success:function(data){
										if(data.code == "1"){
											self._initSurveyList();
										}else{
											fh.alert("关闭失败！",false,function(){
												self._initSurveyList();
											});
										}
									},
									error:function(){
										fh.alert("关闭失败！");
									}
								});
							});
						},
						// 空白问卷初始化
						_onClickSurveyInit:function(){
							this.$el.empty();
							var data = {
									id:"",
									title:"调查问卷",
									titleHtml:"调查问卷"
							}
							var html = _.template(addTemplate,{data:data});
							this.$el.append(html);
							this._initCommonQuestions();
							this.uiRanderUtil.minDateNowInit(this,".SID-beginDate",".SID-endDate","yy-mm-dd");
							this.uiRanderUtil._setDateByType(".SID-beginDate",".SID-endDate",7);
				            this.reloadCkeditor();
						},
						// 问卷编辑页面点击预览
						_submitAndPeview:function(){
							this._beforeSubmit("peview");
							return;
						},
						// 问卷发布提交
						_publishSubmit:function(){
							var self = this;
							if (self._beforeSubmit("checkData") == "checkDataOk") {
								fh.confirm('问卷发布后将不可编辑，确认发布？',function(){
									self._beforeSubmit();
								});
							}
						},
						// 跳转至问卷设置页面
						_toSetSurvey:function(){
							this._beforeSubmit("nextPage");
							return;
						},
						_toSetSurveyByShow:function(){
							this.$el.find(".SID-survey-page1").hide();
							this.$el.find(".SID-survey-page2").show();
							return;
						},
						_toQuestionSurvey:function(){
							this.$el.find(".SID-survey-page1").show();
							this.$el.find(".SID-survey-page2").hide();
							return;
						},
						// 添加问题
						_onClickAddProblem:function(e){
							var self = this;
							var type = $(e.currentTarget).attr("data-type");
							var len1 = this.$el.find(".SID-question-ul>li[data-type='0']").length;
							var len2 = this.$el.find(".SID-question-ul>li[data-type='6']").length;
							var len3 = this.$el.find(".SID-question-ul>li[data-type='7']").length;
							var all = this.$el.find(".SID-question-ul>li").length;
							var sequ = all-len1-len2-len3+1;
							var data = {
									sequ:sequ,
									blankFlag:1,
									type:type,
									required : "1"
							}
							switch(type)
							{
						        case "1":
									data.title = "单选题";
									self._addRadio(data);
						        break;
						        case "2":
									data.title = "选择列表";
									self._addSelect(data);
						        break;
						        case "3":
									data.title = "多选题";
									data.selMin = "0";
									data.selMax = "0";
									self._addCheckbox(data);
						        break;
						        case "4":
									data.title = "单行填空题";
									self._addInput(data);
						        break;
						        case "5":
									data.title = "多行填空题";
									self._addTextarea(data);
						        break;
						        case "6":
						        	data.sequ=0;
						        	data.title = "请输入信息";
						        	self._addRemark(data);
						        break;
						        case "7":
						        	data.sequ=0;
						        	self._addPage(data);
						        break;
						        default:return;
						        break;
						    }
							// 滚动到底部
							var obj = this.$el.find(".questionnaire-right");
							var height = this.$el.find(".questionnaire-list").height();
							obj.animate({scrollTop:height+'px'},"slow"); 
							this._initSort();
						},
						// 排序
						_initSort:function(){
							var self = this;
							$("#ID-question-ul").sortable({
								containment: ".questionnaire-list",
								axis: "y",
								handle: ".SID-draggable-question" ,
						    	revert: true,
								stop:function( event, ui ){
									self._updateQuestionSequ(null,"reset");
									self._updatePageTitle();
								}
						    });
						},
						_addRadio:function(data){
							var optionHtml = "";
							if (data.blankFlag == 1) {
								for (var i=1;i<=2;i++) {
									data.optionSequ = "选项"+i;
									data.customInput = 0;
									optionHtml += _.template(optionTemplate,{data:data});
								}
							} else {
								for(var i=0,option; option=data.options[i++];) {
									data.optionSequ = option.optionHtml;
									data.customInput = option.customInput;
									optionHtml += _.template(optionTemplate,{data:data});
								}
							}
							data.optionHtml = optionHtml;
							var html = _.template(radioTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this.reloadCkeditor();
						},
						reloadCkeditor:function(){
							if (!this.ckeditorFlag){
								this.$el.find(".SID-validate-div").removeAttr("contenteditable");
								return;
							}
							if (!this.ckeditorInitFlag) {
								return;
							}
							this.destroyCkeditor();
							CKEDITOR.inlineAll();
							for(name in CKEDITOR.instances)
							{
								var ckObj = CKEDITOR.instances[name];
								var ckId = ckObj.id;
							}
							$(".SID-validate-div").removeAttr("title");
						},
						destroyCkeditor:function(){
							for(name in CKEDITOR.instances)
							{
								var ckObj = CKEDITOR.instances[name];
								var ckId = ckObj.id;
								ckObj.destroy();
							}
						},
						_addSelect:function(data){
							var optionHtml = "";
							if (data.blankFlag == 1) {
								data.optionSel = ["选项1","选项2","选项3"];
								optionHtml += _.template(optionTemplate,{data:data});
							} else {
								data.optionSel = []
								for(var i=0,option; option=data.options[i++];) {
									data.optionSel.push(option.option);
								}
								optionHtml += _.template(optionTemplate,{data:data});
							}
							data.optionHtml = optionHtml;
							var html = _.template(selectTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this.reloadCkeditor();
						},
						_addCheckbox:function(data){
							var optionHtml = "";
							if (data.blankFlag == 1) {
								for (var i=1;i<=2;i++) {
									data.optionSequ = "选项"+i;
									data.customInput = 0;
									optionHtml += _.template(optionTemplate,{data:data});
								}
							} else {
								for(var i=0,option; option=data.options[i++];) {
									data.optionSequ = option.optionHtml;
									data.customInput = option.customInput;
									optionHtml += _.template(optionTemplate,{data:data});
								}
							}
							data.optionHtml = optionHtml;
							var html = _.template(checkboxTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this.reloadCkeditor();
						},
						_addInput:function(data){
							var html = _.template(inputTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this.reloadCkeditor();
						},
						_addTextarea:function(data){
							var html = _.template(textareaTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this.reloadCkeditor();
						},
						_addRemark:function(data){
							var html = _.template(remeakTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this.reloadCkeditor();
						},
						_addPage:function(data){
							var html = _.template(pageTemplate,{data:data});
							this.$el.find(".SID-question-ul").append(html);
							this._updatePageTitle();
						},
						// 添加选项
						_addOption:function(e){
							var type = $(e.currentTarget).parent().parent().parent().attr("data-type");
							var optionHtml = "";
							switch(type)
							{
						        case "1":
									var sequ = $(e.currentTarget).parent().parent().find(".SID-option-dd").length + 1;
									var data = {
											optionSequ:"选项" + sequ,
											customInput : 0,
											type:type
									}
						        	optionHtml = _.template(optionTemplate,{data:data});
									$(e.currentTarget).parent().before(optionHtml);
						        break;
						        case "2":
						        	var sequ = $(e.currentTarget).parent().parent().find(".SID-option-dd").length + 1;
						        	var data = {
											optionSequ:"选项" + sequ,
											type:2,
											op:"edit"
									}
						        	optionHtml = _.template(optionTemplate,{data:data});
									$(e.currentTarget).parents(".SID-question-li").find(".SID-edit-sel").append(optionHtml);
						        break;
						        case "3":
									var sequ = $(e.currentTarget).parent().parent().find(".SID-option-dd").length + 1;
									var data = {
											optionSequ:"选项" + sequ,
											customInput : 0,
											type:type
									}
						        	optionHtml = _.template(optionTemplate,{data:data});
									$(e.currentTarget).parent().before(optionHtml);
						        break;
						        case "4":
						        break;
						        case "5":
						        break;
						        case "6":
						        break;
						        case "7":
						        break;
						        default:return;
						        break;
						    }
							this.reloadCkeditor();
						},
						_delQuestion:function(e){
							var self = this;
							var liObj = $(e.currentTarget).parents(".SID-question-li");
							fh.confirm('删除此题卡？',function(){
								// 更新题目序号
								self._updateQuestionSequ(liObj);
								liObj.remove();
								self._updatePageTitle();
							});
						},
						_copyQuestion:function(e){
							var liObj = $(e.currentTarget).parent().parent().parent();
							liObj.after(liObj.prop("outerHTML"));
							this._updateQuestionSequ(liObj,'copy');
							this.reloadCkeditor();
						},
						_updateQuestionSequ:function(liObj,opType){
							if (opType == 'reset') {
								var allLiObj = this.$el.find("#ID-question-ul>.SID-question-li").find(".SID-question-sequ");
								for(var i=0,sequObj; sequObj=allLiObj[i++];) {
									$(sequObj).attr("data-sequ",i);
									$(sequObj).html("Q"+i+".");
								}
								return;
							}
							var type = liObj.attr("data-type");
							if (type == '6' || type == '7') {
								return;
							} else {
								for(var i=0,sequObj; sequObj=liObj.nextAll().find(".SID-question-sequ")[i++];) {
									var seq = $(sequObj).attr("data-sequ");
									var newSeq = Number(seq)-1;
									if (opType == 'copy') {
										newSeq = Number(seq)+1;
									}
									$(sequObj).attr("data-sequ",newSeq);
									$(sequObj).html("Q"+newSeq+".");
								}
							}
						},
						_updatePageTitle:function(){
							var pageObjs = this.$el.find(".SID-question-ul>li[data-type='7']");
							var pageObjs2 = this.$el.find(".SID-question-ul2>li[data-type='7']");
							var total = pageObjs.length + pageObjs2.length;
							for(var i=0,page; page=pageObjs[i++];) {
								$(page).find(".SID-page-title").text("页码:"+i+"/"+total);
							}
							pageObjs2.find(".SID-page-title").text("页码:"+total+"/"+total);
						},
						_upOption:function(e){
							var ddObj = $(e.currentTarget).parents(".SID-option-dd");
							var tempVal = ddObj.find(".SID-updownFlag").val();
							var prevDdObj = ddObj.prev(".SID-option-dd");
							if (prevDdObj.length>0) {
								prevDdObj.before(ddObj.prop("outerHTML"));
								prevDdObj.prev(".SID-option-dd").find(".SID-updownFlag").val(tempVal);
								ddObj.remove();
								this.reloadCkeditor();
							}
						},
						_downOption:function(e){
							var ddObj = $(e.currentTarget).parents(".SID-option-dd");
							var tempVal = ddObj.find(".SID-updownFlag").val();
							var nextDdObj = ddObj.next(".SID-option-dd");
							if (nextDdObj.length>0) {
								nextDdObj.after(ddObj.prop("outerHTML"));
								nextDdObj.next(".SID-option-dd").find(".SID-updownFlag").val(tempVal);
								ddObj.remove();
								this.reloadCkeditor();
							}
						},
						_delOption:function(e){
							var ddObj = $(e.currentTarget).parents(".SID-option-dd");
							var siblingsDdObj = ddObj.siblings(".SID-option-dd");
							if (siblingsDdObj.length>0) {
								ddObj.remove();
								this.reloadCkeditor();
							} else {
								fh.alert("选项个数不能为0");
							}
						},
						_moreOption:function(e){
							var ddObj = $(e.currentTarget).parents(".SID-option-dd");
							var customInput = ddObj.attr("custom_input");
							var data = {
									customInput : customInput
							}
							var setHtml = _.template(optionSetTemplate,{data:data});
							var initData = {
									ddObj : ddObj,
									setHtml : setHtml,
									type : 'option'
							}
							this.childrenView.moreView = new MoreView();
							this.childrenView.moreView.render(initData);
						},
						_moreQuestion:function(e){
							var liObj = $(e.currentTarget).parents(".SID-question-li");
							var questionType = liObj.attr("data-type");
							var questionRequired = liObj.attr("question_required");
							var data = {
									required : questionRequired,
									questionType : questionType
							}
							if (questionType == "3") {
								var selMax = liObj.attr("question_selMax");
								var selMin = liObj.attr("question_selMin");
								var selLength = liObj.find(".SID-option-dd").length;
								data.selMax = selMax;
								data.selMin = selMin;
								data.selLength = selLength;
							}
							var setHtml = _.template(questionSetTemplate,{data:data});
							var initData = {
									liObj : liObj,
									setHtml : setHtml,
									type : 'question',
									questionType : questionType
							}
							if (questionType == "3") {
								var selMax = liObj.attr("question_selMax");
								var selMin = liObj.attr("question_selMin");
								var selLength = liObj.find(".SID-option-dd").length;
								initData.selMax = selMax;
								initData.selMin = selMin;
								initData.selLength = selLength;
							}
							this.childrenView.moreView = new MoreView();
							this.childrenView.moreView.render(initData);
						},
						_toEditSelOption:function(e){
							var optionHtml = "";
							var optionObjs = $(e.currentTarget).prev(".SID-question-sel").find("option");
							var data = {
									type:2,
									op:"edit"
							}
							for(var i=0,optionObj; optionObj=optionObjs[i++];) {
								data.optionSequ = $(optionObj).val();
								optionHtml += _.template(optionTemplate,{data:data});
							}
							$(e.currentTarget).parent().next(".SID-edit-sel").html(optionHtml);
							$(e.currentTarget).parent().next(".SID-edit-sel").show();
							$(e.currentTarget).parent().hide();
							$(e.currentTarget).parents(".SID-question-li").find(".SID-add-option").show();
							$(e.currentTarget).parents(".SID-question-li").find(".SID-edit-ok-option").show();
						},
						_finishEditSel:function(e){
							var liObj = $(e.currentTarget).parents(".SID-question-li");
							// 获取选项
							var optionObjs = liObj.find(".SID-edit-sel > .SID-option-dd > .SID-sel-option-value");
							// 组织新选项
							var newHtml = "";
							for(var i=0,optionObj; optionObj=optionObjs[i++];) {
								newHtml += "<option value='"+$(optionObj).val()+"'>"+$(optionObj).val()+"</option>";
							}
							// 赋值
							var selDdObj = liObj.find(".SID-option-sel-dd");
							selDdObj.find(".SID-question-sel").html(newHtml);
							// 清空编辑区
							liObj.find(".SID-edit-sel").html("");
							// 按钮状态复原
							liObj.find(".SID-edit-sel").hide();
							selDdObj.show();
							liObj.find(".SID-add-option").hide();
							liObj.find(".SID-edit-ok-option").hide();
						},
						_validateInputFocusin:function(e){
							var value = $(e.currentTarget).val();
							$(e.currentTarget).attr("data-value",value);
							$(e.currentTarget).addClass("background-blue");
							$(e.currentTarget).parent().addClass("active");
						},
						_validateInputFocusout:function(e){
							var value = $(e.currentTarget).val();
							var oldValue = $(e.currentTarget).attr("data-value");
							var maxlength = $(e.currentTarget).attr("maxlength");
							if (value == "") {
								$(e.currentTarget).val(oldValue);
							} else if (value.length > maxlength) {
								$(e.currentTarget).val(oldValue);
							}
							$(e.currentTarget).removeClass("background-blue");
							$(e.currentTarget).parent().removeClass("active");
						},
						_validateDivFocusin:function(e){
							var value = $(e.currentTarget).val();
							var html = $(e.currentTarget).html();
							var text = $(e.currentTarget).text();
							$(e.currentTarget).attr("data-value",value);
							$(e.currentTarget).attr("data-html",html);
							$(e.currentTarget).attr("data-text",text);
							$(e.currentTarget).addClass("background-blue");
							$(e.currentTarget).parent().addClass("active");
						},
						_validateDivFocusout:function(e){
							var value = $(e.currentTarget).val();
							var html = $(e.currentTarget).html();
							var text = $(e.currentTarget).text();
							var oldValue = $(e.currentTarget).attr("data-value");
							var oldHtml = $(e.currentTarget).attr("data-html");
							var oldText = $(e.currentTarget).attr("data-text");
							var maxlength = $(e.currentTarget).attr("data-maxlength");
							if (text == "") {
								//$(e.currentTarget).val(oldValue);
								$(e.currentTarget).html(oldHtml);
							} else if (text.length > maxlength) {
								//$(e.currentTarget).val(oldValue);
								$(e.currentTarget).html(oldHtml);
							}
							$(e.currentTarget).removeClass("background-blue");
							$(e.currentTarget).parent().removeClass("active");
						},
						_beforeSubmit:function(nextPageFlag){
							var page1Obj = this.$el.find(".SID-survey-page1");
							var titleHtml = page1Obj.find(".SID-survey-title").html();
							var title = page1Obj.find(".SID-survey-title").text();
							var id = page1Obj.find("#ID-survey-id").val();
							var status = 0;
							if (nextPageFlag != "nextPage" && nextPageFlag != "peview") {
								status = 1;
							}
							var data = {
								id:id,
								title : title,
								titleHtml : titleHtml,
								pushTo : 1,
								status : status,
								suQuestion:[],
								suParticipants:[]
							}
							var questionliObjs = page1Obj.find("#ID-question-ul>.SID-question-li");
							var effectiveQuestionNum = 0;
							for(var i=0,liObj; liObj=questionliObjs[i++];) {
								var type = $(liObj).attr("data-type");
								var required = $(liObj).attr("question_required");
								var questionTitle = $(liObj).find(".SID-question-title").text();
								var questionTitleHtml = $(liObj).find(".SID-question-title").html();
								var suQuestionObj = {
									question:questionTitle,//问题题目
									questionHtml:questionTitleHtml,//问题题目
									type:type,//问题类型
									required:required,//是否必填
									sequ:i,//排序号
									suQustionOptions:[]
								}
								if (type != "6" && type != "7") {
									effectiveQuestionNum++;
								}
								switch(type)
								{
							        case "1":
							        	var ddObjs = $(liObj).find(".SID-option-dd");
							        	for(var j=0,ddObj; ddObj=ddObjs[j++];) {
							        		var optionTitle = $(ddObj).find(".SID-option-title").text();
							        		var optionTitleHtml = $(ddObj).find(".SID-option-title").html();
							        		var customInput = $(ddObj).attr("custom_input");
							        		var optionObj = {
							        			option:optionTitle,
							        			optionHtml:optionTitleHtml,
							        			customInput:customInput,
							        			sequ:j
							        		};
							        		suQuestionObj.suQustionOptions.push(optionObj);
							        	}
							        break;
							        case "2":
							        	var selOptionObjs = $(liObj).find(".SID-option-sel-dd>.SID-question-sel>option");
							        	for(var j=0,selOptionObj; selOptionObj=selOptionObjs[j++];) {
							        		var optionTitle = $(selOptionObj).text();
							        		var optionObj = {
							        			option:optionTitle,
							        			optionHtml:optionTitle,
							        			customInput:0,
							        			sequ:j
							        		};
							        		suQuestionObj.suQustionOptions.push(optionObj);
							        	}
							        break;
							        case "3":
										var selMax = $(liObj).attr("question_selMax");
										var selMin = $(liObj).attr("question_selMin");
										suQuestionObj.selMax = selMax;
										suQuestionObj.selMin = selMin;
							        	var ddObjs = $(liObj).find(".SID-option-dd");
							        	for(var j=0,ddObj; ddObj=ddObjs[j++];) {
							        		var optionTitle = $(ddObj).find(".SID-option-title").text();
							        		var optionTitleHtml = $(ddObj).find(".SID-option-title").html();
							        		var customInput = $(ddObj).attr("custom_input");
							        		var optionObj = {
							        			option:optionTitle,
							        			optionHtml:optionTitleHtml,
							        			customInput:customInput,
							        			sequ:j
							        		};
							        		suQuestionObj.suQustionOptions.push(optionObj);
							        	}
							        break;
							        case "4":
							        break;
							        case "5":
							        break;
							        case "6":
							        break;
							        case "7":
							        break;
							        default:return;
							        break;
							    }
								data.suQuestion.push(suQuestionObj);
							}
							if (effectiveQuestionNum < 1) {
								fh.alert("请完成问卷信息编辑");
								return "";
							}
							// 问卷设置
							if (nextPageFlag != "nextPage" && nextPageFlag != "peview") {
								var page2Obj = this.$el.find(".SID-survey-page2");
								var lis = page2Obj.find(".rank-list > .rank-item");
					            for(var i=0,li;li=lis[i++];){
					            	var type = $(li).attr("data-type");
					            	var id = $(li).attr("data-depUuid");
					            	var deptOrder = $(li).attr("data-depOrder");
					            	var name = $(li).find("span").text();
					            	var privObj = new Object();
					            	privObj.entityType = type;
					            	privObj.entityId = id;
					            	privObj.entityName = name;
					            	if (type == "dept")
					            		privObj.deptOrder = deptOrder;
					            	data.suParticipants.push(privObj);
					            }
					            if (nextPageFlag != "nextPage" && nextPageFlag != "peview") {
									if (data.suParticipants.length<1) {
										fh.alert("请设置调查范围");
										return "";
									}
								}
					            var beginDate = page2Obj.find(".SID-beginDate").val();
					            var endDate = page2Obj.find(".SID-endDate").val();
					            var pushTo = page2Obj.find(".SID-pushTo").is(':checked');
					            if (beginDate!="")
					            	data.effectiveTime = beginDate;
					            if (endDate!="")
					            	data.endTime = endDate;
					            if (beginDate!="" && endDate!="") {
					            	if (new Date(beginDate).getTime() > new Date(endDate).getTime()) {
					            		fh.alert("请设置有效的收集日期");
					            		return;
					            	}
					            }
					            if (!pushTo)
					            	data.pushTo = 0;
							}
							if (nextPageFlag == "checkData") {
								return "checkDataOk";
							}
							
							this._submit(data,nextPageFlag);
						},
						_submit:function(data,nextPageFlag){
							var id = data.id;
							var param = JSON.stringify(data);
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var submitParam = ropParam+ "&method=mapps.survey.add&jsonStr="+encodeURIComponent(param); 
							if (data.id!="") {
								submitParam = ropParam+ "&method=mapps.survey.update&jsonStr="+encodeURIComponent(param);
							}
							this.showLoading();
							$.ajax({
								type: "POST",
								contentType:"application/x-www-form-urlencoded; charset=UTF-8",
								url:servicePath,
								data:submitParam,
								success:function(reData){
									if (reData.code == "1") {
										self.$el.find("#ID-survey-id").val(reData.id);
										if (nextPageFlag == "nextPage") {
											self.$el.find(".SID-save-ok").show();
											setTimeout(function(){
												self.$el.find(".SID-save-ok").hide();
												self.$el.find(".SID-survey-page1").hide();
												self.$el.find(".SID-survey-page2").show();
			                				},500);
										} else if (nextPageFlag == "peview") {
											self.$el.find(".SID-save-ok").show();
											setTimeout(function(){
												self.$el.find(".SID-save-ok").hide();
												$.ajax({
													type:"POST",
													url:servicePath+"?"+ropParam+ "&method=mapps.survey.preview&surveyId="+reData.id,
									                success:function(data){
									                	if (data.code == "1") {
									                		window.open("peview.html#ID-PageMeetDetail/"+reData.id);
															return;
															self.childrenView.roomViewSnippetView = new RoomViewSnippetView();
															self.childrenView.roomViewSnippetView.render(data);
									                	}
									                }
												});
											},500);
										} else {
											self._initSurveyListHtml();
										}
									} else {
										fh.alert("数据处理失败");
									}
								},
				                complete:function(){
			                    	self.hideLoading();
				                }
							});
						},

						_orgAdd : function(){
							var depLis = this.$el.find(".SID-deplist > li");
							var depUuids = [];
							for(var i=0,li;li=depLis[i++];){
								depUuids.push($(li).attr("data-depUuid"));
							}
							this.childrenView.orgAddSnippetView= new OrgAddSnippetView();
							this.childrenView.orgAddSnippetView.render(this,depUuids);
						},
						_setDep:function(nodes){
							var deplist = $(".SID-deplist");
							deplist.empty();
							for(var i=0,node;node=nodes[i++];){
								deplist.append("<li class='SID-priv-li rank-item' data-type='dept' data-depOrder='"+node.depOrder+"' data-depUuid='"+node.depUuid+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+node.depName+"</span></li>");
				            }
						},
						_onClickDelLi:function(e){
							var obj = $(e.target);
							obj.parent().remove();
						},
						_onClickDepClear:function(){
							$(".SID-deplist").empty();
						},
						_searchUser:function(e){
							var _self = this;
							var formObj = $(e.currentTarget).parents("form");
							var username = formObj.find(".SID-suInput").val();
							if (username == ""){
								formObj.find(".pop-rankbox-maskColor").css("display","none");
								return;
							}
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
				            var param = "";
							param = formObj.serialize();
							//this.$el.find("#subForm").serialize();
							if (param!="") {
								param = "&"+param;
							}
							var url = servicePath + "?" +ropParam + "&method=mapps.thirdpart.mobileark.getusers"+param;
				            $.ajax({
								type : "POST",
								url : url,
								success : function(data) {
									if (data.code == "1") {
										_self._setUser(formObj,data.userList);
									}
								},
								error : function(){
								}
							});
						},
						_setUser:function(formObj,users){
							if (users.length == 0){
								return;
							}
							var userList = formObj.find(".SID-userlist");
							var userSearch = formObj.find(".pop-searchbar");
							userList.empty();
							var liObjs = formObj.find(".SID-userbox>li");
							var userIdArr = new Array();
							for(var i=0,liObj;liObj=liObjs[i++];){
								userIdArr.push($(liObj).attr("data-depUuid"));
							}
							var pushCount=0;
							for(var i=0,user;user=users[i++];){
								var flag = true;
								for(var j=0,userId;userId=userIdArr[j++];){
									if (user.userUuid == userId)
										flag = false;
								}
								if (flag) {
									userList.append("<li class='SID-priv-li rank-item SID-user-li' data-type='user' data-depUuid='"+user.userUuid+"'><span style='pointer-events:none;'>"+user.userName+"</span></li>");
									pushCount ++;
								}
							}
							if (pushCount == 0) {
								return ;
							}
							formObj.find(".pop-rankbox-maskColor").css("display","block");
						},
						_chooseUser:function(e){
							var formObj = $(e.currentTarget).parents("form");
							var type = $(e.target).attr("data-type");
			            	var id = $(e.target).attr("data-depUuid");
			            	var name = $(e.target).text();
			            	var userBox = formObj.find(".SID-userbox");
			            	formObj.find(".SID-suInput").val("");
			            	formObj.find(".pop-rankbox-maskColor").css("display","none");
			            	userBox.append("<li class='SID-priv-li rank-item' data-type='user' data-depUuid='"+id+"'><i class='fhicon-close3 SID-del-li'></i><span style='pointer-events: none;'>"+name+"</span></li>");
						},
						_toggleCheckBox:function(e){
							var checkObj = $(e.target);
							var inputObj = checkObj.nextAll("input");
							var labelObj = checkObj.nextAll("label");
							if (checkObj.is(":checked")) {
								if (checkObj.attr("data-type") == "begin") {
									this.uiRanderUtil._setDate(".SID-beginDate",new Date());
									labelObj.html("开始收集日期：");
								} else if (checkObj.attr("data-type") == "end") {
									var millisecond = 1000 * 60 * 60 * 24;
									this.uiRanderUtil._setDate(".SID-endDate",new Date(new Date().getTime() + (7 * millisecond)));
									labelObj.html("截止收集日期：");
								}
								inputObj.show();
							} else {
								if (checkObj.attr("data-type") == "begin") {
									labelObj.html("提交后立即发布");
								} else if (checkObj.attr("data-type") == "end") {
									labelObj.html("不限制截止日期");
								}
								inputObj.hide();
								inputObj.val("");
							}
						},
				        _sessionForever:function(){
				        	var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.survey.sessionforever",
				                success:function(data){
	                    			setTimeout(function(){
	                    				self._sessionForever();
	                				},1000*60*5);
				                }
							});
				        }
			
					});
			return RoomSnippetView;
		});
define(
		[ 'jquery', 'views/communication-base-view'
		  ,'util/datatableUtil'
		  ,'text!../../templates/room/surveyTemplateList.html'
		  ,'text!../../templates/room/templateSeeTemplate.html'
		  ,'text!../../templates/room/mySurveyAddTemplate.html'
		  ,'text!../../templates/room/commonSurveyAddTemplate.html'
		  ,'text!../../templates/room/question/commonQuestionsTemplate.html'
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
		  ,'views/meet/templateDetailView'
		  ,'views/room/more-view','ckeditor'
		  ],
		function($, CommunicationBaseView,datatableUtil,Template,detailTemplate,
			addTemplate,addCommontemplate,commonQuestionsTemplate
			,radioTemplate,selectTemplate,checkboxTemplate,inputTemplate,textareaTemplate,remeakTemplate,pageTemplate
			,optionTemplate,questionSetTemplate,optionSetTemplate
			,RoomViewSnippetView,MoreView) {
			var treeObj;
			var datatable;
			var SurveyTemplateViewSnippetView = CommunicationBaseView
					.extend({
						events : {
						    'click .SID-delete':'templateDel',
						    'click .SID-submit':'editTemplate',
						    'click .SID-copy':'copyTemplate',
						    'click .SID-addSurveyGroup':'addSurveyGroup',
						    'click .SID-commonTemplate':'showCommonTemplate',
						    'click .SID-myTemplate':'showMyTemplate',
						    'click .SID-commonGroupDel':'delCommonGroup',
						    'blur input.SID-commonGroup':'saveCommonGroup',
						    'click .SID-addCommonGroup':'addCommonGroup',
						    'click .SID-search':'searchGroup',
						    'click .SID-clearSearch':'clearSearch',
						    'click .SID-add-problem':'_onClickAddProblem',
						    'click .SID-commonQuestion-btn':'_addCommonQuestion',
						    'click .SID-save-btn':'_toSaveSurveyTemplate',
						    'click .SID-cancel-btn':'cancelBack',
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
						    'click .SID-addTemplate':'addTemplate',
						    'click .SID-detail':'privewTemplate',
						    'click .SID-surveyTemplate-detail':'_onClickPeviewBtn',
						    'click .SID-submitPreview':'_submitAndPeview',
						    'click .SID-commonGroup-user':'_commonGroupUser'
						},
						_submitAndPeview:function(){
							this._beforeSubmit("peview");
							return;
						},
						initialize : function() {
							this.childrenView = {};
							this.data = {};
							this.isAdmin = "";
							this.type = "";
							this.parentView;
							this.defaultid="";
							this.ckeditorFlag=true;
							this.ckeditorInitFlag=true;
						},
						render : function() {
							this._setContentHTML();
							this._sessionForever();
							return this;
						},
						destroy : function() {
							this.destroyCkeditor();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						_setContentHTML : function (){
							var self = this;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var template = _.template(Template);
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.groupGet",
								success:function(result){
								    var groupHtml = self.parseResult(result);
								    var data = {'groupHtml' : groupHtml}
									var html = template({data:data});
									self.$el.append(html);
									if (self.defaultid != "") {
										self.$el.find(".SID-myTemplate").removeClass("h3-active");
										self.$el.find(".SID-commonTemplate").removeClass("h3-active");
										var group = self.$el.find(".SID-commonGroup1");
										for(var i=0,groupObj; groupObj=group[i++];) {
											if ($(groupObj).attr("defaultid")==self.defaultid) {
												$(groupObj).parent().addClass("h3-active");
											}
										}
										self._initCommonSurveyTemplateList();
										if(self.isAdmin == "0")
										{
										    self.$el.find(".SID-addTemplate").hide();
										}
									} else if(self.type == "my") {
										self.$el.find(".SID-myTemplate").addClass("h3-active");
										self.$el.find(".SID-commonTemplate").removeClass("h3-active");
										self._initMySurveyTemplateList();
									} else {
										self.$el.find(".SID-myTemplate").removeClass("h3-active");
										self.$el.find(".SID-commonTemplate").addClass("h3-active");
									    self._initCommonSurveyTemplateList();
										if(self.isAdmin == "0")
										{
										    self.$el.find(".SID-addTemplate").hide();
										}
									}
								}
							});
						},
						parseResult : function(result)
						{
							this.isAdmin = result.isAdmin;
							var html = '<ul class="SID-commonGroups">';
						    if(result.isAdmin == '0')
						    {
						    	$(result.surveyGroups).each(function(i,data){
							        html +='<li class="SID-commonGroup-user">'
							               +'<input type="text" value="'+data.name+'" defaultvalue= "'+data.name+'" defaultid="'+data.id+'" readonly="readonly" class="input-textNoLine SID-commonGroup1" />'
							               +'</li>'
						    	
						    	})
						    	html +='</ul>'
						    }
						    else
						    {
						    	$(result.surveyGroups).each(function(i,data){
							    	html +='<li>'
						               +'<input type="text" maxlength="8" value="'+data.name+'" defaultvalue= "'+data.name+'" defaultid="'+data.id+'" class="input-textNoLine SID-commonGroup" />'
						               +'<a class="questionnaire-del SID-commonGroupDel"></a>'
						               +'</li>'
						    	})
						    	html +='</ul>'
						    	html +='<a class="right blue SID-addCommonGroup">新增</a>'
						    }
						    return html;
						},
						_commonGroupUser:function(e){
							this.$el.find(".type-problem").find(".h3-active").removeClass("h3-active");
							var id = $(e.currentTarget).find(".SID-commonGroup1").attr("defaultid");
							$(e.currentTarget).addClass("h3-active");
							this.defaultid = id;
							this.$el.empty();
							this.render();
						},
						previewImage: function() {
			                $('.SID-image').viewer({
			                	url: 'data-original'
			                });
			            },
			            _initCommonSurveyTemplateList : function(){
			            	var self = this;
							var tableObj={};
							var param = this._initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.surveyTemplate.webget" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"名称","sWidth":"35%","mDataProp":"title","sDefaultContent": "" ,"sClass":"left","bSortable":true,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-surveyTemplate-detail" data-roomId="'+o.aData.id+'">'+o.aData.title+'</a>';
								return str;
							}},
							{"sTitle":"类别","sWidth":"20%","mDataProp":"groupName","sDefaultContent": "" ,"sClass":"left","bSortable":true,"fnRender":function(o,val){
								return o.aData.groupName;
							}},
							{"sTitle":"使用次数","sWidth":"80px","mDataProp":"useTimes","sDefaultContent": "" ,"sClass":"right","bSortable":true,"fnRender":function(o,val){
								return o.aData.useTimes;
							}},
							{"sTitle":"题目数","sWidth":"70px","mDataProp":"questions","sDefaultContent": "" ,"sClass":"right","bSortable":true,"fnRender":function(o,val){
								return o.aData.questions;
							}},
							{"sTitle":"操作","sWidth":"50px","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str ='';
								str += '<a href="javascript:void(0)" class="SID-detail" data-roomId="'+o.aData.id+'"><span class="fhicon-eye"></span>查看</a>';
								if(self.isAdmin == '1')
								{
								    str += '<a href="javascript:void(0)" class="SID-submit" data-roomId="'+o.aData.id+'"><span class="fhicon-active"></span>编辑</a>';
									str += '<a href="javascript:void(0)" class="SID-copy" data-roomId="'+o.aData.id+'"><span class="fhicon-meeting-jurisdiction"></span>复制</a>';
									str += '<a href="javascript:void(0)" class="SID-delete" data-roomId="'+o.aData.id+'"><span class="fhicon-delete"></span>删除</a>';
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
														"aaData" : data.surveyTemplateList ? data.surveyTemplateList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_initMySurveyTemplateList : function(){
							var tableObj={};
							var param = this._initSearchParam();
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var url = servicePath + "?" + ropParam + "&method=mapps.surveyTemplate.webget" + param;
							
							tableObj.url = url;
							//控制是否可分页
							tableObj.bPaginate = true;
							// tableObj.aaSorting = [[2,'asc']];
							tableObj.aoColumns=[
							{"sTitle":"名称","sWidth":"300px","mDataProp":"title","sDefaultContent": "" ,"sClass":"left","bSortable":true,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-surveyTemplate-detail" data-roomId="'+o.aData.id+'">'+o.aData.title+'</a>';
								return str;
							}},
							{"sTitle":"题目数","sWidth":"80px","mDataProp":"questions","sDefaultContent": "right" ,"sClass":"right","bSortable":true,"fnRender":function(o,val){
								return o.aData.questions;
							}},
							{"sTitle":"最后编辑时间","sWidth":"80px","mDataProp":"modifiedTime","sDefaultContent": "right" ,"sClass":"center","bSortable":true,"fnRender":function(o,val){
								return o.aData.modifiedTimeStr;
							}},
							{"sTitle":"操作","sWidth":"80px","mDataProp":"cz","sDefaultContent": "" ,"sClass":"center","bSortable":false,"fnRender":function(o,val){
								var str = '<a href="javascript:void(0)" class="SID-detail" data-roomId="'+o.aData.id+'"><span class="fhicon-eye"></span>查看</a>';
								str += '<a href="javascript:void(0)" class="SID-submit" data-roomId="'+o.aData.id+'"><span class="fhicon-active"></span>编辑</a>';
								str += '<a href="javascript:void(0)" class="SID-copy" data-roomId="'+o.aData.id+'"><span class="fhicon-meeting-jurisdiction"></span>复制</a>';
								str += '<a href="javascript:void(0)" class="SID-delete" data-roomId="'+o.aData.id+'"><span class="fhicon-delete"></span>删除</a>';
								var retBox='<div class="tb-opt-box"><a class="table-operation"><span class="fhicon-set2"></span></a><div class="tb-opt-main">'+str+'</div></div>'
								return retBox;
							}}
							];
							var param="";
							var jsonProc = function(data) {
								var jsonData = {
										"iTotalDisplayRecords" : data.total ? data.total : 0,
												"iTotalRecords" : data.total ? data.total : 0,
														"aaData" : data.surveyTemplateList ? data.surveyTemplateList : ''
								};
								return jsonData;
							}
							this.datatable=datatableUtil(tableObj,param,jsonProc);
							
						},
						_initSearchParam:function(){
							var param = "";
							var title = this.$el.find(".SID-title").val();
							if (title!="") {
								param += "&title="+encodeURIComponent(title);
							}
							var type = '1';
							if(this.$el.find(".SID-myTemplate").hasClass("h3-active"))
							{
							    type = '2';
							}
							
							param +="&type="+type;
							if (this.defaultid != "") {
								param +="&defaultid="+this.defaultid;
							}
							return param;
						},
						templateDel : function(e)
						{
							var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							fh.confirm('模板删除后不可恢复，确定删除？',function(){
								var appContext = self.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.delete&surveyTemplateId="+roomId,
									success:function(ajax){
									   if(self.type =="my")
									   {
										   self.showMyTemplate();
									   }else{
										   self.showCommonTemplate();
									   }
									}
								});
							});
						},
						showCommonTemplate : function()
						{
							this.type = "common";
							this.defaultid = "";
							this.$el.empty();
							this.render();
						},
						showMyTemplate : function()
						{
							this.type = "my";
							this.defaultid = "";
							this.$el.empty();
							this.render();
						},
						saveCommonGroup : function(e)
						{
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var obj = $(e.target);
							var value = obj.val();
							if(value == "" )
							{
							    obj.val(obj.attr("defaultvalue"))
							}
							else
							{
								var name = obj.val();
								var id = obj.attr("defaultid");
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.groupSave&groupName="+name+"&groupId="+id,
									success:function(){
									   obj.attr("defaultvalue",value)
									}
								});
							}
							
						},
						addCommonGroup : function()
						{
							var self = this;
							var i = self.$el.find(".SID-commonGroups").children('li').length+1;
							var name = '类别'+i;
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.groupSave&groupName="+name,
								success:function(result){
								    var html = '<li>'
								            +'<input type="text" maxlength="8" value="'+name+'" defaultvalue= "'+name+'" defaultid="'+result.id+'" class="input-textNoLine SID-commonGroup" />'
								            +'<a  class="questionnaire-del SID-commonGroupDel"></a>'
								            +'</li>';
								    self.$el.find(".SID-commonGroups").append(html);
								}
							})
						},
						delCommonGroup : function(e)
						{
							var appContext = this.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var id = $(e.target).siblings(".SID-commonGroup").attr("defaultid");
							fh.confirm('类别删除后不可恢复，确定删除？',function(){
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.groupDel&groupId="+id,
									success:function(result){
										$(e.target).parent().remove();
									}
								})
							})
						},
						searchGroup : function()
						{
							if(this.$el.find(".SID-myTemplate").hasClass("h3-active"))
							{
							    this._initMySurveyTemplateList();
							}
							else
							{
							    this._initCommonSurveyTemplateList();
							}
						},
						clearSearch : function()
						{
							var title = this.$el.find(".SID-title").val("");
						},
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
									data.optionSequ = option.option;
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
									data.optionSequ = option.option;
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
										title:question.question,
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
						// 跳转至问卷设置页面
						_toSaveSurveyTemplate:function(){
							this._beforeSubmit("nextPage");
							return;
						},
						_beforeSubmit:function(nextPageFlag){
							var page1Obj = this.$el.find(".SID-survey-page1");
							var titleHtml = page1Obj.find(".SID-survey-title").html();
							var title = page1Obj.find(".SID-survey-title").text();
							var id = page1Obj.find("#ID-survey-id").val();
							var status = 0;
							if (nextPageFlag != "nextPage") {
								status = 1;
							}
							var groupId =　"";
							if(this.$el.find(".SID-Group-Select").length > 0)
							{
							    groupId = this.$el.find(".SID-Group-Select").val();
							}
							var type = "1"
							if(this.isAdmin == '0')
							{
								type = "2";
							}
							else if(this.type == "my")
							{
							    type = "2";
							}
							var data = {
								id:id,
								groupId : groupId,
								title : title,
								titleHtml : titleHtml,
								pushTo : 1,
								type : type,
								status : status,
								suQuestion:[],
								suParticipants:[]
							}
							var questionliObjs = page1Obj.find("#ID-question-ul>.SID-question-li");
							for(var i=0,liObj; liObj=questionliObjs[i++];) {
								var type = $(liObj).attr("data-type");
								var required = $(liObj).attr("question_required");
								var questionTitle = $(liObj).find(".SID-question-title").html();
								var suQuestionObj = {
									question:questionTitle,//问题题目
									type:type,//问题类型
									required:required,//是否必填
									sequ:i,//排序号
									suQustionOptions:[]
								}
								switch(type)
								{
							        case "1":
							        	var ddObjs = $(liObj).find(".SID-option-dd");
							        	for(var j=0,ddObj; ddObj=ddObjs[j++];) {
							        		var optionTitle = $(ddObj).find(".SID-option-title").html();
							        		var customInput = $(ddObj).attr("custom_input");
							        		var optionObj = {
							        			option:optionTitle,
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
							        			customInput:0,
							        			sequ:j
							        		};
							        		suQuestionObj.suQustionOptions.push(optionObj);
							        	}
							        break;
							        case "3":
							        	var ddObjs = $(liObj).find(".SID-option-dd");
							        	for(var j=0,ddObj; ddObj=ddObjs[j++];) {
							        		var optionTitle = $(ddObj).find(".SID-option-title").html();
							        		var customInput = $(ddObj).attr("custom_input");
							        		var optionObj = {
							        			option:optionTitle,
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
							this._submit(data,nextPageFlag);
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
						_submit:function(data,nextPageFlag){
							var id = data.id;
							var param = JSON.stringify(data);
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							var submitParam
							var submitParam = ropParam+ "&method=mapps.surveyTemplate.add&jsonStr="+encodeURIComponent(param); 
							if (data.id!="") {
								submitParam = ropParam+ "&method=mapps.surveyTemplate.update&jsonStr="+encodeURIComponent(param);
							}
							this.showLoading();
							$.ajax({
								type: "POST",
								contentType:"application/x-www-form-urlencoded; charset=UTF-8",
								url:servicePath,
								data:submitParam,
								success:function(reData){
								    if(nextPageFlag == 'peview')
								    {
								    	self.$el.find(".SID-save-ok").show();
										setTimeout(function(){
											self.$el.find(".SID-save-ok").hide();
											$.ajax({
												type:"POST",
												url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.preview&surveyTemplateId="+reData.id,
								                success:function(data){
								                	console.log(data);
								                	if (data.code == "1") {
														self.childrenView.roomViewSnippetView = new RoomViewSnippetView();
														self.childrenView.roomViewSnippetView.render(data);
								                	}
								                }
											});
										},500);
								    }else{
										self.$el.empty();
									    self.render();
								    }
								},
				                complete:function(){
				                    setTimeout(function(){
				                    	self.hideLoading();
				                    }, 300);
				                }
							});
						},
						cancelBack : function()
						{
							this.$el.empty();
							this.render();
						},
						// 编辑按钮事件
						editTemplate:function(e){
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
							var groupSelect = this.initGroupSelect();
							self.showLoading();
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.preview&surveyTemplateId="+id,
								success:function(data){
									if (data.code == "1") {
										self.$el.empty();
										var templateHtml = addCommontemplate;
										if(self.type == "my")
										{
											templateHtml = 	addTemplate;
										}else if(self.isAdmin == '0')
										{
											templateHtml = 	addTemplate;
										}
										var hdata = {
										    id:data.survey.id,
										    title:data.survey.title,
										    titleHtml:data.survey.titleHtml,
										    selectHtml:groupSelect
										}
										var html = _.template(templateHtml,{data:hdata});
										self.$el.append(html);
										self.ckeditorInitFlag = false;
										self._initCommonQuestions();
										self.$el.find(".SID-Group-Select").val(data.survey.groupId);
										// 题目,选项
										self._initQuestion(data.survey.suQuestion);
										self._updateQuestionSequ(null,"reset");
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
						copyTemplate : function(e)
						{
							var self = this;
							var obj = $(e.target);
							var id =obj.attr("data-roomId");
							if(id == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							fh.confirm('确定复制该模板？',function(){
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.copy&surveyId="+id,
									success:function(ajax){
										if(self.type =="my")
										   {
											   self.showMyTemplate();
										   }else{
											   self.showCommonTemplate();
										   }
									}
								});
							})
						},
						privewTemplate : function(e)
						{
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
								url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.preview&surveyTemplateId="+id,
								success:function(data){
									if (data.code == "1") {
										self.$el.empty();
										var hdata = {
										    id:data.survey.id,
										    title:data.survey.title,
										    titleHtml:data.survey.titleHtml,
										    selectHtml:self.groupSelect
										}
										var html = _.template(detailTemplate,{data:hdata});
										self.$el.append(html);
										self.ckeditorFlag = false;
										// 题目,选项
										self._initQuestion(data.survey.suQuestion,'1');
										self.$el.find(".SID-survey-page1").find(".questionnaire-right").css("left","220px");
										self.$el.find(".SID-survey-page1").find(".questionnaire-list").find(".SID-question-li[data-type='6']").find("textarea").removeClass("SID-validate-input").attr("readonly","readonly");
										self.$el.find(".SID-survey-page1").find(".questionnaire-list").find(".SID-validate-input").attr("readonly","readonly").removeClass("SID-validate-input");
										self.$el.find(".SID-survey-page1").find(".questionnaire-list").find("dd").removeClass("choose");
										self._updateQuestionSequ(null,"reset");
										self.$el.find(".SID-validate-div").addClass("q-word");
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
						_onClickPeviewBtn:function(e){
				        	var self = this;
							var obj = $(e.target);
							var roomId =obj.attr("data-roomId");
							if(roomId == ""){
								return;
							}
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.surveyTemplate.preview&surveyTemplateId="+roomId,
				                success:function(data){
				                	console.log(data);
				                	if (data.code == "1") {
										self.childrenView.roomViewSnippetView = new RoomViewSnippetView();
										self.childrenView.roomViewSnippetView.render(data,'preview');
				                	}
				                }
							});
				        },
						addTemplate : function()
						{
							var type = this.type;
						    var title = "公共模板"
						    var templateHtml = addCommontemplate;
							if(this.type == "my")
							{
								title = "我的模板"
								templateHtml = 	addTemplate;
							}else if(this.isAdmin == '0')
							{
								title = "我的模板"
								templateHtml = 	addTemplate;
							}
							var groupSelect = this.initGroupSelect();
							this.$el.empty();
							var data = {
									id:"",
									title:title,
									titleHtml:title,
									selectHtml:groupSelect
							}
							var html = _.template(templateHtml,{data:data});
							this.$el.append(html);
							this._initCommonQuestions();
							this.reloadCkeditor();
						},
						// 初始化常用问题html
				        _initCommonQuestions:function(){
				        	var html = _.template(commonQuestionsTemplate,{});
							this.$el.find(".SID-commonQuestion-div").append(html);
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
				        },
				        initGroupSelect : function()
				        {
				        	var select = '';
				        	this.$el.find(".SID-commonGroups li").each(function(){
				                var id = $(this).find(".SID-commonGroup").attr("defaultid");
				                var name = $(this).find(".SID-commonGroup").attr("defaultvalue");
				                select +='<option value="'+id+'">'+name+'</option>';
				            })
				            return select;
				        }
				        
						
					});
			return SurveyTemplateViewSnippetView;
		});
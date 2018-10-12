define([
       "jquery" , 'views/communication-base-view'
      ,'text!../../templates/meet/surveyQuestionTemplate.html'
	  ,'text!../../templates/meet/surveyPreviewTemplate.html'
], function($, CommunicationBaseView
		,questionTemplate,detailTemplate){

    var view = Backbone.View.extend({
        events: {
            'click .SID-prev-btn':'_onClickPrevBtn',
            'click .SID-next-btn':'_onClickNextBtn'
        },
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.surveyId="";
            this.surveyData={};
            this.showFlag = "show";
            this.detailData ={};
            this.commonDialog;
        },
        render:function(data){
        	var self = this;
        	this.detailData = data;
            //渲染页面
        	var html = _.template(detailTemplate,{data:data.survey});
			self.$el.append(html);
			//题目绘制
			var title = data.survey.title;
			if (title.length > 10) {
				title = title.substring(0,10)+"...";
			}
			var commonDialog = fh.commonOpenDialog('roomDetailDialog', title, 400, 574, this.el);
			commonDialog.addBtn("cannel","关闭",function(){
				self.destroy();
				commonDialog.cancel();
			});
			this.commonDialog = commonDialog;
			self._initQuestion(data.survey.suQuestion);
        },
        destroy:function(){
            this.undelegateEvents();
            this.unbind();
            this.$el.empty();
			this.remove();
        },
        _initQuestion:function(questions){
        	var self = this;
        	var template = questionTemplate;
			var divHtml = "";
			var pageNum = 1;
			var questionArr = [];
			for(var i=0,question; question=questions[i++];) {
				if (question.type == "7") {
					// 分页
					if (questionArr.length > 0) {
						var pageHtml=_.template(template,{
	                        imgPath: "",
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
                        imgPath: "",
                        questionArr:questionArr,
                        pageNum:pageNum
                    });
					divHtml += pageHtml;
					pageNum++;
					questionArr = [];
				}
			}
			$(this.commonDialog.dg).find("#ID-question-div").html(divHtml);
			if (pageNum > 2) {
				$(this.commonDialog.dg).find(".SID-prev-btn").hide();
				$(this.commonDialog.dg).find(".SID-next-btn").show();
			}
			this._updateQuestionSequ();
        },
		_updateQuestionSequ:function(){
			var allLiObj = $(this.commonDialog.dg).find(".SID-question-ul>.SID-question-li").find(".SID-question-sequ");
			for(var i=0,sequObj; sequObj=allLiObj[i++];) {
				$(sequObj).attr("data-sequ",i);
				$(sequObj).html("Q"+i+".");
			}
			return;
		},
        _onClickPrevBtn:function(e){
        	// 当前页
        	var currentPageObj = $(this.commonDialog.dg).find(".SID-question-ul[class$='SID-active']");
        	var pageNum = currentPageObj.attr("data-pageNum");
        	// 恢复默认
        	currentPageObj.removeClass("SID-active");
        	currentPageObj.hide();
        	// 按钮控制
        	if (currentPageObj.prev().prev().length>0) {
        		$(this.commonDialog.dg).find(".SID-prev-btn").show();
        	} else {
        		$(this.commonDialog.dg).find(".SID-prev-btn").hide();
        	}
        	// 翻页
        	$(this.commonDialog.dg).find(".SID-next-btn").show();
        	currentPageObj.prev(".SID-question-ul").addClass("SID-active");
        	currentPageObj.prev(".SID-question-ul").show();
        	$("#lhgdg_inbox_roomDetailDialog").scrollTop(0);
        },
        _onClickNextBtn:function(e){
        	var currentPageObj = $(this.commonDialog.dg).find(".SID-question-ul[class$='SID-active']");
        	var pageNum = currentPageObj.attr("data-pageNum");
        	currentPageObj.removeClass("SID-active");
        	currentPageObj.hide();
        	if (currentPageObj.next().next().length>0){
        		$(this.commonDialog.dg).find(".SID-next-btn").show();
        	} else {
        		$(this.commonDialog.dg).find(".SID-next-btn").hide();
        	}
        	$(this.commonDialog.dg).find(".SID-prev-btn").show();
        	currentPageObj.next(".SID-question-ul").addClass("SID-active");
        	currentPageObj.next(".SID-question-ul").show();
        	$("#lhgdg_inbox_roomDetailDialog").scrollTop(0);
        }
		
    });

    return view;
});
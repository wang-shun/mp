define([
       "jquery" , 'views/communication-base-view'
      ,'text!../../templates/meet/answerQuestionTemplate.html'
	  ,'text!../../templates/meet/answerPreviewTemplate.html'
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
			var commonDialog = fh.commonOpenDialog('roomDetailDialog1', title, 400, 574, this.el);
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
				if (question.type != "7") {
					questionArr.push(question);
				}
			}
			var pageHtml=_.template(template,{
                imgPath: "",
                questionArr:questionArr,
                pageNum:pageNum
            });
			$(this.commonDialog.dg).find("#ID-question-div").html(pageHtml);
			this._updateQuestionSequ();
			if (self.showFlag == "show") {
				self.$el.find("#ID-question-div").find("input,select,textarea").attr("disabled","disabled");
			}
        },
		_updateQuestionSequ:function(){
			var allLiObj = $(this.commonDialog.dg).find(".SID-question-ul>.SID-question-li").find(".SID-question-sequ");
			for(var i=0,sequObj; sequObj=allLiObj[i++];) {
				$(sequObj).attr("data-sequ",i);
				$(sequObj).html("Q"+i+".");
			}
			return;
		}
    });

    return view;
});
define(
	['jquery', 'views/communication-base-view'
	, 'text!../../templates/detail/planDetail.html'],
	function($, CommunicationBaseView, planDetailTemplate) {
		var IndexRouterSnippentView = CommunicationBaseView
			.extend({
				events: {
					'click .SID-BtnPlanDetailBack':'back',
					'click .SID-approve-submit' : '_onSubmitApprove'
				},
				initialize: function() {
					var self = this;
					this.preRender();
					this.parentObj;
					this.toast=new Prompt("3",{
						parent:self.$el[0]
					});
				},
				render: function(parentObj) {
					this.parentObj = parentObj;
					var listData=this.parentObj.dayListData;
					var data={};
					if(this.parentObj.planDetailData)data=this.parentObj.planDetailData;
					var html = _.template(
						planDetailTemplate,{
							data:data
						}
					);
					$(".SID-planDetail-snippet").append(html);

					return false;
				},
				refresh: function() {
					this.$el.empty();
					this.render(this.parentObj);
				},
				destroy: function() {
					this.undelegateEvents();
					this.unbind();
					this.$el.empty();
				},
				//Method
				//Events Handler
				back:function(e){
                	this.destroy();
                    history.go(-1);
					return false;
				},
				//初始化
				_onLoad:function(){
					var self=this;
				},
				_bindClick:function(){
					var self = this;
					self.$el.find(".SID-showimg").bind("click", function(e){
						self._onClickShowImg(e);
					});
				},
				_onSubmitApprove:function(e){
					var type = $(e.currentTarget).attr("data-type");
					var reservedId = $(e.currentTarget).attr("data-reservedId");
					var self = this;
					if (reservedId == "")
						return;
					if (type != "0" && type != "1")
						return;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParamNoV');
					var url = servicePath;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&v=2.0&method=mapps.meetingroom.reserved.updateapprove&reservedId="+reservedId
						      +"&approved=" + type,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								self.toast.setText("审批成功");
								self.toast.show();
								setTimeout(function(){
									self.back();
								}, 1000);
							} else if (data.code == "300013") {
								self.toast.setText("此预约不存在");
								self.toast.show();
								setTimeout(function(){
									self.back();
								}, 1000);
							} else if (data.code == "300010") {
								self.toast.setText("此预约状态已变更");
								self.toast.show();
								setTimeout(function(){
									self.back();
								}, 1000);
							} else if (data.code == "300017") {
								self.toast.setText("此预约已过期");
								self.toast.show();
								setTimeout(function(){
									self.back();
								}, 1000);
							} else {
								self.toast.setText("审批异常");
								self.toast.show();
							}
						},
						error : function(){
							$(".SID-loading").hide();
							self.toast.setText("审批异常");
							self.toast.show();
						}
					});
				}
			});

		return IndexRouterSnippentView;
	});
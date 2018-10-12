define(
	['jquery', 'views/communication-base-view'
	, 'text!../../templates/result/resultDetail.html'],
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
					this.toast=new Prompt("6",{
						parent:self.$el[0]
					});
					this.reservedId;
				},
				render: function(reservedId) {
					this.reservedId = reservedId;
					this._getReservedDetail();
					return false;
				},
				_getReservedDetail:function(){
					var self = this;
					var appContext = this.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					var url = servicePath;
					$(".SID-loading").show();
					$.ajax({
						type : "POST",
						url : url,
						data : ropParam + "&method=mapps.meetingroom.reserved.detailone&reservedId="+self.reservedId,
						success : function(data) {
							$(".SID-loading").hide();
							if (data.code == "1") {
								if (data.reservedDetailList&&data.reservedDetailList.length>0) {
									var detailObj = data.reservedDetailList[0];
								}
								var html = _.template(
									planDetailTemplate,{
										data:detailObj
									}
								);
								$(".SID-planDetail-snippet").append(html);
							} else {
								self.toast.setText(data.message);
								self.toast.show();
							}
						},
						error : function(){
							$(".SID-loading").hide();
							self.toast.setText("数据获取失败");
							self.toast.show();
						}
					});
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
					mplus.closeWindow();
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
					var self = this;
					var type = $(e.currentTarget).attr("data-type");
					var reservedId = self.reservedId;
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
								self.$el.find(".SID-btn-box").hide();
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
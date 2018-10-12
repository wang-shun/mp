define(
		[ 'jquery', 'views/communication-base-view','viewer'],
		function($, CommunicationBaseView) {
			var MoreView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-hasOther':'_onClickHasOther'
						},
						initialize : function() {
							this.data = {};
						},
						render : function(initData) {
							var self = this;
							this.data = initData;
							this.$el.append(initData.setHtml);
							var title = "类型名称";
							var commonDialog = fh.commonOpenDialog('roomDetailDialog', title, 400, 160, this.el);
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("ok","确定",function(){
								self._sumbitForm(commonDialog);
							});
							return this;
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						_sumbitForm : function(commonDialog){
							if (this.data.type == 'option') {
								var customInput = "0";
								this.data.ddObj.find(".SID-other-input").hide();
								this.data.ddObj.find(".SID-other-span").hide();
								if (this.$el.find(".SID-hasOther").is(":checked")) {
									if (this.$el.find(".SID-optionRequired").is(":checked")) {
										customInput = "2";
									} else {
										customInput = "1";
										this.data.ddObj.find(".SID-other-span").show();
									}
									this.data.ddObj.find(".SID-other-input").val("");
									this.data.ddObj.find(".SID-other-input").show();
								}
								this.data.ddObj.attr("custom_input",customInput);
							} else if (this.data.type == 'question') {
								var required = "1";
								if (this.$el.find(".SID-questionRequired").is(":checked")) {
									required = "0";
									this.data.liObj.find(".SID-required-flag").hide();
								} else {
									this.data.liObj.find(".SID-required-flag").show();
								}
								this.data.liObj.attr("question_required",required);
							}
							commonDialog.cancel();
							this.destroy();
						}
					});
			return MoreView;
		});
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
							var title = "题目设置";
							if (this.data.type == 'option') {
								title = "选项设置";
							}
							var commonDialog = fh.commonOpenDialog('roomDetailDialog', title, 400, 200, this.el);
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
						_onClickHasOther:function(e){
							if ($(e.currentTarget).is(":checked")) {
								this.$el.find(".SID-optionRequired-li").show();
								this.$el.find(".SID-optionRequired").attr('checked','checked');
							} else {
								this.$el.find(".SID-optionRequired-li").hide();
							}
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
								if (this.data.questionType == "3") {
									var selLength = this.data.selLength;
									var selmin = $(commonDialog.dg).find(".SID-question-selmin").val();
									var selmax = $(commonDialog.dg).find(".SID-question-selmax").val();
									var number = /^([1-9]\d{0,2})$/;
									if ((selmin != "" && !number.test(selmin)) || (selmax != "" && !number.test(selmax))) {
										$(commonDialog.dg).find(".SID-set-error").html("请填写1-999的数字");
										return;
									} else if (selmin != "" && selmin > selLength) {
										$(commonDialog.dg).find(".SID-set-error").html("最少选择数不能超过选项数量");
										return;
									} else if (selmax != "" && selmax > selLength) {
										$(commonDialog.dg).find(".SID-set-error").html("最多选择数不能超过选项数量");
										return;
									} else if (selmin != "" && selmax != "" && selmin > selmax) {
										$(commonDialog.dg).find(".SID-set-error").html("最多选择数不能小于最少选择数量");
										return;
									}
									if (this.$el.find(".SID-questionRequired").is(":checked") && (selmin != "" || selmax != "")) {
										$(commonDialog.dg).find(".SID-set-error").html("该题是非必答题，不应设置最多最少选项");
										return;
									}
									this.data.liObj.attr("question_selMin",selmin == "" ? "0" : selmin);
									this.data.liObj.attr("question_selMax",selmax == "" ? "0" : selmax);
								}
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
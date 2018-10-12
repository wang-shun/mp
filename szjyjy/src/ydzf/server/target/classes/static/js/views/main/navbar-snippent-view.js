define(
		[ 'jquery', 'views/communication-base-view', 'selectbox' ],
		function($, CommunicationBaseView) {
			var NavbarSnippentView = CommunicationBaseView
					.extend({
						events : {
							'change select.SID-orgList' : 'orgChange'
						},
						initialize : function() {
							this.preRender();
						},
						render : function() {
							console.log('footer setup...');
							this.setUpNavbarInfo();
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
						},
						setUpNavbarInfo : function() {
							var self = this;
							this.$el
									.find('p')
									.html(
											'<span class="fhicon-uniE178"></span>您现在所在位置 >');
							var url = this.constants.AJAX.PATH
									+ "/org/manage/orgadminList!getLoginOrgList.action";
							$
									.ajax({
										type : "POST",
										url : url,
										async : false,
										data : '',
										success : function(data) {
											if (data.uType == "2") {
												var text = "<select class='SID-orgList' name= 'SID-orgList'>";
												var orgList = data.orgList;
												var orgId = data.orgId;
												for ( var i = 0; i < orgList.length; i++) {
													var selected = "";
													if (orgId == orgList[i].orgid) {
														selected = "selected";
													}
													text = text
															+ "<option value ='"
															+ orgList[i].orgid
															+ ","
															+ orgList[i].ecid
															+ "' "
															+ selected
															+ " >"
															+ orgList[i].orgname
															+ "</option>";
												}
												text = text + "</select>";
												$(".SID-select-ecid").append(
														text);
												$(".SID-select-ecid").addClass('selChannel right selChannelTop');
											}
											// $('.SID-orgList').change();
											self.$el.find(".SID-orgList").selectbox();

										},
										error : function() {
											fh.alert("获取机构数据失败！");
										}
									});
						},

						orgChange : function() {
							var self = this;
							var orgIdAndEcid = $(".SID-orgList").val();
							var url = this.constants.AJAX.PATH
									+ "/org/manage/orgadminList!setOrg.action";
							$.ajax({
								type : "POST",
								url : url,
								async : false,
								data : 'orgIdAndEcid=' + orgIdAndEcid,
								success : function(data) {
									self.eventHub
											.publishEvent('ORG_CHANGE', '');
								},
								error : function() {
								}
							});

						},
						subscribeEvents : function() {
							var self = this;
							this.eventHub
									.subscribeEvent('MENU_GROUP_CLICKL',
											function(msg) {
												self.$el.find('p').html(
														'<span class="fhicon-uniE178"></span>您现在所在位置 > '
																+ '<a>' + msg
																+ '</a>');
											});
							this.eventHub.subscribeEvent('MENU_CLICKL',
									function(msg) {
										self.$el.find('p').html(
												'<span class="fhicon-uniE178"></span>您现在所在位置 > '
														+ '<a>'
														+ msg.menuGroupName
														+ '</a>' + ' > '
														+ '<a>' + msg.menuName
														+ '</a>');
									});
						}
					});

			return NavbarSnippentView;
		});
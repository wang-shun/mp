define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/welcome/welcomeTemplate.html','text!../../templates/welcome/listTemplate.html'],
		function($, CommunicationBaseView,Template,listTemplate) {
			var RoomSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.chaildView = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							return this;
						},
						refresh : function() {
						},
						destroyBusinessViews : function(){
							$.each(this.chaildView, function(index, view) {
								 view.destroy();
							});		
							this.chaildView = {};
						},		
						destroy : function() {
							this.destroyBusinessViews()
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						setContentHTML : function (){
							var self = this;
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);

							$("#ID-DatePicker").datepicker({});
							$("#ID-DatePicker").datepicker( "setDate", new Date() );
							$("#ID-DatePicker").on("change", function() {
								self.initData();
							});
							$("#ID-DatePickerPrev").click(function(e){
								var currentDate = $("#ID-DatePicker" ).datepicker("getDate");
								currentDate.setDate(currentDate.getDate()-1);
								$("#ID-DatePicker").datepicker( "setDate", currentDate );
								$("#ID-DatePicker").trigger("change");
							});
							$("#ID-DatePickerNext").click(function(e){
								var currentDate = $("#ID-DatePicker" ).datepicker("getDate");
								currentDate.setDate(currentDate.getDate()+1);
								$("#ID-DatePicker").datepicker( "setDate", currentDate );
								$("#ID-DatePicker").trigger("change");
							});
							this.initData();
						},
						initData:function(){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.meetingroom.reserved.servicedetail&reservedDate="+$("#ID-DatePicker").val(),
								success:function(data){
									if (data.code == "1") {
										var html = _.template(listTemplate, {
											'imgPath': self.constants.IMAGEPATH,
											'room' : data.roomList
										});
										self.$el.find(".SID-body").html(html);
									}
								},
								error:function(){
									fh.alert("数据获取失败！");
								}
							});
						}
					});
			return RoomSnippetView;
		});
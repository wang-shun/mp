define([
    "app",

    "text!../../templates/meet/meetQrcodeTemplate.html",
    "text!../../templates/meet/meetQrcodeAxisTemplate.html"
], function(app, meetQrcodeTemplate, meetQrcodeAxisTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            meetQrcodeTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            /*DOM*/
            this.axisContainer=document.getElementById("ID-MeetQrcode-Axis");
            this.$el.find("article")[0].style.minHeight=(window.innerHeight-84)+"px";

            //加载数据
            this.loadData();
        },
        refresh:function(){
            console.log("会议扫码页面meetQrcodeView：刷新");
            this.loadData();
        },
        destroy:function(){
            console.log("会议扫码页面meetQrcodeView：移除");
        },
        loadData:function(){
            var self=this;
            
            var data=app.signinSequList;
            //没有数据
            if(data.length<=0){
                self.axisContainer.innerHTML=app.nodataHTML;
                return;
            }
            //编译渲染
            var html=_.template(meetQrcodeAxisTemplate,{
                imgPath: app.constants.IMAGEPATH,
                data:data
            });
            self.axisContainer.innerHTML=html;
        },

        /*=========================
          Method
          ===========================*/

        /*=========================
          Events
          ===========================*/

		events: {
			'click .SID-back':'back',
			'click .SID-signIn':'_onClickSignIn'
		},
		_onClickSignIn:function(e){
			var self = this;
			var sequId = $(e.currentTarget).attr("data-signId");
			var time = $(e.currentTarget).attr("data-time");
			
			var url = app.serviceUrl;
			var ropParam = app.ropMethod.signin+"&sessionId="+window.token;
            
			mplus.scanQRCode({
			    success: function (res) {
			    	var result = res.resultStr; // 扫码返回的结果
			    	var data = ropParam + "&participantId="+result+"&sequId="+sequId;
			    	$.ajax({
		            	url : url,
						data : data,
		        		success : function(data) {
		        			if(data.code == "1"){
		        				app.toast.setText(data.personName+"第"+time+"次签到成功");
		        				app.toast.show();
		        				app.routerViews.meetDetailView.refresh(app.routerViews.meetDetailView.meetingId);
		        				setTimeout(function(){
		        					//self.parentObj.reloadDetailSign();
		        					//self.back();
		        				},750);
		        			}else if(data.code == "100002"){
		        				app.toast.setText("请勿重复签到");
		        				app.toast.show();
		        			}else{
		        				app.toast.setText("请扫描所服务会议的二维码");
		        				app.toast.show();
		        			}
		        			//连续扫描
		        			setTimeout(function(){
		        				app.loading.hide();
		        				$(e.currentTarget).click();
	        				},500);
		        		},
						error : function(e){
							app.toast.setText(res.errMsg);
							app.toast.show();
						}
					});
				},
				error: function (res) {
					//self.toast.setText(res.errMsg);
					//self.toast.show();
				}
			});
		}
        /*=========================
          Event Handler
          ===========================*/
    });

    return view;
});
define([
    "app",

    "text!../templates/detail/emailTemplate.html"
], function(app, detailTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.actId = "";
        	this.actData={};
        },
        render:function(id){
        	this.actId = id;
        	// 关于必填项控制的信息
        	this.actData = app.routerViews.detailView.actData;
            console.log("详情Email:根据id："+id);
            var self = this;

            //渲染页面
            var html=_.template(
            detailTemplate,{
                imgPath: app.constants.IMAGEPATH,
                actData : this.actData
            });
            this.$el.append(html);

            //header
            this.header=this.$el.find("header")[0];
            this.article=this.$el.find("article");
            //更新表单控件
            app.formControls.update();
            //表单
            this.formContainer=document.getElementById("ID-Email-Form");
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
        },
        refresh:function(id){
        	this.actId = id;
        	this.actData = app.routerViews.detailView.actData;
            console.log("详情Email：刷新");
            this.$el.find("article").scrollTop(0);
        },
        destroy:function(){
            console.log("详情Email：移除");
            this.$el.find("#ID-Email-mail").val("");
        },
        /*=========================
          Method
          ===========================*/
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Back' : '_onBack',
            'click .SID-EmailSubmitBtn':'_onClickSubmitBtn'
        },
        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(){
        	this.destroy();
            history.go(-1);
        },
        _validate:function(){
            var validator=new Validator();
            validator.add(this.formContainer["NID-Email-mail"],[{
                rule:'required',
                errorMsg:'请输入邮箱地址'
            }]);
            validator.add(this.formContainer["NID-Email-mail"],[{
                rule:'mail',
                errorMsg:'邮箱地址格式错误'
            }]);
            var error=validator.start();
            return error;
        },
        _onClickSubmitBtn:function(){
        	var self = this;
            var error=this._validate();
            if(error){
            	self.loadPanel.hide();
                app.toast.setText(error.msg);
                app.toast.show();
                //error.field.parentNode.classList.add("inputbox-error");
                return;
            }
            var formData=new Form("#ID-Email-Form").serializeJson();
            var data={};
            data["actId"] = this.actId;
            data["email"] = formData["NID-Email-mail"][0];
            
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.sendEmail+"&sessionId="+window.token;
            self.loadPanel.show();
            $.ajax({
            	url : app.serviceUrl+"?"+ropParam,
                data : data,
				type : "POST",
				dataType: 'json',
				success : function(data) {
					if(data.code=="1") {
						app.toast.setText("发送成功");
						app.toast.show();
						setTimeout(function(){
							self.destroy();
				            history.go(-1);
			            },1000);
					} else {
						app.toast.setText(data.message);
						app.toast.show();
					}
				},
				error : function(){
					app.toast.setText("发送失败");
					app.toast.show();
				},
                complete:function(){
                	self.loadPanel.hide();
                }
			});
        }
    });

    return view;
});
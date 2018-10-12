define([
    "app",

    "text!../templates/detail/enterTemplate.html"
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
            console.log("详情Enter:根据id："+id);
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
            this.formContainer=document.getElementById("ID-Enter-Form");
            //计数控件
            this.countValues=new CountValues({
                onInput:function(e){
                    var input=e.target;
                    input.nextElementSibling.innerHTML=input.value.length+"/100";
                    //console.log("输入中");
                },
                onInputOut:function(e){
                    //console.log("超过数字限制时");
                },
                onInputIn:function(e){
                    //console.log("没有超过数字限制时");
                }
            });
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
            if (this.actData.phone == "1") {
            	this.$el.find("#ID-Enter-Phone").val(app.phone);
            }
            this.setRequired();
        },
        refresh:function(id){
        	this.actId = id;
        	this.actData = app.routerViews.detailView.actData;
            console.log("详情Enter：刷新");
            this.$el.find("article").scrollTop(0);
            this.$el.find("#ID-Enter-Form")[0].reset();
            if (this.actData.phone == "1") {
            	this.$el.find("#ID-Enter-Phone").val(app.phone);
            }
            this.setRequired();
        },
        setRequired:function(){
        	this.$el.find(".app-required").html("");
        	if (this.actData.phone == "1") {
        		this.$el.find(".SID-title-phone").html("*");
            }
            if (this.actData.sex == "1") {
            	this.$el.find(".SID-title-sex").html("*");
            }
            if (this.actData.idCard == "1") {
            	this.$el.find(".SID-title-idcard").html("*");
            }
            if (this.actData.remark == "1") {
            	this.$el.find(".SID-title-remark").html("*");
            }
        },
        destroy:function(){
            console.log("详情Enter：移除");
            this.$el.find("#ID-Enter-Form")[0].reset();
        },
        /*=========================
          Method
          ===========================*/
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Back' : '_onBack',
            'click .SID-EnterSubmitBtn':'_onClickSubmitBtn'
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
            if (this.actData.phone == "1") {
	            validator.add(this.formContainer["NID-Enter-Phone"],[{
	                rule:'required',
	                errorMsg:'请输入手机号'
	            }]);
            }
            validator.add(this.formContainer["NID-Enter-Phone"],[{
                rule:'phone',
                errorMsg:'手机号码不正确'
            }]);
            if (this.actData.sex == "1") {
	            validator.add(this.formContainer["NID-Enter-Sex"],[{
	                rule:'required',
	                errorMsg:'请选择性别'
	            }]);
            }
            if (this.actData.idCard == "1") {
	            validator.add(this.formContainer["NID-Enter-IdCard"],[{
	                rule:'required',
	                errorMsg:'请输入身份证号'
	            }]);
            }
            if (this.actData.remark == "1") {
	            validator.add(this.formContainer["NID-Enter-Remark"],[{
	                rule:'required',
	                errorMsg:'请输入备注'
	            }]);
            }

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

            var idCard = this.$el.find("#ID-Enter-IdCard").val();
            if (idCard != "") {
            	this.idvalidator = new IDValidator();
            	var bool = this.idvalidator.isValid(idCard);
            	if (!bool) {
            		app.toast.setText("身份证号码不合法");
                    app.toast.show();
                    return;
            	}
            }
            var formData=new Form("#ID-Enter-Form").serializeJson();
            var data={};
            data["actId"] = this.actId;
            data["phone"] = formData["NID-Enter-Phone"][0];
            if ($("[name='NID-Enter-Sex']:checked").length>0) {
            	data["sex"] = formData["NID-Enter-Sex"][0];
            } else {
            	data["sex"] = "";
            }
            data["idCard"] = formData["NID-Enter-IdCard"][0];
            data["remark"] = formData["NID-Enter-Remark"][0];
            
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.enterActivity+"&sessionId="+window.token;
            self.loadPanel.show();
            $.ajax({
            	url : app.serviceUrl+"?"+ropParam,
                data : data,
				type : "POST",
				dataType: 'json',
				success : function(data) {
					if(data.code=="1") {
						app.toast.setText("活动报名成功");
						app.toast.show();
						app.routerViews.detailView.refresh(self.actId);
						if (!app.routerViews.detailView.homePage) {
							app.routerViews.indexView.refresh();
						}
						setTimeout(function(){
							self.destroy();
							history.go(-1);
			            },1000);
					} else if(data.code=="300022" || data.code=="300019") {
						app.toast.setText(data.message);
						app.toast.show();
						app.routerViews.detailView.errorChangeBtn();
						setTimeout(function(){
							self.destroy();
				            history.go(-1);
			            },1000);
					} else if(_.intersection([data.code],app.errorCode)) {
						app.toast.setText(data.message);
						app.toast.show();
					} else {
						app.toast.setText(data.message);
						app.toast.show();
					}
				},
				error : function(){
					app.toast.setText("活动报名失败");
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
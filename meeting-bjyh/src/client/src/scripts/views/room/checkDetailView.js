define([
    "app",

    "text!../../templates/room/checkDetailTemplate.html",
    "text!../../templates/room/checkDetailContentTemplate.html"
], function(app, checkDetailTemplate, checkDetailContentTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.flag = 0;
        },
        render:function(flag){
        	this.flag = flag;
            var self = this;
            //渲染页面
            var data=app.approvalDetail;
            var html=_.template(
            checkDetailTemplate,{
                imgPath: app.constants.IMAGEPATH,
                data: data
            });
            this.$el.append(html);
            this.changeBtnStatus(data);
            //详情数据
            this.listContainer=this.$el.find("#ID-CheckDetail-Content");
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            //加载数据
            this.loadData();
        },
        refresh:function(flag){
        	this.flag = flag;
            console.log("审批详情页checkDetailView：刷新");
            this.loadData();
        },
        destroy:function(){
            console.log("审批详情页checkDetailView：移除");
        },
        changeBtnStatus:function(approvalDetail){
        	var html="";
        	if (approvalDetail.approved=="0" || approvalDetail.status=="a"){
        		html='<button class="button color-cancel SID-Check-No"><i class="icon icon-rdorefuse size20"></i><span>不同意</span></button><button class="button color-success SID-Check-Yes"><i class="icon icon-rdook size20"></i><span>同意</span></button>';
        	} else if (approvalDetail.status=="r") {
        		html='<button class="button color-cancel"><i class="icon icon-rdorefuse size20"></i><span>不同意</span></button><button disabled class="button color-success"><i class="icon icon-rdook size20"></i><span>同意</span></button>';
        	} else {
        		html='<button disabled class="button color-cancel"><i class="icon icon-rdorefuse size20"></i><span>不同意</span></button><button class="button color-success"><i class="icon icon-rdook size20"></i><span>同意</span></button>';
        	}
        	this.$el.find(".SID-CheckBtn").html(html);
        },
        loadData:function(){
        	app.loading.show();
            var self=this;
            var data=app.approvalDetail;
            console.log(data);
            //没有数据
            if(data.length<=0){
                self.listContainer[0].innerHTML=app.nodataHTML;
                return;
            }
            this.changeBtnStatus(data);
            //编译渲染
            var listHTML=_.template(checkDetailContentTemplate,{
                imgPath: app.constants.IMAGEPATH,
                data:data
            });
            self.listContainer[0].innerHTML=listHTML;
            app.loading.hide();
        },

        /*=========================
          Model
          ===========================*/

        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back':'_onBack',
            //'submit #ID-Search-Form' : '_onSubmit'
        	'click .SID-Check-No':'_onClickNo',
        	'click .SID-Check-Yes':'_onClickYes',
            'click .SID-phone':'_onClickPhone'
        },
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onBack:function(){
            if(app.isHomePage){
            	mplus.closeWindow();
                return;
            }
            history.go(-1)
        },
        _onClickPhone:function(e){
        	var phone = e.currentTarget.getAttribute("data-phone");
        	if (phone!="") {
        		mplus.callPhone({
        			phones: [phone], // 号码数组
        			success: function (res) {
    			    },
    			    fail: function (res) {
    				}
    			});
        	}
        },
        _onClickYes:function(){
        	var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.updateApprove+"&sessionId="+window.token;
            var pageParam = "&reservedId="+app.approvalDetail.reservedId
            +"&approved=1";
            //status
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                    console.log(data);
                    self._callMsg(data);
                },
                error:function(msg){
                    self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        _onClickNo:function(){
        	var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.updateApprove+"&sessionId="+window.token;
            var pageParam = "&reservedId="+app.approvalDetail.reservedId
            +"&approved=0";
            //status
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                    console.log(data);
                    self._callMsg(data);
                },
                error:function(msg){
                    self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        _callMsg:function(data){
        	var subFlag = true;
        	if (data.code == "1") {
				app.toast.setText("审批成功");
				app.toast.show();
			} else if (data.code == "300013") {
				app.toast.setText("此预约不存在");
				app.toast.show();
			} else if (data.code == "300010") {
				app.toast.setText("此预约状态已变更");
				app.toast.show();
			} else if (data.code == "300017") {
				app.toast.setText("此预约已过期");
				app.toast.show();
			} else {
				subFlag = false;
				app.toast.setText("审批异常");
				app.toast.show();
			}
        	if (subFlag) {
        		if (this.flag==1) {
        			setTimeout(function(){
        				mplus.closeWindow();
        			}, 1000);
            	} else {
					//app.routerViews.checkView.refresh();
					app.routerViews.indexView.refreshByIndex(2);
					setTimeout(function(){
						history.go(-1);
					}, 1000);
            	}
        	}
        },
        /*=========================
          Event Handler
          ===========================*/
        _onSubmit:function(e){
            e.preventDefault();
            alert("提交");
        }
    });

    return view;
});
define([
    "app",

    "text!../../templates/index/indexTemplate.html",
    "text!../../templates/index/indexListTemplate.html"
], function(app, indexTemplate, indexListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        },
        render:function(){
            var self = this;

            //渲染页面
            var html=_.template(
            indexTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //this.isAdmin();
            //内容区域
            this.article=this.$el.find("article");

            //列表
            this.listContainer=this.$el.find("#ID-Index-List");

            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.$el[0],
                css:{
                    top:"44px",
                    backgroundColor:"#20aeff"
                }
            });

            this.popConfirm=new Alert("放弃编辑此页面？",{
                onClickCancel:function(e){
                    e.hide();
                }
            });
            
            //下拉刷新
            this.drag=DfPull({
                overflowContainer:this.article[0],
                parent:this.article[0],
                onTopRefresh:function(e){
                    console.log("头部刷新");
                    self.loadData(true);
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
            this.drag.bottomNoData();
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            //加载数据
            this.loadData(false);
        },
        refresh:function(){
            console.log("首页：刷新");
            this.$el.find("article").scrollTop(0);
            this.loadData(false);
        },
        destroy:function(){
            console.log("首页：移除");
        },
        loadData:function(flag){
        	if (!flag) {
        		app.loading.show();
        	}
            var self = this;
            if(window.serviceUrl != undefined){
            	app.serviceUrl = window.serviceUrl;
            }
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.borrowDeviceList+"&sessionId="+window.token;
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                    if(data.code!=1){//请求错误
                        return;
                    }
                    self.errorPanel.removeClass("active");
                    //没有数据
                    if(data.deviceList.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        //标识头部与尾部状态
                        self.drag.topNoData();
                        self.drag.bottomNoData();
                        return;
                    }
                    //编译渲染
                    var listHTML=_.template(indexListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data.deviceList
                    });
                    self.drag.topNoData();
                    if (flag) {
                    	self.prompt.show();
                    }
                    self.listContainer[0].innerHTML=listHTML;
                },
                error:function(msg){
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        isAdmin:function(){
        	var self = this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.isAdmin+"&sessionId="+window.token;
            $.ajax({
            	url : url,
				data : ropParam,
				success:function(ajax){
					console.log(ajax);
					if(ajax.code == "1"){
						if (ajax.adminFlag) {
							self.$el.find("#ID-Index-BtnCheck").show();
						} else {
							self.$el.find("#ID-Index-BtnCheck").hide();
						}
					}
				},
				error:function(){
				}
			});
        },
        /*=========================
          Method
          ===========================*/
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click #ID-Index-BtnAdd':'_onClickBtnAdd',
            'click #ID-Index-BtnCheck':'_onClickBtnCheck',
            'click .SID-Index-Li': '_onClickLi',
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onClickBtnAdd:function(e){
			var self = this;
			var url = app.serviceUrl;
			var ropParam = app.ropMethod.deviceCheck+"&sessionId="+window.token;
        	mplus.scanQRCode({
			    success: function (res) {
			    	var result = res.resultStr; // 扫码返回的结果"xiao M note##deviceId789";//
			    	// 设备型号##序列号
			    	if (result == "") {
			    		app.toast.setText("无效二维码");
        				app.toast.show();
			    		return;
			    	} 
			    	var deviceArr = result.split("##");
			    	if (deviceArr.length != 2 || deviceArr[0] == "" || deviceArr[1] == "") {
			    		app.toast.setText("无效二维码");
        				app.toast.show();
			    		return;
			    	}
			    	var deviceName = encodeURIComponent(deviceArr[0]);
			    	var deviceId = encodeURIComponent(deviceArr[1]);
			    	var data = ropParam + "&deviceName="+deviceName+"&deviceId="+deviceId;
			    	$.ajax({
		            	url : url,
						data : data,
		        		success : function(data) {
		        			if(data.code == "300007" || data.code == "300008" || data.code == "300009"){
		                        self.popConfirm.setText(app.confirmMsg[data.code]);
		                        self.popConfirm.setOnClickOk(function(){
		                            self._onClickBtnAdd1(result);
		                        });
		                        self.popConfirm.show();
		        			} else if(data.code == "300002" || data.code == "300003" || data.code == "300004" || data.code == "300005" || data.code == "300006" || data.code == "300010"){
		        				app.toast.setText(data.message);
		        				app.toast.show();
		        			} else{
		        				app.toast.setText("数据处理失败");
		        				app.toast.show();
		        			}
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
        },
        _onClickBtnAdd1:function(resultStr){
        	this.popConfirm.hide();
			var self = this;
			var url = app.serviceUrl;
			var ropParam = app.ropMethod.deviceSubmit+"&sessionId="+window.token;
	    	var result = resultStr; // 扫码返回的结果"xiao M note##deviceId789";//
	    	// 设备型号##序列号
	    	if (result == "") {
	    		app.toast.setText("无效二维码");
				app.toast.show();
	    		return;
	    	} 
	    	var deviceArr = result.split("##");
	    	if (deviceArr.length != 2 || deviceArr[0] == "" || deviceArr[1] == "") {
	    		app.toast.setText("无效二维码");
				app.toast.show();
	    		return;
	    	}
	    	var deviceName = encodeURIComponent(deviceArr[0]);
	    	var deviceId = encodeURIComponent(deviceArr[1]);
	    	var data = ropParam + "&deviceName="+deviceName+"&deviceId="+deviceId;
	    	$.ajax({
            	url : url,
				data : data,
        		success : function(data) {
        			if(data.code == "1"){
        				app.toast.setText(data.message);
        				app.toast.show();
        				self.loadData(true);
        			}else if(data.code == "300002" || data.code == "300003" || data.code == "300004" || data.code == "300005" || data.code == "300006" || data.code == "300010"){
        				app.toast.setText(data.message);
        				app.toast.show();
        			}else{
        				app.toast.setText("数据处理失败");
        				app.toast.show();
        			}
        		},
				error : function(e){
					app.toast.setText(res.errMsg);
					app.toast.show();
				}
			});
        },
        _onClickBtnCheck:function(e){
            var hash="#"+app.pages.check;//审批页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickLi:function(e){
        	var meetingId = $(e.currentTarget).attr("data-meetingId");
        	if (meetingId==""){
        		return;
        	}
            var hash="#"+app.pages.meetDetail+"/"+meetingId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
    });

    return view;
});
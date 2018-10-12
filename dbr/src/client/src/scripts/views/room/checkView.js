define([
    "app",

    "text!../../templates/room/checkTemplate.html",
    "text!../../templates/room/checkListTemplate.html"
], function(app, roomTemplate, roomListTemplate){

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
            roomTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //header
            this.header=this.$el.find("header")[0];
            this.article=this.$el.find("article");
            //列表
            this.listContainer=this.$el.find("#ID-Check-List");

            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.article[0],
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
            console.log("审批checkView：刷新");
            this.loadData(false);
        },
        destroy:function(){
            console.log("审批checkView：移除");
        },
        loadData:function(flag){
        	if (!flag) {
        		app.loading.show();
        	}
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.deviceList+"&sessionId="+window.token;
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                    if(data.code!=1){//请求错误
                        return;
                    }
                    var data=data.deviceList;
                    //没有数据
                    if(data.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        self.drag.topNoData();
                        self.drag.bottomNoData();
                        return;
                    }
                    //编译渲染
                    var listHTML=_.template(roomListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.drag.topNoData();
                    if (flag) {
                    	self.prompt.show();
                    }
                    self.listContainer[0].innerHTML=listHTML;
                },
                error:function(msg){
                    //标识头部与尾部状态
                    self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
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
            'click #ID-Room-Back' : '_onClickBack',
            'click .SID-Check-Li' : '_onClickLi',
            'click #ID-Index-BtnSearch':'_onClickBtnSearch',
            'click #ID-AddDevice-Btn':'_onClickBtnAdd'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onClickBtnSearch:function(e){
            var hash="#"+app.pages.meetSearch;//会议搜索页面
            app.router.navigate(hash, { trigger : true, replace : false });
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
	    	var result = resultStr; // 扫码返回的结果
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
        				app.routerViews.indexView.loadData(true);
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
        _onClickBack:function(){
            this.destroy();
            history.go(-1);
        },
        _onClickLi:function(e){
//        	var reservedId = e.currentTarget.getAttribute("data-reservedId");
//        	for(var i=0,room;room=this.meetingList[i++];){
//				if (room.reservedId==reservedId)
//					app.approvalDetail=room;
//			}
//            var hash="#"+app.pages.checkDetail;//会议室预定详情页面
//            app.router.navigate(hash, { trigger : true, replace : false });
        }
    });

    return view;
});
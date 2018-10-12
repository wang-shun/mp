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
        	this.offset = 1;
        	this.limit = 10;
        	this.timestamp = 0;
        	this.endflag = 0;
        	this.initFlag = 0;
            this.dragFlag=0;
        },
        render:function(){
            var self = this;

            //渲染页面
            var html=_.template(
            indexTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            //日历
            this.divCalendar=this.$el.find("#ID-Index-Calendar");
            this.calendar=new Calendar(this.divCalendar[0],{
                viewType:"week",
                isShowDayNum:true,
                //disableBeforeDate:new Date(),
                activeDate:new Date(),
                prevHTML:'<i class="icon-arrowleft"></i>',
                nextHTML:'<i class="icon-arrowright"></i>',
                onChange:function(e){
                    self.dragFlag=0;
                	if (self.initFlag == 1) {
                		self.loadData(true);
                	}
                }
            });

            //内容区域
            this.article=this.$el.find("article");

            //列表
            this.listContainer=this.$el.find("#ID-Index-List");

            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.$el[0],
                css:{
                    top:"161px",
                    backgroundColor:"#20aeff"
                }
            });

            //下拉刷新
            this.drag=DfPull({
                overflowContainer:this.article[0],
                parent:this.article[0],
                onTopRefresh:function(e){
                    console.log("头部刷新");
                    self.dragFlag=1;
                    self.loadData();
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomRefresh:function(e){
                    console.log("底部刷新");
                    self.loadMoreData();
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
            this.initFlag = 1;
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
            this.loadPanel.find("header").remove();
            this.loadPanel.css({"top":"161px"});
            this.loadPanel.css({"bottom":"60px"});
            //加载数据
            this.loadData();
        },
        refresh:function(){
            this.dragFlag=0;
            console.log("首页：刷新");
            this.$el.find("article").scrollTop(0);
            this.loadData();
        },
        destroy:function(){
            console.log("首页：移除");
        },
        loadData:function(){
        	this.reloadPage(false);
        },
        loadMoreData:function(){
        	this.reloadPage(true);
        },
        reloadPage:function(nextPageFlag){
            var self = this;
            if(this.dragFlag==0)self.loadPanel.show();
            // 是否为加载下一页  true 是
			if (!nextPageFlag) {
				this.offset = 1;
				this.timestamp = 0;
            }
            var year = this.calendar.activeDate.year();
			var month = this.calendar.activeDate.month();
			var day = this.calendar.activeDate.date();
			var selectDate = year + "-" + month + "-" + day;
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.meetList;
            var pageParam = "&selectDate="+selectDate+"&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            if (app.initLoginUserFlag==0) {
            	pageParam += "&loginUserFlag=1";
            }
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                    if(data.code!=1){//请求错误
                        return;
                    }
                    self.errorPanel.removeClass("active");
                	console.log(data);
                    if (app.initLoginUserFlag==0) {
                    	app.loginUser.username = data.user.userName;
                    	app.loginUser.loginId = data.user.loginId;
                    	app.loginUser.deptId = data.user.deptUuid;
                    	// 管理员标识处理
//                    	app.loginUser.isAdmin = data.adminFlag;
//                    	if (app.loginUser.isAdmin == 1) {
//                    		self.$el.find("#ID-Index-BtnQrcode").show();
//                    	} else {
//                    		self.$el.find("#ID-Index-BtnQrcode").hide();
//                    	}
                    	app.initLoginUserFlag = 1;
                    }
                    self.offset = self.offset + 1;
					self.timestamp = data.timestamp;
					self.endflag = data.endflag;
                    //没有数据
                    if(data.meeting.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        //标识头部与尾部状态
                        self.drag.topNoData();
                        self.drag.bottomNoData();
                        return;
                    }
                    self.drag.bottomContainer.style.display="block";
                    //编译渲染
                    var listHTML=_.template(indexListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data.meeting
                    });
                    
                    if (!nextPageFlag) {//头部刷新
                        self.listContainer[0].innerHTML=listHTML;
                        setTimeout(function(){
                            if (self.endflag) {
                                self.drag.topNoData();
                                self.drag.bottomNoData();
                            }else{
                                self.drag.topComplete();
                            }
                            if(self.dragFlag){
                                self.prompt.show();
                            }
                            self.dragFlag=1;
                        }, 300);
                    }else{//底部刷新
                        self.listContainer[0].innerHTML+=listHTML;
                        if (self.endflag) {
                            self.drag.bottomNoData();
                        }else{
                            self.drag.bottomComplete();
                        }
                    }
                },
                error:function(msg){
                    //标识头部与尾部状态
                    self.drag.topNoData();
                    self.drag.bottomNoData();

                    //self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                        self.loadPanel.hide();
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
            'click #ID-Index-BtnSearch':'_onClickBtnSearch',
            'click #ID-Index-BtnAdd':'_onClickBtnAdd',
            'click #ID-Index-BtnQrcode':'_onClickBtnQrcode',
            'click .SID-Index-Li': '_onClickLi',
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
            var hash="#"+app.pages.meetAdd+"/0";//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnQrcode:function(e){
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.signin;
            mplus.scanQRCode({
                success: function (res) {
                    var result = res.resultStr; // 扫码返回的结果
                    var data = ropParam + "&participantId="+result;
                    $.ajax({
                        url : url,
                        data : data,
                        success : function(data) {
                            if(data.code == "1"){
                                app.toast.setText(data.personName+"第"+data.signNum+"次签到成功");
                                app.toast.show();
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
                                $(e.currentTarget).click();
                            },1000);
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
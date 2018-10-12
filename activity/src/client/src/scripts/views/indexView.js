define([
    "app",
    "text!../templates/index/indexTemplate.html",
    "text!../templates/index/indexTopFourTemplate.html",
    "text!../templates/index/indexDefaultTemplate.html"
], function(app, indexTemplate, indexTopFourTemplate, indexDefaultTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
            this.offset=1;//页数
            this.limit=5;//每页条数
            this.timestamp=0;//时间戳

            this.initFlag=0;
            this.ropMethod="";
            this.promptFlag = 0;
        },
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            indexTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            //header
            this.header=this.$el.find("header")[0];
            this.article=this.$el.find("article");

            //列表
            this.listContainer=this.$el.find("#ID-Index-List");

            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.$el[0],
                css:{
                    top:"88px",
                    backgroundColor:"#9783DB"
                }
            });

            //对话框
            this.confirm=new Alert("我是Confirm框",{
                onClickCancel:function(e){
                    e.hide();
                }
            });
            //下拉刷新
            this.drag=DfPull({
                overflowContainer:this.article[0],
                parent:this.article[0],
                onTopRefresh:function(e){
                    self.promptFlag = 1;
                    var status = self.$el.find(".SID-Index-State.active").attr("data-status");
                    if (status == "1") {//综合排序
                    	self.loadTopFour();
                    } else {
                    	self.reloadPage(false,0);
                    }
                    e.bottomContainer.style.display="none";
                },
                onTopComplete:function(e){
                },
                onBottomRefresh:function(e){
                    self.loadMoreData();
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });

            this.drag.bottomNoData();
            //筛选框
            this.dialog=new Dialog("#ID-Index-DialogFilter",
                {
                    overflowContainer:self.$el[0],
                    position:"top",
                    animation:"slideDown",
                    css:{width:"100%",paddingTop:"84px"},
                    onHid:function(e){
                        self.header.style.zIndex="2";
                        self.$el.find("#ID-Check-BtnFilter").removeClass("active");
                    },
                    onClickMask:function(e){
                        e.hide();
                        //self.$el.find(".SID-Index-State.active").click();
                    }
                }
            );
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
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
        },
        loadData:function(){
        	this.$el.find(".SID-Index-State.active").click();
        },
        loadMoreData:function(){
        	this.reloadPage(true,0);
        },
        loadTopFour:function(){
        	if (this.initFlag==0) {
        		app.loading.show();
        	}
        	var self = this;
        	var url = app.serviceUrl;
            var ropParam = app.ropMethod.indexTopFour+"&sessionId="+window.token;
            $.ajax({
            	url : url,
                data : ropParam,
                success:function(data){
                	if (data.code == "1") {
                		
                		var listHTML=_.template(indexTopFourTemplate,{
                            data:data.activity,
                            imgPath: app.constants.IMAGEPATH
                        });
                        self.listContainer[0].innerHTML=listHTML;
                        self.loadImg(data.activity,"top");
                        //初始化轮播图
                        if(self.$el.find("#ID-Index-Carousel")[0]){
                            var s1=new Slider("#ID-Index-Carousel",{
                                "pagination":".slider-pagination",
                                "loop":true
                            });
                        }
                        self.reloadPage(false,1);
                	}
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
        reloadPage:function(nextPageFlag,topFourFlag){
            var self = this;
            // 是否为加载下一页  true 是
			if (!nextPageFlag) {
				this.offset = 1;
				this.timestamp = 0;
            }

            //初始化请求参数
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.indexList+"&sessionId="+window.token;
            var order = self.$el.find(".SID-Index-State.active").attr("data-status");
            var pageParam = "&order="+order+"&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            $.ajax({
            	url : url,
                data : ropParam+pageParam,
                success:function(data){
                	console.log(data);
                    if(data.code==1){//请求错误
	                    //更新时间戳
	                    self.timestamp=data.timestamp;
	                    self.endflag = data.endflag;
	                    self.offset += 1;
	                    self.errorPanel.removeClass("active");
	                    //没有数据
	                    if(!data || data.activity.length == 0){
	                        self.listContainer[0].innerHTML=app.nodataHTML;
	                        //标识头部与尾部状态
	                        self.drag.topNoData();
	                        self.drag.bottomNoData();
	                        self.updateStyle();
	                        return;
	                    }
	                    self.drag.bottomContainer.style.display="block";
	                    //编译渲染
	                    var listHTML=_.template(indexDefaultTemplate,{
	                        data:data.activity,
	                        imgPath: app.constants.IMAGEPATH,
	                        nowTime:new Date().getTime()
	                    });
	                    self.loadImg(data.activity,"list");
	                    if (!nextPageFlag) {//头部刷新
	                    	if (topFourFlag == 1) {
	                    		self.$el.find("#ID-Index-List").append(listHTML);
	                        	//self.listContainer[0].innerHTML+=listHTML;
	                        } else {
	                        	self.$el.find("#ID-Index-List").html(listHTML);
	                        	//self.listContainer[0].innerHTML=listHTML;
	                        }
	                        setTimeout(function(){
	                            if (self.endflag) {
	                                self.drag.topNoData();
	                                self.drag.bottomNoData();
	                            }else{
	                                self.drag.topComplete();
	                            }
	                            if(self.initFlag==1 && self.promptFlag == 1){
	                                self.prompt.show();
	                                self.promptFlag = 0;
	                            }
	                            self.initFlag=1;
	                        }, 300);
	                    }else{//底部刷新
	                    	self.$el.find("#ID-Index-List").append(listHTML);
	                        //self.listContainer[0].innerHTML+=listHTML;
	                        if (self.endflag) {
	                            self.drag.bottomNoData();
	                        }else{
	                            self.drag.bottomComplete();
	                        }
	                    }
                    } else {
                    	self.drag.topNoData();
                        self.drag.bottomNoData();
                    	self.listContainer[0].innerHTML=app.nodataHTML;
                    }
                    self.updateStyle();
                },
                error:function(msg){
                    //标识头部与尾部状态
                    self.drag.topNoData();
                    self.drag.bottomNoData();
                    self.listContainer[0].innerHTML=app.nodataHTML;
                    //self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        loadImg:function(data,type){
        	var self = this;
        	for(var i=0,li; li=data[i++];) {
    			if (li.actPosterUrl) {
    				var actId = li.id;
    				var actPosterUrl = li.actPosterUrl;
        			var img = new Image();
        			img.src=actPosterUrl;
        		    img.onload=function(e){
        		    	var url = $(e.currentTarget).attr("src");
        		    	for(var i=0,li; li=data[i++];) {
        		    		if (li.actPosterUrl == url) {
        		    			if (type == "top") {
        		    				self.$el.find(".slider-slide[data-actId='"+li.id+"']").css("backgroundImage","url("+url+")");
        		    			} else if (type == "list") {
        		    				self.$el.find(".SID-actDiv>.card-photo[data-actId='"+li.id+"']").css("backgroundImage","url("+url+")");
        		    			}
        		    		}
        		    	}
        		    }; 
    			}
    		}
        },
        updateStyle:function(){
        	var self = this;
        	var status = self.$el.find(".SID-Index-State.active").attr("data-status");
        	self.$el.find(".SID-past-activity-btn").hide();
        	self.$el.find(".SID-actDiv>.SID-Index-Del").hide();
            if (status == "1") {//综合排序
            	self.$el.find(".SID-actDiv>.card-photo>.tagline").show();
            } else if (status == "2") {//即将开始
            	self.$el.find(".SID-actDiv>.card-photo>.tagline").show();
            } else if (status == "3") {//最新发布
            	self.$el.find(".SID-actDiv>.card-photo>.tagline").show();
            } else if (status == "4") {//人气最高
            	self.$el.find(".SID-actDiv>.card-photo>.tagline").show();
            } else if (status == "5") {//我参与的
            	self.$el.find(".SID-past-activity-btn").show();
            } else if (status == "6") {//我创建的
            	self.$el.find(".SID-past-activity-btn").show();
            	self.$el.find(".SID-actDiv>.SID-Index-Del").css("display","-webkit-box");
            }
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
            'click .SID-past-activity-btn':'_onClickBtnSearchHistory',
            'click #ID-Index-BtnAdd':'_onClickBtnAdd',
            'click .SID-Index-Li': '_onClickLi',
            'click #ID-Index-BtnFilter' : '_onClickBtnFilter',
            'click .SID-Index-State':'_onClickState',
            'click .SID-Index-Del' : '_onClickDel'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onClickBtnSearch:function(e){
        	if(!this.dialog.isHid){
            	this.$el.find("#ID-Index-BtnFilter").click();
            }
            var hash="#"+app.pages.search;//会议搜索页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnSearchHistory:function(e){
        	if(!this.dialog.isHid){
            	this.$el.find("#ID-Index-BtnFilter").click();
            }
            var hash="#"+app.pages.searchHistory;//会议搜索页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnAdd:function(e){
        	if(!this.dialog.isHid){
            	this.$el.find("#ID-Index-BtnFilter").click();
            }
            var hash="#"+app.pages.add;//会议添加页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickLi:function(e){
        	var meetingId = $(e.currentTarget).attr("data-actId");
        	if (meetingId==""){
        		return;
        	}
            var hash="#"+app.pages.detail+"/"+meetingId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickBtnFilter:function(e){
            this.header.style.zIndex="11";
            this.dialog.toggle();
            if(this.dialog.isHid){
                e.currentTarget.classList.remove("active");
            }else{
                e.currentTarget.classList.add("active");
            }
        },
        _onClickDel:function(e){
        	e.stopPropagation();
        	var actId = $(e.currentTarget).attr("data-actId");
        	var self = this;
            this.confirm.setText("您确定删除此活动吗？");
            this.confirm.setOnClickOk(function(e){
            	e.hide();
                var url = app.serviceUrl;
                var ropParam = app.ropMethod.deleteActivity+"&sessionId="+window.token;
                var pageParam = "&actId="+actId;
                app.loading.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
	        			app.loading.hide();
	        			if(data.code == "1"){
					    	app.toast.setText("删除成功");
					    	app.toast.show();
					    	self.loadData();
	        			}else{
					    	app.toast.setText("删除失败");
					    	app.toast.show();
	        			}
					},
					error : function(){
						app.loading.hide();
				    	app.toast.setText("删除失败");
				    	app.toast.show();
					},
	                complete:function(){
	                    setTimeout(function(){
	                        app.loading.hide();
	                    }, 300);
	                }
	        	});
            });
            this.confirm.show();
        },
        _onClickState:function(e){
        	app.loading.show();
        	var obj = $(e.currentTarget);
            var status = $(e.currentTarget).attr("data-status");
            this.$el.find(".SID-Index-State.active").removeClass("active");
            obj.addClass("active");
            this.$el.find("#ID-Index-BtnFilter>.app-stateFilterBar-font>span").text(obj.find("div").text());
            if(!this.dialog.isHid){
            	this.$el.find("#ID-Index-BtnFilter").click();
            }
            this.$el.find("article").scrollTop(0);
            if (status == "1") {//综合排序
            	this.loadTopFour();
            } else {
            	this.reloadPage(false,0);
            }
        }
    });

    return view;
});
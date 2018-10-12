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
        	this.initFlag = 0;
            this.dragFlag=0;
			this.pagination=[
				{
                    hasData:null,
                    offset:1,//当前页数
                    limit:10,//每页条数
                    timestamp:0,//时间戳
                    endflag:0,//结束标识
                    type:"&type=0"
	            },
	            {
	                hasData:null,
	                offset:1,
	                limit:10,
	                timestamp:0,
	                endflag:0,
                    type:"&type=1"
	            }
			];
			this.topPx=99;
			this.surveyDetail={};
			this.drag = [];
        },
        render:function(){
            var self = this;

            //渲染页面
            var html=_.template(
            indexTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //滑动页面
			this.sliderContainers=document.querySelectorAll("#ID-Pages > .slider-wrapper > .slider-slide");
            this.sliderContainer=document.getElementById("ID-Pages");
            this.sliderContainer.style.height=(window.innerHeight-this.topPx)+"px";

            this.tabbarContainer=document.getElementById("ID-Tabbar");
            this.tabs=this.tabbarContainer.querySelectorAll(".SID-Tabbar");

            this.activeIndex=0;
			this.slider=new Slider("#ID-Pages",{
                onSlideChangeEnd:function(e){
                	if (e.activeIndex==self.activeIndex){
                		return;
                	}
                    self._tabActive(e.activeIndex);
                    if(!self.pagination[e.activeIndex].hasData){
						self.loadData(e.activeIndex);
					}
                    self.activeIndex = e.activeIndex;
                }
            });

            //内容区域
            this.article=this.$el.find("article");

            //列表
            this.listContainer=this.$el.find(".SID-Index-List");

            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.$el[0],
                css:{
                    top:"44px",
                    backgroundColor:"#11cdde"
                }
            });

            //下拉刷新
            this.drag[0]=DfPull({
                overflowContainer:this.sliderContainers[0],
                parent:this.sliderContainers[0],
                onTopRefresh:function(e){
                    console.log("头部刷新");
                    self.dragFlag=1;
                    self.loadData($(".SID-Tabbar.active").attr("data-index"));
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomRefresh:function(e){
                    console.log("底部刷新");
                    self.loadMoreData($(".SID-Tabbar.active").attr("data-index"));
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
            this.drag[0].bottomNoData();
            this.drag[0].bottomContainer.style.display="block";
            this.drag[1]=DfPull({
                overflowContainer:this.sliderContainers[1],
                parent:this.sliderContainers[1],
                onTopRefresh:function(e){
                    console.log("头部刷新");
                    self.dragFlag=1;
                    self.loadData($(".SID-Tabbar.active").attr("data-index"));
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomRefresh:function(e){
                    console.log("底部刷新");
                    self.loadMoreData($(".SID-Tabbar.active").attr("data-index"));
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
            this.drag[1].bottomNoData();
            this.drag[1].bottomContainer.style.display="block";
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
            this.loadPanel.css({"top":"0px"});
            this.loadPanel.css({"bottom":"0px"});
            self.listContainer[0].innerHTML=app.nodataHTML;
            self.listContainer[1].innerHTML=app.nodataHTML;
            this.$el.find(".SID-NoData").css("height",(window.innerHeight-this.topPx-2)+"px");
            //加载数据
            this.loadData(0);
        },
        _tabActive:function(index){
            for(var i=0,t;t=this.tabs[i++];){
                t.classList.remove("active");
            }
            this.tabs[index].classList.add("active");
        },
        refresh:function(){
            this.dragFlag=0;
            console.log("首页：刷新");
            this.$el.find(".slider-slide").scrollTop(0);
            var index = this.$el.find(".SID-Tabbar[class$='active']").attr("data-index");
            this.loadData(index);
        },
        destroy:function(){
            console.log("首页：移除");
        },
        loadData:function(activeIndex){
        	this.reloadPage(false,activeIndex);
        },
        loadMoreData:function(activeIndex){
        	this.reloadPage(true,activeIndex);
        },
        reloadPage:function(nextPageFlag,activeIndex){
            var self = this;
            if(this.dragFlag==0)self.loadPanel.show();
            // 是否为加载下一页  true 是
			if (!nextPageFlag) {
				this.pagination[activeIndex].offset = 1;
				this.pagination[activeIndex].timestamp = 0;
            }
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.meetList;
            var pageParam = "&offset="+this.pagination[activeIndex].offset
            +"&limit="+this.pagination[activeIndex].limit+"&timestamp="+this.pagination[activeIndex].timestamp+this.pagination[activeIndex].type;
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                	console.log(data);
                    if(data.code!=1){//请求错误
                    	self.errorPanel.addClass("active");
                        return;
                    }
                    self.errorPanel.removeClass("active");

                    self.pagination[activeIndex].offset = self.pagination[activeIndex].offset + 1;
					self.pagination[activeIndex].timestamp = data.timestamp;
					self.pagination[activeIndex].endflag = data.endflag;
                    //没有数据
                    if(data.surveyList.length<=0){
                        self.listContainer[activeIndex].innerHTML=app.nodataHTML;
                        $(self.listContainer[activeIndex]).find(".SID-NoData").css("height",(window.innerHeight-self.topPx-2)+"px");
                        //标识头部与尾部状态
                        self.drag[activeIndex].topNoData();
                        self.drag[activeIndex].bottomNoData();
                        return;
                    }
                    self.drag[activeIndex].bottomContainer.style.display="block";
                    //编译渲染
                    var listHTML=_.template(indexListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data.surveyList
                    });
                    
                    if (!nextPageFlag) {//头部刷新
                        self.listContainer[activeIndex].innerHTML=listHTML;
                        setTimeout(function(){
                            if (self.pagination[activeIndex].endflag) {
                                self.drag[activeIndex].topNoData();
                                self.drag[activeIndex].bottomNoData();
                            }else{
                                self.drag[activeIndex].topComplete();
                            }
                            if(self.dragFlag){
                                self.prompt.show();
                            }
                            self.dragFlag=1;
                        }, 300);
                    }else{//底部刷新
                        self.listContainer[activeIndex].innerHTML+=listHTML;
                        if (self.pagination[activeIndex].endflag) {
                            self.drag[activeIndex].bottomNoData();
                        }else{
                            self.drag[activeIndex].bottomComplete();
                        }
                    }
                },
                error:function(msg){
                    //标识头部与尾部状态
                    self.drag[activeIndex].topNoData();
                    self.drag[activeIndex].bottomNoData();
                    self.errorPanel.addClass("active");
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
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Index-Li': '_onClickLi',
            'click #ID-Tabbar .tab' : '_onClickTab',//点击tab
            'click .SID-Back':'_onBack'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickTab:function(e){
            var index=0;
            this._eachBreak(this.tabs,function(n,i){
                if(n==e.target){
                    index=i;
                    return false;
                }
            });
            this._tabActive(index);
            this.slider.slideTo(index);
        },
		_eachBreak:function(arr,fn){
            for(var i=0;i<arr.length;i++){
                if(fn(arr[i],i)==false)break;
            }
        },
        _onBack:function(e){
            mplus.closeWindow();
        },
        _onClickPanelError:function(e){
        	this.loadPanel.show()
            this.refresh();
        },
        _onClickLi:function(e){
        	var liObj = $(e.currentTarget);
        	var meetingId = liObj.attr("data-meetingId");
        	if (meetingId==""){
        		return;
        	}
        	this.surveyDetail.id = meetingId;
        	this.surveyDetail.title = liObj.attr("data-title");
        	this.surveyDetail.status = liObj.attr("data-status");
        	this.surveyDetail.answered = liObj.attr("data-answered");
        	var hash="#"+app.pages.meetDetail+"/"+meetingId;//会议详情页面
        	app.router.navigate(hash, { trigger : true, replace : false });
        },
    });

    return view;
});
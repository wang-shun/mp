define([
    "app",

    "text!../templates/search/searchHistoryTemplate.html",
    "text!../templates/search/searchResultTemplate.html"
], function(app, searchTemplate, searchResultTemplate){

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
            searchTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            this.header=this.$el.find("header")[0];
            this.article=this.$el.find("article");
            this.txtSearch=document.getElementsByName("NID-SearchHistory-Search")[0];
            this.listContainer=this.$el.find("#ID-SearchHistory-List");
            this.listContainer[0].innerHTML=app.nodataHTML;
            app.formControls.update();
            //下拉刷新
            this.drag=DfPull({
                overflowContainer:this.article[0],
                parent:this.article[0],
                onTopRefresh:function(e){
                    self.promptFlag = 1;
                	self.reloadPage(false,0);
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
            this.dialog=new Dialog("#ID-SearchHistory-DialogFilter",
                {
                    overflowContainer:self.$el[0],
                    position:"top",
                    animation:"slideDown",
                    css:{width:"100%",paddingTop:"124px"},
                    onHid:function(e){
                        self.header.style.zIndex="4";
                        self.$el.find("#ID-Check-BtnFilter").removeClass("active");
                    },
                    onClickMask:function(e){
                        e.hide();
                        //self.$el.find(".SID-Search-State.active").click();
                    }
                }
            );
            //对话框
            this.confirm=new Alert("我是Confirm框",{
                onClickCancel:function(e){
                    e.hide();
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
            this.loadData();
        },
        loadData:function(){
        	var status = app.routerViews.indexView.$el.find(".SID-Index-State.active").attr("data-status");
        	console.log(status);
        	this.$el.find(".SID-SearchHistory-State[data-status='"+status+"']").click();
        	//this.$el.find(".SID-SearchHistory-State.active").click();
        },
        loadMoreData:function(){
        	this.reloadPage(true,0);
        },
        refresh:function(){
            this.dragFlag=0;
            console.log("搜索页searchHistoryView：刷新");
            this.$el.find("article").scrollTop(0);
            this.loadData();
        },
        destroy:function(){
            console.log("搜索页searchHistoryView：移除");
            this.txtSearch.value="";
            this.txtSearch.nextElementSibling.style.display="none";
            this.listContainer[0].innerHTML=app.nodataHTML;
            this.$el.find(".SID-SearchHistory-State")[0].click();
            this.drag.bottomNoData();
        },

        /*=========================
          Method
          ===========================*/
        reloadPage:function(nextPageFlag,topFourFlag){
            var self=this;
        	var keyword=this.txtSearch.value;
//            if(!keyword){
//            	 self.loadPanel.hide();
//               app.prompt.setText("请输入活动名称");
//               app.prompt.show();
//               return;
//            }
            var self = this;
            // 是否为加载下一页  true 是
			if (!nextPageFlag) {
				this.offset = 1;
				this.timestamp = 0;
            }

            //初始化请求参数
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.indexHistoryList+"&sessionId="+window.token;
            var order = self.$el.find(".SID-SearchHistory-State.active").attr("data-status");
            var pageParam = "&order="+order+"&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp+"&content="+keyword;
            $.ajax({
            	url : url,
                data : ropParam+pageParam,
                success:function(data){
                    if(data.code==1){//请求错误
	                    //更新时间戳
	                    self.timestamp=data.timestamp;
	                    self.endflag = data.endflag;
	                    self.offset += 1;
	                    self.errorPanel.removeClass("active");
	                    //没有数据
	                    if(!data || !data.activity || data.activity.length==0){
	                        self.listContainer[0].innerHTML=app.nodataHTML;
	                        //标识头部与尾部状态
	                        self.drag.topNoData();
	                        self.drag.bottomNoData();
	                        return;
	                    }
	                    self.drag.bottomContainer.style.display="block";
	                    //编译渲染
	                    var listHTML=_.template(searchResultTemplate,{
	                        data:data.activity,
	                        imgPath: app.constants.IMAGEPATH,
	                        nowTime:new Date().getTime()
	                    });
	                    self.loadImg(data.activity,"list");
	                    if (!nextPageFlag) {//头部刷新
	                    	if (topFourFlag == 1) {
	                    		self.$el.find("#ID-SearchHistory-List").append(listHTML);
	                        	//self.listContainer[0].innerHTML+=listHTML;
	                        } else {
	                        	self.$el.find("#ID-SearchHistory-List").html(listHTML);
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
	                    	self.$el.find("#ID-SearchHistory-List").append(listHTML);
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
                        self.loadPanel.hide();
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
        	var status = self.$el.find(".SID-SearchHistory-State.active").attr("data-status");
        	self.$el.find(".SID-past-activity-btn").hide();
        	self.$el.find(".SID-actDiv>.SID-Search-Del").hide();
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
            	self.$el.find(".SID-actDiv>.SID-Search-Del").css("display","-webkit-box");
            }
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back' : '_onBack',
            'submit #ID-SearchHistory-SearchForm' : '_onSubmit',
            'click .SID-Search-Li ' : '_onClickLi',
            'click #ID-SearchHistory-BtnFilter' : '_onClickBtnFilter',
            'click .SID-SearchHistory-State':'_onClickState',
            'click .SID-Search-Del' : '_onClickDel'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            $("#ID-SearchHistory-SearchForm").submit();
        },
        _onBack:function(){
            this.destroy();
            history.go(-1);
        },
        _onSubmit:function(e){
            e.preventDefault();
            var keyword=this.txtSearch.value;
            this.keyword = keyword;
//            if(!keyword){
//                app.prompt.setText("请输入活动名称");
//                app.prompt.show();
//                return;
//            }
            this.loadData();
            //this._searchByKeyWord("工作圈");
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
                self.loadPanel.show();
    			$.ajax({
    				type : "POST",
    				url : url,
    				data : ropParam + pageParam,
	        		success : function(data) {
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
				    	app.toast.setText("删除失败");
				    	app.toast.show();
					},
	                complete:function(){
	                    setTimeout(function(){
	                        self.loadPanel.hide();
	                    }, 300);
	                }
	        	});
            });
            this.confirm.show();
        },
        _onClickState:function(e){
            var self=this;
        	var obj = $(e.currentTarget);
            var status = $(e.currentTarget).attr("data-status");
            this.$el.find(".SID-SearchHistory-State.active").removeClass("active");
            obj.addClass("active");
            this.$el.find("#ID-SearchHistory-BtnFilter>.app-stateFilterBar-font>span").text(obj.find("div").text());
            if(!this.dialog.isHid){
            	this.$el.find("#ID-SearchHistory-BtnFilter").click();
            }
            
            var keyword=this.txtSearch.value;
            this.keyword = keyword;
//            if(!keyword){
//                return;
//            }
        	self.loadPanel.show();
            this.$el.find("article").scrollTop(0);
            this.reloadPage(false,0);
        }
    });

    return view;
});
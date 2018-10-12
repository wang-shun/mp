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
        	this.offset = 1;
        	this.limit = 10;
        	this.timestamp = 0;
        	this.endflag = 0;
        	this.meetingList=[];
            this.dragFlag=0;
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

            //筛选框
            this.dialog=new Dialog("#ID-Check-DialogFilter",
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
                    }
                }
            );

            //弹出框
            this.prompt=new Prompt("更新完成",{
                parent:this.article[0],
                css:{
                    top:"90px",
                    backgroundColor:"#20aeff"
                }
            });
            
            //下拉刷新
            this.drag=DfPull({
                overflowContainer:this.article[0],
                topParent:this.article[0],
                bottomParent:this.article[0],
                onTopRefresh:function(e){
                    console.log("头部刷新");
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
            console.log("审批checkView：刷新");
            this.loadData();
        },
        destroy:function(){
            console.log("审批checkView：移除");
            /*this.undelegateEvents();
            this.unbind();
            this.$el.empty();*/
            this.dialog.hide();
        },
        loadData:function(){
        	this.meetingList = [];
        	this.reloadPage(false);
        },
        loadMoreData:function(){
        	this.reloadPage(true);
        },
        reloadPage:function(nextPageFlag){
            app.loading.show();
            // 是否为加载下一页  true 是
			if (nextPageFlag) {
			} else {
				this.offset = 1;
				this.timestamp = 0;
			}
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomApprove+"&sessionId="+window.token;
            var pageParam = "&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            var param="";
            var deviceList = this.$el.find(".SID-Check-State.active");
            $.each(deviceList, function (i, n) {
            	if (i==0) {
            		param += "&status=" + $(n).attr("data-status");
            	} else {
            		param += "," + $(n).attr("data-status");
            	}
            });
            console.log(param);
            //status
            $.ajax({
            	url : url,
				data : ropParam+pageParam+param,
                success:function(data){
                	console.log(data);
                    if(data.code!=1){//请求错误
                        return;
                    }
                    self.errorPanel.removeClass("active");
                    self.offset = self.offset + 1;
					self.timestamp = data.timestamp;
					self.endflag = data.endflag;
                    var data=data.reserved;
                    self.meetingList = data;
                    //没有数据
                    if(data.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        //标识头部与尾部状态
                        self.drag.topNoData();
                        self.drag.bottomNoData();
                        return;
                    }
                    self.drag.bottomContainer.style.display="block";
                    //编译渲染
                    var listHTML=_.template(roomListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
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
        _loadStateData:function(state){
            app.loading.show();
            this.offset = 1;
			this.timestamp = 0;
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetRoomApprove+"&sessionId="+window.token;
            var pageParam = "&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            var param="";
            var deviceList = this.$el.find(".SID-Check-State.active");
            $.each(deviceList, function (i, n) {
            	if (i==0) {
            		param += "&status=" + $(n).attr("data-status");
            	} else {
            		param += "," + $(n).attr("data-status");
            	}
            });
            console.log(param);
            //status
            $.ajax({
            	url : url,
				data : ropParam+pageParam+param,
                success:function(data){
                    console.log(data);
                    //没有数据
                    if(data.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        return;
                    }
                    //编译渲染
                    var listHTML=_.template(roomListTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.listContainer[0].innerHTML=listHTML;
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
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back' : '_onBack',
            'click #ID-Check-BtnFilter' : '_onClickBtnFilter',
            'click .SID-Check-Li' : '_onClickLi',
            'click .SID-Check-State' : '_onClickState'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onBack:function(){
            if(!this.dialog.isHid){
                this.dialog.hide();
                return;
            }
            this.destroy();
            history.go(-1);
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
        _onClickLi:function(e){
        	var reservedId = e.currentTarget.getAttribute("data-reservedId");
        	for(var i=0,room;room=this.meetingList[i++];){
				if (room.reservedId==reservedId)
					app.approvalDetail=room;
			}
            var hash="#"+app.pages.checkDetail;//会议室预定详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickState:function(e){
            var target=e.currentTarget;
            var state=target.getAttribute("data-state");
            e.currentTarget.classList.toggle("active");
            this.loadData();
        }
    });

    return view;
});
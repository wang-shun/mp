define([
    "app",

    "text!../../templates/meet/meetSearchTemplate.html",
    //"text!../../templates/meet/meetSearchResultTemplate.html"
    "text!../../templates/index/indexListTemplate.html"
], function(app, meetSearchTemplate, meetSearchResultTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.offset = 1;
        	this.limit = 5;
        	this.timestamp = 0;
        	this.endflag = 0;
        	this.keyword = "";
        },
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            meetSearchTemplate,{
                initHTML:app.searchInitHTML,
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            this.txtSearch=document.getElementsByName("NID-MeetSearch-Search")[0];
            this.resultContainer=this.$el.find("#ID-MeetSearch-Result");
            app.formControls.update();
            //内容区域
            this.article=this.$el.find("article");
            //下拉刷新
            this.drag=DfPull({
                overflowContainer:this.article[0],
                topContainer:false,
                topParent:this.article[0],
                bottomParent:this.article[0],
                onTopRefresh:function(e){
                    console.log("头部刷新");
                    self._searchByKeyWord(self.keyword,false);
                },
                onTopComplete:function(e){
                    console.log("头部完成");
                },
                onBottomRefresh:function(e){
                    console.log("底部刷新");
                    self._searchByKeyWord(self.keyword,true);
                },
                onBottomNoData:function(e){
                    e.bottomContainer.style.display="none";
                }
            });
            this.drag.topNoData();
            this.drag.bottomNoData();
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
        },
        refresh:function(){
        	this.errorPanel.removeClass("active");
            console.log("搜索页meetSearchView：刷新");
        },
        destroy:function(){
            console.log("搜索页meetSearchView：移除");
            this.txtSearch.value="";
            this.txtSearch.nextElementSibling.style.display="none";
            this.resultContainer[0].innerHTML=app.searchInitHTML;
        },

        /*=========================
          Method
          ===========================*/
        _searchByKeyWord:function(keyWord,nextPageFlag){
            $("[name='NID-MeetSearch-Search']").blur();
            var self=this;
            console.log("您正在查询的关键字是："+keyWord);
            // 是否为加载下一页  true 是
			if (!nextPageFlag) {
				this.offset = 1;
				this.timestamp = 0;
            }
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.meetList+"&sessionId="+window.token;
            var pageParam = "&meetingName="+keyWord+"&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                	console.log(data);
                	if(data.code!="1"){//请求失败
                        this.error("请求失败");return;
                    }
                    self.errorPanel.removeClass("active");

                    self.offset = self.offset + 1;
					self.timestamp = data.timestamp;
					self.endflag = data.endflag;
					
                    var data=data.meeting;

                    
                    //编译渲染
                    var resultHTML=_.template(meetSearchResultTemplate,{
                        noDataHTML:app.searchNodataHTML,
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });

                    //没有数据
                    if(data.length<=0){
                        self.drag.topNoData();
                        self.drag.bottomNoData();
                        self.resultContainer[0].innerHTML=resultHTML;
                        return;
                    }
                    //高亮显示
//                    self.$el.find(".list-title").each(function(i,n){
//                        var html=$(n).html();
//                        var keyWordReg=new RegExp(keyWord,"g");
//                        $(n).html(html.replace(keyWordReg, "<span class=keyword>"+keyWord+"</span>"));
//                    });

                    if (!nextPageFlag) {//头部刷新
                    	self.resultContainer[0].innerHTML=resultHTML;
                        setTimeout(function(){
                            if (self.endflag) {
                                self.drag.topNoData();
                                self.drag.bottomNoData();
                            }else{
                                self.drag.topComplete();
                            }
                        }, 300);
                    }else{//底部刷新
                        self.resultContainer[0].innerHTML+=resultHTML;
                        if (self.endflag) {
                            self.drag.bottomNoData();
                        }else{
                            self.drag.bottomComplete();
                        }
                    }
                },
                error:function(msg){
                    self.drag.topNoData();
                    self.drag.bottomNoData();
                    self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    },500);
                }
            });
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back' : '_onBack',
            'submit #ID-MeetSearch-SearchForm' : '_onSubmit',
            'click .SID-Search-Li ' : '_onClickLi',
            'input [name="NID-MeetSearch-Search"]' : '_onChangeTxtSearch',
            'click [name="NID-MeetSearch-Search"]+.icon' : '_onClickClearSearch',
            'click .SID-Index-Li': '_onClickLi',
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            $("#ID-MeetSearch-SearchForm").submit();
        },
        _onBack:function(){
            this.destroy();
            history.go(-1);
            return false;
        },
        _onSubmit:function(e){
            e.preventDefault();
            var keyword=this.txtSearch.value;
            if(!keyword){
                app.prompt.setText("请输入会议名称");
                app.prompt.show();
                return;
            }
            this.keyword = keyword;
            this._searchByKeyWord(keyword,false);
            //this._searchByKeyWord("工作圈");
        },
        _onClickLi:function(e){
        	var meetingId = $(e.currentTarget).attr("data-meetingId");
        	if (meetingId==""){
        		return;
        	}
            var hash="#"+app.pages.meetDetail+"/"+meetingId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onChangeTxtSearch:function(e){
            if(e.currentTarget.value==""){
                //console.log("nodata");
            }
        },
        _onClickClearSearch:function(e){
            //console.log("clear");
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
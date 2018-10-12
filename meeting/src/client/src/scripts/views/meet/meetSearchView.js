define([
    "app",

    "text!../../templates/meet/meetSearchTemplate.html",
    "text!../../templates/index/indexListTemplate.html"
], function(app, meetSearchTemplate, meetSearchResultTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.offset = 1;
        	this.limit = 20;
        	this.timestamp = 0;
        	this.endflag = 0;
        },
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            meetSearchTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            this.txtSearch=document.getElementsByName("NID-MeetSearch-Search")[0];
            this.resultContainer=this.$el.find("#ID-MeetSearch-Result");
            this.resultContainer[0].innerHTML=app.nodataHTML;
            app.formControls.update();
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
        },
        refresh:function(){
            console.log("搜索页meetSearchView：刷新");
        },
        destroy:function(){
            console.log("搜索页meetSearchView：移除");
            this.txtSearch.value="";
            this.txtSearch.nextElementSibling.style.display="none";
            this.resultContainer[0].innerHTML=app.nodataHTML;
        },

        /*=========================
          Method
          ===========================*/
        _searchByKeyWord:function(keyWord){
            var self=this;
            console.log("您正在查询的关键字是："+keyWord);
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.meetList;
            var pageParam = "&meetingName="+keyWord+"&offset="+this.offset
            +"&limit="+this.limit+"&timestamp="+this.timestamp;
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                	if(data.code!="1"){//请求失败
                        this.error("请求失败");return;
                    }
                    self.errorPanel.removeClass("active");

                    var data=data.meeting;
                    //没有数据
                    if(data.length<=0){
                        self.resultContainer[0].innerHTML=app.nodataHTML;
                        app.prompt.setText("查询无数据");
                        app.prompt.show();
                        return;
                    }
                    //编译渲染
                    var resultHTML=_.template(meetSearchResultTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.resultContainer[0].innerHTML=resultHTML;
                    //高亮显示
                    self.$el.find(".list-title").each(function(i,n){
                        var html=$(n).html();
                        var keyWordReg=new RegExp(keyWord,"g");
                        $(n).html(html.replace(keyWordReg, "<span class=keyword>"+keyWord+"</span>"));
                    });
                },
                error:function(msg){
                    self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        self.loadPanel.hide();
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
            'click .SID-Index-Li' : '_onClickLi'
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
        },
        _onSubmit:function(e){
            e.preventDefault();
            var keyword=this.txtSearch.value;
            if(!keyword){
                app.prompt.setText("请输入会议名称");
                app.prompt.show();
                return;
            }
            this._searchByKeyWord(keyword);
            //this._searchByKeyWord("工作圈");
        },
        _onClickLi:function(e){
        	var meetingId = $(e.currentTarget).attr("data-meetingId");
        	if (meetingId==""){
        		return;
        	}
            var hash="#"+app.pages.meetDetail+"/"+meetingId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        }
    });

    return view;
});
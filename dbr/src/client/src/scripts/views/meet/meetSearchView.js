define([
    "app",

    "text!../../templates/meet/meetSearchTemplate.html",
    "text!../../templates/meet/meetSearchResultTemplate.html"
], function(app, meetSearchTemplate, meetSearchResultTemplate){

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
        _searchByKeyWord:function(keyWord1){
            var self=this;
            var keyWord = encodeURIComponent(keyWord1);
            console.log("您正在查询的关键字是："+keyWord);
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.deviceList+"&sessionId="+window.token;
            var pageParam = "&deviceName="+keyWord;
            $.ajax({
            	url : url,
				data : ropParam+pageParam,
                success:function(data){
                	if(data.code!="1"){//请求失败
                        this.error("请求失败");
                        return;
                    }
                    self.errorPanel.removeClass("active");

                    var data=data.deviceList;
                    //没有数据
                    if(data.length<=0){
                        self.resultContainer[0].innerHTML=app.nodataHTML;
                        return;
                    }
                    //编译渲染
                    var resultHTML=_.template(meetSearchResultTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data
                    });
                    self.resultContainer[0].innerHTML=resultHTML;
                },
                error:function(msg){
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
            'click #ID-MeetSearch-Back' : '_onClickBack',
            'submit #ID-MeetSearch-SearchForm' : '_onSubmit',
            'click .SID-Search-Li ' : '_onClickLi'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            $("#ID-MeetSearch-SearchForm").submit();
        },
        _onClickBack:function(){
            this.destroy();
            history.go(-1);
        },
        _onSubmit:function(e){
            e.preventDefault();
            var keyword=this.txtSearch.value;
            if(!keyword){
                app.prompt.setText("请输入设备型号");
                app.prompt.show();
                return;
            }
            this._searchByKeyWord(keyword);
            //this._searchByKeyWord("工作圈");
        },
        _onClickLi:function(e){
//        	var meetingId = $(e.currentTarget).attr("data-meetingId");
//        	if (meetingId==""){
//        		return;
//        	}
//            var hash="#"+app.pages.meetDetail+"/"+meetingId;//会议详情页面
//            app.router.navigate(hash, { trigger : true, replace : false });
        }
    });

    return view;
});
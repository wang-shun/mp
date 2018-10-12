define([
    "app",
    "text!../../templates/meet/meetSignTemplate.html",
    "text!../../templates/meet/meetSignListTemplate.html"
], function(app, meetSignTemplate, meetSignListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(){
            var self = this;
            //渲染页面
            var html=_.template(
            meetSignTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            this.listContainer=this.$el.find("#ID-MeetSignList-List");

            //加载数据
            this.loadData();
        },
        refresh:function(){
            console.log("签到列表页MeetSignListView：刷新");
            this.loadData();
        },
        destroy:function(){
            console.log("签到列表页MeetSignListView：移除");
        },
        loadData:function(){
            var self=this;
            
            var data=app.signDetail;
            //编译渲染
            var listHTML=_.template(meetSignListTemplate,{
                imgPath: app.constants.IMAGEPATH,
                nodataHTML:app.nodataHTML,
                data:data
            });
            self.listContainer[0].innerHTML=listHTML;
        },

        /*=========================
          Method
          ===========================*/
        
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-phone':'_onClickPhone',
            'click .SID-Back':'_onBack'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(e){
            history.go(-1);
        },
        _onClickPhone:function(e){
        	var phone = e.currentTarget.getAttribute("data-phone");
        	if (phone!="") {
        		mplus.callPhone({
        			phones: [phone], // 号码数组
        			success: function (res) {
    			    },
    			    fail: function (res) {
    				}
    			});
        	}
        }
    });

    return view;
});
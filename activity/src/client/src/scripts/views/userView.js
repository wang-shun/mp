define([
    "app",

    "text!../templates/detail/userTemplate.html"
], function(app, detailTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.actId = "";
        },
        render:function(id){
        	this.actId = id;
        	// 关于必填项控制的信息
        	var actData = app.routerViews.detailView.actData;
            console.log("详情EnterUser:根据id："+id);
            var self = this;

            //渲染页面
            var html=_.template(
            detailTemplate,{
                imgPath: app.constants.IMAGEPATH,
                actData : actData
            });
            this.$el.append(html);

            //header
            this.header=this.$el.find("header")[0];
            this.article=this.$el.find("article");

            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
        },
        refresh:function(id){
        	this.actId = id;
            console.log("详情User：刷新");
            this.$el.find("article").scrollTop(0);
        },
        destroy:function(){
        	this.$el.find("article").scrollTop(0);
            console.log("详情User：移除");
        },
        /*=========================
          Method
          ===========================*/
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Back' : '_onBack'
        },
        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(){
            history.go(-1)
        }
    });

    return view;
});
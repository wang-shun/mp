define([
    "app",

    "text!../templates/detail/detailTemplate.html",
    "text!../templates/detail/detailContentTemplate.html"
], function(app, detailTemplate,detailContentTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){
        	this.actId = "";
        	this.actData = {};
        	this.contentMaxLength=50;
        	this.homePage=false;
        },
        render:function(id){
        	this.actId = id;
            console.log("详情Detail:根据id："+id);
            var self = this;

            //渲染页面
            var html=_.template(
            detailTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            //header
            this.header=this.$el.find("header")[0];
            this.article=this.$el.find("article");

            //列表
            this.contentContainer=this.$el.find("#ID-Detail-Content");

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
            //右上角弹出框
            this.menupop=new Dialog("#ID-Popup-Popover",{
                position:"top-right",
                animation:"zoom",
                css:{overflow:"visible",top:"54px",right:"6px"}
            });
            if (app.workCircleFlag) {
            	this.$el.find("#ID-Popup-Menu").show();
            	this.$el.find("#ID-Share-Btn").show();
            }
            //加载数据
            this.loadData();
        },
        refresh:function(id){
        	this.menupop.hide();
        	this.actId = id;
            this.dragFlag=0;
            console.log("详情Detail：刷新");
            this.$el.find("article").scrollTop(0);
            this.loadData();
        },
        destroy:function(){
            console.log("详情Detail：移除");
        },
        loadData:function(){
        	var self=this;
        	self.$el.find("#ID-Export-Btn").hide();
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.detailActivity+"&sessionId="+window.token;
            var pageParam = "&actId="+self.actId;
            self.loadPanel.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	self.errorPanel.removeClass("active");
                	console.log(data);
                    if(data.code!=1){//请求错误
                    	if(app.isHomePage){
	                        app.toast.setText("链接失效");
	        				app.toast.show();
	                        setTimeout(function(){
	                        	mplus.closeWindow();
	                        }, 1000);
                    	} else if (data.code == 0) {
                    		app.toast.setText("数据请求失败");
	        				app.toast.show();
	                        setTimeout(function(){
	                        	history.go(-1);
	                        }, 1000);
                    	}
                        return;
                    }
                    self.actData = data;
                    //渲染页面
                    var html=_.template(
                    detailContentTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:data,
                        imFlag:app.imFlag,
                        contentMaxLength:self.contentMaxLength
                    });
                    self.contentContainer.html(html);
                    if (data.createFlag=="1"){
                    	self.$el.find("#ID-Popup-Menu").show();
                    	self.$el.find("#ID-Export-Btn").show();
                    } else {
                    	if (!app.workCircleFlag) {
                    		self.$el.find("#ID-Popup-Menu").hide();
                    	}
                    	self.$el.find("#ID-Export-Btn").hide();
                    }
                },
                error:function(msg){
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
        loadImg:function(data){
        	var self = this;
        	for(var i=0,li; li=data.photoList[i++];) {
    			if (li.phoneRoute) {
    				var phoneId = li.phoneId;
    				var phoneRoute = li.phoneRoute;
        			var img = new Image();
        			img.src=phoneRoute;
        		    img.onload=function(e){
        		    	var url = $(e.currentTarget).attr("src");
        		    	for(var i=0,li; li=data.photoList[i++];) {
        		    		if (li.phoneRoute == url) {
    		    				self.$el.find("#ID-DetailImageDiv>a[data-photoid='"+li.phoneId+"']").css("backgroundImage","url("+url+")");
        		    		}
        		    	}
        		    }; 
    			}
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
            'click .SID-Back' : '_onBack',
            'click #ID-Popup-Menu':'_onClickMenu',
            'click .SID-EnterBtn' : '_onClickEnterBtn',
            'click .SID-enterUser' : '_onClickEnterUserBtn',
            'click .SID-GroupBtn' : '_onClickBtnGroupChat',
            'click .SID-open-detail' :'_onClickOpenDetailBtn',
            'click .SID-close-detail' :'_onClickCloseDetailBtn',
            'click #ID-Export-Btn':'_onClickExportBtn',
            'click .SID-ChatMessage' :'_onClickChatMessageBtn',
            'click .SID-detailPhoto' : '_onClickShowPhoto',
            'click #ID-Share-Btn':'_onClickShareBtn'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.loadData();
        },
        _onBack:function(){
            if(app.isHomePage){
            	mplus.closeWindow();
                return;
            }
            history.go(-1)
        },
        _onClickShareBtn1:function(e){
        	this.menupop.hide();
        	mplus.shareWorkCircle({
        		comment: "comment",//用户评论
        		title: "title",//分享标题
        		desc: "desc",//分享描述
        		appname: "活动报名",//分享应用名
        		appid: "activity",//分享应用id
        		imgUrl: "app_local:4:activity:/images/1.jpg",//分享图标，见imgUrl链接格式定义
        		link: "app_scheme://4:activity/param/index.html#ID-PageDetail/50907fe8-c172-4652-b2af-84c3899b4a4a",
        		id: "250446ef-9e17-4fa3-bd5e-b80419d77183",//圈子id
    		    success: function (res) {
    		    	//alert(JSON.stringify(res));
    		    	app.toast.setText("分享成功");
    				app.toast.show();
    		    },
    			fail: function (res) {
    				app.toast.setText("分享失败:"+res.errMsg);
    				app.toast.show();
    			}
    		});
        },
        _onClickShareBtn:function(e){
        	this.menupop.hide();
        	var actId = this.actData.actId;
        	var url = app.serviceUrl;
            var ropParam = app.ropMethod.shareCircle+"&sessionId="+window.token;
            var pageParam = "&actId="+actId;
            app.loading.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	if (data.code == '100001'){
                		app.toast.setText("分享失败");
    			    	app.toast.show();
    			    	return;
                	} else if (data.code == '300011' || data.code == '300023') {
                		app.toast.setText(data.message);
    			    	app.toast.show();
    			    	return;
                	}
                	mplus.shareWorkCircle({
                		comment: data.comment,//用户评论
                		title: data.title,//分享标题
                		desc: data.desc,//分享描述
                		appname: data.appname,//分享应用名
                		appid: data.appid,//分享应用id
                		imgUrl: data.imgUrl,//分享图标，见imgUrl链接格式定义
                		link: data.link,//分享链接，见link链接格式
                		id: data.id,//圈子id
            		    success: function (res) {
            		    	//alert(JSON.stringify(res));
            		    	app.toast.setText("分享成功");
            				app.toast.show();
            		    },
            			fail: function (res) {
            				app.toast.setText("分享失败:"+res.errMsg);
            				app.toast.show();
            			}
            		});
                },
                error:function(msg){
			    	app.toast.setText("分享失败");
			    	app.toast.show();
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        _onClickShowPhoto:function(e){
        	var currentUrl = $(e.currentTarget).attr("data-url");
        	var urls=[];
            $('.SID-detailPhoto').each(function(i,n){
                var url=$(n).attr("data-url");
                urls.push(url);
            });
            mplus.previewImage({
                current: currentUrl, // 当前显示图片的http链接
                urls: urls // 需要预览的图片http链接列表
            });
        },
        _onClickOpenDetailBtn:function(){
        	this.$el.find("#ID-ContentDiv").text(this.actData.actContent);
        	this.$el.find(".SID-open-detail").hide();
        	this.$el.find(".SID-close-detail").show();
        	this.$el.find("#ID-DetailImageDiv").show();
        },
        _onClickCloseDetailBtn:function(){
        	var content = this.actData.actContent;
        	if (content.length > this.contentMaxLength) {
        		content = content.substring(0,this.contentMaxLength)+"...";
        	}
        	this.$el.find("#ID-ContentDiv").text(content);
        	this.$el.find(".SID-open-detail").show();
        	this.$el.find(".SID-close-detail").hide();
        	this.$el.find("#ID-DetailImageDiv").hide();
        },
        _onClickChatMessageBtn:function(e){
        	var account = this.actData.createImAccount;
        	var name = this.actData.createName;
        	mplus.showChatMessage({
        		imaccount: account, // im用户账号
        		name: name,//昵称
        		success: function (res) {
        		     // 发送成功回调
    		    },
    		    fail: function (res) {
    		        app.toast.setText("聊天窗口打开失败："+res.errMsg);
			    	app.toast.show();
    			}
    		});
        },
        _onClickBtnGroupChat:function(e){
        	var self = this;
        	var actId = this.actData.actId;
			var id = this.actData.disGroupId;
			var name = this.actData.actTitle;
			if (name.length > 10) {
				name = name.substr(0,10)+"...";
			}
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.joinGroup+"&sessionId="+window.token;
            var pageParam = "&groupId="+id+"&actId="+actId;
            app.loading.show();
			$.ajax({
				type : "POST",
				url : url,
				data : ropParam + pageParam,
                success:function(data){
                	if (data.code == '300018' || data.code == '300011' || data.code == '100001'){
                		app.toast.setText("讨论组打开失败");
    			    	app.toast.show();
    			    	return;
                	}
					mplus.showGroupMessage({
						imaccount: id, // im群组账号
						name: name,
					    fail: function (res) {
					    	app.toast.setText("讨论组窗口打开失败");
					    	app.toast.show();
						}
					});
                },
                error:function(msg){
			    	app.toast.setText("讨论组打开失败");
			    	app.toast.show();
                },
                complete:function(){
                    setTimeout(function(){
                        app.loading.hide();
                    }, 300);
                }
            });
        },
        _onClickMenu:function(e){
            this.menupop.show();
        },
        _onClickEnterBtn:function(e){
        	if (this.actId==""){
        		return;
        	}
        	if(app.isHomePage){
            	this.homePage = true;
            } else {
            	this.homePage = false;
            }
            var hash="#"+app.pages.enter+"/"+this.actId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickExportBtn:function(e){
        	this.menupop.hide();
        	if (this.actId==""){
        		return;
        	}
        	if(app.isHomePage){
            	this.homePage = true;
            } else {
            	this.homePage = false;
            }
            var hash="#"+app.pages.email+"/"+this.actId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onClickEnterUserBtn:function(e){
        	if (this.actId==""){
        		return;
        	}
        	if(app.isHomePage){
            	this.homePage = true;
            } else {
            	this.homePage = false;
            }
            var hash="#"+app.pages.user+"/"+this.actId;//会议详情页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        errorChangeBtn:function(){
        	this.$el.find(".SID-DisEnterBtn").show();
        	this.$el.find(".SID-EnterBtn").hide();
        }
    });

    return view;
});
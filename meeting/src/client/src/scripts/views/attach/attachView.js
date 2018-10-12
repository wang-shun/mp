define([
    "app",

    "text!../../templates/attach/attachTemplate.html",
    "text!../../templates/attach/attachCatalogueTemplate.html",
    "text!../../templates/attach/attachActiveListTemplate.html"
], function(app, attachTemplate, attachCatalogueTemplate, attachActiveListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(){
            var self = this;

            //渲染页面
            var html=_.template(
            attachTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);

            //内容区域
            this.article=this.$el.find("article");

            //列表
            this.listContainer=this.$el.find("#ID-Attach-List");

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

            //加载数据
            this.loadData();
        },
        refresh:function(){
            console.log("附件Attach：刷新");
            /*this.article.scrollTop=0;
            this.loadData("-1");*/
        },
        destroy:function(){
            console.log("附件Attach：移除");
        },
        loadData:function(folderId){
            var self=this;
            self.loadPanel.show();
            var folderId=folderId?folderId:"-1";
        	//var url = "http://192.168.100.102:38011/api";
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.attachList+"&folderId="+folderId;
            $.ajax({
                url : url,
                data : ropParam,
                success:function(data){
                    console.log(data);
                    var html=_.template(
                            attachCatalogueTemplate, {
                            imgPath:app.constants.IMAGEPATH,
                            attach:data.documentList,
                            folder:data.folderList
                        });
                    self.$el.find("#ID-Attach-List").html(html);
                    //设置选中
                    var cbos = document.querySelectorAll(".SID-AttachList-Cbo");
                    for(var i=0,cbo;cbo=cbos[i++];){
                        for(var j=0;j<app.activeAttachData.length;j++){
                            if(app.activeAttachData[j].filePath == cbo.getAttribute('data-path')) {
                                cbo.setAttribute("checked","checked");
                            }
                        }
                    }
                },
                error:function(msg){
                    app.toast.setText("获得附件列表失败，请重试");
                    app.toast.show();
                },
                complete:function(){
                    setTimeout(function(){
                        self.loadPanel.hide();
                    }, 300);
                }
            });
        },
        /*=========================
          Method
          ===========================*/
        //保存选中附件到app.activeAttachData
        _saveActiveAttach:function(){
            var self = this;
            var cbos = document.querySelectorAll(".SID-AttachList-Cbo");
            for(var i=0,cbo;cbo=cbos[i++];){
                if(cbo.checked){
                    var temp={};
                    temp.filePath=cbo.getAttribute('data-path');
                    temp.fileName=cbo.getAttribute('data-name');
                    temp.contentType=cbo.getAttribute('data-type');
                    temp.size=cbo.getAttribute('data-size');
                    temp.uploadTime = cbo.getAttribute('data-uploadTime');
                    //temp.privilege=$("input[name='"+temp.filePath+"']:checked").val();
                    var noexist=true;
                    for(var j=0;j<app.activeAttachData.length;j++){
                        if(app.activeAttachData[j].filePath == cbo.getAttribute('data-path')) {
                            noexist=false;
                        }
                    }
                    if(noexist){
                        app.activeAttachData.push(temp);
                    }
                }else{
                    for(var k=0;k<app.activeAttachData.length;k++){
                        if (app.activeAttachData[k].filePath == cbo.getAttribute('data-path')) {
                            app.activeAttachData.splice(k,1);
                            break;
                        }
                    }
                }
            }

        },
        //删除选中附件
        _removeActiveAttachById:function(id){
            var self = this;
            for(var i=0;i<app.activeAttachData.length;i++){
                if (app.activeAttachData[i].filePath == id) {
                    app.activeAttachData.splice(i,1);
                    document.querySelector(".SID-AttachList-Cbo[data-path='"+id+"']").removeAttribute("checked");
                    break;
                }
            }
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click #ID-AttachBack' : '_onSubmit',
            'click .SID-AttachList-Folder' : '_openFolder',
            'click .SID-Attch-BtnToFolder' : '_toFolder',
            'click #ID-Attch-BtnRootFolder' : '_toFolder',
        },

        /*=========================
          Event Handler
          ===========================*/
        _onBack:function(e){
            history.go(-1);
        },
        _onSubmit:function(e){
            this._saveActiveAttach();
            var self = this;
            //清空附件列表
            var html = _.template(
                attachActiveListTemplate, {
                'imgPath': app.constants.IMAGEPATH,
                'attach' : app.activeAttachData
            });
            $("#ID-MeetAdd-DivAttach").html(html);
            history.go(-1);
        },
        //打开文件夹
        _openFolder:function(e){
            var id = e.currentTarget.getAttribute("data-folderid");
            var name = e.currentTarget.getAttribute("data-foldername");
            $("#ID-Attach-Crumbs").append("<a class='SID-Attch-BtnToFolder' data-folderid='"+id+"'>&gt;&nbsp&nbsp"+name+"</a>");
            this._saveActiveAttach();
            this.loadData(id);
        },
        //跳转文件夹
        _toFolder:function(e){
            var target=e.currentTarget;
            var id = target.getAttribute("data-folderid");
            if(id=="-1"){
                $("#ID-Attach-Crumbs").html("");
            }else{
                index = $(target).index();
                $(".SID-Attch-BtnToFolder:gt("+index+")").remove();
            }
            this._saveActiveAttach();
            this.loadData(id);
        }
    });

    return view;
});
define([
    "app",

    "text!../../templates/tree/treeSearchTemplate.html",
    "text!../../templates/tree/treeSearchResultTemplate.html"
], function(app, treeSearchTemplate, treeSearchResultTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(){
            this.tree=app.router.views.treeView.tree;
            var self = this;
            //渲染页面
            var html=_.template(
            treeSearchTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //DOM
            this.txtSearch=document.getElementsByName("NID-TreeSearch-Search")[0];
            this.resultContainer=this.$el.find("#ID-TreeSearch-Result");
            this.resultContainer[0].innerHTML=app.nodataHTML;
            //数据请求错误
            this.errorPanel=self.$el.find(".SID-Error");
            if(!this.errorPanel[0]){
                self.$el.append(app.errorHTML);
                this.errorPanel=self.$el.find(".SID-Error");
            }
            app.formControls.update();
        },
        refresh:function(){
            if(self.errorPanel){
        	   self.errorPanel.removeClass("active");
            }
            this.tree=app.router.views.treeView.tree;
            console.log("搜索页treeSearchView：刷新");
        },
        destroy:function(){
            console.log("搜索页treeSearchView：移除");
            this.txtSearch.value="";
            this.txtSearch.nextElementSibling.style.display="none";
            this.resultContainer[0].innerHTML=app.nodataHTML;
        },

        /*=========================
          Method
          ===========================*/
        _searchByKeyWord:function(keyWord){
            $("[name='NID-TreeSearch-Search']").blur();
            app.loading.show();
            var self=this;
            console.log("您正在查询的关键字是："+keyWord);
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.getUsers+"&sessionId="+window.token+"&userName="+encodeURIComponent(keyWord);
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                	self.errorPanel.removeClass("active");
                    var data=JSON.stringify(data).replace(/userUuid/mgi,"id").replace(/deptUuid/mgi,"parentId").replace(/userName/mgi,"name");
                    var userData=JSON.parse(data).userList;
                    //没有数据
                    if(userData.length<=0){
                        self.resultContainer[0].innerHTML=app.nodataHTML;
                        return;
                    }
                    //编译渲染
                    var resultHTML=_.template(treeSearchResultTemplate,{
                        imgPath: app.constants.IMAGEPATH,
                        data:userData,
                        tree:self.tree
                    });
                    self.resultContainer[0].innerHTML=resultHTML;

                    //高亮显示
//                    self.$el.find(".tree-title").each(function(i,n){
//                        var html=$(n).html();
//                        var keyWordReg=new RegExp(keyWord,"g");
//                        $(n).html(html.replace(keyWordReg, "<span class=keyword>"+keyWord+"</span>"));
//                    });
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
            'click .SID-Back' : '_onBack',
            'submit #ID-TreeSearch-SearchForm' : '_onSubmit',
            'click .SID-TreeSearch-BtnAdd' : '_onClickBtnAdd'
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            $("#ID-TreeSearch-SearchForm").submit();
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
                app.prompt.setText("请输入姓名");
                app.prompt.show();
                return;
            }
            this._searchByKeyWord(keyword);
        },
        _onClickBtnAdd:function(e){
            console.log("添加");
            var line=e.target.parentNode;
            line.classList.add("active");
            var opts={};
            var type=0,deporder=0;
            if(line.getAttribute("data-loginid")){
                type="1";
            }
            if(line.getAttribute("data-deporder")){
                deporder=line.getAttribute("data-deporder");
            }
            opts.id=line.getAttribute("data-id");
            opts.type=type;
            opts.name=line.getAttribute("data-name");
            opts.order=deporder;
            opts.parentid=line.getAttribute("data-parentid");

            this.tree.addSelected(opts);
        }
    });

    return view;
});
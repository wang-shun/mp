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
            //loading
            this.loadPanel=self.$el.find(".SID-Load");
            if(!this.loadPanel[0]){
                self.$el.append(app.loadHTML);
                this.loadPanel=self.$el.find(".SID-Load");
            }
            app.formControls.update();
        },
        refresh:function(){
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
            var self=this;
            self.loadPanel.show();
            console.log("您正在查询的关键字是："+keyWord);
            var self=this;
            var url = window.serviceUrl;
            var ropParam = app.ropMethod.getUsers+"&userName="+encodeURIComponent(keyWord);
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                	console.log(data);
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
                    self.$el.find(".tree-title").each(function(i,n){
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
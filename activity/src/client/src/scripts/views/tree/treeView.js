define([
    "app",

    "text!../../templates/tree/treeTemplate.html"
], function(app, treeTemplate, treeListTemplate){

    var view = Backbone.View.extend({
        /*=========================
          Model
          ===========================*/
        initialize:function(){},
        render:function(selected){
            var self = this;
            //渲染页面
            var html=_.template(
            treeTemplate,{
                imgPath: app.constants.IMAGEPATH
            });
            this.$el.append(html);
            //列表
            this.listContainer=this.$el.find("#ID-Tree-Box");
            //初始化树结构
            this._initTree();
            //添加树结构选中项
            this._selectedTree(selected);
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
            //加载群组
            this.loadGroup();
        },
        refresh:function(selected){
            console.log("树搜索页：刷新");
            //添加树结构选中项
            this._selectedTree(selected);
            if(!this.groupData){
                //加载群组
                this.loadGroup();
            }
        },
        destroy:function(){
            console.log("树搜索页：移除");
        },
        _drawOpacityLoad:function(){
            this.loadPanel.find("header").css({"display":"none"});
            this.loadPanel.css({"backgroundColor":"rgba(0, 0, 0, 0.4)"});
        },
        _drawWhiteLoad:function(){
            this.loadPanel.find("header").css({"display":"block"});
            this.loadPanel.css({"backgroundColor":"white"});
        },
        loadGroup:function(){
            var self=this;
            self._drawWhiteLoad();
            self.loadPanel.show();
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.getDepartments+"&sessionId="+window.token;
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                	self.errorPanel.removeClass("active");
                    //格式化数据字段为:id、name、parentid
                    var data=JSON.stringify(data).replace(/depUuid/mgi,"id").replace(/depName/mgi,"name").replace(/parentId/mgi,"parentid");
                    self.groupData=JSON.parse(data).depList;
                    //没有数据
                    if(self.groupData.length<=0){
                        self.listContainer[0].innerHTML=app.nodataHTML;
                        return;
                    }
                    //渲染
                    self.tree.setData(self.groupData);
                    self.tree.update();
                },
                error:function(msg){
                    self.errorPanel.addClass("active");
                },
                complete:function(){
                    setTimeout(function(){
                        self.loadPanel.hide();
                        self._drawOpacityLoad();
                    }, 300);
                }
            });
        },
        loadPeople:function(e){
            var self=this;
            var id=e.targetLine.getAttribute("data-id");
            var ul=e.targetLine.nextElementSibling;
            self.loadPanel.show();
            var self=this;
            var url = app.serviceUrl;
            var ropParam = app.ropMethod.getUsers+"&sessionId="+window.token+"&depUuid="+id+"&depScope=0";
            $.ajax({
            	url : url,
				data : ropParam,
                success:function(data){
                	e.targetLine.hasData=true;
                    var data=JSON.stringify(data).replace(/userUuid/mgi,"id").replace(/deptUuid/mgi,"parentid").replace(/userName/mgi,"name");
                    var userData=JSON.parse(data).userList;
                    //没有数据
                    if(!userData || userData.length<=0){
                        return;
                    }
                    //渲染
                    self.tree.addData(userData,id,ul);
                },
                error:function(msg){
                    app.toast.setText("加载数据失败");
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
        _initTree:function(){
            var self=this;
            this.tree=new Tree("#ID-Tree-List",{
                bar:"#ID-Tree-Bar",
                btnDelClass:"icon-clear-fill",
                onData:function(option){
                    if(option.loginId){
                        var photo="";
//                        if(option.avatarUrl){
//                            photo='<span class="tree-photo" style="background-image:url('+option.avatarUrl+')"></span>';
//                        }else{
                            photo='<span class="tree-photo" style="background-color:'+option.name.substr(0,1).toPinyin().substr(0,1).toColor()+'">'+option.name.substr(option.name.length-2,2)+'</span>';
//                        }
                        option.html='<div class="tree-icon">'+photo+'</div>'+
                        '<div class="tree-title">'+option.name+'</div>';
                    }
                },
                onClick:function(e){
                    /*console.log("点击行："+e.targetLine);
                    console.log("点击元素："+e.target);
                    console.log("点击的li："+e.targetLi);*/
                    if(e.target.classList.contains("tree-btnadd"))return;
                    if(e.targetLine.hasData || e.targetLine.getAttribute("data-loginid")){
                        return;
                    }
                    var id=e.targetLine.getAttribute("data-id");
                    self.loadPeople(e);
                },
                onClickLastChild:function(e){//没有子节点
                    console.log("没有子节点");
                }
            });
        },
        _selectedTree:function(selected){
            if(!selected)return;
            var selArr=selected.split(",");
            for(var i=0,opt;opt=selArr[i++];){
                var optArr=opt.split(":");
                var opts={};
                opts.id=optArr[0];
                opts.type=optArr[1];
                opts.name=optArr[2];
                opts.order=optArr[3];
                opts.parentid=optArr[4];
                this.tree.addSelected(opts);
            }
        },
        _reset:function(){
            this.tree.reset();
        },
        /*=========================
          Events
          ===========================*/
        events: {
            'click .SID-Error' : '_onClickPanelError',
            'click .SID-Back' : '_onBack',
            'click #ID-Tree-BtnSearch' : '_onClickSearch',
            'click #ID-Tree-BtnSubmit' : '_onSubmit',
        },

        /*=========================
          Event Handler
          ===========================*/
        _onClickPanelError:function(e){
            this.refresh();
        },
        _onBack:function(e){
            this._reset();
            history.go(-1);
        },
        _onClickSearch:function(e){
            var hash="#"+app.pages.treeSearch;//树搜索页面
            app.router.navigate(hash, { trigger : true, replace : false });
        },
        _onSubmit:function(e){
            //获得值
            var selected=this.tree.selected,name="",opts="";
            for(var n in selected){
                var type=0,deporder=0;
                if(selected[n].loginid || selected[n].type){
                    type="1";
                }
                if(selected[n].deporder){
                    deporder=selected[n].deporder;
                }
                if(selected[n].order){
                    deporder=selected[n].order;
                }
                name+=selected[n].name+",";
                opts+=selected[n].id+":"+type+":"+selected[n].name+":"+deporder+":"+selected[n].parentid+",";
            }
            name=name.substr(0,name.length-1);
            opts=opts.substr(0,opts.length-1);
            //赋值
            $("#ID-MeetAdd-People").val(name);
            $("[name='NID-MeetAdd-People']").val(opts);
            //返回上一页
            this._reset();
            history.go(-1);
            
        }
    });

    return view;
});
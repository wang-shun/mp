define(
    [ 'jquery', 'views/communication-base-view',
      'text!../../templates/monitor/valuedefineTemplate.html',
      'text!../../templates/monitor/valuedefineListTemplate.html',
      'views/monitor/add-fieldvalue-snippet-view','editableselect'],
    function($, CommunicationBaseView,Template,ListTemplate,FieldValueView) {
        var InstanceSnippetView = CommunicationBaseView
            .extend({
                events : {
                	'click .SID-fieldvalue-delete':'_onDeleteValueField',
                	'click .SID-fieldvalue-save':'_onSaveFieldValue',
                },
                initialize : function() {
                	this.views = {};
                    this.childView = {};
                    this.parentView = {};
                    this.data={};
                    this.dialog;
                },
                render : function(parentView,data) {
                    this.parentView = parentView;
                    this.data = data;
                    this._setContentHTML();
                    this.holdList();
                    return this;
                },
                destroy : function() {
                    this._destroyBusinessViews();
                    this.undelegateEvents();
                    this.unbind();
                    this.$el.empty();
                    this.remove();
                },
                _destroyBusinessViews : function() {
                    $.each(this.childView, function(index, view) {
                        view.destroy();
                    });
                },
                refresh : function() {
                },
                _setContentHTML : function (){
                    var self = this;
                    var html = _.template(Template, {});
                    this.$el.append(html);
                    var commonDialog;
                    commonDialog =fh.servicemanagerOpenDialog('valuedefineDialog', this.data.title, 800, 500, this.el);
                    commonDialog.addBtn("ok","关闭",function(){
						commonDialog.cancel();
					});
                    commonDialog.addBtn("add","新增",function(){
                    	self.views.FieldValueView = new FieldValueView();
                    	var data = {};
						data.title = "新增值域定义";
						data.id = self.data.id;
						data.unitList = self.data.unitList;
						self.views.FieldValueView.render(self,data);
					});
                    this.dialog = commonDialog;
                },
                holdList : function(){
                	var self = this;
                	var appContext = self.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					$.ajax({
						type:"POST",
						url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.field.list&id="+self.data.id,
						success:function(ajax){
							self.data.list = ajax.list;
							$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.unit.get",
								success:function(ajax){
									self.data.unitList = ajax.unitList;
									var ListHtml = _.template(ListTemplate, {
										'list': self.data.list,
										'unitlist': self.data.unitList,
									});
				            		self.$el.find(".SID-instanceBox").html(ListHtml);
				            		
				            		var unitselectList = self.$el.find(".unitselect");
									for(var i=0;i<unitselectList.length;i++){
										$(unitselectList[i]).editableSelect({ effects: 'fade' });
									}
								}
							});
						}
					});
                },
                _onDeleteValueField : function(e){
                	var self = this;
					var fieldid = $(e.target).attr("data-id");
					fh.confirm('删除此值域定义？',function(){
						var appContext = self.getAppContext();
						var servicePath = appContext.cashUtil.getData('servicePath');
						var ropParam = appContext.cashUtil.getData('ropParam');
						$.ajax({
							type:"POST",
							url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.field.delete&id="+fieldid,
							success:function(ajax){
								if(ajax.code=="1"){
									self.holdList();
                            	}else{
					    			fh.alert(ajax.message);
					    		}
							}
						});
					},true,"",self.dialog,function(){},null);
                },
                _onSaveFieldValue:function(e){
					var self = this;
					var id = $(e.target).attr("data-id");
					var field = $(e.target).parent().parent().find(".field").html().trim();
					var name = escape($(e.target).parent().parent().find(".name").val());
					var nameTarget = $(e.target).parent().parent().find(".name");
					if(name == ""){
						self._exalert("名称字段为必填项",true,function(){
							$(nameTarget[0]).focus();
						});
						return;
					}
					var unit = escape($(e.target).parent().parent().find(".unitselect").val());
					var unitorgin = $(e.target).parent().parent().find(".unitselect").val();
					var isunitexit = 0;
					var unitList = self.data.unitList;
					for(var k=0;k<unitList.length;k++){
						if(unitList[k].unit == unitorgin){
							isunitexit = 1;
						}
					}
					if(isunitexit == 0 && unitorgin != ""){
						self._exalert("单位:\""+unitorgin+"\"不存在<br/>请选择下拉菜单中的单位");
						return;
					}
					var appContext = self.getAppContext();
					var servicePath = appContext.cashUtil.getData('servicePath');
					var ropParam = appContext.cashUtil.getData('ropParam');
					
                    //this.showLoading();
                    $.ajax({
                        type : "POST",
                        url:encodeURI(servicePath+"?"+ropParam+ "&method=mapps.servicemanager.field.edit&field="+field+"&name="+name+"&unit="+unit+"&id="+id),
                        success : function(response) {
                            //_self.hideLoading();
                            if(response.code == "1"){
                            	self.holdList();
                            }else{
                            	self._exalert(response.message);
                            }
                        },
                        error : function(){
                            //_self.hideLoading();
                        	self._exalert("数据处理失败");
                        }
                    });
				},
				_exalert:function(info,ischild,handler){
                	var _self = this;
					fh.alert(info,true,handler,_self.dialog,null);
                },
            });
        return InstanceSnippetView;
    });

define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/editSelfDefineTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination','cursorPosition'],
		function($, CommunicationBaseView,Template,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-insertword' : '_onClickInsertWord',
							'click .SID-insertsymbol' : '_onClickInsertSymbol',
							'mouseup .SID-editarea' : '_onEditConfig',
							'keyup .SID-editarea' : '_onEditConfig',
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.pos = {text: "", start: 0, end: 0};
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
                            this.passedTest=0;
							this._setContentHTML();
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
							var fieldList = self.data.fieldList;
							var fieldStr = '';
							for(var i=0;i<fieldList.length;i++){
								fieldStr += '<option value="'+fieldList[i].field+'">'+fieldList[i].field+'</option>';
							}
							self.$el.find(".SID-field").append(fieldStr);
							self.$el.find(".SID-editarea").val(self.data.filledvalue);
							var commonDialog;
							commonDialog =fh.servicemanagerOpenDialog('dbAddDialog', this.data.title, 780, 500, this.el,null,null,null,self.parentView.dialog);
							commonDialog.addBtn("cannel","取消",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("save","确定",function(){
								self._clickSave();
							});
							self.dialog = commonDialog;
						},
						_onEditConfig:function(e){
							var self = this;
							var tx = self.$el.find(".SID-editarea")[0];
							self.pos = cursorPosition.get(tx);
						},
						_onClickInsertWord:function(e){
							var self = this;
							var tx = self.$el.find(".SID-editarea")[0];
							var insertwordBox = $(e.currentTarget).parent();
							var func = insertwordBox.find(".SID-func").val();
							var insertwordStr = func+"("+insertwordBox.find(".SID-field").val()+")";
							if(func == ""){
								insertwordStr = insertwordBox.find(".SID-field").val();
							}
							cursorPosition.add(tx, self.pos, insertwordStr);
						},
						_onClickInsertSymbol:function(e){
							var self = this;
							var tx = self.$el.find(".SID-editarea")[0];
							var insertsymbol=$(e.currentTarget).val();
							cursorPosition.add(tx, self.pos, insertsymbol);
						},
						_clickSave:function(){
							var self = this;
							if(self.$el.find(".SID-editarea").val().trim() == ""){
								self.parentView._fillfieldTextarea("");
								self.dialog.cancel();
								self.destroy();
								return;
							}
							//sql字符串校验
							var selfdefinevalue = self.$el.find(".SID-editarea").val();
							var regexStr = "((mean|min|max|count|sum|stddev)\\((";
							var fieldList = self.data.fieldList;
							var fieldStr = "";
							for(var i=0;i<fieldList.length;i++){
								if(i != 0){
									regexStr += "|";
									fieldStr += "|";
								}
								regexStr += fieldList[i].field;
								fieldStr += "("+fieldList[i].field+")";
							}
							regexStr += ")\\))|"+fieldStr;
							selfdefinevalue = selfdefinevalue.replaceAll(regexStr,"").replaceAll("\\(","").replaceAll("\\)","")
							.replaceAll("\\+","").replaceAll("-","").replaceAll("\\*","").replaceAll("/","").replaceAll(" ","");
							if(selfdefinevalue != ""){
								self._exalert("表达式含有无关字段");
								return;
							}
							
							//sql逻辑校验
							var sql = "select "+self.$el.find(".SID-editarea").val()+" from "+self.data.measurement+" limit 10";
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
		                	$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.influx.validatesql&sql="+sql.replace(/\+/g, '%2B'),
								success:function(response){
									if(response.code == "1"){
										self.parentView._fillfieldTextarea(self.$el.find(".SID-editarea").val());
										self.dialog.cancel();
										self.destroy();
                                    }else{
                                    	self._exalert(response.message);
                                    }
								}
							});
						},
                        _exalert:function(info,ischild,handler){
                        	var _self = this;
							fh.alert(info,true,handler,_self.dialog,null);
                        },
					});
			return AddSnippetView;
		});
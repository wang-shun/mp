define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/viewJSONTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination','jsonviewer'],
		function($, CommunicationBaseView,Template
				,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.dialog;
						},
						render : function(parentView,data) {
							this.parentView = parentView;
							this.data = data;
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
							self.$el.append(html);
							var commonDialog;
							commonDialog =fh.servicemanagerOpenDialog('checkAlertlogDialog', this.data.title, 800, 600, this.el,null,null,null,self.parentView.dialog);
							commonDialog.addBtn("cannel","关闭",function(){
								self.destroy();
								commonDialog.cancel();
							});
							commonDialog.addBtn("collapse","全部折叠",function(){
								self.$el.find('.SID-jsonData-Box').JSONView('collapse');
							});
							commonDialog.addBtn("expand","全部展开",function(){
								self.$el.find('.SID-jsonData-Box').JSONView('expand');
							});
							self.dialog = commonDialog;
							self._fillData();
						},
		                _fillData : function(){
		                	var self = this;
		                	var jsonData = self.data.jsonData;
		                	//填充并格式化json数据
		                	self.$el.find('.SID-jsonData-Box').JSONView(jsonData, {collapsed: true});
		                	self._emphasize();
		                },
		                _emphasize:function(){
		                	var self = this;
		                	var aList = self.$el.find('.SID-jsonData-Box').find(".prop");
		                	var emphasizeList = self.data.emphasizeList;
		                	if(emphasizeList != undefined){
		                		for(var i=0;i<aList.length;i++){
		                			for(var j=0;j<emphasizeList.length;j++)
		                			if($(aList[i]).html().indexOf(emphasizeList[j]) > 0){
		                				$(aList[i]).css("color","#1788fb");
		                			}
		                		}
		                	}
		                }
					});
			return AddSnippetView;
		});
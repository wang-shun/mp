define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/monitor/addRuleTemplate.html'
		  ,'text!../../templates/monitor/addConditionTemplate.html'
		  ,'text!../../templates/monitor/kapacitorFilterTemplate.html'
		  ,'util/datatableUtil','datatable_lnpagination','cursorPosition','editableselect'],
		function($, CommunicationBaseView,Template,ConditionTemplate,kapacitorFilterTemplate,datatableUtil,datatableLnpagination) {
			var treeObj;
			var datatable;
			var AddSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-smalltab' : '_selectTab',
							'click .SID-addcondition-btn' : '_onClickAddCondition',
							'click .SID-insertword' : '_onClickInsertWord',
							'mouseup .SID-message' : '_onEditConfig',
							'keyup .SID-message' : '_onEditConfig',
							'click .SID-rulesave-btn' : '_addForm',
							'keyup .checkInputMark' : '_extraValidate',
							'change .checkBoxMark' : '_extraValidate',
							'click .SID-kapacitorFilter' : '_showFilter',
							'click .SID-closeFilter' : '_closeFilter',
							'click .SID-closeCondition' : '_closeCondition',
							'change .SID-filter-tagvalue' : '_clickFilter',
							'change .SID-filter-groupby' : '_clickFilter',
							'click .SID-toggleFilter' : '_toggleFilter',
							'change .SID-func' : '_clickFunc',
							'change .SID-valueField' : '_clickFunc',
						},
						initialize : function() {
							this.childView = {};
							this.parentView = {};
							this.data={};
							this.dialog;
							this.conditionNum = {"1":0,"2":0,"3":0};
							this.conditionShow = "1";
							this.pos = {text: "", start: 0, end: 0};
							this.measurementId = "";
							this.finalwhere = "";
							this.finalgroupby = "";
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
							this.$el.append(html);
							if(self.data.retentionList[0].series != undefined){
								var retentionList = self.data.retentionList[0].series[0].values;
								var retentionStr = '<option value="" selected></option>';
								for(var i=0;i<retentionList.length;i++){
									var selected = "";
									retentionStr += '<option value="'+retentionList[i][0]+'">'+retentionList[i][0]+'</option>';
								}
								self.$el.find(".SID-rp").append(retentionStr);
							}
							var measurementStr = '<option value="" selected></option>';
							var measurementList = self.data.measurementList;
							for(var i=0;i<measurementList.length;i++){
								var selected = "";
//								if(measurementList[i].isDefault == "1" && self.data.measurementDetail == null){
//									selected = "selected";
//								}
								measurementStr += '<option value="'+measurementList[i].id+'" '+selected+'>'+measurementList[i].measurement+'</option>';
							}
							self.$el.find(".SID-measurement").append(measurementStr).editableSelect({ 
								effects: 'fade' ,
								onSelect:function (li) {
									self.measurementId = li.value;
									self._onChangeMeasurement();
								},
							});
							self._clickFunc();
							self._clickFilter();
							if(self.data.ruleDetail != null){
								self._fillData(this.data);
							}
							
							//过滤模块大小缩放
							$(function() {
								$(document).mousemove(function(e) {
									if (!!this.move) {
										var posix = !document.move_target ? {'x': 0, 'y': 0} : document.move_target.posix,
											callback = document.call_down || function() {
												$(this.move_target).css({
													'top': e.pageY - posix.y,
													'left': e.pageX - posix.x
												});
											};

										callback.call(this, e, posix);
									}
								}).mouseup(function(e) {
									if (!!this.move) {
										var callback = document.call_up || function(){};
										callback.call(this, e);
										$.extend(this, {
											'move': false,
											'move_target': null,
											'call_down': false,
											'call_up': false
										});
									}
								});

								var $box = $('#box').mousemove(function(e) {
								    var offset = $(this).offset();
								    if(($box.height() - (e.pageY - offset.top)) < 10){
								    	$box.css({
								            'cursor': 's-resize',
								        });
								    }else{
								    	$box.css({
								            'cursor': 'auto',
								        });
								    }
								}).mousedown(function(e) {
								    var offset = $(this).offset();
								    if(($box.height() - (e.pageY - offset.top)) < 10){
								    	var posix = {
									            'w': $box.width(), 
									            'h': $box.height(), 
									            'x': e.pageX, 
									            'y': e.pageY
									        };
									    
									    $.extend(document, {'move': true, 'call_down': function(e) {
									        $box.css({
									            //'width': Math.max(30, e.pageX - posix.x + posix.w),
									            'height': Math.max(30, e.pageY - posix.y + posix.h)
									        });
									    }});
									    return false;
								    }
								});
							});
							//全部加载完毕之后展现
							self.$el.find(".main-cons").show();
						},
		                _fillData : function(data){
		                	var self = this;
		                	//填充基本信息
		                	self.$el.find(".SID-id").val(data.ruleDetail.alertRule.id);
		                	self.$el.find(".SID-name").val(data.ruleDetail.alertRule.name);
		                	self.$el.find(".SID-rp").val(unescape(data.ruleDetail.alertRule.rp));
		                	self.$el.find(".SID-measurement").editableSelect('select', self.$el.find(".es-list").find("li[value='"+data.ruleDetail.alertRule.measurement+"']")).blur();
		                	self.$el.find(".SID-pastTime").val(data.ruleDetail.alertRule.pastTime);
		                	self.$el.find(".SID-func").val(data.ruleDetail.alertRule.func);
		                	var condition1Html = _.template(ConditionTemplate, {
								'conditionShow': "1",
								'alertConditionList': data.ruleDetail.alertConditionList,
							});
		                	self.$el.find(".conditionList1").append(condition1Html);
		            		var condition2Html = _.template(ConditionTemplate, {
								'conditionShow': "2",
								'alertConditionList': data.ruleDetail.alertConditionList,
							});
		            		self.$el.find(".conditionList2").append(condition2Html).hide();
		            		var condition3Html = _.template(ConditionTemplate, {
								'conditionShow': "3",
								'alertConditionList': data.ruleDetail.alertConditionList,
							});
		            		self.$el.find(".conditionList3").append(condition3Html).hide();
		            		self.$el.find(".SID-message").val(data.ruleDetail.alertRule.message);
		            		for(var i=0;i<data.ruleDetail.alertMethodList.length;i++){
		            			if(data.ruleDetail.alertMethodList[i].alertMethod == "email"){
		            				if(self.$el.find(".SID-mail").val() != ""){
		            					self.$el.find(".SID-mail").val(self.$el.find(".SID-mail").val()+","+data.ruleDetail.alertMethodList[i].config);
		            				}else{
		            					self.$el.find(".SID-mail").val(data.ruleDetail.alertMethodList[i].config);
		            				}
		            				if(self.$el.find(".SID-checkmail").attr("checked") != "checked"){
		            					self.$el.find(".SID-checkmail").click();
		            				}
		            			}else if(data.ruleDetail.alertMethodList[i].alertMethod == "sms"){
		            				if(self.$el.find(".SID-msg").val() != ""){
		            					self.$el.find(".SID-msg").val(self.$el.find(".SID-msg").val()+","+data.ruleDetail.alertMethodList[i].config);
		            				}else{
		            					self.$el.find(".SID-msg").val(data.ruleDetail.alertMethodList[i].config);
		            				}
		            				if(self.$el.find(".SID-checkmsg").attr("checked") != "checked"){
		            					self.$el.find(".SID-checkmsg").click();
		            				}
		            			}
		            		}
		                },
		                _onChangeMeasurement:function(){
		                	var self = this;
		                	var selectmeasurementId = self.measurementId;
		                	var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
		                	$.ajax({
								type:"POST",
								url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.field.list&id="+selectmeasurementId,
								success:function(ajax){
									self.data.valuedefineList = ajax.list;
									var valuedefineStr = '<option value="" selected></option>';
									var valuedefineList = self.data.valuedefineList;
									for(var i=0;i<valuedefineList.length;i++){
										var selected = "";
//										if(measurementList[i].isDefault == "1" && self.data.measurementDetail == null){
//											selected = "selected";
//										}
										valuedefineStr += '<option value="'+valuedefineList[i].id+'" '+selected+'>'+valuedefineList[i].field+':'+unescape(valuedefineList[i].name).replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];})+'</option>';
									}
									self.$el.find(".SID-valueField").html(valuedefineStr);
									if(self.data.ruleDetail != null){
										self.$el.find(".SID-valueField").val(self.data.ruleDetail.alertRule.valueField);
										self._clickFunc();
									}
								}
							});
		                	var measurement = self.$el.find('.SID-measurement').val();
							if(measurement != ""){
								$.ajax({
									type:"POST",
									url:servicePath+"?"+ropParam+ "&method=mapps.servicemanager.filter.get&id="+measurement,
									success:function(ajax){
										var kapacitorFilterHtml = _.template(kapacitorFilterTemplate, {
											'filterList': ajax.filterList,
										});
					                	self.$el.find(".SID-filterBox").html(kapacitorFilterHtml);
					                	if(self.data.ruleDetail != null){
					                		if(self.data.ruleDetail.alertRule.groupBy != ""){
					                			var groupbyList = self.data.ruleDetail.alertRule.groupBy.replaceAll("'","").split(",");
						                		for(var k=0;k<groupbyList.length;k++){
						                			self.$el.find("#"+groupbyList[k]+"groupby").attr("checked","checked");
						                		}
					                		}
					                		if(self.data.ruleDetail.alertRule.whEre != ""){
					                			var whereList = self.data.ruleDetail.alertRule.whEre.split(" and ");
						                		for(var k=0;k<whereList.length;k++){
						                			var wherekey = whereList[k].split("=")[0].replaceAll("\"","");
						                			var wherevalue = whereList[k].split("=")[1].replaceAll("'","");
						                			self.$el.find("#"+wherekey+"tag"+wherevalue).attr("checked","checked");
						                		}
					                		}
					                		self._clickFilter();
										}
									}
								});
							}
		                },
		                _clickFunc :function(){
		                	var self = this;
		                	if(self.$el.find(".SID-func").val() != ""){
		                		self.$el.find(".SID-field-value-tag").html("{{index .Fields \""+self.$el.find(".SID-func").val()+"\"}}");
		                	}else{
		                		var valueField = self.$el.find(".SID-valueField").find("option:selected").text();
		                		if(valueField != ""){
		                			valueField = valueField.split(":")[0];
		                			self.$el.find(".SID-field-value-tag").html("{{index .Fields \""+valueField+"\"}}");
		                		}
		                	}
		                },
		                _selectTab :function(Event){
							var _this=$(Event.currentTarget);
							this.$el.find(".SID-smallmenu li").removeClass("active");
							_this.addClass("active");
							
                            this.$el.find(".conditionList1").hide();
                            this.$el.find(".conditionList2").hide();
                            this.$el.find(".conditionList3").hide();
                            this.conditionShow = _this.data("name");
                            this.$el.find(".conditionList"+_this.data("name")).show();
							return this;
						},
						_onClickAddCondition:function(e){
							var self = this;
							var alertConditionList = [];
							var ruleType = "";
							if(self.conditionShow == "1"){
								ruleType = "threshold";
							}else if(self.conditionShow == "2"){
								ruleType = "relative";
							}else if(self.conditionShow == "3"){
								ruleType = "deadman";
							}
							var alertCondition = {
				    			id:"",
				    			ruleId:"",
				    			ruleType:ruleType,
				    			alertLevel:"",
				    			setting1:"",
				    			setting2:"",
				    			setting3:"",
				    			setting4:"",
				    		};
							alertConditionList.push(alertCondition);
							//if(this.conditionNum[this.conditionShow] < 5){
								var conditionHtml = _.template(ConditionTemplate, {
									'conditionShow': self.conditionShow,
									'alertConditionList': alertConditionList,
								});
			            		this.$el.find(".conditionList"+this.conditionShow).append(conditionHtml);
			            		this.conditionNum[this.conditionShow] ++;
							//}
						},
						_showFilter:function(e){
							var self = this;
							//过滤模块宽度调整
							self.$el.find("#bigbox").width(self.$el.find("#kapacitorFilterBox").width()-1);
							self.$el.find('#bigbox').show();
						},
						_closeFilter:function(e){
							this.$el.find('#bigbox').hide();
						},
						_closeCondition:function(e){
							$(e.currentTarget).parent().parent().parent().remove();
						},
						_toggleFilter:function(e){
							var self = this;
							if($(e.currentTarget).find("span").attr("class").indexOf("fhicon-arrowU2") > -1){
								$(e.currentTarget).find("span").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
								$(e.currentTarget).parent().parent().find(".tagvalueBox").hide();
							}else{
								$(e.currentTarget).find("span").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
								$(e.currentTarget).parent().parent().find(".tagvalueBox").show();
							}
						},
						_clickFilter:function(e){
							var self = this;
							var tagvalueStr = "";
							var groupbyStr = "";
							var connectStr = "";
							var tagvalueList = self.$el.find('.SID-filterBox').find(".SID-filter-tagvalue");
							for(var i=0;i<tagvalueList.length;i++){
								if($(tagvalueList[i]).attr("checked") == "checked"){
									var tag = $(tagvalueList[i]).attr("data-tag");
									var onestr = "\""+tag+"\"='"+$(tagvalueList[i]).val()+"'";
									if(tagvalueStr == ""){
										tagvalueStr += onestr;
									}else{
										tagvalueStr += " and "+onestr;
									}
								}
							}
							var groupbyList = self.$el.find('.SID-filterBox').find(".SID-filter-groupby");
							
							//重置标签tag模板box
							self.$el.find('.SID-LabelsBox').html(self.$el.find('.SID-LabelsBoxStatic').html());
							
							for(var i=0;i<groupbyList.length;i++){
								if($(groupbyList[i]).attr("checked") == "checked"){
									var tag = "'"+$(groupbyList[i]).attr("data-tag")+"'";
									if(groupbyStr == ""){
										groupbyStr += tag;
									}else{
										groupbyStr += ","+tag;
									}
									//定义标签tag模板
									var LabelTagTemplate = '<a class="meet-add SID-insertword" style="padding: 0 10px;display:inline-block">{{ index .Tags "'+$(groupbyList[i]).attr("data-tag")+'" }}</a>';
									self.$el.find('.SID-LabelsBox').append(LabelTagTemplate);
								}
							}
							if(tagvalueStr != "" && groupbyStr != ""){
								connectStr = " | ";
							}
							var filterStr = tagvalueStr+connectStr+((groupbyStr == "")?"":("group by "+groupbyStr));
							self.$el.find('.SID-kapacitorFilter').val(filterStr);
							self.finalwhere = tagvalueStr;
							self.finalgroupby = groupbyStr;
						},
						_onEditConfig:function(e){
							var self = this;
							var tx = self.$el.find(".SID-message")[0];
							self.pos = cursorPosition.get(tx);
						},
						_onClickInsertWord:function(e){
							var self = this;
							var tx = self.$el.find(".SID-message")[0];
							var insertword=$(e.currentTarget).html();
							cursorPosition.add(tx, self.pos, insertword);
						},
                        _saveForm:function(){
				        	var self = this;
				        	var method = "mapps.servicemanager.rule.save";
				        	this._sumbitForm(method,"保存预警规则",self._closeMethod);
				        },
				        _addForm:function(){
				        	var self = this;
				        	var method = "mapps.servicemanager.rule.save";
				        	this._sumbitForm(method,"保存预警规则",self._closeMethod);
				        },
				        _closeMethodTest:function(title,commonDialog,_self){
			    			_self._exalert(title+"成功",false,function(){
							});
				        },
                        _closeMethod:function(title,_self){
							fh.alert(title+"成功",false,function(){
								_self.parentView.holdList();
								_self.parentView._onClickCancel();
							});
                        },
                        _exalert:function(info,ischild,handler){
                        	var _self = this;
							fh.alert(info,true,handler,_self.dialog,null);
                        },
                        _extraValidate:function(){
                        	var self = this;
                        	var checkResult = {
                        		checkFlag:true,
                        		alertConditionList:[],
                        	};
                        	$(self.$el.find(".SID-mail").parents(".form-item").find('.wrong-text')).html("");
                        	$(self.$el.find(".SID-msg").parents(".form-item").find('.wrong-text')).html("");
                        	$(self.$el.find(".SID-message").parents(".form-item").find('.wrong-text')).html("");
                        	if(self.$el.find(".SID-checkmail").attr("checked") == "checked"){
                        		if(self.$el.find(".SID-mail").val() == ""){
                        			$(self.$el.find(".SID-mail").parents(".form-item").find('.wrong-text')).append("请填写邮件地址");
                        			checkResult.checkFlag = false;
                        		}else{
                        			var mailList = self.$el.find(".SID-mail").val().split(",");
                        			var mailerror = 0;
                        			for(var j=0;j<mailList.length;j++){
                        				if(new RegExp(/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/).test(mailList[j]) == false){
                            				checkResult.checkFlag = false;
                            				mailerror = 1;
                            			}
                        			}
                        			if(mailerror == 1){
                        				$(self.$el.find(".SID-mail").parents(".form-item").find('.wrong-text')).append("邮件地址格式不正确!多个邮箱请以英文逗号隔离.");
                        			}
                        		}
                        	}
                        	if(self.$el.find(".SID-checkmsg").attr("checked") == "checked"){
                        		if(self.$el.find(".SID-msg").val() == ""){
                        			$(self.$el.find(".SID-msg").parents(".form-item").find('.wrong-text')).append("请填写短信地址");
                        			checkResult.checkFlag = false;
                        		}else{
                        			var msgList = self.$el.find(".SID-msg").val().split(",");
                        			var msgerror = 0;
                        			for(var j=0;j<msgList.length;j++){
                        				if(new RegExp(/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/).test(msgList[j]) == false){
                            				checkResult.checkFlag = false;
                            				msgerror = 1;
                            			}
                        			}
                        			if(msgerror == 1){
                        				$(self.$el.find(".SID-msg").parents(".form-item").find('.wrong-text')).append("手机号码格式不正确!多个号码请以英文逗号隔离.");
                        			}
                        		}
                        	}
                        	if(self.$el.find(".SID-checkmail").attr("checked") != "checked" && self.$el.find(".SID-checkmsg").attr("checked") != "checked"){
                        		$(self.$el.find(".SID-mail").parents(".form-item").find('.wrong-text')).append("请至少勾选一种报警方式");
                        		$(self.$el.find(".SID-msg").parents(".form-item").find('.wrong-text')).append("请至少勾选一种报警方式");
                        		checkResult.checkFlag = false;
                        	}
//                        	if(self.$el.find(".SID-checkmail").attr("checked") == "checked" || self.$el.find(".SID-checkmsg").attr("checked") == "checked"){
//                        		if(self.$el.find(".SID-message").val() == ""){
//                        			$(self.$el.find(".SID-message").parents(".form-item").find('.wrong-text')).append("勾选告警方式后请填写信息!");
//                        			checkResult.checkFlag = false;
//                        		}
//                        	}
                        	
                        	var conditionList1 = self.$el.find(".conditionList1").find(".form-item-editbox");
					    	for(var i=0;i<conditionList1.length;i++){
					    		var alertCondition = {
					    			id:"",
					    			ruleId:"",
					    			ruleType:"threshold",
					    			alertLevel:$(conditionList1[i]).find(".SID-conditionmethod").val(),
					    			setting1:$(conditionList1[i]).find(".SID-conditionrange").val(),
					    			setting2:$(conditionList1[i]).find(".SID-conditionvalue").val().trim(),
					    		};
					    		if(alertCondition.setting2 != ""){
					    			if(alertCondition.setting1 == "in" || alertCondition.setting1 == "out"){
						    			if(new RegExp("\\([0-9]{1,},[0-9]{1,}\\)").test(alertCondition.setting2)){
						    				$(conditionList1[i]).parents(".conditionBox").find('.wrong-text').html("");
											$(conditionList1[i]).parents(".conditionBox").find('.form-item-tip').hide();
						    			}else{
						    				$(conditionList1[i]).parents(".conditionBox").find('.wrong-text').html("请填写范围值:(a,b)");
											$(conditionList1[i]).parents(".conditionBox").find('.form-item-tip').show();
											checkResult.checkFlag = false;
						    			}
						    		}else{
						    			if(new RegExp("^[0-9]*$").test(alertCondition.setting2)){
						    				$(conditionList1[i]).parents(".conditionBox").find('.wrong-text').html("");
											$(conditionList1[i]).parents(".conditionBox").find('.form-item-tip').hide();
						    			}else{
						    				$(conditionList1[i]).parents(".conditionBox").find('.wrong-text').html("请填写数字");
											$(conditionList1[i]).parents(".conditionBox").find('.form-item-tip').show();
											checkResult.checkFlag = false;
						    			}
						    		}
						    		checkResult.alertConditionList.push(alertCondition);
					    		}else{
					    			$(conditionList1[i]).parents(".conditionBox").find('.wrong-text').html("");
									$(conditionList1[i]).parents(".conditionBox").find('.form-item-tip').hide();
					    		}
					    	}
					    	
					    	var conditionList2 = self.$el.find(".conditionList2").find(".form-item-editbox");
					    	for(var i=0;i<conditionList2.length;i++){
					    		var alertCondition = {
					    			id:"",
					    			ruleId:"",
					    			ruleType:"relative",
					    			alertLevel:$(conditionList2[i]).find(".SID-conditionmethod").val(),
					    			setting1:$(conditionList2[i]).find(".SID-conditiontime").val(),
					    			setting2:$(conditionList2[i]).find(".SID-conditionrate").val(),
					    			setting3:$(conditionList2[i]).find(".SID-conditionrange").val(),
					    			setting4:$(conditionList2[i]).find(".SID-conditionvalue").val().trim(),
					    		};
					    		if(alertCondition.setting4 != ""){
					    			if(alertCondition.setting3 == "in" || alertCondition.setting3 == "out"){
						    			if(new RegExp("\\([0-9]{1,},[0-9]{1,}\\)").test(alertCondition.setting4)){
						    				$(conditionList2[i]).parents(".conditionBox").find('.wrong-text').html("");
											$(conditionList2[i]).parents(".conditionBox").find('.form-item-tip').hide();
						    			}else{
						    				$(conditionList2[i]).parents(".conditionBox").find('.wrong-text').html("请填写范围值:(a,b)");
											$(conditionList2[i]).parents(".conditionBox").find('.form-item-tip').show();
											checkResult.checkFlag = false;
						    			}
						    		}else{
						    			if(new RegExp("^[0-9]*$").test(alertCondition.setting4)){
						    				$(conditionList2[i]).parents(".conditionBox").find('.wrong-text').html("");
											$(conditionList2[i]).parents(".conditionBox").find('.form-item-tip').hide();
						    			}else{
						    				$(conditionList2[i]).parents(".conditionBox").find('.wrong-text').html("请填写数字");
											$(conditionList2[i]).parents(".conditionBox").find('.form-item-tip').show();
											checkResult.checkFlag = false;
						    			}
						    		}
						    		checkResult.alertConditionList.push(alertCondition);
					    		}else{
					    			$(conditionList2[i]).parents(".conditionBox").find('.wrong-text').html("");
									$(conditionList2[i]).parents(".conditionBox").find('.form-item-tip').hide();
					    		}
					    	}
					    	
					    	var conditionList3 = self.$el.find(".conditionList3").find(".form-item-editbox");
					    	for(var i=0;i<conditionList3.length;i++){
					    		var alertCondition = {
					    			id:"",
					    			ruleId:"",
					    			ruleType:"deadman",
					    			alertLevel:$(conditionList3[i]).find(".SID-conditionmethod").val(),
					    			setting1:$(conditionList3[i]).find(".SID-conditiontime").val(),
					    		};
						    	checkResult.alertConditionList.push(alertCondition);
					    	}
                        	
                        	return checkResult;
                        },
						_sumbitForm : function(method,title,closeMethod){
							var param = "";
							var _self = this;
							var measurement = _self.$el.find(".SID-measurement").val();
                        	var ismeasurementexit = 0;
							var measurements = _self.data.measurementList;
							for(var k=0;k<measurements.length;k++){
								if(measurements[k].measurement == measurement){
									ismeasurementexit = 1;
								}
							}
							if(ismeasurementexit == 0){
								fh.alert("指标:"+measurement+"不存在<br/>请选择下拉菜单中的指标");
								return;
							}
							var extraValidate = _self._extraValidate();
						    this.validateForm("formMsg");
						    if(this.validateResult() && extraValidate.checkFlag){
						    	var data = {};
						    	
						    	var alertRule = {
						    		id:_self.$el.find(".SID-id").val(),
						    		systemId:"",
						    		name:escape(_self.$el.find(".SID-name").val()),
						    		pastTime:_self.$el.find(".SID-pastTime").val(),
						    		rp:_self.$el.find(".SID-rp").val(),
						    		measurement:_self.measurementId,
						    		valueField:_self.$el.find(".SID-valueField").val(),
						    		message:escape(_self.$el.find(".SID-message").val().replaceAll(/\+/g,"%2B")),
						    		func:_self.$el.find(".SID-func").val(),
						    		whEre:_self.finalwhere,
						    		groupBy:_self.finalgroupby,
						    		enabled:"",
						    	};
						    	
						    	var alertMethodList = [];
						    	if(_self.$el.find(".SID-checkmail").attr("checked") == "checked"){
						    		var mailList = _self.$el.find(".SID-mail").val().split(",");
						    		for(var j=0;j<mailList.length;j++){
						    			var alertMethod = {
							    			id:"",
							    			ruleId:"",
							    			alertMethod:"email",
							    			config:mailList[j],
							    		};
							    		alertMethodList.push(alertMethod);
                        			}
						    	}
						    	if(_self.$el.find(".SID-checkmsg").attr("checked") == "checked"){
						    		var msgList = _self.$el.find(".SID-msg").val().split(",");
						    		for(var j=0;j<msgList.length;j++){
						    			var alertMethod = {
							    			id:"",
							    			ruleId:"",
							    			alertMethod:"sms",
							    			config:msgList[j],
							    		};
							    		alertMethodList.push(alertMethod);
                        			}
						    	}
						    	
						    	data.alertRule = alertRule;
						    	data.alertMethodList = alertMethodList;
						    	data.alertConditionList = extraValidate.alertConditionList;
						    	var param = JSON.stringify(data);
								
				                var appContext = this.getAppContext();
								var servicePath = appContext.cashUtil.getData('servicePath');
								var ropParam = appContext.cashUtil.getData('ropParam');
								showCover();
                                $.ajax({
                                    type : "POST",
                                    url:encodeURI(servicePath+"?"+ropParam+ "&method="+method+"&ruleSaveJson="+param),
                                    success : function(response) {
                                    	hideCover();
                                        if(response.code == "1"){
                                            closeMethod(title,_self);
                                        }else{
                                            fh.alert(response.message);
                                        }
                                    },
                                    error : function(){
                                    	hideCover();
                                        fh.alert("数据处理失败");
                                    }
                                });
						    }
						}
					});
			return AddSnippetView;
		});
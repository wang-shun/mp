define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/law/lawDetailTemplate.html','text!../../templates/law/lawExpDetailTemplate.html',
		  'text!../../templates/law/lawSamplingTableTemplate.html'],
		function($, CommunicationBaseView,Template,ExpTemplate,TemplateSamplingTable) {
			var treeObj;
			var datatable;
			var RoomViewSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
						},
						render : function(ajax) {
							this._setContentHTML(ajax);
							return this;
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
							this.remove();
						},
						_setContentHTML : function (ajax){
							var self = this;
							var type = ajax.type
							var html="";
							if(type=="1"){
								var template = _.template(Template);
								html = template({
									'law' :ajax.law,
									'lawFiles' :ajax.lawFiles
								});
							}else{
								var template = _.template(ExpTemplate);
								html = template({
									'law' :ajax.lawExp,
									'lawFiles' :ajax.lawFiles
								});
							}
							
							this.$el.append(html);
							self.$el.find(".SID-samplingTable").empty();
							var htmlTable = _.template(TemplateSamplingTable, {
								'lawSamplings' :ajax.lawSamplings
							});
							self.$el.find(".SID-samplingTable").append(htmlTable);
							var commonDialog;
							if(type=="1"){
								commonDialog = fh.commonOpenDialog('DetailDialog', "进口食品/化妆品检验检疫监督记录单详情：", 750, 800, this.el);
								commonDialog.addBtn("cannel","关闭",function(){
									self.destroy();
									commonDialog.cancel();
								});
								this._initData(ajax);
							}else{
								commonDialog = fh.commonOpenDialog('DetailDialog', "出口食品/化妆品检验检疫监督记录单详情：", 750, 800, this.el);
								commonDialog.addBtn("cannel","关闭",function(){
									self.destroy();
									commonDialog.cancel();
								});
								this._initExpData(ajax);
							}
							
							
						},
						_initData :function (ajax){
							//1.运输包装情况：
							var packingTrans = ajax.law.packingTrans
							if("0"==packingTrans){
								var packing_trans0 = document.getElementById("packing_trans0");
								packing_trans0.checked=true;
							}else if("1"==packingTrans){
								var packing_trans1 = document.getElementById("packing_trans1");
								packing_trans1.checked=true;
							}else if("2"==packingTrans){
								var packing_trans2 = document.getElementById("packing_trans2");
								packing_trans2.checked=true;
							}
							//2.有无木质包装：
							var iwoodPackaging = ajax.law.iwoodPackaging
							if("0"==iwoodPackaging){
								var iwood_packaging0 = document.getElementById("iwood_packaging0");
								iwood_packaging0.checked=true;
							}else if("1"==iwoodPackaging){
								var iwood_packaging1 = document.getElementById("iwood_packaging1");
								iwood_packaging1.checked=true;
							}
							//3.木质包装是否合格：
							var woodPackagingGualified = ajax.law.woodPackagingGualified
							if("0"==woodPackagingGualified){
								var wood_packaging_qualified0 = document.getElementById("wood_packaging_qualified0");
								wood_packaging_qualified0.checked=true;
							}else if("1"==woodPackagingGualified){
								var wood_packaging_qualified1 = document.getElementById("wood_packaging_qualified1");
								wood_packaging_qualified1.checked=true;
							}
							//4.现场、运输工具卫生状况是否符合要求：
							var healthStatus = ajax.law.healthStatus
							if("0"==healthStatus){
								var health_status0 = document.getElementById("health_status0");
								health_status0.checked=true;
							}else if("1"==healthStatus){
								var health_status1 = document.getElementById("health_status1");
								health_status1.checked=true;
							}
							
							//5.齿动物及其、蚊、蝇、蟑螂等医学媒介：
							var medicalVector = ajax.law.medicalVector
							if("0"==medicalVector){
								var medical_vector0 = document.getElementById("medical_vector0");
								medical_vector0.checked=true;
							}else if("1"==medicalVector){
								var medical_vector1 = document.getElementById("medical_vector1");
								medical_vector1.checked=true;
							}
							//6.夹带废旧物品、生活垃圾及其它有毒有害物质：
							var poisonousHarmful = ajax.law.poisonousHarmful
							if("0"==poisonousHarmful){
								var poisonous_harmful0 = document.getElementById("poisonous_harmful0");
								poisonous_harmful0.checked=true;
							}else if("1"==poisonousHarmful){
								var poisonous_harmful1 = document.getElementById("poisonous_harmful1");
								poisonous_harmful1.checked=true;
							}
							//7.携带土壤、杂草、动物尸体、动植物残留物：
							var residue = ajax.law.residue
							if("0"==residue){
								var residue0 = document.getElementById("residue0");
								residue0.checked=true;
							}else if("1"==residue){
								var residue1 = document.getElementById("residue1");
								residue1.checked=true;
							}
							
							//8.是否与其他有毒有害物质混装：
							var otherMixed = ajax.law.otherMixed
							if("0"==otherMixed){
								var other_mixed0 = document.getElementById("other_mixed0");
								other_mixed0.checked=true;
							}else if("1"==otherMixed){
								var other_mixed1 = document.getElementById("other_mixed1");
								other_mixed1.checked=true;
							}
							
							//9.货证是否相符：
							var cargoCertificate = ajax.law.cargoCertificate
							if("0"==cargoCertificate){
								var cargo_certificate0 = document.getElementById("cargo_certificate0");
								cargo_certificate0.checked=true;
							}else if("1"==cargoCertificate){
								var cargo_certificate1 = document.getElementById("cargo_certificate1");
								cargo_certificate1.checked=true;
							}
							//10.其他异常及不符合情况：
							var otherNotconform = ajax.law.otherNotconform
							if("0"==otherNotconform){
								var other_notconform0 = document.getElementById("other_notconform0");
								other_notconform0.checked=true;
							}else if("1"==otherNotconform){
								var other_notconform1 = document.getElementById("other_notconform1");
								other_notconform1.checked=true;
							}
							
							//11.抽/采样：
							var sampling = ajax.law.sampling
							if("0"==sampling){
								var sampling0 = document.getElementById("sampling0");
								sampling0.checked=true;
							}else if("1"==sampling){
								var sampling1 = document.getElementById("sampling1");
								sampling1.checked=true;
							}
							//12.是否送检：
							var submission = ajax.law.submission
							if("0"==submission){
								var submission0 = document.getElementById("submission0");
								submission0.checked=true;
							}else if("1"==submission){
								var submission1 = document.getElementById("submission1");
								submission1.checked=true;
							}
							//14.产品包装是否完整
							var packingWhole = ajax.law.packingWhole
							if("0"==packingWhole){
								var packing_whole0 = document.getElementById("packing_whole0");
								packing_whole0.checked=true;
							}else if("1"==packingWhole){
								var packing_whole1 = document.getElementById("packing_whole1");
								packing_whole1.checked=true;
							}
							//15.标签检验：
							var labelCheck = ajax.law.labelCheck
							if("0"==labelCheck){
								var label_check0 = document.getElementById("label_check0");
								label_check0.checked=true;
							}else if("1"==labelCheck){
								var label_check1 = document.getElementById("label_check1");
								label_check1.checked=true;
							}
							//16.是否允许技术处理：
							var technicalProcessing = ajax.law.technicalProcessing
							if("0"==technicalProcessing){
								var technical_processing0 = document.getElementById("technical_processing0");
								technical_processing0.checked=true;
							}else if("1"==technicalProcessing){
								var technical_processing1 = document.getElementById("technical_processing1");
								technical_processing1.checked=true;
							}
						},
						_initExpData : function(ajax){
							//1.检验检疫方式：
							var inspectionAndQuarantine = ajax.lawExp.inspectionAndQuarantine
							if("0"==inspectionAndQuarantine){
								var Inspection_and_quarantine0 = document.getElementById("Inspection_and_quarantine0");
								Inspection_and_quarantine0.checked=true;
							}else if("1"==inspectionAndQuarantine){
								var Inspection_and_quarantine1 = document.getElementById("Inspection_and_quarantine1");
								Inspection_and_quarantine1.checked=true;
							}else if("2"==inspectionAndQuarantine){
								var Inspection_and_quarantine2 = document.getElementById("Inspection_and_quarantine2");
								Inspection_and_quarantine2.checked=true;
							}
							//2.现场、运输工具卫生状况是否符合要求：
							var healthStatus = ajax.lawExp.healthStatus
							if("0"==healthStatus){
								var health_status0 = document.getElementById("health_status0");
								health_status0.checked=true;
							}else if("1"==healthStatus){
								var health_status1 = document.getElementById("health_status1");
								health_status1.checked=true;
							}
							//3.是否与其他有毒有害物质混装：
							var otherMixed = ajax.lawExp.otherMixed
							if("0"==otherMixed){
								var other_mixed0 = document.getElementById("other_mixed0");
								other_mixed0.checked=true;
							}else if("1"==otherMixed){
								var other_mixed1 = document.getElementById("other_mixed1");
								other_mixed1.checked=true;
							}
							//4.运输包装情况：
							var packingTrans = ajax.lawExp.packingTrans
							if("0"==packingTrans){
								var packing_trans0 = document.getElementById("packing_trans0");
								packing_trans0.checked=true;
							}else if("1"==packingTrans){
								var packing_trans1 = document.getElementById("packing_trans1");
								packing_trans1.checked=true;
							}else if("2"==packingTrans){
								var packing_trans2 = document.getElementById("packing_trans2");
								packing_trans2.checked=true;
							}else if("3"==packingTrans){
								var packing_trans3 = document.getElementById("packing_trans3");
								packing_trans3.checked=true;
							}else if("4"==packingTrans){
								var packing_trans4 = document.getElementById("packing_trans4");
								packing_trans4.checked=true;
							}
							//5.感官检验：
							var sensoryTest = ajax.lawExp.sensoryTest
							if("0"==sensoryTest){
								var other_mixed0 = document.getElementById("sensory_test0");
								sensory_test0.checked=true;
							}else if("1"==sensoryTest){
								var sensory_test1 = document.getElementById("sensory_test1");
								sensory_test1.checked=true;
							}
							
							//6.产品包装是否完整
							var packingWhole = ajax.lawExp.packingWhole
							if("0"==packingWhole){
								var packing_whole0 = document.getElementById("packing_whole0");
								packing_whole0.checked=true;
							}else if("1"==packingWhole){
								var packing_whole1 = document.getElementById("packing_whole1");
								packing_whole1.checked=true;
							}
							//7.有无渗漏等异常：
							var leakageAnomaly = ajax.lawExp.leakageAnomaly
							if("0"==leakageAnomaly){
								var leakage_anomaly0 = document.getElementById("leakage_anomaly0");
								leakage_anomaly0.checked=true;
							}else if("1"==leakageAnomaly){
								var leakage_anomaly1 = document.getElementById("leakage_anomaly1");
								leakage_anomaly1.checked=true;
							}
							
							//8.标签检验：
							var labelCheck = ajax.lawExp.labelCheck
							if("0"==labelCheck){
								var label_check0 = document.getElementById("label_check0");
								label_check0.checked=true;
							}else if("1"==labelCheck){
								var label_check1 = document.getElementById("label_check1");
								label_check1.checked=true;
							}
							//9.抽/采样：
							var isneedSmpl = ajax.lawExp.isneedSmpl
							if("0"==isneedSmpl){
								var sampling0 = document.getElementById("sampling0");
								sampling0.checked=true;
							}else if("1"==isneedSmpl){
								var sampling1 = document.getElementById("sampling1");
								sampling1.checked=true;
							}
							
							
							
							
							
						}
						
					});
			return RoomViewSnippetView;
		});
define(["jquery","preferenceUtil", "dataTables_min", "datatable_lnpagination","datatable_columresize"], 
	function($,preferenceUtil){
		var datatableObj = {};
		var perPageNum;
		var that;
		var objId;
		datatableObj = function(tableObj, parmObj ,callback ,completecallback,domObj) {
			//表格所在view的对象
			if(domObj){
				that = domObj;
			}
			
			perPageNum = 10;
			
			if(!tableObj.bPaginate){
				perPageNum = "-1";
			}
			
			objId=tableObj.tbID ? tableObj.tbID : "grid-table";
			
			$("#jsFfTips").text("").hide();
		    //查询 checkbox依旧选中的bug修复
			if(tableObj && tableObj.url){
			    if($("#"+objId) && $("#"+objId+" thead").find("input:checkbox").length>0){
			        $("#"+objId+" thead").find("input:checkbox").removeAttr("checked");
			    }
			    //是否显示title
			    var isShowTitle=tableObj.showTitle ?  tableObj.showTitle : false;
			    // 是否显示分页插件
				var hidepage = tableObj.nopage ? tableObj.nopage : "showpage";
				//是否有遮罩效果
				var hasCover=(tableObj.hasCover == undefined || tableObj.hasCover != false) ? true : false; 
				tableObj.aaSorting = tableObj.aaSorting != undefined
						? tableObj.aaSorting
						: [];
				var datatable = $('#'+objId).dataTable({
					"aLengthMenu" : tableObj.aLengthMenu ? tableObj.aLengthMenu : [[10,15,20,25], [10,15,20,25]],
					"iDisplayLength" : perPageNum ? perPageNum : 10,
					"iDisplayStart" : tableObj.iDisplayStart ? tableObj.iDisplayStart : 0,
		 			"bDestroy" : true,
					"bServerSide" : true,
					"bAutoWidth": false,
					"sPaginationType" : "lnPagination",
					//"bProcessing" : true,s
					"oLanguage" : {
						sLengthMenu : "_MENU_ 条记录",
						sInfo : "共 _TOTAL_ 条记录,每页显示",
						sInfoEmpty : "共 0 条记录,每页",
						sInfoFiltered : "",
						sEmptyTable : "没有符合条件的数据！",
						//sProcessing : "正在加载数据...",
						sZeroRecords: "没有符合条件的数据！"
					},				
					"bFilter" : true,
					"sServerMethod" : "POST",
					"bPaginate":tableObj.bPaginate ? true : tableObj.bPaginate,
					"aaSorting" : tableObj.aaSorting,
					"sAjaxSource" : tableObj.url,
					"fnServerData": function (sSource, aoData, fnCallback, settings) {
						if(hasCover){
							showCover("正在加载数据······");
						}
			            var sEcho = '';
			            for (var i = 0; i < aoData.length; i++) {
			                if (aoData[i].name == 'sEcho') {
			                    sEcho = aoData[i].value;
			                }
			            }
			            aoData=togetherParams(aoData, tableObj, parmObj,settings);
			            var sort = $.parseJSON(aoData).page.sort;
			            var sortName = $.parseJSON(aoData).page.sortName;
			            sSource = tableObj.url + "&limit=" + settings._iDisplayLength
			                    + "&offset="+Math.ceil(Math.ceil(parseInt(settings._iDisplayStart) / parseInt(settings._iDisplayLength)) +1)
			                    + "&sort=" + sortName + " " + sort;
			            //params
			            settings.jqXHR = $.ajax({
			            	"type":"POST",
			                "url": sSource,
			                "data": aoData,
			                "success":function(responseBody,resultcode,resultmessage){
			                	if(typeof(callback)=="function"){
				                	responseBody = callback(responseBody)
				                }
				                hideCover();	
				                
				                fnCallback(responseBody);
								if(typeof(completecallback)=="function"){
									completecallback(responseBody)
				                }
			                },
			                "dataType": "json",
			                "contentType":"application/json",
			                "cache": false,
			                "error": function (xhr, error, thrown) {
			                	 hideCover();
			                    if (error == "parsererror") {
			                    	fh.alert("DataTables warning: JSON data from server could not be parsed. " +
			                                            "This is caused by a JSON formatting error.");
			                    }else{
			                    	fh.alert("服务器错误");
			                    }
			                }
		            	});
	        		},
		        	"fnStateLoad":function(){},
					"fnServerParams" : tableObj.fnServerParams?tableObj.fnServerParams:null,
					"aoColumns" : tableObj.aoColumns,
					"sDom" : 'zt<"bottom    ' + hidepage + ' clearfix"iflrp>',
					"fnCreatedRow" : tableObj.fnCreatedRow
							? function(nRow, aData, iDataIndex){
								tableObj.fnCreatedRow(nRow, aData, iDataIndex)
								createdRowFn(nRow, aData, iDataIndex);
								//row中的dom 动态绑定事件
								optClickFun(nRow);
							}
							: function(nRow, aData, iDataIndex) {
								createdRowFn(nRow, aData, iDataIndex);
								//row中的dom 动态绑定事件
								optClickFun(nRow);
							},
					"fnHeaderCallback" : tableObj.fnHeaderCallback
							? tableObj.fnHeaderCallback
							: function(nHead, aData, iStart, iEnd, aiDisplay) {
								headCheck(nHead, aData, iStart, iEnd, aiDisplay);
							},
					"fnDrawCallback":function(oSettings ){
						//列的显示与隐藏
						//setTimeout(function(){
						if($("#"+objId).find("tbody").find("tr").length<=1 && $("#"+objId).find("tbody").find("tr").find("td").length<=1){
							$("#"+objId).find("tbody").find("tr").find("td").attr("colspan",$("#"+objId).find("th").not(".hide").length)
						}else{
							$("#"+objId).find("thead th").each(function(index){
								if($(this).hasClass("hide")){
									$("#"+objId).find("tbody > tr").each(function(){
										$(this).find("td").eq(index).addClass("hide")
									});
								}
								else
								{
									$("#"+objId).find("tbody > tr").each(function(){
										$(this).find("td").eq(index).removeClass("hide")
									});
								}
							})
						}
						
						$("#"+objId+" th").click(function(){
							if(!$(this).hasClass("sorting_disabled")){
							}
						});
						
						$("select[name=grid-table_length]").live("change",function(){
							//preferenceUtil.customPreferenceSet("perPageNum",$(this).val());
							//perPageNum=customPreferenceLoad("perPageNum");
							perPageNum = $(this).val();
							if("" != perPageNum){
								$("select[name=grid-table_length]").val(perPageNum);
							}	
						});
					}
				});
				refreshDatable(datatable);
				$("#"+objId).find("tr").each(function(){
					var lastTd=$(this).find("td").last()
					if(lastTd.find("img").length>0 || lastTd.find("a").length>0){
						lastTd.css("overflow","visible")
					}
				})
				return datatable;
			}
		}
	
		// 拼接传送给服务器端的参数 格式：sort="proName asc,proCode desc&order=''"
		togetherParams =  function(aoData, tableObj, parmObj,settings) {
			//自定义的格式
			var customJson='{"page":{"offset":"'+Math.ceil(Math.ceil(parseInt(settings._iDisplayStart) / parseInt(settings._iDisplayLength)) +1)+'","limit":"'+settings._iDisplayLength+'","sort":"","sortName":""},"params":{}}'
			//升序字符
			var sortasc=[];
			//降序字符
			var sortdesc=[];
			//ret obj
			var retObj={};
			var sortColumnCount = 0;
			var sortField = [], sortDir = [], dataProp = [], sortProp = [], sortColumnIndex = [];
			var sortCollen=0;
			var sortStr = ""
			for (var a=0;a < aoData.length;a++) {
				if (aoData[a].name == "iSortingCols") {
					sortColumnCount = aoData[a].value
				}
				if (aoData[a].name.indexOf("mDataProp_") >= 0) {
	
					dataProp.push(aoData[a].value)
				}
				if (aoData[a].name.indexOf("sSortDir_") >= 0) {
					sortProp.push(aoData[a].value)
				}
				if (aoData[a].name.indexOf("iSortCol_") >= 0) {
					sortColumnIndex.push(aoData[a].value)
				}
				if (aoData[a].name.indexOf("default_param") >= 0) {
					sortColumnIndex.push({
						"default_param" : aoData[a].value
					})
				}
				if(aoData[a].name.indexOf("iSortingCols") >= 0){
					sortCollen=aoData[a].value;
				}
			}
			
			if (sortColumnCount >= 2) {
				for (var col = 0; col < sortColumnIndex.length; col++) {
					if (tableObj.aaSorting && tableObj.aaSorting.length == 0
							&& typeof(sortColumnIndex[col]) == "object") {
						sortStr = sortColumnIndex[col]["default_param"]
					} else {
						if (typeof(sortColumnIndex[col]) == "object")
							continue;
						sortField.push(dataProp[sortColumnIndex[col]])
						sortDir.push(sortProp[col])
						sortStr += dataProp[sortColumnIndex[col]] + " " + sortProp[col]
								+ " , "
					}
				}
	
				if (tableObj.aaSorting && tableObj.aaSorting.length == 0) {
				} else {
					sortStr = sortStr.slice(0, -2);
				}
	
				var trimStr = $.trim(sortStr);
				if (trimStr.charAt(trimStr.length - 1) == ",") {
					sortStr = sortStr.slice(0, -2);
				}
			}
			else if(sortColumnCount==1){
				customJson='{"page":{"offset":"'+Math.ceil(Math.ceil(parseInt(settings._iDisplayStart) /parseInt(settings._iDisplayLength)) +1)+'","limit":"'+settings._iDisplayLength+'","sort":"'+sortProp[0]+'","sortName":"'+dataProp[sortColumnIndex[0]]+'"},"params":{}}'	
			}
			else {
			}
			 //添加额外的参数的时候拼接参数
			if (parmObj) {
				customJson='{"page":{"offset":"'+Math.ceil(Math.ceil(parseInt(settings._iDisplayStart) /parseInt(settings._iDisplayLength)) +1)+'","limit":"'+settings._iDisplayLength+'","sort":"'+(sortProp[0] ? sortProp[0] : "") +'","sortName":"'+(dataProp[sortColumnIndex[0]] ? dataProp[sortColumnIndex[0]] : "")+'"},"params":{'+parmObj
				customJson+='}}'
			}
			
			for(var t=0;t<sortProp.length;t++){
				var curColIndex=sortColumnIndex[t]
				var curSortFName=dataProp[curColIndex]
				if(sortProp[t]=="desc"){
					sortdesc.push(curSortFName)
				}
				if(sortProp[t]=="asc"){
					sortasc.push(curSortFName)
				}
			}
			
			//判断是否有添加的默认排序
			if(sortColumnIndex.length>sortProp.length){
				var lastObj=sortColumnIndex[sortColumnIndex.length-1]
				var lastStr=lastObj.default_param.split(",")
				for(var sL=0;sL<lastStr.length;sL++)
				{
					
					if($.trim(lastStr[sL]).split(" ")[1]=="desc")
					{
						sortdesc.push($.trim(lastStr[sL]).split(" ")[0])
					}
					if($.trim(lastStr[sL]).split(" ")[1]=="asc")
					{
						sortasc.push($.trim(lastStr[sL]).split(" ")[0])
					}
				}
			}
			retObj.sortdesc=sortdesc.toString();
			retObj.sortasc=sortasc.toString();
			$("#"+objId).data("sortObj",retObj)
			var sEcho = '';
		    for (var i = 0; i < aoData.length; i++) {
		        if (aoData[i].name == 'sEcho') {
		            sEcho = aoData[i].value;
		        }
		    }
		    aoData.push({"name":"","value":customJson})
			return customJson;
	    }
	
		createdRowFn = function(nRow, aData, iDataIndex){
			// tr元素hover时改变背景色
			hoverChangeTdBg($(nRow));
			// 当前行记录中checkbox的选中状态（与TR元素相关）
			//setCheckRecord(nRow, aData, iDataIndex)
			//checkBox click
			setCheckBoxOnly(nRow, aData, iDataIndex)
			//给td添加title属性 显示全部内容
			showTdTitle(nRow, aData, iDataIndex)
		}
		
		// 表格 列表 hover的时改变颜色
		hoverChangeTdBg = function(obj) {
			//obj.find("td").last().css("overflow","visible")
			var lastTd=obj.find("td").last()
			if(lastTd.find("img").length>0 || lastTd.find("a").length>0){
				lastTd.css("overflow","visible")
			}
			obj.live("mouseover", function() {
				var _this = $(this);
				$("#"+objId).find("tr.contenttr").removeClass("contenttr")
				_this.addClass("hover_tr_tdbg")
			}).live("mouseout", function() {
				var _this = $(this);
				_this.removeClass("hover_tr_tdbg")
			})
		}
		
		//checkbox only click
		setCheckBoxOnly = function(nRow, aData, iDataIndex){
			$(nRow).find("input:checkbox").live("change",function(){
				//去除不能点击状态的checkbox对象
				//if($(this).is(":disabled")) return;
				if ($(this).is(":checked")) {
					//$(this).removeAttr("checked")
					$(this).parents("tr").addClass("selectedtr")
				} else {
					//单选情况
					if ($(this).parents("table").hasClass("Js_singleCheck")) {
						$(this).attr("checked", "checked")
						$(this).parents("tr").addClass("selectedtr").siblings("tr")
								.removeClass("selectedtr")
						$(this).parents("tr").siblings("tr").find("input:checkbox")
								.removeAttr("checked")
					} else {
						$(this).parents("tr").removeClass("selectedtr")
					}
				}
	
				var checked = $(this).parents("table").find("tbody")
						.find("input:checkbox:checked").length
				var all = $(this).parents("table").find("tbody").find("input:checkbox").length
				if (checked == all) {
					$(this).parents("table").find("th").find("input[type=checkbox]:enabled").attr(
							"checked", "checked")
				} else {
					$(this).parents("table").find("th").find("input:checkbox")
							.removeAttr("checked")
				}
			})
		}
		
		//给td添加title属性
		showTdTitle = function (nRow, aData, iDataIndex){
			//if(navigator.userAgent.indexOf("Firefox")>=0){
			//向body中插入一个<div>模拟title的展现  修复ff的bug
			var ffTip='<div id="jsFfTips" class="fftips"></div>'
			if($("#jsFfTips") && $("#jsFfTips").length<1){
				// $("body").append(ffTip);
			}
			
			$(nRow).find("td").each(function(index){
				var that=$(this);
				if($(this).find("a").length>0){
					if(0==index || 1==index){
						$(this).attr("rel",$(this).find("a").html())
					}else{
						return;
					}
				}else if($(this).find("img").length>0){
					if(0==index || 1==index){
						$(this).attr("rel",$(this).text())
					}else{
						return;
					}
				}else{
					$(this).attr("rel",$(this).html())
				}
				if($.trim($(this).text())=="") {
					$(this).removeAttr("rel")
				}
				that.hover(function(e){
					var eTop=Math.max(e.clientY+document.body.scrollTop+5,e.clientY+document.documentElement.scrollTop+5);
					var eLeft=Math.max(e.clientX+document.body.scrollLeft+5,e.clientX+document.documentElement.scrollLeft+5);
					if(that.attr("rel")==undefined) return;
					var titles =that.attr("rel");
					// that.attr("rel").replaceAll("<","&lt").replaceAll(">","&gt"); 
					titles = titles.replaceAll("&ltbr&gt","<br>"); 
					if(document.documentElement.clientWidth-100 <= 0){
						$("#jsFfTips").css({"top":eTop+"px","left":"auto","right":"0px"}).html(titles).show();
						if(eTop+20>=document.documentElement.clientHeight){
							$("#jsFfTips").css("top",document.documentElement.clientHeight-20+"px")
						}
					}
					else
					{
						$("#jsFfTips").css({"top":eTop+"px","left":eLeft+"px"}).html(titles).show();
						if(eTop+32>document.documentElement.clientHeight+Math.max(document.body.scrollTop,document.documentElement.scrollTop)){
							$("#jsFfTips").css({"top":document.documentElement.clientHeight+Math.max(document.body.scrollTop,document.documentElement.scrollTop)-32});
						}
					}
					
				},function(){
					$("#jsFfTips").text("").hide();
				})
			})
		}
		
		//动态绑定 交互效果
		optClickFun = function (nRow){
			$(nRow).find(".tb-opt-box").live("click",function(){
				//if($(this).children("img").attr("src").indexOf("devrulegrey.png")!=-1){
				//	return;
				//}
				var _this=$(this)
				//_this.parents(".dataTables_wrapper").css("z-index","8")
				var top=_this.offset().top;
				var _thisChild=_this.find("div.tb-opt-main");
				
				if(_thisChild.is(":visible")){
					_thisChild.hide();
					_thisChild.prev().removeClass("active");
				}
				else 
				{
					$("div.tb-opt-main").not(_thisChild).hide();
					$("div.tb-opt-main").not(_thisChild).prev().removeClass("active");
					if(_thisChild.outerHeight(true)<=2*top){
						var splitH=Math.floor(_thisChild.outerHeight(true)/2)
						//_thisChild.css("top","-"+splitH+"px").show();
						//_thisChild.find("img:first").css("top",splitH-4+"px")
					}
					else
					{
						//_thisChild.find("img:first").css("top",top-4+"px")
						//_thisChild.css("top","-"+top+"px").show();
					}
					_thisChild.show();
					_thisChild.prev().addClass("active");
				}
			})
			//子集的hover绑定
			$(nRow).find(".tb-opt-main").hover(function(){
				$(this).show();
				$(this).prev().addClass("active");
			},function(){
				$(this).hide();
				$(this).prev().removeClass("active");
				//$(this).parents(".dataTables_wrapper").css("z-index","1")
			})
		}
		
		// datatable checkbox
		headCheck = function (nHead, aData, iStart, iEnd, aiDisplay) {
			if ($(nHead).parents("table").hasClass("Js_singleCheck")) {
				$("input:checkbox", nHead).remove();
			} else {
				$("input:checkbox", nHead).live("change", function() {
					var checked = $(this).is(":checked");
					if (checked) {
						$(this).parents("table").find("input:checkbox:visible:enabled").attr("checked", checked);
						$(this).parents("table").find("input:checkbox:visible:enabled").parents("tr").addClass("selectedtr");
						
						if(that){
							that.checkStatus = true;
						}
					} else {
						$(this).parents("table").find("input:checkbox:visible:enabled").removeAttr("checked")
						$(this).parents("table").find("input:checkbox:visible:enabled").parents("tr").removeClass("selectedtr")
					}
				})
			}
	
		}
		
		// refresh datatable
		refreshDatable = function (datatable) {
			$(".datatable_refresh_ln").live("click", function() {
				$("#"+objId).find("input:checkbox").removeAttr("checked");
				datatable.fnDraw(false);
			})
		}
		
		showCover = function (string){
			var hMax=Math.max(document.documentElement.clientHeight, document.body.offsetHeight)
			var string=(string !=undefined && string!="") ? string : "正在处理中······";
			var clientH=hMax;
			var clientW=document.documentElement.clientWidth;
			//-ms-filter:”progid:DXImageTransform.Microsoft.Alpha(opacity=50)”; 解决IE8的各个小版本问题
			var coverDiv='<div class="Js_cover" style="position:absolute;top:0px;opacity:0.4;-ms-filter:progid:DXImageTransform.Microsoft.Alpha(opacity=50);filter:alpha(opacity=40); left:0px;z-index:9998; background-color:#fff;width:'+clientW+'px;height:'+clientH+'px;" class="coverbg"></div>'
			var coverContents='<div class="Js_coverMsg" style="position:absolute;z-index:9999; left:50%; top:50%; margin-top:-40px; margin-left:-125px; width:200px; height:30px; text-align:center;line-height:30px; padding:20px; background:#fff; border:5px solid #424954">'+string+'</div>'
			$(document.body).append(coverDiv)
			$(document.body).append(coverContents)
			$(document.body).find(".Js_cover").css("opacity","0.4")
		}
		
		//关闭遮罩层
		hideCover = function ()
		{
			if($(document.body).find(".Js_cover") && $(document.body).find(".Js_cover").length>0){
				$(document.body).find(".Js_cover").remove();
			}
			if($(document.body).find(".Js_coverMsg") && $(document.body).find(".Js_coverMsg").length>0){
				$(document.body).find(".Js_coverMsg").remove();
			}
		}
		
		//带checkbox的select初始化加载
		check_select_css = function(selectScope,checkStatus,that){
			//带checkbox的select
			//高级查询，新增修改删除等操作后，记忆原来状态
			if(selectScope == 'all'){
				that.$el.find("#check_select_id").val("all");
				if(checkStatus){
					if(that.$el.find("#"+objId+" input[type=checkbox][class!=SID-checkAll]").length>0){
						that.$el.find(".SID-page-tar").text("全部");
						that.$el.find(".SID-page-tar").prev().val("all");
						that.$el.find("#"+objId+" input[type=checkbox]").attr("checked","checked");
					}
				}
			}
		}
		// 获取选中的对象内容
		getSeleteObjs = function(datatable, tableObj) {
			// 获取选中的行的数据
			var selectedObj = [];
			var gridtable = $("#grid-table");
			if (tableObj != undefined) {
				if (tableObj.tableObjID != undefined && tableObj.tableObjID != "") {
					gridtable = $("#" + tableObj.tableObjID);
				}
			}

			for (var chktr = 0; chktr < gridtable.find("tbody")
				.find("input:checkbox:checked").length; chktr++) {
				var thisChkTr = gridtable.find("tbody")
					.find("input:checkbox:checked").eq(chktr).parents("tr");
				var thisIndex = gridtable.find("tbody").find("tr")
					.index(thisChkTr);
				selectedObj.push(datatable.fnGetData(thisChkTr[0]));
			}
			return selectedObj;
		}		
		// 获取选中的对象内容
		getAllObjs = function(datatable, tableObj) {
			// 获取选中的行的数据
			var selectedObj = [];
			var gridtable = $("#grid-table");
			if (tableObj != undefined) {
				if (tableObj.tableObjID != undefined && tableObj.tableObjID != "") {
					gridtable = $("#" + tableObj.tableObjID);
				}
			}

			for (var chktr = 0; chktr < gridtable.find("tbody")
				.find("input:checkbox").length; chktr++) {
				var thisChkTr = gridtable.find("tbody")
					.find("input:checkbox").eq(chktr).parents("tr");
				var thisIndex = gridtable.find("tbody").find("tr")
					.index(thisChkTr);
				selectedObj.push(datatable.fnGetData(thisChkTr[0]));
			}
			return selectedObj;
		}		
		return datatableObj;
	});



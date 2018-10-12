var weekControl = function(parentObj,defaultValue,objectId,dataId,dataName){
	currentlyWeek(parentObj,defaultValue,objectId,dataId,dataName);
}

function currentlyWeek(parentObj,defaultValue,objectId,dataId,dataName){
		var max;
		var min;
		var weekMax;
		var weekMin;
		if(defaultValue != null && defaultValue != ""){
			var temp = defaultValue.split("~");
			weekMin = new Date(temp[0].replace(/-/g,"/"));
			weekMax =  new Date(temp[1].replace(/-/g,"/"));
			min = weekMin.getTime();
			max = weekMax.getTime();
		}else{
			var d = new Date();
			var weekNum = d.getDay();
			max = d.getTime()+(7-weekNum)*24*3600*1000;
			min = d.getTime()-(weekNum-1)*24*3600*1000;
			weekMax = new Date(max); 
			weekMin = new Date(min);
		}
		
		var newDiv = document.createElement("div");
		newDiv.id="week";
		newDiv.name="week";
		var data1 = dateFormater(weekMin).replace(/-/g,'')+dateFormater(weekMax).replace(/-/g,'');
		var data2 = dateFormater(weekMin)+"~"+dateFormater(weekMax);
		newDiv.innerHTML = '<input type="hidden" id="max" value="'+max+'"/>'+
						   '<input type="hidden" id="min" value="'+min+'"/>'+
						   '<input type="hidden" id="dataId" name="'+dataId+'" value="'+data1+'"/>'+
						   '<input type="hidden" id="dataName" name="'+dataName+'" value="'+data2+'"/>'+
						   '<img alt="上一周" style="float:left" src="../../common/skin/1/image/page-prev.gif" onclick="previousWeek(\''+objectId+'\')"/>'+
						   '<div style="float:left" id="displayWeek">'+dateFormater(weekMin)+"～"+dateFormater(weekMax)+'</div>'+
						   '<img alt="下一周" style="float:left" src="../../common/skin/1/image/page-next.gif" onclick="nextWeek(\''+objectId+'\')"/>';
		parentObj.appendChild(newDiv);
		setLiName(objectId);
		
	}

	function dateFormater(date){
		var y = date.getYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+"-"+(m > 9 ? m : "0"+m)+"-"+(d > 9 ?d : "0"+d);
	}

	function previousWeek(objectId){
		var elem = document.getElementById("min").value;
		var max = parseInt(elem)-24*3600*1000
		var min = max - 6*24*3600*1000;
		setValue(max,min);
		setLiName(objectId);
		
	}
	function nextWeek(objectId){
		var elem = document.getElementById("max").value;
		var min = parseInt(elem)+24*3600*1000;
		var max = min + 6*24*3600*1000;
		setValue(max,min);
		setLiName(objectId);
		
	}

	function setValue(max,min,dataId,dataName){
		var weekMax = new Date(max); 
		var weekMin = new Date(min);
		var data1 = dateFormater(weekMin).replace(/-/g,'')+dateFormater(weekMax).replace(/-/g,'');
		var data2 = dateFormater(weekMin)+"~"+dateFormater(weekMax);
		document.getElementById("max").value=max;
		document.getElementById("min").value=min;
		document.getElementById("dataId").value=data1;
		document.getElementById("dataName").value=data2;
		document.getElementById("displayWeek").innerHTML=dateFormater(weekMin)+"～"+dateFormater(weekMax);
	}
	
	function setLiName(objectId){
		var ele = document.getElementById(objectId);
		if(ele.innerHTML.indexOf(":")==-1){
			ele.innerHTML = ele.innerHTML+":&nbsp;"+document.getElementById("displayWeek").innerHTML;
		}else{
			ele.innerHTML = ele.innerHTML.substring(0,ele.innerHTML.indexOf(":"))+":&nbsp;"+document.getElementById("displayWeek").innerHTML;
		}
	}
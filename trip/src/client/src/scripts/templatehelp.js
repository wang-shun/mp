template.config("escape", false);
template.helper('getFacility', function(facilitys,tp) {
	var fs = "" ;
	for(var i=0 ; i <facilitys.length ;i++ ){
		if(facilitys[i].value > 0 ){
			fs += '<span class="label label-success">'+facilitys[i].name+'</span>&nbsp;' ;	
		}
	}
	return fs ;
});

template.helper('getLevel', function(level,tp) {
	var fs = "" ;
	if(!level){
		return "" ;
	}
	for(var i=1 ; i <=level ;i++ ){
		fs += '<i class="fa fa-star"></i>' ;
	}
	return fs ;
});

template.helper('getRmTyImg', function(imgUrl,rmTypes) {
	
	if(imgUrl){
		return imgUrl
	}else{
		return "images/no-pic.jpg"  ;
	}
	
});

template.helper('getOtherInfo', function(rmType,tp) {
	
	var extendInfo = rmType.extends ;
	//console.log(extendInfo) ;
	if(extendInfo){
		var ret = "" ;
		for(var j=0 ; j < extendInfo.length ; j++ ){
			if(extendInfo[j].value >  0 ){
				ret += "<span class=\"label label-success\" >" +extendInfo[j].name+"<span>"
			}
		}
		return ret ;
	}else{
		return "无" ;
	}
	
});

template.helper('getTimeStr', function(time,tp) {
	
    var str = "" ;
    var format = "YYYY-MM-DD" ;
    
    if(tp == 1 ){
    	format = "YYYY-MM-DD HH:mm"
    }else if(tp==2){
    	format = "MM月DD日"
    }
    
    if(time){
    	str = moment(time).format(format) ;
    }
    return str ;
});

//一段时间的价格累加
template.helper('getRoomAmount', function(roomId,tp) {
   	//return roomStatus ;
	var prices = 0 ;
	for(var i=0 ; i<_rooms.length ; i++){
		var room  = _rooms[i] ;
		if(roomId == room.rmTypeID){
			prices += room.spotPrice ;
		}
	}
	return prices ;
});

//获取价格
template.helper('getSportPrice', function(roomId,d) {
   	//return roomStatus ;
	var format = "YYYY-MM-DD" ;
	
	for(var i=0 ; i<_rooms.length ; i++){
		var room  = _rooms[i] ;
		var str = moment(room.endOfDay).format(format) ;
		if(roomId == room.rmTypeID && d == str){
			return room.spotPrice;
		}
	}
	return "" ;
});
//获取价格
template.helper('getRackPrice', function(roomId,d) {
   	//return roomStatus ;
	var format = "YYYY-MM-DD" ;
	
	for(var i=0 ; i<_rooms.length ; i++){
		var room  = _rooms[i] ;
		var str = moment(room.endOfDay).format(format) ;
		if(roomId == room.rmTypeID && d == str){
			return room.rackPrice;
		}
	}
	return "" ;
});
//获取距离
template.helper('getDistance', function(distance,tp) {
	if(distance &&distance>0){
		var str = "" ;
		if(distance >= 1000){
			str += fomatFloat(distance/1000,1) + "km" ; 
		}else{
			str += distance+ "m" ;
		}
		return str ;
	}else{
		return "" ;
	}
});
		
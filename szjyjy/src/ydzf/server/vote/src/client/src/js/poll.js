var Poll = (function(){
	var URL = {
		getVoteInfo:"&method=mapps.vote.query.votebyid&format=json&v=1.0&appKey=",
		voting:"&method=mapps.vote.query.voteing&format=json&v=1.0&appKey="
	};
	var Template = {
		voteInfo:"../tpl/voteInfo.html"
	}

	function getVoteInfo(param,success,error){
		ajaxModel.getData(URL.getVoteInfo,param).then(success,error);
	}
	function renderVoteInfo(data,renderFun){
		/*$.get(Template.voteInfo,function(tpl){
			var html = Handlebars.compile(tpl)(data);
			renderFun(html)
		});*/
		var tpl = $("#voteInfoTemplate").html();
		var html = Handlebars.compile(tpl)(data);
		renderFun(html)
	}
	function voting(param,success,error){
		return ajaxModel.postData(URL.voting,param).then(success,error);
	}

	return {
		getVoteInfo:getVoteInfo,
		renderVoteInfo:renderVoteInfo,
		voting:voting
	}
})();

Handlebars.registerHelper("ifName", function (value, options) {
	return value=="1" ? "记名投票":"不记名投票"
});
Handlebars.registerHelper("timer", function (value, options) {
	return parseDate(value);
});

Handlebars.registerHelper("percent", function (value1, value2) {
	if(value1 == 0 ||value2 == 0) return "0%";
	var percent = value1/value2*100;
	if(percent>0 && percent<10){
		return "10%"
	}
	return percent.toFixed(2)+"%";
});

Handlebars.registerHelper("chooseCount", function (maxChoose, multiple, options) {
	if(multiple=="1"){
		if(maxChoose == 0 || maxChoose == ""){
			return "投票项数无限制";
		}else{
			return "最多选"+maxChoose+"项";
		}
	}else{
		return "最多选1项";
	}
	
});
Handlebars.registerHelper("nameColor", function (value) {
    return value ? value.toPinyin().substring(0,1).toColor() : "";
});
Handlebars.registerHelper("splitName", function (name) {
    var result = name;
    if(name){
        var len = name.length;
        result = name.substring(len-1);
    }
    return result
});

function parseDate(str){
	var list = str.split(/[\s]/);
	return list[0];
}

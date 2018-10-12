var Result = (function(){
	var URL = {
		getVoteInfo:"&method=mapps.vote.query.votebyid&format=json&v=1.0&appKey="
	};
	var Template = {
		voteInfo:"../tpl/result.html"
	}

	function getVoteInfo(param,success,error){
		ajaxModel.getData(URL.getVoteInfo,param).then(success,error);
	}
	function renderVoteInfo(data,renderFun){
		var tpl = $("#resultTemplate").html();
		var html = Handlebars.compile(tpl)(data);
		renderFun(html)
	}

	return {
		getVoteInfo:getVoteInfo,
		renderVoteInfo:renderVoteInfo
	}
})();

Handlebars.registerHelper("ifName", function (value, options) {
	return value ? "记名投票":"不记名投票"
});
Handlebars.registerHelper("timer", function (value, options) {
	return parseDate(value);
	//return value;
});
Handlebars.registerHelper("percent", function (value1, value2) {
	if(value1 == 0 ||value2 == 0) return "0%";
	var percent = value1/value2*100;
	if(percent>0 && percent<10){
		return "10%"
	}
	return percent.toFixed(2)+"%";
});

Handlebars.registerHelper("percents", function (value1, value2) {
	if(value1 == 0 ||value2 == 0) return "0";
	var percent = value1/value2*100;
	return percent.toFixed(2);
});


Handlebars.registerHelper("colorCSS", function (index) {
	return "color"+index%10;
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

//allvoteCount
/*Handlebars.registerHelper("allvoteCount", function (items, options) {
	alert(items.length)
});*/

function parseDate(str){
	var list = str.split(/[\s]/);
	return list[0];
}

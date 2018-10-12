var PollList = (function(){
	var URL = {
		involved:"&method=mapps.vote.query.involve&format=json&v=1.0&appKey=",//我参与的
		canInvolveVote:"&method=mapps.vote.query.caninvolve&format=json&v=1.0&appKey=",
		createdVote:"&method=mapps.vote.query.created&format=json&v=1.0&appKey=",
		deleteVote:"&method=mapps.vote.delete.votebyid&format=json&v=1.0&appKey="
	};
	var Template = {
		involved:"../tpl/involvedList.html",
		createdVote:"../tpl/createdVoteList.html"
	}
	/*var sessionId = getParameter("sessionId");
	alert(sessionId);*/
	

	/*var getInvolved = function(param,success,error){
		ajaxModel.getData(URL.involved,param).then(success,error);
	};*/
	function getInvolved(param,success,error){
		ajaxModel.getData(URL.canInvolveVote,param).then(success,error);
	};
	function getCreatedVote(param,success,error){
		ajaxModel.getData(URL.createdVote,param).then(success,error);
	};

	function renderInvolved(data,renderFun){
		/*$.get(Template.involved,function(tpl){
			var html = Handlebars.compile(tpl)(data);
			renderFun(html)
		});*/
		var tpl = $("#involvedList").html();
		var html = Handlebars.compile(tpl)(data);
		renderFun(html)
	}
	function renderCreatedVote(data,renderFun){
		/*$.get(Template.createdVote,function(tpl){
			var html = Handlebars.compile(tpl)(data);
			renderFun(html)
		});*/
		var tpl = $("#createdVoteList").html();
		var html = Handlebars.compile(tpl)(data);
		renderFun(html)
	}

	function deleteCard(param,success,error){
		ajaxModel.getData(URL.deleteVote,param).then(success,error);
	}

	
	return {
		getInvolved:getInvolved,
		getCreatedVote:getCreatedVote,
		renderInvolved:renderInvolved,
		renderCreatedVote:renderCreatedVote,
		deleteCard:deleteCard
	}
})();

Handlebars.registerHelper("equal", function (attr, expect, options) {
	return attr === expect ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper("pollState", function (state, options) {
	return state ? "已投":"未投";
});

Handlebars.registerHelper("pollStateCss", function (state, options) {
	return state ? "color-success":"";
});
Handlebars.registerHelper("percent", function (value1, value2) {
	if(value1 == 0 ||value2 == 0) return "0%";
	var percent = value1/value2*100;
	if(percent>0 && percent<10){
		return "10%"
	}
	return percent.toFixed(2)+"%";
});
Handlebars.registerHelper("timer", function (value, options) {
	return parseDate(value);
});
/*Handlebars.registerHelper("timerClass", function (expire, options) {
	var time = parseDate(expire),
		timeList = time.split("-"),
		today = new Date();
	var timeDate = new Date(timeList);
	if(today>timeDate){
		return "color-disable";
	}
});*/
//false未过期
Handlebars.registerHelper("timerClass", function (value, options) {
	return value ? "color-disable":"";
});
Handlebars.registerHelper("stateCss", function (involved, isExpired, options) {
	if(involved || isExpired){
		return "color-disable";
	}else{
		return "color-success";
	}
});
function parseDate(str){
	var list = str.split(/[\s]/);
	// console.log(list[0])
	return list[0];
}


 	
var PollAdd = (function(){
	var URL = {
		voteAdd:"&method=mapps.vote.save.vote&format=json&v=1.0&appKey=",
		getVoteNum:"&method=mapps.vote.query.usersize&format=json&v=1.0&appKey="
	};
	var Template = "../tpl/poll-list.html";

	function getInvolved(param,success,error){
		ajaxModel.getData(URL.involved,param).then(success,error);
	};
	function getVoteNum(param,success,error){
		ajaxModel.getData(URL.getVoteNum,param).then(success,error);
	};
	function submitForm(param,success,error){
		return ajaxModel.postData(URL.voteAdd,param).then(success,error);
	};


	function render(data,renderFun){
		/*$.get(Template,function(tpl){
			var html = Handlebars.compile(tpl)(data);
			renderFun(html)
		});*/
		var tpl = $("#treeTemplate").html();
		var html = Handlebars.compile(tpl)(data);
			renderFun(html)
	}
	function renderTree(data,fun){
		var tpl = $("#treeTemplate").html();
        var html = Handlebars.compile(tpl)({
                "nodes":data
            });
        fun(html);
	}
	return {
		getInvolved:getInvolved,
		render:render,
		submitForm:submitForm,
		renderTree:renderTree,
		getVoteNum:getVoteNum
	}
})();

Handlebars.registerHelper("equal", function (attr, expect, options) {
	return attr === expect ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper("pollState", function (state, options) {
	return state === 1 ? "未投":"已投"
});


 	
/***
  *  功能：将只读表格变成可编辑表格
  *  支持自定义事件进行编辑
  *  表格样式可以自己设置
  *  只负责tbody部分
  *  data结构样式如下:     {rows:3, data:[{name:"littlehow", age:18},{...}], column:["name", "age"]}
  *  @author littlehow
  *  @time 2016/7/29 星期五
  **/
(function() {
	var $ = function(node) {
		return typeof node == "string" ? document.getElementById(node) : node;
	}
	var $1 = function(node, parent){
		var nd = document.createElement(node);
		if(parent) parent.appendChild(nd);
		return nd;
	}
	/** 绑定事件流 */
	var bind = function(obj, eventName, funcionName){
		if(obj.addEventListener){
			obj.addEventListener(eventName, funcionName,false);
		 }else if(obj.attachEvent) {
			obj.attachEvent("on" + eventName, funcionName);
		 }else{
			obj["on" + eventName] = funcionName;
		 }
	};

	var fulltable = function(tbody, data){
		var pd = data.data;
		var column = data.column;
		for(var i=0, len=data.rows; i<len; i++){
			var tr = $1("tr", tbody);
			var cd = pd[i];
			for(var j=0,jlen=column.length; j<jlen; j++){
				var td = $1("td", tr);
				td.innerHTML = cd[column[j]];//innerText不兼容火狐，可以自己写innerText和textContent的兼容，这里就直接用innerHTML了
			}
		}
	}
	var littlehow_edit_table = function(tbody, ev){
		this.tbody = $(tbody);
		this.editColumn = [];
		this.event = ev ? ev : "click";//默认为单机事件
		this.init = function(data) {//data可以是undefined
			if(data && data.rows > 0) {
				//判断是否有数据
				fulltable(this.tbody, data);
			}
			var len = this.tbody.rows[0].cells.length;
			//调用可编辑
			if(data && data.edit){
				var json = {};//去重用，这里就不实现set去重了
				for(var i=0; i<data.edit.length; i++){
					var v = data.edit[i];
					if(v>=0 && v<len){
						var jv = json["json" + v];
						if(jv) continue;
						else json["json" + v] = "json"+v;
						this.editColumn.push(v);
					}
				}
			}else{
				for(var i=0; i<len; i++){
					this.editColumn.push(i);
				}
			}
			this.editLen = this.editColumn.length;
			this.edit();
		}
		this.edit = function(){
			var rows = this.tbody.rows;
			for(var i=0,len=rows.length; i<len; i++){
				var row = rows[i];
				for(var j=0, jlen=this.editLen; j<jlen; j++){
					bind( row.cells[this.editColumn[j]], this.event, this.click);
				}
			}
			/** 版本一实现
			var tds = this.tbody.getElementsByTagName("td");
			for(var i=0,len=tds.length; i<len; i++){
				bind(tds[i], this.event, this.click);
			}*/
		}
		this.click = function(){
			if(this.children.length > 0) return;
			var v = this.innerHTML;
			this.innerHTML = "";
			var input = $1("input", this);
			input.type = "text";
			input.value = v;
			input.focus();//光标聚集
			bind(input, "blur", blur);
		}
		var blur = function(){
			var v = this.value;
			this.parentNode.innerHTML = v;
		}
	}
	window.$$ = function(id, ev){
		return new littlehow_edit_table(id, ev);
	};
})()
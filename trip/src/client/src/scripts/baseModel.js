$(function(){

//	window.SERVICEURL = "http://192.168.5.44:820/api";
	window.SERVICEURL = "http://miap.cc:8470/api";
//	window.SERVICEURL = "http://192.168.6.86:18018/api";
//	window.SERVICEURL = "http://192.168.5.44:820/api";

	window.appKey = "mr" ;
	window.secret = "FHuma025" ;

	return window.model = {
        /**
         * ajax请求
         * @param  {[type]} url         [description]
         * @param  {[type]} data        [description]
         * @param  {[type]} type        [description]
         * @param  {[type]} contentType [description]
         * @return {[type]}             [description]
         */
        ajax: function(url, data, type) {
            var ops = {
                url: url,
                type: type || "GET",
                data: data || {}
            };
           // contentType && (ops.contentType = contentType);
            return $.ajax(ops);
        } ,
        /**
         * 调用rop接口
         * @param {Object} method api方法
         * @param {Object} version api版本号
         * @param {Object} data  请求参数
         * @param {Object} showerror  是否显示错误信息
         */
        api: function(method, version, data,showerror) {
        	
        	var token = "" ;
        	try{
        		if (NativeObj) {
					token = escape(NativeObj.getToken());
				}
        	}catch(e){
        		 console.log(e);
        		 //alert(e);
        	}
//		    alert(token)
            var url = SERVICEURL+"?format=json&method="+method+"&v="+version;
            url +="&appKey="+appKey ;
            url +="&secret="+secret ;
            url +="&sessionId="+token ;
            //console.log(data);
            //alert(url);
            return this.ajax(url,data,"POST").then(function(data){
            	//请求成功
            	var fg = data.code ;
            	if(fg == 1){
            		//console.log(data.data);
            		return data.data ;
            	}else{
            		//console.log(data);
            		layer.closeAll();
            		if(showerror){
            			layer.msg("请求错误："+data.message);
            		}
            	}
            },function(){
            	//请求失败
            	layer.closeAll();
            	if(showerror){
            		layer.msg("发送请求发生错误，请检查网络环境");
            	}
            });
        }
    }
});
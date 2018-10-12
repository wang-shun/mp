define(["spin", "underscore","jquery"], 
	function(Spinner, _, $){
		var opts = {
			  lines: 10, // The number of lines to draw
			  length: 12, // The length of each line
			  width: 3, // The line thickness
			  radius: 8, // The radius of the inner circle
			  color: '#1d80dd', // #rbg or #rrggbb
			  speed: 1, // Rounds per second
			  trail: 100, // Afterglow percentage
			  shadow: true // Whether to render a shadow
		};	
		var _progressBarInstances = {};
		var ProgressBar = {};
		/**
		* view : 缓冲所在的view. 如果只传view做为参数，缓冲块加在当前view的dom节点上。
		* containerSID : 缓冲所在的dom节点的SID，如果传view和containerSID做为参数，缓冲块加在当前view的有containerSID做为Class的dom节点上。
		* 不传参数默认缓冲块加在body上。
		*/
		ProgressBar.start = function(view, containerSID){
			var spinnerContinerId;
			var targetDom;
			var height,width,top,left;
			if (arguments.length == 0) {
				spinnerContinerId = 'spinner_full_page';
				targetDom = $('body');
				height = '100%';
				width='100%';
				top='0px';
				left = '0px';				
			}else if(arguments.length == 1){
				spinnerContinerId = view.cid;
				targetDom = view.$el;
				var position = targetDom.position();
				height = targetDom.children().height()+'px';
				width= targetDom.children().width()+'px';
				top= position.top+'px';
				left = position.left+'px';			
			}else{
				spinnerContinerId = view.cid+containerSID;
				targetDom = view.$el.find('.'+containerSID);
				var position = targetDom.position();
				height = targetDom.height()+'px';
				width= targetDom.width()+'px';
				top= position.top+'px';
				left = position.left+'px';				
			}

			var spinnerContiner = $('<div id="'+spinnerContinerId+'" style="width: '+width+';height: '+height+';position: absolute;top: '+top+';left: '+left+';z-index: 1000"></div>');
			targetDom.append(spinnerContiner);
			var target = document.getElementById(spinnerContinerId);
			_progressBarInstances[spinnerContinerId] = new Spinner(opts).spin(target);	
			return _progressBarInstances[spinnerContinerId];
		};
		ProgressBar.stop = function(view, containerSID){
			var spinnerContinerId;
			var targetDom;
			if (arguments.length == 0) {
				spinnerContinerId = 'spinner_full_page';
				targetDom = $('body');
			}else if(arguments.length == 1){
				spinnerContinerId = view.cid;
				targetDom = view.$el;
			}else{
				spinnerContinerId = view.cid+containerSID;
				targetDom = view.$el.find('.'+containerSID);
			}			
			_progressBarInstances[spinnerContinerId].stop();
			targetDom.find('#'+spinnerContinerId).remove();
		};
		ProgressBar.stopAll = function(){
			var spinnerContinerId;
			$.each(_progressBarInstances, function(id, bar) {
				_progressBarInstances[id].stop();
				$('#'+id).remove();
			});		
		};	
		return ProgressBar;
});
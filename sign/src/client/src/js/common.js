var SimpleTab = function(options) {
	var defaults = {
		container: "#carouselPage",
		tabbar: "tabbar",
		tab: ".tab"
	}
	this.options = $.extend({}, defaults, options);
	this._init();
	this._bindEvents();

}
SimpleTab.prototype = {
	_init: function() {
		var options = this.options;
		var tabbar = document.getElementById(options.tabbar);
		var tab = tabbar.querySelectorAll(options.tab);
		var self = this;
		self.slider = new Slider(options.container, {
			"onSlideChangeEnd": function(e) {
				tabActive(e.index);
				self.compatibleIosScroll(e);
			}
		});
		//选中tab
		function tabActive(index) {
			for (var i = 0, t; t = tab[i++];) {
				t.classList.remove("active");
			}
			tab[index].classList.add("active");
		}
		//绑定tab点击事件
		function tabBindClick() {
			for (var i = 0, t; t = tab[i++];) {
				(function(i) {
					t.addEventListener("click", function() {
						self.slider.slideTo(i - 1);
						tabActive(i - 1);
					}, false);
				})(i);
			}
		}
		
		tabBindClick();
	},
	compatibleIosScroll: function(e){
        var container=e.target;
        var clientHeight=container.clientHeight; 
        var scrollTop=container.scrollTop; 
        var scrollHeight=container.scrollHeight;
        if(scrollTop==0){
            container.scrollTop=1;
        }
        // console.log(clientHeight+" - "+scrollTop+" - "+scrollHeight);
        if (clientHeight+scrollTop>=scrollHeight){
            container.scrollTop=container.scrollTop-1;
            // console.log(carouselPage)
        }
    },
	_bindEvents: function(){
		var self = this;
		var slides=document.querySelectorAll(".slider-slide");
	    [].slice.call(slides).forEach(function(n,i,a){
	        n.addEventListener("scroll",function(e){
	            self.compatibleIosScroll(e);
	        },false);
	    });
	}

}

$.fn.extend({
	getUserIcon:function(config){
		var options = {
			url:"&method=mapps.sign.getUserIcon&format=json&v=1.0&appKey="
		}
		$.extend(options, config);
		var userIds = [];
		var method = {
			getUserIconFromServer: function(userIds,success){
				ajaxModel.getData(options.url,{
					"userIds":userIds.join(",")
				}).then(function(res){
					success && success(res.data);
				})
			}
		}
		this.each(function(){
			var $this = $(this);
			userIds.push($this.data("iconName"));
			
		});
		method.getUserIconFromServer(userIds,function(data){
			for(var name in data){
				if(data.hasOwnProperty(name) && data[name] !== ""){
					var icon = data[name];
					// console.log($('[data-icon-name=\''+name+'\']'));
					$('[data-icon-name=\''+name+'\']').css({
						"background-image":"url("+window.arkURL+"/clientdownload?"+icon+")"
					}).html("");
					
				}
			}
		});

		return this;
	},
	previewSlider:function(config){
		if(!config.previewBlock) return;
		var defaultOptions = {
			"previewBlock":"",
			"selector":"",
			"imageType":"background-image",
			"containerId":"previewSlider"
		}
		var options = $.extend({},defaultOptions,config);
		var curIndex = 0;//当前点击位置
		var methods = {
			createPreviewImgHTML: function(){
                var url = $(this).css(options.imageType);
                var _html = document.createElement("div");
                var s = $(_html).css({
                    "width": "100%",
                    // "height": "100%",
                    "position":"absolute",
                    "top":"0",
                    "bottom":"0",
                    "background":"#fff",
                    "background-size": "contain",
                    "background-repeat": "no-repeat",
                    "background-position": "center",
                    "background-color": "#000",
                    "transition":"all 0.3s ease",
                    "-webkit-transition":"all 0.3s ease",
                    "background-image":url
                });
                return s[0].outerHTML;
            },
			createContainerHTML:function(html){
				return '<section><div class="slider-container" id="'+options.containerId+'" style="height:100%;">'+
        			'<div class="slider-wrapper">'+ html +
        			'</div>'+
        			'<div class="slider-pagination" style="text-align:center"></div>'+
        			'</div></section>'
			},
			bindEvents:function(obj,slider){
				var self = this;
				var $this = $(obj);
				$this.on("click",function(e){
					$this.remove();
				});
			}
		}
		this.each(function(){//预览图片块
			$this = $(this);
			var $block = $this.parents(options.previewBlock);
			var imgs = options.selector ? $block.find(options.selector) : $block.children();
			curIndex = options.selector ? imgs.index($this) : $this.index();
			var wrapperHTML = "";
			imgs.each(function(){
				var _previewImgHtml = methods.createPreviewImgHTML.call(this);
				wrapperHTML+='<div class="slider-slide">'+_previewImgHtml+'</div>';
				// console.log($(this).css(options.imageType));
			})
			// console.log(wrapperHTML)
			var _html = methods.createContainerHTML(wrapperHTML);
			
			// methods.bindEvents.call($(_html).appendTo("body").addClass("active"));
			var obj = $(_html).appendTo("body").addClass("active");
			
			var s = new Slider("#"+options.containerId,{
				"pagination":".slider-pagination"
		    });
		    s.slideTo(curIndex,true);
		    methods.bindEvents(obj,s);
		    setTimeout(function(){
                s.container.style.height = "100%";
                s.updateContainerSize();
            },1000);
		})
	}	
})
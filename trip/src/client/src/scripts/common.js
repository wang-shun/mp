function getUrlParam(name) {
   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
   var url = decodeURI(window.location.search);
   var r = url.substr(1).match(reg); //匹配目标参数
   if (r != null) return r[2]; return null; //返回参数值
 }

// 设定 url 中的 QueryString 值
function setUrlParam(url, param, v)
{
	var re = new RegExp("(///?|&)" + param + "=([^&]+)(&|$)", "i");
	var m = url.match(re);
	if (m)
	{
		return (url.replace(re, function($0, $1, $2) { return ($0.replace($2, v)); } ));
	}
	else
	{
	  if (url.indexOf('?') == -1)
  		return (url + '?' + param + '=' + encodeURI(v));
	  else
		return (url + '&' + param + '=' + encodeURI(v));
	}
}
function ex(fg){
	//Mplus.closeBackListener();
	if(fg){
		
//		layer.msg("离开酒店预订？", {
//			  time: 0 //不自动关闭
//			  ,btn: ['确定', '取消']
//			  ,yes: function(index){
//					mplus.closeWindow();
//				}
//		});
		return ;
		layer.confirm("离开酒店预订？",function(){
			mplus.closeWindow();	
		})
		
	}else{
		mplus.closeWindow();	
	}
}

var Mplus = {
		//3.7.3 获取当前地理位置接口
		getLocation : function(success,fail){
			// alert("getLocation");
			mplus.getLocation({
			    success: function (res) {
			    	success && success(res);
			        // var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
			        // var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
			        // var speed = res.speed; // 速度，以米/每秒计
			        // var accuracy = res.accuracy; // 位置精度
				},
				fail: function (res) {
				    fail && fail(res);
				}
			});
		},
		previewImage : function(current,urls){
	        mplus.previewImage({
	            current: current, // 当前显示图片的http链接
	            urls: urls // 需要预览的图片http链接列表
	        });
	    },
	    config:function(accessid){
	    	// setTimeout(function(){
	            mplus.config({
	                accessid : accessid//访问接入码，图片上传和下载接口调用前必须设置。
	            });
	        // },1000)
	    },
	    chooseImage: function(sourceType,beforefun,success,fail){
	    	var self = this;
	        // alert(1)
	    	/*mplus.config({
			    accessid : '4a4400ab-d8ac-401d-9350-4491957b31f4' //访问接入码，图片上传和下载接口调用前必须设置。
			});*/
			// alert("调用chooseImage");
		    mplus.chooseImage({
			    // 最多只能选择9张图片
			    sourceType: sourceType || ['album', 'camera'],
	            showOption:"0",
			    success: function (res) {
			    	// alert("chooseImage成功"+JSON.stringify(res));
	                var localIds = res.localIds;  
				    success && self.uploadImage(localIds[0],beforefun,success,fail);
				},
				fail: function (res) {
				    // fail && fail(res);
				    // alert("chooseImage失败")
				    // new Toast(res.errMsg).show();
				}
			});	
	    },
	    

	    uploadImage: function(localId,beforefun,success,fail){
	    	if(!localId) throw "input localId";
	        beforefun && beforefun();
	    	var self = this;
	    	// alert("调用uploadImage，localId="+localId);
	    	mplus.uploadImage({
				localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
				isShowProgressTips: 0, //0或1， 默认为1，显示进度提示（显示上传中）
	            maxLength: "80",
	            pwidth:"800",
				success: function (res) {
					// alert("uploadImage成功:"+JSON.stringify(res))
				    success && self.downloadImage(res.serverId,success,fail); // 返回图片的服务器端ID
				},
				fail: function (res) {
				    fail && fail(res);
				    // alert("uploadImage失败")
				    // new Toast(res.errMsg).show();
	                new Toast("上传失败！").show();
				}
			});
	    },
	    uploadImages: function(localIds,success,fail){
	        // alert("调用uploadImage，localId="+localIds);
	        if(localIds.length == 0) return;
	        var self = this;
	        // alert(localIds[0]);
	        mplus.uploadImage({
	            localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
	            isShowProgressTips: 0, //0或1， 默认为1，显示进度提示（显示上传中）
	            maxLength: "80",//填具体数值，单位为KB
	            pwidth:"300",
	            success: function (res) {
	                // alert("uploadImage成功:"+JSON.stringify(res))
	                localIds.shift();   
	                success && self.downloadImage(res.serverId,function(url){
	                    // alert(res.serverId)
	                    // alert(url)
	                    success(url,localIds.length);
	                    self.uploadImages(localIds,success,fail);
	                },fail); // 返回图片的服务器端ID
	            },
	            fail: function (res) {
	                fail && fail(res);
	                new Toast("上传失败！").show();
	            }
	        });
	    },
	    downloadImage: function(serverId,success,fail){
	        // alert("调用downloadImage,serverId="+serverId);
	    	mplus.downloadImage({
			    serverId: serverId, // 需要下载的图片的服务器端ID，由uploadImage接口获得
			    success: function (res) {
	                // alert("downloadImage成功:"+JSON.stringify(res));
			        var downloadUrl = res.downloadUrl; // 返回图片下载地址
			        success && success(downloadUrl);
				},
				fail: function (res) {
	                // alert("downloadImage失败")
	                fail && fail(res);
			        // new Toast(res.errMsg).show();
				}
			});
	    },
	    chooseImageGetBase64: function(sourceType,success,fail){
	        var self = this;
	        mplus.chooseImage({
	            // 最多只能选择9张图片
	            sourceType: sourceType || ['album', 'camera'],
	            showOption:"0",
	            success: function (res) {
	                // alert("chooseImage成功"+JSON.stringify(res));
	                var localIds = res.localIds;  
	                for(var i=0,len=localIds.length;i<len;i++){
	                    var localId = localIds[i];
	                    success && self.getBase64ImageFromId(localId,success,fail);
	                }
	            },
	            fail: function (res) {
	                // fail && fail(res);
	                // alert("chooseImage失败")
	                // new Toast(res.errMsg).show();
	            }
	        }); 
	    },
	    getBase64ImageFromId : function(localId,success,fail){
	        mplus.getBase64ImageFromId({
	            localId: localId, // 图片的本地ID，由chooseImage接口获得
	            maxLength:'80',// 整数值，单位为KB，可不填。如果设置此值，上传的图片大小<=maxLength,如maxLength:'300'
	            pwidth:'300',//压缩之后的图片宽度，可不填。如果设置此值，必须同时设置maxLength。
	            success: function (res) {
	                var base64Image = res.base64Image; // 返回图片的base64编码数据
	                var imgType= res.base64ImageType;//图片类型，返回类型：”png”,”jpeg”
	                var src = "data:image/"+imgType+";base64,"+base64Image;
	                success && success(src,localId);
	            },
	            fail: function (res) {
	                fail && fail(res);
	                new Toast(res.errMsg).show();
	            }
	        });
	    },
	    //3.7.3 获取当前地理位置接口
	    getLocation: function(success,fail){
	    	// alert("getLocation");
	    	// setTimeout(function(){
	    		mplus.getLocation({
		            success: function (res) {
		          //   	var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				        // var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
				        // var speed = res.speed; // 速度，以米/每秒计
				        // var address = res.address; //地址详情说明
		                success && success(res);
		            },
		            fail: function (res) {
		                // new Toast(res.errMsg).show();
	                    fail&&fail(res)
		            }
		        });
	    	// },2000)
	    	
	    },
	    //3.7.1 选择当前地理位置接口
	    chooseLocation: function(success,fail){
	    	mplus.chooseLocation({
	            range:"1500",
	            success: function (res) {
	                // alert(JSON.stringify(res));
	          //   	var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
			        // var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
			        // var name = res.name; //位置名
			        // var address = res.address; //地址详情说明
	                success && success(res);
	            },
	            fail: function (res) {
	                // new Toast(res.errMsg).show();
	            }
	        });
	    },
	    //获取当前用户部门id
	    getUserGroupId: function(success,fail){
	    	mplus.getUserGroupId({
	            success: function (res) {
	            	if(!res.groupId){
	            		new Toast("获取部门异常").show();
	            	}
	                success && success(res.groupId);
	            },
	            fail: function (res) {
	                new Toast(res.errMsg).show();
	                fail && fail();
	            }
	        });
	    },
	    //3.3.4 获取部门直属成员和子部门
	    getOrgGroupsAndMembers: function(groupId,success,fail){
	    	mplus.getOrgGroupsAndMembers({
	    		groupId:groupId,
	            success: function (res) {
	                setTimeout(function(){
	                    // alert(JSON.stringify(res));
	                    var orgGroupArr = res.orgGroups; // 返回子部门数组
	                    var orgMemberArr = res.orgMembers; // 返回部门成员数组
	                    success && success(orgGroupArr,orgMemberArr);
	                },0)
	            },
	            fail: function (res) {
	                new Toast(res.errMsg).show();
	                fail && fail();
	            }
	        });
	    },
	    /**
	     * 隐藏顶部
	     * @param  {[boolean]} flage [true:隐藏]
	     * @return {[type]}       [description]
	     */
	    hideHeader: function(flage){
	    	var type = (flage===true ? 1 : 0);
	    	mplus.hideHeader({
	    		isHide:type
	    	})
	    },
	    /**
	     * 3.3.5 根据关键字搜索成员
	     */
	    searchOrgMembers: function(param,success,fail){
	        // alert(condition)
	        var data = {
	            condition:"",
	            limit:"20",
	            from:"0"//页数
	        }
	        if(typeof param === "object"){
	            $.extend(data,param);
	        }else if(typeof param === "string"){
	            data.condition = param;
	        }
	        // alert(JSON.stringify(data))
	        mplus.searchOrgMembers({
	            condition:data.condition,
	            limit:data.limit,
	            from:data.from,
	            success: function (res) {
	                // alert("success"+JSON.stringify(res));
	                // setTimeout(function(){
	                    var orgMemberArr = res.orgMembers || []; // 返回部门成员数组
	                    success && success(orgMemberArr);
	                // },0)
	                
	            },
	            fail: function (res) {
	                new Toast(res.errMsg).show();
	            }
	        });
	    },
	    //3.5.10 获取当前用户姓名
	    getUserName : function(success,fail){
	        /*mplus.getUserName({
	         success: function (res) {
	         alert('res'+res);
	         var userName = res.userName;
	         success && success(userName);
	         },
	         fail: function (res) {
	         alert('fail');
	         new Toast(res.errMsg).show();
	         }
	         });*/
	        return NativeObj.getUserName();
	    },
	    //3.5.7 获取群组
	    getUserGroups : function(success,fail){
	        mplus.getUserGroups({
	            success: function (res) {
	                var userGroupArr = res.userGroups;
	                //for(var i=0;i< userGroupArr.length;i++)
	                //{
	                //    var id = userGroupArr[i].id;//返回群组id
	                //    var name= userGroupArr[i].name;//返回群组名称
	                //}
	                success & success(userGroupArr);
	            },
	            fail: function (res) {
	                new Toast(res.errMsg).show();
	                fail && fail();
	            }
	        });
	    },

	    //3.5.8 获取群组成员
	    getUserGroupMembers : function(groupId,success,fail){
	        mplus.getUserGroupMembers({
	            groupId:groupId,//指定的群组id
	            success: function (res) {
	                setTimeout(function(){
	                    var userMemberArr = res.userGroupMembers;
	                    //for(var i=0;i< userMemberArr.length;i++)
	                    //{
	                    //    var id = userMemberArr[i].id;//返回群组成员id
	                    //    var name= userMemberArr[i].name;//返回群组成员名称
	                    //    alert('id:'+id);
	                    //    alert('name:'+name);
	                    //}
	                    success & success(userMemberArr);
	                },1000)
	            },
	            fail: function (res) {
	                new Toast(res.errMsg).show();
	                fail && fail();
	            }
	        });
	    },
	    //3.5.10获取当前用户所在部门全路径id
	    getUserGroupFullId : function(success,fail){
	        mplus.getUserGroupFullId({
	            success: function (res) {
	                var groupFullId = res.groupFullId;//groupFullId以@分隔，从上到下，最底层为直属部门id，如<groupfullid>
	                //b1f00858-0b99-1212-910d-c75ssa7eb16f@ec5ea37e-e5fb-4cfc-1212-00d24d750d2b@19494f45-46af-1212-a329-39b886ggc6a3@10aaa8c3-2437-1122-8e01-baffce320927
	                //</groupfullid>
	                success && success(groupFullId);
	            },
	            fail: function (res) {
	                alert(res.errMsg);
	            }
	        });
	    },
	    //是否监听返回键（0否1是）
	    _setBackListener: function(flag){
	        var ua = navigator.userAgent.toLowerCase(); 
	        if(/iphone|ipad|ipod/.test(ua)) {
	            //alert("iphone");      
	        }else if (/android/.test(ua)) {
	            mplus.setBackListener({
	                active: flag
	            });
	        }
	    },
	    openBackListener: function(){
	        this._setBackListener("1");
	    },
	    closeBackListener: function(){
	        this._setBackListener("0");
	    }

	}

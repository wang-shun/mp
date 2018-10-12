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
    	setTimeout(function(){
            mplus.config({
                accessid : accessid//访问接入码，图片上传和下载接口调用前必须设置。
            });
        },1000)
    },
    chooseImage: function(sourceType,max,success,fail){
    	var self = this;
//		alert("调用chooseImage");
	    mplus.chooseImage({
		    // 最多只能选择9张图片
            max:max, 
		    sourceType: sourceType || ['album', 'camera'],
		    success: function (res) {
//		    	alert("chooseImage成功"+JSON.stringify(res));
			    success && self.uploadImage(res.localIds[0],success);
			},
			fail: function (res) {
//				alert(res.errMsg);
			    //new Toast(res.errMsg).show();
			}
		});	
    },
    

    uploadImage: function(localId,success,fail){
    	if(!localId) throw "input localId";
    	var self = this;
//    	alert("调用uploadImage，localId="+localId);
    	mplus.uploadImage({
			localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
			isShowProgressTips: 1, //0或1， 默认为1，显示进度提示（显示上传中）
            maxLength: "80",//填具体数值，单位为KB
            pwidth:"800",
			success: function (res) {
//				alert("uploadImage成功:"+JSON.stringify(res))
			    success && self.downloadImage(res.serverId,success); // 返回图片的服务器端ID
			},
			fail: function (res) {
			    // fail && fail(res);
//			    alert("uploadImage失败"+res.errMsg);
			    new Toast(res.errMsg).show();
			}
		});
    },
    downloadImage: function(serverId,success,fail){
        // alert("调用downloadImage,serverId="+serverId);
    	mplus.downloadImage({
		    serverId: serverId, // 需要下载的图片的服务器端ID，由uploadImage接口获得
		    success: function (res) {
//                alert("downloadImage成功:"+JSON.stringify(res));
		        var downloadUrl = res.downloadUrl; // 返回图片下载地址
		        success && success(downloadUrl);
			},
			fail: function (res) {
                // alert("downloadImage失败")
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
	                new Toast(res.errMsg).show();
	            }
	        });
    	// },2000)
    	
    },
    //3.7.1 选择当前地理位置接口
    chooseLocation: function(success,fail){
    	mplus.chooseLocation({
            range:"1500",
            success: function (res) {
          //   	var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
		        // var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
		        // var name = res.name; //位置名
		        // var address = res.address; //地址详情说明
                success && success(res);
            },
            fail: function (res) {
                new Toast(res.errMsg).show();
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
            }
        });
    },
    getOrgName: function(success,fail){
        mplus.getOrgName({
            success: function (res) {
                var orgName = res.orgName;
                success && success(orgName);
            },
            fail: function (res) {
                new Toast(res.errMsg).show();
            }
        });
    },
    getUserName: function(success,fail){
        mplus.getUserName({
            success: function (res) {
                var userName = res.userName;
                success && success(userName);
            },
            fail: function (res) {
                new Toast(res.errMsg).show();
            }
        });
    },
    getOrgGroups: function(groupId,success,fail){
        mplus.getOrgGroups({
            groupId:groupId,
            success: function (res) {
                // alert(JSON.stringify(res));
                setTimeout(function(){
                    var orgGroupArr = res.orgGroups; // 返回子部门数组
                    success && success(orgGroupArr);
                },0)
            },
            fail: function (res) {
                new Toast(res.errMsg).show();
            }
        });
    },
    //3.3.4 获取部门直属成员和子部门
    getOrgGroupsAndMembers: function(groupId,success,fail){
    	mplus.getOrgGroupsAndMembers({
    		groupId:groupId,
            success: function (res) {
                // alert(JSON.stringify(res));
                setTimeout(function(){
                    var orgGroupArr = res.orgGroups; // 返回子部门数组
                    var orgMemberArr = res.orgMembers; // 返回部门成员数组
                    success && success(orgGroupArr,orgMemberArr);
                },0)
            },
            fail: function (res) {
                new Toast(res.errMsg).show();
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
                setTimeout(function(){
                    var orgMemberArr = res.orgMembers || []; // 返回部门成员数组
                    success && success(orgMemberArr);
                },0)
                
            },
            fail: function (res) {
                new Toast(res.errMsg).show();
            }
        });
    }

}


function previewImage(container,clickDom,isfromMplus){
    var $container = $(container),
        $clickDom = $(clickDom);
    var urls = [];
    var current = "";
    var curURL = $clickDom.data("href");
    if(!curURL) return;
    $container.find("[data-href]").each(function(){
        (function(s){
            var url = "";
            if(isfromMplus){
                url = $(s).data("href");
                current = curURL;
            }else{
                url = $(s).data("href");
                current = curURL;
            }
            urls.push(url);
        })(this);  
    })
    console.log(current);
    console.log(urls);
    Mplus.previewImage(current,urls);
}
/**
 * [lazyloadImg description]
 * @param  {[number]} photoType [0:大图 1:小图,默认大图]
 * @return {[type]}           [description]
 */
function lazyloadImg(photoType){
    var url = window.photoDownload;
    if(photoType){
        url = window.photoDownload_m;
    }
    $("[data-bg],[data-img]").each(function(){
        (function(s){
            var img = new Image();
            var data = $(s).data();
            if(data["img"]){
                img.src = $(s).data('img');
                // img.src = $(s).data('img');
                img.onload=function(){
                    $(s).attr('src', img.src);
                }
            }else if(data["bg"]){
                // img.src = $(s).data('bg');
                img.src = $(s).data('bg');
                img.onload=function(){
                    $(s).css("background-image","url(" + img.src + ")");
                }
            }
        })(this);
    });
}


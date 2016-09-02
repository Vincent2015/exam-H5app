	
	//全局变量用于存放用户信息
	var memInfo;
	var printAlert = false;
//	var basePath = "http://172.20.19.200:10555/yycollege/";
	var basePath = "http://yycollege.upesn.com/yycollege/";
	window.onload = function(){
//		hiddenmenu();
		//获取code
		var code = getRequest(location.search)["code"];
		//获取用户信息
		var URL =basePath+ "getUserInfo?code="+code;
		$.ajax({
			type : 'GET',
			async : true,
			url : URL,
			cache : false,
			contentType : "application/json",
			success : function(backData) {
				debugInfo(JSON.stringify(backData));
				if(backData.flag == "0"){
					memInfo = backData.data;
					getSignInfo();
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
		
		
	};
	
	
//	function hiddenmenu(){
//		var dataHide = {
//  			function: 'hiddenMenu'
//  		}
//		YonYouJSBridge.send(JSON.stringify(dataHide), function(res) {
//                  if(typeof res === 'string') {
//                      res = JSON.parse(res);
//                  }
//                  if(res.error_code == 0) {
//                      alert(res.error_description)
//                  } else {
//                      alert(res.data.codeString);
//                      alert(res.data.code);
//                  }
//  			});
//	}
	//查询用户签到信息
	function getSignInfo(){
		var URL1 = basePath + "register/find?memberId="+memInfo.memberId;
		debugInfo('1111111'+URL1);
		$.ajax({
			type : 'GET',
			async : false,
			url : URL1,
			cache : false,
			contentType : "application/json",
			success : function(backData) {
				debugInfo(JSON.stringify(backData));
				if(backData.flag == "0"){					
						memInfo['rank'] = backData.data.rank;
						//显示排名.
				    	document.getElementsByClassName('home')[0].style.display='none';
				    	document.getElementsByClassName('end')[0].style.display='block';
				    	$(".tip .sign_name").html(memInfo.name);
				    	$(".info .rank").html(memInfo.rank);				      
				}else if (backData.flag == "1"){
						memInfo['rank'] = null;
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
	}
	
	
	
	function getRequest(url) { 
		var theRequest = new Object(); 
		if (url.indexOf("?") != -1) { 
			var str = url.substr(1); 
			strs = str.split("&"); 
			for(var i = 0; i < strs.length; i ++) { 
			theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); 
			}; 
		} 
		return theRequest; 
	} 

	function route() {
    	// body...
    	var hash = location.hash;
    	console.log(hash);
    }
    window.onpopstate=function (state) {
    	// body...
    	console.log(state);
    	goback();

    };
    function goback (argument) {
    	// body...
    	document.getElementsByClassName('end')[0].style.display='none';
    	document.getElementsByClassName('home')[0].style.display='block';
    }
    function  goend(e) {
    	//扫码签到
       debugInfo('222'+memInfo.rank);
    	if(memInfo.rank != null){
    		document.getElementsByClassName('home')[0].style.display='none';
		  	document.getElementsByClassName('end')[0].style.display='block';
		   	$(".tip .sign_name").html(memInfo.name);
		   	$(".info .rank").html(memInfo.rank);
    		e.preventDefault();
    		e.stopPropagation();
    	}else{
    		debugInfo('shitshitshitshitshitshit');
    		if (window.yyesn && window.yyesn.scanQRCode) {
    		debugInfo(JSON.stringify(window.yyesn));
//  		e.preventDefault();
//  		e.stopPropagation();
    		debugInfo(JSON.stringify(window.yyesn.scanQRCode));
            window.yyesn.scanQRCode(function(data) {
            	debugInfo(8888);
            }, function(data, res) {
            	debugInfo("data:"+data);
            	debugInfo("res:"+res);
            	var data = typeof data === 'string' ? JSON.parse(data) : data;
            	debugInfo("mem信息:"+JSON.stringify(memInfo));
            	var qrCodeUrl = data.parameters.qrString;
            	debugInfo(qrCodeUrl);
             	saveQRCode(qrCodeUrl);
            });
    	  }else{
    	  	debugInfo(1);
    	  }
    	}
    	debugInfo('shit');
    }
    
    /**
     * 保存扫描二维码签到结果
     * @param {Object} param
     */
	function saveQRCode(param){
		debugInfo('svae');
		debugInfo(JSON.stringify(memInfo));
		var url = basePath + "register/save";
		var memberId = memInfo.memberId;
		var name = memInfo.name;
		var deptName = "";
		if(!memInfo.deptName){
			deptName = "用友";
		}else{
			deptName = memInfo.deptName;
		}
		var content = param;
//		var obj = {"memberId":memberId,"name":"aa","deptName":"bb","content":content};
		var obj = {"memberId":memberId,"name":name,"deptName":deptName,"content":content};
		debugInfo(JSON.stringify(obj));

       $.ajax({
			type:'POST',
			async:true,
			url:url,
			cache:false,
			dataType: 'json',
			contentType:"application/json",
			data:JSON.stringify(obj),//将对象序列化成string便于requestBody接受
			success:function(data){
				debugInfo("返回的数据"+JSON.stringify(data));
				//显示排名.
		    	document.getElementsByClassName('home')[0].style.display='none';
		    	document.getElementsByClassName('end')[0].style.display='block';
		    	$(".tip .sign_name").html(data.data.name);
		    	$(".info .rank").html(data.data.rank);

			},
			error:function(err){
				debugInfo(JSON.stringify(err));
			}
		});
		
	}
	
	/**
	 * 
	 * @param {Object} msg
	 */
	function debugInfo(msg){
		if(printAlert){
			alert(msg);
		}else{
			
		}
	}

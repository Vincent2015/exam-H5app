//定义全局变量
var memInfo;
//var basePath = "http://172.20.19.200:10555/yycollege/";
var basePath = "http://yycollege.upesn.com/yycollege/";
var printAlert = false;
var totalPapreCounts = 10;
var timerId;/*答题计时器ID*/
var userdTime;/*答题过程中所花费的时间*/
var countTimer;/*3 2 1倒计时*/
var scoreTimer;/*答题计时器ID*/
var thisLeftTime_best1;
var thisRightTime_best1; 
var thisCurrent;
function tiktok(){
//	var timeLeft = 30*100,
//	timer = setInterval(function(){
//		tStr = (((--timeLeft)/100).toFixed(2)+'').split('.');
//		timeLeft==0&&clearInterval(timer);
//		$('.used_time_info input').eq(1).val(tStr[1])&&$('.used_time_info input').eq(0).val(tStr[0])
//	},10);
	var timeUsed = 0,
	scoreTimer = setInterval(function(){
		tStr = (((++timeUsed)/100).toFixed(2)+'').split('.');
		timerId = scoreTimer;
		userdTime = timeUsed;
		$('.used_time_info input').eq(1).val(tStr[1])&&$('.used_time_info input').eq(0).val(tStr[0]);
	},10);
	
}

/*获取的所有题目*/
var paperArr = [];
/*当前第几题*/
var answerCount = 1;

$(function(){
	/*快速点击事件*/
	FastClick.attach(document.body);
	setHeader(true);
	//获取用户信息  查看用户成绩并初始化答题须知/竞赛说明页面
	getUserInfo();
	
	//初始化home page 底部div
	var screenHeight  = document.body.clientHeight;
//	var homePicHeight = $("body").width()*107/75;
	var homePicHeight = $(".home").height();
	var homeOptHeight = px2rem(screenHeight-homePicHeight);
	$(".home_opt").css("height",homeOptHeight+'rem');
	
	
	$(".home_page").addClass("cur_page");
//	var pageDivArr = $("#main").children();
	$('.pre').css('display',"none");
	$(".pre").off("click").on("click",function(){
		if($(".cur_page").prev()){
			var prePage = $(".cur_page").prev();
			if(prePage.hasClass("pre") || prePage.hasClass("next") || !prePage.length){
				return false;
			}
			if(prePage.hasClass("count_down_page") || prePage.hasClass("share_score_page")){
				$("#main").css("background-color","#DD4B4B");
			}else{
				$("#main").css("background-color","#FFFFFF");
			}
			$('.pre').css('display',"block");
			$(".cur_page").addClass("hidden");
			$(".cur_page").removeClass("cur_page");
			prePage.addClass("cur_page").removeClass("hidden");
		}
		if($(".cur_page").next()){
			$('.next').css('display',"block");
		}
	});
	$(".next").off("click").on("click",function(){
		if($(".cur_page").next()){
			var nextPage = $(".cur_page").next();
			if(nextPage.hasClass("pre") || nextPage.hasClass("next") || !nextPage.length){
				return false;
			}
			if(nextPage.hasClass("count_down_page") || nextPage.hasClass("share_score_page")){
				$("#main").css("background-color","#DD4B4B");
			}else{
				$("#main").css("background-color","#FFFFFF");
			}
			$('.next').css('display',"block");
			$(".cur_page").addClass("hidden");
			$(".cur_page").removeClass("cur_page");
			nextPage.addClass("cur_page").removeClass("hidden");
		}
		if($(".cur_page").prev()){
			$('.pre').css('display',"block");
		}
	});
	
	//点击开始考试按钮响应
	$(".home_opt .start").off("click").on("click",function(){
		//清空答题时间计时器,答题计数器从1开始,防止人为操作返回继续答题
		clearInterval(timerId);
		answerCount = 1;
		//切换页头
		setHeader(false);
		var route1 = {"route":"#count"};
		addroute(route1);
		//获取试题
		getAndShowPagers();
		
		var countDownPage = $(".cur_page").next();
		$(".cur_page").addClass("hidden");
		$(".cur_page").removeClass("cur_page");
		countDownPage.addClass("cur_page").removeClass("hidden");
		
		//这里做个判断如果倒计时不是从3开始那么要从三开始
		$(".time_count_down").css("background-image","url(./assets/img/count_down_3.jpg)");
		
		//开启倒计时定时器
		var trace = 2;
		 countTimer = setInterval(function(){
			if(trace==0){
				clearInterval(countTimer);
				blockExamPage();
				return false;
			}
			var imgSrc = "./assets/img/count_down_"+trace+".jpg";
			$(".time_count_down").css("background-image","url("+imgSrc+")");
			trace--;
		},1000);
	});
	
	//点击英雄榜事件
	$(".rank").on("click",function(){
		//切换页头
		setHeader(false);
		var route1 = {"route":"#scores"};
		addroute(route1);
	    //获取英雄榜数据
		getHeroData();
		var heroPage = $(".score_page");
		$(".cur_page").addClass("hidden");
		$(".cur_page").removeClass("cur_page");
		heroPage.addClass("cur_page").removeClass("hidden");
		
	
	});
	
	$('.share').on("click",function(){
		//切换页头
		setHeader(false);
		$(".header .htitle").text("最佳成绩");
		var route1 = {"route":"#share"};
		addroute(route1);
		$(".exam_pass_page").addClass("hidden");
		$(".cur_page").removeClass("cur_page");
		$('.share_score_page').removeClass('hidden');
		$('.share_score_page').addClass('cur_page');
		//$(".share_rank_div span i").html(thisCurrent);
		if ($('.best_scores').css('display') == 'none'){
          $(".share_rank_div span i").text($(".this_victory span i").text());
		}else{
		  $(".share_rank_div span i").text($(".best_scores span i.best").text());	
		}
	})
	
	//答题选择事件(点击选项也可以)
	$(".options").off("click").on("click",function(e){
		e.preventDefault();
		e.stopPropagation();
		$(this).siblings(".options").children("input").removeAttr("checked");
		$(this).children("input").attr("checked","checked");
		if($(this).children("label").attr("for") == $(".title_content").attr("ans")){
			//答对
			var randomInt = getRanInt(5);
			$(this).css("color","#3fb31b");
			$(".model_layer").css("display","block");
			$(".model_content").css("display","block");
			$(".answer_pic").css("background-image","url(./assets/img/wright/wright_"+randomInt+".jpg)");
			$(".answer_res_info").css("display","block");
			$(".answer_res_info").html("恭喜你,答对了!");
			$(".answer_res_info").css("background-color","#DD4B4B");
			
			$(this).children("label").css("background-image","url(./assets/img/answer_wright.png)");
			setTimeout(hideModelWright,1000);
		}else{
			//答错
			var randomInt = getRanInt(5);
			$(this).css("color","#DD4B4B");
			$(".model_layer").css("display","block");
			$(".model_content").css("display","block");
			$(".answer_pic").css("background-image","url(./assets/img/wrong/wrong_"+randomInt+".jpg)");
			$(".answer_res_info").css("display","block");
			$(".answer_res_info").html("再接再厉哦!");
			$(".answer_res_info").css("background-color","#FAAB00");
			$(this).children("label").css("background-image","url(./assets/img/answer_wrong.png)");
			setTimeout(hideModelWrong,2000);
		}
	});
	
	//答完题后点击"在挑战一次" 响应事件
	$(".re_challenge").on("click",function(){
		var reAnswerPage = $(".home_page");
		addroute(route);
		$(".cur_page").addClass("hidden");
		$(".cur_page").removeClass("cur_page");
		reAnswerPage.addClass("cur_page").removeClass("hidden");
		var screenHeight  = document.body.clientHeight;
		var homePicHeight = $(".home").height();
		var homeOptHeight = px2rem(screenHeight-homePicHeight);
		$(".home_opt").css("height",homeOptHeight+'rem');
		getUserScores();
	});
	
	$('#shareexam').on('click',function(){
	//分享
         var func = function(YonYouJSBridge){
            	function share(){
            	var url1 = basePath+"exam/share.html?memId="+memInfo.memberId;
            	url1 = basePath+"exam/share.html?memId="+memInfo.memberId+'&username='+memInfo.name;
//          	alert(url1);
            	var imgpath = basePath+"exam/assets/img/shareicon.png";
                var  title= '用友文化3.0答题挑战赛', content = '我在用友文化3.0的答题比赛中挑战成功，你也来试试吧，还可以抽奖哦！',
                    data = '{"function":"share","parameters":{"content":"' + content + '","title":"' + title + '","url":"'+ url1 +'","imgUrl":"'+imgpath+'"}}';
//                  alert(JSON.stringify(data));
                    YonYouJSBridge.send(data, function(responseData){});
            };
            share()
            };
		
		connectWebViewJavascriptBridge(func);
	

})

	$('.header .close').on('click',function(){		
             var func1 = function(YonYouJSBridge){
             	function quit(){        
             	  var data ='{"function":"closePage"}';
                  YonYouJSBridge.send(data, function(responseData){});
             	};
             	quit();
             }
             connectWebViewJavascriptBridge(func1);
	})
	
	$(document).off("click",".confirm_quit").on("click",".confirm_quit",function() {
			 $(".cur_page").addClass('hidden');			 
			  $(".cur_page").prev().addClass('hidden');		
			  $(".cur_page").prev().prev().removeClass('hidden');
			  $('.cur_page').removeClass('cur_page')
			  $(".home_page").addClass("cur_page");			  
			  showQuitModel("none","");
			  setHeader(true);
			  Navroute(route);
			  if(countTimer){
	         	clearInterval(countTimer);
	         }
	         if(countTimer){
	         	clearInterval(scoreTimer);
	         }
	})
	$(document).off("click",".cancel_quit").on("click",".cancel_quit",function() {
		
		var param ={"route":"#exam"};
		addroute(param);
		showQuitModel("none","");
	})

	$('.get_prize').on('click',payward);

	$('.home_best_score').on('click',function(){
		var param ={"route":"#score"};
		addroute(param);
		$('.cur_page').addClass('hidden');
		$('.cur_page').removeClass('cur_page');
		$('.exam_pass_page').removeClass('hidden');
		$('.exam_pass_page').addClass('cur_page');
		$('.this_scores').addClass('hidden');
		$('.best_scores span:first-child i').text($('#bst-1').val().toString()+'.'+$('#bst-2').val().toString());
		$('.best_scores span:last-child i').text($('#bst-3').text());
		$('.share_score_wrap span').text(memInfo.name);
		$("#bst-4").val(thisLeftTime_best1);
		$("#bst-5").val(thisRightTime_best1);		
		setHeader(false);
	});
	
});
	
	/**
	 * 根据code获取用户信息
	 */
	function getUserInfo(){
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
//				debugInfo(JSON.stringify(backData));
				if(backData.flag == "0"){
					memInfo = backData.data;
					getUserScores();
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
		
		//便于测试
//		if (!memInfo){
//			memInfo = {"memberId":"2851030","name":"刘海超","deptName":"社交协同"};
//			getUserScores();
//		}
	}
	
	/**
	 * 查询用户最好成绩
	 */
	function getUserScores(){
		var memId = memInfo.memberId;
//		debugInfo(JSON.stringify(memId));
		var url = basePath + "exam/find?memberId="+memId;
		$.ajax({
			type : 'GET',
			async : true,
			url : url,
			cache : false,
			contentType : "application/json",
			success : function(backData) {
//				debugInfo(JSON.stringify(backData));
				if(backData.flag == "0"){
					var resData = backData.data;
//					var secTime = parseInt(resData.bestDuration)/1000;
//					var timeArr = (secTime+"").split(".");
//					var leftTime = timeArr[0];
//					var rightTime = timeArr[1].substr(0,2); 
					var leftTime = formatMicroTime(resData.bestDuration)[0];
					var rightTime = formatMicroTime(resData.bestDuration)[1];
				     thisLeftTime_best1 = leftTime;
                     thisRightTime_best1 = rightTime;  
					//初始化需要成绩的各个页面
					if($(".home_page").hasClass("cur_page")){
						//更换banner
						$(".home_best_score").css("display","block");						
						$(".home_score_div input").eq(0).val(leftTime);
						$(".home_score_div input").eq(1).val(rightTime);
						$(".home_rank_div span i").html(resData.bestPercent);
						$(".home").css("background-image","url(./assets/img/exam_notice_bg.jpg)");
					}
				}else{
					//没有成绩
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
	}
	
	/**
	 * 获取并展示试题
	 */
	function getAndShowPagers(){
		var paperNums = totalPapreCounts;
		var url = basePath + "exam/question?number="+paperNums;
		$.ajax({
			type : 'GET',
			async : true,
			url : url,
			cache : false,
			contentType : "application/json",
			success : function(backData) {
//				debugInfo(JSON.stringify(backData));
				console.log(JSON.stringify(backData));
				if(backData.flag == "0"){
					//已成功获取试题
					paperArr = backData.data;
					//将第一题展现  (用完之后就从数组中除掉)
//					index = 0;
//					var firstQuestion = paperArr[index];//.splice(0,1);				
					var firstQuestion = paperArr.splice(0,1)[0];				
					$(".answer_counts_right i").html(answerCount);
					$(".title_content").html(answerCount+"."+firstQuestion.title);
					$(".title_content").attr("ans",firstQuestion.answer);
					$("#a").siblings("span").html(firstQuestion.a);
					$("#b").siblings("span").html(firstQuestion.b);
					$("#c").siblings("span").html(firstQuestion.c);
					$("#d").siblings("span").html(firstQuestion.d);
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
	}
	
	/**
	 * 获取英雄榜数据
	 */
	function getHeroData(){
		var top = 20;
		//获取自己的最好成绩
		var url = basePath + "exam/find?memberId="+memInfo.memberId;
		var myRank = "";
		$.ajax({
			type : 'GET',
			async : true,
			url : url,
			cache : false,
			contentType : "application/json",
			success : function(backData) {
				console.log(JSON.stringify(backData));
				if(backData.flag == "0"){
					myRank = backData.data.ranking;
					if(myRank == "0"){
						$(".score_page .top").removeClass("active");
						$(".score_page .top .rankword").css("display","none");
					}else{
						$(".score_page .top").addClass("active");
						$(".score_page .top .rankword").css("display","block");
						$(".score_page .top .rankword em").html(myRank);
						var deadline = getNowTime();
						$(".rankword .time").html(deadline);
					}
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
		//如果没哟正确取到我的成绩
		if(myRank == "" || myRank == "0"){
			$(".score_page .top").removeClass("active");
			$(".score_page .top .rankword").css("display","none");
		}
		getHeroList(top);
		
	}
	
	function blockExamPage(){
		//开始计时
		tiktok();
		var route1 = {"route":"#exam"};
		addroute(route1);
		//展示题目
		var paperPage = $(".cur_page").next();
		$(".cur_page").addClass("hidden");
		$(".cur_page").removeClass("cur_page");
		paperPage.addClass("cur_page").removeClass("hidden");
	}
	
	function getHeroList(top){
		var url = basePath + "exam/list?number="+top;
		$.ajax({
			type : 'GET',
			async : true,
			url : url,
			cache : false,
			contentType : "application/json",
			success : function(backData) {
//				debugInfo(JSON.stringify(backData));
				console.log(JSON.stringify(backData));
				if(backData.flag == "0"){
					// 清空原来英雄榜数据
					$(".score_page .scorelist").empty();
					//已经获得了英雄榜单数据
					var heroArr = backData.data;
					for(var i = 0; i< heroArr.length; i++){
//						var duration = "";
//						if(!((parseInt(heroArr[i].duration)/1000) % 1)){
//							duration = parseInt(heroArr[i].duration)/1000.00 + '.00';
//						}else{
//							duration = parseInt(heroArr[i].duration)/1000.00 + '';
//						}
//						var arr = duration.split(".");
						var arr = formatMicroTime(heroArr[i].duration);
						var secDuration = arr[0]+"."+arr[1].substr(0,2)+" S";
						var thisDeptName = heroArr[i].deptName;
						if(!thisDeptName){
							thisDeptName = "";
						}
						var temp = $('<li class="scoreitem">'+
										'<div class="ranknum">'+
											'<div class="num">'+heroArr[i].order+'</div>'+
										'</div>'+
										'<div class="info">'+
											'<span class="name">'+heroArr[i].name+'</span>'+
											'<span class="dep">'+thisDeptName+'</span>'+
										'</div>'+
										'<div class="score">'+
											secDuration+
										'</div>'+
									'</li>');
						if(heroArr[i].order == "1"){
							temp.find(".ranknum").addClass("h");
						}else if(heroArr[i].order == "2"){
							temp.find(".ranknum").addClass("m");
						}else if(heroArr[i].order == "3"){
							temp.find(".ranknum").addClass("l");
						}
						
						$(".score_page .scorelist").append(temp);
					}
					
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
	
	}
	
	
	/**
	 * 从url中获得参数
	 * @param {Object} url
	 */
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
	
	/**显示/关闭调试信息
	 * @param {Object} msg
	 */
	function debugInfo(msg){
		if(printAlert){
			alert(msg);
		}else{
			
		}
	}
	
	/**
	 * 获取一个1到intNum的整数
	 * @param {Object} intNum
	 */
	function getRanInt(intNum){
		var ran = Math.random()*intNum;
		var intRes = Math.ceil(ran);
		return intRes;
	}
	
	/**
	 * 隐藏遮罩和图片并将选项重置(答错时)
	 */
	function hideModelWrong(){
		$(".model_layer").css("display","none");
		$(".model_content").css("display","none");
		$(".answer_res_info").css("display","none");
		//重置选项
		$(".paper_content .options").css("color","#666666");
		$(".paper_content .options input[type='radio']").removeAttr("checked");
		$(".paper_content .options label").css("background-image","url(./assets/img/option_unchecked.png)");
	}
//	var index;
	/**
	 * 答对时图层处理(进入到下一题)
	 */
	function hideModelWright(){
		answerCount++;
		$(".model_layer").css("display","none");
		$(".model_content").css("display","none");
		$(".answer_res_info").css("display","none");
		//显示下一题
//		index = index + 1;
//		var nextQuestion = paperArr[index];//paperArr.splice(0,1);				
		var nextQuestion = paperArr.splice(0,1)[0];	
		//如果还有题目
		if(nextQuestion){
			$(".answer_counts_right i").html(answerCount);
			$(".title_content").html(answerCount+"."+nextQuestion.title);
			$(".title_content").attr("ans",nextQuestion.answer);
			$("#a").siblings("span").html(nextQuestion.a);
			$("#b").siblings("span").html(nextQuestion.b);
			$("#c").siblings("span").html(nextQuestion.c);
			$("#d").siblings("span").html(nextQuestion.d);
			//重置选项
			$(".paper_content .options").css("color","#666666");
			$(".paper_content .options input[type='radio']").removeAttr("checked");
			$(".paper_content .options label").css("background-image","url(./assets/img/option_unchecked.png)");
		}
		
		//如果是最后一道题跳转到"考试通过界面"
		if(answerCount > totalPapreCounts){
			clearInterval(timerId);
			debugInfo("已经答完所有题目了");
			//保存成绩
			var memId = memInfo.memberId;
			var name = memInfo.name;
			var deptName = memInfo.deptName;
			var duration = userdTime*10;
			var url = basePath + "exam/save";
			var obj = {
				"memberId":memId,
				"name":name,
				"deptName":deptName,
				"duration":duration
			};
			$.ajax({
			type : 'POST',
			async : true,
			url : url,
			cache : false,
			data:JSON.stringify(obj),
			contentType : "application/json",
			success : function(backData) {
				debugInfo(JSON.stringify(backData));
				console.log(JSON.stringify(backData));
				if(backData.flag == "0"){
					$('.this_scores').removeClass('hidden');
					setHeader(false);
//					$(".cur_page").addClass('hidden');					
//					var nextpage = $(".cur_page").next();
//					$('.cur_page').removeClass('cur_page');
//					nextpage.addClass('cur_page').removeClass('hidden');
					var route = {"route":"#score"};
                    addroute(route);
					
					//显示考试通过成绩页
					var paperPassPage = $(".exam_pass_page");
					$(".cur_page").addClass("hidden");
					$(".cur_page").removeClass("cur_page");
					paperPassPage.addClass("cur_page").removeClass("hidden");
					
					//根据返回的数据是否有历史成绩来判断是否是第一次考试
					var usefulData = backData.data;
//					var thisTime = parseInt(usefulData.curDuration)/1000;
//					var timeArr = (thisTime+"").split(".");
//					var thisLeftTime = timeArr[0];
//					var thisRightTime = timeArr[1].substr(0,2); bestDuration
					var thisLeftTime = formatMicroTime(usefulData.curDuration)[0];
					var thisRightTime = formatMicroTime(usefulData.curDuration)[1];
					if  (usefulData.bestDuration){
					  thisLeftTime_best1 = formatMicroTime(usefulData.bestDuration)[0];
					  thisRightTim_best1 = formatMicroTime(usefulData.bestDuration)[1]; 
					}else{
						thisLeftTime_best1 = thisLeftTime;
					    thisRightTim_best1 = thisRightTime; 
					    thisCurrent = usefulData.curPercent;
					}
					$(".share_score_div input").eq(0).val(thisLeftTime_best1);
					$(".share_score_div input").eq(1).val(thisRightTim_best1);
					$(".share_rank_div span i").html(usefulData.bestPercent);
					$('.share_score_wrap span').text(memInfo.name);
					$(".this_cost_time input").eq(0).val(thisLeftTime);
					$(".this_cost_time input").eq(1).val(thisRightTime);
					$(".this_victory span i").html(usefulData.curPercent);
					if (!usefulData.bestDuration || !usefulData.bestPercent){
						//首次考试
						$(".best_scores").css("display","none");
						
					}else{
						//非首次考试
						$(".best_scores").css("display","block");
//						var bestTime = parseInt(usefulData.bestDuration)/1000;
//						var bestArr = (bestTime+"").split(".");
//						var bestLeftTime = bestArr[0];
//						var bestRightTime = bestArr[1].substr(0,2);
						var bestLeftTime = formatMicroTime(usefulData.bestDuration)[0];
						var bestRightTime = formatMicroTime(usefulData.bestDuration)[1];
						$(".best_scores span i").eq(0).html(bestLeftTime+"."+bestRightTime);
						$(".best_scores span i").eq(1).html(usefulData.bestPercent);
					}
					//恢复默认选项(重置题号)
					answerCount = 1;
					$(".paper_content .options").css("color","#666666");
					$(".paper_content .options input[type='radio']").removeAttr("checked");
					$(".paper_content .options label").css("background-image","url(./assets/img/option_unchecked.png)");
				}
			},
			error : function(tt) {
				debugInfo(tt);
			}
		});
		
		}
		
	}
	
	/**
	 * 离开弹出框组件
	 * @param {Object} block_hide
	 * @param {Object} info  确定要放弃答题?...
	 */
	function showQuitModel(block_hide,info){
		if(block_hide=="block"){
			var str = $('<div class="quit_layer"></div>'+
					 '<div class="quit_content">'+
					 	'<span class="quit_close"></span>'+
					 	'<span class="quit_info">'+info+'</span>'+
					 	'<div class="quit_confirm_cancel">'+
					 		'<span class="confirm_quit"></span>'+
					 		'<span class="cancel_quit"></span>'+
					 	'</div>'+
					 '</div>');
			$(str[0]).css("display",block_hide);
			$(str[1]).css("display",block_hide);
			$("#main").append(str);
		}else{
			$(".quit_layer").css("display",block_hide);
			$(".quit_content").css("display",block_hide);
		}
	}



//function connectWebViewJavascriptBridge(callback){
//	    if (window.WebViewJavascriptBridge){
//	        callback(WebViewJavascriptBridge);
//	    } else {
//	        document.addEventListener('WebViewJavascriptBridgeReady', function(){
//	            callback(WebViewJavascriptBridge);
//	        }, false);
//	    }
//}
//
//function sharecc(){
//	
//   connectWebViewJavascriptBridge(function(YonYouJSBridge){ //隐藏APP自带的菜单栏
//			var data = {"function":"share","parameters":{"content":"aaaa","title":"bbbb","imgUrl":"http://upesn.com/default.jpg"},"url":"http://www.baidu.com"};
//			     YonYouJSBridge.send(data, function(responseData) {
//			     	alert(JSON.stringify(responseData));
//			     });
//		});
//
//}


function setHeader(isHome,title) {
	// body...
	if(isHome){
		$('.header .back').addClass('hidden');
		$('.header .close').removeClass('hidden');
    }else{
        $('.header .close').addClass('hidden');
		$('.header .back').removeClass('hidden');
    }
	
}

//页面路由
var route = {"route":"#home"};
addroute(route);
// var route = {"key":"#count"};
// var route = {"key":"#exam"};
// var route = {"key":"#score"};
// var route = {"key":"#scores"};
// var route = {"key":"#share"};
function addroute(param) {	
	history.pushState(param, "", param.route)	
}

function  Navroute(param) {
	// body...	
	history.replaceState(param, "", param.route);	
}
window.onpopstate = function(event) {
	var state = event.state;
	console.log(state.route);
	$(".header .htitle").text("文化四六级");
	switch(state.route){
		case "#home":
				$(".cur_page").addClass('hidden');
				// $(".cur_page").removeClass('cur_page');
				$(".cur_page").prev().removeClass('hidden');
				if(countTimer){
		         	clearInterval(countTimer);
		         }
		        $('.cur_page').removeClass('cur_page')
				$(".home_page").addClass("cur_page");	
				$(".home_page").removeClass('hidden').addClass('cur_page');
				$('.exam_page').addClass('hidden');			
				$('.exam_pass_page').addClass('hidden');
				$('.page_turning_eff').css('display','none');
				 setHeader(true);
			   break;
		case "#count":
              showQuitModel("block","确定要放弃答题?");			 
			  break;
		case "#exam":
			  $(".cur_page").addClass('hidden');			 
			  $(".cur_page").prev().addClass('hidden');		
			  $(".cur_page").prev().prev().addClass('hidden');			  
			  $(".cur_page").prev().prev().prev().removeClass('hidden');			  
			  $('.cur_page').removeClass('cur_page')
			  $(".home_page").addClass("cur_page");
			  setHeader(true);
			  Navroute(route);
	         
			break;
		case "#score":
			  $(".cur_page").addClass('hidden');			 
			  // $(".cur_page").prev().addClass('hidden');		
			  // $(".cur_page").prev().prev().addClass('hidden');			  
			  // $(".cur_page").prev().prev().prev().addClass('hidden');
			  // $(".cur_page").prev().prev().prev().prev().addClass('hidden');	
			  $('.cur_page').removeClass('cur_page')
			  // $(".cur_page").prev().prev().removeClass('hidden').addClass('cur_page');
			  $(".exam_pass_page").removeClass('hidden');
			  $(".exam_pass_page").addClass('cur_page');
			  // $(".home_page").addClass("cur_page");
			  setHeader(false);
			  break;
	    case "#scores":			  
			  $(".cur_page").addClass('hidden');			 
			  $(".cur_page").prev().addClass('hidden');		
			  $(".cur_page").prev().prev().addClass('hidden');
			  $(".cur_page").prev().prev().prev().addClass('hidden');
			  $(".cur_page").prev().removeClass('hidden');

			  setHeader(true);
			  break;
		default:
		   break;

	 }
  
};

/**
 * 获取当前截止时间
 */
function getNowTime(){
 	var date = new Date();
//	var curTime = date.toLocaleString();
//	var beforeBlank = curTime.split(" ")[0];
//	var tempArr1 = beforeBlank.split("/");
//	var strBlank = tempArr1[0]+"年"+tempArr1[1]+"月"+tempArr1[2]+"日";
//	var afterBlank = curTime.split(" ")[1];
	var strBlank = date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
	var hourTime = date.getHours();
	if(hourTime < 10){
		hourTime = "0"+hourTime;
	}
	var minTime = date.getMinutes();
	if(minTime<10){
		minTime = "0"+minTime;
	}
	var secTime = date.getSeconds();
	if(secTime<10){
		secTime = "0"+secTime;
	}
	var afterBlank = hourTime+":"+minTime+":"+secTime;
	return strBlank+" "+afterBlank;
}

/**
 * 格式化毫秒时间(着重处理1000和100的整数倍)
 * @param {Object} microTime
 */
function formatMicroTime(microTime){
	var duration = "";
	var intTime = parseInt(microTime);
	if(!((intTime/1000) % 1)){
		duration = intTime/1000.00 + '.00';
	}else if(!((intTime/100) % 1)){
		duration = intTime/1000.00 + '0';
	}else{
		duration = intTime/1000.00 + '';
	}
	var arr = duration.split(".");
	return arr;
}
function payward() {
    var str = $('<div class="quit_layer"></div>'+
			 '<div class="quit_content">'+
			 	'<span class="quit_close"></span>'+
			 	'<span class="quit_info" style="font-size:0.5rem;top:1.5rem">一大波奖品正在路上...</span>'+			
			 '</div>');			
    $(str[0]).css("display","block");
	$(str[1]).css("display","block");
	$("#main").append(str);	
	setTimeout(function() {
	$(str[0]).css("display","none");
	$(str[1]).css("display","none");
	},1000);
}

package com.yonyou.esn.yycollege.controller;

import com.alibaba.fastjson.JSON;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.utils.HttpClientUtil;
import com.yonyou.esn.yycollege.utils.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共功能
 *
 */
@RestController
public class PublicController {
	

	//调用企业空间后台获取用户信息
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getUserInfo", method=RequestMethod.GET)
	public Object getUserInfo(@RequestParam("code") String code){

		JsonResult jsonResult = new JsonResult();

		//获取token
		String token_url = "https://open.esn.ren/openapi/token?appid=9ad99e4505b4d4ab&secret=dac8be17266b965cafef4912d1cff0383fc9f263b76d354cbff3b3bd6747";
		String token = "";
		String resultStr = "";
		try {
			resultStr = HttpClientUtil.httpsGet(token_url);
			token = (String)((Map<String, String>)((Map<String, Object>) JSON.parse(resultStr)).get("data")).get("access_token");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(token)){
			jsonResult.setFlag("900");
			jsonResult.setDesc("获取用户详细信息时出错!");
			return jsonResult;
		}

		//获取用户信息
		String info_url = "https://open.esn.ren/openapi/certified/userInfo/" + code + "?access_token=" + token;
		Map<String,String> resultMap = null;
		try {
			resultStr = HttpClientUtil.httpsGet(info_url);
			resultMap = (Map<String, String>)((Map<String, Object>) JSON.parse(resultStr)).get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if( null == resultMap){
			jsonResult.setFlag("901");
			jsonResult.setDesc("获取用户详细信息时出错!");
			return jsonResult;
		}


		//返回数据整理
		Map<String, String> data = new HashMap<String, String>();
		data.put("memberId", resultMap.get("member_id"));
		data.put("name", resultMap.get("name"));
		data.put("email", resultMap.get("email"));
		data.put("deptName", resultMap.get("dept_name"));
		data.put("mobile", resultMap.get("mobile"));
		data.put("avatar", resultMap.get("avatar"));

		jsonResult.setData(data);
		return jsonResult;
	}
	
}

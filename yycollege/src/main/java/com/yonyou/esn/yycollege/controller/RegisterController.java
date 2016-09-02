/**
 * 
 */
package com.yonyou.esn.yycollege.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.esn.yycollege.model.Register;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.model.vo.RegisterVo;
import com.yonyou.esn.yycollege.service.RegisterService;

/**
 * @author jingzz
 * @time 2016年8月22日 上午11:39:07
 * @name yycollege/com.yonyou.esn.yycollege.controller.TestController
 * @since 2016年8月22日 上午11:39:07
 */
@RestController
public class RegisterController {

	@Autowired
	private RegisterService registerService;

	@RequestMapping(value = "/register/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult save(@RequestBody RegisterVo registerVo) {

		JsonResult jsonResult = registerService.saveRegisterInfo(registerVo);
		return jsonResult;
	}
	
	@RequestMapping(value = "/register/find", method = RequestMethod.GET)
	public JsonResult find(@RequestParam("memberId") String memberId) {
		if (StringUtils.isEmpty(memberId)) {
			return new JsonResult("1", "memberId参数不能为空", null);
		}
		Register regVo = registerService.getRegInfoByMemberId(memberId);
		JsonResult jsonResult = new JsonResult();
		if (regVo.getName() != null) {
			jsonResult.setFlag("0");
			jsonResult.setDesc("操作成功！");
			jsonResult.setData(regVo);
		} else {
			jsonResult.setFlag("1");
			jsonResult.setDesc("无该id["+memberId+"]对应的用户信息");
			jsonResult.setData(regVo);
		}
		return jsonResult;
	}

}

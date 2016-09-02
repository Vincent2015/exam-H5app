/**
 * 
 */
package com.yonyou.esn.yycollege.service;

import org.springframework.stereotype.Service;

import com.yonyou.esn.yycollege.model.Register;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.model.vo.RegisterVo;

/**
 * @author jingzz
 * @time 2016年8月22日 下午3:18:14
 * @name yycollege/com.yonyou.esn.yycollege.service.RegisterService
 * @since 2016年8月22日 下午3:18:14
 */
@Service
public interface RegisterService {
	/**
	 * 保存签到信息
	 * @author jingzz
	 * @param registerVo
	 * @return
	 */
	JsonResult saveRegisterInfo(RegisterVo registerVo);

	/**
	 * @author jingzz
	 * @param memberId
	 * @return
	 */
	Register getRegInfoByMemberId(String memberId);
	

}

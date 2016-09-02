/**
 * 
 */
package com.yonyou.esn.yycollege.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yonyou.esn.yycollege.mapper.RegisterMapper;
import com.yonyou.esn.yycollege.model.Register;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.model.vo.RegisterVo;
import com.yonyou.esn.yycollege.service.RegisterService;

/**
 * @author jingzz
 * @time 2016年8月22日 下午3:18:42
 * @name yycollege/com.yonyou.esn.yycollege.service.impl.RegisterServiceImpl
 * @since 2016年8月22日 下午3:18:42
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	
	@Value("#{yycollegeEnv.QRCode_content}")
	private String QRCODE_CONTENT;
	
	private static final Logger LOG = LoggerFactory.getLogger(RegisterServiceImpl.class);
	
	@Autowired
	private RegisterMapper registerMapper;

	public JsonResult saveRegisterInfo(RegisterVo registerVo) {
		String msg = "";
		if (registerVo == null || registerVo.getMemberId() == null) {
			msg = "参数不能为空";
			LOG.error(msg);
			return new JsonResult("1", msg, null);
		}
		RegisterVo rvo = null;
		if (!QRCODE_CONTENT.equals(registerVo.getContent())) {
			msg = "二维码不正确，二维码信息["+registerVo.getContent()+"]";
			LOG.error(msg);
			return new JsonResult("1", msg, null);
		}
		Register register = new Register();
		register.setDeptName(registerVo.getDeptName());
		register.setMemberId(registerVo.getMemberId());
		register.setName(registerVo.getName());
		register.setTime(System.currentTimeMillis());
		//判断是否已经签到
		Register oldReg = registerMapper.selectByMemberId(register.getMemberId());
		if (oldReg != null) {
			rvo  = new RegisterVo();
			rvo.setRank(oldReg.getRank());
			rvo.setName(oldReg.getName());
			return new JsonResult("1", "签到信息已经存在", rvo);
		}
		int num = registerMapper.insertSelective(register);
		if (num == 0) {
			msg = "对用户["+register.getName()+"]("+register.getMemberId()+")签到失败";
			LOG.error(msg);
		}else {
			int rank = registerMapper.getRegisterRankByRegTime(register.getTime());
			int updateNum = registerMapper.updateRankByMemberId(register.getMemberId(),rank);
			if (updateNum > 0) {
				rvo  = new RegisterVo();
				rvo.setRank(rank);
				rvo.setName(register.getName());
				return new JsonResult("0", "签到成功", rvo);
			}else{
				msg = "对用户["+register.getName()+"]("+register.getMemberId()+")计算排名失败";
				LOG.error(msg);
			}
		}
		return new JsonResult("1006", msg, null);
	}
	
	public Register getRegInfoByMemberId(String memberId) {
		Register reg = registerMapper.selectByMemberId(memberId);
		if (reg != null) {
			reg.setDeptName(null);
			reg.setId(null);
			reg.setTime(null);
		}
		return reg == null ? new Register() : reg;
	}
	
}

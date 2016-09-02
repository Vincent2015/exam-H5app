/**
 * 
 */
package com.yonyou.esn.yycollege.service;

import com.yonyou.esn.yycollege.model.vo.ExamParamsVo;
import com.yonyou.esn.yycollege.model.vo.JsonResult;

/**
 * @author jingzz
 * @time 2016年8月23日 下午2:38:57
 * @name yycollege/com.yonyou.esn.yycollege.service.ExamService
 * @since 2016年8月23日 下午2:38:57
 */
public interface ExamService {

	/**
	 * 获取指定数量的随机问题信息
	 * @author jingzz
	 * @param number
	 * @return
	 */
	JsonResult getNumRandomQuestions(int number);

	/**
	 * 保存试题的成绩记录
	 * @author jingzz
	 * @param examVo
	 * @return
	 */
	JsonResult saveOrUpdateQuestions(ExamParamsVo examVo);

	/**
	 * @author jingzz
	 * @param memberId
	 * @return
	 */
	JsonResult getPersonExamScore(String memberId, Long startTime, Long endTime);

	/**
	 * @author jingzz
	 * @param number
	 * @param endTime 
	 * @param startTime 
	 * @return
	 */
	JsonResult getHeroList(int number, Long startTime, Long endTime);

}

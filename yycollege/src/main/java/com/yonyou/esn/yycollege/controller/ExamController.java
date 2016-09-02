/**
 * 
 */
package com.yonyou.esn.yycollege.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.esn.yycollege.model.vo.ExamParamsVo;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.service.ExamService;

/**
 * @author jingzz
 * @time 2016年8月23日 下午2:36:25
 * @name yycollege/com.yonyou.esn.yycollege.controller.ExamController
 * @since 2016年8月23日 下午2:36:25
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

	@Autowired
	private ExamService examService;

	/**
	 * 查询试题
	 * 
	 * @author jingzz
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/question", method = RequestMethod.GET)
	public JsonResult getQuestions(@RequestParam("number") int number) {
		return examService.getNumRandomQuestions(number);
	}

	/**
	 * 保存答题成绩
	 * 
	 * @author jingzz
	 * @param examVo
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveGrade(@RequestBody ExamParamsVo examVo) {
		return examService.saveOrUpdateQuestions(examVo);
	}

	/**
	 * 获取指定人员的答题成绩
	 * 
	 * @author jingzz
	 * @param memberId
	 * @param startTime 
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public JsonResult getScore(@RequestParam("memberId") String memberId,
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime) {
		return examService.getPersonExamScore(memberId, startTime, endTime);
	}

	/**
	 * 获取前number名次的英雄榜
	 * 
	 * @author jingzz
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult getHeroList(@RequestParam("number") int number,
			@RequestParam(name = "startTime", required = false) Long startTime,
			@RequestParam(name = "endTime", required = false) Long endTime) {
		return examService.getHeroList(number, startTime, endTime);
	}

}

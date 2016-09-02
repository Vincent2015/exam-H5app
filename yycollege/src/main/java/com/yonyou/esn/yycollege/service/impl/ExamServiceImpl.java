/**
 * 
 */
package com.yonyou.esn.yycollege.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yonyou.esn.yycollege.common.ExamLib;
import com.yonyou.esn.yycollege.mapper.ExamLogMapper;
import com.yonyou.esn.yycollege.mapper.ExamMapper;
import com.yonyou.esn.yycollege.model.Exam;
import com.yonyou.esn.yycollege.model.ExamLog;
import com.yonyou.esn.yycollege.model.TestPaper;
import com.yonyou.esn.yycollege.model.vo.ExamParamsVo;
import com.yonyou.esn.yycollege.model.vo.ExamRespVo;
import com.yonyou.esn.yycollege.model.vo.HeroListVo;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.service.ExamService;
import com.yonyou.esn.yycollege.utils.StringUtil;

/**
 * @author jingzz
 * @time 2016年8月23日 下午2:39:16
 * @name yycollege/com.yonyou.esn.yycollege.service.impl.ExamServiceImpl
 * @since 2016年8月23日 下午2:39:16
 */
@Service
public class ExamServiceImpl implements ExamService {

	private static final Logger LOG = LoggerFactory.getLogger(ExamServiceImpl.class);

	@Autowired
	private ExamMapper examMapper;

	@Autowired
	private ExamLib examLib;

	@Autowired
	private ExamLogMapper examLogMapper;

	public JsonResult getNumRandomQuestions(int number) {
		number = number <= 0 ? 20 : number;
		List<TestPaper> testPapers = examLib.getRandomTitle(number);
		if (CollectionUtils.isEmpty(testPapers)) {
			String msg = "试题数据为空";
			LOG.error(msg);
			return new JsonResult("1001", msg, testPapers);
		}
		return new JsonResult("0", "获取试题成功", testPapers);
	}

	public JsonResult saveOrUpdateQuestions(ExamParamsVo examVo) {
		if (examVo == null || examVo.getMemberId() == null || examVo.getDuration() == null) {
			return new JsonResult("1", "参数错误", examVo);
		}
		Exam record = new Exam();
		record.setDeptName(examVo.getDeptName());
		record.setDuration(examVo.getDuration());
		record.setMemberId(examVo.getMemberId());
		record.setName(examVo.getName());
		record.setUpdateTime(System.currentTimeMillis());
		int num = 0;
		boolean newScoreIsBetter = false;
		ExamLog examLog = new ExamLog();
		BeanUtils.copyProperties(record, examLog);

		//添加答题记录,记录可以丢失，但不能影响系统正常服务
		try {
			examLogMapper.insertSelective(examLog);
		} catch (Exception e) {
			LOG.error(examLog.getDeptName() + "部门的[" + examLog.getName() + "](" + examLog.getMemberId() + ")在"
					+ StringUtil.getTimeString(examLog.getUpdateTime()) + "时的答题记录保存出现异常！"+examLog, e);
		}

		Exam exam = examMapper.getExamScoreByMemberId(examVo.getMemberId());
		if (exam == null) {
			num = examMapper.insertSelective(record);
		} else {
			newScoreIsBetter = exam.getDuration() > record.getDuration();
			if (newScoreIsBetter) {
				num = examMapper.updateByMemberId(record);
			}
		}

		// 判断成绩是否添加或更新成功
		if (num == 0 && exam == null) {
			String msg = record.getDeptName() + "部门的[" + record.getName() + "](" + record.getMemberId() + "新增成绩记录失败";
			LOG.error(msg);
			return new JsonResult("1002", msg, null);
		} else if (num == 0 && exam != null && newScoreIsBetter) {
			String msg = record.getDeptName() + "部门的[" + record.getName() + "](" + record.getMemberId() + "成绩记录更新失败";
			LOG.error(msg);
			return new JsonResult("1003", msg, null);
		}

		int totalCount = getTotalCount();
		int totalOtherCount = totalCount - 1;
		if (exam == null) {// 没有成绩
			if (totalCount == 1) {// 没有其他人
				ExamRespVo examRespVo = new ExamRespVo();
				examRespVo.setCurDuration(record.getDuration());
				examRespVo.setCurPercent("100%");
				return new JsonResult("0", "成绩记录成功", examRespVo);
			} else {// 有其他人
				/*
				 * 在第一次记录成绩时，认为第一次的成绩是最好的
				 */
				return getCurAndBestScore(record, true, exam, totalOtherCount);
			}
		} else {// 有成绩
			if (totalCount == 1) {// 没有其他人
				ExamRespVo examRespVo = new ExamRespVo();
				examRespVo.setCurDuration(record.getDuration());
				examRespVo.setCurPercent("100%");
				examRespVo.setBestDuration(newScoreIsBetter ? record.getDuration() : exam.getDuration());
				examRespVo.setBestPercent("100%");
				return new JsonResult("0", "成绩记录成功", examRespVo);
			} else {// 有其他人
				return getCurAndBestScore(record, newScoreIsBetter, exam, totalOtherCount);
			}
		}
	}

	/**
	 * 获取总数量
	 * @author jingzz
	 * @return
	 */
	private int getTotalCount() {
		return examMapper.getTotalCount(null, null);
	}

	/**
	 * 获取当前和最好成绩的用时和排名比例
	 * 
	 * @author jingzz
	 * @param newRecord
	 * @param newScoreIsBetter
	 * @param bestRecord
	 * @param totalOtherCount
	 * @return
	 */
	protected JsonResult getCurAndBestScore(Exam newRecord, boolean newScoreIsBetter, Exam bestRecord,
			int totalOtherCount) {
		ExamRespVo examRespVo = new ExamRespVo();

		// 获取比其落后的人员数量
		int curLaterCount = examMapper.getLaterPersonCount(newRecord.getDuration(),null,null);

		// 比其落后的人员比例
		String curScoreRankPercent = getScoreRankPercent(curLaterCount, totalOtherCount);

		examRespVo.setCurDuration(newRecord.getDuration());
		examRespVo.setCurPercent(curScoreRankPercent);

		// 处理最好成绩
		if (bestRecord != null) {
			Integer bestLaterCount = null;
			String bestScoreRankPercent = null;
			examRespVo.setBestDuration(newRecord.getDuration());
			examRespVo.setBestPercent(curScoreRankPercent);
			if (!newScoreIsBetter) {// 新成绩没有历史最好成绩好
				// 获取比其最好成绩要差的人员数量
				bestLaterCount = examMapper.getLaterPersonCount(bestRecord.getDuration(),null,null);
				// 获取相应的比例
				bestScoreRankPercent = getScoreRankPercent(bestLaterCount, totalOtherCount);
				examRespVo.setBestDuration(bestRecord.getDuration());
				examRespVo.setBestPercent(bestScoreRankPercent);
			}
		}
		return new JsonResult("0", "成绩记录成功", examRespVo);
	}

	// 获取用户答题所花时间比多少人少
	private static String getScoreRankPercent(int numerator, int denominator) {
		if (numerator <= 0) {
			return "00.00%";
		}else if (denominator <= 0) {
			return "100%";
		} else {
			double num = (numerator / (double) denominator) * 100;
			DecimalFormat df = new DecimalFormat(".00");
			String format = df.format(num) + "%";
			if (format.indexOf(".") == 0) {
				format = "0" + format;
			}
			return format;
		}
	}
	
	@Override
	public JsonResult getPersonExamScore(String memberId, Long startTime, Long endTime) {
		Exam exam = examMapper.getExamScoreByMemberId(memberId);
		if (exam != null) {
			int totalCount = examMapper.getTotalCount(startTime,endTime);
			int totalOtherCount = totalCount - 1;
			int laterPersonCount = examMapper.getLaterPersonCount(exam.getDuration(),startTime,endTime);
			String scoreRankPercent = getScoreRankPercent(laterPersonCount, totalOtherCount);
			ExamRespVo data = new ExamRespVo();
			data.setBestDuration(exam.getDuration());
			data.setBestPercent(scoreRankPercent);
			Integer ranking = totalCount - laterPersonCount;
			data.setRanking(ranking);
			data.setName(exam.getName());
			return new JsonResult("0", "查询用户[" + exam.getName() + "](" + exam.getMemberId() + ")成绩成功", data);
		}
		String msg = "id为[" + memberId + "]用户的考试信息不存在";
		LOG.error(msg);
		return new JsonResult("1004", msg, null);
	}

	@Override
	public JsonResult getHeroList(int number, Long startTime, Long endTime) {
		number = number <= 0 ? 10 : number;
		List<Exam> lists = examMapper.getHeroListByDuration(number,startTime,endTime);
		List<HeroListVo> results = null;
		if (!CollectionUtils.isEmpty(lists)) {
			results = new ArrayList<HeroListVo>(lists.size());
			for (int i = 0; i < lists.size(); i++) {
				Exam exam = lists.get(i);
				HeroListVo hlvo = new HeroListVo();
				hlvo.setDeptName(exam.getDeptName());
				hlvo.setDuration(exam.getDuration());
				hlvo.setMemberId(exam.getMemberId());
				hlvo.setName(exam.getName());
				hlvo.setOrder(i + 1);
				results.add(hlvo);
			}

		}
		return new JsonResult("0", "查询成功", CollectionUtils.isEmpty(results) ? new ArrayList<HeroListVo>(0) : results);
	}
}

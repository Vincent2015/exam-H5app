package com.yonyou.esn.yycollege.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yonyou.esn.yycollege.model.Exam;

public interface ExamMapper {
    int insert(Exam record);

    int insertSelective(Exam record);

    Exam selectByPrimaryKey(Long id);

    int updateByMemberId(Exam record);

    int updateByPrimaryKey(Exam record);

	/**
	 * @author jingzz
	 * @param memberId
	 * @return
	 */
	Exam getExamScoreByMemberId(@Param("memberId")String memberId);

	/**
	 * 获取所花时间更多的记录数
	 * @author jingzz
	 * @param duration
	 * @return
	 */
	int getLaterPersonCount(@Param("duration")Long duration,@Param("startTime")Long startTime,@Param("endTime") Long endTime);

	/**
	 * 获取时间段内的所有记录
	 * @author jingzz
	 * @return
	 */
	int getTotalCount(@Param("startTime")Long startTime,@Param("endTime") Long endTime);

	/**
	 * @author jingzz
	 * @param number
	 * @return
	 */
	List<Exam> getHeroListByDuration(@Param("number")int number,@Param("startTime")Long startTime,@Param("endTime")Long endTime);
}
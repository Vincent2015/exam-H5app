package com.yonyou.esn.yycollege.mapper;

import org.apache.ibatis.annotations.Param;

import com.yonyou.esn.yycollege.model.Register;

public interface RegisterMapper {
    int insert(Register record);

    int insertSelective(Register record);

    Register selectByMemberId(@Param("memberId")String memberId);

    int updateByPrimaryKeySelective(Register record);

    int updateByPrimaryKey(Register record);

	/**
	 * 根据签到时间获取签排名
	 * @author jingzz
	 * @param memberId
	 * @return
	 */
	int getRegisterRankByRegTime(@Param("time")long time);

	/**
	 * @author jingzz
	 * @param memberId 
	 * @param rank
	 */
	int updateRankByMemberId(@Param("memberId")String memberId, @Param("rank")int rank);
}
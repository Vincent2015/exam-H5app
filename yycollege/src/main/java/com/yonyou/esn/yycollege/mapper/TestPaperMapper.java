package com.yonyou.esn.yycollege.mapper;

import java.util.List;

import com.yonyou.esn.yycollege.model.TestPaper;

public interface TestPaperMapper {
    int insert(TestPaper record);

    int insertSelective(TestPaper record);

    TestPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestPaper record);

    int updateByPrimaryKey(TestPaper record);

	/**
	 * 获取所有的题目
	 * @author jingzz
	 * @return
	 */
	List<TestPaper> getAllTitle();
}
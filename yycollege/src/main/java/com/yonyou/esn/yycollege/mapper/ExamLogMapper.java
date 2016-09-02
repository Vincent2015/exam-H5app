package com.yonyou.esn.yycollege.mapper;

import com.yonyou.esn.yycollege.model.ExamLog;

public interface ExamLogMapper {
    int insert(ExamLog record);

    int insertSelective(ExamLog record);

    ExamLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ExamLog record);

    int updateByPrimaryKey(ExamLog record);
}
package com.yonyou.esn.yycollege.mapper;

import com.yonyou.esn.yycollege.model.Image;

public interface ImageMapper {
    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKeyWithBLOBs(Image record);

    int updateByPrimaryKey(Image record);
}
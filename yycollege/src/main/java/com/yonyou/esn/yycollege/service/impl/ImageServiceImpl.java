/**
 * 
 */
package com.yonyou.esn.yycollege.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.esn.yycollege.mapper.ImageMapper;
import com.yonyou.esn.yycollege.model.Image;
import com.yonyou.esn.yycollege.model.vo.ImageVo;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.service.ImageService;

/**
 * @author jingzz
 * @time 2016年8月25日 上午11:31:30
 * @name yycollege/com.yonyou.esn.yycollege.service.impl.ImageServiceImpl
 * @since 2016年8月25日 上午11:31:30
 */
@Service
public class ImageServiceImpl implements ImageService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);
	
	@Autowired
	private ImageMapper imageMapper;

	@Override
	public JsonResult saveImage(ImageVo imageVo) {
		if (imageVo == null || imageVo.getMemberId() == null) {
			return new JsonResult("1", "参数错误", null);
		}
		
		String imageId = getImageId(imageVo.getMemberId());
		Image record = new Image();
		record.setId(imageId);
		record.setImgstr(imageVo.getImgStr());
		record.setMemberId(imageVo.getMemberId());
		int num = imageMapper.insert(record);
		if (num > 0) {
			imageVo.setImgId(imageId);
			imageVo.setImgStr(null);
			imageVo.setMemberId(null);
			return new JsonResult("0", "成功", imageVo);
		}
		imageVo.setImgStr(null);
		String msg = "添加图片失败";
		LOG.error(msg);
		return new JsonResult("1", msg, imageVo);
	}

	@Override
	public JsonResult findImage(String imgId) {
		if (imgId != null) {
			Image image = imageMapper.selectByPrimaryKey(imgId);
			if (image != null) {
				ImageVo imageVo = new ImageVo();
				imageVo.setImgStr(image.getImgstr());
				imageVo.setMemberId(image.getMemberId());
				return new JsonResult("0", "获取图片成功", imageVo);
			}
		}
		return new JsonResult("1", "获取图片失败", null);
	}
	
	/**
	 * 获取图片的id
	 * @author jingzz
	 * @param memberId
	 * @return
	 */
	private String getImageId(String memberId){
		if (memberId != null) {
			return memberId +"-" + getCurDateTimeStr();
		}
		return null;
	}
	
	private String getCurDateTimeStr(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String format = sdf.format(date);
		return format;
	}
}

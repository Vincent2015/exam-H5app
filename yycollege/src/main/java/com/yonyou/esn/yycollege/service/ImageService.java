/**
 * 
 */
package com.yonyou.esn.yycollege.service;

import com.yonyou.esn.yycollege.model.vo.ImageVo;
import com.yonyou.esn.yycollege.model.vo.JsonResult;

/**
 * @author jingzz
 * @time 2016年8月25日 上午11:31:12
 * @name yycollege/com.yonyou.esn.yycollege.service.ImageService
 * @since 2016年8月25日 上午11:31:12
 */
public interface ImageService {

	/**
	 * 存储图片
	 * @author jingzz
	 * @param imageVo
	 * @return
	 */
	JsonResult saveImage(ImageVo imageVo);

	/**
	 * @author jingzz
	 * @param imgId
	 * @return
	 */
	JsonResult findImage(String imgId);

}

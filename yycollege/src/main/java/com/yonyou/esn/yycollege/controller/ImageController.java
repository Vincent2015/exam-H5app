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

import com.yonyou.esn.yycollege.model.vo.ImageVo;
import com.yonyou.esn.yycollege.model.vo.JsonResult;
import com.yonyou.esn.yycollege.service.ImageService;

/**
 * @author jingzz
 * @time 2016年8月25日 上午11:29:51
 * @name yycollege/com.yonyou.esn.yycollege.controller.ImageController
 * @since 2016年8月25日 上午11:29:51
 */
@RestController
@RequestMapping("/image")
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	/**
	 * 保存图片
	 * @author jingzz
	 * @param imageVo
	 * @return
	 */
	@RequestMapping(value="/save",method= RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveImage(@RequestBody ImageVo imageVo){
		return imageService.saveImage(imageVo);
	}
	
	/**
	 * 查找图片
	 * @author jingzz
	 * @param imgId
	 * @return
	 */
	@RequestMapping(value="/find",method= RequestMethod.GET)
	public JsonResult getImage(@RequestParam("imgId") String imgId){
		return imageService.findImage(imgId);
	}
}

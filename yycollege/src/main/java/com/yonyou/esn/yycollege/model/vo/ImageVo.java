package com.yonyou.esn.yycollege.model.vo;

/**
 * 图片的base64信息
 * @author jingzz
 * @time 2016年8月25日 上午11:32:19
 * @name yycollege/com.yonyou.esn.yycollege.model.vo.Image
 * @since 2016年8月25日 上午11:32:19
 */
public class ImageVo {
	
	//存储图片的成员
	private String memberId;
	
	//图片的base64编码
	private String imgStr;
	
	//图片的id
	private String imgId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getImgStr() {
		return imgStr;
	}

	public void setImgStr(String imgStr) {
		this.imgStr = imgStr;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	@Override
	public String toString() {
		return "ImageVo [memberId=" + memberId + ", imgStr=" + imgStr + ", imgId=" + imgId + "]";
	}
}

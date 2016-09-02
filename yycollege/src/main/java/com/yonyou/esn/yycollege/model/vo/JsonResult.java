/**
 * 
 */
package com.yonyou.esn.yycollege.model.vo;

/**
 * @author jingzz
 * @time 2016年8月22日 下午3:19:41
 * @name yycollege/com.yonyou.esn.yycollege.model.vo.JsonResult
 * @since 2016年8月22日 下午3:19:41
 */
public class JsonResult {
	
	private String flag;
	
	private String desc;
	
	private Object data;


	public JsonResult(String flag, String desc, Object data) {
		super();
		this.flag = flag;
		this.desc = desc;
		this.data = data == null ? new Object() : data;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data == null ? new Object() : data;
	}

	@Override
	public String toString() {
		return "JsonResult [flag=" + flag + ", desc=" + desc + ", data=" + data + "]";
	}

	public JsonResult() {
		setFlag("0");
		setDesc("ok");
		setData(null);
	}
}

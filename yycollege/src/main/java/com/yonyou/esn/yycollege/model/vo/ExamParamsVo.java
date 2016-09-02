/**
 * 
 */
package com.yonyou.esn.yycollege.model.vo;

/**
 * 试题参数VO
 * @author jingzz
 * @time 2016年8月23日 下午2:43:27
 * @name yycollege/com.yonyou.esn.yycollege.model.vo.ExamVo
 * @since 2016年8月23日 下午2:43:27
 */
public class ExamParamsVo {
	
	private String memberId;
	
	private String name;
	
	private String deptName;
	
	private Long duration;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "ExamParamsVo [memberId=" + memberId + ", name=" + name + ", deptName=" + deptName + ", duration="
				+ duration + "]";
	}
	
}

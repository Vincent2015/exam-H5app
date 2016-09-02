/**
 * 
 */
package com.yonyou.esn.yycollege.model.vo;

/**
 * @author jingzz
 * @time 2016年8月25日 上午11:16:45
 * @name yycollege/com.yonyou.esn.yycollege.model.vo.HeroListVo
 * @since 2016年8月25日 上午11:16:45
 */
public class HeroListVo {
	
	private Long duration;

    private String memberId;

    private String name;

    private String deptName;

    private Integer order;

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "HeroListVo [duration=" + duration + ", memberId=" + memberId + ", name=" + name + ", deptName="
				+ deptName + ", order=" + order + "]";
	}
}

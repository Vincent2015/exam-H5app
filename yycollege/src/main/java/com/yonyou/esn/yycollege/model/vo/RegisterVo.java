/**
 * 
 */
package com.yonyou.esn.yycollege.model.vo;

/**
 * @author jingzz
 * @time 2016年8月22日 下午3:16:28
 * @name yycollege/com.yonyou.esn.yycollege.model.vo.RegisterVo
 * @since 2016年8月22日 下午3:16:28
 */
public class RegisterVo {
	
	private String memberId;
	
    private String name;
    
    private String deptName;
    
    private String content;
    
    private int rank;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

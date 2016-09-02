package com.yonyou.esn.yycollege.model;

public class Exam {
    private Long id;

    private Long duration;

    private String memberId;

    private String name;

    private String deptName;

    private String remark;
    
    private Long updateTime;

    public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	@Override
	public String toString() {
		return "Exam [id=" + id + ", duration=" + duration + ", memberId=" + memberId + ", name=" + name + ", deptName="
				+ deptName + ", remark=" + remark + ", updateTime=" + updateTime + "]";
	}
    
}
/**
 * 
 */
package com.yonyou.esn.yycollege.model.vo;

/**
 * 试题返回值VO
 * @author jingzz
 * @time 2016年8月23日 下午2:43:27
 * @name yycollege/com.yonyou.esn.yycollege.model.vo.ExamVo
 * @since 2016年8月23日 下午2:43:27
 */
public class ExamRespVo {
	
	private Long curDuration;
	
	private String curPercent;
	
	private Long bestDuration;
	
	private String bestPercent;
	
	private Integer ranking;

	private String name;
	
	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public Long getCurDuration() {
		return curDuration;
	}

	public void setCurDuration(Long curDuration) {
		this.curDuration = curDuration;
	}

	public String getCurPercent() {
		return curPercent;
	}

	public void setCurPercent(String curPercent) {
		this.curPercent = curPercent;
	}

	public Long getBestDuration() {
		return bestDuration;
	}

	public void setBestDuration(Long bestDuration) {
		this.bestDuration = bestDuration;
	}

	public String getBestPercent() {
		return bestPercent;
	}

	public void setBestPercent(String bestPercent) {
		this.bestPercent = bestPercent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ExamRespVo [curDuration=" + curDuration + ", curPercent=" + curPercent + ", bestDuration="
				+ bestDuration + ", bestPercent=" + bestPercent + ", ranking=" + ranking + "]";
	}

}

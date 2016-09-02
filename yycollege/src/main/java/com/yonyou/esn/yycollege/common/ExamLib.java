/**
 * 
 */
package com.yonyou.esn.yycollege.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.yonyou.esn.yycollege.model.TestPaper;

/**
 * 缓存试题
 * @author jingzz
 * @time 2016年8月23日 下午3:03:31
 * @name yycollege/com.yonyou.esn.yycollege.common.ExamLib
 * @since 2016年8月23日 下午3:03:31
 */
public class ExamLib {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExamLib.class);
	
	private static ExamLib examLib;
	
	/**试题的缓存**/
	private static Map<Integer, TestPaper> subjects = new HashMap<Integer, TestPaper>(60);
	
	private ExamLib() {
		if (examLib != null) {
			try {
				LOG.error("试图对单例对象进行反射攻击的非法操作");
				throw new IllegalAccessException("禁止使用反射对单例对象产生新的实例对象！");
			} catch (IllegalAccessException e) {
				LOG.error("有对单例对象的非法反射操作，但拦截处理出现异常",e);
			}
		}
	}
	
	public static ExamLib getInstance(){
		if (examLib == null) {
			synchronized (ExamLib.class) {
				if (examLib == null) {
					examLib = new ExamLib();
				}
			}
		}
		return examLib;
	}
	
	/**
	 * 获取指定数量的随机题目
	 * @author jingzz
	 * @param number
	 * @return
	 */
	public List<TestPaper> getRandomTitle(int number){
		if (CollectionUtils.isEmpty(subjects)) {
			return new ArrayList<TestPaper>(0);
		}
		
		List<TestPaper> list = new ArrayList<TestPaper>(number);

		//记录题号，避免重复
		Set<Integer> titleNos = new HashSet<Integer>();
		
		int size = subjects.size();
		number = number > size ? size :number < 1 ? 10 : number;
		for (int i = 0; i < number; i++) {
			int index = (int)Math.round(Math.random() * size);
			TestPaper testPaper = subjects.get(index);
			//如果选题为空，重新选题
			//如果已经选择了题目，直接跳过，重新选题
			if (testPaper == null || titleNos.contains(index)) {
				i--;
				continue;
			}
			
			titleNos.add(index);
			list.add(testPaper);
		}
		return list;
	}
	
	/**
	 * 设置题目
	 * @author jingzz
	 * @param lists 题目列表
	 */
	public void setTitle(List<TestPaper> lists){
		if (!CollectionUtils.isEmpty(lists)) {
			for (int i = 0; i < lists.size(); i++) {
				subjects.put(i, lists.get(i));
			}
		}
	}
}

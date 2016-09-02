/**
 * 
 */
package com.yonyou.esn.yycollege.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yonyou.esn.yycollege.mapper.TestPaperMapper;
import com.yonyou.esn.yycollege.model.TestPaper;

/**
 * @author jingzz
 * @time 2016年8月23日 下午3:18:27
 * @name yycollege/com.yonyou.esn.yycollege.common.ConfInstance
 * @since 2016年8月23日 下午3:18:27
 */
@Configuration
public class ConfInstance {
	
	@Autowired
	private TestPaperMapper testPaperMapper;
	
	/**
	 * 在应用启动时注入题库数据
	 * @author jingzz
	 * @return
	 */
	@Bean
	public ExamLib injectData(){
		List<TestPaper> testPapers = testPaperMapper.getAllTitle();
		ExamLib examLib = ExamLib.getInstance();
		examLib.setTitle(testPapers);
		return examLib;
	}
}

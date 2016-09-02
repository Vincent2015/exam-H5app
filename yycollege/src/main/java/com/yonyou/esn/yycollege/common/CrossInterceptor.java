/**
 * 
 */
package com.yonyou.esn.yycollege.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author jingzz
 * @time 2016年8月23日 上午11:45:39
 * @name yycollege/com.yonyou.esn.yycollege.common.CrossInterceptor
 * @since 2016年8月23日 上午11:45:39
 */
public class CrossInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","*");
        response.addHeader("Access-Control-Max-Age","100");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Allow-Credentials","false");
		return super.preHandle(request, response, handler);
	}
	
}

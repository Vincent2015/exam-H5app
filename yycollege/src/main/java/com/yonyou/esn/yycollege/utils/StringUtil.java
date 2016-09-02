package com.yonyou.esn.yycollege.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * 字符串工具类
 * 
 *
 */
public class StringUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static String getCurrentTimeString(){
		return sdf.format(new Date());
	}
	
	/**
	 * 得到时间字符串
	 * @param time
	 * @return
	 * @author   lizhgb
	 * @Date   2016-1-30 下午8:44:28
	 */
	public static String getTimeString(long time){
		return sdf.format(new Date(time));
	}
	
	/**
	 * 限制字符串的长度
	 * @param str
	 * @param length
	 * @return
	 * @author   lizhgb
	 * @Date   2016-2-4 上午10:52:18
	 */
	public static String limit(String str, int length) {
		if (isEmpty(str) || length <= 0 || str.length() < length){
			return str;
		}else{
			return str.substring(0, length - 1);
		}
	}
	
	

	/**
	 * 获得一个不带-符号的uuid
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}


	
	
	public static boolean isEmpty(String s){
		return s == null || "".equals(s);
	}
	
	public static boolean isEmpty(String s, String... more){
		return isEmpty(s) || isEmpty(more);
	}
	
	public static boolean isEmpty(String [] strs){
		for (String str: strs){
			if (isEmpty(str)){
				return true;
			}
		}
		return false;
	}
}

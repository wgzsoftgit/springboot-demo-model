package com.demomodel.query.quartzdemo.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @ClassName CommonUtil
 * @Description 通用工具类
 * @Author simonsfan
 * @Date 2019/1/8
 * Version  1.0
 */
public class CommonUtil {

    /**
     * 获取具体的异常信息
     *
     * @param ex
     * @return
     */
    public static String getExceptionDetail(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            ex.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
    public static void main(String[] args) {
		//使用样例
    	try {
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println( CommonUtil.getExceptionDetail(e));
		}
	}

}

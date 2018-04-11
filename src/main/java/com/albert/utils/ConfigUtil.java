/**
* @{#} ConfigUtil.java Created on 2015-6-23 上午11:16:40
*
* Copyright (c) 2015 by SHUANGYI software.
*/
package com.albert.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigUtil {
	private static Properties p = new Properties();

	// 用静态代码块
	static {
		try {
			String path =null;
			String s = ConfigUtil.class.getResource("ConfigUtil.class").toString();
			System.out.println(s);
			if(s.startsWith("file")){
				path = ConfigUtil.class.getClassLoader().getResource("app.properties").getPath();
			}else{
				path = System.getProperty("user.dir") +
						 "\\resources\\app.properties";
			}
			p.load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void writeProperties(String keyname, String keyvalue) throws Exception {
		try {
			OutputStream fos = new FileOutputStream("config.properties");
			p.setProperty(keyname, keyvalue);
			p.store(fos, "Update '" + keyname + "' value");
		} catch (IOException e) {
			throw new Exception("属性文件更新错误");
		}
	}
	public static String getValue(String propName){
		return p.getProperty(propName);
	}
}

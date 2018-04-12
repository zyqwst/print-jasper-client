/**
 * 
 */
package com.albert.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/** 
* @ClassName: JsonUtil 
* @Description: 
* @author albert
* @date 2018年1月10日 下午4:34:25 
*  
*/
public class JsonUtil {
	
	public static Gson getGson(){
		return  new GsonBuilder()
		        .setPrettyPrinting()
		        .create();
	}
	public static String toJson(Object obj){
		return getGson().toJson(obj);
	}
	public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
	    T object =getGson().fromJson(json, classOfT);
	    return object;
	  }
	public static String readJsonFromStream() throws Exception{
		return readJsonFromFile(getJsonConfig());
	}
	
	/**
	 * 从文件读取json
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	public static String readJsonFromFile(InputStream path) throws Exception{
		StringBuffer str;
		try {
			InputStreamReader is= new InputStreamReader(path);
			BufferedReader bufReader=new BufferedReader(is);
			str = new StringBuffer();
			String line = null;
			while((line=bufReader.readLine())!=null){
				str.append(line);
			}
			bufReader.close();
			is.close();
			return str.toString();
		} catch (FileNotFoundException e) {
			throw new Exception("file not found："+path);
		} catch (IOException e) {
			throw new Exception(e);
		}
	}
	
	public static void writeObject(Object obj)throws Exception{
		writeJson(getGson().toJson(obj));
		
	}
	
	/**
	 * json字符串写入文件
	 * @param path
	 * @param json
	 * @throws Exception
	 */
	public static void writeJson(String json) throws Exception{
		try {
			File file = new File(JsonUtil.class.getResource("/").getPath()+"sbconfig.json");
			FileOutputStream out = new FileOutputStream(file);
			PrintWriter writer = new PrintWriter(out);
			writer.write(json);
			writer.close();
		} catch (FileNotFoundException e) {
			throw new Exception("file not found：");
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		writeJson("abc");
	}
	
	public static InputStream getJsonConfig(){
		return JsonUtil.class.getClassLoader().getResourceAsStream("sbconfig.json");
	}
}

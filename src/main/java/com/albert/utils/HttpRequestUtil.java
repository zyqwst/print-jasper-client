package com.albert.utils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import com.sy.domain.ResponseEntity;

public class HttpRequestUtil {

    /**
     * @param urlAll
     *            :请求接口
     * @param httpArg
     *            :参数
     * @return 返回结果
     * @throws Exception 
     */
    public static Object request(String httpUrl) throws Exception {
        Object entity = null;
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();// 正常访问
            connection.setConnectTimeout(10000);
            connection.connect();
            is = connection.getInputStream();
            ois = new ObjectInputStream(is);
            entity = ois.readObject();
        } catch (SocketException e) {
            throw new Exception("Connection timed out: connect");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally{
        	if(is!=null)
        	 is.close();
        	if(ois!=null)
             ois.close();
        }
        return entity;
    }
    
    public static void main(String[] args) throws Exception {

        String httpUrl = "http://192.168.1.40:9000/medicalinstruments-product/print?method=printdata&&key="+"3d10d015-0edb-4152-885c-add62b79ef53";
        Object en = HttpRequestUtil.request(httpUrl);
        System.out.println(en);
    }

}
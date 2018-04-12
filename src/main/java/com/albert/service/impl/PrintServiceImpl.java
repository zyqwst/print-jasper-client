package com.albert.service.impl;

import java.io.InputStream;

import javax.print.PrintService;

import net.sf.jasperreports.engine.JasperPrint;

import com.albert.service.CommonService;
import com.albert.utils.JasperUtil;
import com.albert.utils.XmlUtil;

public class PrintServiceImpl implements CommonService {
    
    static PrintServiceImpl instance;
    public static PrintServiceImpl instance(){
        if(instance==null) instance=new PrintServiceImpl();
        return instance;
    }
	@Override
	public void print(String xml,String json) throws Exception{
		InputStream in = null;
		try {
			in = XmlUtil.str2InputStream(xml);
			JasperUtil.printJson(in, json);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("打印出错："+e.getMessage());
		} finally{
		    if(in!=null)
		    in.close();
		}
		
	}
	@Override
	public void print(JasperPrint jasper) throws Exception {
		JasperUtil.printJasper(jasper);
	}
	@Override
	public void print(JasperPrint jasper, PrintService print) throws Exception {
		JasperUtil.print(jasper, print);
	}

}
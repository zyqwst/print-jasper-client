package com.albert.service;

import net.sf.jasperreports.engine.JasperPrint;

public interface CommonService {

	public void print(String xml,String json) throws Exception;
	public void print(JasperPrint jasper) throws Exception;
	
	
}
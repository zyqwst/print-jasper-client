package com.albert.model;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;

/** 
* @ClassName: ConfigEntity 
* @Description: 
* @author albert
* @date 2018年1月10日 下午4:31:24 
*  
*/
public class ConfigEntity {
	
	private List<JasperForPrinter> jasperPrinters;
	
	private String appName;

	public ConfigEntity() {
	}
	public ConfigEntity(List<JasperForPrinter> jasperForPrinters){
		this.jasperPrinters= jasperForPrinters;
	}
	public void add(JasperForPrinter j){
		if(jasperPrinters==null)jasperPrinters= new ArrayList<>();
		
		if(jasperPrinters.contains(j)) jasperPrinters.remove(j);
		jasperPrinters.add(j);
	}
	
	/**
	 * 检查打印机
	 * @return
	 */
	public ConfigEntity checkPrinter() {
		if(jasperPrinters==null || jasperPrinters.size()==0) return this;
		PrintService[] services = PrinterJob.lookupPrintServices();
		
		for(JasperForPrinter j : this.jasperPrinters){
			for(PrintService p : services){
				if(p.getName().equals(j.getPrinter())){
					j.setStatus(true);
					break;
				}
				j.setStatus(false);
			}
		}
		return this;
	}
	public List<JasperForPrinter> getJasperPrinters() {
		return jasperPrinters;
	}

	public void setJasperPrinters(List<JasperForPrinter> jasperPrinters) {
		this.jasperPrinters = jasperPrinters;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	
}

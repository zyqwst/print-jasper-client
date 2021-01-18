package com.albert.utils;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 * jasperSoft报表生成 调用工具
 * 
 * @author Albert
 * 
 */
public class JasperUtil {


	/**
	 * 指定的打印机(javabean)
	 * 
	 * @param fileName
	 * @param params
	 * @param conn
	 * @param printer
	 * @throws Exception
	 * @throws JRException
	 */
	public static <T extends Object> void print(String fileName, List<T> list, PrintService printerService)
			throws Exception {
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream inReport = null;
		try {
			inReport = new FileInputStream(fileName);
			jasperReport = JasperCompileManager.compileReport(inReport);
			jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(list));

			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
			printServiceAttributeSet.add(new PrinterName(printerService.getName(), null));

			JRAbstractExporter je = new MyJRPrintServiceExporter();

			// 设置打印内容
			je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			// 设置指定打印机
			je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printerService);
			je.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "模板" + fileName + "打印出错:" + e.getMessage());
			throw new Exception(e);
		}

	}

	public static <T extends Object> void print(String fileName, PrintService printerService) throws Exception {
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream inReport = null;
		try {
			inReport = new FileInputStream(fileName);
			jasperReport = JasperCompileManager.compileReport(inReport);
			jasperPrint = JasperFillManager.fillReport(jasperReport, null);

			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
			printServiceAttributeSet.add(new PrinterName(printerService.getName(), null));

			JRAbstractExporter je = new MyJRPrintServiceExporter();

			// 设置打印内容
			je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			// 设置指定打印机
			je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printerService);
			je.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "模板" + fileName + "打印出错:" + e.getMessage());
			throw new Exception(e);
		}

	}

	/**
	 * 数据库源
	 * 
	 * @param fileName
	 * @param list
	 * @throws JRException
	 */
	public static void preview(String fileName, Map<String, Object> params, Connection conn) throws JRException {
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream inReport = null;
		try {
			inReport = new FileInputStream(fileName);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jasperReport = JasperCompileManager.compileReport(inReport);
			jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		} catch (JRException e) {
			e.printStackTrace();
		}
		// 预览
		// JasperViewer.setDefaultLookAndFeelDecorated(false);
		// JasperViewer.viewReport(jasperPrint, false);

		// 直接打印 true显示打印机设置
		JasperPrintManager.printReport(jasperPrint, false);
	}
	/**
	 * 指定打印机打印
	 * @param jasperPrint
	 * @param printService
	 * @throws Exception
	 */
	public static void print(JasperPrint jasperPrint,PrintService printService) throws Exception{
	    try {
			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
			printServiceAttributeSet.add(new PrinterName(printService.getName(),null));
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
			configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
			configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
			configuration.setDisplayPageDialog(false);
			configuration.setDisplayPrintDialog(false);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	  }
	public static void printJasper(JasperPrint jasperPrint ) throws Exception {
		JasperViewer.viewReport(jasperPrint, false);
	}
	/**
	 * 选择打印机后打印
	 * @param jasperPrint
	 * @throws Exception
	 */
	public static void printSet(JasperPrint jasperPrint ) throws Exception {

		JasperPrintManager.printReport(jasperPrint, true);
	}
	
	
	public static void main(String[] args) throws Exception {
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream inReport = null;
		try {
			inReport = new FileInputStream(JsonUtil.class.getResource("/").getPath()+ "demo.jrxml");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		jasperReport = JasperCompileManager.compileReport(inReport);
		jasperPrint = JasperFillManager.fillReport(jasperReport,new HashMap<>());

		JasperExportManager.exportReportToPdfFile(jasperPrint,"./e.pdf");
	}
}

package com.albert.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

import com.albert.model.EntityBase;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

/**
 * jasperSoft报表生成 调用工具
 * 
 * @author Albert
 * 
 */
public class JasperUtil {

	/**
	 * 打印预览(javabean)
	 * 
	 * @param fileName
	 * @param list
	 * @throws JRException
	 */
	public static <T extends EntityBase> void printJson(InputStream inReport, String json) throws Exception {

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		InputStream is = new ByteArrayInputStream(json.getBytes());
		paramsMap.put("JSON_INPUT_STREAM", is);
		jasperReport = JasperCompileManager.compileReport(inReport);
		jasperPrint = JasperFillManager.fillReport(jasperReport, paramsMap);
		// 预览
		JasperViewer.setDefaultLookAndFeelDecorated(false);
		JasperViewer.viewReport(jasperPrint, false);

		// 直接打印 true显示打印机设置
		// JasperPrintManager.printReport(jasperPrint, false);
	}

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
	 * 指定的打印机
	 * 
	 * @param fileName
	 * @param params
	 * @param conn
	 * @param printer
	 * @throws JRException
	 */
	public static void preview(String fileName, Map<String, Object> params, Connection conn,
			PrintService printerService) throws JRException {
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
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(printerService.getName(), null));

		JRAbstractExporter je = new MyJRPrintServiceExporter();

		// 设置打印内容
		je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		// 设置指定打印机
		je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printerService);
		je.exportReport();

	}
	public static void printJasper(JasperPrint jasperPrint ) throws Exception {

		// 预览
		JasperViewer.viewReport(jasperPrint, false);

		// 直接打印 true显示打印机设置
		// JasperPrintManager.printReport(jasperPrint, false);
	}
	public static void main(String[] args) throws JRException {
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream inReport = null;
		try {
			inReport = new FileInputStream("resources/outorder_blank.jrxml");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("param", "22");
			jasperReport = JasperCompileManager.compileReport(inReport);
			jasperPrint = JasperFillManager.fillReport(jasperReport, params);
		} catch (JRException e) {
			e.printStackTrace();
		}
		// 预览
		JasperViewer.setDefaultLookAndFeelDecorated(false);
		JasperViewer.viewReport(jasperPrint, false);
	}
}

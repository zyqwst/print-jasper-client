/**
 * 
 */
package com.albert.utils;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.albert.model.Column;

/**
 * @author albert
 * 解析XML生成表头
 */
public class TableHeaderProvider {
	/**
	 * 从文档中解析出表头信息
	 * @param doc
	 * @return
	 */
	public static List<Column> getColumns(Document doc) {  
        ArrayList<Column> list = new ArrayList<Column>();  
        Element root = doc.getRootElement();  
        @SuppressWarnings("unchecked")
		Iterator<Element> el = root.elementIterator("head");  
        while (el.hasNext()) {  
            Element e = (Element) el.next();  
            Column c = new Column(e.attributeValue("name"),Integer.parseInt(e.attributeValue("width")));
            list.add(c);  
        }  
        return list;  
    }  
	/**
	 * 获取xml表头文件
	 * @param file
	 * @return
	 */
	public static Document getDocument(String file) {  
        SAXReader reader = new SAXReader();  
        Document document = null;  
        try {  
            document = reader.read(TableHeaderProvider.class.getResource("")+file);  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
        return document;  
    }  
	/**
	 * 设置表格的表头和宽度
	 * @params xmlFile
	 * @param table
	 */
	public static void packTable(String xmlFile,JTable table,DefaultTableModel model){
		
		Document doc = getDocument(xmlFile);
		List<Column> cols = getColumns(doc);
		
		Vector<Object> v = new Vector<Object>();
		for(int i =0 ;i<cols.size();i++){
			v.add(cols.get(i).getName());
		}
		model.setDataVector(new Vector<Object>(), v);
		table.setModel(model);
		table.getTableHeader().setReorderingAllowed(false);
        table.setEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i=0;i<cols.size();i++){
			table.getColumnModel().getColumn(i).setPreferredWidth(cols.get(i).getWidth());
		}
        makeFace(table);
	}
	/**
	 * 可排序的表格
	 * @param xmlFile
	 * @param table
	 * @param model
	 */
	public static void packTableWithSort(String xmlFile,JTable table,DefaultTableModel model){
		packTable(xmlFile, table, model);
		tableSort(table, true);
	}
	/**
	 * 不可排序表格
	 * @param xmlFile
	 * @param table
	 * @param model
	 */
	public static void packTableNoSort(String xmlFile,JTable table,DefaultTableModel model){
        packTable(xmlFile, table, model);
        tableSort(table, false);
    }
	
	/**
	 * 表格是否可排序
	 * @param table
	 * @param sort
	 */
	public static void tableSort(JTable table,Boolean sort){
		table.setAutoCreateRowSorter(sort);//点击表头排序
	}
	/**
	 * 奇偶行颜色不同
	 * @param table
	 */
	public static void makeFace(JTable table) {
		   try {
		    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
		     public Component getTableCellRendererComponent(JTable table,
		       Object value, boolean isSelected, boolean hasFocus,
		       int row, int column) {
		      if (row % 2 == 0)
		       setBackground(Color.white); // 设置奇数行底色
		      else if (row % 2 == 1)
		       setBackground(new Color(240, 255, 255)); // 设置偶数行底色
		      return super.getTableCellRendererComponent(table, value,
		        isSelected, hasFocus, row, column);
		     }
		    };
		    for (int i = 0; i < table.getColumnCount(); i++) {
		     table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
		    }
		   } catch (Exception ex) {
		    ex.printStackTrace();
		   }
		}
	public static void setRowColor(JTable table,final Color color,final int rowNum){
	    try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
             public Component getTableCellRendererComponent(JTable table,
               Object value, boolean isSelected, boolean hasFocus,
               int row, int column) {
               if(row==rowNum){
                   setBackground(color); 
               }
              return super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
             }
            };
            for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
           }
           } catch (Exception ex) {
            ex.printStackTrace();
           }
	}
	public static void main(String[] args) {
		JTable table = new JTable();
		TableHeaderProvider.packTable("table_resources/purcontract.xml", table,null);
	}
}

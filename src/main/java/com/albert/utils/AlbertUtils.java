/**
 * @{#} AlbertUtils.java Created on 2015-10-9 下午1:47:02
 *
 * Copyright (c) 2015 by SHUANGYI software.
 */
package com.albert.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * @author albert
 * @version 1.0
 */
public class AlbertUtils {
    /**
     * 判断字符串是否为合格的浮点数
     * 
     * @param str
     * @return
     */
    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * 保留几位小数
     * @param val
     * @param scale
     * @return
     */
    public static Double round(Double val , int scale){
        double   f   =   val;  
        BigDecimal   b   =   new   BigDecimal(f);  
        double   f1   =   b.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();  
        return f1;
    }
    
    /**
     * 显示splash
     * 
     * @return
     * @throws MalformedURLException
     */
    public static JWindow createSplash() throws MalformedURLException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 得到屏幕的尺寸
        JWindow window = new JWindow();
        JLabel label = new JLabel(PictureUtil.getPicture("splash.jpg"));
        window.getContentPane().add(label);
        window.setBounds((screenSize.width - 600) / 2,
                (screenSize.height - 400) / 2, 600, 300);

        window.setVisible(true);
        return window;
    }

    /*************************************************
     * 函数: centerWindow 函数描述:窗口置中
     *************************************************/
    public static void centerWindow(Container window) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = window.getSize().width;
        int h = window.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        window.setLocation(x, y - 20);
    }

    /**
     * 导出excel 当前页
     * 
     * @param table
     * @param file
     * @throws IOException
     */
    public static void exportTable(JTable table, File file) throws IOException {
        TableModel model = table.getModel();
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
        for (int i = 1; i < model.getColumnCount(); i++) {
            bWriter.write(model.getColumnName(i));
            bWriter.write("\t");
        }
        bWriter.newLine();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 1; j < model.getColumnCount(); j++) {
                bWriter.write(model.getValueAt(i, j).toString());
                bWriter.write("\t");
            }
            bWriter.newLine();
        }
        bWriter.close();
        System.out.println("write out to: " + file);
    }

    /**
     * 字符串首字母大写
     * 
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder())
                    .append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
    }
    /**
     * 生成处方号
     * @return
     */
    public static String generateHisNo(){
        StringBuilder sb = new StringBuilder("XZ");
        String time = new Date().getTime()+"";
        sb.append(time.substring(3, time.length()));
        return sb.toString();
    }
    /**
     * 修改表头字体颜色
     * @param table
     * @param columnIndex
     * @param c
     */
    public static void setTableHeaderColor(JTable table, int columnIndex, final Color c)
    {
        TableColumn column = table.getTableHeader().getColumnModel().getColumn(columnIndex);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)  
            {
                JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                comp.setForeground(c);
                return comp;
            }
        };
        column.setHeaderRenderer(cellRenderer);
    }
    /**
     * 获取md5摘要
     * @param str
     * @return
     */
    public static String md5(String str){  
        String pwd = null;  
        try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(str.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            pwd = new BigInteger(1, md.digest()).toString(16);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return pwd;  
    }  
    /**
     * @param val
     * @return
     */
    public static Double nvl(Double val){
        if(val==null) 
            return 0d;
        return val;
    }
    public static Long nvl(Long val){
        if(val==null) 
            return 0l;
        return val;
    }
    
    public static String addIn(List<Object> list){
        if(list==null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(" ('"+list.get(0)+"'");
        for(int i=1;i<list.size();i++){
            sb.append(",'"+list.get(i)+"'");
        }
        sb.append(")");
        return sb.toString();
    }
    public static String addInString(List<String> list){
        if(list==null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(" ('"+list.get(0)+"'");
        for(int i=1;i<list.size();i++){
            sb.append(",'"+list.get(i)+"'");
        }
        sb.append(")");
        return sb.toString();
    }
    public static String addInLong(List<Long> list){
        if(list==null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(" ('"+list.get(0)+"'");
        for(int i=1;i<list.size();i++){
            sb.append(",'"+list.get(i)+"'");
        }
        sb.append(")");
        return sb.toString();
    }
    public static String addIn(Object[] o){
        if(o==null || o.length==0) return "";
        StringBuilder sb = new StringBuilder(" ('"+o[0]+"'");
        for(int i=1;i<o.length;i++){
            sb.append(",'"+o[i]+"'");
        }
        sb.append(")");
        return sb.toString();
    }
    public static Boolean isEmpty(List list){
    	if(list==null || list.size()==0){
    		return true;
    	}
    	return false;
    }
    public static Boolean isNotEmpty(List list){
    	if(list==null || list.size()==0){
    		return false;
    	}
    	return true;
    }
    
    public static Boolean isImage(String fileName){
    	if(fileName.endsWith("jpg") ) return true;
    	if(fileName.endsWith("JPG") ) return true;
    	if(fileName.endsWith("jpeg") ) return true;
    	if(fileName.endsWith("JPEG") ) return true;
    	if(fileName.endsWith("gif") ) return true;
    	if(fileName.endsWith("GIF") ) return true;
    	if(fileName.endsWith("bmp") ) return true;
    	if(fileName.endsWith("BMP") ) return true;
    	if(fileName.endsWith("png") ) return true;
    	if(fileName.endsWith("PNG") ) return true;
    	return false;
    }
    
    
}

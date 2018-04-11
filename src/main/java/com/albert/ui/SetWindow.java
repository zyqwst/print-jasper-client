/**
* @{#} SetWindow.java Created on 2017-8-28 上午9:16:13
*
* Copyright (c) 2017 by SHUANGYI software.
*/
package com.albert.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.albert.utils.SyTableModel;
import com.albert.utils.TableHeaderProvider;

public class SetWindow {

    private JFrame frame;
    private JTable table;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SetWindow window = new SetWindow();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public SetWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    		
        frame = new JFrame();
        Toolkit tk=Toolkit.getDefaultToolkit() ;
        Image image=tk.createImage("resources/image/sylogo.png"); 
        frame.setIconImage(image); 
        frame.setTitle("报表打印机设置");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 得到屏幕的尺寸
        frame.setBounds(0,0,screenSize.width/2,screenSize.height/2);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JScrollPane scrollPane = new JScrollPane();
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );
        
        table = new JTable();
        TableHeaderProvider.packTableNoSort("print_dialog.xml", table, new SyTableModel());
		scrollPane.setViewportView(table);
		frame.getContentPane().setLayout(groupLayout);
    }

}

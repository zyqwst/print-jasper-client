/**
* @{#} SetWindow.java Created on 2017-8-28 上午9:16:13
*
* Copyright (c) 2017 by SHUANGYI software.
*/
package com.albert.ui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import com.albert.AppContext;
import com.albert.model.JasperForPrinter;
import com.albert.utils.SyTableModel;
import com.albert.utils.TableHeaderProvider;

public class SetWindow {

    private JFrame frame;
    private JTable table;
    
    private JasperForPrinter selected;
    private AppContext context;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new SetWindow(AppContext.INSTANCE()).visible(true);;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SetWindow(AppContext context) {
    		this.context = context;
        initialize();
    }
    public void visible(boolean b) {
    		frame.setVisible(b);
    }
    private void initialize() {
    		
        frame = new JFrame();
        Toolkit tk=Toolkit.getDefaultToolkit() ;
        Image image=tk.createImage("resources/image/sylogo.png"); 
        frame.setIconImage(image); 
        frame.setTitle("报表打印机设置");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0,900,540);
        frame.setLocationRelativeTo(null);
        
        JScrollPane scrollPane = new JScrollPane();
        
        JLabel label = new JLabel("报    表：");
        
        final JLabel name_lb = new JLabel("");
        
        JLabel label_2 = new JLabel("打 印 机：");
        
        JLabel label_3 = new JLabel("打印预览：");
        
        JButton btn_delete = new JButton("删除");
        btn_delete.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
		        		try {
		        			if(selected==null) throw new Exception("请选择要删除的数据");
						context.deleteJasper(selected);
						selected = null;
						fillTable();
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame, e1.getMessage());
					}
	        	}
        });
        
        
        
        final JComboBox printer_cb = new JComboBox();
        
        for(String s : AppContext.INSTANCE().getAllPrinter()) {
        	printer_cb.addItem(s);
        }
        
        final JComboBox priview_cb = new JComboBox();
        priview_cb.addItem("是");
        priview_cb.addItem("否");
        JButton btn_modify = new JButton("修改");
        btn_modify.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		try {
	        			if(selected==null) throw new Exception("请选择要修改的数据");
					selected.setPreview(priview_cb.getSelectedItem().equals("是")?true:false);
					selected.setPrinter(printer_cb.getSelectedItem().toString());
					context.saveConfig();
					fillTable();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
	        	}
        });
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
        			.addGap(4)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(label)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(name_lb, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(btn_delete, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btn_modify, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(label_2)
        						.addComponent(label_3))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(priview_cb, 0, 197, Short.MAX_VALUE)
        						.addComponent(printer_cb, 0, 199, Short.MAX_VALUE))))
        			.addContainerGap())
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(label)
        				.addComponent(name_lb))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(label_3)
        				.addComponent(priview_cb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(label_2)
        				.addComponent(printer_cb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btn_delete)
        				.addComponent(btn_modify))
        			.addContainerGap(382, Short.MAX_VALUE))
        		.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
        );
        
        table = new JTable();
        TableHeaderProvider.packTableNoSort("print_dialog.xml", table, new SyTableModel());
		scrollPane.setViewportView(table);
		frame.getContentPane().setLayout(groupLayout);
		fillTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				int row = table.getSelectedRow(); //获得行位置 
        			table.setRowSelectionInterval(row, row);
				selected = context.getConfig().getJasperPrinters().get(table.convertRowIndexToModel(row));
				
				name_lb.setText(selected.getJasper());
				printer_cb.setSelectedItem(selected.getPrinter());
				priview_cb.setSelectedItem(selected.getPreview()?"是":"否");
			}
		});
    }
    private void fillTable(){
    	DefaultTableModel tableModel = (DefaultTableModel) table
    	        .getModel();
    	        tableModel.setRowCount(0);
    	        
    	        int i =1;
    	        for(JasperForPrinter s : context.getConfig().getJasperPrinters()){
    	          String[] arr=new String[5];
    	          arr[0]=(i)+"";
    	          arr[1]=s.getJasper();
    	          arr[2]=s.getPrinter();
    	          arr[3]=s.getPreview()?"是":"否";
    	          arr[4]=s.getStatus()?"打印机可用":"打印机不可用";
    	          tableModel.addRow(arr);
    	          i++;
    	        }
    	        table.updateUI();
    }
}

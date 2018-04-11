/**
* @{#} SyTableModel.java Created on 2015-10-12 下午4:12:12
*
* Copyright (c) 2015 by SHUANGYI software.
*/
package com.albert.utils;

import javax.swing.table.DefaultTableModel;

public class SyTableModel extends DefaultTableModel {
    /**
     * 
     */
    private static final long serialVersionUID = -2015945862684402038L;

    public SyTableModel()
    {
    super();
    }

    public boolean isCellEditable(int row, int column)
    {
        return false;//父类的方法里面是 return true的，所以就可以编辑了~~~
    }
   
}

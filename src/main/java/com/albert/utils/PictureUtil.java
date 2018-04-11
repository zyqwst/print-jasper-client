package com.albert.utils;

import javax.swing.ImageIcon;

public class PictureUtil {

	public static ImageIcon getPicture(String name) {
		ImageIcon icon = new ImageIcon("static/img/" + name);
		return icon;
	}
	
}

package com.splus.subservice;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ListenerClass implements ServletContextListener {
	
	private static String baseIp;
	private static String proPicDir;
	private static String proPicPath;
	private static String imageDir;
	private static String imagePath;

	public static String getBaseIp() {
		return baseIp;
	}

	public static String getProPicDir() {
		return proPicDir;
	}

	public static String getProPicPath() {
		return proPicPath;
	}

	public static String getImageDir() {
		return imageDir;
	}

	public static String getImagePath() {
		return imagePath;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		ServletContext context = arg0.getServletContext();
		baseIp = context.getInitParameter("baseIp");
		proPicDir = context.getInitParameter("proPicDir");
		proPicPath = context.getInitParameter("proPicPath");
		imageDir = context.getInitParameter("imageDir");
		imagePath = context.getInitParameter("imagePath");
		
	}
	
	public static String getProPicLink(String userName) {
		
		File picFile = new File(getProPicDir()+userName+".jpg");
		if(picFile.exists())
			return getProPicPath()+userName+".jpg";
		return "";
	}
	
	public static String getRegnImageLink(String regnId) {
		
		File picFile = new File(getImageDir()+regnId+".jpg");
		if(picFile.exists())
			return getImagePath()+regnId+".jpg";
		return "";
	}

}

package com.yoloapps.autonitrotyper;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenCapturer 
{
	private int x=390;
	private int y=595;
	private int w=515;
	private int h=120;
	private Robot robot;
	
	public ScreenCapturer()
	{
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage capture(int x, int y, int x2, int y2, String path)
	{
		BufferedImage image = robot.createScreenCapture(new Rectangle( x, y, x2-x, y2-y));
	    File file = new File(path);
	    try {
	    	if(file.exists())
	    	while(!file.renameTo(file));
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return image;
	}
	
	public BufferedImage capture(int x, int y, String path)
	{
		BufferedImage image = robot.createScreenCapture(new Rectangle( x, y, w, h));
	    File file = new File(path);
	    try {
	    	while(!file.renameTo(file));
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return image;
	}
	
	public BufferedImage capture(String path)
	{
		BufferedImage image = robot.createScreenCapture(new Rectangle( x, y, w, h));
	    File file = new File(path);
	    try {
	    	while(!file.renameTo(file));
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return image;
	}
}

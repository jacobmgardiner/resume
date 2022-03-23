package com.yoloapps.autonitrotyper;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class Main 
{

	private static String imagePath;
	private static String text;
	private static long speed=1;
	private static Robot robot;
	private static BufferedImage lastImage;
	private static BufferedImage curImage;
	private static int x=390;
	private static int y=595;
	private static long startTime;
	private static long endTime;
	private static int loops=35;
	private static Scanner scanner;
	private static int x2;
	private static int y2;
	private static ScreenCapturer sc;
	private static Ocr ocr;
	private static AutoTyper at;

	public static void main(String[] args) 
	{
		//System.out.println("Move the mouse to the top left corner of the text area and type \"start\".");
		System.out.println("Type \"help\" for a list of commands");
		//System.out.println("To stop the program, ");
		
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		sc=new ScreenCapturer();
		ocr=new Ocr();
		at=new AutoTyper();
		
		imagePath=new File("").getAbsolutePath()+"\\screencapture.jpg";
		
		waitForUserInput();
	}

	private static void start() 
	{
		shiftFocus();
		
		int n=0;
		while(n<=loops)
		{
			System.out.println("\nloop "+n+"/"+loops);
			
			startTime=System.currentTimeMillis();
			curImage=sc.capture(x,y,x2,y2,imagePath);
			endTime=System.currentTimeMillis();
			System.out.println("image captured in: "+(endTime-startTime));
			
			startTime=System.currentTimeMillis();
			if(areSame())
			{
				endTime=System.currentTimeMillis();
				System.out.println("similarity checked in: "+(endTime-startTime));
				
				startTime=System.currentTimeMillis();
//				at.typeAllCharacters();
//				at.waitForFinish();
				at.typeAllCharacters();
				endTime=System.currentTimeMillis();
				System.out.println("typed all in: "+(endTime-startTime));
			}
			else
			{
				endTime=System.currentTimeMillis();
				System.out.println("unsimilarity checked in: "+(endTime-startTime));
				
				startTime=System.currentTimeMillis();
				text=ocr.getTextFromImage(imagePath);
				System.out.println("TEXT: \""+text+"\"");
				endTime=System.currentTimeMillis();
				System.out.println("text extracted in: "+(endTime-startTime));
				
				startTime=System.currentTimeMillis();
//				at.typeText(speed, text);
//				at.waitForFinish();
				at.typeText(text);
				endTime=System.currentTimeMillis();
				System.out.println("typed all in: "+(endTime-startTime));
			}
			
			n++;
		}
	}
	
	private static void shiftFocus() 
	{
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_TAB);
	}

	public static void waitForUserInput()
	{
		System.out.println("Enter a command.");
		scanner=new Scanner(System.in);
		String command=scanner.nextLine();
		if(command.equals("start")||command.equals("s"))
		{
			start();
			waitForUserInput();
		}
		else if(command.equals("coord1")||command.equals("c1"))
		{
			System.out.println("Move the mouse to the top left corner of the text area and type \"done\" to set the coordinates of the top left corner of the text area.");
			scanner.nextLine();
			x = MouseInfo.getPointerInfo().getLocation().x;
			y = MouseInfo.getPointerInfo().getLocation().y;
			System.out.println("Coordinates set to "+x+", "+y+".");
			waitForUserInput();
		}
		else if(command.equals("coord2")||command.equals("c2"))
		{
			System.out.println("Move the mouse to the top left corner of the text area and type \"done\" to set the coordinates of the bottom right corner of the text area.");
			scanner.nextLine();
			x2 = MouseInfo.getPointerInfo().getLocation().x;
			y2 = MouseInfo.getPointerInfo().getLocation().y;
			System.out.println("Coordinates set to "+x2+", "+y2+".");
			waitForUserInput();
		}
		else if(command.equals("loops")||command.equals("l"))
		{
			System.out.println("Enter the number of times auto typer should type.");
			int i=scanner.nextInt();
			loops=i;
			waitForUserInput();
		}
		else if(command.equals("screenCapture")||command.equals("sc"))
		{
			shiftFocus();
			sc.capture(x,y,x2,y2,imagePath);
			System.out.println("screen capture saved to "+imagePath+".");
			waitForUserInput();
		}
		else if(command.equals("readText")||command.equals("rt"))
		{
			shiftFocus();
			sc.capture(x,y,x2,y2,imagePath);
			String text=ocr.getTextFromImage(imagePath);
			System.out.println("Text: "+text);
			waitForUserInput();
		}
		else if(command.equals("type")||command.equals("t"))
		{
			System.out.println("Enter what you want to be typed.");
			String text=scanner.nextLine();
			shiftFocus();
			at.typeText(text);
			System.out.println("Finished typing.");
			waitForUserInput();
		}
		else if(command.equals("setImagePath")||command.equals("sip"))
		{
			System.out.println("Enter the full path of the desired save location of screen captures.");
			String text=scanner.nextLine();
			imagePath=text;
			System.out.println("Updated the save location to "+imagePath+".");
			waitForUserInput();
		}
		else if(command.equals("help")||command.equals("h"))
		{
			System.out.println("Here are some commands:");
			System.out.println("	start/s- starts the auto typer and runs it for the set amount of loops (default # of loops is 35)");
			System.out.println("	loops/l- set the number of times auto typer should type (default is 35)");
			System.out.println("	coord1/c1- set the coordinates of the top left corner of the screen capture rectangle");
			System.out.println("	coord2/c2- set the coordinates of the bottom right corner of the screen capture rectangle");
			System.out.println("	screenCapture/sc- take a screen shot within the set coordinates");
			System.out.println("	type/t- automatically types the text entered");
			System.out.println("	readText/rt- takes a screenshot and prints any text found");
			System.out.println("	setImagePath/sip- set the save location of the screen captures");
			waitForUserInput();
		}
		else
		{
			System.out.println("Unknown command: "+command);
			waitForUserInput();
		}
	}

	private static boolean areSame() 
	{
		boolean same=true;
		if(lastImage!=null)
		{
			for ( int x = 0; x < lastImage.getWidth(); x++ ) {
		        for ( int y = 0; y < lastImage.getHeight(); y++ ){
		            int p1 = lastImage.getRGB( x, y );
		            int p2 = curImage.getRGB( x, y );
		            if ( p1 != p2 ) 
		            {
		                same=false;
		            }
		        }
		    }
		}
		else
		{
			same=false;
		}
		lastImage=curImage;
		if(same)
		{
			System.out.println("SAME!!!");
		}
		return same;
	}

}

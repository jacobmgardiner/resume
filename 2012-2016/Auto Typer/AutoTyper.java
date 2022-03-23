package com.yoloapps.autonitrotyper;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class AutoTyper 
{
	private static Robot robot;
	private Timer t=new Timer();	
	private int n=0;
	private long speed=50;
	private boolean done=true;
	private TimerTask task;
	
	public AutoTyper()
	{
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void typeText(long speed, String text)
	{
		done=false;
		n=0;
		text=" "+text;
		final String ftext=text;
		
//		robot.keyPress(KeyEvent.VK_ALT);
//		robot.keyPress(KeyEvent.VK_TAB);
//		robot.keyRelease(KeyEvent.VK_ALT);
//		robot.keyRelease(KeyEvent.VK_TAB);
		
		t=null;
		t=new Timer();
		task=new TimerTask()
		{

			@Override
			public void run()
			{
				if(n<ftext.length())
				{
					typeCharacter(ftext.charAt(n));
				}
				else
				{
					stop();
				}
				n++;
			}
		};
		t.schedule(task, 0, speed);
	}
	
	public void stop()
	{
		done=true;
		task.cancel();
		t.cancel();
		t.purge();
		n=0;
	}
	
	public void typeText(String text)
	{
//		text=" "+text;

		n=0;
		while(n<text.length())
		{
			typeCharacter(text.charAt(n));
			n++;
		}
		n=0;
	}
	
	public static void typeCharacter(char character)
	{
		switch (character) {
        case 'a': robot.keyPress(KeyEvent.VK_A);robot.keyRelease(KeyEvent.VK_A); break;
        case 'b': robot.keyPress(KeyEvent.VK_B);robot.keyRelease(KeyEvent.VK_B); break;
        case 'c': robot.keyPress(KeyEvent.VK_C);robot.keyRelease(KeyEvent.VK_C); break;
        case 'd': robot.keyPress(KeyEvent.VK_D);robot.keyRelease(KeyEvent.VK_D); break;
        case 'e': robot.keyPress(KeyEvent.VK_E);robot.keyRelease(KeyEvent.VK_E); break;
        case 'f': robot.keyPress(KeyEvent.VK_F);robot.keyRelease(KeyEvent.VK_F); break;
        case 'g': robot.keyPress(KeyEvent.VK_G);robot.keyRelease(KeyEvent.VK_G); break;
        case 'h': robot.keyPress(KeyEvent.VK_H);robot.keyRelease(KeyEvent.VK_H); break;
        case 'i': robot.keyPress(KeyEvent.VK_I);robot.keyRelease(KeyEvent.VK_I); break;
        case 'j': robot.keyPress(KeyEvent.VK_J);robot.keyRelease(KeyEvent.VK_J); break;
        case 'k': robot.keyPress(KeyEvent.VK_K);robot.keyRelease(KeyEvent.VK_K); break;
        case 'l': robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_L); break;
        case 'm': robot.keyPress(KeyEvent.VK_M);robot.keyRelease(KeyEvent.VK_M); break;
        case 'n': robot.keyPress(KeyEvent.VK_N);robot.keyRelease(KeyEvent.VK_N); break;
        case 'o': robot.keyPress(KeyEvent.VK_O);robot.keyRelease(KeyEvent.VK_O); break;
        case 'p': robot.keyPress(KeyEvent.VK_P);robot.keyRelease(KeyEvent.VK_P); break;
        case 'q': robot.keyPress(KeyEvent.VK_Q);robot.keyRelease(KeyEvent.VK_Q); break;
        case 'r': robot.keyPress(KeyEvent.VK_R);robot.keyRelease(KeyEvent.VK_R); break;
        case 's': robot.keyPress(KeyEvent.VK_S);robot.keyRelease(KeyEvent.VK_S); break;
        case 't': robot.keyPress(KeyEvent.VK_T);robot.keyRelease(KeyEvent.VK_T); break;
        case 'u': robot.keyPress(KeyEvent.VK_U);robot.keyRelease(KeyEvent.VK_U); break;
        case 'v': robot.keyPress(KeyEvent.VK_V);robot.keyRelease(KeyEvent.VK_V); break;
        case 'w': robot.keyPress(KeyEvent.VK_W);robot.keyRelease(KeyEvent.VK_W); break;
        case 'x': robot.keyPress(KeyEvent.VK_X);robot.keyRelease(KeyEvent.VK_X); break;
        case 'y': robot.keyPress(KeyEvent.VK_Y);robot.keyRelease(KeyEvent.VK_Y); break;
        case 'z': robot.keyPress(KeyEvent.VK_Z);robot.keyRelease(KeyEvent.VK_Z); break;
        case 'A': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_A);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_A); break;
        case 'B': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_B);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_B); break;
        case 'C': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_C);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_C); break;
        case 'D': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_D);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_D); break;
        case 'E': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_E);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_E); break;
        case 'F': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_F);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_F); break;
        case 'G': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_G);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_G); break;
        case 'H': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_H);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_H); break;
        case 'I': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_I);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_I); break;
        case 'J': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_J);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_J); break;
        case 'K': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_K);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_K); break;
        case 'L': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_L); break;
        case 'M': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_M);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_M); break;
        case 'N': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_N);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_N); break;
        case 'O': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_O);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_O); break;
        case 'P': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_P);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_P); break;
        case 'Q': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_Q);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_Q); break;
        case 'R': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_R);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_R); break;
        case 'S': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_S);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_S); break;
        case 'T': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_T);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_T); break;
        case 'U': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_U);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_U); break;
        case 'V': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_V);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_V); break;
        case 'W': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_W);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_W); break;
        case 'X': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_X);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_X); break;
        case 'Y': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_Y);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_Y); break;
        case 'Z': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_Z);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_Z); break;
        case '`': robot.keyPress(KeyEvent.VK_BACK_QUOTE);robot.keyRelease(KeyEvent.VK_BACK_QUOTE); break;
        case '0': robot.keyPress(KeyEvent.VK_0);robot.keyRelease(KeyEvent.VK_0); break;
        case '1': robot.keyPress(KeyEvent.VK_1);robot.keyRelease(KeyEvent.VK_1); break;
        case '2': robot.keyPress(KeyEvent.VK_2);robot.keyRelease(KeyEvent.VK_2); break;
        case '3': robot.keyPress(KeyEvent.VK_3);robot.keyRelease(KeyEvent.VK_3); break;
        case '4': robot.keyPress(KeyEvent.VK_4);robot.keyRelease(KeyEvent.VK_4); break;
        case '5': robot.keyPress(KeyEvent.VK_5);robot.keyRelease(KeyEvent.VK_5); break;
        case '6': robot.keyPress(KeyEvent.VK_6);robot.keyRelease(KeyEvent.VK_6); break;
        case '7': robot.keyPress(KeyEvent.VK_7);robot.keyRelease(KeyEvent.VK_7); break;
        case '8': robot.keyPress(KeyEvent.VK_8);robot.keyRelease(KeyEvent.VK_8); break;
        case '9': robot.keyPress(KeyEvent.VK_9);robot.keyRelease(KeyEvent.VK_9); break;
        case '-': robot.keyPress(KeyEvent.VK_MINUS);robot.keyRelease(KeyEvent.VK_MINUS); break;
        case '=': robot.keyPress(KeyEvent.VK_EQUALS);robot.keyRelease(KeyEvent.VK_EQUALS); break;
        case '~': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_BACK_QUOTE);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_BACK_QUOTE); break;
        case '!': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_1);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_1); break;
        case '@': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_2);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_2); break;
        case '#': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_3);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_3); break;
        case '$': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_4);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_4); break;
        case '%': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_5);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_5); break;
        case '^': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_6);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_6); break;
        case '&': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_7);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_7); break;
        case '*': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_8);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_8); break;
        case '(': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_9);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_9); break;
        case ')': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_0);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_0); break;
        case '_': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_MINUS);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_MINUS); break;
        case '+': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_EQUALS);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_EQUALS); break;
        case '\t': robot.keyPress(KeyEvent.VK_TAB);robot.keyRelease(KeyEvent.VK_TAB); break;
        case '\n': robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER); break;
        case '[': robot.keyPress(KeyEvent.VK_OPEN_BRACKET);robot.keyRelease(KeyEvent.VK_OPEN_BRACKET); break;
        case ']': robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET); break;
        case '\\': robot.keyPress(KeyEvent.VK_BACK_SLASH);robot.keyRelease(KeyEvent.VK_BACK_SLASH); break;
        case '{': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_OPEN_BRACKET);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_OPEN_BRACKET); break;
        case '}': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET); break;
        case '|': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_BACK_SLASH);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_BACK_SLASH); break;
        case ';': robot.keyPress(KeyEvent.VK_SEMICOLON);robot.keyRelease(KeyEvent.VK_SEMICOLON); break;
        case ':': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_SEMICOLON);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_SEMICOLON); break;
        case '\'': robot.keyPress(KeyEvent.VK_QUOTE);robot.keyRelease(KeyEvent.VK_QUOTE); break;
        //case '"': robot.keyPress(KeyEvent.VK_QUOTEDBL);robot.keyRelease(KeyEvent.VK_QUOTEDBL); break;
        case ',': robot.keyPress(KeyEvent.VK_COMMA);robot.keyRelease(KeyEvent.VK_COMMA); break;
        case '<': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_COMMA);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_COMMA); break;
        case '.': robot.keyPress(KeyEvent.VK_PERIOD);robot.keyRelease(KeyEvent.VK_PERIOD); break;
        case '>': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_PERIOD);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_PERIOD); break;
        case '/': robot.keyPress(KeyEvent.VK_SLASH);robot.keyRelease(KeyEvent.VK_SLASH); break;
        case '?': robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_SLASH);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_SLASH); break;
        case ' ': robot.keyPress(KeyEvent.VK_SPACE);robot.keyRelease(KeyEvent.VK_SPACE); break;
        default:
            //throw new IllegalArgumentException("Cannot type character " + character);
        	System.out.println("unknown symbol!!"+character);
        	
        }
	}

	public void waitForFinish()
	{
		while(!done)
		{
			
		}
		
	}
	
	public void typeAllCharacters()
	{
		robot.keyPress(KeyEvent.VK_A);robot.keyRelease(KeyEvent.VK_A);
	    robot.keyPress(KeyEvent.VK_B);robot.keyRelease(KeyEvent.VK_B);
	    robot.keyPress(KeyEvent.VK_C);robot.keyRelease(KeyEvent.VK_C);
	    robot.keyPress(KeyEvent.VK_D);robot.keyRelease(KeyEvent.VK_D);
	    robot.keyPress(KeyEvent.VK_E);robot.keyRelease(KeyEvent.VK_E);
	    robot.keyPress(KeyEvent.VK_F);robot.keyRelease(KeyEvent.VK_F);
	    robot.keyPress(KeyEvent.VK_G);robot.keyRelease(KeyEvent.VK_G);
	    robot.keyPress(KeyEvent.VK_H);robot.keyRelease(KeyEvent.VK_H);
	    robot.keyPress(KeyEvent.VK_I);robot.keyRelease(KeyEvent.VK_I);
	    robot.keyPress(KeyEvent.VK_J);robot.keyRelease(KeyEvent.VK_J);
	    robot.keyPress(KeyEvent.VK_K);robot.keyRelease(KeyEvent.VK_K);
	    robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_L);
	    robot.keyPress(KeyEvent.VK_M);robot.keyRelease(KeyEvent.VK_M);
	    robot.keyPress(KeyEvent.VK_N);robot.keyRelease(KeyEvent.VK_N);
	    robot.keyPress(KeyEvent.VK_O);robot.keyRelease(KeyEvent.VK_O);
	    robot.keyPress(KeyEvent.VK_P);robot.keyRelease(KeyEvent.VK_P);
	    robot.keyPress(KeyEvent.VK_Q);robot.keyRelease(KeyEvent.VK_Q);
	    robot.keyPress(KeyEvent.VK_R);robot.keyRelease(KeyEvent.VK_R);
	    robot.keyPress(KeyEvent.VK_S);robot.keyRelease(KeyEvent.VK_S);
	    robot.keyPress(KeyEvent.VK_T);robot.keyRelease(KeyEvent.VK_T);
	    robot.keyPress(KeyEvent.VK_U);robot.keyRelease(KeyEvent.VK_U);
	    robot.keyPress(KeyEvent.VK_V);robot.keyRelease(KeyEvent.VK_V);
	    robot.keyPress(KeyEvent.VK_W);robot.keyRelease(KeyEvent.VK_W);
	    robot.keyPress(KeyEvent.VK_X);robot.keyRelease(KeyEvent.VK_X);
	    robot.keyPress(KeyEvent.VK_Y);robot.keyRelease(KeyEvent.VK_Y);
	    robot.keyPress(KeyEvent.VK_Z);robot.keyRelease(KeyEvent.VK_Z);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_A);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_A);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_B);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_B);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_C);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_C);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_D);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_D);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_E);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_E);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_F);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_F);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_G);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_G);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_H);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_H);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_I);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_I);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_J);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_J);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_K);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_K);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_L);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_M);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_M);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_N);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_N);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_O);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_O);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_P);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_P);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_Q);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_Q);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_R);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_R);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_S);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_S);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_T);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_T);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_U);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_U);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_V);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_V);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_W);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_W);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_X);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_X);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_Y);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_Y);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_Z);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_Z);
	    robot.keyPress(KeyEvent.VK_BACK_QUOTE);robot.keyRelease(KeyEvent.VK_BACK_QUOTE);
	    robot.keyPress(KeyEvent.VK_0);robot.keyRelease(KeyEvent.VK_0);
	    robot.keyPress(KeyEvent.VK_1);robot.keyRelease(KeyEvent.VK_1);
	    robot.keyPress(KeyEvent.VK_2);robot.keyRelease(KeyEvent.VK_2);
	    robot.keyPress(KeyEvent.VK_3);robot.keyRelease(KeyEvent.VK_3);
	    robot.keyPress(KeyEvent.VK_4);robot.keyRelease(KeyEvent.VK_4);
	    robot.keyPress(KeyEvent.VK_5);robot.keyRelease(KeyEvent.VK_5);
	    robot.keyPress(KeyEvent.VK_6);robot.keyRelease(KeyEvent.VK_6);
	    robot.keyPress(KeyEvent.VK_7);robot.keyRelease(KeyEvent.VK_7);
	    robot.keyPress(KeyEvent.VK_8);robot.keyRelease(KeyEvent.VK_8);
	    robot.keyPress(KeyEvent.VK_9);robot.keyRelease(KeyEvent.VK_9);
	    robot.keyPress(KeyEvent.VK_MINUS);robot.keyRelease(KeyEvent.VK_MINUS);
	    robot.keyPress(KeyEvent.VK_EQUALS);robot.keyRelease(KeyEvent.VK_EQUALS);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_BACK_QUOTE);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_BACK_QUOTE);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_1);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_1);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_2);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_2);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_3);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_3);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_4);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_4);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_5);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_5);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_6);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_6);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_7);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_7);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_8);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_8);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_9);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_9);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_0);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_0);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_MINUS);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_MINUS);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_EQUALS);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_EQUALS);
	    robot.keyPress(KeyEvent.VK_TAB);robot.keyRelease(KeyEvent.VK_TAB);
	    robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER);
	    robot.keyPress(KeyEvent.VK_OPEN_BRACKET);robot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
	    robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
	    robot.keyPress(KeyEvent.VK_BACK_SLASH);robot.keyRelease(KeyEvent.VK_BACK_SLASH);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_OPEN_BRACKET);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_BACK_SLASH);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_BACK_SLASH);
	    robot.keyPress(KeyEvent.VK_SEMICOLON);robot.keyRelease(KeyEvent.VK_SEMICOLON);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_SEMICOLON);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_SEMICOLON);
	    robot.keyPress(KeyEvent.VK_QUOTE);robot.keyRelease(KeyEvent.VK_QUOTE);
	    //robot.keyPress(KeyEvent.VK_QUOTEDBL);robot.keyRelease(KeyEvent.VK_QUOTEDBL);
	    robot.keyPress(KeyEvent.VK_COMMA);robot.keyRelease(KeyEvent.VK_COMMA);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_COMMA);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_COMMA);
	    robot.keyPress(KeyEvent.VK_PERIOD);robot.keyRelease(KeyEvent.VK_PERIOD);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_PERIOD);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_PERIOD);
	    robot.keyPress(KeyEvent.VK_SLASH);robot.keyRelease(KeyEvent.VK_SLASH);
	    robot.keyPress(KeyEvent.VK_SHIFT); robot.keyPress(KeyEvent.VK_SLASH);robot.keyRelease(KeyEvent.VK_SHIFT); robot.keyRelease(KeyEvent.VK_SLASH);
	    robot.keyPress(KeyEvent.VK_SPACE);robot.keyRelease(KeyEvent.VK_SPACE);
	}
}

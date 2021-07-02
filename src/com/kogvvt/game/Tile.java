package com.kogvvt.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import com.kogvvt.game.Point;
import java.awt.image.BufferedImage;
import java.lang.*;


public class Tile {
	
	public static final int width = 80;
	public static final int height = 80;
	public static final int slideSpeed = 20;
	public static final int arcWidth = 15;
	public static final int arcHeight = 15;
	
	private int value;
	private BufferedImage tileImage;
	private Color background;
	private Color text;
	private Font font;
	private Point slideTo;
	private int x;
	private int y;
	private boolean canCombine;
	
	public Tile(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		setSlideTo(new Point(x,y));
		tileImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}
	
	private void drawImage() {
		Graphics2D g = (Graphics2D)tileImage.getGraphics();
		
		/* TODO: 
		 * 
		 * Color[]	colorArray;
		 * colorArray = new Color[12];
		 * 
		 * colorArray[0] = burgundowykutacz17
		 * write all background colors and text colors
		 * 
		 *
		 * for(int i = 1 ;i<=2048; i*=2){
		 * 		
		 * 		if(value == i){
		 * 			background = colorArray[Math.log(i)/Math.log(2)];
		 *			 	if(value>4){
		 * 					text = new Color (0xfffffffff);
		 * 				}
		 * 		}
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		switch(value) {
			case 2:
				background = new Color(0xe9e9e9);
				text = new Color(0x000000000);
				break;
			case 4:
				background = new Color(0xe6daab);
				text = new Color(0x000000000);
				break;
			case 8:
				background = new Color(0xf79d3d);
				text = new Color(0xffffffff);
				break;
			case 16:
				background = new Color(0xf28007);
				text = new Color(0xffffffff);
				break;
			case 32:
				background = new Color(0xf55e3b);
				text = new Color(0xffffffff);			
				break;
			case 64:
				background = new Color(0xff0000);
				text = new Color(0xffffffff);			
				break;
			case 128:
				background = new Color(0xe9de84);
				text = new Color(0xffffffff);			
				break;
			case 256:
				background = new Color(0xf6e873);
				text = new Color(0xffffffff);				
				break;
			case 512:
				background = new Color(0xf5e455);
				text = new Color(0xffffffff);			
				break;
			case 1024:
				background = new Color(0xf7e12c);
				text = new Color(0xffffffff);
				break;
			case 2048:
				background = new Color(0xffe400);
				text = new Color(0xffffffff);
				break;
			default:
				background = new Color(0x00000000);
				text = new Color(0xffffffff);
				break;	
		}
		
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, width, height);
		
		g.setColor(background);
		g.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
		
		g.setColor(text);
		
		if(value <=64) {
			font = Game.main.deriveFont(36f);
		}else {
			font = Game.main;
		}
		
		g.setFont(font);
		
		int drawX = width / 2 - DrawUtils.getMessageWidth("" + value, font, g) / 2;
		int drawY = height / 2 + DrawUtils.getMessageHeight("" + value, font, g) / 2;
		g.drawString("" + value, drawX, drawY);
		g.dispose();
	}
	
	public void update() {
		
		
	}
	
	public void render(Graphics2D g) {
		g.drawImage(tileImage,x,y,null);
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public boolean canCombine() {
		return canCombine;
	}

	public void setCanCombine(boolean canCombine) {
		this.canCombine = canCombine;
	}

	public Point getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Point slideTo) {
		this.slideTo = slideTo;
	}

}

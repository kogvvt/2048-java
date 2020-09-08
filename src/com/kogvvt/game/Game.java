package com.kogvvt.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.RenderableImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import jdk.internal.jshell.tool.StopDetectingInputStream;
import jdk.javadoc.internal.tool.Start;

public class Game extends JPanel implements KeyListener, Runnable {

	
	private static final long serialVersionUID = 1L;
	public static final int width = 400;
	public static final int height = 630;
	public static final Font main = new Font("Courier",Font.PLAIN, 28);
	private Thread game;
	private boolean running;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	private long startTime;
	private long elapsed;
	private boolean set;
	
	public Game(){
		setFocusable(true);
		setPreferredSize(new Dimension(width, height));
		addKeyListener(this);
	}
	
	private void update() {
		
		
		
	}
	
	private void render() {
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		// rendering the board
		g.dispose();
		
		Graphics2D g2d = (Graphics2D)getGraphics();
		g2d.drawImage(image,0,0,null);
		g2d.dispose();
		
		
	}
	
	@Override
	public void run() {
		int fps = 0;
		int updates = 0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0 / 60;
		
		// last update time in ns
		double then = System.nanoTime();
		double unprocessed = 0;
		
		while(running) {
			boolean shouldRender  = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerUpdate;
			then = now;
			
		//update queue
			while(unprocessed >=1) {
				updates++;
				update();
				unprocessed--;
				shouldRender = true;
				
			}
			//render
			
			if(shouldRender){
				fps++;
				render();
				shouldRender = false;
			}else{
				try{
					Thread.sleep(1);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			// FPS Timer
			if(System.currentTimeMillis()-fpsTimer >1000) {
				System.out.printf("%d fps %d udpates",fps,updates);
				System.out.println();
				fps = 0;
				updates = 0;
				fpsTimer += 1000;
			}
		}
	}
	
	public synchronized void start() {
		if(running) return;
		running = true;
		game = new Thread(this, "game");
		game.start();
	}
	
	public synchronized void stop() {
		if(!running) return;
		running = false;
		System.exit(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}

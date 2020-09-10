package com.kogvvt.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameBoard {
	
	public static final int rows = 4;
	public static final int cols = 4;
	
	private final int startingTiles = 2;
	private Tile[][] board;
	private boolean dead;
	private boolean won;
	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;
	
	private static int spacing = 10;
	public static int boardWidth = (cols+1) * spacing + cols * Tile.width;
	public static int boardHeight = (rows+1) * spacing +rows * Tile.height;
	
	private boolean hasStarted;
	
	public GameBoard(int x, int y){
		this.x = x;
		this.y = y;
		board = new Tile[rows][cols];
		gameBoard = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB);
		
		createBoardImage();
	}
	
	private void createBoardImage() {
		Graphics2D g = (Graphics2D)gameBoard.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, boardWidth, boardHeight);
		g.setColor(Color.lightGray);
		
		for(int row = 0; row<rows; row++) {
			for(int col = 0; col < cols; col++) {
				int x = spacing + spacing * col + Tile.width * col;
				int y = spacing + spacing * row + Tile.height * row;
				g.fillRoundRect(x, y, Tile.width, Tile.height, Tile.arcWidth, Tile.arcHeight);
			}
		}
	}
	
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D)finalBoard.getGraphics();
		g2d.drawImage(gameBoard,0,0, null);
		
		//drawing tiles
		
		g.drawImage(finalBoard,x,y,null);
		g2d.dispose();
	}
	
	public void update() {
		checkKeys();
	}
	
	private void checkKeys() {
		//move tiles to left, right, up and down
		
		if(Keyboard.typed(KeyEvent.VK_LEFT)) {
			if(!hasStarted) hasStarted = true;
		}
		
		if(Keyboard.typed(KeyEvent.VK_RIGHT)) {
			if(!hasStarted) hasStarted = true;
		}
		
		if(Keyboard.typed(KeyEvent.VK_UP)) {
			if(!hasStarted) hasStarted = true;
		}
		
		if(Keyboard.typed(KeyEvent.VK_DOWN)) {
			if(!hasStarted) hasStarted = true;
		}
	
	}
	

}

package com.kogvvt.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

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
		start();
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
	
	private void start() {
		for(int i=0; i<startingTiles; i++) {
			spawnRandom();	
		}
	}
	
	private void spawnRandom() {
		Random random = new Random();
		boolean notValid = true;
		
		while(notValid) {
			int location = random.nextInt(rows*cols);
			int row = location / rows;
			int col = location % cols;
			Tile current = board[row][col];
			if(current == null) {
				int value = random.nextInt(10) <9 ? 2 : 4;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}
	}
	
	public int getTileX(int col) {
		return spacing + col * Tile.width + col * spacing;
	}
	
	public int getTileY(int row) {
		return spacing + row *Tile.height + row * spacing;
	}
	
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D)finalBoard.getGraphics();
		g2d.drawImage(gameBoard,0,0, null);
		
		for(int row = 0; row<rows; row++) {
			for(int col = 0; col<cols; col++) {
				Tile current = board[row][col];
						if(current == null) continue;
						current.render(g2d);
			}
		}
		
		g.drawImage(finalBoard,x,y,null);
		g2d.dispose();
	}
	
	public void update() {
		checkKeys();
		
		for(int row = 0; row<rows; row++) {
			for(int col = 0; col<cols; col++) {
				Tile current = board[row][col];
				if(current == null) continue;
				current.update();
				//reset position
				if(current.getValue() == 2048) {
					won = true;
				}
				
			}
		}
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

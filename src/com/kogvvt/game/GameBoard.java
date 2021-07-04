package com.kogvvt.game;

import java.awt.Color;
import java.awt.Graphics2D;
import com.kogvvt.game.Point;
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
	public static int boardWidth = (cols + 1) * spacing + cols * Tile.width;
	public static int boardHeight = (rows + 1) * spacing + rows * Tile.height;

	private boolean hasStarted;

	public GameBoard(int x, int y) {
		this.x = x;
		this.y = y;
		board = new Tile[rows][cols];
		gameBoard = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB);

		createBoardImage();
		start();
	}

	private void createBoardImage() {
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, boardWidth, boardHeight);
		g.setColor(Color.lightGray);

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = spacing + spacing * col + Tile.width * col;
				int y = spacing + spacing * row + Tile.height * row;
				g.fillRoundRect(x, y, Tile.width, Tile.height, Tile.arcWidth, Tile.arcHeight);
			}
		}
	}

	private void start() {
		for (int i = 0; i < startingTiles; i++) {
			spawnRandom();
		}
	}

	private void spawnRandom() {
		Random random = new Random();
		boolean notValid = true;

		while (notValid) {
			int location = random.nextInt(rows * cols);
			int row = location / rows;
			int col = location % cols;
			Tile current = board[row][col];
			if (current == null) {
				int value = random.nextInt(10) < 9 ? 2 : 4;
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
		return spacing + row * Tile.height + row * spacing;
	}

	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.render(g2d);
			}
		}

		g.drawImage(finalBoard, x, y, null);
		g2d.dispose();
	}

	public void update() {
		checkKeys();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.update();
				resetPosition(current, row, col);
				if (current.getValue() == 2048) {
					won = true;
				}

			}
		}
	}

	private void resetPosition(Tile current, int row, int col) {
		if (current == null)
			return;

		int x = getTileX(col);
		int y = getTileY(row);

		int distX = current.getX() - x;
		int distY = current.getY() - y;

		if (Math.abs(distX) < Tile.slideSpeed) {
			current.setX(current.getX() - distX);
		}

		if (Math.abs(distY) < Tile.slideSpeed) {
			current.setY(current.getY() - distY);
		}

		if (distX < 0) {
			current.setX(current.getX() + Tile.slideSpeed);
		}

		if (distX > 0) {
			current.setX(current.getX() - Tile.slideSpeed);
		}

		if (distY < 0) {
			current.setY(current.getY() + Tile.slideSpeed);
		}

		if (distY > 0) {
			current.setY(current.getY() - Tile.slideSpeed);
		}
	}

	private boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction dir) {
		boolean canMove = false;
		Tile current = board[row][col];
		if (current == null)
			return false;
		boolean move = true;
		int newCol = col;
		int newRow = row;
		while (move) {
			newCol += horizontalDirection;
			newRow += verticalDirection;
			if (checkOutOfBounds(dir, newRow, newCol))
				break;
			if (board[newRow][newCol] == null) {
				board[newRow][newCol] = current;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				canMove = true;
			} else if (board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].canCombine()) {
				board[newRow][newCol].setCanCombine(false);
				board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
				canMove = true;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				// board[newRow][newCol].setCombineAnimation(true);
				// add to score
			} else {
				move = false;
			}
		}

		return canMove;
	}

	private boolean checkOutOfBounds(Direction dir, int row, int col) {
		if (dir == Direction.LEFT) {
			return col < 0;
		} else if (dir == Direction.RIGHT) {
			return col > cols - 1;
		} else if (dir == Direction.UP) {
			return row < 0;
		} else if (dir == Direction.DOWN) {
			return row > rows - 1;
		}
		return false;
	}

	private void moveTiles(Direction dir) {
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;

		if (dir == Direction.LEFT) {
			horizontalDirection = -1;
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else
						move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}

		else if (dir == Direction.RIGHT) {
			horizontalDirection = 1;
			for (int row = 0; row < rows; row++) {
				for (int col = cols - 1; col >= 0; col--) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else
						move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}

		else if (dir == Direction.UP) {
			verticalDirection = -1;
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else
						move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		}

		else if (dir == Direction.DOWN) {
			verticalDirection = 1;
			for (int row = rows - 1; row >= 0; row--) {
				for (int col = 0; col < cols; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else
						move(row, col, horizontalDirection, verticalDirection, dir);
				}
			}
		} else {
			System.out.println(dir + "is not a valid direction!");
		}

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.setCanCombine(true);
			}
		}
		if (canMove) {
			spawnRandom();
			// check if the game is over
			checkDead();
		}

	}

	private void checkDead() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (board[row][col] == null)
					return;
				if (checkSurroundingTiles(row, col, board[row][col])) {
					return;
				}
			}
		}

		dead = true;
		// setHighScore(score)
	}

	private boolean checkSurroundingTiles(int row, int col, Tile current) {
		if (row > 0) {
			Tile check = board[row - 1][col];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;
		}
		if (row < rows - 1) {
			Tile check = board[row + 1][col];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;
		}
		if (col > 0) {
			Tile check = board[row][col - 1];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;

		}
		if (col < cols - 1) {
			Tile check = board[row][col + 1];
			if (check == null)
				return true;
			if (current.getValue() == check.getValue())
				return true;

		}
		return false;
	}

	private void checkKeys() {
		// move tiles to left, right, up and down

		if (Keyboard.typed(KeyEvent.VK_LEFT)) {
			moveTiles(Direction.LEFT);
			if (!hasStarted)
				hasStarted = true;
		}

		if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
			moveTiles(Direction.RIGHT);
			if (!hasStarted)
				hasStarted = true;
		}

		if (Keyboard.typed(KeyEvent.VK_UP)) {
			moveTiles(Direction.UP);
			if (!hasStarted)
				hasStarted = true;
		}

		if (Keyboard.typed(KeyEvent.VK_DOWN)) {
			moveTiles(Direction.DOWN);
			if (!hasStarted)
				hasStarted = true;
		}

	}

}

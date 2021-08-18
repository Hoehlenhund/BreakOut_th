import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.event.KeyEvent;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: Breakout
 * 
 * A simple version of the Breakout game.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Eduard Moser
 */
public class Breakout extends GraphicsProgram {

	GLabel gameOver;

	// Ball
	private int POSITION_BALLX = 145;
	private int POSITION_BALLY = 300;
	private int BALL_RADIUS = 10;
	private int vx = -8;
	private int vy = 8;
	private GOval ball;

	// Map
	private int SIZE_TABLE_X = 300;
	private int SIZE_TABLE_Y = 600;

	// Bricks
	private static final int BRICK_LENGHT = 40;
	private static final int BRICK_HEIGHT = 15;
	private static final int BRICK_STARTX = 10;
	private static final int BRICK_STARTY = 45; // Dif. BRICK * 3
	private static final int NMB_OF_BRICKSX = 7; // Care Map Size
	private static final int NMB_OF_BRICKSY = 4;
	private static final int y = BRICK_HEIGHT + BRICK_HEIGHT;

	private int brick_full = NMB_OF_BRICKSX + (NMB_OF_BRICKSY * NMB_OF_BRICKSX);
	private int brick_count = 0;

	// Paddle
	private int PADDLE_LENGHT = 40;
	private int PADDLE_HEIGHT = 10;
	private int POSITION_PADDLEX = 135;
	private int POSITION_PADDLEY = 550;
	private GRect paddle;

	private int life = 3;

	public void run() {
		setSize(SIZE_TABLE_X, SIZE_TABLE_Y);
		setBackground(Color.BLACK);
		addKeyListeners();
		getGameStarted();
		setup();
		gameLoop();
	}

	private void setup() {
		addMouseListeners();
		createBall();
		createPaddle();
		createBricksX(y);
		createBricksY();
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		paddle.setLocation(x, POSITION_PADDLEY);
	}

	private void gameLoop() {
		while (true) {
			moveBall();
			checkForCollisionWithWall();
			checkCollisionWithPaddle();
			checkCollisionWithBricks();
			pause(40);

			if (ball.getY() > 590) {
				life -= 1;
				removeAll();
				if (life == 0) {
					GLabel gameTotalOver = new GLabel("Game Over");
					gameTotalOver.setFont(new Font("Arial", Font.PLAIN, 16));
					gameTotalOver.setColor(Color.WHITE);
					add(gameTotalOver, (SIZE_TABLE_X - gameTotalOver.getWidth()) / 2,
							(SIZE_TABLE_Y - gameTotalOver.getHeight()) / 2);
					break;
				} else {
					gameOver = new GLabel("Try again: " + life + " ♥ left");
					gameOver.setFont(new Font("Arial", Font.PLAIN, 16));
					gameOver.setColor(Color.WHITE);
					add(gameOver, (SIZE_TABLE_X - gameOver.getWidth()) / 2, (SIZE_TABLE_Y - gameOver.getHeight()) / 2);
					waitForClick();
					setup();
				}
			}
		}
	}

	private void getGameStarted() {
		GLabel gameStart = new GLabel("Ready? You have " + life + " ♥ left");
		gameStart.setFont(new Font("Arial", Font.PLAIN, 16));
		gameStart.setColor(Color.WHITE);
		add(gameStart, (SIZE_TABLE_X - gameStart.getWidth()) / 2, (SIZE_TABLE_Y - gameStart.getHeight()) / 2);
		waitForClick();
		remove(gameStart);
	}

	private void youWon() {
		while (true) {
			removeAll();
			GLabel gameOne = new GLabel("Well Done!");
			gameOne.setFont(new Font("Arial", Font.PLAIN, 16));
			gameOne.setColor(Color.WHITE);
			add(gameOne, (SIZE_TABLE_X - gameOne.getWidth()) / 2, (SIZE_TABLE_Y - gameOne.getHeight()) / 2);
			break;
		}
	}

	private void createBall() {
		ball = new GOval(BALL_RADIUS, BALL_RADIUS);
		ball.setColor(Color.WHITE);
		ball.setFilled(true);
		add(ball, POSITION_BALLX, POSITION_BALLY);
	}

	private void createBricksX(int y) {
		int x = BRICK_STARTX;
		for (int i = 0; i < NMB_OF_BRICKSX; i++) {
			GRect brick = new GRect(BRICK_LENGHT, BRICK_HEIGHT);
			add(brick, x, y);
			x = x + BRICK_LENGHT;
			RandomGenerator rgen = new RandomGenerator();
			Color col = rgen.nextColor();
			brick.setFillColor(col);
			brick.setFilled(true);
		}
	}

	private void createBricksY() {
		int y = BRICK_STARTY;
		for (int i = 0; i < NMB_OF_BRICKSY; i++) {
			createBricksX(y);
			y = y + BRICK_HEIGHT;
		}
	}

	private void createPaddle() {
		paddle = new GRect(PADDLE_LENGHT, PADDLE_HEIGHT);
		paddle.setColor(Color.WHITE);
		paddle.setFilled(true);
		add(paddle, POSITION_PADDLEX, POSITION_PADDLEY);
	}

	private void checkForCollisionWithWall() {
		double x = ball.getX();
		double y = ball.getY();
		if ((x < 0) || (x + BALL_RADIUS > SIZE_TABLE_X)) {
			vx = -vx;
		}
		if ((y < 0) || (y + BALL_RADIUS > SIZE_TABLE_Y)) {
			vy = -vy;
		}
	}

	private void checkCollisionWithPaddle() {
		double x = ball.getX();
		double y = ball.getY() + BALL_RADIUS;
		GObject obj = getElementAt(x, y);
		if (obj != null && obj == paddle) {
			vy = -vy;
		}
	}

	private void moveBall() {
		ball.move(vx, vy);
	}

	private void checkCollisionWithBricks() {
		double x = ball.getX();
		double y = ball.getY();
		GObject obj = getElementAt(x, y);
		if (obj != null && obj != paddle) {
			remove(obj);
			vy = -vy;
			int c = brick_count += 1;
			if (c == brick_full) {
				youWon();
			}
		}
	}
}

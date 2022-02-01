import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

enum Directions {UP, DOWN, LEFT, RIGHT;
	Directions(){ }
	
	static Directions getDirectionFrom(KeyEvent e){
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: return Directions.LEFT;
		case KeyEvent.VK_RIGHT: return Directions.RIGHT;
		case KeyEvent.VK_DOWN: return Directions.DOWN;
		case KeyEvent.VK_UP: return Directions.UP;
		}
		return null;
	}
	static Directions getOpposite(Directions direction) {
		switch(direction) {
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		case DOWN: return UP;
		case UP: return DOWN;
		}
		return null;
	}

	boolean isAdjacent(Directions otherDirection) {
		return this != otherDirection && getOpposite(otherDirection) != this;
	}
}

public class GamePanel extends JPanel implements ActionListener{
	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	private static final int UNIT_SIZE = 50;
	private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	private static final int TIMER_DELAY = 75; 
	
	private Directions direction;	//current direction of the snake
	private Directions nextDirection; //next direction of the snake, which will be applied on the next tick
	private boolean canTurn;  //if a player can turn in the current tick
	
	private final int x[] = new int[GAME_UNITS]; //x coordinates of every "block" of the snake.
	private final int y[] = new int[GAME_UNITS]; //y coordinates of every "block" of the snake.
	private int bodyParts = 6;                   //current amount of "blocks" in the snake.
	
	private Random rand = new Random();
	private int appleX;
	private int appleY;
	private int applesEaten;
	
	private boolean running = false;
	private Timer timer;
	
	public GamePanel() {
		rand = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		running = true; 
		direction = Directions.RIGHT;
		nextDirection = direction;
		canTurn = true;
		applesEaten = 0;
		generateApple();
		timer = new Timer(TIMER_DELAY, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//drawing grid
		for(int i =0; i<SCREEN_HEIGHT/UNIT_SIZE; ++i) {
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
		}
		for (int i =0; i<SCREEN_WIDTH/UNIT_SIZE; ++i) {
			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
		}
		
		//drawing snake
		if (bodyParts > 0) {
			//body
			g.setColor(Color.green);
			for(int i =1; i<bodyParts; ++i) {
				g.fillRect(x[i]+1, y[i]+1, UNIT_SIZE-2, UNIT_SIZE-2); //we subtract 1 and 2 to make margins in the snake 
			}
			//head
			g.setColor(Color.yellow);
			g.fillRect(x[0]+1, y[0]+1, UNIT_SIZE-2, UNIT_SIZE-2);
		}
		
		//drawing apple
		g.setColor(Color.red);
		g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
	}
	/**
	 * This method is called every tick. It is responsible for moving the snake on the grid.
	 * It iterates from the last element of the array to the first element (not including),
	 * thus, every part of the body moves one square closer to the head. 
	 * Head movement depends on direction, so we check it and only then move head.
	 */
	public void move() {
		//if a player cannot turn, i.e. has made a 'sharp turn' (see isSharpTurn() method),
		//than the direction for this tick was already set in keyPressed method.
		if (canTurn ) direction = nextDirection; 
		canTurn = true;	 
		
		//moving body
		for(int i=bodyParts -1; i>0; --i) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		//moving head
		switch (direction) {
		case UP:
			y[0] -= UNIT_SIZE;
			break;
		case DOWN:
			y[0] += UNIT_SIZE;
			break;
		case LEFT:
			x[0] -= UNIT_SIZE;
			break;
		case RIGHT:
			x[0] += UNIT_SIZE;
			break;
		}
		
	}
	
	/**
	 * This method is called every tick. It is responsible for checking the collisions of the snake with apples.
	 * If a collision is detected, new apple is generated.
	 */
	public void checkApple() {
		if (appleX == x[0] && appleY == y[0]) {
			++applesEaten;
			++bodyParts;
			generateApple();
			repaint();
		}
	}
	
	public void generateApple() {
		boolean intersects;
		do {
			intersects = false;
			appleX = rand.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
			appleY = rand.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
			for(int i=0; i<bodyParts; ++i) {
				if (appleX == x[i] && appleY == y[i]) {
					intersects = true;
					break;
				}
					
			}
		} while (intersects); //apple coordinates will be regenerated, if apple has spawned inside the snake.
	}
	/**
	 * This method is called every tick. It is responsible for checking the collisions of the snake with itself and with walls.
	 * First, it iterates from the first element (after the head) and checks, if head has collided with the body.
	 * Second, it checks if head has collided with any of four walls. 
	 * If it does, gameOver() method is called. 
	 */
	public void checkCollisions() {
		//head collision with body
		for(int i=1; i<bodyParts; ++i) {
			if (x[0] == x[i] && y[0] == y[i]) {
				gameOver();
			}
		}
		//head collision with walls
		//left wall
		if (x[0] < 0) 	gameOver();
		
		//right wall
		else if (x[0] > SCREEN_WIDTH - UNIT_SIZE) gameOver();
		
		//upper wall
		else if (y[0] < 0) gameOver();
		
		//bottom wall
		else if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) gameOver();
	}
	
	public void gameOver() {
		running = false;
	}
	@Override 
	public void actionPerformed(ActionEvent e) {
		if (running) {
			checkApple();
			move();
			checkCollisions();
		}
		repaint();
	}
	
	
	public class MyKeyAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			Directions desiredDirection = Directions.getDirectionFrom(e);
			if (desiredDirection == null) return;
			
			//see isSharpTurn() method
			if (isSharpTurn(desiredDirection)) {
				canTurn = false;
				direction = nextDirection; 
				nextDirection = desiredDirection;
			}
			else {		
				//we change out next direction only when player doesn't make 180 degree turn
				nextDirection = desiredDirection.isAdjacent(direction) ? desiredDirection : nextDirection;
			}
		}
	}
	
	/**
	 * When a player needs to change direction 2 times very quickly - it registers as a 'sharp turn'.
	 * In that case the player might change direction 2 times in one tick, which is not allowed. 
	 * This method detects, if it is this case and if the order of directions is not illegal.
	 * @param desiredDirection direction, which player pressed to go.
	 * @return true, if current direction, next direction and desired location have legal order. E.g RIGHT, UP, RIGHT - is valid, but RIGHT, UP, DOWN - is not
	 */
	public boolean isSharpTurn(Directions desiredDirection) {
		return direction.isAdjacent(nextDirection) && nextDirection.isAdjacent(desiredDirection);
	}
}



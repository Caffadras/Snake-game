import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JPanel;
import javax.swing.Timer;

enum Directions {UP, DOWN, LEFT, RIGHT}

public class GamePanel extends JPanel implements ActionListener{
	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	private static final int UNIT_SIZE = 25;
	private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	private static final int TIMER_DELAY = 75; 
	
	private Directions direction; 
	private final int x[] = new int[GAME_UNITS]; //x coordinates of every "block" of the snake.
	private final int y[] = new int[GAME_UNITS]; //y coordinates of every "block" of the snake.
	private int bodyParts = 6;                   //current amount of "blocks" in the snake.
	
	private boolean running = false;
	Timer timer;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		running = true; 
		direction = Directions.RIGHT;
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
			g.setColor(Color.yellow);
			g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
	
			g.setColor(Color.green);
			for(int i =1; i<bodyParts; ++i) {
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
	}
	/**
	 * This method is called every tick. It is responsible for moving the snake on the grid.
	 * It iterates from the last element of the array to the first element (not including),
	 * thus, every part of the body moves one square closer to the head. 
	 * Head movement depends on direction, so we check it and only then move head.
	 */
	public void move() {
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
	
	public void checkApple() {
		
	}
	
	public void checkCollisions() {
		
	}
	
	public void gameOver() {
		
	}
	@Override 
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != Directions.RIGHT) direction = Directions.LEFT;
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != Directions.LEFT) direction = Directions.RIGHT;
				break;
			case KeyEvent.VK_DOWN:
				if (direction != Directions.UP) direction = Directions.DOWN;
				break;
			case KeyEvent.VK_UP:
				if (direction != Directions.DOWN) direction = Directions.UP;
				break;
		
			}
		}
	}
	
	
}



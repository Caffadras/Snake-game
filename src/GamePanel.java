import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener{
	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	private static final int UNIT_SIZE = 25;
	private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	private static final int TIMER_DELAY = 75; 
	
	private Directions direction; 
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
	}
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		for(int i =0; i<SCREEN_HEIGHT/UNIT_SIZE; ++i) {
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
		}
		for (int i =0; i<SCREEN_WIDTH/UNIT_SIZE; ++i) {
			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
		}
	}
	
	
	@Override 
	public void actionPerformed(ActionEvent e) {
		
	}
	
	
}

enum Directions {UP, DOWN, LEFT, RIGHT}

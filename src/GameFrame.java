import javax.swing.JFrame; 

public class GameFrame extends JFrame{
	
	public GameFrame(){
		
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setVisible(true);
		this.setResizable(false);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}

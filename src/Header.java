

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Generic label for the text that will go at the top of each half of the screen.
 * Only the one above the game will be updated though
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class Header extends JPanel implements Observer
{
	JLabel label;
	MyClient info;
	
	public Header(String text, MyClient info)
	{
		this.info = info;
		label = new JLabel(text);
		add(label);
	}

	/**
	 * what to do when notified of change
	 */
	@Override
	public void update(Observable arg0, Object arg1)
	{

		String isMyTurn = "You are "+info.getToken()+". Xs go first";
		label.setText("Opponent: "+info.getOpponent() +"                                           "+isMyTurn); // \n and \t weren't doing anything hence that mess
		
		//System.out.println("UPDATED HEADER");
		
		repaint();
		
	}
	
}

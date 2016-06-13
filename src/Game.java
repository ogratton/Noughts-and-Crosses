

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * The right-hand side of the GUI containing the grid, text and a concede button 
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class Game extends JPanel
{
	public Game(Header header, Grid grid, Button button)
	{
		setLayout(new BorderLayout());
		add(header, BorderLayout.NORTH);
		add(grid, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
	}

}

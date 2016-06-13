

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * The left-hand side of the GUI containing the lobby table, text and an exit button 
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class Lobby extends JPanel
{

	public Lobby(Header header, Table table, Button button)
	{
		setLayout(new BorderLayout());
		add(header, BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
	}
}

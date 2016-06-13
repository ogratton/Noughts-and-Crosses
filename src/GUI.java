

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * Makes a panel consisting of the lobby on the left and the game on the right
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class GUI extends JPanel
{
	public GUI(NotificationQueue toServerQueue, MyClient info)
	{		
		Header lobbyHeader = new Header("Players online:", info);
		Header gameHeader = new Header("Select an opponent", info);
		Table table = new Table(toServerQueue, info);
		Grid grid = new Grid(toServerQueue, info);
		Button exit = new Button("EXIT", toServerQueue);
		Button leave = new Button("CONCEDE", toServerQueue);
		
		Lobby lobby = new Lobby(lobbyHeader, table, exit);
		Game game = new Game(gameHeader, grid, leave);
		
		info.addObserver(table);
		info.addObserver(gameHeader);
		info.addObserver(grid);
		
		GridLayout layout = new GridLayout(1,0);
		setLayout(layout);
		add(lobby);
		add(game);
	}
}
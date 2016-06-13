

import java.util.ArrayList;
import java.util.Observable;

/**
 * Contains the information the client needs to know about itself
 * 
 * @author Oliver
 *
 */
public class MyClient extends Observable
{
	private String name;
	private boolean inGame;
	private int x = -1;
	private int y = -1;
	private String opponent = null;
	private boolean myTurn;
	private String token;
	private String[][] board;
	private ArrayList<String> onlinePlayers = new ArrayList<String>();

	public MyClient(String _name)
	{
		super();
		this.name = _name;
	}

	/**
	 * @return the name of the client this object belongs to
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the name of the person this player is playing
	 * null if not in game
	 */
	public String getOpponent()
	{
		return opponent;
	}

	/**
	 * Set to null when game ends
	 * @param opp the new opponent (null if removing)
	 */
	public void setOpponent(String opp)
	{
		opponent = opp;

		// tell the GUI that we've changed
//		setChanged();
//		notifyObservers(); // for some reason this made it not work so it's commented out
	}
	
	/**
	 * Sets the token the client is playing as
	 * @param token "X" or "O"
	 */
	public void setToken(String token)
	{
		this.token = token;
	}
	
	/**
	 * @return "X" if client playing as crosses or "O" if noughts
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @return true if client in a game else false
	 */
	public boolean inGame()
	{
		return this.inGame;
	}

	/**
	 * changes the client's in-game status
	 * @param inGame new desired state of inGame()
	 */
	public void setInGame(boolean inGame)
	{
		this.inGame = inGame;
	}

	/**
	 * The move the client has to execute x and y should be reset to -1 with
	 * this function after execution of the move
	 * 
	 * @param i
	 * @param j
	 */
	public void setMove(int i, int j)
	{
		x = i;
		y = j;
	}

	/**
	 * @return an int array of x then y coord of the move the player wants to perform
	 */
	public int[] getMove()
	{
		return new int[] { x, y };
	}

	/**
	 * @return true if it is this client's turn
	 */
	public boolean getTurn()
	{
		return myTurn;
	}

	/**
	 * Set whether it is this client's turn or not
	 * @param bool true if it is their go else false
	 */
	public void setTurn(boolean bool)
	{
		myTurn = bool;
	}

	/**
	 * @return the full board
	 */
	public String[][] getBoard()
	{
		return board;
	}

	/**
	 * @param i x coord
	 * @param j y coord
	 * @return the value of a specific square in the board
	 */
	public String getBoardSquare(int i, int j)
	{
		return board[i][j];
	}

	/**
	 * set a specific square to a given token's value
	 * @param i x coord
	 * @param j y coord
	 * @param token X or O
	 */
	public void setBoardSquare(int i, int j, String token)
	{
		board[i][j] = token;
	}

	/**
	 * set the entire board to this new array
	 * @param board new board
	 */
	public void setBoard(String[][] board)
	{
		this.board = board;

		// tell the GUI that we've changed
		setChanged();
		notifyObservers(); // this is the place it seems to be most successful
	}

	/**
	 * @return an ArrayList of all the players online at the moment
	 */
	public ArrayList<String> getAllPlayers()
	{
		return onlinePlayers;
	}

	/**
	 * Reads the string of names the server gives us and updates our list
	 * accordingly POSSIBLE SYNCHRONISING ERROR POTENTIAL MAYBE I DON'T KNOW
	 * 
	 * @param allNames
	 */
	public void setPlayersOnline(String allNames)
	{
		String[] arr = allNames.split(" ");
		ArrayList<String> newOnline = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++)
		{
			newOnline.add(arr[i]);
		}
		onlinePlayers = newOnline;

		// tell the GUI that we've changed
//		setChanged();
//		notifyObservers();

	}

}

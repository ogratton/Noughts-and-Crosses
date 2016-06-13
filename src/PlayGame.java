/**
 * The main game thread. One is started for every player in a game
 * 
 * @author Oliver
 *
 */
public class PlayGame extends Thread
{

	private final int SLEEP_TIME = 10;

	private MyClient info;
	private String opponent; // the other player
	private boolean isCross; // whether this thread is playing with noughts or crosses
	private String myToken;
	private String opToken;

	private String[][] board; // our local copy of the board, to be updated and printed after each move

	private NotificationQueue queue;

	public static final String BLANK = ".", CROSS = "X", NOUGHT = "O";

	/**
	 * Make a new game
	 * 
	 * @param _client
	 * @param _opponent
	 * @param _isCross
	 * @param _clientTable
	 */
	public PlayGame(MyClient _info, NotificationQueue _queue)
	{
		info = _info;
		queue = _queue;
		opponent = info.getOpponent();
		isCross = info.getTurn(); // if it is the players go first they will be the cross
		myToken = isCross ? CROSS : NOUGHT;
		opToken = isCross ? NOUGHT : CROSS;

		// generate a new 3x3 grid board
		board = new String[3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				board[i][j] = BLANK;
			}
		}
		// update the client's personal copy of the board (which can be accessed by the GUI)
		info.setBoard(board);
	}

	/**
	 * Runs until the game is over
	 */
	public void run()
	{

		// while there is a game to be played, do the following, depending on whether or not it is our turn
		// actions are largely the same except for printing
		// a player cannot move when it is not their go due to the if statement in ClientSender
		try
		{
			while (info.inGame())
			{
				// if it's our turn
				if (info.getTurn())
				{
					System.out.println(">> It's your turn!");
					System.out.println(this.boardToString());
					// wait for client to type a move
					while (!this.moveMain());
					System.out.println(this.boardToString());

				}
				// it's the opponent's turn
				else
				{
					System.out.println(">> " + opponent + "'s move");
					while (!this.moveMain());

				}
				Thread.sleep(SLEEP_TIME);
			}
		}
		catch (InterruptedException e)
		{
			System.err.println("PlayGame interrupted");
		}

		System.out.println("GAME OVER");
		info.setOpponent(null);
		info.setTurn(false);
		queue.offer(new PlainNote("/leave"));
		// it is a server used command but should also be available to the user. make it interrupt if used by user
		return;

	}

	/**
	 * Constantly checks MyClient for a move to do and executes it when it finds
	 * one Also checks whether the move was a deciding one (results in end of
	 * game)
	 * 
	 * @return whether or not the detected move was successful
	 */
	public boolean moveMain()
	{
		int[] move = info.getMove();;

		try
		{
			synchronized (info)
			{
				while (move[0] == -1)
				{
					move = info.getMove();;
					Thread.sleep(SLEEP_TIME);
				}
			}
		}
		catch (InterruptedException e)
		{
			// probably won't happen. hopefully
			System.out.println("INTERRUPTED!");
		}

		//System.out.println("Move received!");

		// do the move they typed
		if (!(this.doMove(info.getMove()[0], info.getMove()[1])))
		{
			// doesn't really matter that this prints to opponent as well as it serves the client right for being silly
			System.out.println("Invalid play: square occupied");
			return false;
		}

		// end of your turn
		info.setTurn(!info.getTurn());

		if (winner(myToken))
		{
			info.setInGame(false);
			System.out.println("You did a win woop");
		}
		else if (winner(opToken))
		{
			info.setInGame(false);
			System.out.println(this.boardToString());
			System.out.println("You lost :(");
		}
		else if (draw())
		{
			info.setInGame(false);
			System.out.println("DRAW! Surprise surprise");
		}

		return true;
	}

	/**
	 * Tries to execute a move i and j are flipped here so that the move command
	 * is more user-friendly (/move 0 2 is bottom left not top right)
	 * 
	 * @param i
	 *            y coord
	 * @param j
	 *            x coord
	 * @return true if move successful, else false
	 */
	public boolean doMove(int j, int i)
	{
		if (board[i][j] == BLANK)
		{
			if (info.getTurn())
			{
				board[i][j] = myToken;
			}
			else
			{
				board[i][j] = opToken;
			}

			// reset the move for the player
			info.setMove(-1, -1);
			info.setBoard(board);
			return true;
		}
		else
		{
			info.setMove(-1, -1);
			return false;
		}
	}

	/**
	 * Detects if noughts, crosses or neither have won (meaning game still
	 * ongoing, not draw)
	 * 
	 * @param token
	 *            X or O
	 * @return if token has won
	 */
	private boolean winner(String token)
	{
		return (board[0][0] == token && board[0][1] == token && board[0][2] == token)
				|| (board[1][0] == token && board[1][1] == token && board[1][2] == token)
				|| (board[2][0] == token && board[2][1] == token && board[2][2] == token)
				|| (board[0][0] == token && board[1][0] == token && board[2][0] == token)
				|| (board[0][1] == token && board[1][1] == token && board[2][1] == token)
				|| (board[0][2] == token && board[1][2] == token && board[2][2] == token)
				|| (board[0][0] == token && board[1][1] == token && board[2][2] == token)
				|| (board[0][2] == token && board[1][1] == token && board[2][0] == token);
	}

	/**
	 * Detects a draw (no blank space left and no winner)
	 * 
	 * @return true if draw situation
	 */
	private boolean draw()
	{
		return (board[0][0] != BLANK && board[0][1] != BLANK && board[0][2] != BLANK)
				&& (board[1][0] != BLANK && board[1][1] != BLANK && board[1][2] != BLANK)
				&& (board[2][0] != BLANK && board[2][1] != BLANK && board[2][2] != BLANK)
				&& (board[0][0] != BLANK && board[1][0] != BLANK && board[2][0] != BLANK)
				&& (board[0][1] != BLANK && board[1][1] != BLANK && board[2][1] != BLANK)
				&& (board[0][2] != BLANK && board[1][2] != BLANK && board[2][2] != BLANK)
				&& (board[0][0] != BLANK && board[1][1] != BLANK && board[2][2] != BLANK)
				&& (board[0][2] != BLANK && board[1][1] != BLANK && board[2][0] != BLANK);

	}

	/**
	 * Converts the 2D array board into a string for printing to the console
	 * 
	 * @return string version of board
	 */
	public String boardToString()
	{
		String toString = "\n";
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				toString += board[i][j];
			}
			toString += "\n";
		}
		return toString;
	}

}

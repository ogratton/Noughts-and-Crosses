

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Receives strings from the server and relays them to the client appropriately
 * 
 * @author Oliver
 *
 */
public class ClientReceiver extends Thread
{

	private final int SLEEP_TIME = 50;

	private BufferedReader fromServer;
	private MyClient info;
	private PlayGame game;

	private NotificationQueue toServerQueue;

	ClientReceiver(MyClient info, BufferedReader server,NotificationQueue toServerQueue)
	{
		this.fromServer = server;
		this.info = info;
		this.toServerQueue = toServerQueue;
	}

	/**
	 * Read and interpret what the server has sent to us
	 */
	public void run()
	{
		// Print to the user whatever we get from the server:
		try
		{
			while (true)
			{
				String s = fromServer.readLine();
				if (s != null)
				{
					String[] arr = s.split(" ");

					try
					{
						// special case: if the server is telling us to start a game, we do not print this to the client, but just start the game
						if (arr[0].equals(Commands.GAME))
						{
							boolean turn = arr[2].equals("true");
							String token = turn ? "Xs" : "Os";
							info.setInGame(true);
							info.setToken(token);
							System.out.println("NEW GAME: You are " + token);
							info.setOpponent(arr[1]);
							info.setTurn(turn);
							game = new PlayGame(info, toServerQueue);
							game.start();
						}
						// special case: if the server is telling us about a move, we do the move
						// the move can be either from ourselves or our opponent
						else if (arr[0].equalsIgnoreCase(info.getName()) || arr[0].equalsIgnoreCase(info.getOpponent()))
						{
							// no need to check parsing as it has been done before being sent
							info.setMove(Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
						}
						// something went wrong on the server so we'll tell the client
						else if (arr[0].equalsIgnoreCase(Commands.ERROR))
						{
							System.out.println(s.split(" ", 2)[1]);
							// TODO send an Error notification which SHOULD be picked up by the GUI and displayed as a pop-up
							//toClientQueue.offer(new ErrorNote("Server", s.split(" ", 2)[1]));
						}
						else if (arr[0].equalsIgnoreCase(Commands.LIST))
						{
							String[] nameList = s.split(" ", 2);
							info.setPlayersOnline(nameList[1]);
							String printedMessage = "Players online: \n";
							// this loop starts at one so as to ignore "/list"
							for (int i = 1; i < nameList.length; i++)
							{
								if (i == nameList.length - 1)
								{
									printedMessage += nameList[i];
								}
								else
								{
									printedMessage += nameList[i] + ", ";
								}

							}
							System.out.println(printedMessage.trim());
						}
						// special case: the game ended suddenly
						else if (arr[0].equals("/reset"))
						{
							info.setOpponent(null);
							info.setTurn(false);
						}

						else
						{
							System.out.println(s);
						}
						Thread.sleep(SLEEP_TIME);
					}
					catch (InterruptedException e)
					{
						System.err.println("ClientReceiver Interrupted");
					}
					// neither of these should happen since ServerReceiver is generating these commands, not the user, but just in case, they are caught
					catch (NullPointerException e)
					{
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
					}
				}

				else
				{
					fromServer.close(); // Probably no point.
					throw new IOException("Got null from server"); // Caught below.
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Server seems to have died " + e.getMessage());
			System.exit(1); // Give up.
		}
		catch (NullPointerException e)
		{
		}

		catch (ArrayIndexOutOfBoundsException e)
		{
		}
	}
}

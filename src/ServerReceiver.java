
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 * Gets messages from client and puts them in a queue, for ServerSender thread
 * to forward to the appropriate client.
 * 
 * @author Oliver
 *
 */
public class ServerReceiver extends Thread
{
	private final int SLEEP_TIME = 10;

	private String myClientsName;
	private BufferedReader myClient;
	private ClientTable clientTable;

	public ServerReceiver(String n, BufferedReader c, ClientTable t)
	{
		myClientsName = n;
		myClient = c;
		clientTable = t;
	}

	public void run()
	{
		try
		{
			while (true)
			{

				String command = myClient.readLine();

				if (command != null)
				{
					String[] arr = command.split(" ", 3); // splits the command like so: "/<command name>","<argument1>","<argument2>"

					if (arr[0].equalsIgnoreCase(Commands.TELL))
					{
						// TODO catch them talking to themselves?

						try
						{
							NotificationQueue queue = clientTable.getQueue(arr[1]);
							Message msg = new Message(myClientsName, arr[2]);
							queue.offer(msg);
						}
						catch (NullPointerException e)
						{
							clientTable.getQueue(myClientsName)
									.offer(new Message("Server", ("/error Invalid recipient: " + arr[1])));
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							clientTable.getQueue(myClientsName).offer(new Message("Server",
									("/error Incorrect usage of /accept:\n'/accept <user-name> <message>")));
						}
					}
					else if (arr[0].equalsIgnoreCase(Commands.MOVE))
					{
						try
						{
							// get the name of the player they are playing
							String opponent = clientTable.getOpponent(myClientsName);
							// try and parse the "coords" they gave into numbers
							int x = makeDoParseInt(arr[1]);
							int y = makeDoParseInt(arr[2]);

							if (x == -1 || y == -1)
							{
								throw new NumberFormatException();
							}

							// send a Move back to the player so they can act on it
							clientTable.getQueue(myClientsName)
									.offer(new Move(myClientsName, (opponent + " " + x + " " + y)));
							// send the same Move to the opponent
							clientTable.getQueue(opponent)
									.offer(new Move(myClientsName, (opponent + " " + x + " " + y)));
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							clientTable.getQueue(myClientsName)
									.offer(new Message("Server", "/error Incorrect usage of /move:\n'/move <x> <y>'"));
						}
						catch (NullPointerException e)
						{
							clientTable.getQueue(myClientsName)
									.offer(new Message("Server", "/error Your game probably ended"));
						}
						catch (NumberFormatException e)
						{
							clientTable.getQueue(myClientsName).offer(
									new Message("Server", "/error Please only use numbers 0, 1 and 2 as coordinates"));
						}
					}

					else if (arr[0].equalsIgnoreCase(Commands.PLAY))
					{
						try
						{
							// is the player trying to play themselves?
							if (!arr[1].equals(myClientsName))
							{
								// is the player not in a game?
								if (clientTable.isAvailable(myClientsName))
								{
									// is the opponent not in a game?
									if (clientTable.isAvailable(arr[1]))
									{
										// the player hasn't already sent them a request
										if (!clientTable.isWilling(myClientsName, arr[1]))
										{
											Request req = new Request(myClientsName);
											NotificationQueue queue = clientTable.getQueue(arr[1]);
											queue.offer(req);
											// now make it 'public' that we are willing to play with this person
											clientTable.addWilling(myClientsName, arr[1]);
											// tell the challenger we did his bidding
											clientTable.getQueue(myClientsName)
													.offer(new Message("Server", ("Request sent")));
										}
										else
										{
											clientTable.getQueue(myClientsName)
													.offer(new Message("Server", ("/error You have already challenged "
															+ arr[1] + ".\nWait for them to respond")));
										}
									}
									else
									{
										clientTable.getQueue(myClientsName).offer(new Message("Server",
												("/error " + arr[1] + " is in another game at the moment")));
									}
								}
								else
								{
									clientTable.getQueue(myClientsName)
											.offer(new Message("Server", ("/error You're in a game already!")));
								}
							}
							else
							{
								clientTable.getQueue(myClientsName)
										.offer(new Message("Server", ("/error You can't play yourself!")));
							}
						}
						catch (NullPointerException e)
						{
							clientTable.getQueue(myClientsName)
									.offer(new Message("Server", ("/error Invalid recipient: " + arr[1])));
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							clientTable.getQueue(myClientsName).offer(
									new Message("Server", ("/error Incorrect usage of /play:\n'/play <user-name>'")));
						}
					}

					// TODO make this tell the clients to start a game on their end
					else if (arr[0].equalsIgnoreCase(Commands.ACCEPT))
					{
						try
						{
							// make sure they aren't trying to accept themselves
							if (!arr[1].equalsIgnoreCase(myClientsName))
							{
								// make sure our client isn't already in a game
								if (clientTable.isAvailable(myClientsName))
								{
									// If the player whom we are accepting has actually sent us a request
									if (clientTable.isWilling(arr[1], myClientsName))
									{
										// and the opponent isn't in-game
										if (clientTable.isAvailable(arr[1]))
										{
											// make each player the opponent of the other
											clientTable.setOpponent(myClientsName, arr[1]);
											clientTable.setOpponent(arr[1], myClientsName);
											// make them unavailable to other players while the game is on
											clientTable.setAvailable(myClientsName, arr[1], false);
											// generate who goes first (true = cross)
											boolean xo = new Random().nextBoolean();
											// tell them we are starting a game
											clientTable.getQueue(myClientsName)
													.offer(new Message("Server", ("/game " + arr[1] + " " + xo)));
											clientTable.getQueue(arr[1]).offer(
													new Message("Server", ("/game " + myClientsName + " " + !xo)));
										}
										else
										{
											clientTable.getQueue(myClientsName).offer(new Message("Server", ("/error "
													+ arr[1] + " is currently in another game.\nTry again in a bit")));
										}
									}
									else
									{
										clientTable.getQueue(myClientsName).offer(new Message("Server",
												"/error " + (arr[1] + " has not requested to play with you :(\nType \""
														+ Commands.PLAY + " " + arr[1] + "\" to challenge them")));
									}
								}
								else
								{
									clientTable.getQueue(myClientsName).offer(
											new Message("Server", "/error Finish or quit the game you're in first!"));
								}
							}
							else
							{
								clientTable.getQueue(myClientsName).offer(new Message("Server",
										"/error Try as you might, you can never accept yourself :("));
							}
						}
						catch (NullPointerException e)
						{
							clientTable.getQueue(myClientsName)
									.offer(new Message("Server", ("/error Invalid recipient: " + arr[1])));
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							clientTable.getQueue(myClientsName).offer(new Message("Server",
									("/error Incorrect usage of /accept:\n'/accept <user-name>'")));
						}
					}

					else if (arr[0].equalsIgnoreCase(Commands.DECLINE))
					{
						try
						{
							// make sure they aren't trying to play with themselves
							if (!arr[1].equalsIgnoreCase(myClientsName))
							{

								if (clientTable.isAvailable(myClientsName))
								{
									// If the player whom we are declining has actually sent us a request
									if (clientTable.isWilling(arr[1], myClientsName))
									{
										clientTable.delWilling(myClientsName, arr[1]);
										clientTable.getQueue(myClientsName)
												// TODO how do we send this to the client for the GUI?
												.offer(new Message("Server", ("You declined " + arr[1])));
										clientTable.getQueue(arr[1]).offer(new Message("Server",
												(myClientsName + " declined your request to play :(")));
									}
									else
									{
										clientTable.getQueue(myClientsName).offer(new Message("Server",
												"/error " + (arr[1] + " has not requested to play with you :(\nType \""
														+ Commands.PLAY + " " + arr[1] + "\" to challenge them")));
									}
								}
								else
								{
									// TODO they probably should be able to decline games when they're in another
									clientTable.getQueue(myClientsName)
											.offer(new Message("Server", "/error You're in a game at the moment"));
								}
							}
							else
							{
								clientTable.getQueue(arr[1])
										.offer(new Message("Server", "/error Don't decline yourself - be happy :)"));
							}

						}
						catch (NullPointerException e)
						{
							clientTable.getQueue(myClientsName)
									.offer(new Message("Server", ("/error Invalid recipient: " + arr[1])));
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							clientTable.getQueue(myClientsName).offer(new Message("Server",
									("/error Incorrect usage of /decline:\n'/decline <user-name>'")));
						}
					}

					// this will most likely be for debugging only as the idea is for the clients to notified every time someone is added or removed
					else if (arr[0].equalsIgnoreCase(Commands.LIST))
					{
						//String strList = "Clients online: \n";
						String strList = Commands.LIST + " ";
						for (String client : clientTable.getKeySet())
						{
							strList = strList + client + " ";
						}
						clientTable.getQueue(myClientsName).offer(new Message("Server", strList.trim()));
					}

					else if (arr[0].equalsIgnoreCase(Commands.LEAVE))
					{
						leaveGame();
					}

					else if (arr[0].equalsIgnoreCase(Commands.EXIT))
					{
						this.notifyExited();
						myClient.close();
						return;
					}

					// only for debug printing
					else if (arr[0].equalsIgnoreCase("/debug"))
					{
						//clientTable.getQueue(myClientsName).offer(new Message("Server", clientTable.playing(myClientsName)));
					}

					// they've typed some gobbledegook and we ain't happy
					else
					{
						clientTable.getQueue(myClientsName)
								.offer(new Message("Server", ("/error Invalid command: '" + command + "'")));
					}
				}
				else
				{
					this.leaveGame();
					this.notifyExited();
					myClient.close();
					return;
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
		catch (

		IOException e)

		{
			this.notifyExited();
			// No point in trying to close sockets. Just give up.
			// We end this thread (we don't do System.exit(1)).
		}
		catch (InterruptedException e)
		{
			System.err.println("ServerReceiver interrupted");
		}
	}

	/**
	 * Sends a notification to all the clients online that someone disconnected
	 */
	private void notifyExited()
	{
		
		leaveGame(); // leave a game if they're in one
		
		clientTable.remove(myClientsName);
		System.out.println(myClientsName + " left");

		// generate a list of all the clients
		String clientList = Commands.LIST + " ";
		for (String client : clientTable.getKeySet())
		{
			clientList = clientList + client + " ";
		}
		// give each of those clients the list of all the clients
		for (String client : clientList.trim().split(" "))
		{
			if (!client.equals(Commands.LIST))
			{
				// we're not using ListUpdate notification type here as that is handled client-side
				clientTable.getQueue(client).offer(new Message("Server", clientList.trim()));
			}

		}
	}

	/**
	 * when a player leaves a game
	 */
	private void leaveGame()
	{
		// if my client is in a game when they have said they wish to leave
		try
		{
			if (!clientTable.isAvailable(myClientsName))
			{
				// tell the opponent their friend left them :(
				NotificationQueue oppQueue = clientTable.getQueue(clientTable.getOpponent(myClientsName));
				if (oppQueue != null)
				{
					oppQueue.offer(new Message("Server","Your opponent left :("));
					oppQueue.offer(new PlainNote("/reset"));
				}
				clientTable.getQueue(myClientsName).offer(new PlainNote("/reset"));
				String opponent = clientTable.getOpponent(myClientsName);
				// make both players "unwilling" to play the other so they can send requests again
				clientTable.delWilling(myClientsName, opponent);
				clientTable.delWilling(opponent, myClientsName);
				// make both players available again. This will end the game loop
				clientTable.setAvailable(myClientsName, true);
				clientTable.setAvailable(opponent, true);
				// remove the opponents
				clientTable.setOpponent(myClientsName, null);
				clientTable.setOpponent(opponent, null);
			}
		}
		catch (NullPointerException e)
		{
			clientTable.getQueue(myClientsName).offer(new Message("Server", "Your opponent already left"));
		}
	}

	private int makeDoParseInt(String str)
	{
		if (str.equals("0"))
		{
			return 0;
		}
		else if (str.equals("1"))
		{
			return 1;
		}
		else if (str.equals("2"))
		{
			return 2;
		}
		else
			return -1;
	}
}

/*
 * TODO compress command "listeners" into functions so it isn't so verbose
 * implement catch OutOfBoundsExceptions for all commands
 */

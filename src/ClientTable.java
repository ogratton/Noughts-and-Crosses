

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This makes a table of names as keys and a structure containing
 * 1) the notification (message and request) queue of the client
 * 2) the availability of the client (0 or 1)
 * 3) the list of players the client is willing to play with (has already sent a request to)
 * as the values
 */
public class ClientTable
{

	private ConcurrentMap<String, ClientInfo> queueTable = new ConcurrentHashMap<String, ClientInfo>();

	// TODO this can't cope with duplicate names yet
	
	/**
	 * Add a new client to the list
	 */
	public void add(String nickname)
	{
		queueTable.put(nickname, new ClientInfo(nickname));
	}

	/**
	 * Remove a player from the list (for when they disconnect)
	 * 
	 * @param nickname
	 *            the player who has left
	 */
	public void remove(String nickname)
	{
		queueTable.remove(nickname);
	}

	/**
	 * Return the queue of messages and requests for the player
	 * 
	 * @param nickname
	 *            the player
	 * @return the notification queue
	 */
	public NotificationQueue getQueue(String nickname)
	{
		return queueTable.get(nickname).getQueue();
	}

	/**
	 * Return the name of the player the client is currently playing
	 * 
	 * @param nickname
	 *            the player
	 * @return the name of the opponents
	 */
	public String getOpponent(String client)
	{
		return queueTable.get(client).playing();
	}

	/**
	 * Change who the client is playing
	 * @param client Client name
	 * @param opponent Opponent name (null if un-busy)
	 */
	public void setOpponent(String client, String opponent)
	{
		queueTable.get(client).setOpponent(opponent);
	}
	
	/**
	 * Used to check if a given client is available to play a game
	 * @param client nickname of client being inquired about
	 * @return true (if in lobby) or false (if in game)
	 */
	public boolean isAvailable(String client)
	{
		return queueTable.get(client).isAvailable();
	}

	/**
	 * Set the availability of a client on the server
	 * @param client Client being changed
	 * @param state new availability
	 */
	public void setAvailable(String client, boolean state)
	{
		queueTable.get(client).setAvailable(state);
	}
	
	/**
	 * Alternative "two birds with one stone" constructor for availability
	 * Sets availability of two clients at once seeing as it takes two to tango
	 * Other kept for posterity/potential necessity
	 * @param client player 1
	 * @param opponent player 2
	 * @param state new availability of both
	 */
	public void setAvailable(String client, String opponent, boolean state)
	{
		queueTable.get(client).setAvailable(state);
		queueTable.get(opponent).setAvailable(state);
	}
	
	/**
	 * Returns an ArrayList of the nicknames of players a client has requested to play
	 * 
	 * @param nickname
	 *            the name of the client concerned
	 * @return a list of all the players this client is willing to play
	 */
	public ArrayList<String> getWilling(String nickname)
	{
		return queueTable.get(nickname).getWilling();
	}

	/**
	 * Is the client willing to play the opponent?
	 * (Is the opponent in the client's willing list?)
	 * @param client
	 * @param opponent
	 * @return whether the client is willing to play the opponent
	 */
	public boolean isWilling(String client, String opponent)
	{
		return this.getWilling(client).contains(opponent);
	}

	/**
	 * add an opponent to the client's "willing" list
	 * 
	 * @param client
	 *            The client whose list is being edited
	 * @param opponent
	 *            The opponent being added
	 */
	public void addWilling(String client, String opponent)
	{
		queueTable.get(client).addWilling(opponent);
	}

	/**
	 * remove a client from an opponent's "willing" list (Client has declined)
	 * 
	 * @param client
	 *            The client being removed
	 * @param opponent
	 *            The opponents whose list is being edited
	 */
	public void delWilling(String client, String opponent)
	{
		queueTable.get(opponent).delWilling(client);
	}

	/**
	 * return the number of clients online
	 */
	public int size()
	{
		return queueTable.size();
	}

	/**
	 * returns a set of the keys (i.e. client names)
	 */
	public Set<String> getKeySet()
	{
		return queueTable.keySet();
	}

}

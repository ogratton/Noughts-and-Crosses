

import java.util.ArrayList;

/**
 * Class containing all the information the SERVER needs to know about a client
 * (Contrast with MyClient for CLIENT-known details)
 * @author Oliver
 *
 */
public class ClientInfo
{
	private String name;

	private NotificationQueue queue;
	private String opponent;
	private ArrayList<String> willing;

	private boolean available;

	/**
	 * Constructor initialises appropriate fields
	 * @param _name
	 */
	public ClientInfo(String _name)
	{
		name = _name;
		queue = new NotificationQueue();
		opponent = null;
		willing = new ArrayList<String>();
		available = true;
	}

	/**
	 * @return name of client
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return NotificationQueue of client
	 */
	public NotificationQueue getQueue()
	{
		return queue;
	}

	/**
	 * True if the client is online and not in-game
	 * @return availability of client
	 */
	public String playing()
	{
		return opponent;
	}

	/**
	 * Make it known to the server who this client is playing
	 * @param name new opponent's name
	 */
	public void setOpponent(String name)
	{
		opponent = name;
	}
	
	/**
	 * Whether the client is available to play a game or not
	 * @return true if in lobby and not in game, false if in game
	 */
	public boolean isAvailable()
	{
		return available;
	}
	
	/**
	 * Changed when player starts or leaves a game
	 * @param isAvailable new availability status
	 */
	public void setAvailable(boolean isAvailable)
	{
		available = isAvailable;
	}

	/**
	 * 
	 * @return A list of all the clients' names who this client is willing to play with (has sent a request to)
	 */
	public ArrayList<String> getWilling()
	{
		return willing;
	}

	/**
	 * Add a person to the willing list (i.e. this client has challenged someone)
	 * @param name person they have challenged
	 */
	public void addWilling(String name)
	{
		willing.add(name);
	}

	/**
	 * Remove someone from the willing list (the person has declined this client)
	 * @param name person being removed
	 */
	public void delWilling(String name)
	{
		willing.remove(name);
		
	}

}

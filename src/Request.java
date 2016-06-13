

/**
 * An instance of a notification made when a player requests to play with
 * another
 * Used in ServerReceiver
 * @author Oliver
 *
 */
public class Request implements Notification
{

	private final String sender;

	public Request(String sender)
	{
		this.sender = sender;
	}

	/**
	 * @return the sender of the request
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * what to do when a Message Notification is printed (print the following instead)
	 */
	public String toString()
	{
		return "New play request from " + sender + "\nType \"/accept " + sender + "\" or \"/decline " + sender
				+ "\" to respond";
	}

	/**
	 * Possibly useless but the intention is to work out what type of notification this is when dealing with notification queues and the like
	 */
	public String type()
	{
		return "Request";
	}
}
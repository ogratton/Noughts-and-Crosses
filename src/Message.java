
/**
 * A type of notification used primarily for communication between clients
 * Occasionally used otherwise but PlainNote has been used since its creation when no sender info is needed
 * @author Oliver
 *
 */
public class Message implements Notification
{

	private final String sender;
	private final String text;

	public Message(String sender, String text)
	{
		this.sender = sender;
		this.text = text;
	}

	/**
	 * @return the name of the sender
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * @return the contents of the message sent
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * what to do when a Message Notification is printed (print the following instead)
	 */
	public String toString()
	{
		if (sender.equalsIgnoreCase("Server"))
		{
			return text;
		}
		else
		{
			return "From " + sender + ": " + text;
		}
	}

	
	/**
	 * Possibly useless but the intention is to work out what type of notification this is when dealing with notification queues and the like
	 */
	public String type()
	{
		return "Message";
	}
}

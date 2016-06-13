

/**
 * For when you just want to send a plain string with no sender gubbins to a queue
 * @author Oliver
 *
 */
public class PlainNote implements Notification
{

	private final String text;
	
	public PlainNote(String text)
	{
		this.text = text;
	}
	
	/**
	 * what to do when a Message Notification is printed (print the following instead)
	 */
	public String toString()
	{
		return text;
	}
	
	/**
	 * Possibly useless but the intention is to work out what type of notification this is when dealing with notification queues and the like
	 */
	public String type()
	{
		return "Plain";
	}

}

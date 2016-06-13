
/**
 * A type of notification used by the server to send moves to clients
 * @author Oliver
 *
 */
public class Move implements Notification
{

	private final String sender;
	private final String text;

	public Move(String sender, String text)
	{
		this.sender = sender;
		this.text = text;
	}

	/**
	 * @return name of sender (server lies and says it's from the typer of the move rather than itself)
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * @return Contents of the move
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * what to do when a Move Notification is printed (print the following instead)
	 */
	public String toString()
	{
		// when turned into an array:
		//0=sender, 1=opponent, 2=x, 3=y
		// e.g. when Oliver moves in the top left corner against Billy:
		// "Oliver Billy 0 0"
		return sender + " " + text;
	}

	/**
	 * Possibly useless but the intention is to work out what type of notification this is when dealing with notification queues and the like
	 */
	public String type()
	{
		return "Move";
	}

}

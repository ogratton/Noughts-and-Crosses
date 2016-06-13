
/**
 * General type passed between clients and servers
 * @author Oliver
 *
 */
public interface Notification
{
	/**
	 * what to do when a Message Notification is printed (print the following instead)
	 */
	public String toString();

	/**
	 * Possibly useless but the intention is to work out what type of notification this is when dealing with notification queues and the like
	 */
	public String type();

}

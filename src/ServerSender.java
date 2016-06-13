

import java.io.PrintStream;

/**
 * Continuously reads from message queue for a particular client,
 * forwarding to the client.
 * @author Oliver
 *
 */
public class ServerSender extends Thread
{
	private final int SLEEP_TIME = 10;
	
	private NotificationQueue queue;
	private PrintStream client;

	public ServerSender(NotificationQueue q, PrintStream c)
	{
		queue = q;
		client = c;
	}

	public void run()
	{
		try
		{
			while (true)
			{
				Notification note = queue.take();
				client.println(note);
				
				Thread.sleep(SLEEP_TIME);

			}
		}
		catch (InterruptedException e)
		{
			System.err.println("ServerSender interrupted");
		}
	}
}

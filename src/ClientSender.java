

import java.io.PrintStream;

// First tells the user's nickname to the server.  Then repeatedly
// reads recipient's nickname and text from the user in two separate
// lines, sending them to the server (read by ServerReceiver thread).

/**
 * Constantly takes from the queue and tells the server what it took
 * @author Oliver
 *
 */
public class ClientSender extends Thread
{

	private final int SLEEP_TIME = 50;

	private String name;
	private MyClient info;
	private PrintStream server;
	private NotificationQueue queue;

	ClientSender(MyClient _info, PrintStream _server, NotificationQueue _queue)
	{
		this.info = _info;
		this.name = info.getName();
		this.server = _server;
		this.queue = _queue;
	}

	public void run()
	{

		try
		{
			// Tell the server what my nickname is on startup:
			server.println(name);

			// Then loop forever sending messages from the queue to the server:
			while (true)
			{

				// if you aren't trying to move when it's not your turn send the command on to the queue
				Notification note = queue.take();
				if (note != null)
				{
					if (!(!info.getTurn() && note.toString().split(" ")[0].equals(Commands.MOVE)))
					{
						//System.out.println(">> FROM QUEUE: " + note);
						server.println(note);
					}

					else
					{
						System.out.println("Not your turn!");
					}
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

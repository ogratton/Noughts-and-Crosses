

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Passes on all that is typed to the console to the queue (which is sent to the server in ClientSender
 * @author Oliver
 *
 */
public class BufferToQueue extends Thread
{
	private final int SLEEP_TIME = 50;

	private NotificationQueue queue;

	public BufferToQueue(MyClient _info, PrintStream _server, NotificationQueue _queue)
	{
		queue = _queue;
	}

	public void run()
	{

		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			while (true)
			{
				// try and read from the console
				String command = user.readLine();
				if (command.length() > 0)
				{

					queue.offer(new PlainNote(command));;

				}

				Thread.sleep(SLEEP_TIME);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Communication broke in ClientServer");
			System.exit(1);
		}
	}
}

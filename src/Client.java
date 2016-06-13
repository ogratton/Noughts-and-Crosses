

// Usage:
//        java Client user-nickname host-name
//
// After initialising and opening appropriate sockets, we start two
// client threads, one to send messages, and another one to get
// messages.
//
// A limitation of our implementation is that there is no provision
// for a client to end after we start it. However, we implemented
// things so that pressing ctrl-c will cause the client to end
// gracefully without causing the server to fail.
//
// Another limitation is that there is no provision to terminate when
// the server dies.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * The starter of all the necessary Client threads
 * Usage: java Client user-nickname host-name
 * @author Oliver
 *
 */
public class Client
{
	
	public static void main(String[] args)
	{

		// Check correct usage:
		if (args.length != 2)
		{
			System.err.println("Usage: java Client user-nickname host-name");
			System.exit(1); // Give up.
		}

		// Initialise information:
		String nickname = args[0];
		String hostname = args[1];

		// Open sockets:
		PrintStream toServer = null;
		BufferedReader fromServer = null;
		Socket server = null;
		NotificationQueue toServerQueue = new NotificationQueue();

		try
		{
			server = new Socket(hostname, Port.number);
			toServer = new PrintStream(server.getOutputStream());
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
		}
		catch (UnknownHostException e)
		{
			System.err.println("Unknown host: " + hostname);
			System.exit(1); // Give up.
		}
		catch (IOException e)
		{
			System.err.println("The server doesn't seem to be running " + e.getMessage());
			System.exit(1); // Give up.
		}

		// Create two client threads:
		MyClient info = new MyClient(nickname);		
		
		BufferToQueue buff = new BufferToQueue(info, toServer, toServerQueue);
		ClientSender sender = new ClientSender(info, toServer, toServerQueue);
		ClientReceiver receiver = new ClientReceiver(info, fromServer, toServerQueue);

		// Start up the GUI
		JFrame frame = new JFrame("Noughts & Crosses by oxg558 - "+info.getName());
		GUI comp = new GUI(toServerQueue, info);
		frame.add(comp);
		frame.setSize(950, 529);
		frame.setLocation(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);

		// Run them in parallel:
		buff.start();
		sender.start();
		receiver.start();

		// Wait for them to end and close sockets.
		try
		{
			sender.join();
			toServer.close();
			receiver.join();
			fromServer.close();
			server.close();
		}
		catch (IOException e)
		{
			System.err.println("Something wrong " + e.getMessage());
			System.exit(1); // Give up.
		}
		catch (InterruptedException e)
		{
			System.err.println("Unexpected interruption " + e.getMessage());
			System.exit(1); // Give up.
		}
	}
}

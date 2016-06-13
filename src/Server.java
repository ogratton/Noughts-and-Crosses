

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
// Usage:
//        java Server
//
// There is no provision for ending the server gracefully.  It will
// end if (and only if) something exceptional happens.
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server to which all clients connect
 * @author Oliver
 *
 */
public class Server
{

	public static void main(String[] args)
	{

		// This will be shared by the server threads:
		ClientTable clientTable = new ClientTable();

		// Open a server socket:
		ServerSocket serverSocket = null;

		// We must try because it may fail with a checked exception:
		try
		{
			serverSocket = new ServerSocket(Port.number);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't listen on port " + Port.number);
			System.exit(1); // Give up.
		}

		// Good. We succeeded. But we must try again for the same reason:
		try
		{
			// We loop for ever, as servers usually do:

			while (true)
			{
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();

				// This is so that we can use readLine():
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// We ask the client what its name is (this is the first action in ClientSender):
				String clientName = fromClient.readLine();

				// For debugging:
				System.out.println(clientName + " connected");

				// We add the client to the tables:
				// TODO tell ALL clients about this change
				clientTable.add(clientName);

				// generate a list of all the clients
				String clientList = Commands.LIST + " ";
				for (String client : clientTable.getKeySet())
				{
					clientList = clientList + client + " ";
				}
				// give each of those clients the list of all the clients
				for (String client : clientList.trim().split(" "))
				{
					if (!client.equals(Commands.LIST))
					{
						// we're not using ListUpdate notification type here as that is handled client-side
						clientTable.getQueue(client).offer(new Message("Server", clientList.trim()));
					}

				}

				// We create and start a new thread to read from the client:
				(new ServerReceiver(clientName, fromClient, clientTable)).start();

				// We create and start a new thread to write to the client:
				PrintStream toClient = new PrintStream(socket.getOutputStream());
				(new ServerSender(clientTable.getQueue(clientName), toClient)).start();
			}
		}
		catch (IOException e)
		{
			// Lazy approach:
			System.err.println("IO error " + e.getMessage());
			// A more sophisticated approach could try to establish a new
			// connection. But this is beyond this simple exercise.
		}
	}
}

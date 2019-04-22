/*
Waits for client to join and sets up sockets
*/
import java.net.*;
import java.io.*;

public class JoinThread extends Thread {
	private ServerSocket serverSock;
	private Server serv;

	public JoinThread(Server serv, int port)
	{
		try {
			serverSock = new ServerSocket(port);
		}
		catch(Exception e)
		{
			System.out.println("Attempted port was illegal");
			System.exit(0);
		}
		this.serv=serv;
	}

	public void run()
	{	//waits for someone to connect, then tells the server about them
		Socket sock;
		while(true)
		{
			try {
				sock = serverSock.accept();
				serv.addPlayer(new SPlayer(new DataInputStream(sock.getInputStream()), new DataOutputStream(sock.getOutputStream()),serv));
			}
			catch(Exception e)
			{}
		}
	}
}
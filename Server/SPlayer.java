import java.io.*;
import java.net.*;

//Player class for client, S=server
public class SPlayer {
	private Server serv;	//reference to server
	private DataInputStream in;	//thing that gets messages
	private DataOutputStream out;	//thing to send messages
	
	// position
	public int x = 0;
	public int y = 0;
	// current movement, changed by keyPress and keyRelease
	public int dx = 0;
	public int dy = 0;

	public SPlayer(DataInputStream in, DataOutputStream out, Server serv)
	{
		this.serv = serv;
		this.in   = in;
		this.out  = out;
		//this thread gets messages from client (key presses) and passes them onto the server
		Thread th = new Thread()
		{	//anonymous class
			public void run() {
				while(true)
				{
					try {
						serv.parseMessage(SPlayer.this,in.readUTF());
					}
					catch(Exception e)
					{	//if player quits
						System.out.println("Exception in SPlayer read thread: "+e);	//debug
						serv.removePlayer(SPlayer.this);
						break;
					}
				}
			}
		};
		th.start();
	}

	public void sendMessage(String str) throws IOException
	{	out.writeUTF(str);	}

	public int getSpeed()
	{	return 5;	}

  // essential method for SPlayers, movement
	public void step() {
		x = x + dx;
		y = y + dy;
		if(serv.wallContainsPoint(x,y) || serv.wallContainsPoint(x+WIDTH,y) || serv.wallContainsPoint(x,y+HEIGHT) || serv.wallContainsPoint(x+WIDTH,y+HEIGHT))
		{
			x -= dx;
			y -= dy;
		}
	}
}

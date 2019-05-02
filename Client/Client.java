import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JPanel implements Runnable,KeyListener {
	private static int FPS = 50;
	
	private DataInputStream in;
	private DataOutputStream out;
	private FakeList players;
	private FakeList walls;
	
	public Client(DataInputStream in, DataOutputStream out)
	{
		Images.setUp();
		this.in = in;
		this.out = out;
		players = new FakeList(5,1,this);
		walls = new FakeList(20,5,this);
		Thread th = new Thread()
		{
			public void run()
			{
				while(true)
				{
					String s=null;
					try {
						s = in.readUTF();
					}
					catch(Exception e)
					{
						System.out.println("Host disconnected");
						System.exit(0);
					}
					Client.this.parseMessage(s);
				}
			}
		};
		th.start();
	}
	
	public void run() {
		int delay = 1000/FPS;
		long startTime=System.currentTimeMillis();
		int i=1;
		while(true)
		{
			try { waitTime((i++)*delay+startTime-System.currentTimeMillis()); } catch(Exception e) {}
			repaint();
		}
	}

	//Helper method for run() loop
	private void waitTime(long n) throws InterruptedException {
		if(n>0)
			Thread.sleep((int)n);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	//idk what this does
		walls.paint(g);			//paint everything in walls
		players.paint(g);		//paint everything in players
	}
	
	public void addNotify()
	{	//starts the thread when the game starts
		super.addNotify();
		(new Thread(this)).start();
	}
	
	public void parseMessage(String instr)
	{
		//System.out.println("Parsing message: "+instr);	//debug
		String[] input = instr.split(" ");
		try {
			if(input[0].equals("set"))
			{
				if(input[1].equals("player") || input[1].equals("players"))
				{
					players.list[Integer.parseInt(input[2])].setCords(Integer.parseInt(input[3]),Integer.parseInt(input[4]));
				}
				if(input[1].equals("wall") || input[1].equals("walls"))
					walls.list[Integer.parseInt(input[2])].setCords(Integer.parseInt(input[3]),Integer.parseInt(input[4]));
			}
			else if(input[0].equals("add"))
			{
				if(input[1].equals("player") || input[1].equals("players"))
          players.add(new Drawable(Integer.parseInt(input[2]),Integer.parseInt(input[3]),Images.getImage("player")));
				if(input[1].equals("wall") || input[1].equals("walls"))
					walls.add(new Drawable(Integer.parseInt(input[2]),Integer.parseInt(input[3]),Images.getImage("wall")));
			}
			else if(input[0].equals("remove"))
			{
				if(input[1].equals("player") || input[1].equals("players"))
					players.remove(Integer.parseInt(input[2]));
				if(input[1].equals("wall") || input[1].equals("walls"))
					walls.remove(Integer.parseInt(input[2]));
			}
		}
		catch(Exception e)
		{
			System.out.println("Bad message caught: "+instr+" has problem "+e);	//debug
		}
	}
	
	public void sendMessage(String str)
	{	//sends a message to the server
		try {
			out.writeUTF(str);
		}
		catch(Exception e)
		{
			System.out.println("Host disconnected (Error code 1B)");
			System.exit(0);
		}
	}
	
	public void keyPressed(KeyEvent k) {
		sendMessage("press "+k.getKeyCode());
	}
	public void keyReleased(KeyEvent k) {
		sendMessage("release "+k.getKeyCode());
	}
	public void keyTyped(KeyEvent k) {}
}

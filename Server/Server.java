import java.io.*;

public class Server implements Runnable {

	public static final int FPS = 25;
	public static final int maxPlayers = 3;
	public SPlayer[] players;
	public Wall[] walls;

	public JoinThread jt;

	public Server() {
		players = new SPlayer[0];
		jt = new JoinThread(this,255);
		jt.start();
		setup();
	}
	
	public void setup()
	{
		int[] wallX = new int[] {};
		int[] wallY = new int[] {};
		walls = new Wall[wallX.length];
		for(int i=0;i<wallX.length;i++)
		{
			walls[i]=new Wall(wallX[i],wallY[i]);
		}
	}

	// the server is a thread
	public void run() {
		int delay = 1000/FPS;
		// loop is using global time, to account for real time delay in the computer's steps
		long startTime=System.currentTimeMillis();
		int i=1;
		while(true)
		{
			try { waitTime((i++)*delay+startTime-System.currentTimeMillis()); } catch(Exception e) {}
			step();
		}
	}

	//Helper method for run() loop
	private void waitTime(long n) throws InterruptedException {
		if(n>0)
			Thread.sleep((int)n);
	}

	private void step()
	{
		for(SPlayer player : players)
			player.step();	//move players
		for(int i=0;i<players.length;i++)
		{
			sendMessage("set players "+i+" "+players[i].x+" "+players[i].y);
		}
	}
	
	public boolean wallContainsX(int testX)
	{
		for(Wall w:walls)
		{
			if(w.isXIn(testX))
				return true;
		}
		return false;
	}
	
	public boolean wallContainsY(int testY)
	{
		for(Wall w:walls)
		{
			if(w.isYIn(testY))
				return true;
		}
		return false;
	}
	
	public boolean wallContainsPoint(int testX, int testY)
	{
		for(Wall w:walls)
		{
			if(w.isPointIn(testX,testY))
				return true;
		}
		return false;
	}

	public void sendMessage(String msg)
	{
		for(int i=0;i<players.length;i++)
		{
			try {
				players[i].sendMessage(msg);
			}
			catch(Exception e)
			{
				removePlayer(i);
			}
		}
	}

	public void parseMessage(SPlayer guy, String instr)
	{
		//System.out.println("Got message: "+instr);	//debug
		// key numbers, https://keycode.info/
		String[] input = instr.toLowerCase().split(" ");
		//remember to use parseInt() to get integers
		try {	//try block catches incomplete messages
			if(input[0].equals("press"))
			{
				int key = Integer.parseInt(input[1]);
				//System.out.println("Player pressed "+key+" key");	//debug
				if(key==65)	//a key
					guy.dx=(-1)*guy.getSpeed();
				else if(key==68)	//d key
					guy.dx=guy.getSpeed();
				else if(key==83)	//s key
					guy.dy=guy.getSpeed();
				else if(key==87)	//w key
					guy.dy=(-1)*guy.getSpeed();
			}
			else if(input[0].equals("release"))
			{
				int key = Integer.parseInt(input[1]);
				if(key==65 || key==68)	//a or d
					guy.dx=0;
				if(key==83 || key==87)	//w or s
					guy.dy=0;
			}
			else if(input[0].equals("click"))
			{
				//create projectile
				//addProjectile(guy,Integer.parseInt(input[1]),Integer.parseInt(input[2]));
			}
		}
		catch(Exception e)
		{	System.out.println("Caught bad message exception");	}	//debug
	}

	public void addPlayer(SPlayer newbie)
	{
		//Tell everyone about the noob:
		sendMessage("add players "+newbie.x+" "+newbie.y);
		
		//Tell the noob about the walls:
		for(Wall w: walls)
		{
			try {
				newbie.sendMessage("add walls "+w.getX()+" "+w.getY());
			}
			catch(Exception e){
				System.out.println("Error sending message to new player");
			}
		}
		
		//Tell the noob about everyone:
		for(int i=0;i<players.length;i++)
		{
			try {
				newbie.sendMessage("add players "+players[i].x+" "+players[i].y);
			}
			catch(Exception e){
				System.out.println("Error sending message to new player");
			}
		}

		//Tell the noob about himself
		try {
			newbie.sendMessage("add players "+newbie.x+" "+newbie.y);
		}
		catch(Exception e)
		{	System.out.println("Error sending message to new player");	}

		//currently maxPlayers does nothing
		SPlayer[] temparr = new SPlayer[players.length+1];
		for(int i=0;i<players.length;i++)
			temparr[i] = players[i];
		temparr[temparr.length-1]=newbie;
		players=temparr;


		System.out.println("Added player.  Now have "+players.length+" players");	//debug
	}

	public void removePlayer(int index)
	{
		SPlayer[] temparr = new SPlayer[players.length-1];
		for(int i=0;i<index;i++)
			temparr[i]=players[i];
		for(int i=index+1;i<players.length;i++)
			temparr[i-1]=players[i];
		players=temparr;
		System.out.println("Removed player "+index);	//debug
		
		//tell the bois:
		sendMessage("remove players "+index);
	}
	
	public void removePlayer(SPlayer p)
	{
		for(int i=0;i<players.length;i++)
		{
			if(players[i]==p)	//ok because pointing to same location in memory
			{
				removePlayer(i);
				return;
			}
		}
		System.out.println("Failed to find player to remove");	//debug
	}
}

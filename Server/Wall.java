public class Wall {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 32;
	
	private int x;
	private int y;

	public Wall(int x, int y)
	{
		this.x=x;		this.y=y;
	}
	
	public int getX()
	{	return x;	}
	public int getY()
	{	return y;	}

	public boolean isPointIn(int testX, int testY)
	{	//returns if a point is in the wall
		return isXIn(testX) && isYIn(testY);
	}

	public boolean isXIn(int testX)
	{	//returns if a x cord is in the wall
		return (x<=testX) && (testX<x+WIDTH);
	}

	public boolean isYIn(int testY)
	{	//returns if a y cord is in the wall
		return (y<=testY) && (testY<y+HEIGHT);
	}

	public void getHit()
	{	//wall gets hit with an attack
		//currently nothing, gets overridden in child classes
	}
}

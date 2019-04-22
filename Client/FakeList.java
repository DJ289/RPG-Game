import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FakeList {
	public Drawable[] list;
	private int curMax;
	private int expandBy;
	private Client client;
	
	public FakeList(int startCap, int expandBy, Client client)
	{
		this.expandBy = expandBy;
		this.client = client;
		list = new Drawable[startCap];
		curMax = 0;
	}
	
	public void paint(Graphics g)
	{
		for(int i=0;i<curMax;i++)
			g.drawImage(list[i].getImage(),list[i].getX(),list[i].getY(),client);
	}
	
	private void expand()
	{
		Drawable[] temp = new Drawable[list.length+expandBy];
		for(int i=0;i<curMax;i++)
			temp[i]=list[i];
		list=temp;
	}
	
	public void add(Drawable thing)
	{
		if(curMax==list.length)
			expand();
		list[curMax++]=thing;
	}
	
	public void remove(int at)
	{
		for(int i=at;i<curMax-1;i++)
			list[i]=list[i+1];
		curMax--;
	}
}
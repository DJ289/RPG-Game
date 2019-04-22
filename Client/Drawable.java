import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Drawable {
	int x,y;
	Image pic;
	
	public Drawable(int x, int y, Image pic)
	{
		this.x=x;
		this.y=y;
		this.pic=pic;
	}
	
	public int getX()
	{	return x;	}
	public int getY()
	{	return y;	}
	public Image getImage()
	{	return pic;	}
	
	public void setCords(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void setPic(Image pic)
	{
		this.pic=pic;
	}
}
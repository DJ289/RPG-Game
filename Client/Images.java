import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Images {
	private static String[] names;
	private static Image[] pics;
	
	public static void setUp()
	{	//gets the images
		String[] files = (new File("images/")).list();
		names = new String[files.length];
		pics = new Image[files.length];
		for(int i=0;i<files.length;i++)
		{
			names[i]=files[i].split(".")[0];
			pics[i]=new ImageIcon("images/"+files[i]).getImage();
		}
	}
	
	public static Image getImage(String instr)
	{
		instr=instr.toLowerCase();
		for(int i=0;i<names.length;i++)
		{
			if(instr.equals(names[i]))
				return pics[i];
		}
		return getImage("notfound");
	}
}
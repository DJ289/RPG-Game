import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) {
		
		//open communciations with server:
		Scanner scan = new Scanner(System.in);
		System.out.print("\nHost ip: ");
		String ip = scan.nextLine();
		System.out.print("\nHost port: ");
		Socket sock = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			sock = new Socket(ip, Integer.parseInt(scan.nextLine()));
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
		}
		catch(Exception e)
		{
			System.out.println("Couldn't find host");
			System.exit(0);
		}
		
		//Create the JFrame and JPanel:
		JFrame frame = new JFrame();
		Client client = new Client(in, out);
		
		frame.setVisible(true);
		frame.add(client);
		frame.addKeyListener(client);
		frame.setSize(1200,800);
		frame.setTitle("Totally Not a Diablo Ripoff");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		client.setBackground(Color.BLACK);
	}
}
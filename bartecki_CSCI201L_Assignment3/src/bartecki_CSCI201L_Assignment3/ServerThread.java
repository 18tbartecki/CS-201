package bartecki_CSCI201L_Assignment3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class ServerThread extends Thread {
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Server server;
	private int drivers;
	private static Vector<Delivery> deliveriesReady = new Vector<Delivery>();
	private static boolean allDeliveriesInQueue = false;
	
	//Initializing new serverThread with main server
	public ServerThread(Socket s, Server server, int drivers) {
		try {
			this.server = server;
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.drivers = drivers;
			this.start();
		} 
		catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}

	//Sends message to client associated with the thread
	public void sendMessage(Message<?> message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		try
		{
			//Prints the appropriate message depending on how many drivers are still needed
			if(drivers > 1)
			{
				Message<String> m = new Message<String>("driversMessage", drivers + " more drivers needed before the service can begin. Waiting...\n");
				server.broadcast(m);
			}
			else if(drivers == 1)
			{
				Message<String> m = new Message<String>("driversMessage", drivers + " more driver is needed before the service can begin. Waiting...\n");
				server.broadcast(m);
			}
			else
			{
				Message<String> m = new Message<String>("arrived", "All drivers have arrived!\nStarting service.\n");
				server.broadcast(m);
			}
			
			//Continues until no more deliveries are left
			while(true)
			{
				Thread.sleep(500);
				synchronized(this)
				{
					//If any deliveries are ready to be taken, the thread will send the list to its associated driver
					if(!deliveriesReady.isEmpty())
					{
						Message<Vector<Delivery>> m = new Message<Vector<Delivery>>("orders", deliveriesReady);
						sendMessage(m);
						oos.reset();
						clearDeliveriesReady();	
						
						Message<?> done = (Message<?>)ois.readObject();
					}
					//If every delivery is ready and the queue has no deliveries left, all have been dispersed
					if(allDeliveriesInQueue && deliveriesReady.isEmpty())
					{
						break;
					}
				}
			}
			
		}
		catch(InterruptedException ie)
		{
			System.out.println("interrupted exception");
		}
		catch(IOException ioe)
		{
			System.out.println("io exception");
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("class not found exception");
		}
	}
	
	//Sets to true when last delivery is added to deliveriesReady queue
	public static void QueueDone()
	{
		allDeliveriesInQueue = true;
	}
	
	public static void addToDeliveriesReady(Delivery d)
	{
		deliveriesReady.add(d);
	}
	
	public static Vector<Delivery> getDeliveriesReady()
	{
		return deliveriesReady;
	}
	
	//Clears queue when orders are removed
	public static void clearDeliveriesReady()
	{
		deliveriesReady.clear();
	}

}

package bartecki_CSCI201L_Assignment3;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;


public class Driver extends Thread {
	
	//Obtain server information and begin a new client
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		System.out.print("Welcome to SalEats v2.0!\nEnter the server hostname: ");
		String hostname = s.nextLine();
		System.out.print("Enter the server port: ");
		int port = validPort(s);
		Driver d = new Driver(hostname, port);	
	}
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Vector<Delivery> currentDeliveries;
	private double hqlatitude = 34.02116;
	private double hqlongitude = -118.287132;
	private Socket s;
	
	//Creates new driver associated with a specific server
	public Driver(String hostname, int port) 
	{
		try
		{
			s = new Socket(hostname, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			hqlatitude = 0;
			hqlongitude = 0;
			this.start();
		}
		catch(IOException ioe)
		{
			System.out.println("Disconnected");
		}
		
	}

	
	public void run() {
		try
		{
			//Continue until associated serverThread dies
			while(true)
			{
				String type = "";
				//Obtain a message from server and take appropriate action
				Message<?> m = (Message<?>)ois.readObject();
				type = m.getType();
				
				if(type.equals("driverMessage"))
				{
					String data = (String) m.getMessage();
					System.out.println(data);
				}
				else if(type.equals("latitude"))
				{
					Double data = (Double) m.getMessage();
					hqlatitude = Double.valueOf(data);	
				}
				else if(type.equals("longitude"))
				{
					Double data = (Double) m.getMessage();
					hqlongitude = Double.valueOf(data);	
				}
				else if(type.equals("arrived"))
				{
					String data = (String) m.getMessage();
					System.out.println(data);
				}
				//New list of orders sent to be delivered
				else if(type.equals("orders"))
				{
					currentDeliveries = (Vector<Delivery>) m.getMessage();
					startDelivery(currentDeliveries);
					Message<String> done = new Message<String>("orderDone", "continue");
					oos.writeObject(done);
					oos.flush();
				}
				//Prints the final line
				else
				{
					String data = (String) m.getMessage();
					System.out.println(data);
				}
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("Object transfer error");
		}
		catch(IOException ioe)
		{
			
		}
	}
			
			
	//Delivers all orders in the vector received  		
	public void startDelivery(Vector<Delivery> currentDeliveries)	
	{
		try{
			double startingLatitude = hqlatitude;
			double startingLongitude = hqlongitude;
			double distanceToHQ = 0;
			
			//Print the starting statement for each order
			for(int i = 0; i < currentDeliveries.size(); i++)
			{
				currentDeliveries.get(i).startingDelivery();
			}
			
			while(!currentDeliveries.isEmpty())
			{
				int index = 0;
				double shortestDistance = calcDistance(currentDeliveries.get(0).getRestaurant(), startingLatitude, startingLongitude);
				//Determine the restaurant closest to current location
				for(int i = 1; i < currentDeliveries.size(); i++)
				{
					double comparer = calcDistance(currentDeliveries.get(i).getRestaurant(), startingLatitude, startingLongitude);
					if(comparer < shortestDistance)
					{
						shortestDistance = comparer;
						index = i;
					}
				}
				
				//Sleeps for distance to restaurant
				Thread.sleep((long) (shortestDistance * 1000)); 	
				
				currentDeliveries.get(index).finishingDelivery();
				//If any deliveries are at the same restaurant make sure to deliver them all
				for(int i = 0; i < currentDeliveries.size(); i++)
				{
					if(currentDeliveries.get(i).getrestaurantName().equals(currentDeliveries.get(index).getrestaurantName()) && i != index)
					{
						currentDeliveries.get(i).finishingDelivery();
						currentDeliveries.remove(i);
					}
				}
				//Update new starting location for next iteration
				startingLatitude = currentDeliveries.get(index).getLatitude();
				startingLongitude = currentDeliveries.get(index).getLongitude();
				distanceToHQ = calcDistance(currentDeliveries.get(index).getRestaurant(), hqlatitude, hqlongitude);
				currentDeliveries.remove(index);
				
				for(int i = 0; i < currentDeliveries.size(); i++)
				{
					currentDeliveries.get(i).continuingDelivery();
				}
			}	
				
			System.out.println(java.time.LocalTime.now() + " Finished all deliveries, returning back to HQ.");
			
			//Sleep for distance back to HQ
			Thread.sleep((long) (1000*distanceToHQ));
			System.out.println(java.time.LocalTime.now() + " Returned to HQ.");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
			
	}
		
	
		
	//Calculates distance to a given restaurant
	public static double calcDistance(Restaurant r, double latitude, double longitude)
	{
		double lat1 = Math.toRadians(latitude);
		double lat2 = Math.toRadians(r.getCoordinates().getLatitude());
		double long1 = Math.toRadians(longitude);
		double long2 = Math.toRadians(r.getCoordinates().getLongitude());
		
		double distance = 3963.0 * Math.acos((Math.sin(lat1) * Math.sin(lat2)) + 
				Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1));
		return distance;
	}
	
	//Checks that port value is valid 
	public static int validPort(Scanner s)
	{
		boolean valid = false;
		int port = 0;
		while(!valid)
		{
			try 
			{
				String userinput = s.nextLine();
				System.out.println("");
				port = Integer.parseInt(userinput);
				//If not positive ask again
				if(port > 0 && port < 65536)
				{
					valid = true;
				}
				else
				{
					System.out.print("Invalid port.\n" + "Reenter port. ");
				}
			}
			catch(NumberFormatException nfe)
			{
				System.out.print("Invalid port.\n" + "Reenter port. ");
			}
		}
		return port;
	}
		
}

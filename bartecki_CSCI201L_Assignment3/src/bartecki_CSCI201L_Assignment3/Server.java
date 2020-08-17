package bartecki_CSCI201L_Assignment3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;


public class Server {
	//Create a new server and run it
	public static void main(String []args)
	{
		Server s = new Server(3456);
	}
	
	private Vector<ServerThread> serverThreads;
	Vector<Delivery> deliveries = new Vector<Delivery>();
	static Vector<Delivery> deliveriesReady = new Vector<Delivery>();
	
	public Server(int port)
	{
		FileReader fr;
		BufferedReader br;
		ServerSocket ss = null;
		Scanner s = new Scanner(System.in);
		
		
		System.out.print("What is the name of the schedule file? ");
		String schedulefile = s.nextLine();
		System.out.println();
		
		boolean validschedulefile = false;
		
		//Repeats until file is found and proper
		while(!validschedulefile)
		{
			try 
			{
				//Obtaining all deliveries from file 
				fr = new FileReader(schedulefile);
				br = new BufferedReader(fr);
				String line = br.readLine();
				
				//Parsing each line of schedule file
				while(line != null)
				{
					String[] info = line.split(", ");
					int tempWait = Integer.parseInt(info[0]);
					String tempRestaurant = info[1].replaceAll("’", "'");
					String tempItem = info[2];
					
					Delivery newdelivery = new Delivery(tempWait, tempRestaurant, tempItem, null);
					deliveries.add(newdelivery);
					
					line = br.readLine();
				}
				validschedulefile = true;
				
			}
			catch(FileNotFoundException fnfe) 
			{
				System.out.println("That file does not exist. What is the name of the schedule file?\n");
			} 
			catch(NumberFormatException nfe)
			{
				System.out.println("That file is not properly formatted. What is the name of the schedule file.\n");
			}
			catch(IOException ie)
			{
				System.out.println("That file is not properly formatted. What is the name of the schedule file.\n");
			}
			catch(ArrayIndexOutOfBoundsException aioobe)
			{
				System.out.println("That file is not properly formatted. What is the name of the schedule file.\n");
			}
			
			//Repeating question if file not valid
			if(!validschedulefile)
			{
				System.out.print("What is the name of the schedule file? ");
				schedulefile = s.nextLine();	
				System.out.println();
			}
		}
		
		//Obtaining lat/long for location and number of drivers
		System.out.print("What is your latitude? ");
		double latitude = validLat(s);
		System.out.print("What is your longitude? ");
		double longitude = validLong(s);
				
		System.out.print("How many drivers will be in service today?\n");
		int num_drivers = validDrivers(s);
		
		//Connecting to the yelp API to obtain locations for each restaurant
		RestaurantParser parser = new RestaurantParser();
		Vector<Restaurant> allRestaurants = new Vector<Restaurant>();
		YelpService yp = new YelpService();
		Gson gson = new Gson();
		
		String name = deliveries.get(0).getrestaurantName().replaceAll("’", "'");
		//Searches the yelp API, getting data into a string
		String next = yp.findRestaurant(name, latitude, longitude);
		
		//Using RestaurantParser class to parse Json data before transferring to an individual restaurant
		parser = gson.fromJson(next, RestaurantParser.class);
		allRestaurants.add(parser.getRestaurantArray().get(0));
		boolean found = true;
		
		//Ensures each restaurant location is only found once to reduce calls to Yelp API
		for(int i = 1; i < deliveries.size(); i++)
		{
			for(int j = 0; j < allRestaurants.size(); j++)
			{
				if(!name.equals(deliveries.get(i).getrestaurantName().replaceAll("’", "'")))
				{
					found = false;
				}
				else
				{
					found = true;
					break;
				}
			}
			
			//If new restaurant, obtain data and add it to list
			if(!found)
			{
				name = deliveries.get(i).getrestaurantName().replaceAll("’", "'");
				
				next = yp.findRestaurant(name, latitude, longitude);
				parser = gson.fromJson(next, RestaurantParser.class);
				allRestaurants.add(parser.getRestaurantArray().get(0));
				found = true;
			}
		}
			
		
		//Set newly obtained data for each delivery 
		for(int i = 0; i < deliveries.size(); i++)
		{
			Delivery d = deliveries.get(i);
			for(int j = 0; j < allRestaurants.size(); j++)
			{
				if(d.getrestaurantName().equals(allRestaurants.get(j).getName()))
				{
					d.setRestaurant(allRestaurants.get(j));
					d.setLatitude(d.getRestaurant().getCoordinates().getLatitude());
					d.setLongitude(d.getRestaurant().getCoordinates().getLongitude());
				}
			}
		}
		
	
		try
		{
			ss = new ServerSocket(port, num_drivers);
			
			System.out.println("Listening on port " + port + ". Waiting for drivers...\n");
			serverThreads = new Vector<ServerThread>();
			
			//For the specified number of drivers, let a new client connect
			for(int i = num_drivers; i > 0; i--)
			{
				Socket socket = ss.accept();
				if(i > 1)
				{
					System.out.println("Connection from " + socket.getInetAddress() + "\nWaiting for " + (i - 1) + " more driver(s)...\n");
				}
				else
				{
					System.out.println("Connection from " + socket.getInetAddress() + "\nStarting service.\n");
				}
				ServerThread thread = new ServerThread(socket, this, i-1);
				serverThreads.add(thread);
			}
			
			//Ensure the final client has time to initialize before broadcast is called
			Thread.sleep(1000);
			
			broadcast(new Message<Double>("latitude", latitude));
			broadcast(new Message<Double>("longitude", longitude));
			
			
			ExecutorService executor = Executors.newFixedThreadPool(deliveries.size());
			
			//Start all delivery threads	
			for(int i = 0; i < deliveries.size(); i++)
			{
				executor.execute(deliveries.get(i));
			}
					
			executor.shutdown();
			
			
			//Don't continue till all deliveries are ready to go out
			while(!executor.isTerminated())
			{
				Thread.yield();
			}
			
			ServerThread.QueueDone();
			
			//Don't continue until all serverThreads/clients have finished their deliveries
			for(int i = 0; i < serverThreads.size(); i++)
			{
				ServerThread thread = serverThreads.get(i);
				if(thread.isAlive())
				{
					i = -1;
				}
			}
			
			System.out.println("All orders completed!");
			broadcast(new Message<String>("complete", java.time.LocalTime.now() + " All orders completed!"));
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch(IOException ioe)
		{
			System.out.println("io exception");
		}
		
	}
	
	//Take in a message object and broadcast it to every client 
	public void broadcast(Message<?> message) {
		if (message.getMessage() != null) {
			for(ServerThread threads : serverThreads) {
				threads.sendMessage(message);
			}
		}
	}
	
	//Ensures the latitude entered is valid
	public static double validLat(Scanner s)
	{
		boolean valid = false;
		double latitude = 0;
		while(!valid)
		{
			try 
			{
				String userinput = s.nextLine();
				System.out.println("");
				latitude = Double.parseDouble(userinput);
				//If not a double or within the range ask again
				if(latitude >= -90 && latitude <= 90)
				{
					valid = true;
				}
				else
				{
					System.out.print("Invalid latitude.\n" + "Reenter latitude. ");
				}
			}
			catch(NumberFormatException nfe)
			{
				System.out.print("Invalid latitude.\n" + "Reenter latitude. ");
			}
		}
		return latitude;
	}
			
	//Ensures the longitude entered is valid
	public static double validLong(Scanner s)
	{
		boolean valid = false;
		double longitude = 0;
		while(!valid)
		{
			try {
				String userinput = s.nextLine();
				System.out.println("");
				longitude = Double.parseDouble(userinput);
				if(longitude >= -180 && longitude <= 180)
				{
					valid = true;
				}
				else
				{
					System.out.print("Invalid longitude.\n" + "Reenter longitude. ");
				}
			}
			catch(NumberFormatException nfe)
			{
				System.out.print("Invalid longitude.\n" + "Reenter longitude. ");
			}
		}
		return longitude;
	}
	
	//Check that driver value entered is valid
	public static int validDrivers(Scanner s)
	{
		boolean valid = false;
		int drivers = 0;
		while(!valid)
		{
			try 
			{
				String userinput = s.nextLine();
				System.out.println("");
				drivers = Integer.parseInt(userinput);
				//If not positive ask again
				if(drivers > 0)
				{
					valid = true;
				}
				else
				{
					System.out.print("Invalid drivers.\n" + "Reenter drivers. ");
				}
			}
			catch(NumberFormatException nfe)
			{
				System.out.print("Invalid drivers.\n" + "Reenter drivers. ");
			}
		}
		return drivers;
	}				
			
}


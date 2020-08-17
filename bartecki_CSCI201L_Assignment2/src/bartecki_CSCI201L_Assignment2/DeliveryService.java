package bartecki_CSCI201L_Assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DeliveryService {
	
		public static void main(String []args)
		{
			FileReader fr;
			BufferedReader br;
			Scanner s = new Scanner(System.in);
			//List to hold each restaurant
			ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
			Vector<Delivery> deliveries = new Vector<Delivery>();
			//List to extract data from json file
			RestaurantArray allRestaurants = new RestaurantArray();
			System.out.print("What is the name of the file containing the restaurant information? ");
			String restaurantfile = s.nextLine();
			System.out.println();
			System.out.print("What is the name of the file containing the schedule information? ");
			String schedulefile = s.nextLine();
			boolean validrestaurantfile = false;
			boolean validschedulefile = false;
			
			
			//Repeats until file is found and proper
			while(!validrestaurantfile || !validschedulefile)
			{
				try 
				{
					System.out.println();
					fr = new FileReader(restaurantfile);
					br = new BufferedReader(fr);
					
					//Obtaining all restaurants from file 
					Gson gson = new Gson();
					allRestaurants = gson.fromJson(br, RestaurantArray.class);
					
					//Creating each individual restaurant
					for(int i = 0; i < allRestaurants.getRestaurantArray().size(); i++)
					{		
						Restaurant r = new Restaurant(allRestaurants.getRestaurantArray().get(i));
						//Restaurant r = allRestaurants.getRestaurantArray().get(i);
						restaurants.add(r);
					}
					
					
					validrestaurantfile = true;
					
					//Checking if any values in any restaurants are null
					for(int i = 0; i < restaurants.size(); i++)
					{
						Restaurant checker = restaurants.get(i);
						if(checker.getName().equals(null) ||
							checker.getAddress().equals(null) ||
							checker.getLatitude() == 0 ||
							checker.getLongitude() == 0 ||
							checker.getMenu().equals(null))
						{
							System.out.println("Missing parameters.\n");
							validrestaurantfile = false;
						}
						else if(checker.getDrivers() == 0)
						{
							System.out.println("Must have at least one driver.\n");
							validrestaurantfile = false;
						}
					}
					
					//Obtaining all deliveries from file 
					fr = new FileReader(schedulefile);
					br = new BufferedReader(fr);
					String line = br.readLine();
					
					//Parsing each line of schedule file
					while(line != null)
					{
						String[] info = line.split(", ");
						int tempWait = Integer.parseInt(info[0]);
						String tempRestaurant = info[1];
						String tempItem = info[2];
						Restaurant r = null;
						//Match delivery to its associated restaurant
						for(int i = 0; i < restaurants.size(); i++)
						{
							if(tempRestaurant.equalsIgnoreCase(restaurants.get(i).getName()))
							{
								r = restaurants.get(i);
							}
						}
						
						Delivery newdelivery = new Delivery(tempWait, tempRestaurant, tempItem, 0, r);
						
						deliveries.add(newdelivery);
						
						line = br.readLine();
					}
					validschedulefile = true;
					
				}
				catch(FileNotFoundException fnfe) 
				{
					if(!validrestaurantfile)
					{
						System.out.println("The file " + restaurantfile + " could not be found.\n");
					}
					else
					{
						System.out.println("The file " + schedulefile + " could not be found.\n");
					}
				} 
				catch(JsonSyntaxException jse)
				{
					System.out.println("Improper " + restaurantfile + " format.\n");
				}
				catch(NumberFormatException nfe)
				{
					System.out.println("Improper data type present.\n");
				}
				catch(NullPointerException npe)
				{
					System.out.println("Missing restaurant parameters.\n");
					validrestaurantfile = false;
				}
				catch(IOException ie)
				{
					System.out.println("Could not read schedule file.\n");
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					System.out.println("Schedule file missing info.\n");
				}
				
				//Repeating question if file not valid
				if(!validrestaurantfile)
				{
					System.out.print("What is the name of the restaurant file? ");
					restaurantfile = s.nextLine();
				}
				else if(!validschedulefile)
				{
					System.out.print("What is the name of the schedule file? ");
					schedulefile = s.nextLine();	
				}
			}
			
			//Obtaining lat/long for location
			System.out.print("What is the latitude? ");
			double latitude = validLat(s);
			System.out.print("What is the longitude? ");
			double longitude = validLong(s);
			
			System.out.println("Starting execution of program...");
			ExecutorService executor = Executors.newCachedThreadPool();
			
			//Initializing the distance for each restaurant/delivery
			for(int i = 0; i < deliveries.size(); i++)
			{
				Delivery d = deliveries.get(i);
				for(int j = 0; j < restaurants.size(); j++)
				{
					Restaurant r = restaurants.get(j);
					if(r.getDistance() == 0)
					{
						double distance = calcDistance(r, latitude, longitude);
						r.setDistance(distance);
					}
					
					if(d.getRestaurant().equalsIgnoreCase(r.getName()))
					{
						d.setDistance(r.getDistance());
						break;
					}
				}
			}
			
			//Start all delivery threads	
			for(int i = 0; i < deliveries.size(); i++)
			{
				executor.execute(deliveries.get(i));
			}
					
			executor.shutdown();
			//Don't continue till all deliveries complete
			while(!executor.isTerminated())
			{
				Thread.yield();
			}
			
			Driver.noMoreDeliveries();
			System.out.println("All orders complete!");
			//Wake up all drivers so program will terminate 
			for(int i = 0; i < restaurants.size(); i++)
			{
				for(int j = 0; j < restaurants.get(i).getDriversArray().size(); j++)
				{
					restaurants.get(i).getDriversArray().get(j).newOrderReady();
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
		
		//Returns the distance given restaurant's latitude and longitude
		public static double calcDistance(Restaurant r, double latitude, double longitude)
		{
			double lat1 = Math.toRadians(latitude);
			double lat2 = Math.toRadians(r.getLatitude());
			double long1 = Math.toRadians(longitude);
			double long2 = Math.toRadians(r.getLongitude());
			
			double distance = 3963.0 * Math.acos((Math.sin(lat1) * Math.sin(lat2)) + 
					Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1));
			return distance;
		}
}




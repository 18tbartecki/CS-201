package bartecki_CSCI201L_Assignment1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Main {
	public static void main(String []args)
	{
		FileReader fr;
		BufferedReader br;
		Scanner s = new Scanner(System.in);
		//List to hold each restaurant
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		//List to extract data from json file
		RestaurantArray allRestaurants = new RestaurantArray();
		System.out.print("What is the name of the restaurant file? ");
		String file = s.nextLine();
		boolean validfile = false;
		
		//Repeats until file is found and proper
		while(!validfile)
		{
			try 
			{
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				
				//Obtaining all data from file 
				Gson gson = new Gson();
				allRestaurants = gson.fromJson(br, RestaurantArray.class);
				
				//Creating each individual restaurant
				for(int i = 0; i < allRestaurants.getRestaurantArray().size(); i++)
				{				
					Restaurant r = allRestaurants.getRestaurantArray().get(i);
					restaurants.add(r);
				}
				
				validfile = true;
				
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
						validfile = false;
					}
				}
				
			}
			catch(FileNotFoundException fnfe) 
			{
				System.out.println("The file " + file + " could not be found.\n");
			} 
			catch(JsonSyntaxException jse)
			{
				System.out.println("Improper file format.\n");
			}
			catch(NumberFormatException nfe)
			{
				System.out.println("Improper data type present.\n");
			}
			catch(NullPointerException npe)
			{
				System.out.println("Missing parameters.\n");
				validfile = false;
			}
			
			//Repeating question if file not valid
			if(!validfile)
			{
				System.out.print("What is the name of the restaurant file? ");
				file = s.nextLine();
			}
		}
		
		
		if(validfile)
		{
			System.out.println("The file has been properly read.\n");
		}
		//Obtaining lat/long for location
		String userinput;
		System.out.print("What is your latitude? ");
		double latitude = validLat(s);
		System.out.print("What is your longitude? ");
		double longitude = validLong(s);
		
		printOptions();
		
		//Iterates until user stops program
		while(true)
		{
			System.out.print("What would you like to do? ");
			
			try 
			{	
				userinput = s.nextLine();
				System.out.println();
				//Option must be integer 1-7
				int option = Integer.parseInt(userinput);
				
				//Display all restaurants
				if(option == 1)
				{
					//Iterate through restaurant array printing correct info
					for(int i = 0; i < restaurants.size(); i++)
					{
						Restaurant r = restaurants.get(i);
						double distance = calcDistance(r, latitude, longitude);
						
						System.out.print(r.getName() + ", located ");
						System.out.printf("%.1f", distance);
						System.out.println(" miles away at " + r.getAddress());
					}
					
					if(restaurants.size() == 0)
					{
						System.out.println("No restaurants to show.");
					}
					
					System.out.println();
					printOptions();
				}
				//Search for a restaurant
				else if(option == 2)
				{
					if(restaurants.size() == 0)
					{
						System.out.println("No restaurants to search.\n");
						continue;
					}
					
					//If not found search again
					boolean found = false;
					while(!found)
					{
						System.out.print("What is the name of the restaurant you would like to search for? ");
						userinput = s.nextLine();
						System.out.println();
						
						//List to keep track of each match of user input
						ArrayList<Restaurant> matches = new ArrayList<Restaurant>();
						ArrayList<Double> distances = new ArrayList<Double>();
						Restaurant checker = restaurants.get(0);
						for(int i = 0; i < restaurants.size(); i++)
						{
							//Ignoring case check if input partially matches any restaurants
							checker = restaurants.get(i);
							if(checker.getName().toLowerCase().contains(userinput.toLowerCase()))
							{
								found = true;
								matches.add(checker);
								
								double distance = calcDistance(checker, latitude, longitude);
								distances.add(distance);
							}		
						}
						
						//Output correct response if matches found or not
						if(found)
						{
							for(int i = 0; i < matches.size(); i++)
							{
								System.out.print(matches.get(i).getName() + ", located ");
								System.out.printf("%.1f", distances.get(i));
								System.out.println(" miles away at " + matches.get(i).getAddress());
							}
						}
						else
						{
							System.out.println(userinput + " could not be found.\n");
						}
					}
					System.out.println();
					printOptions();
				}
				//Search for a menu item
				else if(option == 3)
				{
					if(restaurants.size() == 0)
					{
						System.out.println("No menus to search.\n");
						continue;
					}
					
					//Variables to track if item found anywhere and if found at certain restaurant
					boolean foundAny = false;
					boolean foundOne = false;
					while(!foundAny)
					{
						System.out.print("What menu item would you like to search for? ");
						userinput = s.nextLine();
						System.out.println();
						
						Restaurant checker = restaurants.get(0);
						for(int i = 0; i < restaurants.size(); i++)
						{
							ArrayList<String> items = new ArrayList<String>();
							checker = restaurants.get(i);
							//Iterate through menu checking for partial matches 
							for(int j = 0; j < checker.getMenu().size(); j++)
							{
								String menuItem = checker.getMenu().get(j);
								//If item found add it to list of items
								if(menuItem.toLowerCase().contains(userinput.toLowerCase()))
								{
									items.add(menuItem);
									foundAny = true;
									foundOne = true;
								}
							}
							
							//If current restaurant has item
							if(foundOne)
							{
								System.out.print(checker.getName() + " serves ");
								//Properly output menu items depending on number of matches
								for(int j = 0; j < items.size(); j++)
								{
									System.out.print(items.get(j));
									if(j == items.size() - 1)
									{
										System.out.print(".\n");
									}
									else if(j == items.size() - 2 && items.size() > 2)
									{
										System.out.print(", and ");
									}
									else if(items.size() > 2)
									{
										System.out.print(", ");
									}
									else
									{
										System.out.print(" and ");
									}
								}
					
							}
							//Set to false for next restaurant
							foundOne = false;
						}
						
						//If not found at any restaurant
						if(!foundAny)
						{
							System.out.println("No restaurant nearby serves " + userinput + ".\n");
						}
					}
					
					System.out.println();
					printOptions();
				}
				//Add a new restaurant
				else if(option == 4)
				{
					//Executes until a new restaurant name is input
					boolean exists = true;
					while(exists)
					{
						System.out.print("What is the name of the restaurant you would like to add? ");
						userinput = s.nextLine();
						System.out.println();
						
						//Checks if restaurant already exists
						for(int i = 0; i < restaurants.size(); i ++)
						{
							if(restaurants.get(i).getName().toLowerCase().equals(userinput.toLowerCase()))
							{
								exists = true;
								break;
							}
							else
							{
								exists = false;
							}
						}
						
						if(restaurants.size() == 0)
						{
							exists = false;
						}
						
						//Restaurant already exists
						if(exists)
						{
							System.out.println("There is already an entry for " + userinput + ".\n");
						}
						//Obtain all new restaurant data and create new restaurant
						else
						{
							Restaurant newR = new Restaurant();
							newR.setName(userinput);
							
							System.out.print("What is the address for " + userinput + "? ");
							String newAddress = s.nextLine();
							newR.setAddress(newAddress);
							System.out.println();
							
							System.out.print("What is the latitude for " + userinput + "? ");
							double newLatitude = validLat(s);
							newR.setLatitude(newLatitude);
							
							
							System.out.print("What is the longitude for " + userinput + "? ");
							double newLongitude = validLong(s);
							newR.setLongitude(newLongitude);
						
							
							System.out.print("What does " + userinput + " serve? ");
							String menuItem = s.nextLine();
							ArrayList<String> newMenu = new ArrayList<String>();
							newMenu.add(menuItem);
							newR.setMenu(newMenu);
							System.out.println();
							
							//Executes until user says menu is done
							boolean menuDone = false;
							while(!menuDone)
							{
								try 
								{
									System.out.println("	1) Yes\n" + "	2) No");
									System.out.print("Does " + newR.getName() + " serve anything else? ");
							
									userinput = s.nextLine();
									System.out.println();
									int choice = Integer.parseInt(userinput);
									
									//Restaurant serves more food
									if(choice == 1)
									{
										System.out.print("What does " + newR.getName() + " serve? ");
										menuItem = s.nextLine();
										//If new item add to menu
										if(!menuItem.equals(""))
										{
											newMenu.add(menuItem);
											System.out.println();
										}
										else
										{
											System.out.println("That is not a valid option.\n");
										}
										
									}
									//Menu is done
									else if(choice == 2)
									{
										newR.setMenu(newMenu);
										System.out.println("There is now a new entry for: ");
										
										double distance = calcDistance(newR, latitude, longitude);
										
										System.out.print(newR.getName() + ", located ");
										System.out.printf("%.2f", distance);
										System.out.println(" miles away at " + newR.getAddress());
										System.out.print(newR.getName() + " serves ");
										
										//Correctly output restaurant info and menu
										for(int i = 0; i < newR.getMenu().size(); i++)
										{
											System.out.print(newR.getMenu().get(i));
											if(i == newR.getMenu().size() - 1)
											{
												System.out.print(".\n");
											}
											else if(i == newR.getMenu().size() - 2 && newR.getMenu().size() > 2)
											{
												System.out.print(", and ");
											}
											else if(newR.getMenu().size() > 2)
											{
												System.out.print(", ");
											}
											else
											{
												System.out.print(" and ");
											}
										}
										
										menuDone = true;
									}
									//If not 1 or 2
									else
									{
										System.out.println("That is not a valid option.\n");
									}
								}
								catch(NumberFormatException nfe)
								{
									System.out.println("That is not a valid option.\n");
								}
							}
						
						//Add new restaurant to lists
						restaurants.add(newR);
						allRestaurants.getRestaurantArray().add(newR);
						}
					}
					
					System.out.println();
					printOptions();
				}
				//Remove a restaurant
				else if(option == 5)
				{
					//List options
					for(int i = 1; i <= restaurants.size(); i++)
					{
						System.out.println("	" + i + ") " + restaurants.get(i-1).getName());
					}
					if(restaurants.size() == 0)
					{
						System.out.println("No restaurants to remove.\n");
						continue;
					}
					System.out.print("Which restaurant would you like to remove? ");
					
					//Executes until valid choice made
					boolean valid = false;
					while(!valid)
					{
						try 
						{
							userinput = s.nextLine();
							System.out.println();
							int choice = Integer.parseInt(userinput);
							//If choice valid, remove that restaurant from both lists
							if(choice <= restaurants.size())
							{
								valid = true;
								System.out.println(restaurants.get(choice - 1).getName() + " is now removed.\n");
								restaurants.remove(choice - 1);
								allRestaurants.getRestaurantArray().remove(choice - 1);
							}
							//Otherwise re-prompt question
							else
							{
								System.out.println("That is not a valid option.\n");
								System.out.print("Which restaurant would you like to remove? ");
							}
						}
						catch(NumberFormatException nfe)
						{
							System.out.println("That is not a valid option.\n");
							System.out.print("Which restaurant would you like to remove? ");
						}
					}
					
					printOptions();
				}
				//Sort Restaurants
				else if(option == 6)
				{
					if(restaurants.size() == 0)
					{
						System.out.println("No restaurants to sort.\n");
						continue;
					}
					
					System.out.println("	1) A to Z\n" + 
							"	2) Z to A\n" +
							"	3) Closest to farthest\n" +
							"	4) Farthest to closest");
					System.out.print("How would you like to sort by? ");
					
					//Create list of distances in same order as current restaurants
					ArrayList<Double> distances = new ArrayList<Double>();
					for(int i = 0; i < restaurants.size(); i++)
					{
						Restaurant r = restaurants.get(i);
						Double distance = calcDistance(r, latitude, longitude);
						distances.add(distance);
					}
					
					int n = restaurants.size(); 
					
					//Executes until user enters valid option
					boolean valid = false;
					while(!valid)
					{
						try 
						{
							userinput = s.nextLine();
							System.out.println();
							int choice = Integer.parseInt(userinput);
							//A to Z
							if(choice == 1)
							{
								System.out.println("Your restaurants are now sorted from A to Z.");
								valid = true;
								Collections.sort(restaurants);
								Collections.sort(allRestaurants.getRestaurantArray());
							}
							//Z to A
							else if(choice == 2)
							{
								System.out.println("Your restaurants are now sorted from Z to A.");
								valid = true;
								Collections.sort(restaurants, Collections.reverseOrder());;
								Collections.sort(allRestaurants.getRestaurantArray(), Collections.reverseOrder());
							}
							//Closest to farthest
							else if(choice == 3)
							{
								System.out.println("Your restaurants are now sorted from closest to farthest.");
								valid = true;
								//Perform selection sort on distance list, swapping restaurants at the same indices
								for (int i = 0; i < n-1; i++) 
							    { 
							        int index = i; 
							        for (int j = i + 1; j < n; j++)
							        {
							            if (distances.get(j) < distances.get(index)) 
							                index = j; 
							        }
							            
							        Collections.swap(distances, index, i);
							        Collections.swap(restaurants, index, i);
							        Collections.swap(allRestaurants.getRestaurantArray(), index, i);
							    }
							}
							//Farthest to closest
							else if(choice == 4)
							{
								System.out.println("Your restaurants are now sorted from farthest to closest.");
								valid = true;
								//Perform reverse selection sort on distance list, swapping restaurants at the same indices
								for (int i = 0; i < n-1; i++) 
							    { 
							        int index = i; 
							        for (int j = i + 1; j < n; j++)
							        {
							            if (distances.get(j) > distances.get(index)) 
							                index = j; 
							        }
							            
							        Collections.swap(distances, index, i);
							        Collections.swap(restaurants, index, i);
							        Collections.swap(allRestaurants.getRestaurantArray(), index, i);
							    }
							}
							//If 1-4 not entered
							else
							{
								System.out.println("That is not a valid option.\n");
								System.out.print("How would you like to sort by? ");
							}
						}
						catch(NumberFormatException nfe)
						{
							System.out.println("That is not a valid option.\n");
							System.out.print("How would you like to sort by? ");
						}
					}
					
					System.out.println();
					printOptions();
				}
				//Exit
				else if(option == 7)
				{
					System.out.println("	1) Yes\n" + "	2) No");
					System.out.print("Would you like to save your edits? ");
					//Executes until 1 or 2 entered 
					boolean valid = false;
					while(!valid)
					{
						try 
						{
							userinput = s.nextLine();
							System.out.println();
							int exiter = Integer.parseInt(userinput);
							//Exit with saving
							if(exiter == 1)
							{
								try
								{
									//Create a new gson with the current allRestaurants array
									Gson gson = new Gson();
									String json = gson.toJson(allRestaurants);
									BufferedWriter writer = new BufferedWriter(new FileWriter(file));
									//Write data to same file
								    writer.write(json);
								    writer.close();
								}
								catch(IOException ioe)
								{
									System.out.println("Error writing to file.");
								}
							
								System.out.println("Your edits have been saved to " + file + ".");
								System.out.println("Thank you for using my program!");
								s.close();
								return;
							}
							//Exit without saving changes
							else if(exiter == 2)
							{
								System.out.println("Thank you for using my program!");
								s.close();
								return;
							}
							else
							{
								System.out.println("That is not a valid option.\n");
								System.out.print("Would you like to save your edits? ");
							}
						}
						catch(NumberFormatException nfe)
						{
							System.out.println("That is not a valid option.\n");
							System.out.print("Would you like to save your edits? ");
						}
					}
				}
				else
				{
					System.out.println("That is not a valid option.\n");
					//Ask what they want to do again
				}
			}
			catch(NumberFormatException nfe)
			{
				System.out.println("That is not a valid option.\n");
			}
		}
	}
	
	//Prints options user can take
	public static void printOptions()
	{
		System.out.println("	1) Display all restaurants\n" +
				"	2) Search for a restaurant\n" + 
				"	3) Search for a menu item\n" + 
				"	4) Add a new restaurant\n" + 
				"	5) Remove a restaurant\n" + 
				"	6) Sort restaurants\n" +
				"	7) Exit");
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

//Class to obtain array of data from json file
class RestaurantArray
{
	private ArrayList<Restaurant> data;

	public ArrayList<Restaurant> getRestaurantArray() {
		return data;
	}

	public void setRestaurantArray(ArrayList<Restaurant> data) {
		this.data = data;
	}
	
}

//Class to model each restaurant with getters and setters for data
class Restaurant implements Comparable<Restaurant>
{
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private ArrayList<String> menu;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public ArrayList<String> getMenu() {
		return menu;
	}
	public void setMenu(ArrayList<String> menu) {
		this.menu = menu;
	}
	
	@Override
	public int compareTo(Restaurant r)
	{
		return this.getName().compareTo(r.getName());
	}
		
}



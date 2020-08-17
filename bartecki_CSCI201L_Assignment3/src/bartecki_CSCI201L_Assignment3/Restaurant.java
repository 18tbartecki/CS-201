package bartecki_CSCI201L_Assignment3;

import java.io.Serializable;
import java.util.ArrayList;

//Must be serializable to send through stream
public class Restaurant implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String name;
	private Coordinates coordinates;
	private double distance;
	
	
	public String getName() {
		return name;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	public double getDistance() {
		return distance;
	}
	
}

class Coordinates implements Serializable
{
	private static final long serialVersionUID = 1L;
	private double latitude;
	private double longitude;
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
}

//Class to obtain data from Yelp API
class RestaurantParser
{
	private ArrayList<Restaurant> businesses;
	
	public ArrayList<Restaurant> getRestaurantArray() {
		return businesses;
	}
		
}		



package bartecki_CSCI201L_Assignment3;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class Test {
	public static void main(String[] args)
	{
		RestaurantParser parser = new RestaurantParser();
		ArrayList<Restaurant> allRestaurants = new ArrayList<Restaurant>();
		YelpService yp = new YelpService();
		Gson gson = new Gson();
		
		
		String first = yp.findRestaurant("Momota Ramen House", 34.02116, -118.287132);
		String second = yp.findRestaurant("Tengoku Ramen Bar", 34.02116, -118.287132);
		
		parser = gson.fromJson(first, RestaurantParser.class);
		allRestaurants.add(parser.getRestaurantArray().get(0));
		System.out.println(allRestaurants.get(0).getCoordinates().getLatitude());
		String name = "hi".replaceAll("’", "'");
	}
}

package Assignment4;

import java.io.Serializable;
import java.util.ArrayList;


//Must be serializable to send through stream
public class Restaurant implements Serializable, Comparable<Restaurant>
{
	public Restaurant(String name,  String image_url, String url, String address, String phone, String price, String rating, String cuisine)
	{
		this.name = name;
		this.address = address;
		this.image_url = image_url;
		this.url = url;
		this.rating = Double.parseDouble(rating);
		this.price = price;
		this.display_phone = phone;
		this.cuisine = cuisine;
	}
	private static final long serialVersionUID = 1L;
	private String name;
	private Location location;
	private String image_url;
	private String url;
	private double rating;
	private String price;
	private String display_phone;
	private String address;
	private ArrayList<Category> categories;
	private String cuisine;
	
	public String getCuisine() {
		return cuisine;
	}
	
	public String getName() {
		return name;
	}
	
	public double getRating() {
		return rating;
	}
	
	public String getPrice() {
		return price;
	}
	
	public String getPhone() {
		return display_phone;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public ArrayList<Category> getCategory()
	{
		return categories;
	}
	
	public String getImage() {
		return image_url;
	}
	
	public String getURL()
	{
		String url = this.url;
		int end = url.indexOf('?');
		String sub = url;
		if(end != -1)
		{
			sub = url.substring(0, end);
		}
		return sub;
	}
	
	public String getAddress()
	{
		return address;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setImage(String image_url) {
		this.image_url = image_url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setPhone(String display_phone) {
		this.display_phone = display_phone;
	}
	
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	
	@Override
	public int compareTo(Restaurant r)
	{
		return getName().toLowerCase().compareTo(r.getName().toLowerCase());
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

class Category
{
	private String alias;
	private String title;
	
	public String getAlias() {
		return alias;
	}
	public String getTitle() {
		return title;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setTitle(String title) {
		this.title = title;
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



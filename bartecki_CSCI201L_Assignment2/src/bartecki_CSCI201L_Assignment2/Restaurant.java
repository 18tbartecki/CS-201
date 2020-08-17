package bartecki_CSCI201L_Assignment2;

import java.util.ArrayList;
import java.util.Vector;

public class Restaurant implements Comparable<Restaurant>
{
	Restaurant r;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private int drivers;
	private ArrayList<String> menu;
	private transient double distance;
	private transient Vector<Driver> driversArray;
	private transient Vector<Delivery> deliveriesWaiting;
	
	public Restaurant(Restaurant r)
	{
		this.r = r;
		this.name = r.getName();
		this.address = r.getAddress();
		this.latitude = r.getLatitude();
		this.longitude = r.getLongitude();
		this.drivers = r.getDrivers();
		this.menu = r.getMenu();
		this.driversArray = new Vector<Driver>();
		this.deliveriesWaiting = new Vector<Delivery>();
		for(int i = 0; i < drivers; i++)
		{
			driversArray.add(new Driver(this));
		}
	}
	
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
	public int getDrivers() {
		return drivers;
	}
	public void setDrivers(int drivers)
	{
		this.drivers = drivers;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	public ArrayList<String> getMenu() {
		return menu;
	}
	public void setMenu(ArrayList<String> menu) {
		this.menu = menu;
	}
	
	public Vector<Driver> getDriversArray() {
		return driversArray;
	}
	public void setDriversArray(Vector<Driver> driversArray) {
		this.driversArray = driversArray;
	}
	
	public Vector<Delivery> getDeliveriesWaiting() {
		return deliveriesWaiting;
	}

	public void setDeliveriesWaiting(Vector<Delivery> deliveriesWaiting) {
		this.deliveriesWaiting = deliveriesWaiting;
	}

	@Override
	public int compareTo(Restaurant r)
	{
		return this.getName().toLowerCase().compareTo(r.getName().toLowerCase());
	}
	
	//Adds delivery to a restaurants queue
	public void startDriver(Delivery d)
	{
		deliveriesWaiting.add(d);
		for(int i = 0; i < drivers; i++)
		{
			Driver nextDriver = getDriversArray().get(i);
			if(nextDriver.isAvailable())
			{
				nextDriver.newOrderReady();
				break;
			}
		}
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

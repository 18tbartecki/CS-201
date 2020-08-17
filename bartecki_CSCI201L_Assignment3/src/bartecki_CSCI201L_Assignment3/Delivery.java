package bartecki_CSCI201L_Assignment3;

import java.io.Serializable;

public class Delivery extends Thread implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int readyTime;
	private String restaurantName; 
	private String menuItem;
	private Restaurant restaurant;
	
	private double latitude;
	private double longitude;
	private double distance;
	
	//Creates new delivery with all necessary info
	public Delivery(int readyTime, String restaurantName, String menuItem, Restaurant restaurant)
	{
		this.readyTime = readyTime*1000;
		this.restaurantName = restaurantName;
		this.menuItem = menuItem;
		this.restaurant = restaurant;
	}
	

	public void startingDelivery() 
	{
		System.out.println(java.time.LocalTime.now() + " Starting delivery of " + menuItem + " to " + restaurantName + "."); 
	}
	public void continuingDelivery() 
	{
		System.out.println(java.time.LocalTime.now() + " Continuing delivery to " + restaurantName + ".");
	}
	public void finishingDelivery() 
	{
		System.out.println(java.time.LocalTime.now() + " Finished delivery of " + menuItem + " to " + restaurantName + ".");
	}

	public void run()
	{
		try {
			//Sleeps until order is ready to go out
			Thread.sleep(readyTime);
			ServerThread.addToDeliveriesReady(this);
			} 
		catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
			} 
		
	}

	public int getReadyTime() {
		return readyTime;
	}


	public String getrestaurantName() {
		return restaurantName;
	}


	public String getMenuItem() {
		return menuItem;
	}
	
	public Restaurant getRestaurant() {
		return restaurant;
	}


	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

		
}



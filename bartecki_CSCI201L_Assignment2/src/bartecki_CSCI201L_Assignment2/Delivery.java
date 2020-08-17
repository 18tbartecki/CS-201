package bartecki_CSCI201L_Assignment2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Delivery extends Thread {
	
	private int readyTime;
	private String restaurant; 
	private String menuItem;
	private double distance;
	private Lock deliveryLock;
	private Condition deliveryDone;
	private Restaurant r;
	
	//Creates new delivery with all necessary info
	public Delivery(int readyTime, String restaurant, String menuItem, double distance, Restaurant r)
	{
		this.readyTime = readyTime*1000;
		this.restaurant = restaurant;
		this.menuItem = menuItem;
		this.r = r;
		deliveryLock = new ReentrantLock();
		deliveryDone = deliveryLock.newCondition();
	}
	
	public void startingDelivery() 
	{
		System.out.println(java.time.LocalTime.now() + " Starting delivery of " + menuItem + " from " + restaurant + "!"); 
	}
	//Notifies drivers that delivery has completed
	public void finishingDelivery() 
	{
		System.out.println(java.time.LocalTime.now() + " Finished delivery of " + menuItem + " from " + restaurant + "!");
		try {
			deliveryLock.lock();
			deliveryDone.signal();
		} finally {
			deliveryLock.unlock();
		}
	}

	public void run()
	{
		try {
			//Sleeps until order is ready to go out
			Thread.sleep(readyTime);
			r.startDriver(this);
			
			deliveryLock.lock();
			deliveryDone.await();
			} 
		catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
			} 
		finally {
			deliveryLock.unlock();
			} 
	}

	public int getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Restaurant getR() {
		return r;
	}

	public void setR(Restaurant r) {
		this.r = r;
	}
		
}



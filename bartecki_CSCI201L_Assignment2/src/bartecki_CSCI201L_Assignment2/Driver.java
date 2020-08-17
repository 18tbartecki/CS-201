package bartecki_CSCI201L_Assignment2;

//import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Driver extends Thread {
	
	private Restaurant r;
	private Lock driverLock;
	private Condition deliveryReady;
	private boolean available;
	private static boolean moreDeliveries;
	
	//Creates new driver associated with a specific restaurant
	public Driver(Restaurant r) 
	{
		this.r = r;
		available = true;
		moreDeliveries = true;
		driverLock = new ReentrantLock();
		deliveryReady = driverLock.newCondition();
		this.start();
	}
	
	public Restaurant getRestaurant()
	{
		return r;
	}
	
	//Notifies waiting drivers that an order is ready
	public void newOrderReady() {
		try {
			driverLock.lock();
			deliveryReady.signal();
		} finally {
			driverLock.unlock();
		}
	}
	
	public static void noMoreDeliveries()
	{
		moreDeliveries = false;
	}
	
	public boolean isAvailable()
	{
		return this.available;
	}
	
	public void run() {
		//Executes while a restaurant has more orders ready to go out
		while(moreDeliveries) 
		{	
			while(!r.getDeliveriesWaiting().isEmpty())
			{
				Delivery delivery = null;
				//Ensures two drivers don't choose the same order
				synchronized(this) {
					delivery = r.getDeliveriesWaiting().remove(0);
				}
				
				//Prints starting delivery then sleeps for distance to/from restaurant
				delivery.startingDelivery();
				try {
					Thread.sleep((long) (r.getDistance()*2*1000)); 	
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				delivery.finishingDelivery();
			}
			//When delivery ready driver gets locked so no other deliveries can be taken by them
			try {
				driverLock.lock();
				available = true;
				deliveryReady.await();
				available = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				driverLock.unlock();
			}
		}
		
	}
		
}

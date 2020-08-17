package Assignment4;

import java.util.ArrayList;

public class Location {
	private ArrayList<String>  display_address;
	
	
	public ArrayList<String>  getDisplayAddress()
	{
		return display_address;
	}
	
	public String getAddress()
	{
		String address = "";
		for(int i = 0; i < display_address.size(); i++)
		{
			if(display_address.get(i) != null)
			{
				address += display_address.get(i);
			}
			if(i != display_address.size()-1)
			{
				address += " ";
			}
	
		}
		return address;	
	}
}

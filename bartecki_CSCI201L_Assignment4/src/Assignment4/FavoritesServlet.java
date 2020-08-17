package Assignment4;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FavoritesServlet")
public class FavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public FavoritesServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String url = request.getParameter("url");
		String image = request.getParameter("image");
		String price = request.getParameter("price");
		String rating = request.getParameter("rating");
		String phone = request.getParameter("phone");
		String cuisine = request.getParameter("cuisine");
		
		String addRemove = request.getParameter("submit");
		String sortType = request.getParameter("sort");
		
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		ArrayList<Restaurant> allRestaurants = new ArrayList<Restaurant>();
		
		if(name != null)
		{
			Restaurant r = new Restaurant(name, image, url, address, phone, price, rating, cuisine);
			
		}
		
		Connection conn = null;
		PreparedStatement psUserId = null;
		PreparedStatement psGetFav = null;
		ResultSet rsId = null;
		ResultSet rsFav = null;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/SalEats?user=root&password=Westswim18&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=PST");
			
			psUserId = conn.prepareStatement("SELECT * FROM Users WHERE Username =?");
			psUserId.setString(1, username);
			rsId = psUserId.executeQuery();
			
			int id = 0;
			while(rsId.next())
			{
				id = rsId.getInt("ID");	
			}
			
			
			if(name != null)
			{
				if(addRemove.equals("Remove from Favorites")) 
				{
					
					psUserId = conn.prepareStatement("INSERT INTO Restaurant (UserID, RestaurantName, Address, Phone, Price, URL, Image, Rating, Cuisine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
					psUserId.setInt(1, id);
					psUserId.setString(2, name);
					psUserId.setString(3, address);
					psUserId.setString(4, phone);
					psUserId.setString(5, price);
					psUserId.setString(6, url);
					psUserId.setString(7, image);
					psUserId.setString(8, rating);
					psUserId.setString(9, cuisine);
					psUserId.executeUpdate();
					session.setAttribute(name, "Remove from Favorites");
				}
				else
				{
					
					psGetFav = conn.prepareStatement("DELETE FROM Restaurant WHERE UserID =? AND RestaurantName =?");
					psGetFav.setInt(1, id);
					psGetFav.setString(2, name);
					psGetFav.executeUpdate();
					session.setAttribute(name, "Add to Favorites");
				}
			}
			else
			{
				psGetFav = conn.prepareStatement("SELECT * FROM Restaurant WHERE UserID =?");
				psGetFav.setInt(1, id);
				rsFav = psGetFav.executeQuery();
						
				while(rsFav.next())
				{
					name = rsFav.getString("RestaurantName");
					image = rsFav.getString("Image");
					address = rsFav.getString("Address");
					url = rsFav.getString("URL");
					phone = rsFav.getString("Phone");
					price = rsFav.getString("Price");
					rating = rsFav.getString("Rating");
					cuisine = rsFav.getString("Cuisine");
							
					Restaurant r = new Restaurant(name, image, url, address, phone, price, rating, cuisine);
					
							
					allRestaurants.add(r);
				}
				if(sortType == null)
				{		
					request.setAttribute("restaurants", allRestaurants);
					RequestDispatcher dispatcher = request.getRequestDispatcher("Favorites.jsp");
					dispatcher.forward(request, response);
				}
				else
				{
					int n = allRestaurants.size();
					if(sortType.equals("atoz"))
					{
						Collections.sort(allRestaurants);;
					}
					else if(sortType.equals("ztoa"))
					{
						Collections.sort(allRestaurants, Collections.reverseOrder());;
					}
					else if(sortType.equals("high"))
					{
						for (int i = 0; i < n-1; i++) 
					    { 
					        int index = i; 
					        for (int j = i + 1; j < n; j++)
					        {
					            if (allRestaurants.get(j).getRating() > allRestaurants.get(index).getRating()) 
					                index = j; 
					        }     
					        Collections.swap(allRestaurants, index, i);
					    }
					}
					else if(sortType.equals("low"))
					{
						for (int i = 0; i < n-1; i++) 
					    { 
					        int index = i; 
					        for (int j = i + 1; j < n; j++)
					        {
					            if (allRestaurants.get(j).getRating() < allRestaurants.get(index).getRating()) 
					                index = j; 
					        }     
					        Collections.swap(allRestaurants, index, i);
					    }
					}
					else if(sortType.equals("recent"))
					{
						Restaurant t; 
				        for (int i = 0; i < n / 2; i++) 
				        { 
				            t = allRestaurants.get(i); 
				            allRestaurants.set(i, allRestaurants.get(n - i - 1));
				            allRestaurants.set(n - i - 1, t);
				        } 
					}
					else if(sortType.equals("old"))
					{
						
					}
					
					for(int i = 0; i < allRestaurants.size(); i++)
					{
						Restaurant r = allRestaurants.get(i);
						
					}
					
					request.setAttribute("restaurants", allRestaurants);
					
					
					RequestDispatcher dispatcher = request.getRequestDispatcher("Sorted.jsp");
					dispatcher.forward(request, response);
				}
				
			}
			
		}
		catch(SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		catch(ClassNotFoundException cnfe)
		{
			
		}
		finally
		{
			try
			{
				psUserId = null;
				if(rsId != null)
				{
					rsId.close();
				}
				if(rsFav != null)
				{
					rsFav.close();
				}
				if(psGetFav != null)
				{
					psGetFav.close();
				}
				if(conn != null)
				{
					conn.close();
				}
			}
			catch(SQLException sqle)
			{
				System.out.println(sqle.getMessage());
			}
		}
	
		
	}
	
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

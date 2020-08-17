package Assignment4;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;


@WebServlet("/RestaurantSearchServlet")
public class RestaurantSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public RestaurantSearchServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		String sort_by = request.getParameter("searchtype");
		
		
		RestaurantParser parser = new RestaurantParser();
		ArrayList<Restaurant> allRestaurants = new ArrayList<Restaurant>();
		YelpService yp = new YelpService();
		
		Gson gson = new Gson();
				
		//Searches the yelp API, getting data into a string
		String next = yp.findRestaurant(name, latitude, longitude, sort_by);
				
		//Using RestaurantParser class to parse Json data before transferring to an individual restaurant
		parser = gson.fromJson(next, RestaurantParser.class);
		for(int i = 0; i < parser.getRestaurantArray().size(); i++)
		{
			if(parser.getRestaurantArray().get(i) != null)
			{
				allRestaurants.add(parser.getRestaurantArray().get(i));
			}
		}
		for(int i = 0; i < allRestaurants.size(); i++)
		{
			if(!allRestaurants.get(i).getCategory().isEmpty())
			{
			allRestaurants.get(i).setCuisine(allRestaurants.get(i).getCategory().get(0).getTitle());
			}
			HttpSession session = request.getSession();
			session.setAttribute(allRestaurants.get(i).getName(), "Add to Favorites");

			
		}
		
		request.setAttribute("restaurants", allRestaurants);
		RequestDispatcher dispatcher = request.getRequestDispatcher("SearchResults.jsp");
		dispatcher.forward(request, response);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

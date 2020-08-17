package Assignment4;

import java.sql.*;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/SalEats?user=root&password=Westswim18&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=PST");
			ps= conn.prepareStatement("SELECT * FROM Users WHERE Username =? AND UserPassword=?");
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();
			String next = "Home.jsp";
			HttpSession session = request.getSession();
			
			if(rs.last())
			{
				//Sign in successful
				if(rs.getRow() == 1)
				{
					request.setAttribute("loggedIn", "true");
					session.setAttribute("username", username);
					session.setAttribute("loggedIn", "true");
				}
				else
				{
					request.setAttribute("logusername", username);
					request.setAttribute("logerror", "Invalid username/password");
					session.setAttribute("loggedIn", "false");
					next = "SignIn.jsp";
				}
			}
			else
			{
				request.setAttribute("logusername", username);
				request.setAttribute("logerror", "Invalid username/password");
				session.setAttribute("loggedIn", "false");
				next = "SignIn.jsp";
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(next);
			dispatcher.forward(request, response);
			
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
				if(rs != null)
				{
					rs.close();
				}
				if(conn != null)
				{
					conn.close();
				}
				if(ps != null)
				{
					ps.close();
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

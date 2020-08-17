package Assignment4;

import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public SignupServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmpassword = request.getParameter("confirmpassword");
		
		
		Connection conn = null;
		
		PreparedStatement psEmail = null;
		PreparedStatement psUser = null;
		ResultSet rsEmail = null;
		ResultSet rsUser = null;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/SalEats?user=root&password=Westswim18&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=PST");
			
			psEmail= conn.prepareStatement("SELECT * FROM Users WHERE Email=?");
			psEmail.setString(1, email);
			rsEmail = psEmail.executeQuery();
			
			String next = "Home.jsp";
			HttpSession session = request.getSession();
			
			if(confirmpassword != null)
			{
				psUser= conn.prepareStatement("SELECT * FROM Users WHERE USERNAME=?");
				psUser.setString(1, username);
				
				rsUser = psUser.executeQuery();
				
				if(rsEmail.last())
				{
					request.setAttribute("email", email);
					request.setAttribute("username", username);
					request.setAttribute("error", "Email is already in use");
					session.setAttribute("loggedIn", "false");
					next = "SignIn.jsp";		
				}
				else if(rsUser.last())
				{
					request.setAttribute("username", username);
					request.setAttribute("email", email);
					request.setAttribute("error", "Username is already in use");
					session.setAttribute("loggedIn", "false");
					next = "SignIn.jsp";		
				}
				else if(!password.equals(confirmpassword))
				{
					request.setAttribute("username", username);
					request.setAttribute("email", email);
					request.setAttribute("error", "Username is already in use");
					session.setAttribute("loggedIn", "false");
					next = "SignIn.jsp";	
				}
				else
				{
					psUser = conn.prepareStatement("INSERT INTO Users (Username, UserPassword, Email) VALUES (?, ?, ?)");
					psUser.setString(1, username);
					psUser.setString(2, password);
					psUser.setString(3, email);
					psUser.executeUpdate();
					session.setAttribute("loggedIn", "true");
					session.setAttribute("username", username);
				}
			}
			else
			{
				if(rsEmail.last())
				{
					session.setAttribute("email", email);
					session.setAttribute("username", username);
					session.setAttribute("loggedIn", "true");	
					session.setAttribute("googleSignIn", "true");
				}
				else
				{
					session.setAttribute("email", email);
					session.setAttribute("username", username);
					session.setAttribute("loggedIn", "true");
					session.setAttribute("googleSignIn", "true");
					psUser = conn.prepareStatement("INSERT INTO Users (Username, UserPassword, Email) VALUES (?, ?, ?)");
					psUser.setString(1, username);
					psUser.setString(2, password);
					psUser.setString(3, email);
					psUser.executeUpdate();
					session.setAttribute("loggedIn", "true");
					session.setAttribute("username", username);
				}
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
				psUser = null;
				if(rsUser != null)
				{
					rsUser.close();
				}
				if(rsEmail != null)
				{
					rsEmail.close();
				}
				
				if(psEmail != null)
				{
					psEmail.close();
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

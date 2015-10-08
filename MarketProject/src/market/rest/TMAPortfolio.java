package market.rest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.logging.Logger;

@Path("/TMAPortfolio")
public class TMAPortfolio {
	
	@GET
	@Produces("text/html")
	public String getTrades() throws SQLException {
		
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/ad3db", "root", "password");
		
		String result = "<table class='table table-striped'><thead><tr><th> Author </th><th> Date </th>"
				+ "<th> Company Name </th><th> Ask Price </th><th> Bid Price </th><th> Position  </th>"
				+ "<th> Shares </th><th> Dollar Profit </th></tr></thead>";
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Trades WHERE Author='SMovingA'");
			while (rs.next()) {
				result += "<tr><td>" + rs.getString(2) + "</td><td>" + rs.getDate(3) + "</td><td>" + rs.getString(4) 
						+ "</td><td>" + rs.getDouble(5)+ "</td><td>" + rs.getDouble(6) + "</td><td>" + rs.getString(7) 
						+ "</td><td>" + rs.getInt(10)+ "</td><td>" + rs.getString(11) + "</td></tr>";
			}
			
			result += "</table>";
	}	
		catch(Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}
		finally {
			if (cn != null) {
				cn.close();
			}
		}
		return result;
	}
}

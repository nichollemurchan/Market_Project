package market.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


@Path("/GetProfit")
public class GetProfit {

	@GET
	@Produces("text/html")
	public String stratagy1(@QueryParam("str") String str) throws SQLException, ClassNotFoundException {
		String temp="<table><tr><td>Time</td><td>Profit</td></tr>";
		Class.forName("com.mysql.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/montestdb", "root", "rU1DDbaTWTSI");
		PreparedStatement st = cn.prepareStatement("select DateCreated, DollarProfit from Trades1"
				+ "where Author=? and shares=0");
		st.setString(1, str);
		ResultSet rs = st.executeQuery();
		while(rs.next()){
			temp += "<tr><td>"+rs.getString(0)+"</td><td>"+rs.getString(1)+"</td></tr>";
		}
		temp += "</table>";
		return temp;
	}
}

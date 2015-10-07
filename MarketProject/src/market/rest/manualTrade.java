package market.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.logging.*;


@Path("/manualTrade")
public class manualTrade {

	@GET
	@Produces("text/html")
	public void stratagy1(@QueryParam("str") String str, @QueryParam("quantity") int quantity, @QueryParam("tradePosition") String tradePosition ) throws SQLException,
			ClassNotFoundException, IOException {

		
		
		/*
		 * database table
		 * 
		 * CREATE TABLE manualTrading(id int AUTO_INCREMENT PRIMARY KEY, DateCreated timestamp, 
CompanyName nvarchar(10), BuyOrSellAmount int,  BuyOrSell nvarchar(5), 
QuantityOfStocks int, QuantityInDollars double(6,2));
		 */

		Class.forName("com.mysql.jdbc.Driver");
		Connection cn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ad3db", "root", "rU1DDbaTWTSI");

		StringBuilder url = new StringBuilder(
				"http://finance.yahoo.com/d/quotes.csv?s=");
		url.append(str + "+");
		url.deleteCharAt(url.length() - 1);
		url.append("&f=saa5bb6&e=.csv");

		String theUrl = url.toString();
		URL obj = new URL(theUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		@SuppressWarnings("unused")
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		int cumulativeQuantity = 0;

		while ((inputLine = in.readLine()) != null) {
			String[] fields = inputLine.split(",");

			
			
			/*
			 * Buy request from user
			 */
			if(tradePosition.equals("Buy")){
				
				System.out.println("Manually Buying " + str + " stock");
				String companyName = fields[0];
			
				int stockAvailableToBuy = (Integer.parseInt(fields[2]));
				if(quantity>stockAvailableToBuy){
					System.out.println("quantity resequested is unavailable. Only " + stockAvailableToBuy + " stocks available to buy");
				} else {
					PreparedStatement st = cn
						.prepareStatement("INSERT INTO manualTrading(CompanyName, BuyOrSellAmount, BuyOrSell, QuantityOfStocks, QuantityInDollars)"
								+ " VALUES(?,?,?,?,?)");
					st.setString(1, fields[0]);//CompanyName
					st.setString(2, Integer.toString(quantity));//BuyOrSellAmount
					st.setString(3, "Buy");//BuyOrSell
	
					try {				
						Statement st1 = cn.createStatement();
						ResultSet rs = st1.executeQuery("SELECT SUM(BuyOrSellAmount) AS overallSize FROM manualtrading WHERE CompanyName ='"+ companyName +"'");
						if (rs.next()) {
							cumulativeQuantity = (rs.getInt(1)+quantity);
						}
					} catch (SQLException ex) {
						System.out.println("Error getting data " + ex);
					}
					
					st.setString(4, Integer.toString(cumulativeQuantity));//QuantityOfStocks
					
					double dollarQuantity = (quantity * (Double.parseDouble(fields[1])));
					st.setString(5, Double.toString(dollarQuantity));//QuantityInDollars
					st.executeUpdate();
				}
			}//buy method
			
			
			/*
			 * Sell request from user
			 */
			
			else{
				String companyName = fields[0];
				int maxSellSize = (Integer.parseInt(fields[4]));
				System.out.println("Manually Selling " + str + " stock");

					try {				
						Statement st2 = cn.createStatement();
						ResultSet rs = st2.executeQuery("SELECT SUM(BuyOrSellAmount) AS overallSize FROM manualtrading WHERE CompanyName ='"+ companyName +"'");
						if (rs.next()) {
							cumulativeQuantity = (rs.getInt(1));
						}
					}
					catch (SQLException ex) {
						System.out.println("Error getting data " + ex);
					}
					finally {
						if (cn != null) {
							//cn.close();
						}
					}
				
				
				if (maxSellSize<quantity){
						System.out.println("Quantity of stocks requested to sell: " + quantity + 
								" is greater than the total amount of stocks available to sell: " + maxSellSize);
						
					}
				
				else if(cumulativeQuantity<quantity){
					System.out.println("Quantity of stocks requested to sell: " + quantity + 
							" is greater than the total amount of stocks currently owned: " + cumulativeQuantity);
					
				}		
				else{
					PreparedStatement st = cn
							.prepareStatement("INSERT INTO manualTrading(CompanyName, BuyOrSellAmount, BuyOrSell, "
									+ "QuantityOfStocks, QuantityInDollars)"
									+ " VALUES(?,?,?,?,?)");
					
					st.setString(1, fields[0]);	
					
					
					st.setString(2, Integer.toString(quantity));
					
					st.setString(3, "Sell");
					
					
					try {				
						Statement st3 = cn.createStatement();
						ResultSet rs = st3.executeQuery("SELECT SUM(BuyOrSellAmount) AS overallSize FROM manualtrading WHERE CompanyName ='"+ companyName+"'");
						if (rs.next()) {
							cumulativeQuantity = (rs.getInt(1)-quantity);
						}
					}
					catch (SQLException ex) {
						System.out.println("Error getting data " + ex);
					}
					
			
					st.setString(4, Integer.toString(cumulativeQuantity));
					double dollarQuantity = (quantity * (Double.parseDouble(fields[3])));
					st.setString(5, Double.toString(dollarQuantity));
					st.executeUpdate();
					
				}
				
			
			}

		}// while

	}
}

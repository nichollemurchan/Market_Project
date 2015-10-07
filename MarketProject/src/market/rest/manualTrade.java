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
	public void stratagy1(@QueryParam("str") String str,
			@QueryParam("quantity") int quantity,
			@QueryParam("tradePosition") String tradePosition)
			throws SQLException, ClassNotFoundException, IOException {

		Class.forName("com.mysql.jdbc.Driver");
		Connection cn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ad3db", "root", "");

		StringBuilder url = new StringBuilder(
				"http://finance.yahoo.com/d/quotes.csv?s=");
		url.append(str + "+");
		url.deleteCharAt(url.length() - 1);
		// url.append("&f=saa5bb6&e=.csv");

		url.append("&f=saba5b6&e=.csv");

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

		while ((inputLine = in.readLine()) != null) {
			String[] fields = inputLine.split(",");

			System.out.println("Manually Trading " + str + " stock");

			String companyName = fields[0];
			double dollarQuantity = (quantity * (Double.parseDouble(fields[1])));
			int stockAvailableToBuy = (Integer.parseInt(fields[3]));
			int stockAvailableToSell = 0;
			int longSize = 0;
			int shortSize = 0;
			double rollingProfit = 0;
			double currentBuy = 0;
			
			try {
				Statement st1 = cn.createStatement();
				ResultSet rs = st1.executeQuery("SELECT SUM(size) "
								+ " FROM Trades WHERE CompanyName ='"
								+ companyName
								+ "'"
								+ "AND Position = 'Long' AND Author ='UserTrade'");
				if (rs.next()) {
					longSize = (rs.getInt(1));
				}
			} catch (SQLException ex) {
				System.out.println("Error getting data " + ex);
			}
			try {
				Statement st1 = cn.createStatement();
				ResultSet rs = st1.executeQuery("SELECT SUM(size) "
						+ " FROM Trades WHERE CompanyName ='" + companyName
						+ "'"
						+ "AND Position = 'Short' AND Author ='UserTrade'");
				if (rs.next()) {
					shortSize = (rs.getInt(1));
				}
			} catch (SQLException ex) {
				System.out.println("Error getting data " + ex);
			}

			
			try {
				Statement st1 = cn.createStatement();
				ResultSet rs = st1.executeQuery("SELECT DollarProfit "
						+ " FROM Trades WHERE CompanyName ='" + companyName
						+ "'"
						+ " AND Author ='UserTrade' ORDER BY (DateCreated) DESC");
				if (rs.next()) {
					rollingProfit = (rs.getDouble(1));
				}
			} catch (SQLException ex) {
				System.out.println("Error getting data " + ex);
			}

			try {
				Statement st1 = cn.createStatement();
				ResultSet rs = st1.executeQuery("SELECT shares "
						+ " FROM Trades WHERE CompanyName ='" + companyName
						+ "'"
						+ " AND Author ='UserTrade' ORDER BY (DateCreated) DESC");
				if (rs.next()) {
					stockAvailableToSell = (rs.getInt(1));
				}
			} catch (SQLException ex) {
				System.out.println("Error getting data " + ex);
			}


			if ((tradePosition.equals("Long") && quantity <= stockAvailableToBuy)
					|| (tradePosition.equals("Short") && quantity <= stockAvailableToSell)) {

				PreparedStatement st = cn
						.prepareStatement("INSERT INTO Trades(Author, CompanyName, Askprice, BidPrice, "
								+ "Position, size, ProfitPercent, shares, DollarProfit)"
								+ " VALUES(?,?,?,?,?,?,?,?,?)");
				st.setString(1, "UserTrade");// author
				st.setString(2, fields[0]);// CompanyName
				st.setString(3, fields[1]);// askprice
				st.setString(4, fields[2]); // bidprice
				st.setString(5, tradePosition);// position (long or short)
				st.setString(6, Integer.toString(quantity));// size (BuyOrSellAmount)
				st.setString(7, "0");// profitPercent

				if (tradePosition.equals("Long")) {

					longSize += quantity;
					
					currentBuy = -(quantity * (Double.parseDouble(fields[1])));
					
					
				} else {

					shortSize += quantity;
             		currentBuy = (quantity * (Double.parseDouble(fields[2])));
				}
				st.setString(8, Integer.toString(longSize - shortSize));// shares
																		// (quantity
																		// of
																		// stock
																		// remaining)
				
				st.setString(9, Double.toString((rollingProfit)+currentBuy));// DollarProfit

				st.executeUpdate();
				

			} else if (tradePosition.equals("Long")
					&& quantity > stockAvailableToBuy) {
				System.out
						.println("Quantity requested to buy is greater than the total amount of stock available to buy" + stockAvailableToBuy);
			} else {
				System.out
						.println("Quantity requested to sell is greater than the total amount of stock owned or available to sell" + stockAvailableToSell);
			}
		}

	}

}

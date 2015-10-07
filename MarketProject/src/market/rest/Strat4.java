package market.rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.logging.*;

@Path("/Strat4")
public class Strat4 {

	@GET
	@Produces("text/html")
	public void stratagy1(@QueryParam("str") String str) {
		
		try{
			
			System.out.println("Bollinger Bands Strategy Activated");
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/ad3db", "root", "rU1DDbaTWTSI");
		/*PreparedStatement in1 = cn.prepareStatement(" drop table Trades1");
		PreparedStatement in2 = cn.prepareStatement("create table Trades1(id int AUTO_INCREMENT PRIMARY KEY, "
				+ "Author nvarchar(15), DateCreated timestamp, CompanyName nvarchar(10), AskPrice double, "
				+ "BidPrice double, Position nvarchar(6), size int, ProfitPercent double, shares int, DollarProfit double);");
		in1.executeUpdate();
		in2.executeUpdate();*/
		
		int count = 0;
		int Transactions = 0;
		int shares = 0;
		double SDTotal = 0;
		double SD = 0;
		double longTotal = 0;
		double longAve = 0;
		double InitialTransaction = 0;
		double MoneyTotal = 0;
		double profit = 0;
		List<Double> SDList = new ArrayList<Double>();
		List<Double> longList = new ArrayList<Double>();
		

		while(true){
			count++;
			StringBuilder url = 
	            new StringBuilder("http://finance.yahoo.com/d/quotes.csv?s=");
            url.append(str + "+");
            url.deleteCharAt(url.length()-1);
            url.append("&f=saba5b6&e=.csv");
        
            String theUrl = url.toString();
            URL obj = new URL(theUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            @SuppressWarnings("unused")
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
      
            while ((inputLine = in.readLine()) != null){
            	String[] fields = inputLine.split(",");
            	
            	if(shares == 0){
            		profit =0;
            	}
			
            	longList.add((Double.parseDouble(fields[1])+Double.parseDouble(fields[2]))/2.0); 
            	if(count > 200){
            		longList.remove(0);
            		longTotal = 0;
            		for(Double d: longList){
            			longTotal += d;
            		}
            		longAve = longTotal/200;
            	}
            	
            	if(count>200){
            		SDList.add(Math.abs(((Double.parseDouble(fields[1])+Double.parseDouble(fields[2]))/2.0)-longAve));
            		if(count > 400){
            			SDList.remove(0);
            			SDTotal = 0;
            			for(Double d: SDList){
            				SDTotal += d;}
            			SD = SDTotal/200;
            		}
            	}
            		if(shares == 0){
            			profit = 0;
            			if((Double.parseDouble(fields[1])+Double.parseDouble(fields[2]))/2.0 < (longAve-SD)){
            				
            				PreparedStatement st = cn.prepareStatement("insert into Trades(Author, CompanyName, AskPrice,"
            					+ " BidPrice, Position, size, ProfitPercent, shares, DollarProfit)"
	    						+ "values(?,?,?,?,?,?,?,?,?)");
            				st.setString(1, "BollingerB");
	    					st.setString(2, fields[0]);
	    					st.setString(3, fields[1]);
	    					st.setString(4, fields[2]);
	    					st.setString(5,"Long");
	    					st.setString(6, fields[3]);
	    					st.setString(7,"0.0");
	    					
	    					//OrderManager.OrderResult bought = OrderManager.getInstance().buyOrder(fields[0], Double.parseDouble(fields[2]), 
	    							//Integer.parseInt(fields[4]));
	    					Transactions++;
	    					shares += Integer.parseInt(fields[3]);
	    					profit -= Double.parseDouble(fields[1])*Integer.parseInt(fields[3]); 
	    					MoneyTotal -= Double.parseDouble(fields[1])*Integer.parseInt(fields[3]); 
	    					InitialTransaction = -profit;
	    					
	    					st.setString(8, Integer.toString(shares));
	    					st.setString(9, Double.toString(MoneyTotal));
	    					st.executeUpdate();
	    					
            			}
            		}
            		
            		if(shares != 0){
            			
            			if((Double.parseDouble(fields[1])+Double.parseDouble(fields[2]))/2.0 > (longAve-SD)){
            				
            			PreparedStatement st = cn.prepareStatement("insert into Trades(Author, CompanyName, AskPrice, "
            						+ "BidPrice, Position, size, ProfitPercent, shares, DollarProfit)"
            						+ "values(?,?,?,?,?,?,?,?,?)");
            					st.setString(1, "BollingerB");
	    						st.setString(2, fields[0]);
	    						st.setString(3, fields[1]);
	    						st.setString(4, fields[2]);
	    						st.setString(5,"Short");
	    						
	    						
	    						Transactions++;
	    						
	    						if(shares >= Integer.parseInt(fields[4])){
	    						shares -= Integer.parseInt(fields[4]);
	    						profit += Double.parseDouble(fields[2])*Integer.parseInt(fields[4]);
	    						MoneyTotal += Double.parseDouble(fields[2])*Integer.parseInt(fields[4]);
	    						st.setString(6, fields[4]);
	    						}else{
	    							profit += Double.parseDouble(fields[2])*shares;
	    							MoneyTotal += Double.parseDouble(fields[2])*shares;
	    							st.setString(6, Integer.toString(shares));
	    							shares = 0;
	    						}
	    						
	    						if(shares == 0){
	    						st.setString(7, Double.toString(100*(profit/InitialTransaction)));
	    						}else{
	    							st.setString(7,"0.0");
	    							}
	    						st.setString(8, Integer.toString(shares));
		    					st.setString(9, Double.toString(MoneyTotal));
	    						st.executeUpdate();
	    						//OrderManager.OrderResult sold = OrderManager.getInstance().sellOrder(fields[0], Double.parseDouble(fields[1]), 
		    						//	Integer.parseInt(fields[3]));
	    						
	    						if(shares == 0 && InitialTransaction != 0){
	    	            			if( Math.abs(profit) >= (0.01*InitialTransaction)){
	    	            				System.out.println("Bollinger Bands Strategy exiting");
	    	            				System.out.println("Profit: "+100*(profit/InitialTransaction)+"%");
	    	            				System.out.println("Number of transactions: "+Transactions);
	    	            				return;
	    	            			}
	    	            		}
            				}
            			}
            		
            		}
            		
            	}
        	
		}catch(IOException i){
			Logger log = Logger.getLogger(this.getClass());
			log.error("ERROR: " + i.getMessage());
		}catch(ClassNotFoundException c){
			Logger log = Logger.getLogger(this.getClass());
			log.error("ERROR: " + c.getMessage());
		}catch(SQLException s){
			Logger log = Logger.getLogger(this.getClass());
			log.error("ERROR: " + s.getMessage());
		}
	}
}
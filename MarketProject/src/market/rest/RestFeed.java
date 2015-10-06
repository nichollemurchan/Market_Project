package market.rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.logging.*;

@Path("/RestFeed")
public class RestFeed {

	@GET
	@Produces("text/html")
	public String getData(@QueryParam("str") String str) {
		 String result = "<table class='table table-striped'<thead><tr><th>StockS</th><th>Ask price</th><th>Bid price</th><th>Change</th></tr></thead>";
		try{
		List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
		StringBuilder url = 
	            new StringBuilder("http://finance.yahoo.com/d/quotes.csv?s=");
            for (String s: items){
            	url.append(s + "+");
            }
        url.deleteCharAt(url.length()-1);
        url.append("&f=sabp2&e=.csv");
        
        String theUrl = url.toString();
        URL obj = new URL(theUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
		@SuppressWarnings("unused")
		int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
       // String result="";
      
        
        while ((inputLine = in.readLine()) != null){
        	System.out.println("Input Line: " + inputLine);
        	String[] fields = inputLine.split(",");
        	if (fields[1].equals("N/A") || fields[2].equals("N/A")){

            }
            else{
                 result += "<tr><td>"+fields[0]+"</td><td>"+fields[1]+"</td><td>"+fields[2]+"</td><td>"+fields[3]+"</td></tr>";
            }

        }    
        result += "</table>";
		
		}catch(IOException i){
			Logger log = Logger.getLogger(this.getClass());
			log.error("ERROR: " + i.getMessage());
		}
		return result;
	}
}

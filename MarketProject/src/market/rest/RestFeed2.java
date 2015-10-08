package market.rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.util.Arrays;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.logging.*;

@Path("/RestFeed2")
public class RestFeed2 {

	@GET
	@Produces("text/html")
	public String getData(){
		String result="<table class='table table-striped'><thead><tr><th> Stocks </th><th> Ask Price </th>"
				+ "<th> Ask Size </th><th> Bid Price </th><th> Bid Size </th><th> Change </th></tr></thead>";
		try{
			
		//String[] stocks = {"GOOG", "IBM", "YHOO", "TXN", "AA", "AAPL"};
		String[] stocks = {"ABF.L", "ADM.L", "AV.L", "BP.L","DC.L","DGE.L","DLG.L","EZJ.L", "HSBA.L", "ITV.L"};
		StringBuilder url = 
	            new StringBuilder("http://finance.yahoo.com/d/quotes.csv?s=");
		for (String s : stocks)
            url.append(s + "+");
        url.deleteCharAt(url.length()-1);
        //url.append("&f=sabp2&e=.csv");
        url.append("&f=saa5bb6p2&e=.csv");
        
        String theUrl = url.toString();
        URL obj = new URL(theUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
		@SuppressWarnings("unused")
		int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        //String result = null;
       
       // String[] fields = null;
        while ((inputLine = in.readLine()) != null){
        	String[] fields = inputLine.split(",");
        	
        	
        	result += "<tr><td>"+fields[0]+"</td><td>"+ round(Double.parseDouble(fields[1]), 2) +"</td><td>"+fields[2]+"</td><td>"+ round(Double.parseDouble(fields[3]), 2) +"</td><td>"
                	+fields[4]+"</td><td>"+ fields[5] + "</td></tr>"; 
        }  
        result += "</table>";
       // Arrays.fill(fields, null);
		
		}catch(IOException i){
			Logger log = Logger.getLogger(this.getClass());
			log.error("ERROR: " + i.getMessage());
		}
		return result;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}


package market.dal;

import market.rest.*;

import java.util.List;

public class DataAccess {
	
	public Double calcShortAverage(List<Double> shortList, int shortCount) {
    	double shortAve = 0;
    	double shortTotal = 0;

       		for(Double d: shortList){
    			shortTotal += d;
    		}
    		shortAve = shortTotal/shortCount;
		return shortAve;
	}
	
	public Double calcLongAverage(List<Double> longList, double longCount) {
    	double longAve = 0;
    	double longTotal = 0;

       		for(Double d: longList){
    			longTotal += d;
    		}
    		longAve = longTotal/longCount;
		return longAve;
	}
	
	public Double calcProfitPercent(double profit, double initialTransaction) {
		double profitP = 0;
		
		profitP = 100 * (profit/initialTransaction);
		return profitP;
	}
	
	public Double calcSEMA(double price, double s, double weighting) {
		
		Double SEMA = ((price - s)*(2/(1+weighting))+s);
		
    	
    	return SEMA;
	}
	
	public Double calcLEMA(double price, double l, double weighting) {
		
		Double LEMA = ((price - l)*(2/(1+weighting))+l);
		
		return LEMA;
	}
	
	public Double calcMovingStandDev(List<Double> sdList, double sDCount) {
		
    	double sD = 0;
    	double sdTotal = 0;

       		for(Double d: sdList){
    			sdTotal += d;
    		}
    		sD = sdTotal/sDCount;

    	return sD;
	}
}

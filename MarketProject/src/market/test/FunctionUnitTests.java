package market.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import java.util.List;

import market.dal.DataAccess;

import org.junit.Test;

public class FunctionUnitTests {
List<Double> exList = new ArrayList<>();
int num = 10;
Double d = 3.0;
Double d1 = 9.0;
Double d2 = 15.0;

DataAccess dal = new DataAccess();

	/**
	 * this unit test, tests the calculation that performs the short moving averages
	 * we input a list of prices and a number to track the amount of entries
	 * @param List<Double>(prices),  Integer no. of entries in list
	 */
	@Test
	public void testCalcShortAverage() {
		exList.add(101.0);
		exList.add(102.0);
		exList.add(103.0);
		exList.add(104.0);
		exList.add(105.0);
		Double shortAve = dal.calcShortAverage(exList, num);
		assertEquals(51.5, shortAve, 0.0001);
	}

	/**
	 * this unit test, tests the calculation that performs the long moving averages
	 * we input a list of prices and a number to track the amount of entries
	 * @param List<Double> (prices),  Integer no. of entries in list
	 */
	@Test
	public void testCalcLongAverage() {
		exList.add(101.0);
		exList.add(102.0);
		exList.add(103.0);
		exList.add(104.0);
		exList.add(105.0);
		Double longAve = dal.calcLongAverage(exList, num);
		assertEquals(51.5, longAve, 0.0001);
	}

	/**
	 * this unit test, tests the calculation that performs the profit percentage
	 * we input two numbers, the first is a percentage of the second
	 * @param Integer (1st) out of Integer (2nd)
	 */
	@Test
	public void testCalcProfitPercent() {

		Double profit = dal.calcProfitPercent(d, d1);
		assertEquals(33.3, profit, 0.1);
	}

	/**
	 * this unit test, test the calculation that performs the short exponential moving average (SEMA)
	 * we input the current price, the previous value of SEMA,  and the weighting factor
	 * @param Integer current price, Integer previous value of SEMA, Integer weighting factor
	 */
	@Test
	public void testCalcSEMA() {

		double sema = dal.calcSEMA(d, d1, d2);
		assertEquals(8.25, sema, 0.1);
	}

	/**
	 * this unit test, test the calculation that performs the long exponential moving average (LEMA)
	 * we input the current price, the previous value of LEMA,  and the weighting factor
	 * @param Integer current price, Integer previous value of LEMA, Integer weighting factor
	 */
	@Test
	public void testCalcLEMA() {

		double lema = dal.calcLEMA(d, d1, d2);
		assertEquals(8.25, lema, 0.1);
	}
	
	/**
	 * this unit test, tests the calculation that performs the moving standing deviation
	 * we input a list of prices and a number to track the amount of entries
	 * @param List<Double> (price-average),  Integer no. of entries in list
	 */
	@Test
	public void testCalcMovingStandDev() {
		exList.add(101.0);
		exList.add(102.0);
		exList.add(103.0);
		exList.add(104.0);
		exList.add(105.0);
		Double sd = dal.calcMovingStandDev(exList, num);
		assertEquals(51.5, sd, 0.0001);
	}

}

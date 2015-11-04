package com.bigdata.util;

public class Stock {

	public void diedai() {
		// double sum = 7693;
		double sum = 4600;
		double temp = 0l;
		double rate = 0.03;

		for (int i = 1; i < 270; i++) {
			// sum = sum + (sum*rate);
			temp = sum;
			sum = sum + (sum * rate);
			System.out.println(i + "==" + sum + "==" + (sum - temp));
		}
		System.out.println(sum);
	}

	public void diedai1() {
		// double sum = 7693;
		double base = 3000;
		double temp = 0l;
		double rate = 0.03;

		double sum = 0d;
		for (int i = 1; i < 270; i++) {
			// sum = sum + (sum*rate);
			if ((sum / base) >= 1) {
				temp = base + Double.valueOf(sum / base).intValue() * base;
			} else {
				temp = base;
			}
			sum = sum + temp * rate;
			System.out.println(i + "==" + temp + "==" + sum);
		}
	}
	
	public static void main(String[] args) {
		Stock stock = new Stock();
		stock.diedai1();
	}
}

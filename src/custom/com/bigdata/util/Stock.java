package com.bigdata.util;

public class Stock {

	//每天涨rate
	public void diedai() {
		// double sum = 7693;
		double sum = 20000;
		double temp = 0l;
		double rate = 0.01;

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
		double rate = 0.01;

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
	
	public void diedai2() {
		// double sum = 7693;
		double base = 3000;
		double temp = 0l;

		double sum = 0d;
		for (int i = 1; i < 270; i++) {
			// sum = sum + (sum*rate);
			if ((sum / base) >= 1) {
				temp = base + Double.valueOf(sum / base).intValue() * base;
			} else {
				temp = base;
			}
			sum = sum + (temp/base)*90;
			System.out.println(i + "==" + temp + "==" + sum);
		}
	}
	
	
	//一半资金用于迭代投资方式
	public void diedai3() {
		double base = 5000;
		double temp = 0l;
		double rate = 0.01;

		double sum = 0d;
		for (int i = 1; i < 270; i++) {
				if ((sum / base) >= 1) {
					temp = base + (Double.valueOf(sum / base)/2) * base;
				} else {
					temp = base;
				}
				sum = sum + (temp/base)* (base*rate);
			System.out.println(i + "==" + temp + "==" + sum);
		}
	}
	
	public static void main(String[] args) {
		Stock stock = new Stock();
		stock.diedai3();
	}
}

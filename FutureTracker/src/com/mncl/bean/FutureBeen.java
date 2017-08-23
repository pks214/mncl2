package com.mncl.bean;

import java.util.Date;

public class FutureBeen implements Comparable<FutureBeen>{

	private String ymd;
	private String strTime;




	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getOpen() {
		return Open;
	}

	public void setOpen(Double open) {
		Open = open;
	}

	public Double getHigh() {
		return High;
	}

	public void setHigh(Double high) {
		High = high;
	}
	public Double getLow() {
		return Low;
	}

	public void setLow(Double low) {
		Low = low;
	}

	public Double getClose() {
		return Close;
	}

	public void setClose(Double close) {
		Close = close;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getOpenoi() {
		return openoi;
	}

	public void setOpenoi(int openoi) {
		this.openoi = openoi;
	}




	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public FutureBeen(String ticker, Date time, Double open,
			Double high, Double low, Double close, int volume, int openoi,String ymd,String strTime) {
		super();		
		this.ticker = ticker;
		this.time = time;
		Open = open;
		High = high;
		Low = low;
		Close = close;
		this.volume = volume;
		this.openoi = openoi;
		this.ymd=ymd;
		this.strTime=strTime;
	}



	@Override
	public String toString() {
		return  ymd+","+ticker + ", " + time + ", "
				+ Open + ", " + High + ", " + Low + ", " + Close
				+ ", " + volume + ", " + openoi+","+strTime;
	}



	private String ticker;
	private Date time;
	private Double Open;
	private Double High;
	private Double Low;
	private Double Close;
	private int volume;
	private int openoi;





	public static void main(String[] args) {


	}

	@Override
	public int compareTo(FutureBeen o) {
		return this.getTicker().compareTo(o.getTicker());
	}
}

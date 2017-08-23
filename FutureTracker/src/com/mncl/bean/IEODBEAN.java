package com.mncl.bean;

public class IEODBEAN{
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOpen() {
		return Open;
	}
	public void setOpen(String open) {
		Open = open;
	}
	public String getHigh() {
		return High;
	}
	public void setHigh(String high) {
		High = high;
	}
	public String getLow() {
		return Low;
	}
	public void setLow(String low) {
		Low = low;
	}
	public String getClose() {
		return Close;
	}
	public void setClose(String close) {
		Close = close;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getOpenoi() {
		return openoi;
	}
	public void setOpenoi(String openoi) {
		this.openoi = openoi;
	}
	@Override
	public String toString() {
		return  ticker + ", " + date + ","
				+ time + "," + Open + "," + High + "," + Low
				+ "," + Close + "," + volume + ", "
				+ openoi ;
	}

	private String ticker;
	public IEODBEAN(String ticker, String date, String time, String open,
			String high, String low, String close, String volume, String openoi) {
		super();
		this.ticker = ticker;
		this.date = date;
		this.time = time;
		Open = open;
		High = high;
		Low = low;
		Close = close;
		this.volume = volume;
		this.openoi = openoi;
	}





	private String date;
	private String time;
	private String Open;
	private String High;
	private String Low;
	private String Close;
	private String volume;
	private String openoi;





	public static void main(String[] args) {


	}


}

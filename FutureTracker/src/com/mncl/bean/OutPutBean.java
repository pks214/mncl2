package com.mncl.bean;

public class OutPutBean {
      public OutPutBean(String entryDate, String exitDate, String symbolName,
			String action, String t1p1, String t2p2, String t1p1Time,
			String t2p2Time, String entryPrice, String exitPrice,
			String entryTime, String exitTime) {
		super();
		this.entryDate = entryDate;
		this.exitDate = exitDate;
		this.symbolName = symbolName;
		this.action = action;
		this.t1p1 = t1p1;
		this.t2p2 = t2p2;
		this.t1p1Time = t1p1Time;
		this.t2p2Time = t2p2Time;
		this.entryPrice = entryPrice;
		this.exitPrice = exitPrice;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
	}


	private String entryDate;
      @Override
	public String toString() {
		return entryDate + "," + exitDate
				+ "," + symbolName + "," + action
				+ "," + t1p1 + "," + t2p2 + ","
				+ t1p1Time + "," + t2p2Time + ","
				+ entryPrice + "," + exitPrice + ","
				+ entryTime + "," + exitTime;
	}


	private String exitDate;
      private String symbolName;
      private String action;
      private String t1p1;
      private String t2p2;
      private String t1p1Time;
      private String t2p2Time;
      private String entryPrice;
      private String exitPrice;
      private String entryTime;
      private String exitTime;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

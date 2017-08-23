package com.mncl.bean;

public class ChangeBean {

	private String lastTime;
	private String basetime;
	private String t1p1;
	private String t2p2;


	public String getSymbolName() {
		return symbolName;
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public double getClosePer() {
		return closePer;
	}

	public void setClosePer(double closePer) {
		this.closePer = closePer;
	}

	public double getOpenPer() {
		return openOiPer;
	}
	public void setOpenPer(double openPer) {
		this.openOiPer = openPer;
	}

	public double getVolumePer() {
		return volumePer;
	}

	public void setVolumePer(double volumePer) {
		this.volumePer = volumePer;
	}



	@Override
	public String toString() {
		return  symbolName + ","
				+ closePer + "," + openOiPer + ","
				+ volumePer+","+basetime+","+lastTime+","+t1p1+","+t2p2;
	}



	public ChangeBean(String symbolName, double closePer, double openPer,
			double volumePer, String lastTimeBase, String firsttime,String t1p1,String t2p2) {
		this.symbolName = symbolName;
		this.closePer = closePer;
		this.openOiPer = openPer;
		this.volumePer = volumePer;
		this.basetime = lastTimeBase;
		this.lastTime = firsttime;
		this.t1p1=t1p1;
		this.t2p2=t2p2;
		
	}



	private String symbolName;
	private double closePer;
	private double openOiPer;
	private double volumePer;


	public static void main(String[] args) {


	}

}

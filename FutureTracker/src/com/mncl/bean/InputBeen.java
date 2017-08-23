package com.mncl.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.opencsv.CSVReader;



public class InputBeen {
	public InputBeen(String starttime, String buytime, String pSquareOffStartTime,
			String pSquareOffEndTime, String endtime, int noofStock, String inputDir) {
		Starttime = getdate(starttime);
		this.buytime = getdate(buytime);
		PSquareOffStartTime =getdate( pSquareOffStartTime);
		PSquareOffEndTime = getdate(pSquareOffEndTime);
		Endtime = getdate(endtime);
		this.noofStock = noofStock;
		this.inputDir = inputDir;
	}

	public Date getStarttime() {
		return Starttime;
	}

	public void setStarttime(Date starttime) {
		Starttime = starttime;
	}

	public Date getEndtime() {
		return Endtime;
	}

	public void setEndtime(Date endtime) {
		Endtime = endtime;
	}

	public int getNoofStock() {
		return noofStock;
	}

	public void setNoofStock(int noofStock) {
		this.noofStock = noofStock;
	}

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}
	/*private Date getdate(String [] arr){
		Calendar time1=Calendar.getInstance();
		time1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
		time1.set(Calendar.MINUTE, Integer.parseInt(arr[1]));
		time1.set(Calendar.SECOND,Integer.parseInt(arr[2]) );		
		return time1.getTime();	
	}*/
	private Date getdate(String  arr){
		SimpleDateFormat sdf = new java.text.SimpleDateFormat ("HH:mm:ss");		
		try {
			return sdf.parse (arr);
		} catch (ParseException e) {

			e.printStackTrace();
		};
		return null;		
	} 

	public InputBeen(String inputFilePath) {

		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(new File(inputFilePath)));
			String[] rowData = null;
			int lineno=1;
			try {
				while((rowData = reader.readNext()) != null)
				{
					if(lineno==1){					
						Starttime=getdate(rowData[1]);
					}else if(lineno==2){						
						Endtime=getdate(rowData[1]);
					}else if(lineno==3){
						noofStock=Integer.parseInt(rowData[1]);
					}else if(lineno==4){
						this.inputDir=rowData[1];
					}else if(lineno==5){						
						PSquareOffStartTime=getdate(rowData[1]);
					}else if(lineno==6){					
						PSquareOffEndTime=getdate(rowData[1]);
					}else if(lineno==7){						
						buytime=getdate(rowData[1]);
					}
					lineno++;				 
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		try {
			if(reader!=null){
				reader.close();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private Date  Starttime;
	private Date  buytime;
	public Date getBuytime() {
		return buytime;
	}

	public void setBuytime(Date buytime) {
		this.buytime = buytime;
	}

	private Date  PSquareOffStartTime;
	private Date  PSquareOffEndTime;
	public Date getPSquareOffStartTime() {
		return PSquareOffStartTime;
	}

	public void setPSquareOffStartTime(Date pSquareOffStartTime) {
		PSquareOffStartTime = pSquareOffStartTime;
	}

	public Date getPSquareOffEndTime() {
		return PSquareOffEndTime;
	}

	public void setPSquareOffEndTime(Date pSquareOffEndTime) {
		PSquareOffEndTime = pSquareOffEndTime;
	}

	private Date  Endtime;
	private int noofStock;
	private String inputDir;


	public static void main(String[] args) {
		String path="E:\\CallPutChecker\\InputParam.csv";
		InputBeen inputObj=new InputBeen(path);
		System.out.println(inputObj.getStarttime() +"-"+inputObj.getEndtime());
		System.out.println(inputObj.getInputDir());
		System.out.println(inputObj.getNoofStock());
	}

}

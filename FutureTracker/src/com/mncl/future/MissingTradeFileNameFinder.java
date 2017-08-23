package com.mncl.future;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.opencsv.CSVReader;

public class MissingTradeFileNameFinder {
	private static  ArrayList<Date> tradingDateList;
	private static ArrayList<String> missingFilesname;
	@SuppressWarnings("resource")
	public static boolean checkForMissingFiles(String
			tradingDateFilePath, String inputDataFolderName) {
		tradingDateList=new ArrayList<Date>();
		missingFilesname=new ArrayList<String>();
		try {
			CSVReader reader;
			reader = new CSVReader(new FileReader(new File(tradingDateFilePath)));
			String[] rowData = null;
			while((rowData = reader.readNext()) != null)
			{	
				tradingDateList.add(getTradingdate(rowData[0].trim())) ;
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}	

		File []files=new File(inputDataFolderName).listFiles();
		String ymd=null;
		Boolean isFirstTime=true;
		int indexDayno=-1;
		Date validDate;
		Date fileDate;
		for(File f:files){
			ymd=f.getName().replace(".csv", "");			
			System.out.println(ymd);
			if(isFirstTime){
				try {
					isFirstTime=false;
					indexDayno=tradingDateList.indexOf(
							new SimpleDateFormat("yyyyMMdd").parse(ymd));
					if(indexDayno==-1){
						System.out.println("Date File is Wrong" +
								" please check for date: " +ymd);
						break;
					}

				} catch (ParseException e) {				
					e.printStackTrace();
				}
			}else{
				try{
					validDate=tradingDateList.get(indexDayno+1);
					fileDate=getTradingdateForFile(ymd);				
					if(validDate.equals(fileDate)){
						indexDayno++;
					}else{					
						while(!validDate.equals(fileDate)){
							missingFilesname.add("Missing File for date::"
									+new SimpleDateFormat("yyyyMMdd").format(validDate));	
							indexDayno++;	
							validDate=tradingDateList.get(indexDayno+1);
						}
						indexDayno++;
					}
				}catch (Exception ex){				
					System.out.println("Kindly ADD latest Date to Date Input File And Run the Code Again");
					return false;				
				}
			}			
		}
		if(missingFilesname.size()==0){
			System.out.println("No File is Missing Input Dir is Correct");
			return false;
		}else{
			for(String d:missingFilesname){             
				System.out.println(d);
			}
			return true;
		}		
	}
	private static Date getTradingdateForFile(String  ymd){
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
		Date date=null;
		try {
			date = dt.parse(ymd);
		} catch (ParseException e) {			
			e.printStackTrace();
		}	
		return date;
	}
	private static Date getTradingdate(String  ddmmyy){
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yy");
		Date date=null;
		try {
			date = dt.parse(ddmmyy);
		} catch (ParseException e) {			
			e.printStackTrace();
		}	
		return date;
	}
	public static void main(String[] args) {
		String tradingDateFilePath="E:\\FutureTracker\\tradingday.csv";
		String inputDataFolderName = "D:\\Data\\NewFutureData\\";
		MissingTradeFileNameFinder
		.checkForMissingFiles(tradingDateFilePath,inputDataFolderName);
	}

}

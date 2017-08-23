package com.mncl.future;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mncl.bean.ChangeBean;
import com.mncl.bean.FutureBeen;
import com.mncl.bean.InputBeen;
import com.mncl.bean.OutPutBean;
import com.mncl.bean.PriceOiVolComp;
import com.mncl.bean.SymbTimeComp;
import com.opencsv.CSVReader;

public class FutureTrakerV4 {
	private  ArrayList<OutPutBean> outputList;	
	private String outputFileName;	
	public FutureTrakerV4(String outputFileName, String tradingDateFilePath,
			String inputDataFolderName, Boolean isFilterAllow){
		outputList = new ArrayList<OutPutBean>();
		this.outputFileName=outputFileName;

		//	String tradingDateFilePath="E:\\FutureTracker\\tradingday.csv";
		//	String inputDataFolderName="D:\\Data\\temp\\";
		boolean missingFile=MissingTradeFileNameFinder
				.checkForMissingFiles(tradingDateFilePath,inputDataFolderName);

		if(missingFile||isFilterAllow){
			if(!isFilterAllow){

			}else{
				System.out.println("Input data Directory have Missing Files please  " +
						"correct inputFile or dont allow Filter");
				System.exit(0);	
			}		
		}

	}

	private FutureBeen getFutureBean(String [] rowData, Date currentTime){
		FutureBeen callPutBean=new FutureBeen(rowData[0], currentTime,
				Double.parseDouble(rowData[3]), Double.parseDouble(rowData[4]), Double.parseDouble(rowData[5]),
				Double.parseDouble(rowData[6]), Integer.parseInt(rowData[7]), 
				Integer.parseInt(rowData[8].trim()),rowData[1],rowData[2]);
		return callPutBean;		
	}

	private Date getdate(String  arr){
		SimpleDateFormat sdf = new java.text.SimpleDateFormat ("HH:mm:ss");		
		try {
			return sdf.parse (arr);
		} catch (ParseException e) {

			e.printStackTrace();
		};
		return null;		
	} 

	private Date getdateAfter5Min(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, 5);      
		return c.getTime();		
	} 

	private void startTracking(InputBeen inputObj) throws IOException {
		File[] files=new File(inputObj.getInputDir()).listFiles();
		Date starttime=inputObj.getStarttime();
		Date endtime=inputObj.getEndtime();
		Date squareStartTime=inputObj.getPSquareOffStartTime();
		Date squareEndTime=inputObj.getPSquareOffEndTime();
		Date buyTime=inputObj.getBuytime();
		Date buyEndTime=getdateAfter5Min(buyTime);				
		String prevYmd=null;
		Map<String,FutureBeen>entryPriceMapPrev=new HashMap<String, FutureBeen>();
		ArrayList<String> topLongShortListPrev=new ArrayList<>();
		boolean isFirstDay=true;
		for(File csvFiles:files){
			List<FutureBeen>dataListOpen=new ArrayList<FutureBeen>();
			Map<String,FutureBeen>exitPriceMap=new HashMap<String, FutureBeen>();
			Map<String,FutureBeen>entryPriceMap = new HashMap<String, FutureBeen>();			
			ArrayList<String> topLongShortList=new ArrayList<>();
			CSVReader reader;
			reader = new CSVReader(new FileReader(csvFiles));
			String[] rowData = null;
			while((rowData = reader.readNext()) != null)
			{				
				Date currentTime=getdate(rowData[2]);
				if(isFirstDay){					                   
					if(currentTime.equals(buyTime)||
							currentTime.equals(buyEndTime)
							||currentTime.after(buyTime)&&
							currentTime.before(buyEndTime) ){						
						entryPriceMap.put(rowData[0].trim(),getFutureBean(rowData,currentTime));						
					}else if(currentTime.equals(starttime)||
							currentTime.equals(endtime)
							||currentTime.after(starttime)&&
							currentTime.before(endtime) ){						
						dataListOpen.add(getFutureBean(rowData,currentTime));						
					}else if(currentTime.before(starttime)){
						break;
					}
				}else{					
					if(currentTime.equals(buyTime)||
							currentTime.equals(buyEndTime)
							||currentTime.after(buyTime)&&
							currentTime.before(buyEndTime) ){						
						entryPriceMap.put(rowData[0].trim(),getFutureBean(rowData,currentTime));						
					}else if(currentTime.equals(starttime)||currentTime.equals(endtime)
							||currentTime.after(starttime)&&currentTime.before(endtime) ){						
						dataListOpen.add(getFutureBean(rowData,currentTime));     
					}else if(currentTime.equals(squareStartTime)||
							currentTime.equals(squareEndTime)
							||currentTime.after(squareStartTime)
							&&currentTime.before(squareEndTime)){						
						exitPriceMap.put(rowData[0].trim(),getFutureBean(rowData,currentTime));
					}
				}

			}			

			Collections.sort(dataListOpen, new SymbTimeComp());									
			String ymd=csvFiles.getName().replace(".csv", "");
			topLongShortList=getLongShortTop(
					getChangeForLongShort(dataListOpen),
					inputObj.getNoofStock());				

			if(!isFirstDay){//here close position buy current closePrice and prev buy and longshort
				/*for(String out:topLongShortListPrev){
					System.out.println(out.toString());
				}

				for (Entry<String, FutureBeen> entry : buyPriceMapPrev.entrySet())
				{
					System.out.println(entry.getValue().toString());
				}

				for (Entry<String, FutureBeen> entry : closePriceMap.entrySet())
				{
					System.out.println(entry.getValue().toString());
				}*/

				outputList.addAll(generateOutput(entryPriceMapPrev,
						exitPriceMap,topLongShortListPrev,ymd,prevYmd));				
			}

			/*for (Entry<String, FutureBeen> entry : buyPriceMap.entrySet())
			{
				System.out.println(entry.getValue().toString());
			}

			for (Entry<String, FutureBeen> entry : closePriceMapPrev.entrySet())
			{
				System.out.println(entry.getValue().toString()+"  :: "+ymd);
			}*/
			topLongShortListPrev=topLongShortList;
			entryPriceMapPrev=entryPriceMap;
			prevYmd=ymd;
			reader.close();
			isFirstDay=false;
		}

		/*for(OutPutBean out:outputList){
			System.out.println(out.toString());

		}*/
		createFile();

	}

	private   void createFile() throws IOException{
		File temp=new File(outputFileName);
		temp.createNewFile();
		FileWriter writer = new FileWriter(temp); 
		String heading="EntryDate"+ "," + "ExitDate"
				+ "," + "Symbol" + "," + "Action"
				+ "," + "T1P1" + "," + "T2P2" + ","
				+ "T1P1Time" + "," + "T2P2Time" + ","
				+ "EntryPrice" + "," + "ExitPrice" + ","
				+ "EntryTime" + "," + "ExitTime";
		writer.write(heading+"\n");
		for(OutPutBean  out:outputList){
			writer.write(out.toString()+"\n");
		}
		writer.flush();
		writer.close();
	}

	private ArrayList<OutPutBean> generateOutput(Map<String, FutureBeen> entryPriceMapPrev,
			Map<String, FutureBeen> exitPriceMap,
			ArrayList<String> topLongShortListPrev, String ymd, String prevYmd) {
		ArrayList<OutPutBean> outputList=new ArrayList<OutPutBean>();
		for(String out:topLongShortListPrev){
			String data[]=out.split(",");			
			FutureBeen entryBean=null;
			FutureBeen exitBean=null;
			try{
				entryBean=entryPriceMapPrev.get(data[1].trim());
				exitBean=exitPriceMap.get(data[1].trim());
				if(((entryBean.getOpen()- exitBean.getOpen())/entryBean.getOpen())*100>40){
					System.out.println("abnormal exit For symbol:"+data[1].trim()
							+" :  Tradingdate:"+ymd +" :EtryPrice:"+entryBean.getOpen() +" :" +
							"ExitPrice:"+ exitBean.getOpen());
					outputList.add(new OutPutBean(prevYmd, ymd, data[1].trim(),
							data[0].trim(), data[7], data[8], data[5], data[6],
							entryBean.getOpen().toString(), "Split-N/A",
							entryBean.getStrTime(), exitBean.getStrTime()));	
				}else{
					outputList.add(new OutPutBean(prevYmd, ymd, data[1].trim(),
							data[0].trim(), data[7], data[8], data[5], data[6],
							entryBean.getOpen().toString(), exitBean.getOpen().toString(),
							entryBean.getStrTime(), exitBean.getStrTime()));	
				}
			}catch(Exception ex){
				if(entryBean!=null){			
				}
			}
		}
		return outputList;		
	}

	private ArrayList<String> getLongShortTop(ArrayList<ChangeBean> outlist,int limit) {		
		Collections.sort(outlist,new PriceOiVolComp());
		ArrayList<ChangeBean> longList=new ArrayList<>();
		ArrayList<ChangeBean> shortList=new ArrayList<>();
		ArrayList<String> mainOutList=new ArrayList<>();
		/*for(ChangeBean out:outlist){
			System.out.println(out.toString());

		}*/
		for(ChangeBean out:outlist){			
			if(out.getClosePer()>0 && out.getVolumePer()>0 && out.getOpenPer()>0){
				longList.add(out);
			}
			if(out.getClosePer()<0 && out.getVolumePer()>0 && out.getOpenPer()>0){
				shortList.add(out);
			}
		}



		for(int i=0;i<limit;i++){
			try{
				mainOutList.add("Long"+","+longList.get(longList.size()-1-i).toString());
			}catch(Exception ex){

			}
		}

		for(int i=0;i<limit;i++){
			try{
				mainOutList.add("Short"+","+shortList.get(i).toString());
			}catch(Exception ex){

			}
		}

		/*	for(String out:mainOutList){
			System.out.println(out.toString());
		}*/
		return mainOutList;
	}

	private ArrayList<ChangeBean> getChangeForLongShort(List<FutureBeen> dataList) {
		boolean isFirstTime=true;
		String currentSymbol;
		String prevSymbol = null;
		double lastCloseBase=0;
		double lastOpenOiBase=0;
		double lastVolumeBase=0;
		double firstlose = 0;
		double firstOpenOi = 0;
		double FirstVolume = 0;
		String firsttime=null;
		String lastTimeBase=null;
		ArrayList<ChangeBean>outputList=new ArrayList<ChangeBean>();
		for(FutureBeen futureBean:dataList){			
			if(isFirstTime){
				isFirstTime=false;
				prevSymbol=currentSymbol=futureBean.getTicker();
				firstlose=futureBean.getClose();
				firstOpenOi=futureBean.getOpenoi();
				FirstVolume=futureBean.getVolume();
				firsttime=futureBean.getStrTime();
			}else{
				currentSymbol=futureBean.getTicker();
				if(currentSymbol.equals(prevSymbol)){
					lastCloseBase=futureBean.getClose();
					lastOpenOiBase=futureBean.getOpenoi();
					lastVolumeBase=futureBean.getVolume();	
					lastTimeBase=futureBean.getStrTime();
				}else{
					outputList.add(getChangeBean(firstlose,firstOpenOi,FirstVolume,
							prevSymbol,lastCloseBase,lastOpenOiBase,
							lastVolumeBase,firsttime,lastTimeBase));
					prevSymbol=currentSymbol=futureBean.getTicker();
					firstlose=futureBean.getClose();
					firstOpenOi=futureBean.getOpenoi();
					FirstVolume=futureBean.getVolume();
					firsttime=futureBean.getStrTime();
				}
			}
		}
		return outputList;
	}


	private ChangeBean getChangeBean(Double firstClose, double firstOpenOi,
			double firstVolume, String Symbol, Double lastCloseBase, double lastOpenOiBase, 
			double lastVolumeBase, String firsttime, String lastTimeBase) {
		double closePer=((lastCloseBase-firstClose)/lastCloseBase)*100;
		double openOiPer=((lastOpenOiBase-firstOpenOi)/lastOpenOiBase)*100;
		double volumePer=((lastVolumeBase-firstVolume)/lastVolumeBase)*100;
		ChangeBean chbean=new ChangeBean(Symbol, closePer, openOiPer,
				volumePer,firsttime,lastTimeBase,firstClose.toString() ,lastCloseBase.toString());
		return chbean;
	}

	public static void main(String[] args) throws ParseException {
		//String path="E:\\FutureTracker\\InputParam.csv";
		//InputBeen inputObj=new InputBeen(path);
		String tradingDateFilePath="E:\\FutureTracker\\tradingday.csv";
		String inputFutureDataDir="D:\\Data\\temp\\";
		String outputFileName="E:\\l1"+"OutPut.csv";
		String starttime="09:30:00";
		String endTime="14:30:00";
		int noOfTopToPick=3;		
		String pos_Square_Off_StartTime="09:15:00";
		String Pos_Square_Off_EndTime="09:30:00";
		String buyTime="14:31:59" ;
		Boolean isFilterAllow=true;


		InputBeen inputObj=new InputBeen(starttime, buyTime, 
				pos_Square_Off_StartTime,
				Pos_Square_Off_EndTime, endTime, noOfTopToPick,
				inputFutureDataDir);

		FutureTrakerV4 futureTracker=new FutureTrakerV4(outputFileName,
				tradingDateFilePath,inputFutureDataDir,isFilterAllow);		
		try {
			futureTracker.startTracking(inputObj);
		} catch (IOException e) {					
			e.printStackTrace();
		}
	}
}

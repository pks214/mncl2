package com.mncl.future.dataGenerator;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.mncl.bean.*;
import com.opencsv.CSVReader;



public class FutureDataGenerator
{

	private static void  convertDateFormateForInpufile(File f,String parrentDir) throws IOException, ParseException{
		//System.out.println(f.getAbsolutePath());
		CSVReader reader = new CSVReader(new FileReader(f)); 
		String[] rowData = null;
		List<com.mncl.bean.IEODBEAN>dumpArrayList=new ArrayList<IEODBEAN>();
		boolean isheader=true;
		while((rowData = reader.readNext()) != null)
		{
			if(isheader){
				isheader=false;
			}else{
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatternew=new SimpleDateFormat("yyyy-MM-dd");
				try {
					if(rowData[0].trim().endsWith("-I")){						
						dumpArrayList.add(new IEODBEAN(rowData[0],formatternew.format(formatter.parse(rowData[1])),
								rowData[2],rowData[3],rowData[4],rowData[5]
										,rowData[6],rowData[7],rowData[8]));
					}
				} catch(ArrayIndexOutOfBoundsException exception) {					
					dumpArrayList.clear();
					break;
				}

			}		
		}
		reader.close();					
		generateFutureFile(f.getName(),dumpArrayList,parrentDir);
	}

	private  static void generateFutureFile(String filename,List<IEODBEAN>dumplist,String parrentDir) throws IOException{
		if(dumplist.size()==0){
			return ;
		}
		try{
			Collections.sort(dumplist,new IeodTimeComp());
		}catch (Exception ex){			
			Collections.sort(dumplist,new IeodTimeComp());
		}
		File inputFile=new File(parrentDir, filename);
		if(inputFile.exists()){
			inputFile.delete();
		}
		File inputFile1=new File(parrentDir, filename);
		BufferedWriter bw=new BufferedWriter(new FileWriter(inputFile1, true)); 
		boolean isFirst=true;
		for(IEODBEAN str:dumplist){
			if(isFirst){
				String header="Ticker,Date,Time,OPEN,HIGH,LOW,CLOSE,Volume,Open Interest";
				bw.write(header+"\n");
				bw.write(str.toString()+"\n");
				isFirst=false;
			}else{	
				bw.write(str.toString()+"\n");
			}

		}
		bw.close();
	}
	private  static String   changeFileName(File f) {
		String str=f.getName().replace(".csv", "");
		str=str.substring(str.length()-8, str.length());
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		SimpleDateFormat formatternew=new SimpleDateFormat("yyyyMMdd");
		File newFile=null;
		try {
			newFile=new File(f.getParentFile()+"\\"+formatternew.format(formatter.parse(str))+".csv");
			f.renameTo(newFile);			
			return newFile.getName();
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return null;
	}

	public static void main(String[] args)  
	{
		//this will generate future data from ieod file 
		//first will change file name then it will grab current month future data
		//then short data by time

		//before run this code check for output dir it should be empty
		//		or dont contain file name of input dir file
		String inputdirname="D:\\Data\\temp\\"; //this is the raw dir of NSE_FO IEOD Data
		String outputDir="D:\\Data\\tempout\\";//in this folder Future output will be store
		File Dir=new File(inputdirname);		
		File[]listFile=Dir.listFiles();
		for(File pro:listFile){
			if(!pro.getName().contains("NSE")){
				break;
			}else{											
				changeFileName(pro);
			}
		}		
		File Dir1=new File(inputdirname);		
		File[]listFile1=Dir1.listFiles();
		for(File pro:listFile1){
			try {								
				convertDateFormateForInpufile(pro,outputDir);
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}

}


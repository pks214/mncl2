package com.mncl.bean;

import java.util.Calendar;
import java.util.Comparator;


public class IeodTimeComp implements Comparator<Object>{  
	public int compare(Object o1,Object o2){  
		IEODBEAN s1=(IEODBEAN)o1;  
		IEODBEAN s2=(IEODBEAN)o2; 
		String time1Arr[]=s1.getTime().split(":");
		String time2Arr[]=s2.getTime().split(":");
		Calendar time1=Calendar.getInstance();
		time1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time1Arr[0]));
		time1.set(Calendar.MINUTE, Integer.parseInt(time1Arr[1]));
		time1.set(Calendar.SECOND,Integer.parseInt(time1Arr[2]) );
		Calendar time2=Calendar.getInstance();
		time2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time2Arr[0]));
		time2.set(Calendar.MINUTE, Integer.parseInt(time2Arr[1]));
		time2.set(Calendar.SECOND,Integer.parseInt(time2Arr[2]) );
		if(time1.getTime().before(time2.getTime())){
			return 1;
		}else if(time2.getTime().before(time1.getTime())){
			return -1;
		}
		return 0;
	}  
}  
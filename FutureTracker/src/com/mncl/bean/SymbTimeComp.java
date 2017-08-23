package com.mncl.bean;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;


public class SymbTimeComp implements Comparator<FutureBeen>{

	@Override
	public int compare(FutureBeen c1, FutureBeen c2) {
		return new CompareToBuilder()
		.append(c1.getTicker(), c2.getTicker())
		.append(c1.getTime(), c2.getTime())
		.toComparison();
	}  
}  
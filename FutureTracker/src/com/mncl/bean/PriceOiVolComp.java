package com.mncl.bean;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class PriceOiVolComp implements Comparator<ChangeBean> {
	@Override
	public int compare(ChangeBean c1, ChangeBean c2) {
		return new CompareToBuilder()
		.append(c1.getClosePer(), c2.getClosePer())
		.append(c1.getOpenPer(), c2.getOpenPer())
		.append(c1.getVolumePer(), c2.getVolumePer())
		.toComparison();
	}

}

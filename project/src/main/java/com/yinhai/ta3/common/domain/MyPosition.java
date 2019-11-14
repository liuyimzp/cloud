package com.yinhai.ta3.common.domain;

import com.yinhai.modules.org.ta3.domain.po.impl.PositionPo;

import java.io.Serializable;

public class MyPosition extends PositionPo implements Serializable {

	private static final long serialVersionUID = -6679835250525233402L;
	private String positionext;

	public String getPositionext() {
		return positionext;
	}

	public void setPositionext(String positionext) {
		this.positionext = positionext;
	}

	public MyPosition(String positionext) {
		super();
		this.positionext = positionext;
	}

	public MyPosition() {
		super();
	}
	
	
}

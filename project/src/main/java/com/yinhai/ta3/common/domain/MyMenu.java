package com.yinhai.ta3.common.domain;

import com.yinhai.modules.authority.ta3.domain.po.MenuPo;

public class MyMenu extends MenuPo {
	private static final long serialVersionUID = -3382653254644735329L;
	private String menuext;

	public String getMenuext() {
		return menuext;
	}

	public void setMenuext(String menuext) {
		this.menuext = menuext;
	}

	public MyMenu(String menuext) {
		super();
		this.menuext = menuext;
	}

	public MyMenu() {
		super();
	}
	
}

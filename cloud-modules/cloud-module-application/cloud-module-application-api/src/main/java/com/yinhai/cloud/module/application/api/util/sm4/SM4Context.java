package com.yinhai.cloud.module.application.api.util.sm4;

import com.yinhai.core.common.api.util.ValidateUtil;

public class SM4Context

{
	private static final int INT_32 = 32;
	private int mode;
	
	private long[] sk;
	
	private boolean isPadding;

	public SM4Context()
	{
		this.mode = 1;
		this.isPadding = true;
		this.sk = new long[INT_32];
	}

	public boolean getPadding() {
		return isPadding;
	}

	public void setPadding(boolean padding) {
		isPadding = padding;
	}

	public long[] getSk() {
		if (!ValidateUtil.isEmpty(sk)){
			return sk.clone();
		}
		return null;
	}

	public void setSk(long[] sk) {
		if (!ValidateUtil.isEmpty(sk)){
			this.sk = sk.clone();
		}
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}

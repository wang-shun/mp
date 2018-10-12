package com.fiberhome.mapps.roptest;

public class AssertRule {
	String expresion;
	String operate;
	Object result;

	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion = expresion;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String toString() {
		return expresion + " " + operate + " " + result;
	}
}

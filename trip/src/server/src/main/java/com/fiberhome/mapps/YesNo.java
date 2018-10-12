package com.fiberhome.mapps;

public class YesNo {
	public static String Yes() {
		return "1";
	}
	
	public static String No() {
		return "0";
	}
	
	public static boolean valueOf(String flag) {
		return "1".equals(flag);
	}
}

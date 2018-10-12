package com.fiberhome.mapps.trip.utils;

import java.security.MessageDigest;

public class TripTokenUtil {

	public static String getToken(String appId,String appKey,String cannelCode ,String secret,String timeStamp){
		String token = "" ;
		try {
			String data = appId+appKey+cannelCode+secret+timeStamp ;
			byte[] source = data.getBytes("UTF-8");
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			messageDigest.update(source);
			byte[] args = messageDigest.digest() ;
			token = byte2hex(args) ; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token ;
	}
	
    /**
     * 二进制转十六进制字符串
     * 
     * @param bytes
     * @return
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }
	
//	public static void main(String[] args){
//		String ss  = TripTokenUtil.getToken("10003", "4d62558c411f4289a8ba40072c5bb326", "test-channel-code-02", "81cc6da54331413f87963fcf53b59bdd", "13922328999");
//		System.out.println(ss);
//	}
	
}

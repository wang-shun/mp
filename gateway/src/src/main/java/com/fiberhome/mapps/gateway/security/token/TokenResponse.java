package com.fiberhome.mapps.gateway.security.token;

public class TokenResponse {
	private String code;
	private String accessToken;
	private int timeout;
	
	public static TokenResponse fail() {
		TokenResponse response = new TokenResponse();
		response.setCode("0");
		return response;
	}
	
	public static TokenResponse success(String accessToken, int timeout) {
		TokenResponse response = new TokenResponse();
		response.setCode("1");
		response.setAccessToken(accessToken);
		response.setTimeout(timeout);
		
		return response;
	}
	
	private TokenResponse() {
		
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}

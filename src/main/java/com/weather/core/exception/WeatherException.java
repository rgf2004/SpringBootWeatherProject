package com.weather.core.exception;

public class WeatherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code = -1;
	
	public WeatherException()
	{
		super();
	}
	
	public WeatherException(int code, String msg)
	{
		super(msg);
		this.code = code;
	}
	
	public WeatherException(int code, String msg, Throwable ex)
	{
		super(msg,ex);
		this.code = code;
	}

		
	public int getCode() {
		return code;
	}
	
}

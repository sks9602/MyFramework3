package project.jun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HoFomatter 
{
	/**
	 * Date 를 SimpleDateFormat 문자열로 반환한다.
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toYmdFormat(Date date,String format)   {
		return (new SimpleDateFormat(format)).format(date);
	}
	/**
	 * Date 를 SimpleDateFormat(yyyy-MM-dd) 문자열로 반환한다.
	 * @param date
	 * @return
	 */
	public static String toYmdFormat(Date date)   {
		return toYmdFormat(date,"yyyy-MM-dd");
	}

	public static String toYmdFormat()   {
		return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String toCurrencyFormat( Object number )
	{
		java.math.BigDecimal value = new java.math.BigDecimal( number.toString() );
		return toCurrencyFormat( value, "###,###,###,###,###,###,###" );
	}

	public static String toCurrencyFormat( Object number, String format )
	{
		java.text.DecimalFormat df = new java.text.DecimalFormat( format );
		String value = null;
		
		try {
			value = df.format( number );
		}
		catch( Exception e )
		{
			value = "0";
		}
		
		return value;
	}

	public static String toNumberRawFormat( Double number )
	{
		return toCurrencyFormat( number, "#####################.###" );
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String toPointFormat( Object number )
	{
		java.math.BigDecimal value = new java.math.BigDecimal( number.toString() );
		return toPointFormat( value, "###,###,###,###,###,###,###.000" );
	}
	
	public static String getCurrencyFormat( Object number )
	{
		java.math.BigDecimal value = new java.math.BigDecimal( number.toString() );
		return getCurrencyFormat( value, "###,###,###,###,###,###,###" );
	}

	public static String getCurrencyFormat( Object number, String format )
	{
		java.text.DecimalFormat df = new java.text.DecimalFormat( format );
		String value = null;
		
		try {
			value = df.format( number );
		}
		catch( Exception e )
		{
			value = "0";
		}
		
		return value;
	}
	
	/**
	 * 년월일의 형태로 조회한다.
	 * @param oriValue
	 * @return
	 */
	public static String toDateFormat(Object oriValue, Map format) {
		if( oriValue instanceof String) {
			String value = oriValue.toString();
			try {
				// 길이가 14일경우에는 yyyy-MM-dd hh24:mi:ss
				if( value.length() == 14 ) {
					return toFormatedDate(value, "yyyyMMddHHmmss",  format.get("YMD").toString());
				} 
				// 길이가 12일경우에는 yyyy-MM-dd hh24:mi
				else if( value.length() == 12 ) {
					return toFormatedDate(value, "yyyyMMddHHmm",  format.get("YMD").toString());
				}
				// 길이가 8일경우에는 yyyy-MM-dd
				else if( value.length() == 8 ) {
					return toFormatedDate(value, "yyyyMMdd",  format.get("YMD").toString());
				} 
				// 길이가 6일경우에는 yyyy-MM
				else if( value.length() == 6 ){
					return toFormatedDate(value, "yyyyMM",  format.get("YM").toString());
				} else {
					return value;
				}
			} catch(Exception e) {
				return value;
			}	
		} else if( oriValue instanceof Date) {
			return (new SimpleDateFormat(format.get("YMD").toString())).format(oriValue);
		} else {
			return oriValue.toString();
		}
	}
	
	/**
	 * 년월일(시분초)의 형태로 조회한다.
	 * @param oriValue
	 * @return
	 */
	public static String toDateTimeFormat(Object oriValue, Map format) {
		if( oriValue instanceof String) {
			String value = oriValue.toString();
			try {
				// 길이가 14일경우에는 yyyy-MM-dd hh24:mi:ss
				if( value.length() == 14 ) {
					return toFormatedDate(value, "yyyyMMddHHmmss",  format.get("YMD").toString() + format.get("HMS").toString());
				} 
				// 길이가 12일경우에는 yyyy-MM-dd hh24:mi
				else if( value.length() == 12 ) {
					return toFormatedDate(value, "yyyyMMddHHmm",  format.get("YMD").toString() + format.get("HM").toString());
				}
				// 길이가 8일경우에는 yyyy-MM-dd
				else if( value.length() == 8 ) {
					return toFormatedDate(value, "yyyyMMdd",  format.get("YMD").toString() );
				} 
				// 길이가 6일경우에는 yyyy-MM
				else if( value.length() == 6 ){
					return toFormatedDate(value, "yyyyMM",  format.get("YM").toString() );
				} else {
					return value;
				}
			} catch(Exception e) {
				return value;
			}	
		} else if( oriValue instanceof Date) {
			return (new SimpleDateFormat(format.get("YMD").toString() + format.get("HMS").toString())).format(oriValue);
		} else {
			return oriValue.toString();
		}
	}
	
	/**
	 * 시분초의 형태로 조회한다.
	 * @param oriValue
	 * @return
	 */
	public static String toTimeFormat(Object oriValue, Map format) {
		if( oriValue instanceof String) {
			String value = oriValue.toString();
			try {
				// 길이가 14일경우에는 yyyy-MM-dd hh24:mi:ss
				if( value.length() == 14 ) {
					return toFormatedDate(value, "yyyyMMddHHmmss",  format.get("HMS").toString());
				} 
				// 길이가 12일경우에는 yyyy-MM-dd hh24:mi
				else if( value.length() == 12 ) {
					return toFormatedDate(value, "yyyyMMddHHmm",  format.get("HM").toString());
				}
				// 길이가 8일경우에는 yyyy-MM-dd
				else if( value.length() == 6 ) {
					return toFormatedDate(value, "yyyyMMdd",  format.get("HMS").toString() );
				} 
				// 길이가 6일경우에는 yyyy-MM
				else if( value.length() == 4 ){
					return toFormatedDate(value, "yyyyMM",  format.get("HM").toString() );
				} else {
					return value;
				}
			} catch(Exception e) {
				return value;
			}	
		} else if( oriValue instanceof Date) {
			return (new SimpleDateFormat(format.get("HMS").toString())).format(oriValue);
		} else {
			return oriValue.toString();
		}
	}
	
	/**
	 * 날짜의 형태로 조회한다.
	 * @param oriValue
	 * @return
	 */
	public static String toFormatedDate(String value, String oriType, String converType) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(oriType);
		Date date = sdf.parse(value);
		return (new SimpleDateFormat(converType).format(date));
	}

	
	

	public static String toPointFormat(Object value, String format) {
				
		java.text.DecimalFormat df = new java.text.DecimalFormat( format );
		String reslut = null;
		
		try {
			reslut = df.format( value );
		}
		catch( Exception e )
		{
			reslut = df.format( 0 );
		}
		
		return reslut;

	}
	
	public static String toNumberFormat(Object value, String format) {
		return toNumbericFormat(value, format);
	}
	
	public static String toIntFormat(Object value, String format) {
		return toNumbericFormat(value, format);
	}
	
	public static String toNumbericFormat(Object value, String format) {
		java.text.DecimalFormat df = new java.text.DecimalFormat( format );
		String reslut = null;
		
		try {
			reslut = df.format( value );
		}
		catch( Exception e )
		{
			reslut = "0";
		}
		
		return reslut;
	}

	
}

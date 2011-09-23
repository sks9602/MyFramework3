package project.jun.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HoUtil 
{
	
	private static final String gvSpecialKey = "*+-./@_";
	
	private static final char[]  arr62 = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z'};
	

	
	/**
	 * <pre>
	 * YYYYMMDD형태를 YYYY-MM-DD형태로 변경한다.
	 * </pre>
	 * @param yyyymmdd
	 * @return
	 * @exception
	 * @see
	 */
	public static String toYmdFormat(String yyyymmdd) {
		HashMap format = new HashMap();
		
		format.put("YMDHMS", "yyyy-MM-dd a hh:mm:ss");
		format.put("YMDHM", "yyyy-MM-dd a hh:mm");
		format.put("YMD", "yyyy-MM-dd");
		format.put("YM", "yyyy-MM");
		format.put("Y", "yyyy");
		return toYmdFormat(yyyymmdd, format);
	}
	
	/**
	 * <pre>
	 * YYYYMMDD형태를 YYYY-MM-DD[hh:mi:ss]형태로 변경한다.
	 * </pre>
	 * @param yyyymmdd
	 * @return
	 * @throws ParseException 
	 * @exception
	 * @see
	 */
	public static String toYmdFormat( String yyyymmdd, HashMap format )
	{
		try {
			SimpleDateFormat sdf = null;
			Date date = null;
			// 길이가 14일경우에는 yyyy-MM-dd hh24:mi:ss
			if( yyyymmdd.length() == 14 ) {
				sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				date = sdf.parse(yyyymmdd);
				return (new SimpleDateFormat((String) format.get("YMDHMS"))).format(date);
			} 
			// 길이가 12일경우에는 yyyy-MM-dd hh24:mi
			else if( yyyymmdd.length() == 12 ) {
				sdf = new SimpleDateFormat("yyyyMMddHHmm");
				date = sdf.parse(yyyymmdd);
				return (new SimpleDateFormat((String) format.get("YMDHM"))).format(date);
			}
			// 길이가 8일경우에는 yyyy-MM-dd
			else if( yyyymmdd.length() == 8 ) {
				sdf = new SimpleDateFormat("yyyyMMdd");
				date = sdf.parse(yyyymmdd);
				return (new SimpleDateFormat((String) format.get("YMD"))).format(date);
			} 
			// 길이가 6일경우에는 yyyy-MM
			else if( yyyymmdd.length() == 6 ){
				sdf = new SimpleDateFormat("yyyyMM");
				date = sdf.parse(yyyymmdd);
				return (new SimpleDateFormat((String) format.get("YM"))).format(date);
			} 
			// 길이가 4일경우에는 yyyy
			else if( yyyymmdd.length() == 4 ){
				sdf = new SimpleDateFormat("yyyyMM");
				date = sdf.parse(yyyymmdd);
				return (new SimpleDateFormat((String) format.get("Y"))).format(date);
			} else {
				return yyyymmdd;
			}
		} catch(Exception e) {
			return yyyymmdd;
		}
		
	}

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

	public static String getNow()   {
		return (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
	}
	
	public static String getNow(String s)   {
		return (new SimpleDateFormat(s)).format(new Date());
	}

	public static String replaceHtmlTags( String src, String[] srcTags, String[] replTags )
	{
		StringBuffer buf = new StringBuffer();
		buf.append( src );
		
		int i = 0;
		while( i < buf.length() )
		{
			if( buf.charAt(i) == '<' )
			{
				i = replaceHtmlTag( buf, i, srcTags, replTags );
				continue;
			}
			else
				i++;
		}
		
		return buf.toString();
	}
	
	private static int replaceHtmlTag( StringBuffer buf, int idx, String[] srcTags, String[] replTags )
	{
		for( int i = 0; i < srcTags.length; i++ )
		{
			if( buf.length() < idx + srcTags[i].length() + 1 )
				continue;
			
			// Start tag 
			if( buf.substring(idx, idx + srcTags[i].length() + 1).toLowerCase().equals("<" + srcTags[i].toLowerCase() ) )
			{
				int start = idx;
				int end = idx + srcTags[i].length() + 1;
				while( end < buf.length() )
				{
					if( buf.charAt(end) == '>' )
						break;
					else
						end++;
				}

				if( replTags[i].toLowerCase().equals("br") )
				{
					buf.replace(start, ++end, "<br />" );
					return idx + 6;
				}
				else if( replTags[i].length() > 0 )
				{
					buf.replace(start, ++end, "<" + replTags[i] + ">" );
					return idx + replTags[i].length() + 2;
				}
				else
				{
					buf.replace(start, ++end, "" );
					return idx;
				}
			}
			// End tag 
			else if( buf.substring(idx, idx+srcTags[i].length() + 2).toLowerCase().equals("</" + srcTags[i].toLowerCase() ) )
			{
				int start = idx;
				int end = idx + srcTags[i].length() + 2;
				while( end < buf.length() )
				{
					if( buf.charAt(end) == '>' )
						break;
					else
						end++;
				}
				
				if( replTags[i].toLowerCase().equals("br") )
				{
					buf.replace(start, ++end, "<br />" );
					return idx + 6;
				}
				else if( replTags[i].length() > 0 )
				{
					buf.replace(start, ++end, "</" + replTags[i] + ">");
					return idx + replTags[i].length() + 3;
				}
				else
				{
					buf.replace(start, ++end, "" );
					return idx;
				}
			}
		}
		
		return idx+1;
	}
	
	
	public static String extractNumberOnly( String value )
	{
		StringBuffer buf = new StringBuffer();
		int len = value.length();
		char c;
		
		for( int i = 0; i < len; i++ ) {
			c = value.charAt(i);
			if( c < '0' || c > '9' )
				continue;
			else
				buf.append( c );
		}
		
		return buf.toString();
	}
	
    public static Object loadInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    	if(classLoader == null)
    		classLoader = HoUtil.class.getClassLoader();

   		Class cls;
		cls = classLoader.loadClass(className);
		Object obj;
		
		obj = cls.newInstance();
    	return obj;
    }
    
    
    /**
     * 
     * 
     */
    public static String findFileInClassPath( String fileName )
    {
      	ClassLoader classLoader = HoUtil.class.getClassLoader();
    	URL url = classLoader.getResource( fileName );
    	if( url != null )
    		return url.getFile();
    	else
    		return null;
    }
    
	public static String decodeASCII( String value, String charSet ) throws UnsupportedEncodingException
	{
		int len = value.length();
		byte[] bytes = new byte[len/2];
		int k = 0;
		for( int i = 0; i < len; i+=2 ) {
			bytes[k] = (byte)Integer.parseInt(value.substring(i,i+2),16);
			k++;
		}
		
		return new String(bytes, charSet);
	}

	/**
	 * 
	 * @param format YY, MM, DD, HH, MI, SS, MS
	 * @return
	 */
	public static String getDate( String format )
	{
		Calendar now=Calendar.getInstance();

		int year  = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day   = now.get(Calendar.DAY_OF_MONTH);
		int hour  = now.get(Calendar.HOUR_OF_DAY);
		int min   = now.get(Calendar.MINUTE);
		int sec   = now.get(Calendar.SECOND);
		int msec  = now.get(Calendar.MILLISECOND);
		
		String sYear  = String.valueOf(year);
		String sMonth = lPad( String.valueOf(month), 2, "0" );
		String sDay   = lPad( String.valueOf(day), 2, "0" );
		String sHour  = lPad( String.valueOf(hour), 2, "0" );
		String sMin   = lPad( String.valueOf(min), 2, "0" );
		String sSec   = lPad( String.valueOf(sec), 2, "0" );
		String sMSec  = lPad( String.valueOf(msec), 2, "0" );
		
		String val = format.replaceAll("YY", sYear);
		val = val.replaceAll("MM", sMonth);
		val = val.replaceAll("DD", sDay);
		val = val.replaceAll("HH", sHour);
		val = val.replaceAll("MI", sMin);
		val = val.replaceAll("SS", sSec);
		val = val.replaceAll("MS", sMSec);
		
		return val;
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

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String toPointFormat( Object number, String format )
	{
		java.math.BigDecimal value = new java.math.BigDecimal( number.toString() );
		return toCurrencyFormat( value, format );
	}

	
	public static String rPad( String src, int size, String padStr )
	{
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		byte[] bSrc = src.getBytes();
		byte[] bPadStr = padStr.getBytes();
		
		buf.write( bSrc, 0, bSrc.length );
		while( buf.size() < size )
			buf.write( bPadStr, 0, bPadStr.length );
		
		return new String(buf.toByteArray(), 0, size);
	}
	
	public static String lPad( String src, int size, String padStr )
	{
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		byte[] bSrc = src.getBytes();
		byte[] bPadStr = padStr.getBytes();
		
		int leftSize = size - bSrc.length;
		if( leftSize < 1 )
			return new String(bSrc, 0, size);
		
		while( buf.size() < leftSize )
			buf.write( bPadStr, 0, bPadStr.length );

		return new String(buf.toByteArray(), 0, leftSize) + src;
	}
	
	public static String extractFileName( String fullPathName )
	{
		int		len, i;
		
		len = fullPathName.length()-1;
		for( i=len; i > -1; i-- ) {
			if( fullPathName.charAt(i) == '/' || fullPathName.charAt(i) == '\\')
				break;
		}
		
		return fullPathName.substring(i+1);
	}
	
	public static String encodeRequestParam( String value )
	{
		String colonCode = "%3A";
		String slashCode = "%2F";
		String equalCode = "%3D";
		String empCode = "%26";
		String percentCode = "%25";
		String plusCode = "%2B";
		String ascii13 = "";
		String ascii10 = "%0A";

		String str = value.replaceAll( "%", percentCode  );
		str = str.replaceAll( ":", colonCode );
		str = str.replaceAll( "/", slashCode );
		str = str.replaceAll( "&", empCode );
		str = str.replaceAll( "=", equalCode );
		str = str.replaceAll( "\\+", plusCode );
		str = str.replaceAll( " ", "+" );
		str = str.replaceAll( "\r", ascii13 );
		str = str.replaceAll( "\n", ascii10 );
		
		return str;
	}
	
	
	public static String escapeJavascript( String s )
	{
		String retValue = "";
		String value = null;
		char[] letter = s.toCharArray();
		for (int j = 0; j <letter.length; j++) {
			if( gvSpecialKey.indexOf( letter[j] ) > -1 )
				value = letter[j] + "";
			else if( letter[j] >= '0' && letter[j] <= '9' )
				value = letter[j] + "";
			else if( letter[j] >= 'A' && letter[j] <= 'Z' )
				value = letter[j] + "";
			else if( letter[j] >= 'a' && letter[j] <= 'z' )
				value = letter[j] + "";
			else {
				value = Integer.toHexString(letter[j]);
				value = value.toUpperCase();
				if( value.length() > 2 )
					value = "%u" + value;
				else
					value = "%" + value;
			}
			retValue = retValue + value;
		}
		return retValue;
	}
	
	public static String unescapeJavascript( String s )
	{
		String  letter = "";
		char	c;
		int i = 0;
		int len = s.length();

		while( i < len ) {
			c = s.charAt(i);
			if( c != '%' ) {
				letter += c;
				i++;
			}
			else {
				c = s.charAt(++i);
				if( c == 'u' ) {
					letter += (char)Integer.parseInt( s.substring(i+1, i+5), 16 );
					i += 5;
				}
				else {
					letter += (char)Integer.parseInt( s.substring(i,i+2), 16 );
					i += 2;
				}
			}
		}
		
		return letter;
	}
	
	public static String escapeString(String s)
	{
        s = s.replaceAll( "&", "&amp;");
        s = s.replaceAll( "<", "&lt;" );
		s = s.replaceAll( ">", "&gt;" );
		s = s.replaceAll( "'", "&#39;" );
		s = s.replaceAll( "\"", "&quot;" );
		s = s.replaceAll( "\\x28", "&#40;" );
		s = s.replaceAll( "\\x29", "&#41;" );
        return s;
	}
	
	public static String escapeForHtml(String s)
	{
        s = s.replaceAll( "&", "&amp;");
        s = s.replaceAll( "<", "&lt;" );
		s = s.replaceAll( ">", "&gt;" );
		s = s.replaceAll( "'", "&#39;" );
		s = s.replaceAll( "\"", "&quot;" );
		s = s.replaceAll( "\\x28", "&#40;" );
		s = s.replaceAll( "\\x29", "&#41;" );
		s = s.replaceAll( "\r\n", "<br>" );
		s = s.replaceAll( "\n", "<br>" );
		s = s.replaceAll( " ", "&nbsp;");
		s = s.replaceAll( "\t", "&nbsp;&nbsp;&nbsp;&nbsp;" );
        return s;
	}

	public static String escapeForInput( String s )
	{
		s = s.replaceAll( "&", "&amp;" );
        s = s.replaceAll( "<", "&lt;" );
		s = s.replaceAll( ">", "&gt;" );
		s = s.replaceAll( "'", "&#39;" );
		s = s.replaceAll( "\"", "&quot;" );
        return s;
	}
	
	public static String charsetEncode( String str, String charsetName ) throws UnsupportedEncodingException
	{
		if( str == null )
			return null;
		
		return charsetEncode(str, null, charsetName);
	}
	
	public static String charsetEncode( String str, String orgCharsetName, String newCharsetName ) throws UnsupportedEncodingException
	{
		if( str == null )
			return null;
		
		if( orgCharsetName == null || orgCharsetName.length() == 0 )
			return new String(str.getBytes(), newCharsetName);
		else
			return new String(str.getBytes(orgCharsetName), newCharsetName);
	}

	private static int convertCode62ToDigit(char sCode)
	{
		int i;
	
		for (i = 0; i < 62; i++)
			if (arr62[i] == sCode)
				return i;
	
		return -1;
	}
	
	private static int square62( int n )
	{
		int i;
	
		int num = 1;
		for(i=0; i<n; i++ )
			num *= 62;
		return num;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// 1A -> 1*62 + 10 = 72
	public static int convert62To10(String code62)
	{
		int i, k;
	
		if (code62 == null || code62.length() == 0)
			return -1;
	
		int nLen = code62.length();
		int nNumber = 0;
		for (i=0, k=nLen-1; i < nLen; i++, k--)
			nNumber += convertCode62ToDigit( code62.charAt(k) ) * square62(i);
		return nNumber;
	}
	
	//////////////////////////////////////////////////////////////////
	// 1A -> 0110
	public static int convert62ToLong10(String code62)
	{
		int i;
	
		String sLongCode = "";
		
		if (code62 == null || code62.length() == 0)
			return -1;
	
		int nLenShortCode = code62.length();
		for (i = 0; i < nLenShortCode; i++)
		{
			String nTempVal = String.valueOf(convertCode62ToDigit(code62.charAt(i)));
			if ( nTempVal == null || nTempVal.equals("-1") )
				return -1;
			nTempVal = "0" + nTempVal;
			sLongCode += nTempVal.substring( nTempVal.length()-2, nTempVal.length() );
		}
	
		return  Integer.parseInt( sLongCode );
	}
	
	////////////////////////////////////////////////////////////////
	// 10 -> 0A
	public static String convert10To62( int nNumber, int nLenCode)
	{
		int i;
		String sShortCode = "";
		String sCode = "";
		
		if (nNumber < 0 || nLenCode < 1)
			return "";
	
		int nQ = nNumber;
		for (i = 0; i < nLenCode; i++)
		{
			if ((sCode = convertDigitToCode(nQ % 62)).equals("") )
				return "";
			sShortCode = sCode + sShortCode;
			nQ = (nQ / 62);
		}
	
		return sShortCode;
	}
	
	public static String intToStr( int nNumber, int nLen )
	{
		String	res = String.valueOf( nNumber );
		int		lenSrc = res.length();
		for( int i = lenSrc; i < nLen; i++ )
			res = "0" + res;
		return res;
	}
	
	private static String convertDigitToCode( int nDigit)
	{
		if (nDigit < 0 || nDigit >= 62)
			return "";
	
		return arr62[nDigit] + "";
	}
	/**
	 * null String To blank
	 * @param  input String
	 * @param  input String이 null 일때 변환할 String
	 * @return input String가 null 이면 BLANK String, null 이 아니면 input String 일 반환한다.
	 */
	public static String replaceNull(String str,String replace) {
		return HoValidator.isEmpty(str)?replace:str;
	}
	
	public static String replaceNull(String str) {
		return HoValidator.isEmpty(str)?"":str;
	}

	public static String addSpace(String str, int amount) {
		return addSpace(str, amount, " ", 0);
	}
	public static String addZero(String str, int amount) {
		return addSpace(str, amount, "0", 1);
	}
	public static String addSharp(String str, int amount) {
		return addSpace(str, amount, "#", 0);
	}

	public static String addSpace(String strData, int amount, String strAdd, int isPositiom) {
		String rtnStr = strData;
		int slen = 0, blen = 0;
		char c;

		try {
			int iDif = 0;
			if(strData==null) strData="";

		    if(strData.getBytes("euc-kr").length>amount) {
				while (blen+1 < amount) {
					c = rtnStr.charAt(slen);
					blen++;
					slen++;
					if ( c  > 127 ) blen++;  //2-byte character..
				}
				rtnStr=rtnStr.substring(0,slen);
			}
		    if(rtnStr==null) {
		        iDif=amount;
		        rtnStr = "";
		    } else {
				byte[] bytes = rtnStr.getBytes();
				iDif = amount - bytes.length;
		    }
			if(isPositiom==1) {
				for(int j=0; j<iDif; j++)
				    rtnStr = strAdd+rtnStr;
			} else {
				for(int j=0; j<iDif; j++)
				    rtnStr = rtnStr+strAdd;
			}
		} catch (Exception e) {
			return null;
		}
		return rtnStr;
	}
    /**
	 * 한글 인코딩 한다.
	 * @param str 원본 String
	 * @return String 인코딩된 내용 
	 */
	public static String toKr(String str) {
		return toKr(str, "8859_1", "euc-kr" );
	}
	
	/**
	 * 한글 인코딩 한다.
	 * @param str 원본 String
	 * @return String 인코딩된 내용 
	 */
	public static String toKr(String str, String characterSet) {
		return toKr(str, "8859_1", characterSet);
	}
	
	/**
	 * 한글 인코딩 한다.
	 * @param str 원본 String
	 * @return String 인코딩된 내용 
	 */
	public static String toKr(String str, String bytesCharSet, String characterSet) {
		String rstr=null;
		try{
			rstr=(str==null)?"":new String(str.getBytes(bytesCharSet), characterSet );
		} catch(java.io.UnsupportedEncodingException e) {
			
		}
		return rstr;
	}


	public static String toJsonString(String str) {
		if(str==null) 
            return null; 

		StringBuffer sb=new StringBuffer(); 
		for(int i=0;i<str.length();i++){ 
            char ch=str.charAt(i); 
            switch(ch){ 
            case '\'': 
                sb.append("\\\'"); 
                break; 
            case '"': 
                sb.append("\\\""); 
                break; 
            case '\\': 
                    sb.append("\\\\"); 
                    break; 
            case '\b': 
                    sb.append("\\b"); 
                    break; 
            case '\f': 
                    sb.append("\\f"); 
                    break; 
            case '\n': 
                    sb.append("\\n"); 
                    break; 
            case '\r': 
                    sb.append("\\r"); 
                    break; 
            case '\t': 
                    sb.append("\\t"); 
                    break; 
            case '/': 
                    sb.append("\\/"); 
                    break; 
            default: 
                    if(ch>='\u0000' && ch<='\u001F'){ 
                            String ss=Integer.toHexString(ch); 
                            sb.append("\\u"); 
                            for(int k=0;k<4-ss.length();k++){ 
                                    sb.append('0'); 
                            } 
                            sb.append(ss.toUpperCase()); 
                    } 
                    else{ 
                            sb.append(ch); 
                    } 
            } 
		}//for 
		
		return sb.toString(); 
	}

	/**
	 * html select object selected
	 * @param comp1
	 * @param comp2
	 * @return
	 */
	public static String selected(String comp1, String comp2) {
		try {
			if (comp1.equals(comp2)) {
				return " selected";
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * html radio object checked
	 * @param comp1
	 * @param comp2
	 * @return
	 */
	public static String checked(String comp1, String comp2) {
		try {
			if (comp1.equals(comp2)) {
				return " checked";
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 파라메터로 넘어온 값을 지정한 길이만큼 잘라 낸다.
	 * @param str
	 * @param amount
	 * @return
	 */
	public static String cutString(String str, int amount) {
		return cutString(str,"euc-kr", amount,"");
	}
	
	/**
	 * 파라메터로 넘어온 값을 지정한 길이만큼 잘라 낸다.
	 */
	public static String cutString(String str, int amount, String tail) {
		return cutString(str,"euc-kr", amount, tail);
	}

	/**
	 * 한글 문자열 자르기
	 * @param in		한글문자열
	 * @param first		시작문자열
	 * @param end		종료문자열
	 * @return
	 */
	public static String cutString(String in, int first, int end){  
	    int strlen = 0; 
	    char c;
	    if(in == null) return null;
	    StringBuffer rtnStrBuf = new StringBuffer(); 
	    for(int j = 0; j < in.length(); j++) 
	    { 
	    	c = in.charAt(j); 
	    	if( (c  <  0xac00 || 0xd7a3 < c )&&( c  <  0xb0a1    || 0xc8fe    < c) ){ 
	    		strlen++; 
	    	}else   
	    		strlen+=2;  
	    	
	    	if(strlen>first){		    	
	    		rtnStrBuf.append(c); 	            	
	    	}	             
	    	
	    	if (strlen>end-1){ 
	    		break; 
	    	}                                
	    } 
	    return rtnStrBuf.toString(); 
	}
	
	public static String cutString(String str, String charSet, int amount, String tail) {
		if (str==null) return "";
		String rtnStr = str;
		int slen = 0, blen = 0;
		char c;

		try {
			if(rtnStr.getBytes(charSet).length>amount) {
				while (blen+1 < amount) {
					c = rtnStr.charAt(slen);
					blen++;
					slen++;
					if ( c  > 127 ) blen++;  //2-byte character..
				}
				rtnStr=rtnStr.substring(0,slen)+tail;
			}
		} catch (Exception e) {
			return null;
		}
		return rtnStr;
	}

	/**
	 * 보안상 문자열 치환
	 * SCRIPT,FORM,INPUT,HREF,OBJECT,IFRAME,'
	 * @param str
	 * @param pattern
	 * @param replace
	 * @param len
	 * @return
	 */
    public static String replaceBanString(String str) {
		String[] pattern1 = {"SCRIPT","FORM","INPUT","OBJECT","IFRAME","XSS","'"};
		String[] replace = {"*SCRIPT","*FORM","*INPUT","*OBJECT","*IFRAME","*XSS","&#039;"};
		String rtn = str;
		for(int i=0; i < pattern1.length;i++) {
			rtn = replaceIgnoreCase(str , pattern1[i] , replace[i]);
		}
		return rtn;
	}
    
	public static String replaceIgnoreCase(String str, String pattern , String replace) {
		return str.replaceAll("(?i)" + pattern, replace );
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
	
	public static String getStringForCLOB(java.sql.Clob clob) {
		return getStringForCLOB(clob, false);
    } 

	
	public static String getStringForCLOB(java.sql.Clob clob, boolean replaceString) {
        StringBuffer sbf = new StringBuffer();
        Reader br = null;
        char[] buf = new char[1024];
        int readcnt;

        try {
        	if( clob == null ) {
        		return sbf.toString();
        	}
            br = clob.getCharacterStream();

            while ((readcnt=br.read(buf,0,1024))!=-1) {
                sbf.append(buf,0,readcnt);
            }

        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
            if(br!=null)
                try {
                    br.close();
                } catch (IOException e) {
                	e.printStackTrace();
               }
        }
        if( !replaceString ) {
        	return sbf.toString();
        } else {
        	return replaceAbandonString(sbf.toString());
        }
    } 
	
	public static String replaceAbandonString(String str) {
		String original = str;
		
    	//String [] oriString = {"(?i)(?:< *?.*?script.*?>)((\n|\r|.)*?)(?:<\\/ *?.*?script *?>)"};
    	String [] oriString = {"(?i)script", "(?i)<.*?onerr", "(?i)<.*?onload", "(?i)<.*?onc", "(?i)embed", "(?i)iframe"};
    	String [] newString = {"<!-- SYSTEMWIZ_XSS -->", "<!-- SYSTEMWIZ_XSS -->", "<!-- SYSTEMWIZ_XSS -->", "<!-- SYSTEMWIZ_XSS -->", "<!-- SYSTEMWIZ_XSS -->", "<!-- SYSTEMWIZ_XSS -->"};
    	
       	for( int i=0 ;i<oriString.length ; i++ ) {
       		original = original.replaceAll(oriString[i], newString[i]);
    	}
       	
       	return original;		
	}
	
	public static boolean isAdandonString(String str) {

		if( HoUtil.replaceNull(str).equals("") ) {
			return false;
		}
		
    	//String [] adandonString = {"(?i)(?:< *?.*?script.*?>)((\n|\r|.)*?)(?:<\\/ *?.*?script *?>)"};
    	String [] adandonString = {"<!-- SYSTEMWIZ_XSS -->"};
    	
    	boolean isMatch = false;
    	Pattern p = null;
    	Matcher m = null;
    	for( int i=0 ;i<adandonString.length ; i++ ) {
    		p = Pattern.compile(adandonString[i]);

    		m = p.matcher(str);
    		
    		if( m.find() ) {
    			return true;
    		}
    	}
    		
		return isMatch;
	}

	/**
	 * _로 연결된 문자열을 대문자로 변경한다.
	 * ex) toCamel("aa_bb_cc") -> aaBbCc
	 * ex) toCamel("AA_BB_CC") -> aaBbCc
	 * @param value
	 * @return
	 */
	public static String toCamel(String value) {
		if( replaceNull(value).equals("") ) {
			return "";
		}
		String [] values = value.toLowerCase().split("_");
		
		StringBuffer sb = new StringBuffer();
		for( int i=0 ; i<values.length ; i++) {
			if( !values[i].equals("") ) {
				sb.append(values[i].substring(0,1).toUpperCase()).append(values[i].substring(1));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 대문자를 _로 변경한다.
	 * ex) fromCamel("aaBbCc") -> aa_bb_cc
	 * @param value
	 * @return
	 */
	public static String fromCamel(String value) {
		if( replaceNull(value).equals("") ) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();

		char [] chars = value.toCharArray();
		for( int i=0 ; i<chars.length ; i++ ) {
			if( i!=0) {
				if( chars[i] >= 'A' && chars[i] <= 'Z' ) {
					sb.append('_');
				}				
			}
			sb.append(Character.toLowerCase(chars[i]));
		}
		
		return sb.toString();
	}	
}

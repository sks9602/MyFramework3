package project.jun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.validator.GenericValidator;

public class HoValidator extends GenericValidator {
		
    /**
	 * 
	 */
	private static final long serialVersionUID = -1907694045517997956L;

	public static boolean isEmail(Object bean)
    {
        String value = null;
        if(isString(bean)) {
            value = (String)bean;
        } else {
        	value = bean.toString();
        }

        return (!GenericValidator.isBlankOrNull(value) && GenericValidator.isEmail(value));
    }


    public static boolean isString(Object o)
    {
        return o != null ? (java.lang.String.class).isInstance(o) : true;
    }

	public static boolean isNumber( String value )
	{
		char c = '0';
		int dotCnt = 0;
		int minusPos = 0;
		
		for( int i = 0; i < value.length(); i++ )
		{
			c = value.charAt(i);
			if( c == '-' )
				minusPos = i;
			else if( c == '.' )
				dotCnt++;
			else if( c < '0' || c > '9' )
				return false;
		}
		
		if( dotCnt > 1 )
			return false;
		else if( minusPos > 0 )
			return false;
		else
			return true;
	}
	
	
	public static boolean isDate(String date) {
		return isDate(date, "yyyyMMdd", false);
	}

	public static boolean isDate(String date, String format) {
		return isDate(date, format, false);
	}

	public static boolean isDate(String date,boolean strict) {
		return isDate(date, "yyyyMMdd", strict);
	}
	
	public static boolean isDummy( String str )
	{
		if(str == null || str.replaceAll("[ \r\n]*", "").equals("") )
			return true;
		return false;		
	}
	
	public static boolean isEmpty( String str )
	{
		if(GenericValidator.isBlankOrNull(str))
			return true;
		return false;		
	}

	public static boolean isNotEmpty(String str){ 
		return !isEmpty(str);			
	}
	
	/**
	 * String 이 null 인지 체크
	 * @param  input String
	 * @return input String가 null이면 true를 반환한다.
	 */
	public static boolean isNull(String str) {
		if(str == null)
			return true;
		return false;
	}
	/**
	 * String 이 null 아닌지 체크
	 * @param  input String
	 * @return input String가 null이 아니면 true를 반환한다.
	 */
	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	/**
     *<pre>
     * PROGRAM NO : HXA-001
     * DESC       :  
     *</pre>
     *
     * @param String code
     * @return boolean
     * @exception Exception
     */
	public static boolean isJuminCode(String code)
	{
		if(code == null || code.equals("")) {
			return false;
		}

		try {
			//code = code.replaceAll("-","").trim();
			code = code.trim();
	
			if (code.length() != 13) {
				return false;
			}
			
			int num = 0;
			int sum = 0;
	
			for (int i=0; i<12; i++) {
				num = (i+2)%10;
				if (i > 7) num += 2;
	
				sum += Integer.parseInt(String.valueOf(code.charAt(i))) * num;
			}
	
			int chkNum = (11 - sum%11)%10;
			
			if (chkNum != Integer.parseInt(String.valueOf(code.charAt(12)))) {
				return false;
			}
			
			
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
     *<pre>
     * PROGRAM NO : HXA-001
     * DESC       :   
     *</pre>
     *
     * @param String code
     * @return boolean
     * @exception Exception
     */
	public static boolean isForeignCode(String code)
	{
		if(code == null || code.equals("")) {
			return false;
		}

		try {
			//code = code.replaceAll("-","").trim();
			code = code.trim();
	
			if (code.length() != 13) {
				return false;
			}
		
			int sum = 0;
		    int odd = 0;
		    
		    int[] buf = new int[13];
			
		    for(int i=0; i<13; i++) { 
	            buf[i] = Integer.parseInt(String.valueOf(code.charAt(i)));   
	        } 
		    
		    odd = buf[7]*10 + buf[8];
		    
		    if(odd%2 != 0) {
		    	return  false; 
	        }
		    
		    if( (buf[11]!=6) && (buf[11]!=7) && (buf[11]!=8) && (buf[11]!=9) ) {
		    	return false;
		    }
		    
		    int[] multipliers = new int[]{2,3,4,5,6,7,8,9,2,3,4,5};
			
		    for(int i=0; i<12; i++) {
		    	sum += (buf[i] *= multipliers[i]);
		    }
		    
		    sum = 11-(sum%11);
		    
		    if (sum>=10)
		    	sum -= 10;
		    
		    sum += 2;
		    
		    if (sum>=10)
		    	sum -= 10;
		    
		    if (sum != buf[12])
		    	return false;
		    	
		
		    
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
     *<pre>
     * PROGRAM NO : HXA-002
     * DESC       :   
     *</pre>
     *
     * @param String code
     * @return boolean
     * @exception Exception
     */
	public static boolean isSaupCode(String code)
	{
		if(code == null || code.equals("")) {
			return false;
		}
		
		try {
			code = code.trim();
	
			if (code.length() != 10) {
				return false;
			}
			
		
			int num = 0;
			int sum = 0;
	
			for (int i=0; i<10; i++) {
				if (i != 8) {
					num = i%3;
	
					if (num == 0) 
						num = 1;
					else if (num == 1) 
						num = 3;
					else if (num == 2) 
						num = 7;
	
					num = (int)code.charAt(i) * num;
				}
				else {
					num = 5;
					num = (int)code.charAt(i) * num;
	
					if (num > 9) {
						num = (int)Math.floor(num/10) + num%10;
					}
				}
	
				sum += num;
			}
			
			if (sum%10 != 0) {
				return false;
			}
		
			
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
     *<pre>
     * PROGRAM NO : HXA-003
     * DESC       :  
     *</pre>
     *
     * @param String code
     * @return boolean
     * @exception Exception
     */
	public static boolean isBubinCode(String code)
	{
		if(code == null || code.equals("")) {
			return false;
		}

		try {
			code = code.trim();
	
			if (code.length() != 13) {
				return false;
			}
			
		
			int[] ChkDgt = {1,2,1,2,1,2,1,2,1,2,1,2};
			int[] nBupin = new int[13];
			int i;
			for( i=0; i < 13; i++ )
				nBupin[i] = code.charAt(i) - '0';

			int  lV1 = 0;
			int  nV2 = 0;
			int  nV3 = 0;

			for( i = 0 ; i < 12 ; i++) {
				lV1 = nBupin[i] * ChkDgt[i];
				if(lV1 >= 10)
					nV2 += lV1 % 10;
				else 
					nV2 += lV1;
			}
			 
			nV3 = nV2 % 10;
			 
			if( nV3 > 0 ) {
				nV3 = 10 - nV3;
			} else { 
				nV3 = 0;
			}

			if( nBupin[12] != nV3) { 
				return false;
			}
			
	
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}

	/*
	 * 문자열이 비교문자열에 존재하는지 확인
	 */
	public static boolean isIn(String str, String [] compareString) {
		
		for( int i=0 ; i<compareString.length ; i++ ) {
			if( str.equals(compareString[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public static void main(String [] args) {
		
		System.out.println( "20101231 -> " + HoValidator.isDate("20101231"));
		System.out.println( "20101234 -> " + HoValidator.isDate("20101234"));
		System.out.println( "20101231 -> " + HoValidator.isDate("20101231", Locale.getDefault()));
		System.out.println( "20101234 -> " + HoValidator.isDate("20101234", Locale.getDefault()));

		System.out.println( "2010년12월31일 -> " + HoValidator.isDate("2010년12월31일", Locale.getDefault()));
		System.out.println( "2010년12월34일 -> " + HoValidator.isDate("2010년12월34일", Locale.getDefault()));

		System.out.println( "2010/12/31 -> " + HoValidator.isDate("2010/12/31", Locale.getDefault()));
		System.out.println( "2010/12/34 -> " + HoValidator.isDate("2010/12/34", Locale.getDefault()));

		System.out.println( "2010/12/31 -> " + HoValidator.isDate("2010/12/31", "yyyy/MM/dd", true) );
		System.out.println( "2010/12/34 -> " + HoValidator.isDate("2010/12/34", "yyyy/MM/dd", true) );

		System.out.println( "2010/12/31 -> " + HoValidator.isDate("2010/12/31", "yyyy/MM/dd", false) );
		System.out.println( "2010/12/34 -> " + HoValidator.isDate("2010/12/34", "yyyy/MM/dd", false) );

		System.out.println( "2010/1/31 -> " + HoValidator.isDate("2010/1/31", "yyyy/MM/dd", true) );
		System.out.println( "2010/1/34 -> " + HoValidator.isDate("2010/1/34", "yyyy/MM/dd", true) );

		System.out.println( "2010/1/31 -> " + HoValidator.isDate("2010/1/31", "yyyy/MM/dd", false) );
		System.out.println( "2010/1/34 -> " + HoValidator.isDate("2010/1/34", "yyyy/MM/dd", false) );
		
		System.out.println( "2010년11월31일 -> " + HoValidator.isDate("2010년11월31일", "yyyy년MM월dd일", false));
		System.out.println( "2010년11월34일 -> " + HoValidator.isDate("2010년11월34일", "yyyy년MM월dd일", false));

		System.out.println( "-------------------------" );
		
		System.out.println("a \r\n a b \r \n ]->" + "a \r\n a b \r \n ]".replaceAll("[ \r\n]*", "") );

		System.out.println("a \r\n a b \r \n]->" + HoValidator.isDummy("a \r\n a b \r \n]") );
		System.out.println(" \r\n  \r \n->" + HoValidator.isDummy(" \r\n  \r \n") );
		
		
	}

}

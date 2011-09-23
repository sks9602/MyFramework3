package project.jun.was.spring;

/**
 * Cookie관리 class
 * 
 * 쿠키정보를 map으로 관리한다.
 * (단, 쿠키를 모두 대문자로 변환해서 map에 저장하며, "wiz_"로 시작한다면 이를 제거해서 map에 등록한다.)
 * 예, wiz_course_code는 COURSE_CODE로 관리한다. 
 */
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.jun.was.parameter.HoRequest;

public class HoCookie {

	private Map cookieMap = new java.util.HashMap();
	private final String COOKIE_PREFIX = "ys-";
    private String encodeCharType = "utf-8";
	private HttpServletResponse response;
	private HttpServletRequest request;
   
    /**
     * 생성자 : cookie정보를 map에 담는다.
     * @param request
     */
    public HoCookie(HoRequest wreq) {
		this.request = wreq.getRequest();
		this.response = wreq.getResponse();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0 ; i < cookies.length ; i++) {
                cookieMap.put(cookies[i].getName().toLowerCase(), cookies[i]);
            }
        }
    }

    public HoCookie(HoRequest wreq, String encodeCharType) {
        this(wreq);
        
        this.encodeCharType = encodeCharType;
    }

    /**
     * 실제로 cookie의 name을 대문자로 변경하여, map에 담는다.
     * 
     * @return Map
     */
    public Map getCookieMap() {
    	Map map = new java.util.HashMap();
    	
    	Set keySet = cookieMap.keySet();
    	Iterator it = keySet.iterator();
    	String key = null;
    	
    	while( it.hasNext()) {
    		key = (String) it.next();
    		
    		if( key.toUpperCase().startsWith("YS-")) {
    			map.put(key.toUpperCase().substring(3), getValue((Cookie)cookieMap.get(key)));
    		}
    	}

    	return map;
    }
    
    /**
     * Cookie를 생성한다.
     * @param name
     * @param value
     * @return
     * @throws IOException
     */
    public Cookie createCookie(String name, String value) throws IOException {
        return new Cookie(name.toLowerCase(), URLEncoder.encode(value, getEncodeCharType()));
    }

    
    public void addCookie(String name, String value) throws IOException {
    	response.addCookie(createCookie(name, URLEncoder.encode(value, getEncodeCharType())));
    }
    /**
     * Cookie를 생성한다.
     * @param name
     * @param value
     * @param path
     * @param maxAge
     * @return
     * @throws IOException
     */
    public Cookie createCookie(
            String name, String value, String path, int maxAge) throws IOException {
        Cookie cookie = new Cookie((COOKIE_PREFIX+name).toLowerCase(), 
                                URLEncoder.encode(value, getEncodeCharType()));
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public void addCookie(String name, String value, String path, int maxAge) throws IOException {
    	response.addCookie(createCookie(name, value, path, maxAge));
    }

    /**
     * Cookie를 생성한다.
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param maxAge
     * @return
     * @throws IOException
     */
    public Cookie createCookie(
            String name, String value,  
            String domain, String path, int maxAge) throws IOException {
        Cookie cookie = new Cookie((COOKIE_PREFIX+name).toLowerCase(), URLEncoder.encode(value, getEncodeCharType()));
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public void addCookie(String name, String value,  
            String domain, String path, int maxAge) throws IOException {
    	response.addCookie(createCookie( name, value, domain, path, maxAge));
    }

    /**
     * Cookie를 조회한다.
     * (주의 : 변경되기전 original cookie에 해당하는 정보임.)
     * @param name
     * @return
     */
    public Cookie getCookie(String name) {
        return (Cookie)cookieMap.get((COOKIE_PREFIX+name).toLowerCase()); 
    }
    
    public String getValue(Cookie cookie) {
    	return getValue(cookie, "");
    }

    public String getValue(Cookie cookie, String defaultValue) {
    	if (cookie == null) return "";
    	try {
    		String value = URLDecoder.decode(cookie.getValue(), this.getEncodeCharType());
    		if( value.startsWith("s:") || value.startsWith("b:") ) {
    			return value.equals("") ? defaultValue : URLDecoder.decode(value.substring(2), this.getEncodeCharType());
    		} else {
    			return value.equals("") ? defaultValue : value.substring(value.indexOf("s"));
    		}
    		// return URLDecoder.decode(cookie.getValue(), this.getEncodeCharType());
    	} catch(Exception e) {
    		return defaultValue;
    	}
    }

    /**
     *      
     * (주의 : 변경되기전 original cookie에 해당하는 정보임.)
     * @param name
     * @return
     * @throws IOException
     */
    public String getValue(String name) throws IOException {
    	return getValue(name, "");
    }

    public String getValue(String name, String defaultValue) throws IOException {
        Cookie cookie = (Cookie)cookieMap.get((COOKIE_PREFIX+name).toLowerCase());
    	if (cookie == null) return "";
    	try {
    		String value = URLDecoder.decode(cookie.getValue(), this.getEncodeCharType());
    		if( value.startsWith("s:") || value.startsWith("b:") ) {
    			return value.equals("") ? defaultValue : URLDecoder.decode(value.substring(2), this.getEncodeCharType());
    		} else {
    			return value.equals("") ? defaultValue : value.substring(value.indexOf("s"));
    		}
    		// return URLDecoder.decode(cookie.getValue(), this.getEncodeCharType());
    	} catch(Exception e) {
    		return defaultValue;
    	}
    }

    public boolean exists(String name) {
        return cookieMap.get((COOKIE_PREFIX+name).toLowerCase()) != null;
    }
    
    public void setEncodeCharType(String encodeCharType) {
    	this.encodeCharType = encodeCharType;
    }
    
    public String getEncodeCharType() {
    	return this.encodeCharType;
    }
    
	public void addCookie(Cookie cookie) {
		response.addCookie(cookie);
	}

}

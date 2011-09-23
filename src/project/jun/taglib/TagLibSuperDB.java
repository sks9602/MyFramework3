package project.jun.taglib;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

import project.jun.dao.HoDaoImpl;
import project.jun.util.HoUtil;
import project.jun.was.config.HoConfig;
import project.jun.was.parameter.HoParameter;
import project.jun.was.parameter.HoRequest;
import project.jun.was.spring.HoController;
import project.jun.was.spring.HoModel;


public abstract class TagLibSuperDB extends BodyTagSupport
{

	/**
	 */
	public final static int PAGE    = 1;
	public final static int REQUEST = 2;
	public final static int SESSION = 3;
	public final static int CONTEXT = 4;

	private static final long serialVersionUID = 4100432080526343154L;
	private HoRequest hoRequest = null;
	private HoConfig  hoConfig  = null;
	private WebApplicationContext factory = null;
	private TagLibUtil   tlUtil = null;
	
	public int doStartTag() throws JspTagException 
	{
		int rtn = EVAL_BODY_INCLUDE;
		try 
		{
			JspWriter out = pageContext.getOut();
			try 
			{
				   ServletContext ctx = pageContext.getServletContext();
					try {
						String attr = FrameworkServlet.SERVLET_CONTEXT_PREFIX + "action"; 

						factory = WebApplicationContextUtils.getWebApplicationContext(ctx, attr);
						
						tlUtil = new TagLibUtil();
						
						tlUtil.setHoModel(super.pageContext.getRequest());
						setHoRequest();
						setHoConfig(factory);
						
					} catch (Exception e) {
						
					}
					
					rtn = doStart( factory, out );
			}
		    catch(Exception e ) 
			{
		    	e.printStackTrace();
				throw new JspTagException( e.getMessage() );
			} finally {

			}
			
			return rtn;
		}
		catch(Exception e ) 
		{
			e.printStackTrace();
			throw new JspTagException( e.getMessage() );
		}
	}

	public int doEndTag() throws JspTagException
	{
        
		int rtn = EVAL_PAGE;

    	JspWriter out = pageContext.getOut();

		try 
		{
			rtn = doEnd( factory, out );
	    	return rtn;
		}
	    catch(Exception e ) 
		{
	    	e.printStackTrace();
			throw new JspTagException( e.getMessage() );
		} finally {

		}
    }

	public TagLibUtil getTagLibUtil() {
		return tlUtil;	
	}
	
	public Locale getLocale() {
		return Locale.getDefault();
	}
		
	public HoModel getHoModel() {
		return tlUtil.getHoModel();
	}
	
	
	public HoParameter getHoParameter() {
		return (HoParameter) getHoModel().get(HoController.HO_PARAMETER);
	}
	
	
	private void setHoRequest(){
		if(hoRequest==null) {
			hoRequest = new HoRequest(this.pageContext);
		}
	}

	public HoRequest getHoRequest() {
		return this.hoRequest;
	}
	
	private void setHoConfig(WebApplicationContext factory){
		if(hoConfig==null) {
			hoConfig = (HoConfig)factory.getBean("config");
		}
	}

	public HoConfig getHoConfig() {
		return this.hoConfig;
	}
	
	public HoDaoImpl getHoDaoImpl(WebApplicationContext ctx) {
		return tlUtil.getHoDaoImpl(ctx);
	}

	public HoDaoImpl getHoDaoImpl(WebApplicationContext ctx, String dataSource) {
		return tlUtil.getHoDaoImpl(ctx, dataSource);
	}	
	
	public boolean isFirstIndex(String indexName) { 
		Integer index =  (Integer) this.pageContext.getAttribute("TAGLIB_INDEX_"+indexName, PAGE);
		
		if( index == null ) {
			return true;
		} else {
			int i = index.intValue();
			
			if( i==0) {
				return true;
			} else {
				return false;
			}
		}	
	}
	
	public int increaseIndex(String indexName, int increase) {
		Integer index =  (Integer) getAttribute("TAGLIB_INDEX_"+indexName);
		
		if( index == null ) {
			index = new Integer(0);
		} else {
			int i = index.intValue();
			i+=increase;
			index = new Integer(i);
		}
		setAttribute("TAGLIB_INDEX_"+indexName, index);

		return index.intValue();
	}
	
	
	public int increaseIndex(String indexName) {
		return increaseIndex(indexName, 1);
	}
	
	public int getCurrentIndex(String indexName) {
		Integer index =  (Integer) getAttribute("TAGLIB_INDEX_"+indexName);
		
		if( index == null ) {
			index = new Integer(0);
		}
		setAttribute("TAGLIB_INDEX_"+indexName, index);
		
		return index.intValue();
	}
	
	public void initIndex(String indexName ) {
		this.pageContext.removeAttribute("TAGLIB_INDEX_"+indexName, PAGE);
		
	}
	
	protected void addNewLine(StringBuffer sb, String text) {
		sb.append(text).append("\r\n");
	}
	
	protected int getSearchMaxTdCnt() {
		return Integer.parseInt((String)((java.util.Map)getHoConfig().getConfigMap()).get("SEARCH_MAX_TD_CNT"));
	}

	
	public String getRequestUri(HoRequest hoRequest) {
		org.springframework.web.util.UrlPathHelper urlPathHelper = new org.springframework.web.util.UrlPathHelper(); 
		String url = urlPathHelper.getOriginatingRequestUri(hoRequest.getRequest());

		if(url.indexOf('?') > 0) {
			return url.substring(0, url.indexOf('?'));
		} else {
			return url; 
		}
		
	}
	
	public void setAttribute(String name, Object value ) {
		this.pageContext.setAttribute(name, value, PAGE);
	}
	
	public Object getAttribute(String name) {
		return this.pageContext.getAttribute(name, PAGE);
	}


	/**
	 * 검색조건의 title을 MessageSource에서 조회한다.
	 * @param ctx
	 * @return
	 */
	public String getMessageTitle (String resource, WebApplicationContext ctx, String code, String arg, String text) {
		try {	 
			if( !resource.equals("")) {
				return ctx.getMessage(resource, argToArray(arg), getLocale() );
			} else {
				return getMessageTitle (ctx, code, arg, text);
			}
		} catch(Exception e) {
			return text;
		}
		
	}

	/**
	 * 검색조건의 title을 MessageSource에서 조회한다.
	 * @param ctx
	 * @return
	 */
	public String getMessageTitle (WebApplicationContext ctx, String code, String arg, String text) {
		try {	
			if( !code.equals("")) {
				if( code.indexOf(",") !=-1 ) {
					String [] titles = code.split(",");
					
					String title = "";
					for( int i=0 ; i<titles.length ; i++) {
						title += ctx.getMessage(titles[i], argToArray(arg, i), getLocale() );
					}
					
					return title;
				} else {
					return ctx.getMessage(code, argToArray(arg), getLocale() );
				}
			} else {
				return text;
			}
		} catch(Exception e) {
			return text;
		}
		
	}
	
	private String [] getArgs(String arg) {
		return arg.split("}.*,.*{");
	}

	/**
	 *  검색조건의 title의 변수를 배열로 만든다.
	 * @return
	 */
	private Object [] argToArray(String arg) {
		Object [] array = null;
		if( !HoUtil.replaceNull(arg).equals("") ) {
			array = arg.split(",");
		}
		return array;
	}

	/**
	 *  검색조건의 title의 변수를 배열로 만든다.
	 * @return
	 */
	private Object [] argToArray(String arg, int i) {
		Object [] array = null;
		if( arg != null ) {
			if( i<getArgs(arg).length ) {
				array = getArgs(arg)[i].replace('{', ' ').replace('}', ' ').split(",");
			}
		}
		return array;
	}
	

	abstract public int doStart( WebApplicationContext factory, JspWriter out ) throws Exception;
	abstract public int doEnd( WebApplicationContext factory, JspWriter out ) throws Exception;
}

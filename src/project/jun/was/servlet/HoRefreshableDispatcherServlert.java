package project.jun.was.servlet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;

public class HoRefreshableDispatcherServlert extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4281324751760023018L;
	
	public static final String SERVLET_ATTRIBUTE_NAME = HoRefreshableDispatcherServlert.class.getName()+ "_SERVLET_ATTRIB";

	protected HandlerExecutionChain getHandler(HttpServletRequest request, boolean cache) throws Exception {
		RequestContextHolder.getRequestAttributes().setAttribute(getClass().getName(), this, RequestAttributes.SCOPE_REQUEST);

		return super.getHandler(request);
	}

}

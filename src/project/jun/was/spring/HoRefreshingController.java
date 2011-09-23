package project.jun.was.spring;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import project.jun.was.servlet.HoRefreshableDispatcherServlert;


public class HoRefreshingController implements Controller, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        refresh(response, request, configurableApplicationContext);
        return null;
    }

    private void refresh(HttpServletResponse response, HttpServletRequest request,
            ConfigurableApplicationContext context) throws IOException, ServletException {
        long start = System.currentTimeMillis();

        log(response, "refreshing context");

        refreshParent(response, request, context, start);

        refreshServlet();

        log(response, "refreshed servlet in " + (System.currentTimeMillis() - start) + " ms.");
    }

    private void refreshParent(HttpServletResponse response, HttpServletRequest request,
            ConfigurableApplicationContext context, long start) throws IOException {
        ConfigurableApplicationContext parent = (ConfigurableApplicationContext) context
                .getParent();

        if (parent != null && "true".equals(request.getParameter("reloadParent"))) {
            parent.refresh();
            log(response, "refreshed parent in " + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private void refreshServlet() throws ServletException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String key = HoRefreshableDispatcherServlert.SERVLET_ATTRIBUTE_NAME;

        DispatcherServlet servlet = (DispatcherServlet) requestAttributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST);

        servlet.init();

    }

    private void log(HttpServletResponse response, String string) throws IOException {
        log(string, response.getWriter());
    }

    private void log(String string, PrintWriter writer) {
        logger.info(string);
        writer.write(string);
        writer.write("");
        writer.flush();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}

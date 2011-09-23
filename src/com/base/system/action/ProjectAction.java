package com.base.system.action;import org.springframework.web.servlet.ModelAndView;import com.base.system.delegate.ProjectDelegate;import project.jun.was.parameter.HoRequest;import project.jun.was.spring.HoController;public abstract class ProjectAction extends HoController {	/**	 * 실제 biz logic실행전 초기에 실행될 method	 * @param request	 * @param response	 * @throws Exception	 */    public void initExecute( HoRequest   hoRequest ) throws Exception { }     /**     * 실제 biz logic실행전 실행될 method     * @param actionFlag     * @throws Exception     */    public void beforeExecute( String actionFlag, ModelAndView mav) throws Exception {     	ProjectDelegate delegate = (ProjectDelegate) super.getHoDelegate();    	delegate.getHoParameter().setSessionObject("COMPANY_CD", "0001");    }    /**     * 실제 biz logic실행후 실행될 method     * @param actionFlag     * @throws Exception     */     public void afterExecute( String actionFlag, ModelAndView mav) throws Exception { }        /**     * exception발생시 실행될 method     * @param actionFlag     * @throws Exception     */    public void exceptionExecute( String actionFlag, ModelAndView mav) throws Exception { }    /**     * 항상 실행될 method     * @param actionFlag     * @throws Exception     */    public void finallyExecute( String actionFlag, ModelAndView mav) throws Exception { }}
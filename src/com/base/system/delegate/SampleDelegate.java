package com.base.system.delegate;import java.sql.SQLException;import javax.naming.NamingException;import project.jun.dao.HoDaoImpl;import project.jun.dao.parameter.HoQueryParameterHandler;import project.jun.dao.parameter.HoQueryParameterMap;import project.jun.dao.result.HoList;import project.jun.dao.result.HoMap;import project.jun.delegate.HoPageNavigation;import project.jun.was.result.exception.HoException;import project.jun.was.result.message.HoMessage;public class SampleDelegate extends ProjectDelegate {	/**	 * 메뉴 목록을 조회한다.	 * @throws NamingException	 * @throws SQLException	 * @throws HoException	 */	public void init() throws NamingException, SQLException, HoException {				HoDaoImpl dao = super.getHoDaoImpl();				HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);		 		HoQueryParameterMap value = hqph.getForSearch();						HoList list = dao.queryForList("Sample.selectList", value);		param.getHoRequest().setSessionObject("PAGE_ROW_CNT", "50");						model.put("list", list);	}		public void list() throws Exception {				HoDaoImpl dao = super.getHoDaoImpl();				HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);		 		HoQueryParameterMap value = hqph.getForSearch();						HoMap listCnt = dao.queryForInfo("Sample.selectListCnt", value);				long totCnt = listCnt.getLong("CNT");				long pageNo     = hqph.getPageNo();		long pageRowCnt = hqph.getPageRowCnt();        // 페이지 네비게이션        HoPageNavigation nav = new HoPageNavigation(param);        nav.setNavigationInfo(totCnt, pageRowCnt, pageNo);        // 페이지범위 설정!!        long beginRowNum = nav.getBeginRowNum();        long endRowNum   = nav.getEndRowNum();                value.put("BEGINROWNUM", beginRowNum);        value.put("ENDROWNUM", endRowNum);		HoList list = dao.queryForList("Sample.selectList", value);     		model.put("TOTAL_CNT", totCnt);		model.put("JSON_DATAS", list);	}		public HoMessage create() throws Exception {				logger.info("  2");		return new HoMessage();	}}
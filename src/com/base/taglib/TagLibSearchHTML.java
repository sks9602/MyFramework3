package com.base.taglib;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.dao.HoDaoImpl;
import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.dao.result.HoList;
import project.jun.taglib.TagLibSuperDB;
import project.jun.util.HoUtil;


public class TagLibSearchHTML extends TagLibSuperDB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type	  = "";   // select_공통코드, checkbox_공통코드, radio_공통코드, selectGroup_공통코드
	private String firstGbn  = ""; // all, choice, none, auto
	private String titleCode = "";
	private String titleText = "";
	private String description = "";
	private String notNull	  = "";
	private String sortColumn = "";
	private String sortDir	= "";
	private String codeColumn = "CODE";
	private String nameColumn = "CODE_NAME";
	private String titleArg = "";
	private String disabled = "";
	private String onchange = "";
	private String append = "";
	private String colspan = "";
	private String rowspan = "";
	private String detaultValue = "";
	private String whereColumn = "";
	private String whereCondition = "";
	
	public int doStart(WebApplicationContext ctx, JspWriter out) throws Exception {
		return EVAL_BODY_INCLUDE;

	}
	
	public int doEnd(WebApplicationContext ctx, JspWriter out) throws Exception {
		 
		StringBuffer buf = new StringBuffer();

		// 공통코드로 combo검색조건을 를 만들 경우.
		if( type.toLowerCase().startsWith("select_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);
			
			printCodeSelect(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		} 
		// 공통코드 checkbox
		else if( type.toLowerCase().startsWith("checkbox_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);
			
			
			printCodeCheckbox(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}	
		// 공통코드 radio
		else if( type.toLowerCase().startsWith("radio_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);
			
			printCodeRadio(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}	
		// select다중선택
		else if( type.toLowerCase().startsWith("selectMulti_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);
			
			printCodeSelectMulti(ctx, out, buf);
	
			// Tag Library 종료
			tagLibEnd(buf);
		}		
		
		// 공통코드로 검색조건을 를 만들 경우.
		else if( type.toLowerCase().startsWith("selectGroup_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);
			
			printCodeSelectGroup(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		} 
		// 
		else if( type.toLowerCase().equals("popup_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printPopup(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}
		// 년
		else if( type.toLowerCase().equals("year_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printYearMonth(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		} 
		// 년/월
		else if( type.toLowerCase().equals("ym_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printYearMonth(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		} 
		// 년/월/주
		else if( type.toLowerCase().equals("ymw_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printYearMonthWeek(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}
		// 날짜( From ~ To )검색조건을 만들 경우
		else if( type.toLowerCase().equals("date_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printDateFromTo(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}  
		// 일반 Text 검색조건을 만들 경우
		else if( type.toLowerCase().equals("text_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printTextField(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}  
		// 숫자를 검색조건을 만들 경우
		else if( type.toLowerCase().equals("number_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printNumberField(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}  
		// 기타  검색 type이 설정되지 않았을경우.
		else {
			// Tag Library 시작.
			tagLibStart(buf);

			printNotSpecified(ctx, out, buf);
			
			// Tag Library 종료
			tagLibEnd(buf);
		}
		out.print( buf.toString() );

		return EVAL_PAGE;

	}

	/**
	 * Tag Library시작 
	 * @param buf
	 * @throws Exception
	 */
	public void tagLibStart(StringBuffer buf ) throws Exception
	{
		buf.append("<!-- Tag Library [ type : "+ type + ( type.toLowerCase().startsWith("code") ? (",  groupCode : " + type.toLowerCase().replaceAll("(?i)code_", "").toUpperCase()) +"(* name refer V_CODE.USEDEF1)" : "" ) + "]");
		if(!description.equals("")) {
			buf.append(description);
		}
		buf.append("-->\r\n");
		
		// buf.append("						<tr>\r\n");
	}

	/**
	 * Front용 Tag Library시작 
	 * @param buf
	 * @throws Exception
	 */
	public void frontTagLibStart(StringBuffer buf ) throws Exception
	{
		buf.append("<!-- Tag Library [ type : "+ type + ( type.toLowerCase().startsWith("code") ? (",  groupCode : " + type.toLowerCase().replaceAll("(?i)code_", "")) +"(* name refer V_CODE.USEDEF1)" : "" ) + "]");
		if(!description.equals("")) {
			buf.append(description);
		}
		buf.append("-->\r\n");
		
		// buf.append("						<tr>\r\n");
	}

	/**
	 * Tag Library종료 
	 * @param buf
	 * @throws Exception
	 */
	public void tagLibEnd(StringBuffer buf ) throws Exception
	{
		// buf.append("						</tr>\r\n");
	}


	/**
	 * 검색조건의 title을 MessageSource에서 조회한다.
	 * @param ctx
	 * @return
	 */
	public String getMessageTitle (String titleResource, WebApplicationContext ctx) {
		try {	 
			if( !titleResource.equals("")) {
				return ctx.getMessage(titleResource, argToArray(), getLocale() );
			} else {
				return getMessageTitle (ctx);
			}
		} catch(Exception e) {
			return titleText;
		}
		
	}

	/**
	 * 검색조건의 title을 MessageSource에서 조회한다.
	 * @param ctx
	 * @return
	 */
	public String getMessageTitle (WebApplicationContext ctx) {
		try {	
			if( !titleCode.equals("")) {
				if( titleCode.indexOf(",") !=-1 ) {
					String [] titles = titleCode.split(",");
					
					String title = "";
					for( int i=0 ; i<titles.length ; i++) {
						title += ctx.getMessage(titles[i], argToArray(i), getLocale() );
					}
					
					return title;
				} else {
					return ctx.getMessage(titleCode, argToArray(), getLocale() );
				}
			} else {
				return titleText;
			}
		} catch(Exception e) {
			return titleText;
		}
		
	}

	/**
	 * 다중객체명으로 변경할 것 인지 여부
	 * s_xxx_es으로 바뀐다.
	 * @return
	 */
	private boolean useMultiName() {
		if( !firstGbn.equals("choice") && !firstGbn.equals("none") ) {
			return true;
		} else {
			return false;
		}
		      
	}

	
	/**
	 * Cookie의 값을 사용할 지 여부.
	 * 
	 */
	private boolean isMaintainPrevious() {
		String       searchMaintain = null;
		try {
			searchMaintain = getHoRequest().getSessionString("SEARCH_MAINTAIN");
			
			if(!searchMaintain.equals("Y")) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			return true;
		}
	}

	
	
	/**
	 *  Admin용 콤보 박스를 만든다.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void printCodeSelect(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		String selectedVal = "";
		String title = "";
		String name  = "";
		
		HoList list = getList(ctx, type.toLowerCase() );
		

		buf.append("							<td class=\"search_t_td\" ");

		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} else {
			buf.append(" >");
		}
		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		try {
			if( getMessageTitle(ctx).equals("")) {
				if( list.size() > 0 ) {
					title = list.getString(0,"CODE_NAME");
					name = list.getString(0,"USEDEF1").toLowerCase();
				} 
			} else {
				title = getMessageTitle(ctx);
				name = type.toLowerCase().replaceAll("(?i)code_", "");
			}
		} catch(Exception e)  {
			title = getMessageTitle(ctx);
			name = type.toLowerCase().replaceAll("(?i)code_", "");
		}
		
		// 전체일경우( 선택 또는 없음이 아닌 경우)
		if( useMultiName() ) {
			name = "s_" + name + "_es";
		} else if( !name.toLowerCase().equals("pagerowcnt")) {
			name = "s_" + name;
		}

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		/*
		buf.append("<input type=\"checkbox\" name=\"title_"+name+"\" id=\"taglib_title_"+name+"\" value=\"Y\" onchange=\"fs_SaveToCookie(this);\" ");
		if( cookie.getValue("title_"+name).equals("Y"))	{
			buf.append(" checked=\"checked\" ");
		}
		buf.append(" />");
		*/
		buf.append("							</td>\r\n");
		
		buf.append("							<td class=\"search_td\" ");

		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} 
		if( !getColspan().equals("") ) {
			buf.append(" colspan=\""+getColspan()+"\" ");
		} 
		buf.append(" >\r\n");
		
		
		if( isMaintainPrevious() ) {
			// TODO
			selectedVal = getHoParameter().get(name );
		} else {
			selectedVal = getHoParameter().get(name);
		}
		
		buf.append("								<select name=\"" + name + "\" id=\"taglib_select_"+name+"\" "); // onchange=\"fs_SaveToCookie(this);
		
		if( onchange.length() > 0 ) {
			buf.append("onchange=\"" + onchange +"\" " );
		}

		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=1 ; list.size() > 0 && i<list.size(); i++) {
			buf.append("									");
			buf.append("<option value=\"" + list.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedVal, list.getString(i, getCodeColumn()))+" >" + list.getString(i, getNameColumn()) + "</option>\r\n");
		}

		buf.append("								</select>\r\n");

		buf.append("							</td>\r\n");
	}

	/**
	 *  Admin용 콤보 박스를 만든다.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void printCodeSelectMulti(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		String name = "";
		String selectedVal = "";
		String title = "";

		HoList list = getList(ctx, type.toLowerCase() );
		

		buf.append("							<td class=\"search_t_td\" ");

		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} else {
			buf.append(" >");
		}
		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		try {
			if( getMessageTitle(ctx).equals("")) {
				if( list.size() > 0 ) {
					title = list.getString(0,"CODE_NAME");
					name = list.getString(0,"USEDEF1").toLowerCase();
				} 
			} else {
				title = getMessageTitle(ctx);
				name = type.toLowerCase().replaceAll("(?i)code_", "");
			}
		} catch(Exception e)  {
			title = getMessageTitle(ctx);
			name = type.toLowerCase().replaceAll("(?i)code_", "");
		}
		
		// 전체일경우( 선택 또는 없음이 아닌 경우)
		if( useMultiName() ) {
			name = "s_" + name + "_es";
		} else if( !name.toLowerCase().equals("pagerowcnt")) {
			name = "s_" + name;
		}

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		/*
		buf.append("<input type=\"checkbox\" name=\"title_"+name+"\" id=\"taglib_title_"+name+"\" value=\"Y\" onchange=\"fs_SaveToCookie(this);\" ");
		if( cookie.getValue("title_"+name).equals("Y"))	{
			buf.append(" checked=\"checked\" ");
		}
		buf.append(" />");
		*/
		buf.append("							</td>\r\n");
		
		buf.append("							<td class=\"search_td\" ");

		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} 
		if( !getColspan().equals("") ) {
			buf.append(" colspan=\""+getColspan()+"\" ");
		} 
		buf.append(" >\r\n");
		
		
		//if( isMaintainPrevious() ) {
		//	selectedVal = getHoParameter().get(name, cookie.getValue(name) );
		//} else {
			selectedVal = getHoParameter().get(name);
		//}
		
		buf.append("								<select name=\"" + name + "\" id=\"taglib_select_"+name+"\" "); // onchange=\"fs_SaveToCookie(this);
		
		if( onchange.length() > 0 ) {
			buf.append("onchange=\"" + onchange +"\" " );
		}

		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=1 ; list.size() > 0 && i<list.size(); i++) {
			buf.append("									");
			buf.append("<option value=\"" + list.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedVal, list.getString(i, getCodeColumn()))+" >" + list.getString(i, getNameColumn()) + "</option>\r\n");
		}

		buf.append("								</select>\r\n");

		buf.append("							</td>\r\n");
	}

	
	
	/**
	 *  Admin용 콤보 박스를 만든다.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void printCodeSelectGroup(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		String name = "";
		String selectedVal = "";
		String title = "";

		HoList list = getList(ctx, type.toLowerCase() );

		
		buf.append("							<td class=\"search_t_td\" ");

		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} else {
			buf.append(" >");
		}
		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		try {
			if( getMessageTitle(ctx).equals("")) {
				if( list.size() > 0 ) {
					title = list.getString(0,"CODE_NAME");
					name = list.getString(0,"USEDEF1").toLowerCase();
				} 
			} else {
				title = getMessageTitle(ctx);
				name = type.toLowerCase().replaceAll("(?i)code_", "");
			}
		} catch(Exception e)  {
			title = getMessageTitle(ctx);
			name = type.toLowerCase().replaceAll("(?i)code_", "");
		}
		
		// 전체일경우( 선택 또는 없음이 아닌 경우)
		if( useMultiName() ) {
			name = "s_" + name + "_es";
		} else if( !name.toLowerCase().equals("pagerowcnt")) {
			name = "s_" + name;
		}

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		/*
		buf.append("<input type=\"checkbox\" name=\"title_"+name+"\" id=\"taglib_title_"+name+"\" value=\"Y\" onchange=\"fs_SaveToCookie(this);\" ");
		if( cookie.getValue("title_"+name).equals("Y"))	{
			buf.append(" checked=\"checked\" ");
		}
		buf.append(" />");
		*/
		buf.append("							</td>\r\n");
		
		buf.append("							<td class=\"search_td\" ");

		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} 
		if( !getColspan().equals("") ) {
			buf.append(" colspan=\""+getColspan()+"\" ");
		} 
		buf.append(" >\r\n");
		
		
		//if( isMaintainPrevious() ) {
		//	selectedVal = getHoParameter().get(name, cookie.getValue(name) );
		//} else {
			selectedVal = getHoParameter().get(name);
		//}
		
		buf.append("								<select name=\"" + name + "\" id=\"taglib_select_"+name+"\" "); // onchange=\"fs_SaveToCookie(this);
		
		if( onchange.length() > 0 ) {
			buf.append("onchange=\"" + onchange +"\" " );
		}

		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=1 ; list.size() > 0 && i<list.size(); i++) {
			buf.append("									");
			buf.append("<option value=\"" + list.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedVal, list.getString(i, getCodeColumn()))+" >" + list.getString(i, getNameColumn()) + "</option>\r\n");
		}

		buf.append("								</select>\r\n");

		buf.append("							</td>\r\n");
	}	
	/**
	 *  공통코드를 checkbox로 만든다.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void printCodeCheckbox(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		String name = "";
		String title = "";

		HoList list = getList(ctx, type.toLowerCase() );
		
		
		buf.append("							<td class=\"search_t_td\">");
		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		if( getMessageTitle(ctx).equals("")) {
			if( list.size() > 0 ) {
				title = list.getString(0,"CODE_NAME");
				name = list.getString(0,"USEDEF1").toLowerCase();
			} 
		} else {
			title = getMessageTitle(ctx);
			name = type.toLowerCase().replaceAll("(?i)code_", "");
		}
		
		// 전체일경우( 선택 또는 없음이 아닌 경우)
		if( useMultiName() ) {
			name = "s_" + name + "_es";
		} else {
			name = "s_" + name;
		}

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		/*
		buf.append("<input type=\"checkbox\" name=\"title_"+name+"\" id=\"taglib_title_"+name+"\" value=\"Y\" onchange=\"fs_SaveToCookie(this);\" ");
		if( cookie.getValue("title_"+name).equals("Y"))	{
			buf.append(" checked=\"checked\" ");
		}
		buf.append(" />");
		*/
		
		if( useMultiName() ) {
			if( list.size() > 0 && list.getString(1, getCodeColumn()).equals("") ) {
				buf.append("<input type=\"checkbox\" name=\"" + name + "_first\" id=\"taglib_checkbox_"+name+"_first\"  checkAllName=\"" + name + "\"  " );

				buf.append(" value=\"Y\" ");
				
				if( !list.getString(0, getCodeColumn()).equals("")) {
					buf.append(" checked=\"checked\" ");
				}
				
				if( append.length() > 0 )
					buf.append( append );

				buf.append("/>");

			}
		}
		
		buf.append("							</td>\r\n");
		
		buf.append("							<td class=\"search_td\">\r\n");
		
		buf.append("								<span style=\"width:100%;\">\r\n");
		

		for( int i=1 ; list.size() > 0 && i<list.size(); i++) {
			// 첫번째 값이  전체  일경우..
			if( useMultiName() && list.getString(i, getCodeColumn()).equals("") ) {
				continue;
			} 
			// 첫번째 값이  전체가 아닌 경우..
			else {
				buf.append("									<span style=\"vertical-align:middle;\">");
				buf.append("<input type=\"checkbox\" name=\"" + name + "\" id=\"taglib_checkbox_"+name+"_"+list.getString(i, getCodeColumn()) +"\" " );

				buf.append(" value=\""+list.getString(i, getCodeColumn()) + "\" ");
				
				if( !list.getString(i, getCodeColumn()).equals(""))	{
					buf.append(" checked=\"checked\" ");
				}

				if( append.length() > 0 )
					buf.append( append );

				buf.append("/> "+ list.getString(i, getNameColumn()).replaceAll("-", "") + " </span>\r\n");
			}
		}
		buf.append("								</span>\r\n");
		buf.append("							</td>\r\n");
	}

	/**
	 *  공통코드를 radio로 만든다.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void printCodeRadio(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		String name = "";
		String title = "";

		HoList list = getList(ctx, type.toLowerCase() );
		
		
		buf.append("							<td class=\"search_t_td\">");
		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		if( getMessageTitle(ctx).equals("")) {
			if( list.size() > 0 ) {
				title = list.getString(0,"CODE_NAME");
				name = list.getString(0,"USEDEF1").toLowerCase();
			} 
		} else {
			title = getMessageTitle(ctx);
			name = type.toLowerCase().replaceAll("(?i)code_", "");
		}
		
		// 전체일경우( 선택 또는 없음이 아닌 경우)
		if( useMultiName() ) {
			name = "s_" + name + "_es";
		} else {
			name = "s_" + name;
		}

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		/*
		buf.append("<input type=\"checkbox\" name=\"title_"+name+"\" id=\"taglib_title_"+name+"\" value=\"Y\" onchange=\"fs_SaveToCookie(this);\" ");
		if( cookie.getValue("title_"+name).equals("Y"))	{
			buf.append(" checked=\"checked\" ");
		}
		buf.append(" />");
		*/
		
		if( useMultiName() ) {
			if( list.size() > 0 && list.getString(1, getCodeColumn()).equals("") ) {
				buf.append("<input type=\"checkbox\" name=\"" + name + "_first\" id=\"taglib_checkbox_"+name+"_first\"  checkAllName=\"" + name + "\"  " );

				buf.append(" value=\"Y\" ");
				
				if( !list.getString(0, getCodeColumn()).equals("")) {
					buf.append(" checked=\"checked\" ");
				}
				
				if( append.length() > 0 )
					buf.append( append );

				buf.append("/>");

			}
		}
		
		buf.append("							</td>\r\n");
		
		buf.append("							<td class=\"search_td\">\r\n");
		
		buf.append("								<span style=\"width:100%;\">\r\n");
		

		for( int i=1 ; list.size() > 0 && i<list.size(); i++) {
			// 첫번째 값이  전체  일경우..
			if( useMultiName() && list.getString(i, getCodeColumn()).equals("") ) {
				continue;
			} 
			// 첫번째 값이  전체가 아닌 경우..
			else {
				buf.append("									<span style=\"vertical-align:middle;\">");
				buf.append("<input type=\"checkbox\" name=\"" + name + "\" id=\"taglib_checkbox_"+name+"_"+list.getString(i, getCodeColumn()) +"\" " );

				buf.append(" value=\""+list.getString(i, getCodeColumn()) + "\" ");
				
				if( !list.getString(i, getCodeColumn()).equals(""))	{
					buf.append(" checked=\"checked\" ");
				}

				if( append.length() > 0 )
					buf.append( append );

				buf.append("/> "+ list.getString(i, getNameColumn()).replaceAll("-", "") + " </span>\r\n");
			}
		}
		buf.append("								</span>\r\n");
		buf.append("							</td>\r\n");
	}
	
	/**
	 * 
	 * @param ctx
	 * @param out
	 * @param buf
	 * @throws Exception
	 */
	public void printYearMonthWeek(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception {

		String title = "";
		String selectedYear = "";
		String selectedMonth = "";
		String selectedWeek = "";

		String yearName = "s_year";
		String monthName = "s_month";
		String weekName = "s_week";
		
		selectedYear = getHoParameter().get(yearName);
		selectedMonth = getHoParameter().get(monthName);
		selectedWeek = getHoParameter().get(weekName);			

		buf.append("							<td class=\"search_t_td\">");

		title = getMessageTitle("HRD_PAGE_1096", ctx);
		if( title.equals("")) {
			title = "강의장일정";
		}

		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		String yearTitle = getMessageTitle("HRD_PAGE_0217", ctx);
		if( yearTitle.equals("")) {
			yearTitle = "년도";
		} 
		
		
		String monTitle = getMessageTitle("HRD_PAGE_0545", ctx);
		if( monTitle.equals("")) {
			monTitle = "월";
		} 
		
		String weekTitle = getMessageTitle("HRD_PAGE_1097", ctx);
		if( monTitle.equals("")) {
			monTitle = "주차";
		} 

		// 검색조건 타이틀 출력.
		buf.append(title);
		buf.append("							</td>\r\n");
		buf.append("								");
		buf.append("							<td class=\"search_td\">\r\n");
		
		// 첫번째 값 없도록..
		this.firstGbn = "none";
		
		HoList yearList = getList(ctx, "eduYearNew".toLowerCase() );
		HoList monthList = getList(ctx, "month".toLowerCase());
		HoList weekList = getList(ctx, "weekList".toLowerCase());

		// 년도
		buf.append("								");
		buf.append("<select name=\""+yearName+"\" id=\"taglib_select_"+yearName+"\" onchange=\"fs_SaveToCookie(this);fs_SetWeek(this, this.form."+monthName+");\" " );

		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=0 ; yearList.size() > 0 &&  i<yearList.size() ; i++ ) {
			buf.append("									");
			buf.append("<option value=\"" + yearList.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedYear, yearList.getString(i, getCodeColumn()))+" >" + yearList.getString(i, getNameColumn()) + "</option>\r\n");
		}
		buf.append("								</select>"+yearTitle + "\r\n");

		// 월
		buf.append("								<select name=\"" + monthName + "\" id=\"taglib_select_"+monthName+"\" onchange=\"fs_SaveToCookie(this);fs_SetWeek(this.form."+yearName+", this);\" " );
		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=0 ; monthList.size() > 0 && i<monthList.size(); i++) {
			buf.append("									");
			buf.append("<option value=\"" + monthList.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedMonth, monthList.getString(i, getCodeColumn()))+" >" + monthList.getString(i, getNameColumn()) + "</option>\r\n");
		}

		buf.append("								</select>"+monTitle + "\r\n");

		
		// 주간
		buf.append("								");
		buf.append("<select name=\"" + weekName + "\" id=\"taglib_select_" + weekName + "\" onchange=\"fs_SaveToCookie(this);\" " );

		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=0 ; weekList.size() > 0 &&  i<weekList.size() ; i++ ) {
			buf.append("									");
			buf.append("<option value=\"" + weekList.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedWeek, weekList.getString(i, getCodeColumn()))+" >" + weekList.getString(i, getNameColumn()) + "</option>\r\n");
		}
		buf.append("								</select>"+weekTitle+"\r\n");

		buf.append("							</td>\r\n");
	}

	/**
	 * 회원명 검색조건을 만들 경우
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void getSearchPopup(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception {

		String title = "";

		String selectedCode = "";
		String selectedName = "";

		String searchCode = "";
		String searchName = "";
		String searchGbn = "";
		String taglib = "";
		// 회원명
		if( type.toLowerCase().equals("member".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0888", ctx);
			if( title.equals("")) {
				title = "회원명";
			}

			searchCode = "s_member_no";
			searchName = "s_member_name";
			searchGbn = "s_"+type.toLowerCase();
			taglib = type.toLowerCase();
		} 
		// 사업장 + 회원명
		else if( type.toLowerCase().equals("memberSite".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0888", ctx);
			if( title.equals("")) {
				title = "회원명";
			}
			searchCode = "s_member_no";
			searchName = "s_member_name";
			searchGbn = "s_member";
			taglib = "member";
		} 
		// 운영자명 검색조건을 만들 경우
		else if( type.toLowerCase().equals("memberOper".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0538", ctx);
			if( title.equals("")) {
				title = "운영자명";
			}
			searchCode = "s_member_oper_no";
			searchName = "s_member_oper_name";
			searchGbn = "s_member_oper";
			taglib = "user_oper" ;
		} 
		// 영업대표명 검색조건을 만들 경우
		else if( type.toLowerCase().equals("memberSales".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0504", ctx);
			if( title.equals("")) {
				title = "영업대표";
			}
			searchCode = "s_member_sales_no";
			searchName = "s_member_sales_name";
			searchGbn = "s_member_sales";
			taglib = "user_sales" ;
		} 
		// 강사명
		else if( type.toLowerCase().equals("lecturer".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0013", ctx);
			if( title.equals("")) {
				title = "강사명";
			}
			searchCode = "s_lecturer_no";
			searchName = "s_lecturer_name";
			searchGbn = "s_"+type.toLowerCase();
			taglib = type.toLowerCase();
		}

		//if(isMaintainPrevious()) {
		//	selectedCode = getHoParameter().get(searchCode, cookie.getValue(searchCode) );
		//	selectedName = getHoParameter().get(searchName, cookie.getValue(searchName) );
		//} else {
			selectedCode = getHoParameter().get(searchCode );
			selectedName = getHoParameter().get(searchName );
		//}

		buf.append("							<td class=\"search_t_td\" ");
		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} 
		buf.append(">");
	
		// 검색조건 타이틀 출력.
		buf.append(title+"\r\n");

		// buf.append("								<span id=\"id_"+type.toLowerCase()+"_searched_btn\" style=\"cursor:hand;border: 1 solid #0000FF;height:6px;margin:0;padding:0;\" class=\"span_btn\" value=\"+\" ext:qtip=\""+title+" 검색이력을 조회합니다.\" onClick=\"fs_ShowSearchedList('id_"+type.toLowerCase()+"_searched_btn', '"+type.toUpperCase()+"');\">+</span>\r\n");
		// buf.append("								<span id=\"id_"+type.toLowerCase()+"_delete_btn\" style=\"cursor:hand;border: 1 solid #0000FF \" class=\"span_btn\" onClick=\"fs_DeleteSearchedList('id_"+type.toLowerCase()+"_searched_btn','"+type.toUpperCase()+"');\" ext:qtip=\""+title+" 검색이력을 삭제합니다.\">삭제</span>\r\n");
		buf.append("							</td>\r\n");
		buf.append("							<td class=\"search_td\" ");
		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} 
		if( !getColspan().equals("") ) {
			buf.append(" colspan=\""+getColspan()+"\" ");
		} 
		buf.append(">");

		buf.append("								<table><tr>");
		
		// 과정입과자일 경우 사업장 combobox만들기.
		if(type.toLowerCase().equals("memberSite".toLowerCase())) {
			this.firstGbn  = "none";
			HoList list = getList(ctx, "cps_biCd");

			String site    = "s_bi_cd";

				
			String selectedSite = "";
			//if(isMaintainPrevious()) {
			//	selectedSite = getHoParameter().get(site, cookie.getValue(site) );
			//} else {
				selectedSite = getHoParameter().get(site );
			//}

			buf.append("									");
			buf.append("<td><select name=\""+site+"\" id=\"taglib_hidden_member_"+site+"\" >\r\n"); //onchange=\"fs_SaveToCookie(this);\" 
			buf.append("<option value=\"\" "+ HoUtil.selected("", selectedSite)+"> -사업장-</option>\r\n");
			for( int i=0 ; i<list.size() ; i++ ) {
				buf.append("										");
				buf.append("<option value=\""+ list.getString(i, "CODE")+"\" "+ HoUtil.selected(list.getString(i, "CODE"), selectedSite)+">"+list.getString(i, "CODE_NAME") + "</option>\r\n");
			}
			buf.append("									");
			buf.append("</select></td>\r\n");
		}
		
		buf.append("									");
		buf.append( "<td><input type=\"text\" name=\""+searchCode+"\" size=\"10\" id=\"taglib_hidden_"+type.toLowerCase()+"_search_no"+getHoParameter().get("p_action_flag") + "\"  value=\"" + selectedCode + "\" /></td>\r\n"); //onchange=\"fs_SaveToCookie(this);\"
		buf.append("									");
		buf.append( "<td><span id=\"taglib_"+taglib+"_search_name_"+getHoParameter().get("p_action_flag") +"\" name=\""+searchGbn+"\" value=\""+selectedName+"\" style=\"width:250px\"></span></td>" );
		buf.append("								");
		buf.append( "</tr></table>\r\n");
		buf.append("							</td>\r\n");

	}
	
	
	
	/**
	 * 교육년도 + 월 검색조건을 만들 경우
	 * @param ctx
	 * @param list
	 * @param out
	 * @param buf
	 * @throws Exception
	 */
	public void printYearMonth(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		String selectedYear = "";
		String selectedMonth = "";
		String title = "";

		buf.append("							<td class=\"search_t_td\">");
		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		String yearTitle = getMessageTitle("HRD_PAGE_0217", ctx);
		if( yearTitle.equals("")) {
			yearTitle = "년도";
		} 
		
		
		String monTitle = getMessageTitle("HRD_PAGE_0545", ctx);
		if( monTitle.equals("")) {
			monTitle = "월";
		} 
		
		title = yearTitle + "&nbsp;/&nbsp;" + monTitle;
		
		String yearName = "s_year";
		String monthName = "s_month";
		

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		buf.append("</td>\r\n");
		
		buf.append("							<td class=\"search_td\">\r\n");

		selectedYear = getHoParameter().get(yearName);
		selectedMonth = getHoParameter().get(monthName);
		
		buf.append("								<select name=\"" + yearName + "\" id=\"taglib_select_"+yearName+"\" onchange=\"fs_SaveToCookie(this);\" " );

		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		HoList yearList = getList(ctx, "eduYear".toLowerCase());
		HoList monthList = getList(ctx, "month".toLowerCase());


		for( int i=0 ; yearList.size() > 0 && i<yearList.size(); i++) {
			buf.append("									");
			buf.append("<option value=\"" + yearList.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedYear, yearList.getString(i, getCodeColumn()))+" >" + yearList.getString(i, getNameColumn()) + "</option>\r\n");
		}

		buf.append("								</select>"+yearTitle + "\r\n");

		
		buf.append("								<select name=\"" + monthName + "\" id=\"taglib_select_"+monthName+"\" onchange=\"fs_SaveToCookie(this);\" " );
		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		if( disabled.length() > 0 )
			buf.append( "disabled=\"" + disabled + "\" ");
		if( append.length() > 0 )
			buf.append( append );

		buf.append(">\r\n");

		for( int i=0 ; monthList.size() > 0 && i<monthList.size(); i++) {
			buf.append("									");
			buf.append("<option value=\"" + monthList.getString(i, getCodeColumn()) + "\" "+ HoUtil.selected(selectedMonth, monthList.getString(i, getCodeColumn()))+" >" + monthList.getString(i, getNameColumn()) + "</option>\r\n");
		}

		buf.append("								</select>"+monTitle + "\r\n");

		buf.append("							</td>\r\n");
	}	
	

	/**
	 * 숫자만 입력 가능한 검색조건 만들기.
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void printNumberField(WebApplicationContext ctx, JspWriter out, StringBuffer buf  ) throws Exception
	{		
		String title = "";

		String size = "10";
		String moneyText = "";

		String startValue = "";
		String endValue = "";
		
		String startName = null;
		String endName = null;

		// 강사료
		if( type.toLowerCase().equals("lecturerFee".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1074", ctx);
			if( title.equals("")) {
				title = "강사료";
			} 
			startName = "s_lecturer_start_fee";
			endName = "s_lecturer_end_fee";
			moneyText = " isMoney=\"Y\" ";
		} 
		//과제수
		else if( type.toLowerCase().equals("taskCnt".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1079", ctx);
			if( title.equals("")) {
				title = "과제수";
			} 
			startName = "s_task_start_cnt";
			endName = "s_task_end_cnt";
		} 
		//시험수
		else if( type.toLowerCase().equals("examCnt".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1093", ctx);
			if( title.equals("")) {
				title = "시험수";
			} 
			startName = "s_exam_start_cnt";
			endName = "s_exam_end_cnt";
		} 
		//진도율
		else if( type.toLowerCase().equals("progress".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0710", ctx);
			if( title.equals("")) {
				title = "진도율";
			} 
			startName = "s_progress_start";
			endName = "s_progress_end";
		} 
		//토론수
		else if( type.toLowerCase().equals("discussCnt".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1094", ctx);
			if( title.equals("")) {
				title = "토론수";
			} 
			startName = "s_discuss_start_cnt";
			endName = "s_discuss_end_cnt";
		} 
		//평가비율합계점수
		else if( type.toLowerCase().equals("evalRateSum".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1095", ctx);
			if( title.equals("")) {
				title = "평가비율합계점수";
			} 
			startName = "s_eval_start_sum";
			endName = "s_eval_end_sum";
		} 
		
		startValue =  getHoParameter().get(startName );
		endValue = getHoParameter().get(endName );
		
		buf.append("							<td class=\"search_t_td\">");

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		buf.append("</td>\r\n");
		buf.append("							<td class=\"search_td\">\r\n");
		buf.append("								");
		buf.append( "<span style=\"width:500px;\">\r\n");
		buf.append("									");
		buf.append( "<input type=\"text\" name=\""+startName+"\" size=\""+size+"\" id=\"taglib_number_"+startName + "_" + getHoParameter().get("p_action_flag") + "\" isNum=\"Y\" "+moneyText+" compareObjId=\"taglib_number_"+endName + "_" + getHoParameter().get("p_action_flag") +"\"  "+moneyText+" onchange=\"fs_SaveToCookie(this);\" value=\"" + startValue + "\" >&nbsp~\r\n");
		buf.append("									");
		buf.append( "&nbsp;<input type=\"text\" name=\""+endName+"\" size=\""+size+"\" id=\"taglib_number_"+endName + "_" + getHoParameter().get("p_action_flag") + "\" isNum=\"Y\" "+moneyText+" compareObjId=\"taglib_number_"+startName + "_" + getHoParameter().get("p_action_flag") +"\"  "+moneyText+" onchange=\"fs_SaveToCookie(this);\" value=\"" + endValue + "\" >\r\n");
		buf.append( "</span>");
		buf.append("							</td>\r\n");

	}
	
	
	/**
	 * 문자 입력 가능한 검색조건 만들기.
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void printTextField(WebApplicationContext ctx, JspWriter out, StringBuffer buf  ) throws Exception
	{
		
		String title = "";

		String selectedCode = "";

		String fieldName = null;
		String size = "50";
		String allowChars = "";
		// 강의계획서
		if( type.toLowerCase().equals("lecturerPlan".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1075", ctx);
			if( title.equals("")) {
				title = "강의계획서명";
			} 
			fieldName = "s_lecplan_name";
		} 
		//강의장명
		else if( type.toLowerCase().equals("classroom".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0020", ctx);
			if( title.equals("")) {
				title = "강의장명";
			} 
			fieldName = "s_classroom_name";
		} 
		//과제명
		else if( type.toLowerCase().equals("task".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1078", ctx);
			if( title.equals("")) {
				title = "과제명";
			} 
			fieldName = "s_task_name";
		} 
		//교육행사명
		else if( type.toLowerCase().equals("event".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1080", ctx);
			if( title.equals("")) {
				title = "교육행사명";
			} 
			fieldName = "s_event_name";
		} 
		//메일수신자명
		else if( type.toLowerCase().equals("mailReceiver".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1082", ctx);
			if( title.equals("")) {
				title = "메일수신자명";
			} 
			fieldName = "s_mail_receiver_name";
		} 
		//메일제목
		else if( type.toLowerCase().equals("mail".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1083", ctx);
			if( title.equals("")) {
				title = "메일제목";
			} 
			fieldName = "s_mail_title";
		} 
		//메일주소
		else if( type.toLowerCase().equals("mailAddress".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1084", ctx);
			if( title.equals("")) {
				title = "메일주소";
			} 
			fieldName = "s_mail_address";
		} 
		//메일템플릿설명
		else if( type.toLowerCase().equals("mailDescription".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1085", ctx);
			if( title.equals("")) {
				title = "메일템플릿설명";
			} 
			fieldName = "s_mail_description";
		} 
		//설문지명
		else if( type.toLowerCase().equals("question".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1086", ctx);
			if( title.equals("")) {
				title = "설문지명";
			} 
			fieldName = "s_question_name";
		} 
		//시험지명
		else if( type.toLowerCase().equals("exam".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1087", ctx);
			if( title.equals("")) {
				title = "시험지명";
			} 
			fieldName = "s_exam_name";
		} 
		//용어명
		else if( type.toLowerCase().equals("dictionary".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1088", ctx);
			if( title.equals("")) {
				title = "용어명";
			} 
			fieldName = "s_dictionary_name";
		} 
		//전문분야
		else if( type.toLowerCase().equals("major".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0623", ctx);
			if( title.equals("")) {
				title = "전문분야";
			} 
			fieldName = "s_major_name";
		} 
		//지문제목
		else if( type.toLowerCase().equals("composit".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1089", ctx);
			if( title.equals("")) {
				title = "지문제목";
			} 
			fieldName = "s_composit_name";
		} 
		//키워드
		else if( type.toLowerCase().equals("keyWord".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1091", ctx);
			if( title.equals("")) {
				title = "키워드";
			} 
			fieldName = "s_key_word_name";
		} 
		//토론명
		else if( type.toLowerCase().equals("dicuss".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1092", ctx);
			if( title.equals("")) {
				title = "토론명";
			} 
			fieldName = "s_discuss_name";
		} 
		//토론명
		else if( type.toLowerCase().equals("bizNo".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0355", ctx);
			if( title.equals("")) {
				title = "사업자번호";
			} 
			fieldName = "s_biz_no";
			allowChars = "1234567890-";
		} 
		//게시판명
		else if( type.toLowerCase().equals("boardName".toLowerCase())) {
			title = getMessageTitle("BASE_PAGE_0011", ctx);
			if( title.equals("")) {
				title = "게시판명";
			} 
			fieldName = "s_board_name";
		} 
		// 기타 Text검색조건
		else if( type.toLowerCase().startsWith("text_".toLowerCase())) {
			title = getMessageTitle("", ctx);
			fieldName = "s_"+type.toLowerCase().substring(5).toLowerCase()+"_name";
		} 
		
		
		selectedCode = getHoParameter().get(fieldName );

		
		buf.append("							<td class=\"search_t_td\">");

		// 검색조건 타이틀 출력.
		buf.append(title);
		
		buf.append("</td>\r\n");
		buf.append("							<td class=\"search_td\">\r\n");
		buf.append("								");
		buf.append( "<span>\r\n");
		buf.append("									");
		buf.append( "<input type=\"text\" name=\""+fieldName+"\" size=\""+size+"\" ");
		// 입력 가능한 문자 등록..
		if( !allowChars.equals("")) {
			buf.append( " allowChars=\""+allowChars+"\"");			
		}
		buf.append( " id=\"taglib_course_search_sq_"+fieldName + "_" + getHoParameter().get("p_action_flag") + "\" onchange=\"fs_SaveToCookie(this);\" value=\"" + selectedCode + "\" >\r\n");
		buf.append("								");
		buf.append( "</span>\r\n");
		buf.append("							</td>\r\n");

	}

	
	/**
	 * 팝업 조건을 만든다.
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void printPopup(WebApplicationContext ctx, JspWriter out, StringBuffer buf  ) throws Exception
	{
		String title = "";

		String selectedCode = "";
		String selectedName = "";

		String courseCode = "s_course_code";
		String courseName = "s_course_name";

		selectedCode = getHoParameter().get(courseCode );
		selectedName = getHoParameter().get(courseName );
		
		buf.append("							<td class=\"search_t_td\">");

		title = getMessageTitle("HRD_PAGE_0110", ctx);
		if( title.equals("")) {
			title = "과정명";
		} 

		// 검색조건 타이틀 출력.
		buf.append(title+"\r\n");

		buf.append("								<span id=\"id_course_searched_btn\" style=\"cursor:hand;border: 1 solid #0000FF;height:6px;margin:0;padding:0;\" class=\"span_btn\"  value=\"+\" ext:qtip=\"과정 검색이력을 조회합니다.\" onClick=\"fs_ShowSearchedList('id_course_searched_btn','COURSE');\">+</span>\r\n");
		buf.append("								<span id=\"id_course_delete_btn\" style=\"cursor:hand;border: 1 solid #0000FF \" class=\"span_btn\" onClick=\"fs_DeleteSearchedList('id_course_searched_btn','COURSE');\" ext:qtip=\"과정 검색이력을 삭제합니다.\">삭제</span>\r\n");

		
		buf.append("							</td>\r\n");
		buf.append("							<td class=\"search_td\">\r\n");
		buf.append("								\r\n");
		buf.append( "<span style=\"width:500px;\">");
		buf.append("									\r\n");
		buf.append( "<input type=\"text\" name=\""+courseCode+"\" size=\"10\" id=\"taglib_course_search_"+courseCode + "_" + getHoParameter().get("p_action_flag") + "\" onchange=\"fs_SaveToCookie(this);\" value=\"" + selectedCode + "\" >");
		buf.append("									\r\n");
		buf.append( "<span id=\"taglib_course_search_name_"+getHoParameter().get("p_action_flag") + "\" name='s_course' style=\"width:100px;\" " );
		if( notNull.length() > 0 )
			buf.append( "notNull=\"" + notNull + "\" " );
		buf.append( "value=\"" + selectedName + "\" ></span>\r\n");
		buf.append("								\r\n");
		buf.append( "</span>");
		buf.append("							</td>\r\n");

	}
	
	/**
	 * 검색 시작일자 ~ 종료일자 달력 컴포넌트 만든다.
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void printDateFromTo(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception {

		String title = "";

		String startYmd = "";
		String endYmd = "";

		String startName = null;
		String endName = null;

		//계약시작일자
		if( type.toLowerCase().equals("contractStartYmd".toLowerCase())) {
			startName = "s_contract_from_start_dt";
			endName = "s_contract_from_end_dt";
			title = getMessageTitle("HRD_PAGE_1076", ctx);
			if( title.equals("")) {
				title = "계약시작일자";
			}
		} 
		//계약종료일자
		else if( type.toLowerCase().equals("contractEndYmd".toLowerCase())) {
			startName = "s_contract_to_start_dt";
			endName = "s_contract_to_end_dt";
			title = getMessageTitle("HRD_PAGE_1077", ctx);
			if( title.equals("")) {
				title = "계약종료일자";
			}
		} 
		//로그인일시
		else if( type.toLowerCase().equals("loginYmd".toLowerCase())) {
			startName = "s_login_start_ymd";
			endName = "s_login_end_ymd";
			title = getMessageTitle("HRD_PAGE_1081", ctx);
			if( title.equals("")) {
				title = "로그인일시";
			}
		} 
		//메일발송일 
		else if( type.toLowerCase().equals("mailSendYmd".toLowerCase())) {
			startName = "s_mail_send_start_ymd";
			endName = "s_mail_send_end_ymd";
			title = getMessageTitle("HRD_PAGE_1105", ctx);
			if( title.equals("")) {
				title = "메일발송일";
			}
		} 
		// 기타 시작 ~ 종료 검색조건
		else if( type.toLowerCase().startsWith("date_".toLowerCase())) {
			startName = "s_"+type.toLowerCase().substring(5).toLowerCase()+"_start_ymd";
			endName = "s_"+type.toLowerCase().substring(5).toLowerCase()+"_end_ymd";
			title = getMessageTitle("", ctx);
		} 

		//if(isMaintainPrevious()) {
			//startYmd = getHoParameter().get(startName,  cookie.getValue(startName));
			//endYmd   = getHoParameter().get(endName,  cookie.getValue(endName) );
		//} else {
			startYmd = getHoParameter().get(startName);
			endYmd   = getHoParameter().get(endName);
		//}
		
		buf.append("							<td class=\"search_t_td\" ");
		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} else {
			buf.append(" >");
		}

		// 검색조건 타이틀 출력.
		buf.append(title);
		buf.append("							</td>\r\n");
		buf.append("							<td class=\"search_td\" ");
		if( !getRowspan().equals("") ) {
			buf.append(" rowspan=\""+getRowspan()+"\" ");
		} 
		if( !getColspan().equals("") ) {
			buf.append(" colspan=\""+getColspan()+"\" ");
		} else {
			buf.append(" >\r\n");
		}
		buf.append("								");
		buf.append( "<span style=\"width:120px;\"><table><tr>\r\n"); //vertical-align:top;
		buf.append("									");
		buf.append( "<td><span style=\"width:50px;\"  name=\""+startName+"\" id=\"taglib_date_"+ startName + "_"+ getHoParameter().get("p_action_flag") +"\"");
		buf.append( "notNull=\""+getNotNull()+"\" ");
		buf.append( "value=\""+ startYmd +"\" compareObjId=\"taglib_date_"+ endName + "_"+ getHoParameter().get("p_action_flag") +"_object\"");
		buf.append( " ></span>\r\n");
		buf.append("									");
		buf.append("<input type=\"hidden\" name=\""+startName+"_init\" value=\""+startYmd+"\"/>");
		buf.append("									");
		buf.append( "</td><td><span style=\"width:10px;vertical-align:middle;padding-top:5px;height:20px;\">&nbsp;~</span></td>\r\n");
		buf.append("									");
		buf.append( "<td><span style=\"width:50px;\"  name=\""+endName+"\" id=\"taglib_date_"+ endName + "_"+ getHoParameter().get("p_action_flag") +"\"");
		buf.append( "notNull=\""+getNotNull()+"\" ");
		buf.append( "value=\""+ endYmd +"\" compareObjId=\"taglib_date_"+ startName + "_"+ getHoParameter().get("p_action_flag") +"_object\"");
		buf.append( " ></span></td>\r\n");
		buf.append( "								");
		buf.append("									");
		buf.append("<input type=\"hidden\" name=\""+endName+"_init\" value=\""+endYmd+"\"/>");
		buf.append( "</tr></table></span>\r\n");
		buf.append("							</td>\r\n");

	}

	/**
	 * 검색 type이 선택되지않은 경우.
	 * @param ctx
	 * @param out
	 * @param buf
	 * @throws Exception
	 */
	public void printNotSpecified(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception {

		String title = "<font color=\"red\">검색 type 없음</font>";
				
		buf.append("							<td class=\"search_t_td\">");

		// 검색조건 타이틀 출력.
		buf.append(title);
		buf.append("							</td>\r\n");
		buf.append("							<td class=\"search_td\">\r\n");
		buf.append("								 TagLibSearch.java를 참고하세요.\r\n");
		buf.append("							</td>\r\n");

	}


	
	/**
	 * 사용할 코드목록을 db에서 조회한다.
	 * @param factory
	 * @return
	 */
	public HoList getList(WebApplicationContext factory, String gbn) {
		HoDaoImpl dao = getHoDaoImpl(factory);

		String groupCode = "";
		HoQueryParameterMap value = new HoQueryParameterMap();

		// 첫번째가.. 선택일 경우.
		if( firstGbn.equals("choice") ) {
			value.put("CODE_GBN", "W1902");
		}
		else if( firstGbn.equals("none") ) {
			// 처리없음
		}
		// 전체일 경우.
		else {
			value.put("CODE_GBN", "W1901");
		}
		
		// 이전 검색조건 사용일 경우 
		if(isMaintainPrevious()) {
			//TODO 이전검색조건을 쿼리에서 가져올수 있도록 수정.
			
		}
		HoList list = null;

		try {
			
			if( isCommonCode() ) { // 공통코드용
				groupCode =  gerGroupCode();
				
				value.put("GROUP_CODE", groupCode );
				value.put("SORT_COLUMN", sortColumn);
				value.put("SORT_DIR", sortDir);
				
				if( !whereColumn.equals("")) {
					value.put("WHERE_COLUMN", whereColumn);
				}
				if( !whereCondition.equals("")) {
					value.put("WHERE_CONDITION", whereCondition);
				}
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect."+groupCode.toUpperCase());
				if( list==null ) {
					list = dao.queryForList("CodeDAO.selectSearchCodeListCombo", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect."+groupCode.toUpperCase(), list);
				}
			} 
			// 과정중분류 코드조회..
			else if( gbn.equals("classify1")) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect.CLASSIFY_CODE1");
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectClassifyCode1", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect.CLASSIFY_CODE1", list);
				}
			}  
			// 과정소분류 코드조회..
			else if( gbn.equals("classify2")) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect.CLASSIFY_CODE2");

				String classify_code1 = "";
				try {
					classify_code1 = getHoParameter().get("s_classify_code1");
				} catch (Exception e) {
				}

				value.add("CLASSIFY_CODE1", classify_code1);	
				
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectClassifyCode2", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect.CLASSIFY_CODE2", list);
				}
			}
			// 과정소분류(상세) 코드조회..
			else if( gbn.equals("classify3")) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect.CLASSIFY_CODE3");
				
				String classify_code1 = "";
				String classify_code2 = "";
				try {
					try {
						classify_code1 = getHoParameter().get("s_classify_code1");
					} catch (Exception e) {
					}
					
					if( !classify_code1.equals("")) {
						classify_code2 = getHoParameter().get("s_classify_code2");
					}
				} catch (Exception e) {
				}

				value.add("CLASSIFY_CODE2", classify_code2);	

				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectClassifyCode3", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect.CLASSIFY_CODE3", list);
				}
			}
			//	월 조회.
			else if( gbn.equals("month")) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect."+ gbn.toUpperCase());
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectMonth", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect."+ gbn.toUpperCase(), list);
				}
			}  
			// 주간 조회
			else if( gbn.equals("weekList".toLowerCase())) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect."+ gbn.toUpperCase());
				
				String selectedYear = "";
				String selectedMonth = "";
				try {
					selectedYear = getHoParameter().get("s_year");
					selectedMonth = getHoParameter().get("s_month");
				} catch (Exception e) {
				}
				
				value.put("PLAN_YEAR", selectedYear );
				value.put("PLAN_MONTH", selectedMonth );
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectWeek", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect."+ gbn.toUpperCase(), list);
				}
			}  
			// 사업장.
			else if( gbn.equals("site")) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect."+ gbn.toUpperCase());
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectSite", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect."+ gbn.toUpperCase(), list);
				}
			}  
			// 메일ID.
			else if( gbn.toLowerCase().equals("mail_id".toLowerCase())) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect."+ gbn.toUpperCase());
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectMail", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect."+ gbn.toUpperCase(), list);
				}
			}  
			// 사용자그룹
			else if( gbn.toLowerCase().equals("use_group".toLowerCase())) { 
				list = (HoList) super.getHoModel().get("com.base.taglib.TabLibSelect."+ gbn.toUpperCase());
				if( list==null ) {
					list = dao.queryForList("CommonCombo.selectUseGroup", value);
					super.getHoModel().put("com.base.taglib.TabLibSelect."+ gbn.toUpperCase(), list);
				}
			}  

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	private String [] getTitleArgs() {
		return getTitleArg().split("}.*,.*{");
	}

	/**
	 *  검색조건의 title의 변수를 배열로 만든다.
	 * @return
	 */
	private Object [] argToArray() {
		Object [] array = null;
		if( !HoUtil.replaceNull(getTitleArg()).equals("") ) {
			array = getTitleArg().split(",");
		}
		return array;
	}

	/**
	 *  검색조건의 title의 변수를 배열로 만든다.
	 * @return
	 */
	private Object [] argToArray(int i) {
		Object [] array = null;
		if( getTitleArg() != null ) {
			if( i<getTitleArgs().length ) {
				array = getTitleArgs()[i].replace('{', ' ').replace('}', ' ').split(",");
			}
		}
		return array;
	}
	
	private String gerGroupCode() {
		String groupCode = "";
		// 공통코드로 combo검색조건을 를 만들 경우.
		if( type.toLowerCase().startsWith("select_".toLowerCase())) {
			groupCode = type.replaceAll("(i?)select_", "") ;
		} 
		// 공통코드 checkbox
		else if( type.toLowerCase().startsWith("checkbox_".toLowerCase())) {
			groupCode = type.replaceAll("(i?)checkbox_", "") ;
		}	
		// 공통코드 radio
		else if( type.toLowerCase().startsWith("radio_".toLowerCase())) {
			groupCode = type.replaceAll("(i?)radio_", "") ;
		}	
		// select다중선택
		else if( type.toLowerCase().startsWith("selectmulti_".toLowerCase())) {
			groupCode = type.replaceAll("(i?)selectMulti_", "") ;
		}		
		
		// 공통코드로 검색조건을 를 만들 경우.
		else if( type.toLowerCase().startsWith("selectgroup_".toLowerCase())) {
			groupCode = type.replaceAll("(i?)selectGroup_", "") ;
		} 
		
		return groupCode.toUpperCase();
	}
	
	private boolean isCommonCode() {
		// 공통코드로 combo검색조건을 를 만들 경우.
		if( type.toLowerCase().startsWith("select_".toLowerCase())) {
			return true;
		} 
		// 공통코드 checkbox
		else if( type.toLowerCase().startsWith("checkbox_".toLowerCase())) {
			return true;
		}	
		// 공통코드 radio
		else if( type.toLowerCase().startsWith("radio_".toLowerCase())) {
			return true;
		}	
		// select다중선택
		else if( type.toLowerCase().startsWith("selectmulti_".toLowerCase())) {
			return true;
		}		
		// 공통코드로 검색조건을 를 만들 경우.
		else if( type.toLowerCase().startsWith("selectgroup_".toLowerCase())) {
			return true;
		} else {
			return false;
		}
		
	}
	
	// 객체명을 조회한다.
	private String getName() {
		String name = "";
		
		return name;
		
	}
	
	public String getCodeColumn() {
		return codeColumn;
	}

	public void setCodeColumn(String codeColumn) {
		this.codeColumn = codeColumn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotNull() {
		return notNull;
	}

	public void setNotNull(String notNull) {
		this.notNull = notNull;
	}

	public String getNameColumn() {
		return nameColumn;
	}

	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	public String getTitleArg() {
		return titleArg;
	}
	public void setTitleArg(String titleArg) {
		this.titleArg = titleArg;
	}
	public String getFirstGbn() {
		return firstGbn;
	}
	public void setFirstGbn(String firstGbn) {
		this.firstGbn = firstGbn;
	}
	public String getTitleText() {
		return titleText;
	}
	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}
	public String getAppend() {
		return append;
	}
	public void setAppend(String append) {
		this.append = append;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getOnChange() {
		return onchange;
	}
	public void setOnChange(String onchange) {
		this.onchange = onchange;
	}
	public String getColspan() {
		return colspan;
	}
	public void setColspan(String colspan) {
		this.colspan = colspan;
	}
	public String getRowspan() {
		return rowspan;
	}
	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}
	public String getDetaultValue() {
		return detaultValue;
	}
	public void setDetaultValue(String detaultValue) {
		this.detaultValue = detaultValue;
	}
	public String getWhereColumn() {
		return whereColumn;
	}
	public void setWhereColumn(String whereColumn) {
		this.whereColumn = whereColumn;
	}
	public String getWhereCondition() {
		return whereCondition;
	}
	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}

}

package com.base.taglib;

import java.util.HashMap;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.dao.result.HoList;
import project.jun.dao.result.HoMap;
import project.jun.taglib.TagLibSuperDB;
import project.jun.util.HoDate;
import project.jun.util.HoUtil;


/**
 * Ext js용.
 * @author USER00
 *
 */
public class TagLibSearch extends TagLibSuperDB {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type	  = "";   // select_공통코드, checkbox_공통코드, radio_공통코드, selectGroup_공통코드
	private String firstGbn  = ""; // all, choice, none, auto
	private String titleText = "";
	private String titleCode = "";
	private String titleArg = "";
	private String description = "";
	private String notNull	  = "";
	private String sortColumn = "";
	private String sortDir	= "";
	private String codeColumn = "CODE";
	private String nameColumn = "CODE_NAME";
	private String disabled = "";
	private String onchange = "";
	private String append = "";
	private String colspan = "";
	private String rowspan = "";
	private String defaultValue = "";
	private int    width  = 150;
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
		else if( type.toLowerCase().startsWith("popup_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printPopup(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}
		// 년
		else if( type.toLowerCase().startsWith("year_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printYearMonth(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		} 
		// 년/월
		else if( type.toLowerCase().startsWith("ym_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printYearMonth(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		} 
		// 년/월/주
		else if( type.toLowerCase().startsWith("ymw_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printYearMonthWeek(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}
		// 날짜( From ~ To )검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("date_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printDateFromTo(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}  
		// 일반 Text 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("text_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printTextField(ctx, out, buf);
			
			// Tag Library 종료
			tagLibEnd(buf);
			
			
		}  
		// 숫자를 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("number_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printNumberField(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}  
		// hidden 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("hidden_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printHiddenField(ctx, out, buf);

			// Tag Library 종료
			tagLibEnd(buf);
		}  
		// label 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("model_".toLowerCase())) {
			// Tag Library 시작.
			tagLibStart(buf);

			printModelField(ctx, out, buf);

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
		
		increaseIndex("SEARCH");
		
		return EVAL_PAGE;
	}

	/**
	 * Tag Library시작 
	 * @param buf
	 * @throws Exception
	 */
	public void tagLibStart(StringBuffer buf ) throws Exception
	{
		int size = getTdSize();
		
		System.out.println( type  + ": here : " + (getCurrentIndex("SEARCH_TD") + size));
		
		if( getCurrentIndex("SEARCH_TD") + size > getSearchMaxTdCnt()  ) {
			// addNewLine(buf, "]} ");	// close compositefield --> TagLibForm.java에서도 주석처리 필요.
			addNewLine(buf, "] } // 1");	
			
			initIndex("SEARCH_TD");
			initIndex("SEARCH_TD_CNT");
			increaseIndex("SEARCH_TR");
		}
		
		if( getCurrentIndex("SEARCH") > 0 ) {
			buf.append(", ");
		}

		if( getCurrentIndex("SEARCH_TD_CNT") == 0 ) {

			increaseIndex("SEARCH_TD", size);

			addNewLine(buf, "{ xtype : 'fieldcontainer', ");
			addNewLine(buf, "	defaults : {labelAlign: 'right', labelWidth: 90 },");
			addNewLine(buf, "   			items: [");			
		} 

		addNewLine(buf, "   // Tag Library [ type : "+ type + ( type.toLowerCase().startsWith("code") ? (",  groupCode : " + type.toLowerCase().replaceAll("(?i)code_", "").toUpperCase()) +"(* name refer V_CODE.USEDEF1)" : "" ) + "]");
		if(!description.equals("")) {
			buf.append(description);
		}

		// buf.append("						<tr>\r\n");
	}

	/**
	 * Front용 Tag Library시작 
	 * @param buf
	 * @throws Exception
	 */
	public void frontTagLibStart(StringBuffer buf ) throws Exception
	{
		addNewLine(buf,"<!-- Tag Library [ type : "+ type + ( type.toLowerCase().startsWith("code") ? (",  groupCode : " + type.toLowerCase().replaceAll("(?i)code_", "")) +"(* name refer V_CODE.USEDEF1)" : "" ) + "]");
		if(!description.equals("")) {
			addNewLine(buf,description);
		}
		addNewLine(buf,"-->\r\n");
		
		// buf.append("						<tr>\r\n");
	}

	/**
	 * Tag Library종료 
	 * @param buf
	 * @throws Exception
	 */
	public void tagLibEnd(StringBuffer buf ) throws Exception
	{
		increaseIndex("SEARCH_TD_CNT");

		int size = getTdSize();
			
		increaseIndex("SEARCH_TD", size);

		if( getCurrentIndex("SEARCH_TD") > getSearchMaxTdCnt()  ) {
			// addNewLine(buf, "]}"); // close compositefield
			addNewLine(buf, "] } // 2");	
			
			initIndex("SEARCH_TD");
			initIndex("SEARCH_TD_CNT");
			increaseIndex("SEARCH_TR");
		}

		/*
		// 공통코드로 combo검색조건을 를 만들 경우.
		if( type.toLowerCase().startsWith("select_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		} 
		// 공통코드 checkbox
		else if( type.toLowerCase().startsWith("checkbox_".toLowerCase())) {
		}	
		// 공통코드 radio
		else if( type.toLowerCase().startsWith("radio_".toLowerCase())) {
		}	
		// select다중선택
		else if( type.toLowerCase().startsWith("selectMulti_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		}		
		// 공통코드로 검색조건을 만들 경우.
		else if( type.toLowerCase().startsWith("selectGroup_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		} 
		// 
		else if( type.toLowerCase().startsWith("popup_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		}
		// 년
		else if( type.toLowerCase().startsWith("year_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		} 
		// 년/월
		else if( type.toLowerCase().startsWith("ym_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		} 
		// 년/월/주
		else if( type.toLowerCase().startsWith("ymw_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		}
		// 날짜( From ~ To )검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("date_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		}  
		// 일반 Text 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("text_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		}  
		// 숫자를 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("number_".toLowerCase())) {
			increaseIndex("SEARCH_TD", size);
		}  
		// hidden 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("hidden_".toLowerCase())) {
		}  
		// 기타  검색 type이 설정되지 않았을경우.
		else {
			increaseIndex("SEARCH_TD", 2);
		}
*/
		

	}

	
	private int getTdSize() {
		int size = 0;
		// 공통코드로 combo검색조건을 를 만들 경우.
		if( type.toLowerCase().startsWith("select_".toLowerCase())) {
			size =  2;
		} 
		// 공통코드 checkbox
		else if( type.toLowerCase().startsWith("checkbox_".toLowerCase())) {
			size = getSearchMaxTdCnt();
		}	
		// 공통코드 radio
		else if( type.toLowerCase().startsWith("radio_".toLowerCase())) {
			size = getSearchMaxTdCnt();
		}	
		// select다중선택
		else if( type.toLowerCase().startsWith("selectMulti_".toLowerCase())) {
			size =  2;
		}		
		// 공통코드로 검색조건을 만들 경우.
		else if( type.toLowerCase().startsWith("selectGroup_".toLowerCase())) {
			size =  2;
		} 
		// 
		else if( type.toLowerCase().startsWith("popup_".toLowerCase())) {
			size =  4;
		}
		// 년
		else if( type.toLowerCase().startsWith("year_".toLowerCase())) {
			size =  1;
		} 
		// 년/월
		else if( type.toLowerCase().startsWith("ym_".toLowerCase())) {
			size =  1;
		} 
		// 년/월/주
		else if( type.toLowerCase().startsWith("ymw_".toLowerCase())) {
			size =  1;
		}
		// 날짜( From ~ To )검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("date_".toLowerCase())) {
			size =  4;
		}  
		// 일반 Text 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("text_".toLowerCase())) {
			size =  2;
			
		}  
		// 숫자를 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("number_".toLowerCase())) {
			size =  2;
		}  
		// hidden 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("hidden_".toLowerCase())) {
			size =  0;
		}  
		// 기타  검색 type이 설정되지 않았을경우.
		else {
			size =  2;
		}
		
		return size;
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

	
	public void printTitle(StringBuffer buf, String title) {
		/*
		addNewLine(buf, "{");
		addNewLine(buf, "xtype : 'label', ");
		// addNewLine(buf, "html : '<span style=\"width:150;\">"+title+"</span> : ',");
		addNewLine(buf, "text : '"+title+" ',");
		addNewLine(buf, "width : 85 ,");
		addNewLine(buf, "cls : 'search_label',"); // x-form-item-label search_label x-form-label-right
		addNewLine(buf, "labelStyle : 'font-weight:bold;text-align:right;vertical-align:bottom;display:inline-block;'");
		addNewLine(buf, "},");
		
		 */
	}

	public void printFieldLabel(StringBuffer buf, String title) {
		//addNewLine(buf, "plugins: [ Ext.ux.FieldLabeler ], ");
		//addNewLine(buf, "fieldLabel : '"+title+"', labelWidth : 100,");
		//addNewLine(buf, "labelAlign : 'right',");
		
		addNewLine(buf, "fieldLabel : '"+title+"',");

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
		String width = "150";
		
		HoList list = getList(ctx, type.toLowerCase() );
		

		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		try {
			if( getMessageTitle(ctx, titleCode, titleArg, titleText).equals("")) {
				if( list.size() > 0 ) {
					title = list.getString(0,"CODE_NAME");
					name = list.getString(0,"USEDEF1").toLowerCase();
					width = list.getString(0,"USEDEF2").equals("") ? String.valueOf(getWidth()) : list.getString(0,"USEDEF2");
				} 
			} else {
				title = getMessageTitle(ctx, titleCode, titleArg, titleText);
				name = type.toLowerCase().replaceAll("(?i)code_", "");
			}
		} catch(Exception e)  {
			title = getMessageTitle(ctx, titleCode, titleArg, titleText);
			name = type.toLowerCase().replaceAll("(?i)code_", "");
		}
		
		// 전체일경우( 선택 또는 없음이 아닌 경우)
		if( useMultiName() ) {
			name = "s_" + name + "_es";
		} else if( !name.toLowerCase().equals("pagerowcnt")) {
			name = "s_" + name;
		}

		selectedVal = getHoParameter().get(name );

		int startIdx = 1;
		printTitle(buf, title);
		addNewLine(buf, " {");
		printFieldLabel(buf, title);
		addNewLine(buf, " xtype : 'combobox',");
		addNewLine(buf, " displayField : '"+getNameColumn()+"',");
		addNewLine(buf, " valueField : '"+getCodeColumn()+"',");
		addNewLine(buf, " name: '"+name+"', hiddenName  : '"+name+"',");
		addNewLine(buf, " typeAhead: true,");
		addNewLine(buf, " mode: 'local', queryMode: 'local', ");
		addNewLine(buf, " forceSelection: false, autoSelect : false,");
		addNewLine(buf, " width: "+getWidth()+",");
		
		if( firstGbn.equals("none")) {
			addNewLine(buf, " emptyText : '',");
		} else {
			addNewLine(buf, " emptyText : '"+list.get(1, getNameColumn())+"',");
			startIdx = 2;
		}
		
		addNewLine(buf, " disableKeyFilter : true,");
		addNewLine(buf, " selectOnFocus : true ,");
		
		if( getNotNull().equals("Y") || firstGbn.equals("choice") || firstGbn.equals("none")) {
			addNewLine(buf, " allowBlank : false ,");
		}
		
		addNewLine(buf, " value : '"+selectedVal+"' ,");
		addNewLine(buf, " store: Ext.create('Ext.data.Store', { ");
		addNewLine(buf, " model : Ext.define('"+getGroupCode()+"', { extend : 'Ext.data.Model',  ");
		addNewLine(buf, " fields: [");
		addNewLine(buf,  list.getMetaDataString("name") );
		addNewLine(buf, " ]}),");
		addNewLine(buf, " data: ");
		addNewLine(buf,  list.toJavascriptArray(startIdx) );
		addNewLine(buf, " }) ");
		addNewLine(buf, " }");

		
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
			if( getMessageTitle(ctx, titleCode, titleArg, titleText).equals("")) {
				if( list.size() > 0 ) {
					title = list.getString(0,"CODE_NAME");
					name = list.getString(0,"USEDEF1").toLowerCase();
				} 
			} else {
				title = getMessageTitle(ctx, titleCode, titleArg, titleText);
				name = type.toLowerCase().replaceAll("(?i)code_", "");
			}
		} catch(Exception e)  {
			title = getMessageTitle(ctx, titleCode, titleArg, titleText);
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

		if( getNotNull().length() > 0 )
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
			if( getMessageTitle(ctx, titleCode, titleArg, titleText).equals("")) {
				if( list.size() > 0 ) {
					title = list.getString(0,"CODE_NAME");
					name = list.getString(0,"USEDEF1").toLowerCase();
				} 
			} else {
				title = getMessageTitle(ctx, titleCode, titleArg, titleText);
				name = type.toLowerCase().replaceAll("(?i)code_", "");
			}
		} catch(Exception e)  {
			title = getMessageTitle(ctx, titleCode, titleArg, titleText);
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

		if( getNotNull().length() > 0 )
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
		if( getMessageTitle(ctx, titleCode, titleArg, titleText).equals("")) {
			if( list.size() > 0 ) {
				title = list.getString(0,"CODE_NAME");
				name = list.getString(0,"USEDEF1").toLowerCase();
			} 
		} else {
			title = getMessageTitle(ctx, titleCode, titleArg, titleText);
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
		if( getMessageTitle(ctx, titleCode, titleArg, titleText).equals("")) {
			if( list.size() > 0 ) {
				title = list.getString(0,"CODE_NAME");
				name = list.getString(0,"USEDEF1").toLowerCase();
			} 
		} else {
			title = getMessageTitle(ctx, titleCode, titleArg, titleText);
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

		title = getMessageTitle("HRD_PAGE_1096", ctx, titleCode, titleArg, titleText);
		if( title.equals("")) {
			title = "강의장일정";
		}

		// 좌측에 나타날 검색 명 및 검색 할 Object의 명을 만든다.
		String yearTitle = getMessageTitle("HRD_PAGE_0217", ctx, titleCode, titleArg, titleText);
		if( yearTitle.equals("")) {
			yearTitle = "년도";
		} 
		
		
		String monTitle = getMessageTitle("HRD_PAGE_0545", ctx, titleCode, titleArg, titleText);
		if( monTitle.equals("")) {
			monTitle = "월";
		} 
		
		String weekTitle = getMessageTitle("HRD_PAGE_1097", ctx, titleCode, titleArg, titleText);
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

		if( getNotNull().length() > 0 )
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
		if( getNotNull().length() > 0 )
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

		if( getNotNull().length() > 0 )
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
			title = getMessageTitle("HRD_PAGE_0888", ctx, titleCode, titleArg, titleText);
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
			title = getMessageTitle("HRD_PAGE_0888", ctx, titleCode, titleArg, titleText);
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
			title = getMessageTitle("HRD_PAGE_0538", ctx, titleCode, titleArg, titleText);
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
			title = getMessageTitle("HRD_PAGE_0504", ctx, titleCode, titleArg, titleText);
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
			title = getMessageTitle("HRD_PAGE_0013", ctx, titleCode, titleArg, titleText);
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
		String yearTitle = getMessageTitle("HRD_PAGE_0217", ctx, titleCode, titleArg, titleText);
		if( yearTitle.equals("")) {
			yearTitle = "년도";
		} 
		
		
		String monTitle = getMessageTitle("HRD_PAGE_0545", ctx, titleCode, titleArg, titleText);
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

		if( getNotNull().length() > 0 )
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
		if( getNotNull().length() > 0 )
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
			title = getMessageTitle("HRD_PAGE_1074", ctx, titleCode, titleArg, titleText);
			if( title.equals("")) {
				title = "강사료";
			} 
			startName = "s_lecturer_start_fee";
			endName = "s_lecturer_end_fee";
			moneyText = " isMoney=\"Y\" ";
		} 
		//과제수
		else if( type.toLowerCase().equals("taskCnt".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1079", ctx, titleCode, titleArg, titleText);
			if( title.equals("")) {
				title = "과제수";
			} 
			startName = "s_task_start_cnt";
			endName = "s_task_end_cnt";
		} 
		//시험수
		else if( type.toLowerCase().equals("examCnt".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1093", ctx, titleCode, titleArg, titleText);
			if( title.equals("")) {
				title = "시험수";
			} 
			startName = "s_exam_start_cnt";
			endName = "s_exam_end_cnt";
		} 
		//진도율
		else if( type.toLowerCase().equals("progress".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_0710", ctx, titleCode, titleArg, titleText);
			if( title.equals("")) {
				title = "진도율";
			} 
			startName = "s_progress_start";
			endName = "s_progress_end";
		} 
		//토론수
		else if( type.toLowerCase().equals("discussCnt".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1094", ctx, titleCode, titleArg, titleText);
			if( title.equals("")) {
				title = "토론수";
			} 
			startName = "s_discuss_start_cnt";
			endName = "s_discuss_end_cnt";
		} 
		//평가비율합계점수
		else if( type.toLowerCase().equals("evalRateSum".toLowerCase())) {
			title = getMessageTitle("HRD_PAGE_1095", ctx, titleCode, titleArg, titleText);
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
	 * hidden 검색조건 만들기.
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void printHiddenField(WebApplicationContext ctx, JspWriter out, StringBuffer buf  ) throws Exception
	{
		addNewLine(buf, "{ xtype     : 'hidden',");

		// 검색조건 타이틀 출력.
		addNewLine(buf, "  name : '" + type.replaceAll("hidden_", "") + "', ");
		addNewLine(buf, "  value     : '"+defaultValue+"' ");
		addNewLine(buf, "} ");

	}
	
	
	public void printModelField(WebApplicationContext ctx, JspWriter out, StringBuffer buf  ) throws Exception
	{
		addNewLine(buf, "{ xtype     : 'hidden',");

		// 검색조건 타이틀 출력.
		addNewLine(buf, "  name : '" + type.replaceAll("hidden_", "") + "', ");
		addNewLine(buf, "  value     : '"+defaultValue+"' ");
		addNewLine(buf, "} ");

	}
	
	/**
	 * 문자 입력 가능한 검색조건 만들기.
	 * @param factory
	 * @param out
	 * @throws Exception
	 */
	public void printTextField(WebApplicationContext ctx, JspWriter out, StringBuffer buf  ) throws Exception
	{
		
		
		HoMap map = getName(ctx);

		
		String title = (String) map.get("title");
		String name = (String) map.get("name");

		printTitle(buf, title);
		
		addNewLine(buf, "{ xtype     : 'trigger',");
		
		printFieldLabel(buf, title);
		
		addNewLine(buf, "  triggerClass : 'x-form-search-trigger',");
		addNewLine(buf, "  listeners : { click : { element: 'el', fn: function(){ alert( this.triggerClass ); } } }, ");
		if( getNotNull().equals("Y")) {
			addNewLine(buf, "  allowBlank : false, ");
		}

		// 검색조건 타이틀 출력.
		addNewLine(buf, "  id : 'id_" + name + "', ");
		addNewLine(buf, "  name : '" + name + "', ");
		addNewLine(buf, "  width : "+ getWidth() +" ,");
		//addNewLine(buf, "  labelStyle: 'font-weight:bold;text-align:right;',");
		addNewLine(buf, "  anchor    : '-5',");
		if(!map.getString("ALLOW_CHARS").equals(""))   {
			addNewLine(buf, "  allowChars    : '"+(String) map.get("ALLOW_CHARS")+"',");
		}
		addNewLine(buf, "  onTriggerClick     : function() { alert('"+((TagLibForm)super.getParent()).getType() + "form :' + this.getId());} , ");
		addNewLine(buf, "  value     : '" + super.getHoParameter().get(name) + "' ");
		
		addNewLine(buf, "} ");
		//addNewLine(buf, ", { xtype : 'imagebutton', ");
		//addNewLine(buf, "imgPath: '/static/ext/extJs3.1/resources/images/ux/add.png', ");
		//addNewLine(buf, "	handler: function() {alert('Creating a new book!');}");
		//addNewLine(buf, "}");
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

		title = getMessageTitle("HRD_PAGE_0110", ctx, titleCode, titleArg, titleText);
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
		if( getNotNull().length() > 0 )
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

		HoMap map = getName(ctx);
	
		
		String title = map.getString("title");
		String name  = map.getString("name");

		
		String startValue = "";
		String endValue = "";

		startValue = super.getHoParameter().get(name + "_start_dt");
		endValue = super.getHoParameter().get(name + "_end_dt");

		if( !getDefaultValue().equals("")) {
			HoDate hd = new HoDate(super.getHoParameter().getDefaut("DateFormat"));
			
			if(getDefaultValue().equals("sysweek")) {
				startValue = hd.getFirstDayOfWeek();
				endValue = hd.getLastDayOfWeek();		
			} else if(getDefaultValue().equals("sysmonth")) {
				startValue = hd.getFirstDayOfMonth();
				endValue = hd.getLastDayOfMonth();	
			} else if(getDefaultValue().equals("sysyear")) {
				startValue = hd.getFirstDayOfYear();
				endValue = hd.getLastDayOfYear();		
			} else if(getDefaultValue().equals("sysdate")) {
				startValue = hd.getToday();
				endValue =  hd.getToday();		
			}
		} 
		
		printTitle(buf, title);	
		//addNewLine(buf, "{ layout     : 'column', width : 230, items : [");
		addNewLine(buf, "{ xtype     : 'datefield_ux', ");
		printFieldLabel(buf, title);
		addNewLine(buf, "  name : '" + name + "_start_dt', ");
		addNewLine(buf, "  anchor    : '-5',");
		addNewLine(buf, "  format    : '"+super.getHoConfig().getDateFormatMapJS(super.getHoParameter().getDefaut("DateFormat"))+"',");
		addNewLine(buf, "  altFormats    : '"+ super.getHoConfig().getDateAltFormatMapJS(super.getHoParameter().getDefaut("DateFormat"))+"',");		
		addNewLine(buf, "  id: 'id_" + name + "_start_dt',");	
		addNewLine(buf, "  endField: 'id_" + name + "_end_dt' ,");	
		addNewLine(buf, "  value     : '" + startValue + "' ");
		addNewLine(buf, "} ");
		addNewLine(buf, ",{");
		addNewLine(buf, " xtype : 'displayfield', ");
		addNewLine(buf, " value : ' ~ '");
		addNewLine(buf, " }");
		addNewLine(buf, ",{ xtype     : 'datefield_ux', style : 'padding-rigth:100px', ");
		addNewLine(buf, "  name : '" + name + "_end_dt', ");
		addNewLine(buf, "  anchor    : '-5',");
		addNewLine(buf, "  format    : '"+super.getHoConfig().getDateFormatMapJS(super.getHoParameter().getDefaut("DateFormat"))+"',");
		addNewLine(buf, "  altFormats    : '"+ super.getHoConfig().getDateAltFormatMapJS(super.getHoParameter().getDefaut("DateFormat"))+"',");		
		addNewLine(buf, "  id: 'id_" + name + "_end_dt',");	
		addNewLine(buf, "  startField: 'id_" + name + "_start_dt' ,");	
		addNewLine(buf, "  value     : '" + endValue + "' ");
		addNewLine(buf, "}  ");
		/*
		addNewLine(buf, ",{");
		addNewLine(buf, " xtype : 'label',");
		addNewLine(buf, " text : ' ', ");
		addNewLine(buf, " width : 117  ");
		addNewLine(buf, " }");
		//addNewLine(buf, "] }");
		 */
		
//		addNewLine(buf, ", new Ext.CycleButton({                               ");
//		addNewLine(buf, "    showText: true,                                 ");
//		addNewLine(buf, "    items: [{                                       ");
//		addNewLine(buf, "        text:'7 days',                           ");
//		addNewLine(buf, "        checked:true                                ");
//		addNewLine(buf, "    },{                                             ");
//		addNewLine(buf, "        text:'1 month '                         ");
//		addNewLine(buf, "    },{                                             ");
//		addNewLine(buf, "        text:'3 months '                         ");
//		addNewLine(buf, "    },{                                             ");
//		addNewLine(buf, "        text:'1 year '                                ");
//		//addNewLine(buf, "        , iconCls:'view-html'                         ");
//		addNewLine(buf, "    }],                                             ");
//		addNewLine(buf, "    changeHandler:function(btn, item){              ");
//		addNewLine(buf, "        Ext.Msg.alert('Change View', item.text);    ");
//		addNewLine(buf, "    }                                               ");
//		addNewLine(buf, "})                                                  ");

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
	public HoList getList(WebApplicationContext ctx, String gbn) {
		String groupCode = "";
		HoQueryParameterMap value = new HoQueryParameterMap();

		// 세션정보를 담는다.
		value.putAll(getHoRequest().getSessionMap());

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
				
				groupCode = getGroupCode();
				
				HashMap options = new HashMap();
				
				options.put("GROUP_CODE", groupCode );
				options.put("SORT_COLUMN", sortColumn);
				options.put("SORT_DIR", sortDir);
				
				
				if( !whereColumn.equals("")) {
					options.put("WHERE_COLUMN", whereColumn);
				}
				if( !whereCondition.equals("")) {
					options.put("WHERE_CONDITION", whereCondition);
				}
				
				list = super.getTagLibUtil().getList(ctx, getGroupCode(), getHoRequest(), getHoParameter(), firstGbn, options);
				
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	
	private String getGroupCode() {
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
	
	/**
	 *  객체명을 조회한다.
	 *  key="title" : title
	 *  key="name"  : html object명
	 * @return
	 */
	private HoMap getName(WebApplicationContext ctx) {

		String fieldName = "";
		
		// 공통코드로 combo검색조건을 를 만들 경우.
		if( type.toLowerCase().startsWith("select_".toLowerCase())&& fieldName.equals("")) {
		} 
		// 공통코드 checkbox
		else if( type.toLowerCase().startsWith("checkbox_".toLowerCase())&& fieldName.equals("")) {
		}	
		// 공통코드 radio
		else if( type.toLowerCase().startsWith("radio_".toLowerCase())&& fieldName.equals("")) {
		}	
		// select다중선택
		else if( type.toLowerCase().startsWith("selectMulti_".toLowerCase())&& fieldName.equals("")) {
		}				
		// 공통코드로 검색조건을 를 만들 경우.
		else if( type.toLowerCase().startsWith("selectGroup_".toLowerCase())&& fieldName.equals("")) {
		} 
		// 
		else if( type.toLowerCase().startsWith("popup_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(6).toLowerCase();
		}
		// 년
		else if( type.toLowerCase().startsWith("year_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(5).toLowerCase();
		} 
		// 년/월
		else if( type.toLowerCase().startsWith("ym_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(3).toLowerCase();
		} 
		// 년/월/주
		else if( type.toLowerCase().startsWith("ymw_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(4).toLowerCase();
		}
		// 날짜( From ~ To )검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("date_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(5).toLowerCase();
		}  
		// 일반 Text 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("text_".toLowerCase()) && fieldName.equals("") ) {
			fieldName = type.toLowerCase().substring(5).toLowerCase();
		}  
		// 숫자를 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("number_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(7).toLowerCase();
		}  
		// hidden 검색조건을 만들 경우
		else if( type.toLowerCase().startsWith("hidden_".toLowerCase())&& fieldName.equals("")) {
			fieldName = type.toLowerCase().substring(7).toLowerCase();
		} 
		
		HoMap map = super.getTagLibUtil().getInfo(ctx, fieldName, getHoRequest(), getHoParameter());

					
		titleCode = map.getString("TITLE_CODE");
		
		if( !titleCode.equals("")) {
			titleText = getMessageTitle(titleCode, ctx, titleCode, titleArg, titleText);
		} 
		
		if(titleText.equals("") ) {
			titleText = map.getString("TEXT");
		}

		
		// 기타 Text검색조건
		/*
		if( titleText.equals("")) {
			titleText = getMessageTitle(ctx, titleCode, titleArg, titleText);
		} 
		*/

		map.put("TITLE", titleText);
				
		return map;
		
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
		return notNull.toUpperCase();
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
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public int getWidth() {
		return width+90;
	}


	public void setWidth(int width) {
		this.width = width;
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

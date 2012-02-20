package com.base.taglib;

import java.util.Locale;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.dao.HoDaoImpl;
import project.jun.dao.parameter.HoQueryParameterHandler;
import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.dao.result.HoList;
import project.jun.taglib.TagLibSuperDB;


public class TagLibButton extends TagLibSuperDB 
{
	 
	private static final long serialVersionUID = 6782221740151368602L;
	
	private String buttons = "";   
	private String id      = "divButton";
	private String className   = "";
	private String width = "";
	private String height = "";
	private String type = "";
	public int doStart(WebApplicationContext ctx, JspWriter out) throws Exception 
	{
		if( type.toLowerCase().equals("grid") ) {
			doStartGridButton(out);
		} else if( type.toLowerCase().equals("ajax") ) {
			doStartAjaxButton(out);
		} else {
			doStartButton(out);
		}

		return EVAL_BODY_INCLUDE;
	
	}
	
	public int doEnd(WebApplicationContext factory, JspWriter out) throws Exception 
	{
		HoList list = getList(factory);

		if( type.toLowerCase().equals("grid") ) {
			doEndGridButton(list, out);
		} else if( type.toLowerCase().equals("ajax") ) {
			doEndAjaxButton(list, out);
		} else {
			doEndButton(list, out);
		}
		
		return EVAL_PAGE;
	}
	
	public HoList getList(WebApplicationContext factory) {
		
		HoDaoImpl dao = getHoDaoImpl(factory);
		
		HoQueryParameterHandler handler  = new HoQueryParameterHandler(getHoParameter());
		
    	HoQueryParameterMap     value = handler.getForDetail();
									
    	value.put("BTN_LIST_ES", handler.getSplit(getButtons()));

    	HoList list = null;
		try {
			// 파라미터에 메뉴정보가 있으면 메뉴권한에 해당하는 버튼목록을 조회한다.
			if( !getHoParameter().get("menu_id").equals("")) {
				list = dao.queryForList("MenuDAO.selectMenuButton", value);
			} 
			// 파라미터에 메뉴정보가 없으면 모든 버튼목록을 조회한다.
			else {
				list = dao.queryForList("MenuDAO.selectButton", value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		return list;
	}
	
	
	/**
	 * Ext grid그리드의 상단에 버튼을 나타낼 경우.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void doStartGridButton(JspWriter out ) throws Exception
	{

		StringBuffer buf = new StringBuffer();

		addNewLine(buf, " tbar = [");
		addNewLine(buf, " 'Grid Width:',                                        "); 
		addNewLine(buf, "	     this.gridWith = new Ext.form.NumberField({  ");
		addNewLine(buf, "	        id: 'x-tbar-grid-fix-width',               ");
		addNewLine(buf, "	        cls: 'x-tbar-page-number',               ");
		addNewLine(buf, "	        allowDecimals: false,                    ");
		addNewLine(buf, "	        allowNegative: false,                    ");
		addNewLine(buf, "	        enableKeyEvents: true,                   ");
		addNewLine(buf, "	        selectOnFocus: true,                     ");
		addNewLine(buf, "	        minValue: 100,                     ");
		addNewLine(buf, "	        maxValue: 3000,                     ");
		addNewLine(buf, "	        submitValue: false                      ");
		addNewLine(buf, "	        })                                      ");
		addNewLine(buf, "	    , this.fixWidth = {                          ");
		addNewLine(buf, "            pressed: false,                         ");
		addNewLine(buf, "            enableToggle:true,                      ");
		addNewLine(buf, "            text: 'Fix',                            ");
		addNewLine(buf, "            toggleHandler: fixGridWidth  ");
		addNewLine(buf, "	    },                                            ");

		out.print( buf.toString() );
	}

	
	
	/**
	 * Ext grid그리드의 상단에 버튼을 나타낼 경우.
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void doEndGridButton(HoList list, JspWriter out ) throws Exception
	{

    	Locale locale = Locale.getDefault();
    	String language = locale.getLanguage();

		StringBuffer buf = new StringBuffer();

		HoQueryParameterHandler handler  = new HoQueryParameterHandler(getHoParameter());
		String [] editBtn = handler.getSplit(getButtons());
		
		boolean first = true;
		
		String varName = "";
		// edit용 버튼
		for( int i=0 ; i<editBtn.length ;  i++ ) {
			if( editBtn[i].startsWith("edit_") ) {
				if( first ) {
					addNewLine(buf, " '->', ");
				}
				if( !first ) {
					addNewLine(buf, " ,  ");
				} 
				
				if( editBtn[i].indexOf('.') > 0 ) {
					varName = editBtn[i].split("\\.")[1];
				}else {
					varName = editBtn[i];
				}
				addNewLine(buf,  "'" + varName.toUpperCase() + ":' , ");
				addNewLine(buf, varName.toLowerCase());
				
				addNewLine(buf,  ", new Ext.Button({");
				addNewLine(buf,  "      id : 'id_"+varName.toLowerCase().toLowerCase()+"_apply',");
				addNewLine(buf,  "      text : '적용', ");
				addNewLine(buf,  "      handler : fs_"+varName.toUpperCase()+"Apply");
				addNewLine(buf,  "      })");
				if( first ) {
					addNewLine(buf, ", '|' ");
				}
				first = false;
				
			}
		}
		// db에서 조회한 버튼
		for( int i=0 ; i<list.size() ; i++ ) {
			if( first ) {
				addNewLine(buf, " '->', ");
			}
			if( !first ) {
				addNewLine(buf, " , '|', ");
			} 
			addNewLine(buf, " new Ext.Button({");
			addNewLine(buf, " text : '"+list.getString(i,"BTN_NM_"+language)+"', ");
			addNewLine(buf, " handler : "+list.getString(i,"BTNFUNC")+", ");
			addNewLine(buf, " iconCls : 'btn_"+list.getString(i,"BTN_CSS")+"', ");
			addNewLine(buf, " tooltip : '"+list.getString(i,"TOOLTIP"+language)+"' ");
			addNewLine(buf, "})");
		}
		addNewLine(buf, " ];");

		out.print( buf.toString() );
	}

	/**
	 * Ajax Data의 상단에 버튼을 나타낼 겨우
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void doStartAjaxButton(JspWriter out ) throws Exception
	{

		
	}

	
	/**
	 * Ajax Data의 상단에 버튼을 나타낼 겨우
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	public void doEndAjaxButton(HoList list, JspWriter out ) throws Exception
	{

		
	}

	/**
	 * TagLib 생성부분
	 * @param queryProvider
	 * @param out
	 * @throws Exception
	 */
	public void doStartButton(JspWriter out ) throws Exception
	{

	}
	
	/**
	 * TagLib 생성부분
	 * @param queryProvider
	 * @param out
	 * @throws Exception
	 */
	public void doEndButton(HoList list, JspWriter out ) throws Exception
	{

    	Locale locale = Locale.getDefault();
    	String language = locale.getLanguage();

		StringBuffer buf = new StringBuffer();

		if( getCurrentIndex("SEARCH_TD") <= getSearchMaxTdCnt() ) {
			// addNewLine(buf, "]} ");	 // close compositefield
			addNewLine(buf, " ] } // 3");	
			
			initIndex("SEARCH_TD");
			initIndex("SEARCH_TD_CNT");
			increaseIndex("SEARCH_TR");
		}
		addNewLine(buf, " ] } ]  ");

		addNewLine(buf, " , ");
		addNewLine(buf, " buttons : [ ");
		/*
		//addNewLine(buf, " header : false, ");
		//addNewLine(buf, " footer : false, ");
		//addNewLine(buf, " frame : false, ");
		addNewLine(buf, " width : 1000, ");
		//addNewLine(buf, " height : 0, ");
		addNewLine(buf, " border : false , ");
		addNewLine(buf, " style : 'margin-bottom : 5px;' ,");
		addNewLine(buf, " layout : 'hbox', ");
		addNewLine(buf, " layoutConfig : { align: 'right', pack : 'end' }, ");
		addNewLine(buf, " itemCls : 'ux-search-button',");
		*/
		//addNewLine(buf, " items : [");
		for( int i=0 ; i<list.size() ; i++ ) {
			if( i > 0 ) {
				addNewLine(buf, " , ");
			} 
			addNewLine(buf, " {");
			addNewLine(buf, " text : '"+list.getString(i,"BTN_NM_"+language)+"', ");
			addNewLine(buf, " handler : "+list.getString(i,"BTNFUNC")+", ");
			addNewLine(buf, " iconCls : 'btn_"+list.getString(i,"BTN_CSS")+"', ");
			addNewLine(buf, " tooltip : '"+list.getString(i,"BTN_NM_"+language)+ " : " + list.getString(i,"TOOLTIP"+language)+"' ");
			addNewLine(buf, "}");
		}
		//addNewLine(buf, " ]");
		addNewLine(buf, " ]");
		
		out.print( buf.toString() );
	}

	public String getButtons() {
		return buttons;
	}
	public void setButtons(String buttons) {
		this.buttons = buttons;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}

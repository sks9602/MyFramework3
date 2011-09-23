package com.base.taglib;

import java.util.HashMap;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.dao.HoDaoImpl;
import project.jun.dao.parameter.HoQueryParameterHandler;
import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.dao.result.HoList;
import project.jun.dao.result.HoMapList;
import project.jun.taglib.TagLibSuperDB;
import project.jun.was.parameter.HoParameter;

public class TagLibInput extends TagLibSuperDB 
{
 
	private static final long serialVersionUID = 1L;
	 
	private String type = "";   // text, number, textarea, select_공통코드, checkbox_공통코드, radio_공통코드, popup, hidden, label, file, date, date2
	private String scope = "";	// param, model
	private String titleText = "";
	private String titleCode = "";
	private String titleArg = "";
	private String name = ""; // TABLE_NAME.COLUMN_NAME(min/max/number or text/length등의 기본적인 validation) or name, 
	private String event = ""; // change:f1, click:f2
	private String disabled = "";
	private String validation = ""; // 추가 validation
	private String model    = "";
	private String defaultValue = "";
	private String append = "";
	private String size = "";
	private String minValue = "Number.MIN_VALUE";
	private String maxValue = "Number.MAX_VALUE";
	
	// for select, checkbox or radio
	private String firstGbn = "";			// none, choice = "--선택--", all = "-- 전체 --", auto = "--자동--"
	private String codeColumn = "CODE";
	private String nameColumn = "CODE_NAME";

	private HoMapList columnList = null;

	public int doStart(WebApplicationContext ctx, JspWriter out) throws Exception 
	{
		StringBuffer buf = new StringBuffer();

		String parentType = super.getParent() == null ? null :  ((TagLibForm)super.getParent()).getType();

		HoList list = getList(ctx);
		
		columnList = list.toHoMapList("COLUMN_NAME");
		
		if( parentType == null ) {

			System.out.println(getName() + "  : " + getType());
			
			if( getType().toLowerCase().startsWith("text")) {
				printInputText(ctx, out, buf);
				
			} else if( getType().toLowerCase().startsWith("select")) {
				printSelect(ctx, out, buf);
				
			} 
		} else {
			
			
		}

		out.print( buf.toString() );

		return EVAL_BODY_INCLUDE;
	}

	public int doEnd(WebApplicationContext factory, JspWriter out) throws Exception 
	{

		return EVAL_PAGE;
	}
	
	
	public HoList getList(WebApplicationContext factory) {
		
		HoDaoImpl dao = getHoDaoImpl(factory);
		
		HoQueryParameterHandler handler  = new HoQueryParameterHandler(getHoParameter());
		
    	HoQueryParameterMap     value = handler.getForDetail();
									
    	value.put("TABLE_NAME", getTableName());

    	HoList list = null;
		try {
			list = dao.queryForList("MainDAO.selectTableColumnList", value);			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		return list;
	}


	/**
	 * <input type="text"> 를 만든다.
	 * @param queryProvider
	 * @param out
	 * @throws Exception
	 */
	public void printInputText(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		
		HoParameter  param = super.getHoParameter();

		
		addNewLine(buf, "  var "+ getName().toLowerCase()+" = new Ext.form.TextField({ ");
		addNewLine(buf, "             id : 'id_"+getName().toLowerCase()+"', ");
		addNewLine(buf, "             width : "+getSize()+", ");
		addNewLine(buf, "             maxLength : "+getMaxLength()+" ");
		addNewLine(buf, "            }); ");
		
	}
	
	/**
	 * <select>를 만든다.
	 * @param factory
	 * @param out
	 * @param buf
	 * @throws Exception
	 */
	public void printSelect(WebApplicationContext ctx, JspWriter out, StringBuffer buf ) throws Exception
	{
		
		HashMap options = new HashMap();

		HoList list = super.getTagLibUtil().getList(ctx, getGroupCode(), getHoRequest(), getHoParameter(), firstGbn, options);

		int startIdx = 1;
		
		addNewLine(buf, "  var "+ getName().toLowerCase()+" = new Ext.form.ComboBox({ ");
		addNewLine(buf, "             id : 'id_"+getName().toLowerCase()+"', ");
		addNewLine(buf, " displayField : '"+getNameColumn()+"',");
		addNewLine(buf, " valueField : '"+getCodeColumn()+"',");
		addNewLine(buf, " name: '"+name+"',");
		addNewLine(buf, " typeAhead: true,");
		addNewLine(buf, " mode: 'local',");
		addNewLine(buf, " forceSelection: false,");
		addNewLine(buf, " width: "+getSize()+",");
		
		if( firstGbn.equals("none")) {
			addNewLine(buf, " emptyText : '',");
		} else {
			addNewLine(buf, " emptyText : '"+list.get(1, getNameColumn())+"',");
			startIdx = 2;
		}
		
		addNewLine(buf, " disableKeyFilter : true,");
		addNewLine(buf, " selectOnFocus : true ,");
				
		addNewLine(buf, " value : '"+defaultValue+"' ,");
		addNewLine(buf, " store: new Ext.data.ArrayStore({ ");
		addNewLine(buf, " fields: [");
		addNewLine(buf,  list.getMetaDataString() );
		addNewLine(buf, " ],");
		addNewLine(buf, " data: ");
		addNewLine(buf, list.toJavascriptDataArray(startIdx) );
		addNewLine(buf, " }) ");
		addNewLine(buf, " })");		
	}
	

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getdefaultValue() {
		return defaultValue;
	}

	public void setdefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getType() {
		if( type.equals("")) {
			if( columnList != null ) {
				
				if( columnList.getString(this.getName(), 0, "DATA_TYPE").indexOf("NUMBER") > 0 ) {
					return "number";
				} else if( !columnList.getString(this.getName(), 0, "CODE").equals("") ) {
					return "select_"+columnList.getString(this.getName(), 0, "CODE");
				} else if(columnList.getString(this.getName(), 0, "DATA_TYPE").equals("DATE")) {
					return "date";
				} else {
					if(columnList.getString(this.getName(), 0, "DATA_TYPE").toUpperCase().indexOf("lob") >= 0 ) {
						return "textarea";
					} else if(columnList.getInt(this.getName(), 0, "DATA_LENGTH") > 256 ) {
						return "textarea";
					} else {
						return "text";
					}
				}
			} else {
				return "text";
			}
		} else {
			return type;
		}
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
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

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getTableName() {
		if( this.name.indexOf('.') > 0 ) {
			return name.split("\\.")[0];
		} else {
			return "";
		}
	}
	
	public String getName() {
		if( this.name.indexOf('.') > 0 ) {
			return name.split("\\.")[1];
		} else {
			return name;
		}
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}
	public String getSize() {
		if( size.equals("") ) {
			if( columnList == null ) {
				if(columnList.getInt(this.getName(), 0, "DATA_LENGTH") > 50 ) {
					String parentType = ((TagLibForm)super.getParent()).getType();
					if( parentType == null ) {
						return "400";
					} else {
						return "100";		
					}
				} else {
					return String.valueOf(columnList.getInt(this.getName(), 0, "DATA_LENGTH") * 8);
				}
			} else {
				return "100";
			}
		} else {
			return size;
		}
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getFirstGbn() {
		return firstGbn;
	}
	public void setFirstGbn(String firstGbn) {
		this.firstGbn = firstGbn;
	}
	public String getCodeColumn() {
		return codeColumn;
	}
	public void setCodeColumn(String codeColumn) {
		this.codeColumn = codeColumn;
	}
	public String getNameColumn() {
		return nameColumn;
	}
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}
	public String getMaxLength() {
		
		if( columnList == null ) {
			return "100";
		} else {
			if( columnList.getInt(this.getName(), 0, "DATA_LENGTH") > 0 ) {
				return columnList.getString(this.getName(), 0, "DATA_LENGTH");
			} else {
				return "100";
			}
		}
	}


	private String getGroupCode() {
		String groupCode = "";
		if( type.equals("")) {
			if( columnList != null ) {
				groupCode = columnList.getString(this.getName(), 0, "CODE");
			} else {
				groupCode = "--NONE--";
			}
		} else {
			// 공통코드로 combo검색조건을 를 만들 경우.
			if( type.toLowerCase().indexOf("select_".toLowerCase()) >= 0) {
				groupCode = type.substring(type.lastIndexOf("_"));
			} 
			// 공통코드 checkbox
			else if( type.toLowerCase().indexOf("checkbox_".toLowerCase())>= 0) {
				groupCode = type.substring(type.lastIndexOf("_"));
			}	
			// 공통코드 radio
			else if( type.toLowerCase().indexOf("radio_".toLowerCase())>= 0) {
				groupCode = type.substring(type.lastIndexOf("_"));
			}	
			// select다중선택
			else if( type.toLowerCase().indexOf("selectmulti_".toLowerCase())>= 0) {
				groupCode = type.substring(type.lastIndexOf("_"));
			}		
			
			// 공통코드로 검색조건을 를 만들 경우.
			else if( type.toLowerCase().indexOf("selectgroup_".toLowerCase())>= 0) {
				groupCode = type.substring(type.lastIndexOf("_"));
			} 
		}		
		
		return groupCode.toUpperCase();
	}
}

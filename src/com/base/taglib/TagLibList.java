package com.base.taglib;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.taglib.TagLibSuperDB;

public class TagLibList extends TagLibSuperDB 
{
 
	private static final long serialVersionUID = 1L;
	 
	private String type ="";
	private String model ="";
	private String titleCode ="";
	private String titleText ="";
	private String titleArg = "";
	private String table ="";
	private String column ="";
	private int width = 100;
	private String align = "";
	private String editor ="";
	private String renderer ="";
	private String sort  ="Y";
	private String hidden  ="N";
	private String expand  ="N";
	private String lock  ="N";
	private String resize  = "Y";
	private String event = "";
	private String altText = "";
	private String altCode = "";
	private String altArg = "";
	

	public int doStart(WebApplicationContext ctx, JspWriter out) throws Exception 
	{
		StringBuffer buf = new StringBuffer();
		
		String parentType = ((TagLibForm)super.getParent()).getType();
		
		if(getCurrentIndex(parentType) > 0 ) {
			addNewLine(buf, ",  ");
		} 
		
		if( type.startsWith("checkbox")) {
			doCheckbox(parentType, buf);
			
		} else if( type.startsWith("number")) {
			doRownum(parentType, buf);
			
		} else if( type.startsWith("expander")) {
			doExpander(parentType, buf);
			
		} else {
			doColumn(ctx, parentType, buf);
		}

		if( this.getExpand().equals("Y")) {
			super.setAttribute(parentType.toUpperCase()+"EXPAND", "id_"+parentType + "_" + this.getColumn());
		}
		out.print( buf.toString() );
		
		increaseIndex(((TagLibForm)super.getParent()).getType());
		
		return EVAL_BODY_INCLUDE;
	}

	public void doCheckbox(String parentType, StringBuffer buf) {
		addNewLine(buf, " sm"+type.replaceAll("checkbox", ""));
		
		super.setAttribute("USE_SM", "Y");
	}
	
	public void doRownum(String parentType, StringBuffer buf) {
		addNewLine(buf, " rm"+type.replaceAll("number", ""));
		
		super.setAttribute("USE_RM", "Y");
	}

	public void doExpander(String parentType, StringBuffer buf) {
		addNewLine(buf, " expander"+type.replaceAll("expander", ""));
		
		super.setAttribute("USE_EXPANDER", " expander"+type.replaceAll("expander", "") );
	}

	public void doColumn(WebApplicationContext ctx, String parentType, StringBuffer buf) {
		addNewLine(buf, "{  ");
		addNewLine(buf, "  header : '"+ this.getTitleText()+"', ");
		addNewLine(buf, "  id : 'id_" +parentType + "_" + this.getColumn()+"', ");
		addNewLine(buf, "  dataIndex : '"+ this.getColumn()+"', ");
		addNewLine(buf, "  width : "+ this.getWidth()+", ");
		addNewLine(buf, "  hidden : "+ this.getHidden()+", ");
		addNewLine(buf, "  align : 'center', ");
		addNewLine(buf, "  locked : "+ this.getLock()+", ");
		addNewLine(buf, "  renderer : "+ this.getRenderer()+", ");
		addNewLine(buf, "  sort   : "+ this.getSort()+", ");
		addNewLine(buf, "  resizable : "+ this.getResize()+", ");
		addNewLine(buf, "  tooltip   : '"+ (this.getSort().equals("true") ? "[정렬가능] " : "[정렬불가] ") + this.getAlt(ctx) + "' ");
		addNewLine(buf, "}  ");
		
	}
	
	public int doEnd(WebApplicationContext factory, JspWriter out) throws Exception 
	{

		return EVAL_PAGE;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getTitleArg() {
		return titleArg;
	}

	public void setTitleArg(String titleArg) {
		this.titleArg = titleArg;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getAlign() {
		return align.equals("") ? "center" : align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getRenderer() {
		return renderer.equals("") ? "renderAlignCenter" : renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public String getSort() {
		return sort.equals("N") ? "false" : "true";
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getHidden() {
		return hidden.equals("Y") ? "true" : "false";
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public String getLock() {
		return lock.equals("Y") ? "true" : "false";
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public String getResize() {
		return resize.equals("N") ? "false" : "true";
	}

	public void setResize(String resize) {
		this.resize = resize;
	}

	public String getAlt(WebApplicationContext ctx) {
		return getMessageTitle (ctx, altCode, altArg, altText);
	}
	
	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getAltCode() {
		return altCode;
	}

	public void setAltCode(String altCode) {
		this.altCode = altCode;
	}

	public String getAltArg() {
		return altArg;
	}

	public void setAltArg(String altArg) {
		this.altArg = altArg;
	}

}

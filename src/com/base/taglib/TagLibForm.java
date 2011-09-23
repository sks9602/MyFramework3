package com.base.taglib;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.dao.result.HoList;
import project.jun.taglib.TagLibSuperDB;


/**
 * Ext js용.
 * @author USER00
 *
 */
public class TagLibForm extends TagLibSuperDB {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type	  = "";   
	private String name  = ""; 
	private String enctype = "";
	private String width = "";
	private String renderTo = "";
	private String bodyStyle	  = "";
	private String action	  = "";
	private String target	  = "";
	private String model	  = "";
	private String view       = "";
	private String lock       = "N";
	
	int repeat = 0;
	
	public int doStart(WebApplicationContext ctx, JspWriter out) throws Exception {
		if( type.toLowerCase().equals("search")) {
			doStartSearchForm(out);

			return EVAL_BODY_INCLUDE;
		} else if( type.indexOf("grid") >= 0 ) {
			doStartGridForm(out);

			return EVAL_BODY_INCLUDE;
		} 
		return EVAL_BODY_INCLUDE;
	}
	
	
	public int doEnd(WebApplicationContext ctx, JspWriter out) throws Exception {
		if( type.toLowerCase().equals("search")) {
			doEndSearchForm(ctx, out);
			
			return EVAL_PAGE;
		} else if( type.indexOf("grid") >= 0  ) {
			doEndGridForm(ctx, out);
			
			return EVAL_PAGE;
		} 
		return EVAL_PAGE;
	}
	
	public int doAfterBody() {

		if( type.toLowerCase().equals("search")) {
			
			return SKIP_BODY;
		} else if( type.indexOf("grid") >= 0  ) {
			
			if( repeat-- > 0 ) {
				return EVAL_BODY_BUFFERED; 
			} else {
				return SKIP_BODY;
			}
		} 
		return SKIP_BODY;	
	}
	
	/**
	 * 검색 form 시작일 경우
	 * @param out
	 * @throws Exception
	 */
	public void doStartSearchForm(JspWriter out) throws Exception {
		StringBuffer buf = new StringBuffer();
		
		addNewLine(buf, "        var "+getRenderTo()+"form = new Ext.form.FormPanel({                                           ");
		addNewLine(buf, "        	unstyled: true,                                                                    ");
		addNewLine(buf, "            autoHeight: true,                                                                 ");
		addNewLine(buf, "            renderTo : '"+getRenderTo()+"',                        ");
		addNewLine(buf, "            name : '"+ getName() +"Form',                                ");
		addNewLine(buf, "            width   : "+ getWidth() +",                                     ");
		addNewLine(buf, "            bodyStyle: '"+ getBodyStyle() +"', ");
		addNewLine(buf, "            footer : false, ");
		addNewLine(buf, "            submit: function() {                                                              ");
		addNewLine(buf, "            	fs_Search("+getRenderTo()+"form , '"+ getTarget() +"');                                      ");
		addNewLine(buf, "            } ,                                                             ");
		addNewLine(buf, "            items   : [{xtype : 'fieldset',                                                   ");
		addNewLine(buf, "                		width : "+(getWidth()-10)+",                                           ");
		addNewLine(buf, "                        collapsible: true,                                                    ");
		addNewLine(buf, "                        title: '검색 조건',                                                   ");
		addNewLine(buf, "                        listeners : {scope : this, collapse : onFieldSetCollapse , expand : onFieldSetExpand } ,   ");
		addNewLine(buf, "                		items : [                                                              ");

		out.print( buf.toString() );
		
	}

	/**
	 * 검색 grid form 시작일 경우
	 * @param out
	 * @throws Exception
	 */
	public void doStartGridForm(JspWriter out) throws Exception {
		StringBuffer buf = new StringBuffer();
		
		System.out.println( " super.getHoModel() :" + super.getHoModel());
		System.out.println( " model :" + model);
		
		HoList list = super.getHoModel().getHoList(model);

		addNewLine(buf, " var "+getRenderTo()+"_store = new Ext.data.JsonStore({                         ");
		addNewLine(buf, "            root: 'datas',                                            ");
		addNewLine(buf, "            totalProperty: 'totalCount',                               ");
		addNewLine(buf, "            idProperty: 'threadid',                                    ");
		addNewLine(buf, "            remoteSort: true,                                          ");
		addNewLine(buf, "                                                                       ");
		addNewLine(buf, "            fields: ["+ list.getMetaDataString() +" ],                  ");

		addNewLine(buf, "                                                                       ");
		addNewLine(buf, "            proxy: new Ext.data.ScriptTagProxy({                       ");
		addNewLine(buf, "                url: '"+getAction()+"' ");
		addNewLine(buf, "                                                                       ");
		addNewLine(buf, "            })                                                         ");
		addNewLine(buf, "        });                                                            ");

		addNewLine(buf, "        var "+getRenderTo()+" = new Ext.grid.EditorGridPanel({  ");
		addNewLine(buf, "        	id : 'data_"+getRenderTo()+"',                 ");
		addNewLine(buf, "            store: "+getRenderTo()+"_store,                ");
		addNewLine(buf, "            clicksToEdit: 1,                ");
		addNewLine(buf, "            trackMouseOver:false,            ");
		addNewLine(buf, "            disableSelection:true,           ");
		addNewLine(buf, "            loadMask: true,                  ");
		addNewLine(buf, "            stripeRows: true,               ");
		addNewLine(buf, "            style: { marginLeft: '5px' },    ");
		if( getLock().equals("Y")) {
			addNewLine(buf, "        colModel: new Ext.ux.grid.LockingColumnModel ({   ");			
		} else {
			addNewLine(buf, "        colModel: new Ext.grid.ColumnModel({   ");			
		}
		addNewLine(buf, "            defaults: { width: 100,  sortable: true },   ");			
		addNewLine(buf, "            columns:[                        ");			

		out.print( buf.toString() );
		
	}

	
	/**
	 * 검색 form 종료일 경우
	 * @param out
	 * @throws Exception
	 */
	public void doEndSearchForm(WebApplicationContext ctx, JspWriter out) throws Exception {
		 
		StringBuffer buf = new StringBuffer();
		
		if( getCurrentIndex("SEARCH_TD") <= getSearchMaxTdCnt() ) {
			// addNewLine(buf, "]} ");	 // close compositefield
			addNewLine(buf, " ] } // 3");	
			
			initIndex("SEARCH_TD");
			initIndex("SEARCH_TD_CNT");
			increaseIndex("SEARCH_TR");
		}
		
		addNewLine(buf, " ] } ]  ");
		addNewLine(buf, " }); ");

		addNewLine(buf, "");
		addNewLine(buf, "");
		
		addNewLine(buf, " function fs_"+type+"FormValidation() { ");
		addNewLine(buf, " if ( !"+type+"form.form.isValid()) {                                                                                          ");
        addNewLine(buf, "   	for( var i=0 ; i < "+type+"form.form.items.length ; i++ ) {                                                             ");
        addNewLine(buf, "                                                                                                                      ");
		addNewLine(buf, " 		if( "+type+"form.form.items.itemAt(i).getXType() == 'compositefield' ) {                                                ");
		addNewLine(buf, " 			for( var j=0 ; j < "+type+"form.form.items.itemAt(i).items.length ; j++ ) {                                         ");
        addNewLine(buf, "                                                                                                                      ");
        addNewLine(buf, "                                                                                                                      ");
		addNewLine(buf, " 				if(!"+type+"form.form.items.itemAt(i).items.itemAt(j).isValid()) {                                              ");
		addNewLine(buf, " 					"+type+"form.form.items.itemAt(i).items.itemAt(j).focus();   ");
		addNewLine(buf, " 					Ext.get("+type+"form.form.items.itemAt(i).items.itemAt(j).getId()).frame(\"ff0000\", 2, { duration: 0.7 });   ");
		addNewLine(buf, " 						return false;   ");
		// addNewLine(buf, " 					alert( Ext.getCmp("+type+"form.form.items.itemAt(i).items.itemAt(j).getId()).invalidText );  ");
		addNewLine(buf, " 				}                                                                                                      ");
		addNewLine(buf, " 			}                                                                                                          ");
		addNewLine(buf, " 		} else {                                                                                                       ");
		addNewLine(buf, " 			if(!"+type+"form.form.items.itemAt(i).isValid()) {                                                                  ");
		addNewLine(buf, " 					"+type+"form.form.items.itemAt(i).focus();   ");
		addNewLine(buf, " 				Ext.get("+type+"form.form.items.itemAt(i).getId()).frame(\"ff0000\", 2, { duration: 0.7 });                       ");
		addNewLine(buf, " 						return false;   ");
		//addNewLine(buf, " 					alert( Ext.getCmp("+type+"form.form.items.itemAt(i).getId()).invalidText );  ");
		addNewLine(buf, " 			}                                                                                                          ");
		addNewLine(buf, " 		}                                                                                                              ");
        addNewLine(buf, "      }                                                                                                               ");
    	addNewLine(buf, " }                                                                                                                    ");
		addNewLine(buf, " return true;   ");
		addNewLine(buf, " } ");

		out.print( buf.toString() );
	}

	/**
	 * 검색 form 종료일 경우
	 * @param out
	 * @throws Exception
	 */
	public void doEndGridForm(WebApplicationContext ctx, JspWriter out) throws Exception {
		StringBuffer buf = new StringBuffer();

		addNewLine(buf, "            ]  ");
		addNewLine(buf, "            }), ");
		
		if( super.getAttribute("USE_SM")!=null && super.getAttribute("USE_SM").equals("Y")) {
			addNewLine(buf, " sm: sm, ");
		}

		if( super.getAttribute(type.toUpperCase()+"EXPAND")!=null) {
			addNewLine(buf, " autoExpandColumn: '"+super.getAttribute(type.toUpperCase()+"EXPAND")+"', ");
		}
		
		if( getLock().equals("Y")) {
			addNewLine(buf, "        view: new Ext.ux.grid.LockingGridView() ,  ");			
		} 
		addNewLine(buf, "            tbar: tbar,                         ");

		addNewLine(buf, "            bbar:                                                      ");
		addNewLine(buf, "new Ext.ux.PagingToolbar({                                              ");
		//addNewLine(buf, "        gridId : 'data_"+getRenderTo()+"',                             ");
		// addNewLine(buf, "        headerRowspan : headerRowspan,                                  ");
		addNewLine(buf, "        pageSize: 20,                                                   ");
		addNewLine(buf, "        store: "+getRenderTo()+"_store ,                                ");
		addNewLine(buf, "        displayInfo: true,                                              ");
		addNewLine(buf, "        displayMsg: '{0} - {1} of {2} Record(s)',                       ");
		addNewLine(buf, "        emptyMsg: '<font color=\"red\">No Record(s) to display</font>' ");
		//addNewLine(buf, "        paramObject : param,                                            ");
		//addNewLine(buf, "        paramNames : objParamArr                                        ");
		addNewLine(buf, "    })  ,                                                                ");       
		addNewLine(buf, "            plugins: [                                     ");
		
		int plugin = 0;
		addNewLine(buf, "            grid_fit   ");
		plugin ++;
		
		if (super.getAttribute("USE_EXPANDER")!= null ) {
			if( plugin > 0 ) {
				addNewLine(buf, " ,  ");
			}
			addNewLine(buf, super.getAttribute("USE_EXPANDER").toString() );
		}
		addNewLine(buf, "            ]                                     ");
		addNewLine(buf, "        });                                                         ");

		addNewLine(buf, " "+getRenderTo()+".render('"+getRenderTo()+"'); ");
		// addNewLine(buf, " "+getRenderTo()+"_store.load({params: "+ getTarget() +"form.getForm().getValues() }); ");
		
		out.print( buf.toString() );

	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return (name.equals("") ? type : name );
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEnctype() {
		return enctype;
	}


	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}


	public int getWidth() {
		return width.equals("") ? 1025 : Integer.parseInt(width);
	}


	public void setWidth(String width) {
		this.width = width;
	}


	public String getRenderTo() {
		if( !renderTo.equals("")) {
			return renderTo;
		} else {
			return this.type;
		}
	}


	public void setRenderTo(String renderTo) {
		this.renderTo = renderTo;
	}


	public String getBodyStyle() {
		return (bodyStyle.equals("") ? "padding: 0px; margin-right:5px;" : bodyStyle);
	}


	public void setBodyStyle(String bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	public String getAction() {
		if( action.equals("") ) {
			if(action.indexOf('?') > 0) { 
				return action.substring(0, action.indexOf('?'));
			} else {
				return action;
			}
		} else {
			return getRequestUri(getHoRequest());
		}
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}


	public String getView() {
		return view;
	}


	public void setView(String view) {
		this.view = view;
	}


	public String getLock() {
		return this.lock;
	}


	public void setLock(String lock) {
		this.lock = lock;
	}


}

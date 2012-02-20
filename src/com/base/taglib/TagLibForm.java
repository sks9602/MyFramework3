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
		
		String labelWidth = "90";
		
		addNewLine(buf, "        var "+getRenderTo()+"form = Ext.widget('form', {                                          ");
		addNewLine(buf, "        	unstyled: true,    border: false,   bodyBorder: false,   autoHeight: true,   bodyPadding: 0, ");
		addNewLine(buf, "            renderTo : '"+getRenderTo()+"',                        ");
		addNewLine(buf, "            name : '"+ getName() +"Form',                                ");
		addNewLine(buf, "            width   : "+ getWidth() +",                                     ");
		addNewLine(buf, "            bodyStyle: '"+ getBodyStyle() +"', ");
		addNewLine(buf, "            fieldDefaults: {         ");  
		addNewLine(buf, "            	labelAlign: 'right',  ");
		addNewLine(buf, "               msgTarget: 'qtip',   ");
		addNewLine(buf, "               labelWidth: "+labelWidth+"      ");
		addNewLine(buf, "            },                       ");
		addNewLine(buf, "            submit: function() {                                                              ");
		addNewLine(buf, "            	fs_Search("+getRenderTo()+"form , '"+ getTarget() +"');                                      ");
		addNewLine(buf, "            } ,                                                             ");
		addNewLine(buf, "            items   : [{xtype : 'uxfieldset',                                                   ");
		addNewLine(buf, "                		 width : "+(getWidth()-10)+",                                           ");
		addNewLine(buf, "                        collapsible: true,   layout: 'anchor',                                ");
		addNewLine(buf, "                        title: '검색 조건',                                                   ");
		addNewLine(buf, "                        listeners : { aftercollapse : { fn : onFieldSetCollapse } , afterexpand : { fn : onFieldSetExpand } } ,   ");
		addNewLine(buf, "                        defaults: {                                                            ");
		addNewLine(buf, "                                  anchor: '100%',                                              ");
		addNewLine(buf, "  			                       labelAlign: 'right',                                         ");
		addNewLine(buf, "                                  // hideLabel: false,                                         ");
		addNewLine(buf, "                                  labelWidth: "+labelWidth+" ,                                 ");
		addNewLine(buf, "                                  layout: {                                                    ");
		addNewLine(buf, "                                  		type: 'hbox',                                            ");
		addNewLine(buf, "                                  		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}   ");
		addNewLine(buf, "                         			}                                                            ");
		addNewLine(buf, "                        },                                                                 ");
		addNewLine(buf, "                		items : [                                                           ");

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
		System.out.println( " list :" + list );
		
		addNewLine(buf, "Ext.define('GridData', { ");
		addNewLine(buf, " 	extend: 'Ext.data.Model',");
		addNewLine(buf, " 	fields: ["+ list.getMetaDataString("name") +"]");
		addNewLine(buf, "}); ");
		

		addNewLine(buf, " var "+getRenderTo()+"_store = Ext.create('Ext.data.Store', {         ");
		addNewLine(buf, "            model: 'GridData',                                            ");
		addNewLine(buf, "            proxy: {                       ");
		addNewLine(buf, "                simpleSortMode: true,      ");
		addNewLine(buf, "                type: 'jsonp',             ");
		addNewLine(buf, "                api : {                    ");
		addNewLine(buf, "                      read   : '"+getAction()+"', ");
		addNewLine(buf, "                      create   : '"+getAction()+"' ");
		addNewLine(buf, "                } ,                        ");
		addNewLine(buf, "                reader : {                 ");
		addNewLine(buf, "                      root : 'datas', totalProperty: 'totalCount'  ");
		addNewLine(buf, "                } ,                        ");
		addNewLine(buf, "                writer : {                 ");
		addNewLine(buf, "                      root : 'datas', type: 'json', writeAllFields: false ");
		addNewLine(buf, "                } ,                        ");
		addNewLine(buf, "	             listeners: {                                              ");
		addNewLine(buf, "	                 exception: function(proxy, response, operation){      ");
		addNewLine(buf, "	                     Ext.MessageBox.show({                             ");
		addNewLine(buf, "	                         title: 'ERROR',                               ");
		addNewLine(buf, "	                         msg: operation.getError(),                    ");
		addNewLine(buf, "	                         icon: Ext.MessageBox.ERROR,                   ");
		addNewLine(buf, "	                         buttons: Ext.Msg.OK                           ");
		addNewLine(buf, "	                     });                                               ");
		addNewLine(buf, "	                 } ,                                                   ");
		addNewLine(buf, "	                 success: function(proxy, response, operation){        ");
		addNewLine(buf, "	                     Ext.MessageBox.show({                             ");
		addNewLine(buf, "	                         title: 'SUCCESS',                             ");
		addNewLine(buf, "	                         msg: operation.getError(),                    ");
		addNewLine(buf, "	                         icon: Ext.MessageBox.INFO,                   ");
		addNewLine(buf, "	                         buttons: Ext.Msg.OK                           ");
		addNewLine(buf, "	                     });                                               ");
		addNewLine(buf, "	                 } 	                                                   ");
		addNewLine(buf, "	             }                                                         ");		
		addNewLine(buf, "            }                                                          ");
		addNewLine(buf, "        });                                                            ");

		addNewLine(buf, "        var "+getRenderTo()+" = Ext.create('Ext.grid.Panel', {  ");
		addNewLine(buf, "        	id : 'data_"+getRenderTo()+"',                 ");
		addNewLine(buf, "            renderTo : '"+getRenderTo()+"',  ");
		addNewLine(buf, "            store: "+getRenderTo()+"_store,                ");
		//addNewLine(buf, "            trackMouseOver:false,            ");
		// addNewLine(buf, "            disableSelection:true,           ");
		addNewLine(buf, "            loadMask: true,                  ");
		//addNewLine(buf, "            stripeRows: true,               ");
		addNewLine(buf, "            height: 600,             ");
		addNewLine(buf, "            style: { marginLeft: '5px' },    ");
		if( getLock().equals("Y")) {
			// addNewLine(buf, "        colModel: new Ext.ux.grid.LockingColumnModel ({   ");			
		} else {
			//addNewLine(buf, "        colModel: Ext.create('Ext.grid.ColumnModel', {   ");			
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

		/*
		if( getCurrentIndex("SEARCH_TD") <= getSearchMaxTdCnt() ) {
			// addNewLine(buf, "]} ");	 // close compositefield
			addNewLine(buf, " ] } // 3");	
			
			initIndex("SEARCH_TD");
			initIndex("SEARCH_TD_CNT");
			increaseIndex("SEARCH_TR");
		}
		addNewLine(buf, " ] } ]  ");
		*/
		addNewLine(buf, " }); ");

		addNewLine(buf, "");
		
		addNewLine(buf, " function fs_"+type+"FormValidation() { ");
		addNewLine(buf, " if ( !"+type+"form.form.isValid()) {                                                                                          ");
        addNewLine(buf, "   	for( var i=0 ; i < "+type+"form.form.items.length ; i++ ) {                                                             ");
        addNewLine(buf, "                                                                                                                      ");
		addNewLine(buf, " 		if( "+type+"form.form.items.itemAt(i).getXType() == 'fieldcontainer' ) {                                                ");
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

		addNewLine(buf, "            ],  ");
		// addNewLine(buf, "            }), ");
		

		if( super.getAttribute(type.toUpperCase()+"EXPAND")!=null) {
			// addNewLine(buf, " autoExpandColumn: '"+super.getAttribute(type.toUpperCase()+"EXPAND")+"', ");
		}
		
		if( getLock().equals("Y")) {
			// addNewLine(buf, "        view: new Ext.ux.grid.LockingGridView() ,  ");			
		} 
		// addNewLine(buf, "            tbar: tbar,                         ");

		if( super.getAttribute("USE_SM")!=null && super.getAttribute("USE_SM").equals("Y")) {
			addNewLine(buf, " selModel : sm, ");
		}
		addNewLine(buf, "            bbar:  Ext.create('Ext.ux.GridPaging', {                     ");
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
		addNewLine(buf, "            grid_fit ");//, Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 1})   ");
		plugin ++;
		
		if (super.getAttribute("USE_EXPANDER")!= null ) {
			if( plugin > 0 ) {
				addNewLine(buf, " ,  ");
			}
			addNewLine(buf, super.getAttribute("USE_EXPANDER").toString() );
		}
		addNewLine(buf, "            ]                                     ");
		addNewLine(buf, "        });                                                         ");

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

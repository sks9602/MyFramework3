<%@ page language="java" contentType="text/html;  charset=utf-8"
	pageEncoding="utf-8"
%><%@include file="/jsp/common/include/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>#{PROJECT NAME} [<%= request.getRemoteAddr().substring(request.getRemoteAddr().lastIndexOf('.')+1) %>]</title>
		
		<link rel="stylesheet" type="text/css" href="<%= G_CONTEXTROOT %>/static/ext/extJs3.1/resources/css/ext-all.css" />
		<script>
			var G_MENU_ID = "<%=param.get("menu_id")%>";
		</script>
		
		<script type="text/javascript" src="<%= G_CONTEXTROOT %>/static/ext/extJs3.1/ext-base-debug.js" ></script>
		<script type="text/javascript" src="<%= G_CONTEXTROOT %>/static/ext/extJs3.1/ext-all-debug.js" ></script>
		<script type="text/javascript" src="<%= G_CONTEXTROOT %>/static/ext/extJs3.1/ux/TabCloseMenu.js" ></script>
		<script type="text/javascript" src="<%= G_CONTEXTROOT %>/static/ext/extJs3.1/ux/multidom.js" ></script>
		<script type="text/javascript" src="<%= G_CONTEXTROOT %>/static/ext/extJs3.1/ux/mif.js" ></script>
			
		<script>

<%
	HoMap menuInfo = model.getHoMap("menuInfo");
%>

     var tabDetails = null;

/*
	tabDetails.on("tabchange", function(_tp, _p) {
		fs_CallIFrameFunction( "fs_RemoveShadow", _p.getId().replace(/mainFrame/,"").replace(/_/,"") )
	});
*/
	Ext.onReady(function(){
		Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		
		var viewport = new Ext.Viewport({
			layout : 'border',
			items:[tabDetails = new Ext.TabPanel({
				border: false,
				activeTab:0,
				id : 'tabDetails',
				region    :'center',
				// height : 800,
				enableTabScroll:true,
				margins:'0 0 0 0',
				tabPosition:'top', //'bottom', top
				defaults: {autoScroll:true},
				layoutOnTabChange: true,
				autoScroll:true,
	        	plugins: new Ext.ux.TabCloseMenu(),
				items: { id: 'mainFrame_'+G_MENU_ID,
			             xtype : 'iframepanel',
			             title: '<%= param.get("link").equals("") ? "메인화면" : "--" + menuInfo.getStringForHtml("MENU_NAME")%>',
			             closable : false,
			             defaultSrc : '<%= param.get("link")%>' 
						}
	     })]
		});

		new Ext.KeyMap(Ext.getDoc(), {
			key: '123s',
			alt: true,
			handler: function(k, e) {
				var t = Ext.getCmp('tabDetails');
				switch(k) {
					case 49:
						t.setActiveTab(0);
						break;
					case 50:
						t.setActiveTab(1);
						break;
					case 51:
						t.setActiveTab(2);
						break;
				}			
			},
			stopEvent: true
		});		
		tabDetails.doLayout(); 
	});





	/**
	* Tab을 삭제한다.
	*/
	function fs_RemoveTab(tabIdx) {
		// var tabDetails = Ext.getCmp('tabDetails');

		if( tabIdx && tabIdx !=0 ) {
			var iFrame = "mainFrame_"+tabIdx;
			
			var iframeObj = Ext.get(iFrame).child('iframe', true) ;
			// eval("Ext.getDom('"+iframeObj.id+"').contentWindow.fs_RemoveInnoDS();"); 
		}
	
		tabDetails.remove(Ext.getCmp("mainFrame_"+tabIdx));
	}
	

	/**
	 * 특정 탭을 활성화 시키기.
	 */
	function fs_ActiveTab(tabIdx) {
		if( tabIdx && tabIdx !=0 ) {
			if( Ext.get('mainFrame_'+G_MENU_ID +"_"+tabIdx) ) {
				//tabDetails.setActiveTab('mainFrame_'+tabIdx);
			}
		} else {
			tabDetails.setActiveTab('mainFrame_'+G_MENU_ID);
		}
	}
	
	/**
	 * 상세 탭을 만든다.
	 */
	function fs_AddTab( tabTitle, tabIdx, url, params) {
		// var tabDetails = Ext.getCmp('tabDetails');
		var iframeTab;
		if( Ext.get('mainFrame_'+tabIdx) ) {
		    iframeTab = Ext.getCmp('mainFrame_'+tabIdx);
			//iframe.setSrc.defer(350, iframe, ["/hrdb/hrd/back/common/popup.do?p_action_flag=r_course_list_json_view&menu_id=" , true]);
			iframeTab.setTitle( tabTitle );
			
		} else {
			var iframe = {
				id: 'mainFrame_'+tabIdx,
			     title: tabTitle,
			     xtype:'iframepanel',
			     //loadMask : true,
			     //autoLoad: {url:url , params : params , method:'GET'},
			     method :'POST',
			     closable : true
			     //defaultSrc: tabSrc
			}; 
			tabDetails.add(iframe);
			
			iframeTab = Ext.getCmp('mainFrame_'+tabIdx);
		}
		
		tabDetails.setActiveTab(iframeTab);
		
		iframeTab.setSrc(url + "?" + new WizHash(params).toQueryString());
	}

	/*
	* iframe에 포함된 function호출
	*/
		function fs_CallIFrameFunction( functionName, tabIdx ) {
			var iFrame = "mainFrame_";
			iFrame += tabIdx ;

			var arg = "";
			
			if( arguments.length == 3 ) {
				for( var i=0 ;i<arguments[2].length ;i++ ) {
					if( i!=0 ) {
						arg += ',';
					}
					if( typeof arguments[2][i] == "object" ) {
						// arg += arguments[2][i];
					} else {
						arg += ("'"+arguments[2][i]+"'");
					}
				}
			}
			
			var iframeObj = Ext.get(iFrame).child('iframe', true) ;
			if( Ext.getDom(iframeObj.id) && eval("Ext.getDom('"+iframeObj.id+"').contentWindow."+functionName ) ) {
				try {
					eval("Ext.getDom('"+iframeObj.id+"').contentWindow."+functionName+"("+arg+");"); 
				} catch(E) {
					error(E.description);
				}
			}
		}
		
		function log(msg) {
			parent.log('INFO', tabDetails.getActiveTab().title, G_MENU_ID, msg);
		}
		
		function error(msg) {
			parent.log('ERROR', tabDetails.getActiveTab().title, G_MENU_ID, msg);
		}

		function fs_MenuSelect(menu_id, url ) {
			parent.fs_MenuSelect(menu_id, url);
		}
	</script>
	</head>
	<body>
		<div></div>
	</body>
</html>

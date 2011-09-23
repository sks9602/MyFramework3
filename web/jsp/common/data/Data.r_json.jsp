
<%@ page
	contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	session="true"
	import="java.util.*"
	import="java.sql.Types.*"
	import="project.jun.dao.result.HoList"
	import="project.jun.util.HoUtil"
	import="org.apache.ibatis.metadata.result.MetaData"
%><%@include file="/jsp/common/include/include.jsp"
%><%
	boolean scriptTag = false;
	String cb = param.get("callback");
	if (!cb.equals("")) {
	    scriptTag = true;
	    response.setContentType("text/javascript");
	    response.setHeader("Content-Type", "charset=utf-8");
	} else {
	    response.setContentType("application/x-json");
	    response.setHeader("Content-Type", "charset=utf-8");
	}
	if (scriptTag) {
	    out.write(cb + "(");
	}

	long cnt = model.getLong("TOTAL_CNT");

	HoList list = model.getHoList("JSON_DATAS");
	
	MetaData md = (list!=null ? list.getMetaData() : null );

%>{"totalCount":"<%= cnt %>","datas":[<% 
                                  for( int i=0 ; list != null && i<list.size() ; i++ ) {
                                	  out.write("\r\n");
										if( i!=0 ) { %>,<%} %>{<% 
										for( int j=0 ; j<md.getColumnCount() ; j++ ) {
											//System.out.println(WizUtil.toJsonString(list.getString(i, md.getColumnName(j))));
									%><% if( j!=0 ) { %>,<%} %>"<%= md.getColumnName(j) %>":"<%= md.getColumnType(j) == java.sql.Types.CLOB  ? HoUtil.toJsonString(HoUtil.getStringForCLOB((java.sql.Clob)list.get(i, md.getColumnName(j))).replaceAll("\r\n","").replaceAll("'","").replaceAll("\"","")) : HoUtil.toJsonString(list.getString(i, md.getColumnName(j)).replaceAll("\r\n","").replaceAll("'","")) %>"<% 
										} 
									%>}<% } %>]}<%
%><%
if (scriptTag) {
    out.write(");");
}
%>

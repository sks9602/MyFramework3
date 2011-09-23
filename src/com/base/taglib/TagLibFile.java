package com.base.taglib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.jsp.JspWriter;

import org.springframework.web.context.WebApplicationContext;

import project.jun.dao.HoDaoImpl;
import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.dao.result.HoList;
import project.jun.taglib.TagLibSuperDB;
import project.jun.was.config.HoConfig;

public class TagLibFile extends TagLibSuperDB 
{

	private static final long serialVersionUID = 1L;
	private String TAG_LIB_FILE  = "com.base.taglib.HoMulitIndex";
	private String TAG_LIB_FIRST = "com.base.taglib.HoMulitFirst";
	 
	private String type = "";   // upload = 업로드, download = 다운로드
	private String companyCd = "";
	private String fileGroupNo      = "";
	private String maxTotalSize = "100";
	private String maxFileSize = "100";
	private String maxFileCnt = "5";
	private String width   = "500";
	private String height   = "100";
	private String button   = "Y";
	private String extension = "";  // default : ppt,xls,doc,hwp,wri,txt,pdf,gul,wmv,asf,avi,mov,jpg,gif,bmp,zip
	private String folderUpload = "N";
	private Map    paramMap          = new HashMap();
	private String gbn      = ""; // course, contents
	private String folderName = "data";
	
	public int doStart(WebApplicationContext ctx, JspWriter out) throws Exception 
	{

		return EVAL_BODY_INCLUDE;
	
	}

	public int doEnd(WebApplicationContext factory, JspWriter out) throws Exception {
		HoList list = getList(factory);

		printFileInfo(list, out);
		
		return EVAL_PAGE;
	}

	/*
	 * 
	 */
	public HoList getList(WebApplicationContext factory) throws NamingException, SQLException {
		
		HoDaoImpl dao = getHoDaoImpl(factory);

    	HoQueryParameterMap value = new HoQueryParameterMap();

    	value.add("COMPANY_CD", companyCd);
    	value.add("FILEGRP_NO", fileGroupNo);
    	
    	HoList list = (HoList) super.getHoRequest().getRequest().getAttribute("com.base.taglib.TagLibFile."+ fileGroupNo.toUpperCase());
		if( list == null) {
			list = dao.queryForList("FileUpload.selectAttachFileList", value);
		}
		
		return list;
	}
	
	/**
	 * TagLib
	 * @param queryProvider
	 * @param out
	 * @throws Exception
	 */
	public void printFileInfo(HoList list, JspWriter out ) throws Exception
	{

    	HoConfig hoConfig = super.getHoConfig();
		StringBuffer buf = new StringBuffer();

		
		// 업로드
		if( type.toLowerCase().equals("upload") ) {
			increaseMulitIndex();
			
			buf.append("	<input type=\"hidden\" name=\"filegrp_no"+getMulitIndex()+"\" value=\""+fileGroupNo+"\"/>\r\n");
			buf.append("	<input type=\"hidden\" name=\"fileUploadIdx\" value=\""+getMulitIndex()+"\"/>\r\n");

			buf.append("	<div id=\"id_innoDS_Uploader"+getMulitIndex()+"\" style=\"vertical-align:top\">\r\n");
			buf.append("		<span style=\"height:"+height+";width:"+width+";\">\r\n");
			buf.append("			<script type=\"text/javascript\" language=\"JavaScript\">");

			buf.append("			<!--\r\n");

			if( !gbn.equals("")) {
				buf.append("				ActionFilePath = \""+ this.getHoRequest().getRequest().getContextPath() +"/jsp/base/system/common/upload/action_file_"+gbn.toLowerCase()+".jsp\"; \r\n");
			} 
			buf.append("				<!-- 이미 등로된 파일 목록 조회 하는 기능. -->\r\n");
			buf.append("				function OnInnoDS"+getMulitIndex()+"Load() {\r\n");

			// 
			if(paramMap!=null) {
				Set keySet = paramMap.keySet();
				
				Iterator it = keySet.iterator();
				
				String key = "";
				
				while( it.hasNext() ) {
					key = (String) it.next();
					
					buf.append("				document.InnoDS"+getMulitIndex()+".AppendPostData('"+key+"', '"+paramMap.get(key).toString()+"'); \r\n");
					
				}
			}
			
			for( int i=0 ; i<list.size() ; i++ ) { 
				buf.append("			     	document.InnoDS"+getMulitIndex()+".AddTempFile(\""+hoConfig.getDirFileUpload()+folderName+"\\\\"+list.getString(i, "FILE_NO")+"\\\\"+list.getString(i, "FILE_NAME")+"\", "+list.getString(i, "FILE_SIZE")+", \""+hoConfig.getDirFileUpload()+"data\\\\"+list.getString(i, "FILE_NO")+"\\\\"+list.getString(i, "FILE_NAME")+"\");\r\n");
				buf.append("			     	g_ExistFiles[\""+getMulitIndex() +":"+ hoConfig.getDirFileUpload()+folderName+"\\\\"+list.getString(i, "FILE_NO")+"\\\\"+list.getString(i, "FILE_NAME")+"\"] = false;\r\n");
				
			} 
			buf.append("				}\r\n");
			
			buf.append("				<!-- 화면 설정. -->\r\n");
			if( this.folderUpload.equals("Y")) {
				buf.append("				FolderUpload = \"type2\";\r\n");
			}
			buf.append("				InnoDSInitMulti("+maxTotalSize+", "+maxFileSize+", "+maxFileCnt+", \""+width+"\", \""+height+"\", \"InnoDS"+getMulitIndex()+"\");\r\n");
			buf.append("			//-->\r\n");
			buf.append("			</script>\r\n");
			
			if( getExtension().length() > 0) {
				buf.append("			<!-- 업로드 가능한 확장자 설정 (* 제한없는 경우 사용하지 말것..) -->\r\n");
				buf.append("			<script for=\"InnoDS"+getMulitIndex()+"\" event=\"OnBeforeAddFile(strFileName);\">\r\n");
				buf.append("			<!--\r\n");
				String [] exts = getExtension().split(",");
				StringBuffer extStr = new StringBuffer();
				for( int i=0 ; i<exts.length ; i++) {
					if( i!=0 ) {
						extStr.append(",");
					}
					extStr.append("\""+ exts[i].replaceAll(" ", "") + "\"");
				}
				buf.append("				return InnoDS_CheckExt(Array("+extStr+"), strFileName);\r\n");
				buf.append("			//-->\r\n");
				buf.append("			</script>\r\n");		
			}
			buf.append("		</span>\r\n");
			if( button.equals("Y")) {
				buf.append("		<span style=\"height:"+height+"px;vertical-align:absmiddle;padding-top:"+Integer.parseInt(height.replaceAll("px", "").replaceAll("%", ""))/2+"px\">\r\n");
				if( this.folderUpload.equals("Y")) {
					buf.append("			<input type=\"button\" value=\"Find Folder\" onClick=\"document.InnoDS"+getMulitIndex()+".OpenFolder();\"> <br/>\r\n");
				}
				buf.append("			<input type=\"button\" value=\"Find File\" onClick=\"document.InnoDS"+getMulitIndex()+".OpenFile();\"> <br/>\r\n");
				buf.append("			<input type=\"button\" value=\"Delete File\" onClick=\"document.InnoDS"+getMulitIndex()+".RemoveSelectedFiles();\"> \r\n");
				buf.append("		</span>\r\n");
			}
			if( getExtension().length() > 0 ) {
				buf.append("            <div>* allowed extension : " + getExtension() + "</div>\r\n");
			}
			buf.append("	</div>\r\n");

			//
			setFirstFalse();
			
			gbn = "";
			paramMap = new HashMap();
			folderUpload = "N";
			extension = "";
		} else {
			buf.append("	<div style=\"vertical-align:top\">\r\n");
			for(int i=0 ; i< list.size();i++) {
				if( !list.getString(i,"FILE_NAME").equals("") ) {
					buf.append("		<a HREF=\""+ this.getHoRequest().getRequest().getContextPath() +"/system/fileDownload.do?p_action_flag=download&company_cd="+list.getString(i,"COMPANY_CD")+"&filegrp_no="+list.getString(i,"FILEGRP_NO")+"&file_no="+list.getString(i,"FILE_NO")+"&file_idx="+ list.getString(i,"FILE_IDX")+"\" target=\"iFrameTemp\">"+list.getString(i,"FILE_NAME")+" ("+list.getCurrencyFormat(i,"FILE_SIZE")+")byte</a><br>");
				}
			}
			buf.append("	</div>\r\n");
		}
		
		out.print( buf.toString() );
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private String getMulitIndex() {
		Integer idx = (Integer) super.getHoRequest().getRequest().getAttribute( TAG_LIB_FILE );
		if( idx == null ) {
			return "1";
		} else {
			return String.valueOf(idx.intValue());
		}
	}
	
	/**
	 * 멀티 일경우 에는 Index를 증가시킨다.
	 */
	private void increaseMulitIndex() {
		Integer idx = (Integer) super.getHoRequest().getRequest().getAttribute( TAG_LIB_FILE );
		if( idx == null ) {
			
			super.getHoRequest().getRequest().setAttribute(TAG_LIB_FILE, new Integer(1)  );
		} else {
			super.getHoRequest().getRequest().setAttribute(TAG_LIB_FILE, new Integer(idx.intValue()+1)  );
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	private void setFirstFalse() {
		super.getHoRequest().getRequest().setAttribute( TAG_LIB_FIRST , new Boolean(false) );
	}	
	
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getFileGroupNo() {
		return fileGroupNo;
	}
	public void setFileGroupNo(String fileGroupNo) {
		this.fileGroupNo = fileGroupNo;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getMaxFileCnt() {
		return maxFileCnt;
	}

	public void setMaxFileCnt(String maxFileCnt) {
		this.maxFileCnt = maxFileCnt;
	}

	public String getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public String getMaxTotalSize() {
		return maxTotalSize;
	}

	public void setMaxTotalSize(String maxTotalSize) {
		this.maxTotalSize = maxTotalSize;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompanyCd() {
		return companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}

	public String getExtension() {
		return extension.equals("") ? "ppt,xls,doc,hwp,wri,txt,pdf,gul,wmv,asf,avi,mov,jpg,gif,bmp,zip" : extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public boolean isMulti() {
		return false;
	}

	public void setIsMulti(boolean isMulti) {
		//this.isMulti = isMulti;
	}

	public String getFolderUpload() {
		return folderUpload;
	}

	public void setFolderUpload(String folderUpload) {
		this.folderUpload = folderUpload;
	}

	public Map getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}

	public String getGbn() {
		return gbn;
	}

	public void setGbn(String gbn) {
		this.gbn = gbn;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}

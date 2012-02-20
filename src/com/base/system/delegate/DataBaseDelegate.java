package com.base.system.delegate; 

import java.sql.SQLException;

import javax.naming.NamingException;

import project.jun.dao.HoDaoImpl;
import project.jun.dao.parameter.HoQueryParameterHandler;
import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.dao.result.HoList;
import project.jun.dao.result.HoMap;
import project.jun.delegate.HoPageNavigation;
import project.jun.was.result.exception.HoException;

public class DataBaseDelegate extends ProjectDelegate {
	
	
	public void init() throws NamingException, SQLException, HoException {
		
		HoDaoImpl dao = super.getHoDaoImpl();
		
		HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);
		 
		HoQueryParameterMap value = hqph.getForSearch();
				
		HoList list = dao.queryForList("DataBase.selectTableList", value);

		param.getHoRequest().setSessionObject("PAGE_ROW_CNT", "50");
				
		model.put("list", list);
	}

	
	public void list() throws NamingException, SQLException, HoException  {
		
		HoDaoImpl dao = super.getHoDaoImpl(); //"DbDao");
		HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);
		 
		HoQueryParameterMap value = hqph.getForSearch();
		
		HoMap listCnt = dao.queryForInfo("DataBase.selectTableListCnt", value);
		
		long pageNo     = hqph.getPageNo();
		long pageRowCnt = hqph.getPageRowCnt();

		
        long totCnt = listCnt.getLong("CNT");


        // 페이지 네비게이션
        HoPageNavigation nav = new HoPageNavigation(param);
        nav.setNavigationInfo(totCnt, pageRowCnt, pageNo);

        // 페이지범위 설정!!
        long beginRowNum = nav.getBeginRowNum();
        long endRowNum   = nav.getEndRowNum();
        
        value.put("BEGINROWNUM", beginRowNum);
        value.put("ENDROWNUM", endRowNum);
		
        
        HoList list = dao.queryForList("DataBase.selectTableList", value);
        
		model.put("TOTAL_CNT", totCnt);
		model.put("JSON_DATAS", list);
	}
	
	public void detail() throws NamingException, SQLException, HoException  {
		
		HoDaoImpl dao = super.getHoDaoImpl();
		HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);
		
		HoQueryParameterMap value = hqph.getForDetail();

		        
		HoList ColumnList = dao.queryForList("DataBase.selectColumnList", value);
		HoList ConstraintsList = dao.queryForList("DataBase.selectConstraintsList", value);
        
		model.put("ds_ColumnList", ColumnList );	 
		model.put("ds_ConstraintsList", ConstraintsList );	 
	}

	public void joinTable() throws NamingException, SQLException, HoException  {
		
		HoDaoImpl dao = super.getHoDaoImpl();
		HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);
		
		HoQueryParameterMap value = hqph.getForDetail();
		        
        HoList ColumnList = dao.queryForList("DataBase.selectColumnList", value);
        HoList ConstraintsList = dao.queryForList("DataBase.selectConstraintsList", value);
        HoList TableAsList = dao.queryForList("DataBase.selectJoinTableAlias", value);
        
        model.put("ds_ColumnList", ColumnList );	 
        model.put("ds_ConstraintsList", ConstraintsList );	 
        model.put("ds_TableAsList", TableAsList );	 
	}

	public void join() throws NamingException, SQLException, HoException  {
		
		HoDaoImpl dao = super.getHoDaoImpl();
		
		HoQueryParameterHandler hqph = new HoQueryParameterHandler(param);
		
		HoQueryParameterMap value = hqph.getForDetail();
		 		
		value.copy(param);
		        
        HoList ColumnList = dao.queryForList("DataBase.selectColumnListForJoin", value);
        HoList ConstraintsList = dao.queryForList("DataBase.selectConstraintsList", value);
        HoList JoinColumnList = dao.queryForList("DataBase.selectJoinColumnList", value);
        HoMap     MaxColumnLength = dao.queryForInfo("DataBase.selectJoinTableMaxColumnLength", value);
        HoList TableAsList = dao.queryForList("DataBase.selectJoinTableAliasFrom", value);
        HoList SubQueryColumnList = dao.queryForList("DataBase.selectSubQueryColumnList", value);
        
        
        
        model.put("ds_ColumnList", ColumnList );	 
        model.put("ds_ConstraintsList", ConstraintsList );	 
        model.put("ds_JoinColumnList", JoinColumnList );	 
        model.put("ds_MaxColumnLengthInfo", MaxColumnLength );	 
        model.put("ds_TableAsList", TableAsList );	 
        model.put("ds_SubQueryColumnList", SubQueryColumnList );	 
	}

		
}

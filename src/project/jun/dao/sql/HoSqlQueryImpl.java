package project.jun.dao.sql;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlQuery;

public class HoSqlQueryImpl extends SqlQuery {

	public HoSqlQueryImpl(){
		super();
	}


	public HoSqlQueryImpl(DataSource ds, String sql){
		super(ds, sql);
	}
	
	public List queryForList() {
		//logger.info(getSql());

		return execute();
	}

	public List queryForList(int i) {
		declareParameter(new SqlParameter(Types.INTEGER));			

		//logger.info(getSql());

		return execute(i);
	}

	public List queryForList(long l) {
		
		declareParameter(new SqlParameter(Types.BIGINT));			

		//logger.info(getSql());

		return execute(l);
	}

	public List queryForList(String s) {
		
		declareParameter(new SqlParameter(Types.VARCHAR));			

		//logger.info(getSql());

		return execute(s);
	}

	public List queryForList(Object[] obj) {
		
		for( int i=0 ; obj!=null && i<obj.length ; i++ ) {
			if( obj[i] instanceof Integer ) {
				declareParameter(new SqlParameter(Types.INTEGER));
			} else if( obj[i] instanceof Long ) {
				declareParameter(new SqlParameter(Types.BIGINT));			
			} else {
				declareParameter(new SqlParameter(Types.VARCHAR));			
			}
		}
		//logger.info(getSql());
		return execute(obj);
	}

	public List queryForList(Map params) {
		/*
		Set keySet = params.keySet();
		Iterator it = keySet.iterator();
		String key = null;
		while (it.hasNext()) {
			key = (String) it.next();
			if( params.get(key) instanceof Integer ) {
				declareParameter(new SqlParameter(Types.INTEGER));
			} else if( params.get(key) instanceof Long ) {
				declareParameter(new SqlParameter(Types.BIGINT));			
			} else {
				declareParameter(new SqlParameter(Types.VARCHAR));			
			}
		}
		*/
		//logger.info(getSql());

		return executeByNamedParam(params);
	}

	public Object queryForObject(int i) {
		
		declareParameter(new SqlParameter(Types.INTEGER));			

		//logger.info(getSql());	

		return findObject(i);
	}

	public Object queryForObject(long l) {
		
		declareParameter(new SqlParameter(Types.BIGINT));			

		//logger.info(getSql());

		return findObject(l);
	}

	public Object queryForObject(String s) {
		
		declareParameter(new SqlParameter(Types.VARCHAR));			

		//logger.info(getSql());

		return findObject(s);
	}

	public Object queryForObject(Object[] obj) {
		//logger.info("++++++++++++++++++++++++++++++++++++lkjflsdjlf");
		
		for( int i=0 ; obj!=null && i<obj.length ; i++ ) {
			if( obj[i] instanceof Integer ) {
				declareParameter(new SqlParameter(Types.INTEGER));
			} else if( obj[i] instanceof Long ) {
				declareParameter(new SqlParameter(Types.BIGINT));			
			} else {
				declareParameter(new SqlParameter(Types.VARCHAR));			
			}
		}

		//logger.info(getSql());

		return findObject(obj);
	}

	public Object queryForObject(Map map) {

		return executeByNamedParam(map);
	}	
	
	public RowMapper newRowMapper(Object[] obj, Map map) {
		// TODO Auto-generated method stub
		if( obj!=null ) {
			for( int i=0 ; i<obj.length ; i++) {
				System.out.println("WizSqlQuery Object " +i+ " : " + obj[i]);
			}
		}
		logger.debug("WizSqlQuery Map " + map);
		
		return new ColumnMapRowMapper();
	}

}

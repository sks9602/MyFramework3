package project.jun.dao.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class HoSqlRawClient {
	private DataSource dataSource = null;
	private HoSqlQueryImpl  sqlQuery = null;
	private HoSqlUpdateImpl sqlUpdate = null;
	
	public HoSqlRawClient(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public List queryForList(String sql) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForList();
	}
	
	public List queryForList(String sql, int i) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForList(i);
	}

	public List queryForList(String sql, long l) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForList(l);
	}

	
	public List queryForList(String sql, String s) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForList(s);
	}

	public List queryForList(String sql, Object[] obj) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForList(obj);
	}

	public List queryForList(String sql, Map map) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForList(map);
	}
	
	public Object queryForObject(String sql, int i) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);

		return sqlQuery.queryForObject(i);
	}

	public Object queryForObject(String sql, long l) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);

		return sqlQuery.queryForObject(l);
	}

	public Object queryForObject(String sql, String s) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);

		return sqlQuery.queryForObject(s);
	}

	public Object queryForObject(String sql, Object[] obj) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);

		return sqlQuery.queryForObject(obj);
	}

	public Object queryForObject(String sql, Map map) {
		sqlQuery = new HoSqlQueryImpl(this.dataSource, sql);
		
		return sqlQuery.queryForObject(map);
	}

	
	public int update(String sql) {
		sqlUpdate = new HoSqlUpdateImpl(this.dataSource, sql);
		return sqlUpdate.update();
	}
	
	public int update(String sql, int i) {
		sqlUpdate = new HoSqlUpdateImpl(this.dataSource, sql);
		
		return sqlUpdate.update(i);
	}
	
	public int update(String sql, long l) {
		sqlUpdate = new HoSqlUpdateImpl(this.dataSource, sql);
		
		return sqlUpdate.update(l);
	}
	
	public int update(String sql, String str) {
		sqlUpdate = new HoSqlUpdateImpl(this.dataSource, sql);
		
		return sqlUpdate.update(str);
	}
	
	public int update(String sql, Object[] obj) {
		sqlUpdate = new HoSqlUpdateImpl(this.dataSource, sql);
		
		return sqlUpdate.update(obj);
	}

	public int update(String sql, Map obj) {
		sqlUpdate = new HoSqlUpdateImpl(this.dataSource, sql);
		
		return sqlUpdate.update(obj);
	}


	public HoSqlBatchImpl createBatch(String sql, ArrayList value) {
		return new HoSqlBatchImpl(this.dataSource, sql, value);
	}

	public int batch(HoSqlBatchImpl sqlBatch, String sql, ArrayList value) {
		if( value == null ) {
			return 0;
		}
		int result = sqlBatch.update(value);
		
		return result;
	}
}

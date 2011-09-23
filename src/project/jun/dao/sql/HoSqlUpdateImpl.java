package project.jun.dao.sql;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class HoSqlUpdateImpl extends SqlUpdate {

	public HoSqlUpdateImpl(){
		super();
	}


	public HoSqlUpdateImpl(DataSource ds, String sql){
		super(ds, sql);
	}
	
	public int update() {
		//logger.info(getSql());

		return update();
	}

	public int update(int i) {
		declareParameter(new SqlParameter(Types.INTEGER));			

		//logger.info(getSql());

		return update(i);
	}

	public int update(long l) {
		
		declareParameter(new SqlParameter(Types.BIGINT));			

		//logger.info(getSql());

		return update(l);
	}

	public int update(String s) {
		
		declareParameter(new SqlParameter(Types.VARCHAR));			

		//logger.info(getSql());

		return update(s);
	}

	public int update(Object[] obj) {
		
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
		return super.update(obj);
	}

	public int update(Map map) {
		
		//logger.info(getSql());
		return super.updateByNamedParam(map);
	}

}

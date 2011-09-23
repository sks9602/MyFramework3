package project.jun.dao.sql;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

public class HoSqlBatchImpl extends BatchSqlUpdate {

	public HoSqlBatchImpl(DataSource ds, String sql, List value){
		super(ds, sql);
		parseParameter(value);
	}


	public void parseParameter( List value){

		Object obj = null;
		if( value!=null && value.size() > 0 ) {
			obj = value.get(0);
		}
		
		Object  [] paramValue = null;
		
		if( obj instanceof String ) {
			paramValue = new String[] { (String)obj };
		} else if ( obj instanceof String [] ) {
			paramValue = (String []) obj;
		} else if ( obj instanceof List ) {
			paramValue = ((List)obj).toArray();
		} 

		for( int i=0 ; paramValue!=null && i<paramValue.length ; i++ ) {
			if( paramValue[i] instanceof Integer ) {
				declareParameter(new SqlParameter(Types.INTEGER));
			} else if( paramValue[i] instanceof Long ) {
				declareParameter(new SqlParameter(Types.BIGINT));			
			} else {
				declareParameter(new SqlParameter(Types.VARCHAR));			
			}
		}

		setBatchSize(100);
	}


	public int update(List value) {
		int result = super.update(value.toArray());
		return result;
	}

}

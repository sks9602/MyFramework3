package project.jun.dao.parameter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Spring JDBC를 사용할 경우 파라미터 정보를 Object배열로 넘겨야 할 경우
 * 사용가능하도록 ArrayList객체를 상속받아 생성.
 * @author 신갑식
 *
 */
public class HoQueryParameterList extends ArrayList {

	protected static Logger          logger     = Logger.getRootLogger();
		
	ArrayList value = new ArrayList();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * short를 wrapper class로 등록한다.
	 * @param entry
	 * @return
	 */
	public boolean add(short entry) {
		return super.add( new Short(entry));
	} 
	
	/**
	 * int를 wrapper class로 등록한다.
	 * @param entry
	 * @return
	 */
	public boolean add( int entry) {
		return super.add( new Integer(entry));
	} 
	
	/**
	 * long를 wrapper class로 등록한다.
	 * @param entry
	 * @return
	 */
	public boolean add( long entry) {
		return super.add( new Long(entry));
	} 
	
	/**
	 * float를 wrapper class로 등록한다.
	 * @param entry
	 * @return
	 */
	public boolean add( float entry) {
		return super.add( new Float(entry));
	} 
	
	/**
	 * double를 wrapper class로 등록한다.
	 * @param entry
	 * @return
	 */
	public boolean add( double entry) {
		return super.add( new Double(entry));
	} 
	
	/**
	 * 객체를 String으로 return한다.
	 * @param entry
	 * @return
	 */
	public String toString() {
		return super.toString();
	}
	
}

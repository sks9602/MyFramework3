package project.jun.dao.ibatis;

/**
 * SqlMapClient 설정 (매핑 및 기타)을 갱신해줄 수 있는 인터페이스. 
 * @author setq
 *
 */
public interface SqlMapClientRefreshable {
	
	/**
	 * SqlMapClilent의 설정 (매핑 및 기타)을 갱신해준다.
	 * @throws Exception
	 */
	void refresh() throws Exception;
	
	
	/**
	 * 설정 파일 체크 주기.
	 * @param ms 천분의 일초 단위. 0이면 체크 비활성화.
	 */
	void setCheckInterval(int ms);
	
}

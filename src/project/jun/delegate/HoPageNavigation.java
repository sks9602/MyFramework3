package project.jun.delegate;

import project.jun.was.parameter.HoParameter;

public class HoPageNavigation
{
	private long pageNo;	
	private long lastPageNo;
	private long prevPageNo;
	private long nextPageNo;
	private long beginPageNo;
	private long endPageNo;
	private long beginRowNum;
	private long endRowNum;
	private long totRowCnt;	// 조회된 전체 Records
	private long pageRowCnt;

	private HoParameter param;

	public HoPageNavigation(HoParameter param) {
		this.param = param;
	}
	

	public void setNavigationInfo( long totRowCnt, long pageRowCnt, long pageNo)
	{
		this.totRowCnt  = totRowCnt;
		this.pageRowCnt = pageRowCnt;
		this.pageNo     = pageNo;

		// 시작페이지
		this.beginPageNo = ((this.pageNo-1) / 10) * 10 + 1;

		// 마지막 페이지
		this.lastPageNo = (int)(totRowCnt/pageRowCnt);
		if( totRowCnt % pageRowCnt > 0 )
			lastPageNo++;

		if( this.pageNo > lastPageNo )
			this.pageNo = lastPageNo;
		else if( this.pageNo < 1 )
			this.pageNo = 1;

		// 끝 페이지
		this.endPageNo = beginPageNo + 9;
		if( endPageNo > lastPageNo )
			endPageNo = lastPageNo;

		// 이전 페이지
		this.prevPageNo = this.beginPageNo -1;
		if( prevPageNo < 1 )
			prevPageNo = 1;

		// 다음 페이지
		this.nextPageNo = this.endPageNo + 1;
		if( nextPageNo > lastPageNo )
			nextPageNo = lastPageNo;

		this.beginRowNum = ((this.pageNo-1)* pageRowCnt);
		this.endRowNum   = this.pageNo * pageRowCnt +1;
		
	}

	public long getBeginRowNum()
	{
		return this.beginRowNum;
	}
	
	public long getEndRowNum()
	{
		return this.endRowNum;
	}
	
	public long getPageNo()
	{
		return this.pageNo;
	}
	
	public String getZeroListMsgKey() {
		if( pageRowCnt == 0 ) {
			return "HO-PAGE-INIT"; //검색조건을 입력하신후 조회하세요.
		} else if( totRowCnt == 0 ){
			return "HO-PAGE-INIT-NONE"; // "조건에 해당하는 데이터가 없습니다.";
		} else {
			return "HO-PAGE-INIT-EXISTS"; // "데이터가 존재합니다.";
		}
	}
}
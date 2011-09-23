package project.jun.aop.advice;

import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;

/**
 * Exception이 발생하면 log를 작성한다.
 * @author 신갑식
 *
 */
public class HoThrowingAdvice implements ThrowsAdvice {
	protected static Logger          logger     = null;

	/**
	 * Exception이 발생하면 Exception log를 작성한다.
	 * @param e
	 * @throws Throwable
	 */
	public void afterThrowing(Exception e) throws Throwable {
		logger = Logger.getLogger(e.getClass());
		logger.warn("==>> Exception : "+e.getClass().getName()+"."+e.getMessage());		
	}
}

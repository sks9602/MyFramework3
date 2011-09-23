package project.jun.aop.advice;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

import project.jun.dao.result.HoList;
import project.jun.dao.result.HoMap;
import project.jun.util.HoArrayList;
import project.jun.was.result.message.HoMessage;


/**
 * 메서드가 return할경우 log를 작성한다.
 * @author 신갑식
 */

public class HoPostLoggingAdvice implements AfterReturningAdvice {
	protected static Logger          logger     = Logger.getLogger(HoPostLoggingAdvice.class); //Logger.getRootLogger();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 * 1. return이 List일 경우 "Return : ${return하는 List의 class명} - [size is ${return하는 List의 size()} ]"를  log
	 * 2. return이 Map일 경우 "Return : ${return하는 Map의 class명} - [size is ${return하는 Map의 내용} ]"를  log
	 * 3. return이 HoList일 경우 "Return : ${return하는 HoList의 class명} - [size is ${return하는 HoList의 size()} ]"를  log
	 * 4. return이 HoMap일 경우 "Return : ${return하는 HoMap의 class명} - [size is ${return하는 HoMap의 내용} ]"를  log
	 * 5. return이 WizMessage일 경우 "Return : ${return하는 WizMessage의 class명} - [size is ${return하는 WizMessage의 message} ]"를  log
	 * 6. return이 HoArrayList일 경우 "Return : ${return하는 HoArrayList의 class명} - [size is ${return하는 HoArrayList의 내용} ]"를  log
	 * 7. return이 String []일 경우 "Return : String [] ${return하는 HoArrayList의 class명} - [size is ${return하는 String []의 내용} ]"를  log
	 * 8. return이 null이 아닐 경우 "Return : ${return하는 값의 class명} - [size is ${return하는 내용} ]"를  log
	 * 9. return이 null일 경우 "Return : --> [null] ]"를  log
	 */
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		String className = target.getClass().getName();

		if( returnValue instanceof List) {
			logger.info("Return : "+ returnValue.getClass().getName() + " - [ size is "+ ((List)returnValue).size()+"]");
		} else if( returnValue instanceof Map) {
			logger.info("Return : "+ returnValue.getClass().getName() + " - [  "+ returnValue +"]");		
		} else if( returnValue instanceof HoList) {
			logger.info("Return : "+ returnValue.getClass().getName() + " - [ size is "+ ((HoList)returnValue).size()+"]");		
		} else if( returnValue instanceof HoMap) {
			logger.info("Return : "+ returnValue.getClass().getName() + " - [  "+ returnValue +"]");		
		} else if( returnValue instanceof HoMessage){
			logger.info("Return : "+returnValue.getClass().getName() + "-->"+ ((HoMessage)returnValue).getMessage());		
		} else if( returnValue instanceof HoArrayList){
			logger.info("Return : "+returnValue.getClass().getName() + "-->");		
			for( int i=0 ; i<((HoArrayList)returnValue).size() ; i++ ) {
				logger.info("\r\n["+i+"]" + ((HoArrayList)returnValue).get(i));
			}
		} else if( returnValue instanceof String [] ){
			logger.info("Return : String [] "+returnValue.getClass().getName() + "-->");		
			for( int i=0 ; i<((String[])returnValue).length ; i++ ) {
				logger.info("\r\n["+i+"]" + ((String[])returnValue)[i]);
			}
		} else if( returnValue != null){
			logger.info("Return : "+returnValue.getClass().getName() + "-->"+ returnValue.toString());		
		} else {
			logger.info("Return : --> [null]");
		}
		logger.info("Ending : "+className+"."+method.getName()+"()");		
	}

	
}

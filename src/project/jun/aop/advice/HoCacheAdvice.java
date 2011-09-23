package project.jun.aop.advice;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import project.jun.dao.HoDaoImpl;
import project.jun.dao.parameter.HoQueryParameterMap;
import project.jun.util.cache.HoCache;
import project.jun.util.cache.HoEhCache;

/**
 * 메서드가 return할경우 log를 작성한다.
 * 
 * @author 신갑식
 */

public class HoCacheAdvice implements MethodInterceptor, InitializingBean {
	protected static Logger logger = Logger.getLogger(HoCacheAdvice.class); // Logger.getRootLogger();

	/*
	 * @see
	 * org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 * 공통 코드일
	 */
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		HoDaoImpl dao = (HoDaoImpl)invocation.getThis();
		//String methodName = invocation.getMethod().getName();
		Object[] arguments = invocation.getArguments();
		Map cacheMap  = dao.getCacheMap();
		
		// 공통코드 목록 조회 일 경우..
		if( cacheMap.containsKey((String)arguments[0])) {
			Object result;
			String cacheCode = (String)cacheMap.get((String)arguments[0]);

			HoCache cache = new HoEhCache(dao.getCache());
			
			HoQueryParameterMap value = (HoQueryParameterMap) arguments[1];
			
			result  = cache.get(value.get(cacheCode));
			if (result == null) {
				result = invocation.proceed();
				logger.info("Caching - CODE[" + value.get(cacheCode)+"]");
				cache.put(value.get(cacheCode), result);
			} else {
				logger.info("Restoring Cache - CODE[" + value.get(cacheCode)+"]");
			}
			return result;
		} else {
			return invocation.proceed();
		}
	}


}

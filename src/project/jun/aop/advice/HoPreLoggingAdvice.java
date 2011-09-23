package project.jun.aop.advice;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;

import project.jun.delegate.HoDelegate;


/**
 * 메서드가 시작되기전에 log를 작성한다.
 * @author 신갑식
 *
 */
public class HoPreLoggingAdvice implements MethodBeforeAdvice {
	protected static Logger          logger     = Logger.getLogger(HoPreLoggingAdvice.class); //Logger.getRootLogger();
	
	/**
	 * 메서드가 시작 되면 class명 arguments등의 log를 작성한다.
	 */
	public void before(Method method, Object[] args, Object target) throws Throwable {
		String className = target.getClass().getName();
		
		logger.info("Starting : "+className+"."+method.getName()+"()" );

		//logger.info("method : "+method.toString());
		//logger.info("target : "+target);
		if( target instanceof HoDelegate && method.getName().indexOf("getHoParameter") == -1 ) {
			
			HoDelegate delegate = (HoDelegate)target;
			if(delegate.getHoConfig().isDebugParameter()) {
				delegate.getHoParameter().infoForParameter();
				delegate.getHoParameter().info();
			}
		}
		//logger.info("target.getClass() : "+target.getClass());
		//WizDelegate delegate = ((WizDelegate) target);
		//delegate.getDataSet().put("DELEGATE_CLASS", target.getClass().getName());

		if(args!=null && args.length>0) {
			for(int i=0 ; i<args.length ; i++) {
				if( args[i] instanceof Object []) {
					Object [] argsObj = (Object []) args[i];
					
					if( argsObj.length == 0 ) {
						logger.info("args["+i+"] (!!Object []!!) : length : " + argsObj.length );
					} else {
						logger.info("args["+i+"] ("+ argsObj[0].getClass().getName() + ") : length : " + argsObj.length );
						
						logger.info("args["+i+"][0] : " +((Object[])args[i])[0]);
						logger.info("~ args["+i+"]["+(argsObj.length-1)+"] : " +((Object[])args[i])[argsObj.length-1]);
					}
					/*
					for( int j=0 ; j<((Object[])args[i]).length ; j++) {
						logger.info("args["+i+"]["+j+"] : " +((Object[])args[i])[j]);
					}
					*/
				} else {
					logger.info("args["+i+"] (" +args[i].getClass().getName() + ") : " + args[i]);
				}
			}
		}
		
	}
	
}

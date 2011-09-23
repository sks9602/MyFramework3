package project.jun.aop.pointcut;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * DAO가 실행이 될경우의 Pointcut
 * @author 신갑식
 *
 */public class HoDAOPointcut extends StaticMethodMatcherPointcut {

	public boolean matches(Method method, Class cls) {
		if( method.getName().startsWith("set")) {
			return false;
		}
		return true;
	}
	
	public ClassFilter getClassFilter() {
		return new ClassFilter() {
			public boolean matches(Class cls) {
				return (cls.getName().toLowerCase().indexOf("dao")!= -1 ) ;
			}
		};
		/*
		WizClassFilter wcf = new WizClassFilter(); 
		wcf.setFilterType("DAO");
		return wcf;
		*/
	}

}



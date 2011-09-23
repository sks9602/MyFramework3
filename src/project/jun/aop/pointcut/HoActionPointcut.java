package project.jun.aop.pointcut;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
/**
 * Action이 실행이 될경우의 Pointcut
 * @author 신갑식
 *
 */
public class HoActionPointcut extends StaticMethodMatcherPointcut {

	public boolean matches(Method method, Class cls) {
		return method.getName().equals("execute");
	}
	
	public ClassFilter getClassFilter() {
		return new ClassFilter() {
			public boolean matches(Class cls) {
				return  (cls.getName().toLowerCase().indexOf("action")!= -1);
			}
		};
		/*
		WizClassFilter wcf = new WizClassFilter(); 
		wcf.setFilterType("DAO");
		return wcf;
		*/
	}

}



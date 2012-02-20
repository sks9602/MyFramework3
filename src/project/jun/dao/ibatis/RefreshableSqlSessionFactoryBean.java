package project.jun.dao.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * iBATIS sqlmap 클라이언트의 sqlMap 및 sqlMapConfig 파일의 변경을 감지, 실시간 적용하는 팩토리 빈.
 * 
 * <h2>개요</h2>
 * iBATIS + Spring 개발시 쿼리 매핑 파일이 변경되면 웹애플리케이션 서버를 재기동해야 적용이 됐었다. 이러한 불편을 없애기
 * 위해 매핑 파일 변경을 실시간으로 감시, 적용하는 모듈을 제공한다.<br />
 * 
 * 감시 대상 이 모듈은 iBATIS sqlmap 클라이언트의 sqlMap 및 sqlMapConfig 파일의 변경을 감지, 실시간 적용해준다.<br />
 * 
 * <h2>제약사항</h2>
 * 감시 대상 파일들은 스타트업 당시에 결정된다. 그러므로 추가된 파일들에 대해서는 감지가 되지 않고, 삭제된 파일들에 대해서는 경고
 * 메시지가 나온다. 예를 들어, sqlMapConfig에 sqlMap 파일이 추가되거나 하면 해당 맵이 적용되기는 하지만, 실시간 변경 감지
 * 대상으로 추가되지는 않는다.<br />
 * 
 * <h2>요구사항</h2>
 * iBATIS sqlmap 2.3.0, Java 1.4, Spring 2.5 이상 또는 iBATIS sqlmap 2.3.2 이상,<br />
 * Java 1.5 이상, Spring 2.5.5 이상<br />
 * 
 * <h2>적용 순서</h2>
 * 1. Spring의 applicationContext 설정 파일 중 sqlMapClient를 얻기 위한
 * SqlMapClientFactory 빈을 신규 클래스로 교체한다. <br />
 * 2. 변경 감지 시간 간격 (1000분의 1초 단위)를 지정한다.<br />
 * <PRE>
 * &lt;bean id="sqlMapClient" class="jcf.dao.ibatis.sqlmap.RefreshableSqlMapClientFactoryBean"&gt; 
 *     &lt;!-- &lt;bean id="sqlMapClient" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean"&gt;--&gt; 
 *     &lt;property name="configLocation" value="classpath:jcf/dao/ibatis/sqlmap/sqlmap-config.xml" /&gt; 
 *     &lt;property name="dataSource" ref="dataSource" /&gt;
 * 
 *     &lt;!-- Java 1.5 or higher and iBATIS 2.3.2 or higher REQUIRED --&gt; 
 *     &lt;property name="mappingLocations" value="jcf/dao/\*\*\/T\*.xml" /&gt; 
 *     &lt;!-- &lt;property name="mappingLocations" value="file:///D:/Type.xml" /&gt;--&gt;
 * 
 *     &lt;property name="checkInterval" value="1000" /&gt; 
 * &lt;/bean&gt;
 * </PRE>
 *
 * 3. 라이브러리 추가 <br />
 * backport-util-concurrent-3.1.jar<br />
 * 
 * 테스트 기존의 applicationContext에 등록되어 있던 sqlMapClient에 해당하는 mappingFile들을 편집하거나,
 * sqlMapConfig 파일 또는 그 파일에 등록된 mappingFile(sqlMap 파일)들을 편집하면 주어진 변경감지 시간 후 변경사항이 적용된다.<br />
 * <br />
 * 
 * @author setq
 */
public class RefreshableSqlSessionFactoryBean extends SqlSessionFactoryBean
		implements SqlMapClientRefreshable, DisposableBean {
	
	private static final Log logger = LogFactory.getLog(RefreshableSqlSessionFactoryBean.class);
	
	private SqlSessionFactory proxy;
	private int interval;
	
	private Timer timer;
	private TimerTask task;
	
    private Resource configLocation;
    private Resource [] mapperLocations;

    private List resouces = new ArrayList();

	/**
	 * 파일 감시 쓰레드가 실행중인지 여부.
	 */
	private boolean running = false;
	
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

    public void setConfigLocation(Resource configLocation)
    {
    	super.setConfigLocation(configLocation);
        this.configLocation = configLocation;
    }

    public void setMapperLocations(Resource mapperLocations[])
    {
    	super.setMapperLocations(mapperLocations);
        this.mapperLocations = mapperLocations;
    }

    
    
	/**
	 * iBATIS 설정을 다시 읽어들인다.<br /> SqlMapClient 인스턴스 자체를 새로 생성하여 교체한다.
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Refreshing SqlSession.");
		}
		/*
		 * WRITE LOCK.
		 */
		w.lock();
		try {
			super.afterPropertiesSet();
			
		} finally {
			w.unlock();
		}
	}
	
	/**
	 * 싱글톤 멤버로 SqlMapClient 원본 대신 프록시로 설정하도록 오버라이드.
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		getResouces();
		
		setRefreshable();
	}
	
	
	public void getResouces() throws IOException {
		
		if( configLocation!=null ) {
			XPathParser parser = new XPathParser( configLocation.getInputStream(), true, null, new XMLMapperEntityResolver());
			
			XNode parent = parser.evalNode("/configuration").evalNode("mappers");

			for(Iterator iterator = parent.getChildren().iterator(); iterator.hasNext();) {
                XNode child = (XNode)iterator.next();
                String resource = child.getStringAttribute("resource");
                String url = child.getStringAttribute("url");
                if(resource != null && url == null) {
                    ErrorContext.instance().resource(resource);
                    this.resouces.add(new ClassPathResource(resource));
                } else if(url != null && resource == null){
                    ErrorContext.instance().resource(url);
                    this.resouces.add(new UrlResource(resource));
                } 
            }
		}	
	}
	
	private void setRefreshable() {
		proxy = (SqlSessionFactory) Proxy.newProxyInstance(
				SqlSessionFactoryBean.class.getClassLoader(), 
				new Class[] { SqlSessionFactory.class} , // DefaultSqlSessionFactory.class }, 
				new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				
				return method.invoke(getParentObject(), args);
			}
			
		});
		
		task = new TimerTask() {
		private Map map = new HashMap();
			
		public void run() {
			
			if (isModified()) {
				try {
					refresh();
				}
				catch (Exception e) {
					logger.error("caught exception", e);
				}
			}
			
		}
		
		
			
		private boolean isModified() {
			boolean retVal = false;
			
			for (int i = 0; i < resouces.size(); i++) {
				Resource resouce = (Resource) resouces.get(i);
				retVal |= findModifiedResource(resouce);
			}

			return retVal;
		}
			
			private boolean findModifiedResource(Resource resource) {
				boolean retVal = false;
				List modifiedResources = new ArrayList();
				
				try {
					long modified = resource.lastModified();
					
					if (map.containsKey(resource)) {
						long lastModified = ((Long) map.get(resource)).longValue();
						
						if (lastModified != modified) {
							map.put(resource, new Long(modified));
							modifiedResources.add(resource.getDescription());
							retVal = true;
						}
						
					}
					else {
						map.put(resource, new Long(modified));
					}
				}
				catch (IOException e) {
					logger.error("caught exception", e);
				}
				
				if (retVal) {
					if (logger.isInfoEnabled()) {
						logger.info("modified files : " + modifiedResources);
					}
				}
				return retVal;
			}
			
		};
		
		timer = new Timer(true);
		resetInterval();
	}
		
	public boolean isSingleton() {
		return true;
	}
	
	public void setCheckInterval(int ms) {
		interval = ms;
		
		if (timer != null) {
			resetInterval();
		}
	}
	
	private void resetInterval() {
		if (running) {
			timer.cancel();
			running = false;
		}
		if (interval > 0) {
			timer.schedule(task, 0, interval);
			running = true;
		}
	}

	public void destroy() throws Exception {
		timer.cancel();
	}
	
	public SqlSessionFactory getObject() {
		logger.info("this.proxy.getClass().getName() : " + this.proxy.getClass().getName());
		return (SqlSessionFactory)this.proxy;
	}
	
	public Class getObjectType() {
		return (this.proxy != null ? this.proxy.getClass() : SqlSessionFactory.class);
	}
	
	private Object getParentObject() throws Exception {
		/*
		 * READ LOCK.
		 */
		r.lock();
		try {
			return super.getObject();
			
		} finally {
			r.unlock();
		}
	}
}



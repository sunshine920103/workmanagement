package com.workmanagement.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 以静态变量保存Spring ApplicationContext, 以便获取服务实现
 * @author Administrator
 *
 */
public class SpringBeanUtil implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;

	/**
	 * 实现接口ApplicationContextAware的构造函数,以便spring容器初始化时实例此类
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;
	}
	
	public static ApplicationContext getApplicationContext(){
		checkApplicationContext();
		return applicationContext;
	}
	
	
	/**
	 * 自动转成所赋值对象的类型
	 * @param name
	 * @return
	 * @throws ChipException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		
		checkApplicationContext();
		Object obj = applicationContext.getBean(name);
		if(obj == null){
			throw new RuntimeException("Missing spring bean config for class name: " + name);
		}
		return (T)obj;
		
	}
	
	public static <T> T getBean(Class<T> clazz){
		checkApplicationContext();
		Map<String, T> beanMap = applicationContext.getBeansOfType(clazz);
		if(beanMap.isEmpty()){
			throw new RuntimeException("Missing spring bean config for class: " + clazz.getName());
		}else{
			return beanMap.values().iterator().next();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName, Class<T> clazz) {
		return (T)applicationContext.getBean(beanName);
	}
	
	
	private static void checkApplicationContext(){
		if(null == applicationContext){
			throw new IllegalStateException("applicationContext==null");
		}
	}

}

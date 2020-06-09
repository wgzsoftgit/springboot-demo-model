package com.demomodel.interfaceDesign;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
/**
 * 编写getContent类，实现ApplicationContextAware，实现该接口的的原因是，此接口可以在载入的时候，
 * 把ApplicationContext接收到，以此来获得所有的bean（通过getbean就能获取到对应的类），
 * 同时此类也可以当做一个spring工具类，负责有关getbean，反射获取对应类，根据注解类，
 * 获取有此注解类注解的有关类的功能。
https://blog.csdn.net/zxysshgood/java/article/details/78399980
 * @author zbj
 *
 */

@Service
public class getContent implements ApplicationContextAware {
 
	private static ApplicationContext applicationContext;
 
	//全局保存相关的personBean实现对象
	public static Map<String, Object> getPersonbeanmap = new HashMap<String, Object>();
 
	// 获取Spring的application到该类
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		System.err.println("com.demomodel.interfaceDesign.getContent####%$$/ApplicationContextAware模式获取到的上下文applicationContext");
		getContent.applicationContext = arg0;
	}
 
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
 
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		cheakApplicationContext();
		return (T) applicationContext.getBean(beanName);
	}
 
	public static <T> T getBean(Class<T> clazz) {
		cheakApplicationContext();
		return (T) applicationContext.getBean(clazz);
	}
 
	/**
	 *  根据传入的自定义注解的类,从Application获取有此注解的所有类
	 * @param cls
	 * @return
	 */
	public static Map<String, Object> getMapbeanwithAnnotion(Class<? extends Annotation> cls) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = applicationContext.getBeansWithAnnotation(cls);
		return map;
	}
 
	/**
	 *  空值检测
	 */
	private static void cheakApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}
 
	// 通过反射操作获取对应的类
	public static Object getTarget(Object proxy) throws Exception {
		if (!AopUtils.isAopProxy(proxy)) {
			return proxy;// 不是代理对象
		}
		// 根据jdk进行反射操作
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			return getJdkDynamicProxyTargetObject(proxy);//&& 同下
			} else { // 根据cglib进行反射操作
			return getCglibProxyTargetObject(proxy);  //&& 同下
		}
	}
 
	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);
		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
		return target;
	}
 
	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy = (AopProxy) h.get(proxy);
		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
		return target;
	}
 
}
//https://blog.csdn.net/zxysshgood/java/article/details/78399980
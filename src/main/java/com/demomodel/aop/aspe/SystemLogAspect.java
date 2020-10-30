package com.demomodel.aop.aspe;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demomodel.aop.Target.Log;

/**
 * @author 杨建
 * @E-mail: email
 * @version 创建时间：2015-10-19 下午4:29:05
 * @desc 切点类
 */

@Aspect
@Component
public class SystemLogAspect {


    private  static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);

    //Controller层切点
    @Pointcut("execution (*  com.demomodel.aop.aspe.*Controller.*(..))")
    public  void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("==========执行controller前置通知===============");
        if(logger.isInfoEnabled()){
            logger.info("before " + joinPoint);
        }
    }

    //配置controller环绕通知,使用在方法aspect()上注册的切入点
      @Around("controllerAspect()")
      public Object around(JoinPoint joinPoint) throws Throwable{
          System.out.println("==========开始执行controller环绕通知===============");
          long start = System.currentTimeMillis();
       //   try {
        	  Object proceed = ((ProceedingJoinPoint)joinPoint).proceed();
              long end = System.currentTimeMillis();
              if(logger.isInfoEnabled()){
                  logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
              }
              System.out.println("==========结束执行controller环绕通知===============");
              long end2 = System.currentTimeMillis();
//          } catch (Throwable e) {
//              long end = System.currentTimeMillis();
//              if(logger.isInfoEnabled()){
//                  logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
//              }
//          }
              return proceed;
      }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public  void after(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();  
        //读取session中的用户
     //   User user = (User) session.getAttribute("user");
        //请求的IP
        String ip = request.getRemoteAddr();
  
         try {

            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
             for (Method method : methods) {
                 if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                     if (clazzs.length == arguments.length&&
                             method.getAnnotation(Log.class)!=null) {
                         operationType = method.getAnnotation(Log.class).operationType();
                         operationName = method.getAnnotation(Log.class).operationName();
                         break;
                    }
                }
            }
            //*========控制台输出=========*//
            System.out.println("=====controller后置通知开始=====");
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);
            System.out.println("方法描述:" + operationName);
           // System.out.println("请求人:" + user.getName());
            System.out.println("请求IP:" + ip);
            //*========数据库日志=========*//
            //保存数据库
           // systemLogService.insert(log);
            System.out.println("=====controller后置通知结束=====");
        }  catch (Exception e) {
            //记录本地异常日志
            logger.error("==后置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
      @AfterReturning("controllerAspect()")
      public void afterReturn(JoinPoint joinPoint){
          System.out.println("=====执行controller后置返回通知=====");
              if(logger.isInfoEnabled()){
                  logger.info("afterReturn " + joinPoint);
              }
      }

    /**
     * 异常通知 用于拦截记录异常日志
     *
     * @param joinPoint
     * @param e
     */
     @AfterThrowing(pointcut = "controllerAspect()", throwing="e")
     public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
    	 String className = joinPoint.getTarget().getClass().getName();//com.demomodel.aop.aspe.AoptextController
         String methodName2 = joinPoint.getSignature().getName();//aop2
         Object[] args = joinPoint.getArgs();
         //开始打log
         System.out.println("异常:" + e.getMessage());
         System.out.println("异常所在类：" + className);
         System.out.println("异常所在方法：" + methodName2);
         System.out.println("异常中的参数：");
         for (int i = 0; i < args.length; i++) {
             System.out.println(args[i].toString());
         }
    	 
    	 
    	 HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
    //    User user = (User) session.getAttribute(WebConstants.CURRENT_USER);
        //获取请求ip
        String ip = request.getRemoteAddr(); 
        //获取用户请求方法的参数并序列化为JSON格式字符串

        
     

        String params = "";
         if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
             for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                params += joinPoint.getArgs()[i] + ";";
            }
        }
         try {

             String targetName = joinPoint.getTarget().getClass().getName();
             String methodName = joinPoint.getSignature().getName();
             Object[] arguments = joinPoint.getArgs();
             Class targetClass = Class.forName(targetName);
             Method[] methods = targetClass.getMethods();
             String operationType = "";
             String operationName = "";
              for (Method method : methods) {
                  if (method.getName().equals(methodName)) {
                     Class[] clazzs = method.getParameterTypes();
                      if (clazzs.length == arguments.length) {
                          operationType = method.getAnnotation(Log.class).operationType();
                          operationName = method.getAnnotation(Log.class).operationName();
                          break;
                     }
                 }
             }
             /*========控制台输出=========*/
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);
            System.out.println("方法描述:" + operationName);
            //System.out.println("请求人:" + user.getName());
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
               /*==========数据库日志=========*/
          
            //保存数据库
           // systemLogService.insert(log);
            System.out.println("=====异常通知结束=====");
        }  catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
         /*==========记录本地异常日志==========*/
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);

    }

}
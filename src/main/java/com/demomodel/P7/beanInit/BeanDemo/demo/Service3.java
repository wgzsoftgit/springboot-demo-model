package com.demomodel.P7.beanInit.BeanDemo.demo;
//上面类中会用到service1和service2，提供了对应的setter方法，一会我们通过setter方法注入依赖对象
public class Service3 {
    private Service1 service1;
    private Service2 service2;
 
    public Service1 getService1() {
        return service1;
    }
 
    public void setService1(Service1 service1) {
        this.service1 = service1;
    }
 
    public Service2 getService2() {
        return service2;
    }
 
    public void setService2(Service2 service2) {
        this.service2 = service2;
    }
 
    @Override
    public String toString() {
        return "Service3{" +
                "service1=" + service1 +
                ", service2=" + service2 +
                '}';
    }
}
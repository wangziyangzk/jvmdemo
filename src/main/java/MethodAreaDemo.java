import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/*
 **  方法区溢出模拟
 **  -XX:PermSize=10M -XX:MaxPermSize=10M
 */
public class MethodAreaDemo {
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Person.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                public Object intercept(Object obj, Method arg1, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            Person person = (Person) enhancer.create();
            person.Hi("Wzy");
        }
    }

    static class Person {
        public String Hi(String str) {
            return "hello " + str;
        }
    }
}  
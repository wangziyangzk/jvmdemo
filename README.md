# 1.什么是内存溢出
JVM运行时可分为虚拟机栈，堆，元空间，程序计数器，本地方法栈等等。在虚拟机管理内存自动内存管理机制下，不需要自己来实现释放内存。但是由于某些操作不当，也可能导致虚拟机异常，比如内存分配空间过小，程序不严密等等。

# 2.常见异常
## 2.1 堆溢出
Java堆用于存储对象实例，因此需要不断地创建对象，并且保证GC Roots之间存在可达路径避免被垃圾回收。
```
/*
 ** VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapDemo {

    public static void main(String[] args) {
        int i = 1;
        List<byte[]> list = new ArrayList<byte[]>();

        while (true) {
            list.add(new byte[10 * 1024 * 1024]);
            System.out.println("第" + (i++) + "次分配");
        }
    }
}

```
运行结果：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190928211143300.png)
## 2.2 栈溢出
每个线程都有一个私有的栈，随着线程的创建而创建，栈里面存放着“栈帧”，每个方法都会创建一个栈帧，栈帧中存放着局部变量表（基本数据类型和对象引用），操作数栈，方法出口等基本信息。和堆一样大小可以固定也可以动态扩展。堆溢出有两种思路：
1.不断创建线程。
2.不断执行方法。
```
/*
** 通过不断创建线程来模拟栈溢出
 */
public class StackDemo1 {
    public static void main(String[] args) {
        int i =0;

        while(true) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }
    }
}

```
运行结果：内存爆满。。。
```
/*
** 通过不断执行方法来模拟栈溢出
 */
public class StackDemo2 {

    static int depth = 0;

    public void countMethod() {
        depth++;
        countMethod();
    }

    public static void main(String[] args) {
        StackDemo2 demo = new StackDemo2();
        try{
            demo.countMethod();
        }finally {
            System.out.println("方法执行了"+depth+"次");
        }

    }
}

```
运行结果：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190928212044116.png)

## 2.3 常量池溢出
如果要模拟常量池溢出，可以使用String的intern()方法。如果常量池包含一个该字符串，就返回该String对象，否则就将该对象添加到常量池中。
注意：JDK1.7以后intern()方法改为在常量池记录Java Heap中首次出现的字符串的引用，因此执行测试代码会导致堆内存溢出。
```
/*
**	-Xms5m -Xmx5m
*/
public class ConstantPoolDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        int i = 0;
        while(true){
            list.add(String.valueOf(i++).intern());
        }
    }
}
```
运行结果：
```
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
    at java.lang.Long.toUnsignedString(Unknown Source)
    at java.lang.Long.toHexString(Unknown Source)
    at java.util.UUID.digits(Unknown Source)
 
    at java.util.UUID.toString(Unknown Source)
    at com.ghs.test.ConstantPoolOOMTest.main(ConstantPoolDemo.java:10)
   ```


## 2.4 方法区溢出
方法区存放的是Class的相关信息，因此可以不断动态添加类来模拟方法区溢出（CGLib）
```
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/*
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
```
运行结果：
```
Exception in thread "main" 
Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
```

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

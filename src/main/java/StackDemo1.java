
/*
 ** 通过不断创建线程来模拟栈溢出
 */
public class StackDemo1 {
    public static void main(String[] args) {
        int i =0;

        while(true) {
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("线程执行");
                }
            }).start();
        }
    }
}


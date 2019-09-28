import java.util.ArrayList;
import java.util.List;

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


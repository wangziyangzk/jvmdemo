import java.util.ArrayList;
import java.util.List;

/*
** 常量池溢出模拟
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

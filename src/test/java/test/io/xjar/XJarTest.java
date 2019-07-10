package test.io.xjar;

import io.xjar.XTool;
import io.xjar.jar.XJar;
import io.xjar.jni.XJni;
import io.xjar.key.XKey;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * @author Payne 646742615@qq.com
 * 2019/7/4 16:24
 */
public class XJarTest {

    @Test
    public void encode() throws Exception {

        XKey xKey = XJar.key("io.xjar");
        byte[] bytes = XJni.getInstance().encode(xKey);
        System.out.println(Arrays.toString(bytes));
    }

    @Test
    public void decode() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XTool.transfer(new FileInputStream("C:\\Users\\Chang\\source\\repos\\Project1\\XJni.so"), bos);
        System.out.println(Arrays.toString(bos.toByteArray()));

//        System.load("C:\\Users\\Chang\\source\\repos\\Project1\\XJni.so");
//        byte[] read = XJni.getInstance().read();
//        System.out.println(Arrays.toString(read));
    }

    @Test
    public void test() {
        Properties properties = System.getProperties();
        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            System.out.println(name + ":" + properties.getProperty(name));
        }

//        XJar.encryptor()
//                .build()
//                .encrypt(
//                        XJar.key("io.xjar"),
//                        "D:\\workspace\\xjar-demo\\target\\xjar-demo-1.0-SNAPSHOT.jar",
//                        "D:\\workspace\\xjar-demo\\target\\xjar-demo-1.0-SNAPSHOT.xjar"
//                );
    }

}

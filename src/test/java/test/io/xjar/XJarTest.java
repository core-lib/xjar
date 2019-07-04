package test.io.xjar;

import io.xjar.XKit;
import io.xjar.jar.XJarEncryptor;
import org.junit.Test;

/**
 * @author Payne 646742615@qq.com
 * 2019/7/4 16:24
 */
public class XJarTest {

    @Test
    public void test() throws Exception {
        XJarEncryptor.builder()
                .build()
                .encrypt(
                        XKit.key("io.xjar"),
                        "C:\\Users\\Chang\\Downloads\\Android-SDK\\target\\nwhs-sdk-v5.3.0-alpha.2.jar",
                        "C:\\Users\\Chang\\Downloads\\Android-SDK\\target\\nwhs-sdk-v5.3.0-alpha.2.xjar"
                );
    }

}

package test.io.xjar;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Payne 646742615@qq.com
 * 2019/7/4 16:24
 */
public class XJarTest {

    @Test
    public void test() throws IOException {
        //初始化代码
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        //获取模板
        Template t = gt.getTemplate("io/xjar/jni/XJni.cpp");
        t.binding("key", new byte[]{1, 2, 3, 4});
        //渲染结果
        String str = t.render();
        System.out.println(str);
    }

}

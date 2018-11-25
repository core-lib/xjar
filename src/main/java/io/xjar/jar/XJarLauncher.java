package io.xjar.jar;

/**
 * JAR包启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 18:41
 */
public class XJarLauncher {

    public static void main(String... args) {
        ClassLoader classLoader = XJarClassLoader.class.getClassLoader();
        System.out.println(classLoader);
        String classpath = System.getProperty("java.class.path");
        System.out.println(classpath);
    }

}

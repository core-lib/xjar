package io.xjar.boot;

import io.xjar.XDecryptor;
import io.xjar.XEncryptor;
import io.xjar.XKit;
import io.xjar.key.XKey;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.boot.loader.LaunchedURLClassLoader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

/**
 * X类加载器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:04
 */
public class XBootClassLoader extends LaunchedURLClassLoader {
    private final XBootURLHandler xBootURLHandler;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public XBootClassLoader(URL[] urls, ClassLoader parent, XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey) throws Exception {
        super(urls, parent);
        this.xBootURLHandler = new XBootURLHandler(xDecryptor, xEncryptor, xKey, this);
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            return null;
        }
        try {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xBootURLHandler);
        } catch (MalformedURLException e) {
            return url;
        }
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        Enumeration<URL> enumeration = super.findResources(name);
        if (enumeration == null) {
            return null;
        }
        return new XBootEnumeration(enumeration);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if ("org.hibernate.boot.archive.internal.JarFileBasedArchiveDescriptor".equals(name)){
            return findJarFileBasedArchiveDescriptor(name);
        }
        if ("org.hibernate.boot.archive.spi.AbstractArchiveDescriptor".equals(name)){
            return findAbstractArchiveDescriptor(name);
        }
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            URL resource = findResource(name.replace('.', '/') + ".class");
            if (resource == null) {
                throw new ClassNotFoundException(name, e);
            }
            try (InputStream in = resource.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                XKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Throwable t) {
                throw new ClassNotFoundException(name, t);
            }
        }
    }

    /**
     * 加载该类后将该类缓存到classPool，注入的源码要用
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    private Class findAbstractArchiveDescriptor(String name) throws ClassNotFoundException{
        URL resource = findResource(name.replace('.', '/') + ".class");
        try {
            InputStream in = resource.openStream();
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(in);
            return ctClass.toClass();
        }catch (Exception e){
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * 加载hibernate的org.hibernate.boot.archive.internal.JarFileBasedArchiveDescriptor类
     * 之后动态修改resolveJarFileReference实现兼容访问加密的xjar
     * @param name
     * @return
     * @throws Exception
     */
    private Class findJarFileBasedArchiveDescriptor(String name) throws ClassNotFoundException{
        String methodName = "resolveJarFileReference";
//        Thread.currentThread().setContextClassLoader(this.getParent());
        try {
            /**
             * 这里的ClassPool类和动态注入源码中的class可能是不同的classloader加载，
             * 导致classNotFound,所以在这手动使用本classLoader加载
             */
            loadClass("org.hibernate.boot.archive.spi.ArchiveDescriptor");
            loadClass("org.hibernate.boot.archive.spi.AbstractArchiveDescriptor");
            loadClass("java.util.jar.JarFile");
            loadClass("io.xjar.XJarFile");
            loadClass("java.lang.Exception");
            URL resource = findResource(name.replace('.', '/') + ".class");
            InputStream in = resource.openStream();
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(in);
            CtMethod resolveJarFileReferenceMethod = ctClass.getDeclaredMethod(methodName);
            resolveJarFileReferenceMethod.setBody(""
                    + "{try {\n" +
                    "final String filePart = super.getArchiveUrl().getFile();\n" +
                    "if ( filePart != null && filePart.indexOf( ' ' ) != -1 ) {\n" +
                    "java.util.jar.JarFile jarFile = new java.util.jar.JarFile( super.getArchiveUrl().getFile() );" +
                    "return new io.xjar.XJarFile(jarFile.getName(), ((Object)this).getClass().getClassLoader());\n" +
                    "}\n" +
                    "else {\n" +
                    "java.util.jar.JarFile jarFile = new java.util.jar.JarFile( super.getArchiveUrl().toURI().getSchemeSpecificPart() );" +
                    "return new io.xjar.XJarFile(jarFile.getName(), ((Object)this).getClass().getClassLoader());\n" +
                    "}\n" +
                    "}\n" +
                    "catch (Exception e) {\n" +
                    "e.printStackTrace();\n" +
                    "}\n" +
                    "return null;}");
            return ctClass.toClass();
        }catch (Exception e){
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }

    private class XBootEnumeration implements Enumeration<URL> {
        private final Enumeration<URL> enumeration;

        XBootEnumeration(Enumeration<URL> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }

        @Override
        public URL nextElement() {
            URL url = enumeration.nextElement();
            if (url == null) {
                return null;
            }
            try {
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xBootURLHandler);
            } catch (MalformedURLException e) {
                return url;
            }
        }
    }
}

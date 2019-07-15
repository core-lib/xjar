package io.xjar.compiler;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.xjar.XCompiler;
import io.xjar.XSignature;
import io.xjar.key.XKey;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * 标准编译器
 *
 * @author Payne 646742615@qq.com
 * 2019/7/15 10:42
 */
public class XStdCompiler implements XCompiler {
    private String srcRoot = "io/xjar/jni";
    private String gccRoot = "";
    private String jdkRoot = new File(System.getProperty("java.home")).getParent();
    private String tmpRoot = System.getProperty("java.io.tmpdir");

    public XStdCompiler() {
    }

    public XStdCompiler(String gccRoot) {
        this.gccRoot = gccRoot;
    }

    public XStdCompiler(String gccRoot, String jdkRoot) {
        this.gccRoot = gccRoot;
        this.jdkRoot = jdkRoot;
    }

    public XStdCompiler(String gccRoot, String jdkRoot, String tmpRoot) {
        this.gccRoot = gccRoot;
        this.jdkRoot = jdkRoot;
        this.tmpRoot = tmpRoot;
    }

    @Override
    public File compile(XKey xKey, XSignature xSignature) throws IOException {
        // 创建目录
        File dir = new File(tmpRoot, UUID.randomUUID().toString());
        if (!dir.exists() && !dir.mkdirs() && !dir.exists()) {
            throw new IOException("could not make directory: " + dir);
        }

        // 渲染源码
        List<String> srcs = new ArrayList<>();
        ResourceLoader resourceLoader = new ClasspathResourceLoader();
        Configuration configuration = Configuration.defaultConfiguration();
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, configuration);
        Enumeration<Resource> resources = Loaders.ant().load("io/xjar/jni/**/*");
        while (resources.hasMoreElements()) {
            Resource src = resources.nextElement();
            String name = src.getName();
            srcs.add(name);
            File file = new File(dir, name);
            File folder = file.getParentFile();
            if (!folder.exists() && !folder.mkdirs() && !folder.exists()) {
                throw new IOException("could not make directory: " + folder);
            }
            Template template = groupTemplate.getTemplate(name);
            template.binding("xKey", xKey);
            template.binding("xSignature", xSignature);
            try (OutputStream out = new FileOutputStream(file)) {
                template.renderTo(out);
            }
        }

        // 编译源码
        StringBuilder cmd = new StringBuilder();
        cmd.append(gccRoot);
        cmd.append(" ").append("g++");
        cmd.append(" ").append("-fPIC -shared -o XJar.so");
        for (String src : srcs) {
            cmd.append(" ").append(src);
        }
        cmd.append(" ").append("-I \"").append(jdkRoot).append(File.separator).append("include").append("\"");
        cmd.append(" ").append("-I \"").append(jdkRoot).append(File.separator).append("include").append(File.separator).append("win32").append("\"");

        try {
            Process process = Runtime.getRuntime().exec(cmd.toString(), null, dir);
            int code = process.waitFor();
            if (code != 0) {
                throw new IllegalStateException("error occurred while compiling c++ lib with code: " + code);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }

        return null;
    }

    public String getGccRoot() {
        return gccRoot;
    }

    public void setGccRoot(String gccRoot) {
        this.gccRoot = gccRoot;
    }

    public String getJdkRoot() {
        return jdkRoot;
    }

    public void setJdkRoot(String jdkRoot) {
        this.jdkRoot = jdkRoot;
    }

    public String getTmpRoot() {
        return tmpRoot;
    }

    public void setTmpRoot(String tmpRoot) {
        this.tmpRoot = tmpRoot;
    }
}

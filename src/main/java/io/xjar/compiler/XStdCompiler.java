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
    private String gccPath = "g++";
    private String libName = "XJar.so";
    private String srcRoot = "io/xjar/jni";
    private String jdkRoot = new File(System.getProperty("java.home")).getParent();
    private String tmpRoot = System.getProperty("java.io.tmpdir");

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
        Enumeration<Resource> resources = Loaders.ant().load(srcRoot + "/**/*");
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

        File lib = new File(dir, libName);

        // 编译源码
        StringBuilder command = new StringBuilder();
        command.append("\"").append(gccPath).append("\"");
        command.append(" ").append("-fPIC");
        command.append(" ").append("-shared");
        command.append(" ").append("-o");
        command.append(" ").append("\"").append(dir.toURI().relativize(lib.toURI())).append("\"");
        for (String src : srcs) {
            command.append(" ").append("\"").append(src).append("\"");
        }
        command.append(" ").append("-I \"").append(jdkRoot).append(File.separator).append("include").append("\"");
        command.append(" ").append("-I \"").append(jdkRoot).append(File.separator).append("include").append(File.separator).append("win32").append("\"");

        try {
            Process process = Runtime.getRuntime().exec(command.toString(), null, dir);
            int code = process.waitFor();
            if (code != 0) {
                throw new IllegalStateException("error occurred while compiling c++ lib with code: " + code);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }

        return lib;
    }

    public String getGccPath() {
        return gccPath;
    }

    public void setGccPath(String gccPath) {
        this.gccPath = gccPath;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getSrcRoot() {
        return srcRoot;
    }

    public void setSrcRoot(String srcRoot) {
        this.srcRoot = srcRoot;
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

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
public abstract class XPlatformCompiler implements XCompiler {
    protected String libName = "xjar.so";
    protected String srcRoot = "io/xjar/jni";
    protected String tmpRoot = System.getProperty("java.io.tmpdir");

    @Override
    public File compile(XKey xKey, XSignature xSignature) throws IOException {
        try {
            File dir = new File(tmpRoot, UUID.randomUUID().toString());
            if (!dir.exists() && !dir.mkdirs() && !dir.exists()) {
                throw new IOException("could not make directory: " + dir);
            }

            List<String> src = render(xKey, xSignature, srcRoot, dir);
            String cmd = convert(src, libName);

            Process process = Runtime.getRuntime().exec(cmd, null, dir);
            int code = process.waitFor();
            if (code != 0) {
                throw new IllegalStateException("error occurred while compiling c++ lib with code: " + code);
            }

            return new File(dir, libName);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    /**
     * 转换编译命令
     *
     * @param src 源码列表
     * @param lib 目标类库
     * @return 编译命令
     */
    protected abstract String convert(List<String> src, String lib);

    /**
     * 渲染源码文件
     *
     * @param xKey       密钥
     * @param xSignature 签名
     * @param path       资源根路径
     * @param dir        目录
     * @return 源码文件相对路径列表
     * @throws IOException I/O异常
     */
    protected List<String> render(XKey xKey, XSignature xSignature, String path, File dir) throws IOException {
        List<String> sources = new ArrayList<>();
        ResourceLoader resourceLoader = new ClasspathResourceLoader();
        Configuration configuration = Configuration.defaultConfiguration();
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, configuration);
        Enumeration<Resource> resources = Loaders.ant().load(path + "/**/*");
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            sources.add(name);
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
        return sources;
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

    public String getTmpRoot() {
        return tmpRoot;
    }

    public void setTmpRoot(String tmpRoot) {
        this.tmpRoot = tmpRoot;
    }
}

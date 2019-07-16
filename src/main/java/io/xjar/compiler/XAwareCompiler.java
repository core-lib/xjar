package io.xjar.compiler;

import io.xjar.XCompiler;
import io.xjar.XSignature;
import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;

/**
 * 自知 编译器
 *
 * @author Payne 646742615@qq.com
 * 2019/7/16 11:10
 */
public class XAwareCompiler implements XCompiler {
    private static final String WINDOWS = "WINDOWS";
    private static final String LINUX = "LINUX";
    private static final String MAC = "MAC";

    private final XCompiler delegate;

    public XAwareCompiler() {
        String os = System.getProperty("os.name");
        if (os == null) {
            throw new IllegalStateException("unknown current operation system");
        } else if (os.toUpperCase().startsWith(WINDOWS)) {
            this.delegate = new XWindowsCompiler();
        } else if (os.toUpperCase().startsWith(LINUX)) {
            this.delegate = new XLinuxCompiler();
        } else if (os.toUpperCase().startsWith(MAC)) {
            this.delegate = new XMacCompiler();
        } else {
            throw new IllegalStateException("unsupported operation system: " + os);
        }
    }

    @Override
    public File compile(XKey xKey, XSignature xSignature) throws IOException {
        return delegate.compile(xKey, xSignature);
    }
}

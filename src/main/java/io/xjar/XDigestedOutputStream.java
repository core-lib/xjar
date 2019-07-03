package io.xjar;

import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 摘要计算的输出流
 *
 * @author Payne 646742615@qq.com
 * 2019/7/3 22:30
 */
public class XDigestedOutputStream extends JarArchiveOutputStream {
    private final XDigest xDigest;

    public XDigestedOutputStream(OutputStream out, XDigest xDigest) {
        super(out);
        this.xDigest = xDigest;
    }

    public XDigestedOutputStream(OutputStream out, String encoding, XDigest xDigest) {
        super(out, encoding);
        this.xDigest = xDigest;
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        xDigest.digest(buffer, offset, length);
        super.write(buffer, offset, length);
    }
}

package io.xjar;

import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * 摘要计算的输入流
 *
 * @author Payne 646742615@qq.com
 * 2019/7/3 22:31
 */
public class XDigestedInputStream extends JarArchiveInputStream {
    private final XDigest xDigest;

    public XDigestedInputStream(InputStream in, XDigest xDigest) {
        super(in);
        this.xDigest = xDigest;
    }

    public XDigestedInputStream(InputStream in, String encoding, XDigest xDigest) {
        super(in, encoding);
        this.xDigest = xDigest;
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        length = super.read(buffer, offset, length);
        if (length > 0) {
            xDigest.digest(buffer, offset, length);
        }
        return length;
    }
}

package io.xjar;

import java.io.IOException;
import java.io.InputStream;

/**
 * 不关闭的输入流
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 17:01
 */
public class XUnclosedInputStream extends InputStream {
    private final InputStream in;

    public XUnclosedInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void mark(int readLimit) {
        in.mark(readLimit);
    }

    @Override
    public void reset() throws IOException {
        in.reset();
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public void close() {
    }
}

package io.xjar;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 不关闭的输出流
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 16:32
 */
public class NoCloseOutputStream extends FilterOutputStream {

    public NoCloseOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void close() throws IOException {

    }

}

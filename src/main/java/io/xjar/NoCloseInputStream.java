package io.xjar;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 不关闭的输入流
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 17:01
 */
public class NoCloseInputStream extends FilterInputStream {

    public NoCloseInputStream(InputStream in) {
        super(in);
    }

    @Override
    public void close() throws IOException {

    }
}

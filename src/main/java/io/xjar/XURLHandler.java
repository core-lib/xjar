package io.xjar;

import io.xjar.key.XKey;
import org.springframework.boot.loader.jar.Handler;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 加密的URL处理器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 13:19
 */
public class XURLHandler extends Handler {
    private final XDecryptor xDecryptor;
    private final XKey key;

    public XURLHandler(XDecryptor xDecryptor, XKey key) {
        this.xDecryptor = xDecryptor;
        this.key = key;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection urlConnection = super.openConnection(url);

        return urlConnection;
    }

}

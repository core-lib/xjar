package io.xjar.loader;

import io.xjar.XDecryptor;
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
    private final XKey xKey;
    private final ClassLoader classLoader;

    public XURLHandler(XDecryptor xDecryptor, XKey xKey, ClassLoader classLoader) {
        this.xDecryptor = xDecryptor;
        this.xKey = xKey;
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection urlConnection = super.openConnection(url);
        return new XURLConnection(urlConnection, xDecryptor, xKey, classLoader);
    }

}

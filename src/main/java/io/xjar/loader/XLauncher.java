package io.xjar.loader;

import io.xjar.XDecryptor;
import io.xjar.XJdkDecryptor;
import io.xjar.key.SymmetricSecureKey;
import io.xjar.key.XKey;
import org.springframework.boot.loader.JarLauncher;

import java.net.URL;

/**
 * X启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:06
 */
public class XLauncher extends JarLauncher {
    private final String[] args;
    private final XDecryptor xDecryptor;
    private final XKey xKey;

    public XLauncher(String[] args) {
        this.args = args;
        String algorithm = "AES/CBC/PKCS7Padding";
        int size = 128;
        byte[] key = null;
        byte[] iv = null;
        this.xDecryptor = new XJdkDecryptor(algorithm);
        this.xKey = new SymmetricSecureKey(
                algorithm,
                size,
                key,
                iv
        );
    }

    public static void main(String[] args) throws Exception {
        new XLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(args);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new XClassLoader(urls, this.getClass().getClassLoader(), xDecryptor, xKey);
    }

}

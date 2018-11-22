package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;

/**
 * 文件夹解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:23
 */
public class XDirDecryptor extends XWrappedDecryptor implements XDecryptor {

    public XDirDecryptor(XDecryptor xDecryptor) {
        super(xDecryptor);
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            super.decrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                decrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }
}

package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;

/**
 * 文件夹加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:01
 */
public class XDirEncryptor extends XWrappedEncryptor implements XEncryptor {

    public XDirEncryptor(XEncryptor xEncryptor) {
        super(xEncryptor);
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            super.encrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                encrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }

}

package io.xjar.dir;

import io.xjar.XDecryptor;
import io.xjar.XEntryDecryptor;
import io.xjar.XEntryFilter;
import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;

/**
 * 文件夹解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:23
 */
public class XDirDecryptor extends XEntryDecryptor<File> implements XDecryptor {

    public XDirDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, null);
    }

    public XDirDecryptor(XDecryptor xDecryptor, XEntryFilter<File> filter) {
        super(xDecryptor, filter);
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            XDecryptor decryptor = filtrate(src) ? xDecryptor : xNopDecryptor;
            decryptor.decrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                decrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }
}

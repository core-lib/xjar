package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * 文件夹加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:01
 */
public class XDirEncryptor extends XEntryEncryptor<File> implements XEncryptor {

    public XDirEncryptor(XEncryptor xEncryptor) {
        super(xEncryptor);
    }

    public XDirEncryptor(XEncryptor xEncryptor, XEntryFilter<File>... filters) {
        this(xEncryptor, Arrays.asList(filters));
    }

    public XDirEncryptor(XEncryptor xEncryptor, Collection<XEntryFilter<File>> xEntryFilters) {
        super(xEncryptor, xEntryFilters);
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            XEncryptor encryptor = filter(src) ? xEncryptor : xNopEncryptor;
            encryptor.encrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                encrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }

}

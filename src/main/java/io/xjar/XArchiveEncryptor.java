package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 记录可过滤的加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 20:38
 */
public abstract class XArchiveEncryptor<E> extends XEntryEncryptor<E> implements XEncryptor, XEntryFilter<E> {

    protected XArchiveEncryptor(XEncryptor xEncryptor, XEntryFilter<E> filter) {
        super(xEncryptor, filter);
    }

    @Override
    public void encrypt(XKey key, String src, String dest) throws IOException {
        encrypt(key, new File(src), new File(dest));
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            encrypt(key, fis, fos);
        }
    }

    protected static abstract class XArchiveEncryptorBuilder<E, T extends XArchiveEncryptor<E>, B extends XArchiveEncryptorBuilder<E, T, B>> extends XEntryEncryptorBuilder<E, T, B> {
    }
}

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
public abstract class XArchiveDecryptor<E> extends XEntryDecryptor<E> implements XDecryptor, XEntryFilter<E> {

    protected XArchiveDecryptor(XDecryptor xDecryptor, XEntryFilter<E> filter) {
        super(xDecryptor, filter);
    }

    @Override
    public void decrypt(XKey key, String src, String dest) throws IOException {
        decrypt(key, new File(src), new File(dest));
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            decrypt(key, fis, fos);
        }
    }

    protected static abstract class XArchiveDecryptorBuilder<E, T extends XArchiveDecryptor<E>, B extends XArchiveDecryptorBuilder<E, T, B>> extends XEntryDecryptorBuilder<E, T, B> {
    }
}

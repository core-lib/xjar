package io.xjar;

import io.xjar.filter.XAllEntryFilter;
import io.xjar.filter.XAnyEntryFilter;
import io.xjar.filter.XMixEntryFilter;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import static io.xjar.XFilters.*;

/**
 * 解密
 *
 * @author Payne 646742615@qq.com
 * 2020/4/30 17:05
 */
public class XDecryption {
    private File src;
    private XKey key;
    private XAnyEntryFilter<JarArchiveEntry> includes = XKit.any();
    private XAllEntryFilter<JarArchiveEntry> excludes = XKit.all();

    public XDecryption from(String src) {
        return from(new File(src));
    }

    public XDecryption from(File src) {
        this.src = src;
        return this;
    }

    public XDecryption use(String password) {
        try {
            this.key = XKit.key(password);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public XDecryption use(String algorithm, int keysize, int ivsize, String password) {
        try {
            this.key = XKit.key(algorithm, keysize, ivsize, password);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public XDecryption include(String ant) {
        includes.mix(ant(ant));
        return this;
    }

    public XDecryption include(Pattern regex) {
        includes.mix(regex(regex.pattern()));
        return this;
    }

    public XDecryption exclude(String ant) {
        excludes.mix(not(ant(ant)));
        return this;
    }

    public XDecryption exclude(Pattern regex) {
        excludes.mix(not(regex(regex.pattern())));
        return this;
    }

    public void to(String dest) throws Exception {
        to(new File(dest));
    }

    public void to(File dest) throws Exception {
        XMixEntryFilter<JarArchiveEntry> filter;
        if (includes.size() == 0 && excludes.size() == 0) {
            filter = null;
        } else {
            filter = XKit.all();
            if (includes.size() > 0) {
                filter.mix(includes);
            }
            if (excludes.size() > 0) {
                filter.mix(excludes);
            }
        }
        XCrypto.decrypt(src, dest, key, filter);
    }
}

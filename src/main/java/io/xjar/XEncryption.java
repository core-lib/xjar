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
 * 加密
 *
 * @author Payne 646742615@qq.com
 * 2020/4/30 17:05
 */
public class XEncryption {
    private File jar;
    private XKey key;
    private XAnyEntryFilter<JarArchiveEntry> includes = XKit.any();
    private XAllEntryFilter<JarArchiveEntry> excludes = XKit.all();

    public XEncryption from(String jar) {
        return from(new File(jar));
    }

    public XEncryption from(File jar) {
        this.jar = jar;
        return this;
    }

    public XEncryption use(String password) {
        try {
            this.key = XKit.key(password);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public XEncryption use(String algorithm, int keysize, int ivsize, String password) {
        try {
            this.key = XKit.key(algorithm, keysize, ivsize, password);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public XEncryption include(String ant) {
        includes.mix(ant(ant));
        return this;
    }

    public XEncryption include(Pattern regex) {
        includes.mix(regex(regex.pattern()));
        return this;
    }

    public XEncryption exclude(String ant) {
        excludes.mix(not(ant(ant)));
        return this;
    }

    public XEncryption exclude(Pattern regex) {
        excludes.mix(not(regex(regex.pattern())));
        return this;
    }

    public void to(String xJar) throws Exception {
        to(new File(xJar));
    }

    public void to(File xJar) throws Exception {
        if (jar == null) {
            throw new IllegalArgumentException("jar to encrypt is null. [please call from(String jar) or from(File jar) before]");
        }
        if (key == null) {
            throw new IllegalArgumentException("key to encrypt is null. [please call use(String password) or use(String algorithm, int keysize, int ivsize, String password) before]");
        }
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
        XCryptos.encrypt(jar, xJar, key, filter);
    }
}

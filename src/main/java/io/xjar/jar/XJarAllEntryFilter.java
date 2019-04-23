package io.xjar.jar;

import io.xjar.XEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring-Boot 所有资源加密过滤器
 *
 * @author Payne 646742615@qq.com
 * 2019/4/23 13:03
 */
public class XJarAllEntryFilter implements XEntryFilter<JarArchiveEntry> {

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return true;
    }
}

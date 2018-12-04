package io.xjar.boot;

import io.xjar.XEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring Boot本身项目资源的滤器。
 *
 * @author Payne 646742615@qq.com
 * 2018/11/28 11:02
 */
public class XBootClassesFilter implements XEntryFilter<JarArchiveEntry> {

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return entry.getName().startsWith("BOOT-INF/classes/");
    }

}

package io.xjar;

import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.JarFileArchive;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * X启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:06
 */
public class XLauncher extends JarLauncher {

    public static void main(String[] args) throws Exception {
        new XLauncher().launch(args);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) {
        return new XClassLoader(urls, this.getClass().getClassLoader());
    }

    @Override
    protected List<Archive> getClassPathArchives() throws Exception {
        Archive archive = new JarFileArchive(new File("D:\\xjar\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"));
        List<Archive> archives = new ArrayList<>(archive.getNestedArchives(new Archive.EntryFilter() {
            @Override
            public boolean matches(Archive.Entry entry) {
                return isNestedArchive(entry);
            }
        }));
        return archives;
    }
}

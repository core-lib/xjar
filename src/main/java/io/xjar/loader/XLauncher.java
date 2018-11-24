package io.xjar.loader;

import io.xjar.XDecryptor;
import io.xjar.XJdkDecryptor;
import io.xjar.key.SymmetricSecureKey;
import io.xjar.key.XKey;
import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.JarFileArchive;

import java.io.File;
import java.io.IOException;
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
        try {
            new XLauncher().launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws IOException {
        XDecryptor xDecryptor = new XJdkDecryptor("AES/CBC/PKCS7Padding");
        XKey xKey = new SymmetricSecureKey(
                "AES",
                128,
                new byte[]{99, -9, -92, -85, 77, -84, -114, -21, -48, 8, -55, 92, -14, 58, 80, 105},
                new byte[]{81, 82, 29, 10, 105, 15, 52, 126, 100, 16, 42, 90, 60, 25, 13, 114}
        );
        return new XClassLoader(urls, this.getClass().getClassLoader(), xDecryptor, xKey);
    }

    @Override
    protected List<Archive> getClassPathArchives() throws Exception {
        Archive archive = new JarFileArchive(new File("D:\\xjar-encrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"));
        return new ArrayList<>(archive.getNestedArchives(new Archive.EntryFilter() {
            @Override
            public boolean matches(Archive.Entry entry) {
                return isNestedArchive(entry);
            }
        }));
    }
}

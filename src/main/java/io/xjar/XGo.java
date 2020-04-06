package io.xjar;

import io.xjar.key.XKey;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * XJar GoLang 启动器
 *
 * @author Payne 646742615@qq.com
 * 2020/4/6 18:20
 */
public class XGo {
    private static final String CLRF = System.getProperty("line.separator");

    public static void make(File xJar, XKey xKey) throws IOException {
        byte[] md5 = XKit.md5(xJar);
        StringBuilder jarMD5 = new StringBuilder();
        for (byte b : md5) {
            if (jarMD5.length() > 0) {
                jarMD5.append(", ");
            }
            jarMD5.append(b & 0xFF);
        }
        byte[] sha1 = XKit.sha1(xJar);

        StringBuilder jarSHA1 = new StringBuilder();
        for (byte b : sha1) {
            if (jarSHA1.length() > 0) {
                jarSHA1.append(", ");
            }
            jarSHA1.append(b & 0xFF);
        }

        byte[] key = xKey.getPassword().getBytes(StandardCharsets.UTF_8);
        StringBuilder jarKEY = new StringBuilder();
        for (byte b : key) {
            if (jarKEY.length() > 0) {
                jarKEY.append(", ");
            }
            jarKEY.append(b & 0xFF);
        }

        Map<String, String> variables = new HashMap<>();
        variables.put("jarMD5", jarMD5.toString());
        variables.put("jarSHA1", jarSHA1.toString());
        variables.put("jarKEY", jarKEY.toString());

        URL url = XGo.class.getClassLoader().getResource("xjar/xjar.go");
        if (url == null) {
            throw new IOException("could not find xjar.go");
        }
        String dir = xJar.getParent();
        File src = new File(dir, "xjar.go");
        try (
                InputStream in = url.openStream();
                Reader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                OutputStream out = new FileOutputStream(src);
                Writer writer = new OutputStreamWriter(out);
                BufferedWriter bw = new BufferedWriter(writer)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                for (Map.Entry<String, String> variable : variables.entrySet()) {
                    line = line.replace("#{" + variable.getKey() + "}", variable.getValue());
                }
                bw.write(line);
                bw.write(CLRF);
            }
            bw.flush();
            writer.flush();
            out.flush();
        }
    }

}

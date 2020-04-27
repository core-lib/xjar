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
        byte[] sha1 = XKit.sha1(xJar);

        byte[] algorithm = xKey.getAlgorithm().getBytes(StandardCharsets.UTF_8);
        byte[] keysize = String.valueOf(xKey.getKeysize()).getBytes(StandardCharsets.UTF_8);
        byte[] ivsize = String.valueOf(xKey.getIvsize()).getBytes(StandardCharsets.UTF_8);
        byte[] password = xKey.getPassword().getBytes(StandardCharsets.UTF_8);

        Map<String, String> variables = new HashMap<>();
        variables.put("xJar.md5", convert(md5));
        variables.put("xJar.sha1", convert(sha1));
        variables.put("xKey.algorithm", convert(algorithm));
        variables.put("xKey.keysize", convert(keysize));
        variables.put("xKey.ivsize", convert(ivsize));
        variables.put("xKey.password", convert(password));

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

    private static String convert(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(b & 0xFF);
        }
        return builder.toString();
    }

}

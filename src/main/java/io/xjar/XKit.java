package io.xjar;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.*;

public abstract class XKit {

    public static void close(Closeable closeable) {
        try {
            close(closeable, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable closeable, boolean quietly) throws IOException {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            if (!quietly) throw e;
        }
    }

    public static long transfer(InputStream in, OutputStream out) throws IOException {
        long total = 0;
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            total += length;
        }
        out.flush();
        return total;
    }

    public static long transfer(Reader reader, Writer writer) throws IOException {
        long total = 0;
        char[] buffer = new char[4096];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, length);
            total += length;
        }
        writer.flush();
        return total;
    }

    public static long transfer(InputStream in, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            return transfer(in, out);
        } finally {
            close(out);
        }
    }

    public static long transfer(Reader reader, File file) throws IOException {
        OutputStream out = null;
        Writer writer = null;
        try {
            out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out);
            return transfer(reader, writer);
        } finally {
            close(writer);
            close(out);
        }
    }

    public static boolean delete(File file) {
        return delete(file, false);
    }

    public static boolean delete(File file, boolean recursively) {
        if (file.isDirectory() && recursively) {
            boolean deleted = true;
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                deleted &= delete(files[i], true);
            }
            return deleted && file.delete();
        } else {
            return file.delete();
        }
    }

    public static void pack(String source, String target) throws IOException {
        pack(new File(source), new File(target));
    }

    public static void pack(File source, File target) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(target)) {
            pack(source, outputStream);
        }
    }

    public static void pack(File source, OutputStream out) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
            pack(source, zipOutputStream);
        }
    }

    public static void pack(File source, ZipOutputStream zipOutputStream) throws IOException {
        Queue<String> parents = new LinkedList<>();
        Queue<File> files = new LinkedList<>();

        if (source.exists()) {
            parents.offer(null);
            files.offer(source);
        }

        while (!files.isEmpty()) {
            File file = files.poll();
            String parent = parents.poll();

            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (int i = 0; children != null && i < children.length; i++) {
                    File child = children[i];
                    parents.offer(parent == null ? "" : parent.isEmpty() ? file.getName() : parent + "/" + file.getName());
                    files.offer(child);
                }
            } else {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    if (file.getName().endsWith(".jar")) {
                        ZipEntry zipEntry = new ZipEntry(parent == null || parent.isEmpty() ? file.getName() : parent + "/" + file.getName());
                        zipEntry.setMethod(ZipEntry.STORED);
                        zipEntry.setSize(file.length());
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                        XKit.transfer(fileInputStream, cos);
                        zipEntry.setCrc(cos.getChecksum().getValue());
                        zipOutputStream.putNextEntry(zipEntry);
                        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                        XKit.transfer(bis, zipOutputStream);
                    } else {
                        ZipEntry zipEntry = new ZipEntry(parent == null || parent.isEmpty() ? file.getName() : parent + "/" + file.getName());
                        zipEntry.setTime(file.lastModified());
                        zipOutputStream.putNextEntry(zipEntry);
                        XKit.transfer(fileInputStream, zipOutputStream);
                    }
                    zipOutputStream.closeEntry();
                } finally {
                    XKit.close(fileInputStream);
                }
            }
        }
    }

    public static void unpack(String source, String target) throws IOException {
        unpack(new File(source), new File(target));
    }

    public static void unpack(File source, File target) throws IOException {
        try (InputStream inputStream = new FileInputStream(source)) {
            unpack(inputStream, target);
        }
    }

    public static void unpack(InputStream in, File target) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(in)) {
            unpack(zipInputStream, target);
        }
    }

    public static void unpack(ZipInputStream zipInputStream, File target) throws IOException {
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.isDirectory()) {
                File directory = new File(target, zipEntry.getName());
                if (!directory.exists() && !directory.mkdirs()) {
                    throw new IOException("could not make directory: " + directory);
                }
                continue;
            }
            FileOutputStream fileOutputStream = null;
            try {
                File file = new File(target, zipEntry.getName());
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw new IOException("could not make directory: " + file.getParentFile());
                }
                fileOutputStream = new FileOutputStream(file);
                XKit.transfer(zipInputStream, fileOutputStream);
            } finally {
                XKit.close(fileOutputStream);
            }
        }
    }

}

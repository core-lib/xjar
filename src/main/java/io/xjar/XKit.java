package io.xjar;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.Stack;

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
        try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(out)) {
            pack(source, zipArchiveOutputStream);
        }
    }

    public static void pack(File source, ZipArchiveOutputStream zipArchiveOutputStream) throws IOException {
        Stack<String> parents = new Stack<>();
        Stack<File> files = new Stack<>();

        if (source.exists()) {
            parents.push(null);
            files.push(source);
        }

        while (!files.isEmpty()) {
            File file = files.pop();
            String parent = parents.pop();

            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (int i = 0; children != null && i < children.length; i++) {
                    File child = children[i];
                    parents.push(parent == null ? file.getName() : parent + File.separator + file.getName());
                    files.push(child);
                }
            } else {
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, parent == null ? file.getName() : parent + File.separator + file.getName());
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    XKit.transfer(fileInputStream, zipArchiveOutputStream);
                    zipArchiveOutputStream.closeArchiveEntry();
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
        try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(in)) {
            unpack(zipArchiveInputStream, target);
        }
    }

    public static void unpack(ZipArchiveInputStream zipArchiveInputStream, File target) throws IOException {
        ZipArchiveEntry zipArchiveEntry;
        while ((zipArchiveEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
            if (zipArchiveEntry.isDirectory()) {
                File directory = new File(target, zipArchiveEntry.getName());
                if (!directory.exists() && !directory.mkdirs()) {
                    throw new IOException("could not make directory: " + directory);
                }
                continue;
            }
            FileOutputStream fileOutputStream = null;
            try {
                File file = new File(target, zipArchiveEntry.getName());
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw new IOException("could not make directory: " + file.getParentFile());
                }
                fileOutputStream = new FileOutputStream(file);
                XKit.transfer(zipArchiveInputStream, fileOutputStream);
            } finally {
                XKit.close(fileOutputStream);
            }
        }
    }

}

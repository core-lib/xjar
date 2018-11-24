package io.xjar;

import java.io.*;
import java.util.jar.Manifest;

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

    public static Manifest manifest(InputStream in) {
        try {
            return new Manifest(in);
        } catch (IOException e) {
            return null;
        }
    }

}

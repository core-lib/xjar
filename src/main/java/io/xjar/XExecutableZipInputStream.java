//package io.xjar;
//
//import org.apache.commons.compress.archivers.zip.ZipLong;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
///**
// * 可执行的Zip包输入流
// *
// * @author Payne 646742615@qq.com
// * 2018/12/6 15:38
// */
//public class XExecutableZipInputStream extends InputStream {
//    private final InputStream in;
//    private final byte[] buf = new byte[30];
//    private int len = 0;
//    private int pos = 0;
//
//    public XExecutableZipInputStream(InputStream in) throws IOException {
//        this(in, null);
//    }
//
//    public XExecutableZipInputStream(InputStream in, OutputStream out) throws IOException {
//        this.in = in;
//        int length;
//        while ((length = in.read(buf)) != -1) {
//            len = length;
//
//            ZipLong zipLong = new ZipLong(buf);
//            if (ZipLong.LFH_SIG.equals(zipLong)) {
//                break;
//            }
//
//            byte[] line = XKit.readln(in);
//            if (out != null) {
//                out.write(buf, 0, length);
//                XKit.writeln(line, out);
//            }
//        }
//    }
//
//    @Override
//    public int read() throws IOException {
//        if (pos < len) {
//            return buf[pos++];
//        }
//        return in.read();
//    }
//
//    @Override
//    public int read(byte[] b) throws IOException {
//        int off = 0;
//        if (pos < len) {
//            off = Math.min(len - pos, b.length);
//            for (int i = 0; i < off; i++) {
//                b[i] = buf[pos++];
//            }
//        }
//        return off == b.length ? off : off + in.read(b, off, b.length - off);
//    }
//
//    @Override
//    public int read(byte[] b, int _off, int _len) throws IOException {
//        if (_off < 0 || _len < 0 || _len > b.length - _off) {
//            throw new IndexOutOfBoundsException();
//        }
//
//        int off = 0;
//        if (pos < len) {
//            off = Math.min(len - pos, _len);
//            for (int i = 0; i < off; i++) {
//                b[_off + i] = buf[pos++];
//            }
//        }
//        return off == _len ? off : off + in.read(b, _off + off, _len - off);
//    }
//
//    @Override
//    public long skip(long n) throws IOException {
//        return in.skip(n);
//    }
//
//    @Override
//    public int available() throws IOException {
//        return in.available();
//    }
//
//    @Override
//    public void close() throws IOException {
//        in.close();
//    }
//
//    @Override
//    public void mark(int readlimit) {
//        in.mark(readlimit);
//    }
//
//    @Override
//    public void reset() throws IOException {
//        in.reset();
//    }
//
//    @Override
//    public boolean markSupported() {
//        return in.markSupported();
//    }
//
//}

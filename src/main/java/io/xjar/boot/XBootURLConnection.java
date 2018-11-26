package io.xjar.boot;

import io.xjar.XDecryptor;
import io.xjar.XEncryptor;
import io.xjar.key.XKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 加密的URL连接
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 13:57
 */
public class XBootURLConnection extends JarURLConnection {
    private final JarURLConnection jarURLConnection;
    private final XDecryptor xDecryptor;
    private final XEncryptor xEncryptor;
    private final XKey xKey;

    public XBootURLConnection(JarURLConnection jarURLConnection, XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey) throws MalformedURLException {
        super(jarURLConnection.getURL());
        this.jarURLConnection = jarURLConnection;
        this.xDecryptor = xDecryptor;
        this.xEncryptor = xEncryptor;
        this.xKey = xKey;
    }

    @Override
    public void connect() throws IOException {
        jarURLConnection.connect();
    }

    @Override
    public int getConnectTimeout() {
        return jarURLConnection.getConnectTimeout();
    }

    @Override
    public void setConnectTimeout(int timeout) {
        jarURLConnection.setConnectTimeout(timeout);
    }

    @Override
    public int getReadTimeout() {
        return jarURLConnection.getReadTimeout();
    }

    @Override
    public void setReadTimeout(int timeout) {
        jarURLConnection.setReadTimeout(timeout);
    }

    @Override
    public URL getURL() {
        return jarURLConnection.getURL();
    }

    @Override
    public int getContentLength() {
        return jarURLConnection.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return jarURLConnection.getContentLengthLong();
    }

    @Override
    public String getContentType() {
        return jarURLConnection.getContentType();
    }

    @Override
    public String getContentEncoding() {
        return jarURLConnection.getContentEncoding();
    }

    @Override
    public long getExpiration() {
        return jarURLConnection.getExpiration();
    }

    @Override
    public long getDate() {
        return jarURLConnection.getDate();
    }

    @Override
    public long getLastModified() {
        return jarURLConnection.getLastModified();
    }

    @Override
    public String getHeaderField(String name) {
        return jarURLConnection.getHeaderField(name);
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return jarURLConnection.getHeaderFields();
    }

    @Override
    public int getHeaderFieldInt(String name, int Default) {
        return jarURLConnection.getHeaderFieldInt(name, Default);
    }

    @Override
    public long getHeaderFieldLong(String name, long Default) {
        return jarURLConnection.getHeaderFieldLong(name, Default);
    }

    @Override
    public long getHeaderFieldDate(String name, long Default) {
        return jarURLConnection.getHeaderFieldDate(name, Default);
    }

    @Override
    public String getHeaderFieldKey(int n) {
        return jarURLConnection.getHeaderFieldKey(n);
    }

    @Override
    public String getHeaderField(int n) {
        return jarURLConnection.getHeaderField(n);
    }

    @Override
    public Object getContent() throws IOException {
        return jarURLConnection.getContent();
    }

    @Override
    public Object getContent(Class[] classes) throws IOException {
        return jarURLConnection.getContent(classes);
    }

    @Override
    public Permission getPermission() throws IOException {
        return jarURLConnection.getPermission();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream in = jarURLConnection.getInputStream();
        return xDecryptor.decrypt(xKey, in);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        OutputStream out = jarURLConnection.getOutputStream();
        return xEncryptor.encrypt(xKey, out);
    }

    @Override
    public String toString() {
        return jarURLConnection.toString();
    }

    @Override
    public boolean getDoInput() {
        return jarURLConnection.getDoInput();
    }

    @Override
    public void setDoInput(boolean doInput) {
        jarURLConnection.setDoInput(doInput);
    }

    @Override
    public boolean getDoOutput() {
        return jarURLConnection.getDoOutput();
    }

    @Override
    public void setDoOutput(boolean doOutput) {
        jarURLConnection.setDoOutput(doOutput);
    }

    @Override
    public boolean getAllowUserInteraction() {
        return jarURLConnection.getAllowUserInteraction();
    }

    @Override
    public void setAllowUserInteraction(boolean allowUserInteraction) {
        jarURLConnection.setAllowUserInteraction(allowUserInteraction);
    }

    @Override
    public boolean getUseCaches() {
        return jarURLConnection.getUseCaches();
    }

    @Override
    public void setUseCaches(boolean useCaches) {
        jarURLConnection.setUseCaches(useCaches);
    }

    @Override
    public long getIfModifiedSince() {
        return jarURLConnection.getIfModifiedSince();
    }

    @Override
    public void setIfModifiedSince(long ifModifiedSince) {
        jarURLConnection.setIfModifiedSince(ifModifiedSince);
    }

    @Override
    public boolean getDefaultUseCaches() {
        return jarURLConnection.getDefaultUseCaches();
    }

    @Override
    public void setDefaultUseCaches(boolean defaultUseCaches) {
        jarURLConnection.setDefaultUseCaches(defaultUseCaches);
    }

    @Override
    public void setRequestProperty(String key, String value) {
        jarURLConnection.setRequestProperty(key, value);
    }

    @Override
    public void addRequestProperty(String key, String value) {
        jarURLConnection.addRequestProperty(key, value);
    }

    @Override
    public String getRequestProperty(String key) {
        return jarURLConnection.getRequestProperty(key);
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
        return jarURLConnection.getRequestProperties();
    }

    @Override
    public URL getJarFileURL() {
        return jarURLConnection.getJarFileURL();
    }

    @Override
    public String getEntryName() {
        return jarURLConnection.getEntryName();
    }

    @Override
    public JarFile getJarFile() throws IOException {
        return jarURLConnection.getJarFile();
    }

    @Override
    public Manifest getManifest() throws IOException {
        return jarURLConnection.getManifest();
    }

    @Override
    public JarEntry getJarEntry() throws IOException {
        return jarURLConnection.getJarEntry();
    }

    @Override
    public Attributes getAttributes() throws IOException {
        return jarURLConnection.getAttributes();
    }

    @Override
    public Attributes getMainAttributes() throws IOException {
        return jarURLConnection.getMainAttributes();
    }

    @Override
    public Certificate[] getCertificates() throws IOException {
        return jarURLConnection.getCertificates();
    }
}

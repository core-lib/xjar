package io.xjar.loader;

import io.xjar.XDecryptor;
import io.xjar.XEncryptor;
import io.xjar.key.XKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.List;
import java.util.Map;

/**
 * 加密的URL连接
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 13:57
 */
public class XURLConnection extends URLConnection {
    private final URLConnection urlConnection;
    private final XDecryptor xDecryptor;
    private final XEncryptor xEncryptor;
    private final XKey xKey;

    public XURLConnection(URLConnection urlConnection, XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey) {
        super(urlConnection.getURL());
        this.urlConnection = urlConnection;
        this.xDecryptor = xDecryptor;
        this.xEncryptor = xEncryptor;
        this.xKey = xKey;
    }

    @Override
    public void connect() throws IOException {
        urlConnection.connect();
    }

    @Override
    public int getConnectTimeout() {
        return urlConnection.getConnectTimeout();
    }

    @Override
    public void setConnectTimeout(int timeout) {
        urlConnection.setConnectTimeout(timeout);
    }

    @Override
    public int getReadTimeout() {
        return urlConnection.getReadTimeout();
    }

    @Override
    public void setReadTimeout(int timeout) {
        urlConnection.setReadTimeout(timeout);
    }

    @Override
    public URL getURL() {
        return urlConnection.getURL();
    }

    @Override
    public int getContentLength() {
        return urlConnection.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return urlConnection.getContentLengthLong();
    }

    @Override
    public String getContentType() {
        return urlConnection.getContentType();
    }

    @Override
    public String getContentEncoding() {
        return urlConnection.getContentEncoding();
    }

    @Override
    public long getExpiration() {
        return urlConnection.getExpiration();
    }

    @Override
    public long getDate() {
        return urlConnection.getDate();
    }

    @Override
    public long getLastModified() {
        return urlConnection.getLastModified();
    }

    @Override
    public String getHeaderField(String name) {
        return urlConnection.getHeaderField(name);
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return urlConnection.getHeaderFields();
    }

    @Override
    public int getHeaderFieldInt(String name, int Default) {
        return urlConnection.getHeaderFieldInt(name, Default);
    }

    @Override
    public long getHeaderFieldLong(String name, long Default) {
        return urlConnection.getHeaderFieldLong(name, Default);
    }

    @Override
    public long getHeaderFieldDate(String name, long Default) {
        return urlConnection.getHeaderFieldDate(name, Default);
    }

    @Override
    public String getHeaderFieldKey(int n) {
        return urlConnection.getHeaderFieldKey(n);
    }

    @Override
    public String getHeaderField(int n) {
        return urlConnection.getHeaderField(n);
    }

    @Override
    public Object getContent() throws IOException {
        return urlConnection.getContent();
    }

    @Override
    public Object getContent(Class[] classes) throws IOException {
        return urlConnection.getContent(classes);
    }

    @Override
    public Permission getPermission() throws IOException {
        return urlConnection.getPermission();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream in = urlConnection.getInputStream();
        return xDecryptor.decrypt(xKey, in);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        OutputStream out = urlConnection.getOutputStream();
        return xEncryptor.encrypt(xKey, out);
    }

    @Override
    public String toString() {
        return urlConnection.toString();
    }

    @Override
    public boolean getDoInput() {
        return urlConnection.getDoInput();
    }

    @Override
    public void setDoInput(boolean doinput) {
        urlConnection.setDoInput(doinput);
    }

    @Override
    public boolean getDoOutput() {
        return urlConnection.getDoOutput();
    }

    @Override
    public void setDoOutput(boolean dooutput) {
        urlConnection.setDoOutput(dooutput);
    }

    @Override
    public boolean getAllowUserInteraction() {
        return urlConnection.getAllowUserInteraction();
    }

    @Override
    public void setAllowUserInteraction(boolean allowuserinteraction) {
        urlConnection.setAllowUserInteraction(allowuserinteraction);
    }

    @Override
    public boolean getUseCaches() {
        return urlConnection.getUseCaches();
    }

    @Override
    public void setUseCaches(boolean usecaches) {
        urlConnection.setUseCaches(usecaches);
    }

    @Override
    public long getIfModifiedSince() {
        return urlConnection.getIfModifiedSince();
    }

    @Override
    public void setIfModifiedSince(long ifmodifiedsince) {
        urlConnection.setIfModifiedSince(ifmodifiedsince);
    }

    @Override
    public boolean getDefaultUseCaches() {
        return urlConnection.getDefaultUseCaches();
    }

    @Override
    public void setDefaultUseCaches(boolean defaultusecaches) {
        urlConnection.setDefaultUseCaches(defaultusecaches);
    }

    @Override
    public void setRequestProperty(String key, String value) {
        urlConnection.setRequestProperty(key, value);
    }

    @Override
    public void addRequestProperty(String key, String value) {
        urlConnection.addRequestProperty(key, value);
    }

    @Override
    public String getRequestProperty(String key) {
        return urlConnection.getRequestProperty(key);
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
        return urlConnection.getRequestProperties();
    }
}

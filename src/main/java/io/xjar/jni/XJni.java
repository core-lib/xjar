package io.xjar.jni;

import io.xjar.XTool;
import io.xjar.key.XKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * JNI
 *
 * @author Payne 646742615@qq.com
 * 2019/7/10 13:19
 */
public class XJni {
    private static volatile XJni instance;

    static {
        System.load("C:\\Users\\Chang\\source\\repos\\Project1\\XJni.so");
    }

    private XJni() {

    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static XJni getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (XJni.class) {
            if (instance != null) {
                return instance;
            }
            instance = new XJni();
        }
        return instance;
    }

    /**
     * 编码
     *
     * @param xKey 密钥
     * @return 密钥编码数据
     */
    public byte[] encode(XKey xKey) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(xKey);
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            XTool.close(oos);
            XTool.close(bos);
        }
    }

    /**
     * 解码
     *
     * @param xKey 密钥编码数据
     * @return 密钥
     */
    public XKey decode(byte[] xKey) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(xKey);
            ois = new ObjectInputStream(bis);
            Object xObj = ois.readObject();
            return (XKey) xObj;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            XTool.close(ois);
            XTool.close(bis);
        }
    }

    public void compile(XKey xKey, String dir) {

    }

    public XKey execute() {

    }

    public native byte[] read();

    public XKey key() {
        byte[] xKey = read();
        return decode(xKey);
    }

}

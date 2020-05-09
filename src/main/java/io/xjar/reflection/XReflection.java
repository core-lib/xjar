package io.xjar.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author Payne 646742615@qq.com
 * 2020/5/9 10:27
 */
public class XReflection {

    public static XField field(Class<?> claxx, String name) throws NoSuchFieldException {
        if (claxx == null) throw new NullPointerException();
        if (name == null) throw new IllegalArgumentException("field name == null");
        while (claxx != null) {
            try {
                Field field = claxx.getDeclaredField(name);
                field.setAccessible(true);
                return new XField(field);
            } catch (NoSuchFieldException e) {
                claxx = claxx.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name);
    }

    public static XMethod method(Class<?> claxx, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        if (claxx == null) throw new NullPointerException();
        if (name == null) throw new IllegalArgumentException("method name == null");
        while (claxx != null) {
            try {
                Method method = claxx.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return new XMethod(method);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                claxx = claxx.getSuperclass();
            }
        }
        throw new NoSuchMethodException(name);
    }

}

package io.xjar.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法
 *
 * @author Payne 646742615@qq.com
 * 2020/5/9 11:04
 */
public class XMethod {
    private final Method method;

    XMethod(Method method) {
        this.method = method;
    }

    public XValue invoke(Object target, Object... args) throws InvocationTargetException {
        try {
            return new XValue(method.invoke(target, args));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Method method() {
        return method;
    }
}

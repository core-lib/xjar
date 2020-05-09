package io.xjar.reflection;

/**
 * 值
 *
 * @author Payne 646742615@qq.com
 * 2020/5/9 11:19
 */
public class XValue {
    private final Object value;

    XValue(Object value) {
        this.value = value;
    }

    public XField field(String name) throws NoSuchFieldException {
        return XReflection.field(value.getClass(), name);
    }

    public XMethod method(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        return XReflection.method(value.getClass(), name, parameterTypes);
    }

    public <T> T value() {
        return (T) value;
    }

}

package io.xjar.reflection;

import java.lang.reflect.Field;

/**
 * 字段
 *
 * @author Payne 646742615@qq.com
 * 2020/5/9 11:04
 */
public class XField {
    private final Field field;

    XField(Field field) {
        this.field = field;
    }

    public XValue get(Object target) {
        try {
            return new XValue(field.get(target));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void set(Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Field field() {
        return field;
    }
}

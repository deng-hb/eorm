package com.denghb.eorm.utils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 反射工具类
 */
public class ReflectUtils {

    /**
     * 获得实体类的所有属性（该方法递归的获取当前类及父类中声明的字段。最终结果以list形式返回）
     */
    public static List<Field> getFields(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        List<Field> fields = new ArrayList<Field>();
        Field[] classFields = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(classFields));

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            List<Field> superClassFields = getFields(superclass);
            fields.addAll(superClassFields);
        }
        return fields;
    }

    /**
     * 通过属性对象和实体对象获取字段的值
     */
    public static Object getFieldValue(Field field, Object object) {
        if (field == null || object == null) {
            return null;
        }

        try {
            // 反射的对象在使用时应该取消 Java 语言访问检查,（用作于反射字段时对字段作用域不检查
            // 例如访问private类型和protected类型的字段）
            field.setAccessible(true);
            return field.get(object);// 获取字段的值
        } catch (Exception e) {
            throw new RuntimeException("Can't get field (" + field.getName() + ") value from object " + object, e);
        }
    }

    /**
     * 将值保存到实体对象的指定属性中
     */
    public static void setFieldValue(Field field, Object object, Object value) throws RuntimeException {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException("Can't set value（" + value + "）to instance " + object.getClass().getName() + "." + field.getName(), e);
        }
    }

    /**
     * 根据实体类创建实体对象
     */
    public static Object createInstance(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Object class mustn't be null");
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can't create instance（" + clazz.getName() + "）  by reflect!", e);
        }
    }

}

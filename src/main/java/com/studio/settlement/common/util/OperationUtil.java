package com.studio.settlement.common.util;

import io.swagger.v3.oas.annotations.media.Schema; // 改为 SpringDoc 注解
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 操作工具类
 * 用于比较对象属性差异和获取Swagger注解信息
 */
@Slf4j // 使用Lombok的日志注解
public final class OperationUtil {

    private OperationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 获取两个对象属性值不同的字段列表
     *
     * @param objOld 旧对象
     * @param objNew 新对象
     * @return 包含属性值不同的字段列表
     */
    public static List<Field> getChangedFields(Object objOld, Object objNew) {
        if (objOld == null || objNew == null) {
            log.warn("比较对象不能为null");
            throw new IllegalArgumentException("比较对象不能为null");
        }

        Class<?> clazz = objOld.getClass();
        if (!clazz.equals(objNew.getClass())) {
            log.warn("比较对象类型必须相同: {} != {}", clazz, objNew.getClass());
            throw new IllegalArgumentException("比较对象类型必须相同");
        }

        List<Field> allFields = Arrays.asList(clazz.getDeclaredFields());
        List<Field> changedFields = new ArrayList<>();

        for (Field field : allFields) {
            field.setAccessible(true);
            try {
                Object oldValue = field.get(objOld);
                Object newValue = field.get(objNew);

                if (!java.util.Objects.equals(oldValue, newValue)) {
                    if (log.isDebugEnabled()) {
                        log.debug("属性值不同: {} : {} -> {}", field.getName(), oldValue, newValue);
                    }
                    changedFields.add(field);
                }
            } catch (IllegalAccessException e) {
                log.error("访问字段失败: {}", field.getName(), e);
            }
        }
        return changedFields;
    }

    /**
     * 获取Schema注解中description属性的值
     *
     * @param field 要获取Schema注解的Field对象
     * @return Schema注解中description属性的值
     */
    public static Optional<String> getSchemaDescription(Field field) {
        if (field == null) {
            return Optional.empty();
        }

        Schema schema = field.getAnnotation(Schema.class);
        if (schema != null) {
            return Optional.ofNullable(schema.description()).filter(s -> !s.isEmpty());
        }
        return Optional.empty();
    }

    /**
     * 获取Schema注解的description值，提供默认值
     *
     * @param field        要检查的字段
     * @param defaultValue 默认值
     * @return 注解的description值，如果不存在则返回默认值
     */
    public static String getSchemaDescription(Field field, String defaultValue) {
        return getSchemaDescription(field).orElse(defaultValue);
    }

    /**
     * 获取Schema注解的description值，或使用字段名作为回退
     *
     * @param field 要检查的字段
     * @return 注解的description值，或字段名称
     */
    public static String getSchemaDescriptionOrFieldName(Field field) {
        return getSchemaDescription(field).orElse(field.getName());
    }
}

package io.coodoo.framework.export.control;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.coodoo.framework.export.boundary.annotation.ExportBooleanLabels;
import io.coodoo.framework.export.boundary.annotation.ExportColumn;
import io.coodoo.framework.export.boundary.annotation.ExportDateTimePattern;
import io.coodoo.framework.export.boundary.annotation.ExportIgnoreField;
import io.coodoo.framework.export.boundary.annotation.ExportTitle;

public final class FileExportUtil {

    private FileExportUtil() {}

    public static List<Field> getFields(Class<?> targetClass) {

        List<Field> fields = new ArrayList<>();

        Class<?> inheritanceClass = targetClass;
        while (inheritanceClass != null) {
            fields.addAll(Arrays.asList(inheritanceClass.getDeclaredFields()).stream()
                            .filter(field -> !field.isAnnotationPresent(ExportIgnoreField.class) && !Collection.class.isAssignableFrom(field.getType())
                                            && (field.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC && (field.getModifiers() & Modifier.STATIC) == 0)
                            .collect(Collectors.toList()));
            inheritanceClass = inheritanceClass.getSuperclass();
        }
        return fields;
    }

    public static String getFieldName(Field field) {
        if (field.isAnnotationPresent(ExportColumn.class)) {
            String fieldName = field.getAnnotation(ExportColumn.class).value();
            if (notEmpty(fieldName)) {
                return fieldName;
            }
        }
        return Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
    }

    public static String getTitle(Field field) {
        if (field.isAnnotationPresent(ExportTitle.class)) {
            return field.getAnnotation(ExportTitle.class).value();
        }
        return null;
    }

    public static String getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null) {

                if (field.isAnnotationPresent(ExportDateTimePattern.class)) {
                    String dateTimePattern = field.getAnnotation(ExportDateTimePattern.class).value();
                    if (value instanceof LocalDateTime) {
                        return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern(dateTimePattern));
                    }
                    if (value instanceof Date) {
                        return new SimpleDateFormat(dateTimePattern).format(((Date) value));
                    }
                } else if (value != null && field.isAnnotationPresent(ExportBooleanLabels.class) && value instanceof Boolean) {
                    if ((boolean) value) {
                        return field.getAnnotation(ExportBooleanLabels.class).trueLabel();
                    } else {
                        return field.getAnnotation(ExportBooleanLabels.class).falseLabel();
                    }
                } else {
                    return value.toString();
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
        return null;
    }

    public static String getCsvValue(Field field, Object object) {
        String fieldValue = getFieldValue(field, object);

        if (notEmpty(fieldValue)) {
            if (fieldValue.contains(FileExportConfig.CSV_SEPARATOR)) {
                return FileExportConfig.CSV_QUOTES + fieldValue + FileExportConfig.CSV_QUOTES;
            }
            return fieldValue;
        }
        return "";
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean notEmpty(String value) {
        return !isEmpty(value);
    }

}

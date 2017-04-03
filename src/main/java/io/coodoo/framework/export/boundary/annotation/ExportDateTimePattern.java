package io.coodoo.framework.export.boundary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date pattern for the value representation in export file
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportDateTimePattern {

    String value() default "dd.MM.yyyy HH:mm";

}

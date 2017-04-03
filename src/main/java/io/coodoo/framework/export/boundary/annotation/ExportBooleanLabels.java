package io.coodoo.framework.export.boundary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

/**
 * Labels for the boolean value representation in export file
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportBooleanLabels {

    @Nonbinding
    String trueLabel() default "X";

    @Nonbinding
    String falseLabel() default "";

}

package com.sky.annotation;

import com.sky.enumeration.OperationType;
import io.swagger.annotations.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoFill {
    /**
     * 操作类型
     * @return
     */
    OperationType value() default OperationType.INSERT;

}

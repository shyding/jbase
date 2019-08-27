package com.jayqqaa12.j2cache.spring.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Repeatable(CacheClearArray.class)
public @interface CacheClear {
    String region() default "";
    String key();

}

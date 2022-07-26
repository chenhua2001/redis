package com.ch.cache.inteceptor;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercept {
    boolean evict() default false;
    boolean aof() default false;
    boolean refresh() default false;
}

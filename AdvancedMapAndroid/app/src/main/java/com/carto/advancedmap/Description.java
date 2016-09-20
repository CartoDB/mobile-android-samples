package com.carto.advancedmap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by aareundo on 31/08/16.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface Description {
    public String value();
}


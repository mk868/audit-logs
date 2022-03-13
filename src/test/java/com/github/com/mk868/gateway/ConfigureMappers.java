package com.github.com.mk868.gateway;

import com.github.com.mk868.gateway.mapper.AppMapperConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;

/**
 * Load beans generated by the mapstruct
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(
    basePackageClasses = AppMapperConfig.class
)
public @interface ConfigureMappers {

}
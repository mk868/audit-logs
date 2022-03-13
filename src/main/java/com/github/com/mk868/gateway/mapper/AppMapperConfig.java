package com.github.com.mk868.gateway.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = "spring",
    implementationPackage = "com.github.com.mk868.gateway.mapper",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AppMapperConfig {

}

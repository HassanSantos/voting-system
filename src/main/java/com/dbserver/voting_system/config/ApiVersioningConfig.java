package com.dbserver.voting_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    private static final String API_PREFIX = "/api/v1";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(API_PREFIX, this::shouldApplyPrefix);
    }

    private boolean shouldApplyPrefix(Class<?> controllerClass) {
        if (!AnnotatedElementUtils.hasAnnotation(controllerClass, RestController.class)) {
            return false;
        }

        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);
        if (requestMapping == null) {
            return true;
        }

        return Stream.concat(Arrays.stream(requestMapping.path()), Arrays.stream(requestMapping.value()))
                .filter(Objects::nonNull)
                .filter(path -> !path.isBlank())
                .noneMatch(path -> path.startsWith("/api/"));
    }
}

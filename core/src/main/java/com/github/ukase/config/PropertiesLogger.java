package com.github.ukase.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.*;

import java.util.*;

@Slf4j
@Configurable
public class PropertiesLogger implements ApplicationListener<ApplicationPreparedEvent> {

    private ConfigurableEnvironment environment;
    private boolean isFirstRun = true;

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        if (isFirstRun) {
            environment = event.getApplicationContext().getEnvironment();
            printProperties();
        }
        isFirstRun = false;
    }

    public void printProperties() {
        for (var propertySource : findPropertiesPropertySources()) {
            log.info("******* " + propertySource.getName() + " *******");
            String[] propertyNames = propertySource.getPropertyNames();
            Arrays.sort(propertyNames);
            for (String propertyName : propertyNames) {
                String resolvedProperty = Optional
                        .ofNullable(environment.getProperty(propertyName))
                        .orElseGet(() -> "");
                String sourceProperty = Optional
                        .ofNullable(propertySource.getProperty(propertyName))
                        .orElseGet(() -> "")
                        .toString();
                if (resolvedProperty.equals(sourceProperty)) {
                    log.info("{}={}", propertyName, resolvedProperty);
                } else {
                    log.info("{}={} OVERRIDDEN to {}", propertyName, sourceProperty, resolvedProperty);
                }
            }
        }
    }

    private List<EnumerablePropertySource> findPropertiesPropertySources() {
        List<EnumerablePropertySource> propertiesPropertySources = new LinkedList<>();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                propertiesPropertySources.add((EnumerablePropertySource) propertySource);
            }
        }
        return propertiesPropertySources;
    }
}
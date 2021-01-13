package com.github.ukase.example.config;

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
        for (EnumerablePropertySource propertySource : findPropertiesPropertySources()) {
            log.info("******* " + propertySource.getName() + " *******");
            String[] propertyNames = propertySource.getPropertyNames();
            Arrays.sort(propertyNames);
            for (String propertyName : propertyNames) {
                String resolvedProperty = environment.getProperty(propertyName);
                String sourceProperty = propertySource.getProperty(propertyName).toString();
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



/*{

    @Autowired
    private AbstractEnvironment environment;

    @PostConstruct
    public void printProperties() {

        log.info("**** APPLICATION PROPERTIES SOURCES ****");

        Set<String> properties = new TreeSet<>();
        for (PropertiesPropertySource p : findPropertiesPropertySources()) {
            log.info(p.toString());
            properties.addAll(Arrays.asList(p.getPropertyNames()));
        }

        log.info("**** APPLICATION PROPERTIES VALUES ****");
        print(properties);

    }

    private List<PropertiesPropertySource> findPropertiesPropertySources() {
        List<PropertiesPropertySource> propertiesPropertySources = new LinkedList<>();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof PropertiesPropertySource) {
                propertiesPropertySources.add((PropertiesPropertySource) propertySource);
            }
        }
        return propertiesPropertySources;
    }

    private void print(Set<String> properties) {
        for (String propertyName : properties) {
            log.info("{}={}", propertyName, environment.getProperty(propertyName));
        }
    }

}
*/
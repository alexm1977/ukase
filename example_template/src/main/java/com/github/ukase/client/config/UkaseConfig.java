package com.github.ukase.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UkaseConfig {

    @Bean
    public AsyncTaskExecutor reportTaskExecutor(
            @Value("${ukase.task-executor.core-pool-size}") int corePoolSize,
            @Value("${ukase.task-executor.max-pool-size}") int maxPoolSize
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setThreadNamePrefix("ukase-task-");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);

        return executor;
    }

    @Bean
    public RestTemplate ukaseRestTemplate(
            @Value("${ukase.login}") String login,
            @Value("${ukase.password}") String password,
            ObjectMapper jsonObjectMapper
    ) {
        return restTemplate(login, password, jsonObjectMapper);
    }

    private RestTemplate restTemplate(
            String login,
            String password,
            ObjectMapper jsonObjectMapper
    ) {
        AuthHttpComponentsClientHttpRequestFactory reqFactory = new AuthHttpComponentsClientHttpRequestFactory(
                login,
                password
        );
        RestTemplate restTemplate = new RestTemplate(reqFactory);

        for (HttpMessageConverter<?> conv : restTemplate.getMessageConverters()) {
            if (conv instanceof AbstractJackson2HttpMessageConverter) {
                ((AbstractJackson2HttpMessageConverter) conv).setObjectMapper(jsonObjectMapper);
            }
        }

        return restTemplate;
    }
}

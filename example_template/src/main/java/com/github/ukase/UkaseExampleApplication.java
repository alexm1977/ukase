package com.github.ukase;

import com.github.ukase.example.config.PropertiesLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"com.github.ukase"}
)
public class UkaseExampleApplication /*extends SpringBootServletInitializer*/ {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UkaseExampleApplication.class);
        application.addListeners(new PropertiesLogger());
        application.run(args);
    }

}

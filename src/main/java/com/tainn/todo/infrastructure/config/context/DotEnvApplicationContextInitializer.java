package com.tainn.todo.infrastructure.config.context;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DotEnvApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Dotenv dotenv = Dotenv.configure().directory("/").load();
        Map<String, Object> dotenvMap = new HashMap<>();

        dotenv.entries().forEach(dotenvEntry -> dotenvMap.put(dotenvEntry.getKey(), dotenvEntry.getValue()));

        // Add these properties to the Spring Environment
        environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvMap));
    }
}

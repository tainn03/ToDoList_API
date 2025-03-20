package com.tainn.todo;

import com.tainn.todo.infrastructure.config.context.DotEnvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@EnableConfigurationProperties
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TodoApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer());
        springApplication.run(args);
    }

}

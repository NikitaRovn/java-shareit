package ru.practicum.shareit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shareit-server")
@Getter
@Setter
public class ServerProperties {
    private String url;
}

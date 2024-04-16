package com.github.aayushjn.travelms.packages.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "travelms")
@Data
public class TravelMsConfig {
    private Services services;

    @Data
    public static class Services {
        private String hotels;
        private String buses;
    }

    public String getHotelsServiceUrl() {
        return "http://" + services.hotels;
    }

    public String getBusesServiceUrl() {
        return "http://" + services.buses;
    }
}

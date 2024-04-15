package com.github.aayushjn.travelms.booking.config;

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
        private String users;
        private String packages;
    }

    public String getUsersServiceUrl() {
        return "http://" + services.users + "/users";
    }

    public String getPackagesServiceUrl() {
        return "http://" + services.packages + "/packages";
    }
}

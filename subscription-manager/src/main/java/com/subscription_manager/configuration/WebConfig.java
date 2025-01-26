package com.subscription_manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration class for setting up Spring Security in the Subscription Manager application.
 *
 * This class defines the security filter chain, specifying which endpoints are publicly accessible
 * and which require authentication. It also configures OAuth2 login settings.
 */
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders(
                                "Access-Control-Allow-Origin",
                                "Access-Control-Allow-Credentials")
                        .allowCredentials(true);
            }
        };
    }
}



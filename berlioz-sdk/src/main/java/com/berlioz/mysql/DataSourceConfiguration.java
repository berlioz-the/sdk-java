package com.berlioz.mysql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
class DataSourceConfiguration {

    DataSourceConfiguration() {

    }

    @Value("${spring.datasource.service:#{null}}")
    public String service;

    @Value("${spring.datasource.endpoint:#{null}}")
    public String endpoint;

    @Value("${spring.datasource.url:#{null}}")
    public String url;

    @Value("${spring.datasource.username:#{null}}")
    public String username;

    @Value("${spring.datasource.password:#{null}}")
    public String password;

    static DataSourceConfiguration fromEnvironment(Environment environment) {
        DataSourceConfiguration config = new DataSourceConfiguration();
        config.url = environment.getProperty("spring.datasource.url");
        config.username = environment.getProperty("spring.datasource.username");
        config.password = environment.getProperty("spring.datasource.password");
        config.service = environment.getProperty("spring.datasource.service");
        config.endpoint = environment.getProperty("spring.datasource.endpoint");
        return config;
    }
}

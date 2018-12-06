package com.samples;

import com.berlioz.http.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import javax.sql.DataSource;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new com.berlioz.spring.Handler());
    }

    @Bean
    RestTemplate restTemplate() {
        return new com.berlioz.http.RestTemplateBuilder()
                .cluster("account")
                .endpoint("api")
                .build();
    }

}

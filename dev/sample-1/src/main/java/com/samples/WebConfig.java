package com.samples;

import com.berlioz.mysql.BerliozDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    ApplicationContext context;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new com.berlioz.spring.Handler());
    }

    @Bean
    public DataSource dataSource() {
//        throw new SQLException("RUBEN TEST");
        return new LazyConnectionDataSourceProxy(new BerliozDataSource(context));
//        return new BerliozDataSource(context);
    }

}

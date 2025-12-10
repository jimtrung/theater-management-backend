package com.github.jimtrung.theater.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {

    @Bean
    public boolean initializeDataSource(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("[INFO] Preinitialized to database successfully");
        }
        catch (SQLException ignored) {}
        return true;
    }
}

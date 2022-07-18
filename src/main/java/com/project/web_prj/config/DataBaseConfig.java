package com.project.web_prj.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// DB 관련 설정 클래스
@Configuration
@ComponentScan(basePackages = "com.prject.web_prj")
public class DataBaseConfig {

    // DB접속 정보 설정 (DataSource설정)
    @Bean
    public DataSource dataSource() {

        HikariConfig config = new HikariConfig();// 우린 HikariCOnfig씀
        config.setUsername("spring4");
        config.setPassword("1234");
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");

        return new HikariDataSource(config);
    }
}

package com.jindognoob.sermon.config;

import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableTransactionManagement
public class JPAConfig {

    // 하이퍼네이트 부가설정
    Properties additionalProperties() {
        Properties properties = new Properties();
        // 방언
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL8Dialect");
        // properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        properties.setProperty("open-in-view", "false");
        properties.setProperty("hibernate.id.new_generator_mappings", "true");

        // 스키마 자동생성
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.id.new_generator_mappings", "true");

        // 디버그용 설정
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");
        return properties;
    }

    @Bean(name="hikariDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        log.info("히카리 데이터소스 생성");
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


    /**
     * LocalContainerEntityManagerFactoryBean은 jpa를 스프링 컨테이너에서 사용할 수 있도록 스프링 프레임워크가 제공하는 기능
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        log.info("엔티티매니저 팩토리 생성");
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource()); // 데이터소스 설정

        emf.setPackagesToScan(new String[] { "com.jindognoob.sermon.domain" }); //@Entity가 붙은 클래스를 자동검색하기위한 시작점 지정

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter(); // JPA 벤더 어댑터 지정, 여기서는 hibernate를 사용하므로 하이버네이트 입력
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(additionalProperties());
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        log.info("트랜잭션 매니저 생성");
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


}

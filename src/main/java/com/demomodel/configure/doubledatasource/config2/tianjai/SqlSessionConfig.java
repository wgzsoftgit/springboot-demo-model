package com.demomodel.configure.doubledatasource.config2.tianjai;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/**
 * yml中，JNDI的 配置

 * @author wgz
 * @date 创建时间：2020年6月3日 上午9:58:10
 */
//spring:
//db
//datasource:
//jndi-name: 'java:comp/env/jdbc/spring_db'
//
//mybatis config
//mybatis:
//mapper-locations: classpath*:mapper/**/*.xml
//@Configuration
public class SqlSessionConfig {

    private Logger logger = LoggerFactory.getLogger(SqlSessionConfig.class);

    @Value("${spring.datasource.jndi-name}")
    private String dataSourceJndiName;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Bean
    public SqlSessionFactoryBean createSqlSessionFactory() {
        SqlSessionFactoryBean sqlSessionFactoryBean = null;
        try {
            // 加载JNDI配置
            Context context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup(dataSourceJndiName);

            // 实例SessionFactory
            sqlSessionFactoryBean = new SqlSessionFactoryBean();
            // 配置数据源
            sqlSessionFactoryBean.setDataSource(dataSource);

            // 加载MyBatis配置文件
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            // 能加载多个，所以可以配置通配符(如：classpath*:mapper/**/*.xml)
            sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocations));
            // 配置mybatis的config文件(我目前用不上)
            // sqlSessionFactoryBean.setConfigLocation("mybatis-config.xml");
        } catch (Exception e) {
            logger.error("创建SqlSession连接工厂错误：{}", e);
        }
        return sqlSessionFactoryBean;
    }
}
//https://blog.csdn.net/qq_20698983/java/article/details/83056403
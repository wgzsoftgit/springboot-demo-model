package com.demomodel.configure.doubledatasource.config3;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;




/**
 * Mybatis-plus ds2数据源配置
 * 多数据源配置依赖数据源配置
 *  配置 map和xml   即dao层
 * @see  DataSourceConfig
 */
@Configuration
@MapperScan(basePackages ="com.demomodel.configure.doubledatasource.textdoubledatasource.secondary.map", sqlSessionTemplateRef  = "ds2SqlSessionTemplate")
public class MybatisPlusConfig4ds2{

    //ds2数据源
    @Bean("ds2SqlSessionFactory")
    public SqlSessionFactory ds2SqlSessionFactory(@Qualifier("ds2DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:com/demomodel/configure/doubledatasource/textdoubledatasource/secondary/map/*.xml"));
        sqlSessionFactory.setPlugins(new Interceptor[]{
                new PaginationInterceptor(),
//                new PerformanceInterceptor()
//                        .setFormat(true),
        });
//        sqlSessionFactory.setGlobalConfig(new GlobalConfig().setBanner(false));
        return sqlSessionFactory.getObject();
    //	return null;
    }

    @Bean(name = "ds2TransactionManager")
    public DataSourceTransactionManager ds2TransactionManager(@Qualifier("ds2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "ds2SqlSessionTemplate")
    public SqlSessionTemplate ds2SqlSessionTemplate(@Qualifier("ds2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
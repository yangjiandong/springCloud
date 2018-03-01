package org.ssh.boot.conf;

import com.github.pagehelper.PageHelper;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("org.ssh.cloud.auth.**.mapper")
public class MyBatisConfig {
    // TransactionManagementConfigurer
    @Autowired
    @Qualifier("primaryDS")
    private DataSource primaryDS;

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(primaryDS);

        sessionFactory.setConfigLocation(new ClassPathResource("mybatis_config.xml"));
        // 映射的实体包
        sessionFactory.setTypeAliasesPackage("org.ssh.boot.auth.entity");

        // 映射mapper文件
        // PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // Resource[] resource = resolver.getResources("classpath:mappers/app/**/*.xml");
        // sessionFactory.setMapperLocations(resource);

        // 配置插件
        // 分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        Interceptor[] plugins = {pageHelper};
        sessionFactory.setPlugins(plugins);
        return sessionFactory.getObject();
    }

   // @Bean
   // public PlatformTransactionManager annotationDrivenTransactionManager() {
   //     return new DataSourceTransactionManager(primaryDS);
   // }

}

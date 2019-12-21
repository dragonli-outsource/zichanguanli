package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.TransactionManager;

//将web.xml里的contextConfigLocation部分，移至此处
@ImportResource(locations= {
        "classpath:applicationContext.xml",
        "classpath:ds_db.xml",
        "classpath:applicationContext-mybatis.xml",
        "classpath:applicationContext-shiro.xml",
        "classpath:applicationContext-druid.xml",
        "classpath:uflo-console-context.xml",
})
@MapperScan({
        "com.dt.module.base.mapper",
        "com.dt.module.cmdb.mapper",
        "com.dt.module.ct.mapper",
        "com.dt.module.flow.mapper",
}) //扫描的mapper
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages={"com.dev","com.dt"})
public class Application  implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Autowired @Qualifier("transactionManager")
    Object transactionManager2;

    public Application(
            @Autowired @Qualifier("transactionManager")
                    PlatformTransactionManager transactionManager
    ){
        DataSourceTransactionManager manager = null;
        System.out.println(transactionManager instanceof PlatformTransactionManager);
        System.out.println(transactionManager instanceof TransactionManager);
        transactionManager = null;
    }

    //	private NettyService nettyService;
    @Value("${http-port:8080}")
    protected int port;

    WebMvcAutoConfiguration ss;


    /**
     * Create JSON object mapper.
     *
     * @return Shared static object mapper from JsonUtil.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }



    /**
     * ManageRedis
     */
//	public static final String MANAGE_REDIS = "manageRedis";
//	@Bean(MANAGE_REDIS)
//	public RedissonClient createRedisClientNew(@Autowired RedisConfigurationManageC2C rc) {
//		return RedisClientBuilderC2C.buildRedissionClient(rc);
//	}
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // TODO Auto-generated method stub
        if(transactionManager2!=null)
            System.out.println("111");
        factory.setPort(this.port);
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

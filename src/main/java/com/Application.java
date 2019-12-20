package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages={"com"})
public class Application  implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {


    //	private NettyService nettyService;
    @Value("${http-port:8080}")
    protected int port;




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
        factory.setPort(this.port);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

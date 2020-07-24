package com.redisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//注意此处一定要添加，否则启动报错，因为我们没有配制数据源
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class )
public class RedisDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RedisDemoApplication.class, args);
    }

}

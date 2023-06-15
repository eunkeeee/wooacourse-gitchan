package com.example.inflearnstudy.beanLifeCycle;

import com.example.inflearnstudy.DataConnectionClient;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    void lifeCycleTest() {
        final ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        final DataConnectionClient client = ac.getBean(DataConnectionClient.class);
        client.close();
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean(initMethod = "init", destroyMethod = "close")
        public DataConnectionClient dataConnectionClient() {
            final DataConnectionClient dataConnectionClient = new DataConnectionClient();
            dataConnectionClient.setUrl("http://gitchan.dev");
            return dataConnectionClient;
        }
    }
}

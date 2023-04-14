package com.example.springbootdemo.autoconfiguration;

import com.example.springbootdemo.profile.DevProfile;
import com.example.springbootdemo.profile.ProductionProfile;
import com.example.springbootdemo.profile.SystemProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionalOnPropertyIntegrationTest {
    
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();
    
    @Test
    public void profileDevTrue() {
        this.contextRunner.withPropertyValues("netology.profile.dev=true")
                .withUserConfiguration(SimpleProfileConfiguration.class)
                .run(context -> {
                    assertThat(context).hasBean("devProfile");
                    SystemProfile systemProfile = context.getBean(DevProfile.class);
                    assertThat(systemProfile.getProfile()).isEqualTo("Current profile is dev");
                    assertThat(context).doesNotHaveBean("prodProfile");
                    
                });
    }
    
    @Test
    public void profileProductionFalse() {
        this.contextRunner.withPropertyValues("netology.profile.dev=false")
                .withUserConfiguration(SimpleProfileConfiguration.class)
                .run(context -> {
                    assertThat(context).hasBean("prodProfile");
                    SystemProfile systemProfile = context.getBean(ProductionProfile.class);
                    assertThat(systemProfile.getProfile()).isEqualTo("Current profile is production");
                    assertThat(context).doesNotHaveBean("devProfile");
                    
                });
    }
    
    @Configuration
    @TestPropertySource("classpath:ConditionalOnPropertyTest.properties")
    protected static class SimpleProfileConfiguration {
        
        @Bean
        @ConditionalOnProperty(prefix = "netology", name = "profile.dev", havingValue = "true")
        public SystemProfile devProfile() {
            return new DevProfile();
        }
        
        @Bean
        @ConditionalOnProperty(prefix = "netology", name = "profile.dev", havingValue = "false")
        public SystemProfile prodProfile() {
            return new ProductionProfile();
        }
    }
}



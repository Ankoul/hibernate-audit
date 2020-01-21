package com.ctw.audit;

import com.ctw.audit.persistence.CredentialsRep;
import com.ctw.audit.persistence.UserRep;
import com.ctw.audit.service.UserDetailsServiceImpl;
import com.ctw.audit.util.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.Executor;

@EnableAsync
@RestController
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class ApplicationRoot extends SpringBootServletInitializer {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationRoot.class);

    @Value("${api.environment}")
    private String environment;

    @Value("${api.version}")
    private String version;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRoot.class, args);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String test() {
        logger.debug("Appplication started Successful!");
        return "{\"started\":\"Success\",\n \"environment\":\"" + environment + "\",\n \"version\":\"" + version+"\"}";
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApplicationRoot.class);
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(Integer.MAX_VALUE);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("API-Thread-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Autowired
    public UserDetailsService userDetailsService(CredentialsRep credentialsRep, UserRep userRep){
        return new UserDetailsServiceImpl(credentialsRep, userRep);
    }

    @Bean
    @SuppressWarnings("rawtypes")
    AuditorAware auditorProvider() {
        return () -> Optional.of(CurrentUser.getId());
    }
}

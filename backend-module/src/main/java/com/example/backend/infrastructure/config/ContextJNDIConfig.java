package com.example.backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class ContextJNDIConfig {

    private final String ejbProviderUrl;
    private final String ejbUser;
    private final String ejbPassword;

    public ContextJNDIConfig(
            @Value("${ejb.config.url}") String ejbProviderUrl,
            @Value("${ejb.config.user}") String ejbUser,
            @Value("${ejb.config.password}") String ejbPassword
    ) {
        this.ejbProviderUrl = ejbProviderUrl;
        this.ejbUser = ejbUser;
        this.ejbPassword = ejbPassword;
    }

    @Bean
    public Context context() throws NamingException {
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL, ejbProviderUrl);
        jndiProps.put(Context.SECURITY_PRINCIPAL, ejbUser);
        jndiProps.put(Context.SECURITY_CREDENTIALS, ejbPassword);
        return new InitialContext(jndiProps);
    }
}

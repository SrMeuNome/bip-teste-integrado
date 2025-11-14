package com.example.backend.infrastructure.config;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.BeneficioEjbServiceRemote;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class EjbClientConfig {
    @Bean
    public BeneficioEjbServiceRemote beneficioEjbService() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        props.put(Context.SECURITY_PRINCIPAL, "bip");
        props.put(Context.SECURITY_CREDENTIALS, "bip");

        Context context = new InitialContext(props);
        return (BeneficioEjbServiceRemote) context.lookup(
                "ejb-module-0.0.1-SNAPSHOT/BeneficioEjbService!com.example.ejb.BeneficioEjbServiceRemote"
        );
    }
}
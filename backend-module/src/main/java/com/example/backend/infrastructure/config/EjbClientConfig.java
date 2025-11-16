package com.example.backend.infrastructure.config;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.BeneficioEjbServiceRemote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class EjbClientConfig {
    private final String ebjModuleName;

    public EjbClientConfig(@Value("${ejb.module.name}") String ebjModuleName) {
        this.ebjModuleName = ebjModuleName;
    }

    @Bean
    public BeneficioEjbServiceRemote beneficioEjbService(Context context) throws NamingException {
        return (BeneficioEjbServiceRemote) context.lookup(getFullName(BeneficioEjbService.class, BeneficioEjbServiceRemote.class));
    }

    private String getFullName(Class classType, Class remoteInterfaceType) {
        String moduleName = ebjModuleName + "/";
        String beanName = classType.getSimpleName();
        String viewClassName = remoteInterfaceType.getName();
        return "ejb:/" + moduleName + beanName + "!" + viewClassName;
    }
}
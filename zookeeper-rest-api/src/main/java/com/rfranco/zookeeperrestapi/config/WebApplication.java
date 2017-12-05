package com.rfranco.zookeeperrestapi.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-21T09:36:39.046+01:00")

public class WebApplication extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SwaggerUiConfiguration.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { MvcConfigurationSupport.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}

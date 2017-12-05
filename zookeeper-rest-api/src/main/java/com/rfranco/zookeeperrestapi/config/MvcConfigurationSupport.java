package com.rfranco.zookeeperrestapi.config;

import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-21T09:36:39.046+01:00")

public class MvcConfigurationSupport extends WebMvcConfigurationSupport {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}

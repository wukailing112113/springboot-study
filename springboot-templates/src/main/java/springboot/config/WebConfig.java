///**
// * Copyright (c) 2016-2019 人人开源 All rights reserved.
// *
// * https://www.renren.io
// *
// * 版权所有，侵权必究！
// */
//
//package springboot.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * WebMvc配置
// * 静态资源访问配置:http://localhost:5000/templates/index.html
// *
// * @author Mark sunlightcs@gmail.com
// */
//@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/statics/**")
//                .addResourceLocations("classpath:/statics/");
//        registry.addResourceHandler("/templates/**")
//                .addResourceLocations("classpath:/templates/");
//    }
//
//}
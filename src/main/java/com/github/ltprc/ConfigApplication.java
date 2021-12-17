package com.github.ltprc;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.github.ltprc.listener.MyHttpSessionListener;
import com.github.ltprc.servlet.MyServlet;

@SpringBootApplication
@ServletComponentScan
public class ConfigApplication {

    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(SpringBootProjectApplication.class);
//        app.setBannerMode(Banner.Mode.OFF);
//        app.run(args);
      
        SpringApplication.run(ConfigApplication.class, args);
    }
    //或者启动如下方式，servlet上不需要添加注解
//    @Bean
//    public ServletRegistrationBean<MyServlet> getServletRegistrationBean(){
//        ServletRegistrationBean<MyServlet> bean = new ServletRegistrationBean<>(new MyServlet());
//        ArrayList<String> url = new ArrayList<>();
//        url.add("/srv");
//        bean.setUrlMappings(url);
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
//    @Bean
//    public ServletListenerRegistrationBean listenerRegist() {
//        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
//        srb.setListener(new MyHttpSessionListener());
//        System.out.println("listener");
//        return srb;
//    }
}

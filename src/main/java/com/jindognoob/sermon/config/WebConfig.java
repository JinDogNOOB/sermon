package com.jindognoob.sermon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/static/", "classpath:/public/",
            "classpath:/", "classpath:/resources/", "classpath:/META-INF/resources/",
            "classpath:/META-INF/resources/webjars/" };

 /*    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000").allowedOriginPatterns("**")
                .allowedHeaders("*").allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(),
                        HttpMethod.OPTIONS.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())
                .maxAge(1800);
    }
 */
    /**
     * 정적자원 추가
     */
 /*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
    */
} 

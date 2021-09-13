/**
 * 
 */
package net.ecology.config;

import javax.servlet.http.HttpSessionListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.ecology.listener.CustomHttpSessionListener;

/**
 * @author ducbq
 *
 */
@Configuration
public class ApplicationSessionConfiguration
{
    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListener()
    {
        return new ServletListenerRegistrationBean<HttpSessionListener>(new CustomHttpSessionListener());
    }
} 
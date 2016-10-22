package me.enmanuel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: enreyes
 * Date: 3/10/2016
 * Time: 9:57 AM
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

}

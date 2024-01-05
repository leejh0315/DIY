package project.DIY.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	
/*
    @Value("${resource.handler}")
    private String resourceHandler;

    @Value("${resource.location}")
    private String resourceLocation;
*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**").allowedOrigins("*");
	}

	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/img/**",
	                                 "/css/**",
	                                 "/temp/**",
	                                 "/mypage/**",
	                                 "/**",
	                                "/js/**,")
                               // + resourceHandler)
	             .addResourceLocations("classpath:/static/img/",
	                                   "classpath:/static/css/",
	                                   "file:/C:/image/temp/",
	                                   "file:/C:/image/profile/",
	                                   "classpath:/static/temp/",
	                                   "classpath:/static/js/");
	                                  // + resourceLocation)
	             
	    

	}

}

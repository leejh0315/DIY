//package project.DIY.config;
//
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import jakarta.servlet.Filter;
//import project.DIY.filter.LoginFilter;
//
//@Configuration
//public class FilterConfig {
//	
//	@Bean
//	public FilterRegistrationBean<Filter> loginFilter() {
//
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
//		filterRegistrationBean.setFilter(new LoginFilter());
//		filterRegistrationBean.setOrder(1);
//		filterRegistrationBean.addUrlPatterns("/*");
//
//		return filterRegistrationBean;
//	}
//}
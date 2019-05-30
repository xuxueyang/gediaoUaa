package uaa.config.myselfconfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uaa.config.myselfconfig.CorsAllowedFilter;

import javax.servlet.Filter;

@Configuration
public class StarsFilterConfiguration {

    @Configuration
    public static class CorsAllowedFilterConfiguration {
        @Bean
        public FilterRegistrationBean corsAllowedFilterRegistration() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            final Filter securityFilter = corsAllowedFilter();
            registration.setFilter(securityFilter);
            registration.addUrlPatterns("/*");
            registration.setName("corsAllowedFilter");
            registration.setOrder(-102);
            return registration;
        }

        @Bean()
        public Filter corsAllowedFilter() {
            return new CorsAllowedFilter();
        }
    }

}

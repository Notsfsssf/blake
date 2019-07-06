package me.perol.blake.config;


import me.perol.blake.filter.JWTAuthenticationFilter;
import me.perol.blake.filter.JWTLoginFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter filter = new JWTLoginFilter(authenticationManager());
        filter.setFilterProcessesUrl("/login");
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager());
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST,"/posts").authenticated()
                .antMatchers(HttpMethod.GET,"/users").authenticated()
                .antMatchers(HttpMethod.PATCH,"/posts/*").authenticated()
                .antMatchers(HttpMethod.DELETE,"/posts/*").authenticated()
                .and()
                .addFilter(filter)
                .addFilter(jwtAuthenticationFilter);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

}

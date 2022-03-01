package com.springsecurity.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index")
                .permitAll()
                .and()
                .formLogin()
                .and().csrf().disable(); // postmanden gönderilen post isteklerine forbidden almamak için default açık olan bu ayarı disable etmek gerekiyor

        http.authorizeRequests()
                .antMatchers("/api/persons/add")
                .hasRole("add_role")
                .antMatchers("/api/persons/update")
                .hasRole("update_role")
                .antMatchers("/api/persons")
                .hasRole("admin");
    }

    /**
     * kendi userlarımı kullanabilmek için PasswordEncoder kullanmamız gerekli
     *
     * why @Bean https://stackoverflow.com/a/64577059/1604503
     * @return
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        // user1 api/persons adresine erişebilir çünkü admin role'ü var
        UserDetails user1 = User.builder()
                .username("user1")
                .password(getEncoder().encode("pass1"))
                .roles("add_role", "admin")
                .build();

        // user2 api/persons adresine erişemez çünkü admin role'ü yok
        UserDetails user2 = User.builder()
                .username("user2")
                .password(getEncoder().encode("pass2"))
                .roles("update_role")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}

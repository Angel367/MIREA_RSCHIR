package com.example.egor.Configurations;

/*
Задание
	Студенту предлагается дополнить задание шестой практики по Java. Нужно создать приложение на микросервисной архитектуре, где один сервис — это приложение шестой практики, а второй сервис — это сервис авторизации.
	Сервис авторизации должен быть написан с помощью Spring Security и содержать JWT token. В качестве СУБД во втором сервисе должен быть использован Redis. Должны быть созданы три роли: USER, SELLER, ADMINISTRATOR.
	USER не имеет доступ к какому-либо методу, который связан с редактированием или просмотром информации по другому пользователю, не имеет возможности добавлять или удалять товары.
	SELLER имеет те же ограничения, что и USER, за исключением, что продавец может добавлять и удалять свои товары.
	ADMINISTRATOR не имеет каких либо ограничений.
	В сервис с приложением маркетплейса добавить бизнес логику, позволяющую пользователь с ролью USER получить роль SELLER.
	Практика должна запускаться с помощью docker-compose. Каждый микросервис должен запускаться в отдельном потоке.

*/

import com.example.egor.Services.UserService;
import com.example.egor.Services.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userServiceImpl;

    public SecurityConfiguration(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure security settings, such as authentication requirements and authorization rules
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/users/register/**", "/api/auth/**", "/orders/**", "/dishes/**").permitAll()
                .antMatchers("/asdasdas/").authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
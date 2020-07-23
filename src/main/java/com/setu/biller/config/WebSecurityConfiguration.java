package com.setu.biller.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.setu.biller.dto.ErrorResponse;
import com.setu.biller.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Krishna Verma
 * @date 23/07/2020
 */
@EnableWebSecurity
@Configuration
@Order(1)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        APIKeyFilter filter = new APIKeyFilter(Constants.API_KEY_HEADER);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (!Constants.AUTH_TOKEN.equals(principal)) {
                throw new BadCredentialsException(Constants.AUTH_ERROR);
            }
            authentication.setAuthenticated(true);
            return authentication;
        });
        http.antMatcher("/api/**").csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(filter).authorizeRequests().anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setErrorCode(Constants.AUTH_ERROR);
                    errorResponse.setStatus(Constants.ERROR);
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                });
    }
}

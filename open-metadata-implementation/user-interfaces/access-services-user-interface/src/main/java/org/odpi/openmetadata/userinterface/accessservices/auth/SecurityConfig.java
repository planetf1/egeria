/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@EnableWebSecurity
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    @Value("${ldap.user.search.base}")
    private String userSearchBase;

    @Value("${ldap.user.search.filter}")
    private String userSearchFilter;

    @Value("${ldap.group.search.base}")
    private String groupSearchBase;

    @Value("${ldap.group.search.filter}")
    private String groupSearchFilter;

    @Value("${ldap.url}")
    private String url;

    @Value("${authentication.source}")
    private String authenticationSource;


    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .anonymous().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/service-worker.js").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/node_modules/**").permitAll()
                .antMatchers("/src/**").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/locales/**").permitAll()
                .antMatchers("/properties/**").permitAll()
                .antMatchers("/open-metadata/ui-admin-services/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthFilter(authService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter("/auth/login", authenticationManager(), authService),
                        UsernamePasswordAuthenticationFilter.class)
                ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public InetOrgPersonContextMapper userContextMapper() {
        return new InetOrgPersonContextMapper();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        if (authenticationSource.equals("ldap"))
            auth
                    .ldapAuthentication()
                    .userSearchBase(userSearchBase)
                    .userSearchFilter(userSearchFilter)
                    .groupSearchBase(groupSearchBase)
                    .groupSearchFilter(groupSearchFilter)
                    .rolePrefix("")
                    .userDetailsContextMapper(userContextMapper())
                    .contextSource()
                    .url(url);
        else auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

}

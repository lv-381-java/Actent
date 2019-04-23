package com.softserve.actent.security.config;

import com.softserve.actent.security.JwtAuthenticationEntryPoint;
import com.softserve.actent.security.JwtAuthenticationFilter;
import com.softserve.actent.security.oauth2.CustomOAuth2UserService;
import com.softserve.actent.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.softserve.actent.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.softserve.actent.security.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/api/v1/auth/*",
                        "/login**",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                )
                .permitAll()
                .antMatchers(
                        "/v2/api-docs",
                        "/configuration/**",
                        "/swagger*/**",
                        "/webjars/**"
                )
                .permitAll()
                .antMatchers("/confirm",
                        "/ws/**",
                        "/chat/message/**",
                        "/topic/**")
                .permitAll()
                .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler);
//                .failureHandler(oAuth2AuthenticationFailureHandler);

        security.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

//    @Bean
//    public PrincipalExtractor principalExtractor(UserRepository userRepository) {
//        return map -> {
//            String id = ((String) map.get("sub"));
//            String email = (String) map.get("email");
//            if(!userRepository.existsByEmailAndProviderIdNull(email)) {
//                return userRepository.findByProviderId(id).orElseGet(() -> {
//                    User newUser = new User();
//                    newUser.setProviderId(id);
//                    newUser.setFirstName((String) map.get("given_name"));
//                    newUser.setLogin((String) map.get("given_name"));
//                    newUser.setLastName((String) map.get("family_name"));
//                    newUser.setEmail(email);
//                    newUser.setStatus(Status.ACTIVE);
//                    Role userRole = Role.ROLE_USER;
//                    newUser.setRoleset(Collections.singleton(userRole));
//
//                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                    String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
//                    System.out.println("token "+token);
//                    return userRepository.save(newUser);
//                });
//            }else throw new ValidationException(ExceptionMessages.USER_BY_THIS_EMAIL_IS_EXIST, ExceptionCode.DUPLICATE_VALUE);
//        };
//    }
}

package de.zalando.zally.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import org.zalando.stups.oauth2.spring.security.expression.ExtendedOAuth2WebSecurityExpressionHandler;
import org.zalando.stups.oauth2.spring.server.TokenInfoResourceServerTokenServices;

@Configuration
@EnableResourceServer
@Profile(value = "production")
@Import(SecurityProblemSupport.class)
class OAuthConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${spring.oauth2.resource.tokenInfoUri}")
    private String tokenInfoUri;

    @Autowired
    private SecurityProblemSupport problemSupport;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(new ExtendedOAuth2WebSecurityExpressionHandler());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .requestMatchers().antMatchers("/**")
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/health").permitAll()
            .antMatchers("/metrics",
                "/api-violations",
                "/supported-rules",
                "/review-statistics").access("#oauth2.hasScope('uid')")
            .antMatchers("**").denyAll();

        http
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport);
    }

    @Bean
    public ResourceServerTokenServices customResourceTokenServices() {
        return new TokenInfoResourceServerTokenServices(tokenInfoUri);
    }
}

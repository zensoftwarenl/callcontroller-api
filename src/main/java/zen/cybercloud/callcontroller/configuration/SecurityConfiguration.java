/*
 * MIT License
 *
 * Copyright (c) 2023. ZEN Software B.V.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package zen.cybercloud.callcontroller.configuration;

import zen.cybercloud.callcontroller.ciscosecurity.HorribleCiscoPhoneSecurityFilter;
import zen.cybercloud.callcontroller.ciscosecurity.HorribleCiscoPhoneSecurityProvider;
import zen.cybercloud.callcontroller.security.JwtAuthenticationFilter;
import zen.cybercloud.callcontroller.security.JwtAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zen.cybercloud.callcontroller.security.SecurityConstants;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
import java.util.UUID;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);

    private String signingSecret;

    public SecurityConfiguration(@Value("${callcontroller.signingSecret}") String signingSecret) {
        this.signingSecret = signingSecret;
    }

    // Section 1: HttpBasic authentication for actuator and internal things
    @Configuration
    @Order(1)
    public class HttpBasicAuthConfig  {

        @Value("${spring.security.user.name}")
        private String adminUsername;

        @Bean
        @Profile("test")
        public UserDetailsService userDetailsService() {

            //    @Bean //used by OpenApiGeneratorTests
            //    public UserDetailsService userDetailsService() {
            //        // @formatter:off
            //        UserDetails userDetails = User.withDefaultPasswordEncoder()
            //                .username("user")
            //                .password("password")
            //                .roles("USER")
            //                .build();
            //        // @formatter:on
            //        return new InMemoryUserDetailsManager(userDetails);
            //    }
            String thePassword = UUID.randomUUID().toString();
            LOG.info(String.format("Creating userDetailsService for actuator and openapi spec: username: %s, password: %s", adminUsername, thePassword));
            return new UserDetailsService() {
                private final PasswordEncoder BCryptPasswordEncoder = new BCryptPasswordEncoder();
                @Override
                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                    // For demonstration purposes, let's create a static user
                    if (adminUsername.equals(username)) {
                        return User.withUsername(adminUsername)
                                .password(BCryptPasswordEncoder.encode(thePassword)) // Encrypted password
                                .roles("ADMIN")
                                .build();
                    } else {
                        throw new UsernameNotFoundException("User not found");
                    }
                }
            };
        }

        @Bean("httpBasicFilterChain")
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            //Todo: where is the generated password? from spring boot?
            http.antMatcher("/actuator/**")
                    .antMatcher("/swagger-ui/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic(withDefaults());
            return http.build();
        }
    }

    // Section 2: Custom authentication for Cisco Phones
    @Configuration
    @Order(2)
    public class CustomAuthConfig  {

        private final HorribleCiscoPhoneSecurityProvider provider;
        private final List<HttpMessageConverter<?>> messageConverters;

        public CustomAuthConfig(HorribleCiscoPhoneSecurityProvider provider, List<HttpMessageConverter<?>> messageConverters) {
            this.provider = provider;
            this.messageConverters = messageConverters;
        }

        @Bean("ciscoFilterChain")
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            HorribleCiscoPhoneSecurityFilter filter = new HorribleCiscoPhoneSecurityFilter(provider,messageConverters);
            http.antMatcher("/cisco/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
    }

    // Section 3: JWT authentication for all services
    @Configuration
    @Order(3)
    public class JwtAuthConfig {
        AuthenticationManager authenticationManager;

        @Bean
        public DaoAuthenticationProvider authenticationProvider(@Qualifier("firestoreUserDetailsService")
                                                                        UserDetailsService userDetailsService) {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService);
            // Additional configuration for the provider if needed
            // ...
            return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            authenticationManager = authenticationConfiguration.getAuthenticationManager();
            return authenticationManager;
        }

        @Bean("jwtFilterChain")
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.cors().disable();
            http.authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
            .and()
                .authorizeRequests()
                .antMatchers("/storage/**", "/callerid/**", "/recordings/**")
                .authenticated()
            .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager,signingSecret))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, signingSecret))
                 //this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            return http.build();
        }
    }
}


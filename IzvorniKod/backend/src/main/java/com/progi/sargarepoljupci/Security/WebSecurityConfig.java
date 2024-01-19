package com.progi.sargarepoljupci.Security;

import com.progi.sargarepoljupci.Security.JWT2.JWTAuthenticationFilter;
import com.progi.sargarepoljupci.Services.KorisnikDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JWTAuthenticationFilter authenticationFilter;

    private final KorisnikDetailsService userDetailsService;
    @Autowired
    public WebSecurityConfig(JWTAuthenticationFilter authenticationFilter, KorisnikDetailsService userDetailsService) {
        this.authenticationFilter = authenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Create and return a BCryptPasswordEncoder bean
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    private static final String[] ADMIN_SECURED_URLs = {"/api/admin/**"};

    private static final String[] UN_SECURED_URLs = {
            "/api/login/**",
            "/api/registration/**",
            "/api/authenticate/**",
            "/parkingLots",
            "/bicycle-spots/for-parking/{parkingId}",
            "/parking-spots/for-parking/{parkingId}",
            "/all-bicycle-spots",
            "/all-parking-spots",
            "/unoccupied",
            "/occupied",
            "/reservableParkingSpots",
            "/accessibleParkingSpots",
            "/{parkingSpotId}/availability",
            "/unmarkedParkingSpots",
            "/unmarkedBicycleSpots",
            "/mark",

            "/availability"

    };

    private static final String[] CLIENT_SECURED_URLs = {
            "/api/client/**",
            "/api/reservation/**"
    };
    private static final String[] MANAGER_SECURED_URLs = {
            "/api/voditelj/**"
    };



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(UN_SECURED_URLs).permitAll()
                .requestMatchers(ADMIN_SECURED_URLs).hasAuthority("ADMIN")
                .requestMatchers(CLIENT_SECURED_URLs).hasAuthority("KLIJENT")
                .requestMatchers(MANAGER_SECURED_URLs).hasAuthority("VODITELJ")
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

package com.example.techconnect.config;

import com.example.techconnect.services.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;


// informs spring that this class is to configure the spring application
@Configuration
// will allow us to edit the MVC security for our application
@EnableWebSecurity
public class SecurityConfiguration {
    // Dependency that we inject, so that we can retrieve the details about the user who is trying to log in
    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    // the @Bean annotation means the class itself is being managed by spring

    // Is a class that is managed by spring, specifically to hash and unhash our user passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // This class is also managed by spring, Is used to manage the users Authentication status.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    // This class is also managed by spring, Will provide filters for our spring security for different URL mappings
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        // Cole, Johnny has recommended to only use one .requestMatchers for .authenticated().Also, I have added the following paths
                        // event/edit/{id}
                        //"/event/edit"

                // Reviews also may need to fall under the permit all method sin ce attends don't have a login


                       // "/event-reviews/{id}"

                        // "/event/{eventId}/delete"
                      //  "/event/{eventId}/reviews"




                        /* Pages that require authentication
                         * only authenticated users can create and edit events */
                        .requestMatchers("/event/create",
                                "/event/{id}/edit",
                                "/discussions/{id}/delete",
                                "/discussion/{id}/comment",
                                "/discussion/create",
                                "/comments/{id}/delete",
                                "/discussion/{id}/edit",
                                "/{id}/editProfile",
                                "/profile",
                                "/editProfile",
                                "/comments/create",
                                "/event/{eventId}/reviews/create",
                                "/event/{eventId}/delete",
                                "/event/{eventId}/reviews/{reviewId}/delete",
                                "/event/{eventId}/reviews/{reviewId}/edit",
                                "/attendee/{eventId}/register",
                                "/attendee/{eventId}/unregister","/event/{eventId}/reviews").authenticated()
                        /* Pages that do not require authentication
                         * anyone can visit the home page, register, login, and view ads */
                // /event/reviews/create is a public page and can be visited by anyone
                        .requestMatchers("/",
                                "/events",
                                "/events/*",
                                "/events/userpro",
                                "/events/allevent",
                                "/SignUpPage",
                                "/LoginPage",
                                "/discussions",
                                "/events.json",
                                "/events/ajax",
                                "/deleteProfile",
                                "/register",
                                "/logout",
                                "/login",
                                "/events/eventsSearchKeyword",
                                "/events/searchEvents",
                                "/events/closestEvents",
                                "/events.json",
                                "/events/profEvents",
                                "/events/ajax",
                                "/events/search",
                                "/events/userEvents",
                                "/AboutUs",
                                "/error").permitAll()
                        // allow loading of static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/keys.js").permitAll()
                )
                /* Login configuration */
                .formLogin((login) -> login.loginPage("/LoginPage").defaultSuccessUrl("/profile"))
                /* Logout configuration */
                .logout((logout) -> logout.logoutSuccessUrl("/LoginPage?logout"))
                .httpBasic(withDefaults());
        return http.build();
    }
}

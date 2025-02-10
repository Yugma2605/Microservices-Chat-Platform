package com.example.api_gateway.config;

import com.example.api_gateway.service.JWTService;
import com.example.api_gateway.service.MyUserDetailsService;
//import com.example.api_gateway.service.MyUserDetailsSevice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import reactor.core.publisher.Mono;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    // Here we are adding this abstract class of OncePerRequestFilter
    // Why the name is OncePerRequestFilter as we are only checking this filter once during a request. Good inbuilt filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  The below mentioned will be the type of header that we will receive from client.
        //  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraWxsIiwiaWF0IjoxNzIzMTgzNzExLCJleHAiOjE3MjMxODM4MTl9.5nf7dRzKRiuGurN2B9dHh_M5xiu73ZzWPr6rbhOTTHs

        // Skip JWT validation for login and register endpoints
        String path = request.getRequestURI();
        System.out.println("Path : "+path);
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
            System.out.println("Skipping JWT filter for path: {}"+ path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        System.out.println("API Gateway JwtFilter invoked; authHeader: " + authHeader);
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
            System.out.println("API Gateway JwtFilter extracted username " + username);
        }
        else {
            System.out.println("JWTFilter : No Valid Authorization Header found");
        }
        // Username should not be null and it is not already authenticated then only enter the loop.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
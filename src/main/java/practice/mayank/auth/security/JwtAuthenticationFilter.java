package practice.mayank.auth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.mayank.auth.service.CustomUserDetailService;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;

    // Request with Credentials -> Allow -> Verify -> Set SecurityContextHolder
    // Request without Credentials -> Deny
    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull  HttpServletResponse response,
           @NonNull  FilterChain filterChain) throws ServletException, IOException {



        // Authorization : Bearer
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

            
        String token = authorization.substring(7);
            
        if(!jwtService.tokenValidation(token)){
            filterChain.doFilter(request,response);
            return;
        }

        //Extracting Claims
        Claims claim = jwtService.extractAllClaims(token);
        String email = claim.getSubject();

        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request,response);

    }

}


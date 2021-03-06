package com.thebund1st.daming.sdk.security;

import com.thebund1st.daming.sdk.jwt.SmsVerificationClaims;
import com.thebund1st.daming.sdk.jwt.SmsVerificationJwtVerifier;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class SmsVerificationJwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private SmsVerificationJwtVerifier smsVerificationJwtVerifier;

    public SmsVerificationJwtAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
        super.setAuthenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
            // does nothing, just let the request pass
        });
        super.setAuthenticationFailureHandler(new SmsVerificationJwtAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        final String jwt = request.getHeader("X-SMS-VERIFICATION-JWT");
        SmsVerificationClaims claims = smsVerificationJwtVerifier.verify(jwt);
        return new SmsVerificationAuthentication(claims, Collections.emptyList());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}

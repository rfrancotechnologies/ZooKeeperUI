package com.rfranco.zookeeperrestapi.services;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CookieAuthenticationService {
    @Value(value="${jwt.secret}")
    private String secret;
    @Value(value="${jwt.timeout_seconds}")
    private int jwtTimeout;

    @Autowired
    private DateTimeProvider dateTimeProvider;

    static final String AUTHENTICATION_COOKIE = "ACCESS_TOKEN";

    private Rfc6265CookieProcessor rfc6265CookieProcessor = new Rfc6265CookieProcessor();

    public String createAuthenticationCookie(UserDetails userDetails) {
        String JWT = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(dateTimeProvider.currentTimeMillis() + jwtTimeout * 1000))
                .claim("permissions", userDetails.getAuthorities()
                        .stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        Cookie cookie = new Cookie(AUTHENTICATION_COOKIE, JWT);
        cookie.setMaxAge(jwtTimeout);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return rfc6265CookieProcessor.generateHeader(cookie) + "; SameSite=strict";
    }

    public String getAuthenticationRemovalCookie() {
        Cookie cookie = new Cookie(AUTHENTICATION_COOKIE, "DELETED");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return rfc6265CookieProcessor.generateHeader(cookie) + "; SameSite=strict";
    }

    public Authentication getAuthentication(HttpServletRequest request) {

        Optional<Cookie> token = Optional.empty();
        if (request.getCookies() != null) {
          token = Arrays.stream(request.getCookies())
                  .filter(cookie -> cookie.getName().equals(AUTHENTICATION_COOKIE)).findFirst();
        }

        if (token.isPresent()) {
            try{
                // parse the token.
                Claims tokenClaims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token.get().getValue()).getBody();
                return getAuthenticationInfoFromJwtClaims(tokenClaims);
            } catch(ExpiredJwtException ex) {
                return null;
            } catch(SignatureException ex) {
                return null;
            }
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthenticationInfoFromJwtClaims(Claims tokenClaims) {
        Collection<String> permissions = tokenClaims.get("permissions", Collection.class);

        if (tokenClaims.getSubject() == null)
            return null;

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(tokenClaims.getSubject(),
                null, permissions.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
        authentication.setDetails(tokenClaims.getExpiration());

        return authentication;
    }
}

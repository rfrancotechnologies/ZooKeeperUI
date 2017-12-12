package com.rfranco.zookeeperrestapi.services;

import com.rfranco.zookeeperrestapi.config.users.ConfiguredUserDetails;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by ruben.martinez on 12/12/2017.
 */
public class CookieAuthenticationServiceTest {
    @Mock
    private DateTimeProvider dateTimeProvider;

    @InjectMocks
    private CookieAuthenticationService cookieAuthenticationService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAuthenticationCookie() throws Exception {
        ReflectionTestUtils.setField(cookieAuthenticationService, "secret", "TestJWTSecret");
        ReflectionTestUtils.setField(cookieAuthenticationService, "jwtTimeout", 1800);
        doReturn(1508424514000L).when(dateTimeProvider).currentTimeMillis();

        ConfiguredUserDetails userDetails = new ConfiguredUserDetails();
        userDetails.setName("TestUser");
        userDetails.setPassword("TestUserPassword");
        HttpCookie obtainedAuthCookie = HttpCookie.parse(cookieAuthenticationService.createAuthenticationCookie(userDetails)).get(0);
        Assert.assertEquals(obtainedAuthCookie.getValue(), "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0VXNlciIsImV4cCI6MTUwODQyNjMxNCwicGVybWlzc2lvbnMiOlsiWk9PS0VFUEVSX1VTRVIiXX0.X2UtlZetHWWGLfVnI0A_eZRb_vhNSSP9t6wDCBlKjwVLYcE7VF_r2lWUNZhecsxLcJzqqgkb41d5PMAsEBcubw");
        Assert.assertEquals(obtainedAuthCookie.getMaxAge(), 1800L);
        Assert.assertEquals(obtainedAuthCookie.getPath(), "/");
        Assert.assertEquals(obtainedAuthCookie.isHttpOnly(), true);
    }

    @Test
    public void getAuthenticationRemovalCookie() throws Exception {
        HttpCookie obtainedAuthCookie = HttpCookie.parse(cookieAuthenticationService.getAuthenticationRemovalCookie()).get(0);
        Assert.assertEquals(obtainedAuthCookie.getValue(), "DELETED");
        Assert.assertEquals(obtainedAuthCookie.getMaxAge(), 0L);
        Assert.assertEquals(obtainedAuthCookie.getPath(), "/");
        Assert.assertEquals(obtainedAuthCookie.isHttpOnly(), true);
    }

    @Test
    public void getAuthentication() throws Exception {
        ReflectionTestUtils.setField(cookieAuthenticationService, "secret", "TestJWTSecret");
        ReflectionTestUtils.setField(cookieAuthenticationService, "jwtTimeout", 1800);
        doReturn(System.currentTimeMillis()).when(dateTimeProvider).currentTimeMillis();

        UserDetails userDetails = mock(UserDetails.class);
        doReturn("TestUser").when(userDetails).getUsername();
        doReturn("TestUserPassword").when(userDetails).getPassword();
        doReturn(Arrays.asList(new SimpleGrantedAuthority("ZOOKEEPER_USER"))).when(userDetails).getAuthorities();

        Cookie testAuthCookie = new Cookie("ACCESS_TOKEN",
                HttpCookie.parse(cookieAuthenticationService.createAuthenticationCookie(userDetails)).get(0).getValue());

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        doReturn(new Cookie[] {testAuthCookie}).when(testRequest).getCookies();

        Authentication obtainedAuthentication = cookieAuthenticationService.getAuthentication(testRequest);
        Assert.assertEquals("TestUser", obtainedAuthentication.getName());
        Assert.assertThat(new ArrayList<GrantedAuthority>(obtainedAuthentication.getAuthorities()),
                hasItem(hasProperty("authority", equalTo("ZOOKEEPER_USER"))));
    }
}
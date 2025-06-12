package com.perepalacin.auth_service.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookiesUtil {

    public static void addHttpOnlyCookie(HttpServletResponse response, String name, String value, Integer maxAgeSeconds, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

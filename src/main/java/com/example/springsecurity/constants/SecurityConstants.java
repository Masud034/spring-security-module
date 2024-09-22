package com.example.springsecurity.constants;


public class SecurityConstants {
    public static final long EXPIRATION_TIME = 86400;
    public static final String TOKEN_PREFIX = "BEARER ";
    public static final String HEADER_STRING = "Authorization";
    public static final long REFRESH_EXPIRATION_TIME = 30*24*60*60*1000;
}

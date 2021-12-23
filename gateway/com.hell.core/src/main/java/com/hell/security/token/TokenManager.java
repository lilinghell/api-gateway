package com.hell.security.token;


import javax.servlet.http.HttpServletRequest;

public interface TokenManager  {
    Token createToken(HttpServletRequest request);

    int verifyToken(HttpServletRequest request);
}

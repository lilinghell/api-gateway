package com.hell.security.token;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ResubmitTokenManager implements TokenManager {
    protected Log log = LogFactory.getLog(this.getClass());
    @Value("${app.token.resubmit.maxEntryNumber:2}")
    private int maxEntryNumber;
    @Value("${app.token.resubmit.delayTime:600}")
    private int delayTime;
    @Value("${app.token.resubmit.tokenListName:_tokenList}")
    private String tokenListName;
    @Value("${app.token.resubmit.tokenName:_tokenName}")
    private String tokenName;
    @Value("${app.token.resubmit.tokenLength:8}")
    private int tokenLength;
    @Value("${app.token.resubmit.ignoreCase:false}")
    private boolean ignoreCase;
    @Value("${app.token.resubmit.chars:\"0123456789ABCDEFGHIJKLMNOPQRSTUVWXZYabcdefghijklmnopqrstuvwxzy\"}")
    private String chars;

    @Override
    public Token createToken(HttpServletRequest request) {
        TokenList tokenList = (TokenList) request.getSession().getAttribute(this.tokenListName);
        if (tokenList == null) {
            tokenList = new TokenList(this.maxEntryNumber, this.delayTime, this.tokenLength, this.chars.toCharArray());
        }
        String tokenId = tokenList.getNextTokenId();
        if (this.ignoreCase) {
            tokenId = tokenId.replaceAll("0", "1");
            tokenId = tokenId.replaceAll("o", "p");
            tokenId = tokenId.replaceAll("1", "2");
            tokenId = tokenId.replaceAll("l", "m");
        }

        Token token = new TokenImpl(tokenId, System.currentTimeMillis());
        tokenList.add(token);
        request.getSession().setAttribute(this.tokenListName, tokenList);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Create Token" + this.tokenListName + "," + this.tokenName + " " + token);
        }
        return token;
    }

    @Override
    public int verifyToken(HttpServletRequest request) {
        TokenList tokenList = (TokenList) request.getSession().getAttribute(this.tokenListName);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Verify Token " + this.tokenListName + "," + this.tokenName + " " + tokenList);
        }

        if (tokenList == null) {
            return 0;
        } else {

            String tokenId = request.getParameter(this.tokenName);;
            if (this.log.isDebugEnabled()) {
                this.log.debug("Get Token " + this.tokenListName + "," + this.tokenName + " " + tokenId);
            }

            if (tokenId == null) {
                return -2;
            } else {
                if (this.ignoreCase) {
                    tokenId = tokenId.toLowerCase();
                }

                try {
                    try {
                        Token token = tokenList.get(tokenId, this.ignoreCase);
                        if (token == null) {
                            return -3;
                        } else {
                            return 1;
                        }
                    } catch (Exception var16) {
                        return -1;
                    }
                } finally {
                    request.getSession().setAttribute(this.tokenListName, tokenList);
                }
            }
        }
    }

    public void setMaxEntryNumber(int maxEntryNumber) {
        this.maxEntryNumber = maxEntryNumber;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setTokenListName(String tokenListName) {
        this.tokenListName = tokenListName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setTokenLength(int tokenLength) {
        this.tokenLength = tokenLength;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }
}

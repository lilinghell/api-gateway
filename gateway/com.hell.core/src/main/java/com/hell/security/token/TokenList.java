package com.hell.security.token;


import com.hell.core.exception.ValidationException;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

@SuppressWarnings("unchecked")
public class TokenList implements Serializable {
    private char[] chars;
    private int delayTime;
    private int maxEntryNumber;
    private int tokenLength;
    private LinkedList tokenList;
    private static Random _randomNumberGenerator = new SecureRandom();

    public TokenList(int maxEntryNumber, int delayTime, int tokenLength) {
        this.chars = new char[]{'3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y'};
        this.tokenList = new LinkedList();
        this.maxEntryNumber = maxEntryNumber;
        this.delayTime = delayTime;
        this.tokenLength = tokenLength;
    }

    public TokenList(int maxEntryNumber, int delayTime, int tokenLength, char[] chars) {
        this(maxEntryNumber, delayTime, tokenLength);
        this.chars = chars;
    }

    public String toString() {
        return this.getClass().getName() + " :" + this.tokenList;
    }

    public String getNextTokenId() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.tokenLength; ++i) {
            int index = _randomNumberGenerator.nextInt(this.chars.length);
            sb.append(this.chars[index]);
        }

        return sb.toString();
    }

    public void add(Token item) {
        synchronized (this.tokenList) {
            this.tokenList.addLast(item);
            if (this.tokenList.size() > this.maxEntryNumber) {
                this.tokenList.removeFirst();
            }
        }
    }

    public Token get(String uniqueId, boolean ignoreCase) throws ValidationException {
        synchronized (this.tokenList) {
            for (int i = 0; i < this.tokenList.size(); ++i) {
                Token item = (Token) this.tokenList.get(i);
                boolean result = ignoreCase ? item.getUniqueId().equalsIgnoreCase(uniqueId) : item.getUniqueId().equals(uniqueId);
                if (result) {
                    long x = System.currentTimeMillis() - item.getAccessDate();
                    if (x > (long) (this.delayTime * 1000)) {
                        this.tokenList.remove(i);
                        throw new ValidationException("token_timeout");
                    }

                    this.tokenList.remove(i);
                    return item;
                }
            }

            return null;
        }
    }
}

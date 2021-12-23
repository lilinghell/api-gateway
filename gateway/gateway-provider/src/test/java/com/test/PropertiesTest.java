package com.test;

import com.hell.ServiceApplication;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServiceApplication.class)
public class PropertiesTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void Encrypt() {
        // 加密
        System.out.println(stringEncryptor.encrypt("ccic1234"));
    }

    @Test
    public void decrypt() {
        // 加密
        System.out.println(stringEncryptor.decrypt("0nYH9D/RaF5fQDBYzzunpvJDYlTI+q9CQrAv6pJH1yx6k0BMQ61RjZS/tsZKmKYM"));
    }
}

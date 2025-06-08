package com.crewManager.pro.config;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
public class keyMakeTest {

    @Test
    public void key만들기(){
            SecureRandom random = new SecureRandom();
            byte[] keyBytes = new byte[32]; // 256 bits
            random.nextBytes(keyBytes);
            String secretKey = Base64.getEncoder().encodeToString(keyBytes);
            System.out.println("Generated Secret Key: " + secretKey);

    }
}

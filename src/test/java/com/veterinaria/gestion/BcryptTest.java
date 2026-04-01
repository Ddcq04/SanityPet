package com.veterinaria.gestion;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    @Test
    public void generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("====== REAL HASH ======");
        System.out.println(encoder.encode("1234"));
        System.out.println("=======================");
    }
}

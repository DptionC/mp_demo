package com.itheima.mp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Autor：林建威
 * @DateTime：2024/5/8 16:28
 **/

@SpringBootTest
class IAddressServiceTest {

    @Autowired
    private IAddressService addressService;

    @Test
    void logindelete() {
        addressService.removeById(59L);
        addressService.getById(59L);
    }
}
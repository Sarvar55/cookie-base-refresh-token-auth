package com.company.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @project: refreshtokenIncookie
 * @author: Sarvar55
 */
@RestController
@RequestMapping("/api/1.0/products")
public class DemoController {

    @GetMapping
    public List<String> getProducts() {
        return List.of("HP", "Casper", "Lenova");
    }
}

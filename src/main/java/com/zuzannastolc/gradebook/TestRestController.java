package com.zuzannastolc.gradebook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
    @GetMapping("/")
    public String helloWorld(){
        return "Hello world!";
    }
}

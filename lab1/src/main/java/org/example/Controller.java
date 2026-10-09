package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@RestController //обработка http
public class Controller {

    @GetMapping(value = "/hello", produces = "text/html;charset=UTF-8")
    public String printHello() {
        return "<h1 style='color: red'>Hello, World!</h1>";
    }

    @GetMapping("/sum")
    public int calculateSum(@RequestParam int a, @RequestParam int b) {
        return a+b;
    }
}

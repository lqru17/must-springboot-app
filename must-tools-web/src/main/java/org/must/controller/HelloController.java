package org.must.controller;

import java.util.List;

import org.m.server.fds.service.FdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@Qualifier("fds") // <-- 告訴Spring注入名稱為"one"的Bean，即DemoServiceOneImpl
    @Autowired
	private FdsService fdsService;
	
    @GetMapping("/hello")
    public String getHello() {
        System.out.println("hello");
        
        List list = fdsService.findAll();
        System.out.println(list);
        return null;
    }

}

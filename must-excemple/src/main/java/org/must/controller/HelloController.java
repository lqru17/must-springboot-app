package org.must.controller;

import org.must.service.FdsService;
import org.must.service.OregService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HelloController {
	
    @Autowired
    @Qualifier("fdsService")
	private FdsService fdsService;
	
    @Autowired
    @Qualifier("oregService")
	private OregService oregService;
    
    @GetMapping("/hello")
    public String getHello() {
        System.out.println("hello");
        return oregService.findAll().toString();
    }

    
}

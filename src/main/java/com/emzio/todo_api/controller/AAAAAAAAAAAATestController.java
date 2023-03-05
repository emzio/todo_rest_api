package com.emzio.todo_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.emzio.todo_api.logic.ProjectService;

@Controller
public class AAAAAAAAAAAATestController {

    private final Logger logger = LoggerFactory.getLogger(AAAAAAAAAAAATestController.class);
    private final ProjectService projectService;

    public AAAAAAAAAAAATestController(ProjectService projectService) {
        this.projectService = projectService;
    }

//    @GetMapping("projecttest")
//    ResponseEntity<String> projectTest(){
//        GroupReadModel group = projectService.createGroup(2, LocalDateTime.now());
//        return ResponseEntity.ok(group.toString());
//    }

    @GetMapping("/basics")
    public ResponseEntity<String> basicsTest(){
        String a = "ABC";
        String b = "ABC";
        String c = new String("ABC");
        Long thirst = Long.valueOf(50);
        Long second = Long.valueOf(50);
        Long third = new Long(50);
        String result = " resul for long " + (thirst==second);
        return ResponseEntity.ok("result a==b " + (a==b)
                + "<br>" + " result a==c " + (a==c)
                + "<br>" + result
                + "<br>" + " result long third " + (thirst==third)
        );
    }
    @GetMapping("/test")
    ResponseEntity<String> test(){
        return new ResponseEntity<>("test", HttpStatus.OK);
    }

    @GetMapping("/headertest")
    ResponseEntity<String> headerTest(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("test1", "another header");
        return new ResponseEntity<>("header test", httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/bodytest")
    ResponseEntity<String[]> bodyTest(){
        String[] body = {"body test", "body2"};
        return ResponseEntity.ok().body(body);
    }
}


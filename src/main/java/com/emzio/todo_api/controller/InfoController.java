package com.emzio.todo_api.controller;

import com.emzio.todo_api.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
public class InfoController {

    private DataSourceProperties properties;

//    @Value("${my.taskAllowMultipleTasksFromTemplate}")
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties properties, TaskConfigurationProperties myProp) {
        this.properties = properties;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    public String url(){
        return properties.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}

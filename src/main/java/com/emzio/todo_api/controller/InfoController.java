package com.emzio.todo_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emzio.todo_api.TaskConfigurationProperties;

@RestController
public class InfoController {

    private DataSourceProperties properties;

//    @Value("${my.taskAllowMultipleTasksFromTemplate}")
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties properties, TaskConfigurationProperties myProp) {
        this.properties = properties;
        this.myProp = myProp;
    }

    @GetMapping("info/url")
    String url(){
        return properties.getUrl();
    }

    @GetMapping("info/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}

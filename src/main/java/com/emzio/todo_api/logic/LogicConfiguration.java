package com.emzio.todo_api.logic;

import com.emzio.todo_api.TaskConfigurationProperties;
import com.emzio.todo_api.model.ProjectRepository;
import com.emzio.todo_api.model.TaskGroupsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:applicationContext.xml")
public class LogicConfiguration {

    /*@Bean
    ProjectService projectService(
            ProjectRepository repository,
            TaskGroupsRepository taskGroupsRepository,
            TaskConfigurationProperties configuration
    ){
        return new ProjectService(repository, taskGroupsRepository, configuration);
    }*/
}

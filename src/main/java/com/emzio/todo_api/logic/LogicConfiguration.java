package com.emzio.todo_api.logic;

import com.emzio.todo_api.TaskConfigurationProperties;
import com.emzio.todo_api.model.ProjectRepository;
import com.emzio.todo_api.model.TaskGroupsRepository;
import com.emzio.todo_api.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupsRepository taskGroupsRepository,
            final TaskGroupService taskGroupService,
            final TaskConfigurationProperties configuration
    ){
        return new ProjectService(repository, taskGroupsRepository, taskGroupService, configuration);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupsRepository repository,
            final TaskRepository taskRepository){
        return new TaskGroupService(repository,taskRepository);
    }
}

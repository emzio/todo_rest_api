package com.emzio.todo_api;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskGroups;
import com.emzio.todo_api.model.TaskGroupsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupsRepository groupsRepository;

    public Warmup(TaskGroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        String description = "Application contextEvent";
        if (!groupsRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            var group = new TaskGroups();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextClosedEvent", null, group),
                    new Task("ContextStartedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group)
            ));
            groupsRepository.save(group);
        }
    }
}
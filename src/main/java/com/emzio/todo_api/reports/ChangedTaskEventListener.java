package com.emzio.todo_api.reports;

import com.emzio.todo_api.model.event.TaskDone;
import com.emzio.todo_api.model.event.TaskEvent;
import com.emzio.todo_api.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);
    private final PersistedEventTaskRepository repository;

    public ChangedTaskEventListener(PersistedEventTaskRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(TaskDone event){
        onChange(event);
    }

    @Async
    @EventListener
    public void on(TaskUndone event){
        onChange(event);
    }

    private void onChange(TaskEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvents(event));
    }
}

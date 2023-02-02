package com.emzio.todo_api;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskAuditable;
import com.emzio.todo_api.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    @Profile("!integration")
    public DataSource e2eTestDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1","sa", "");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean
    @Primary
    @Profile("integration")
    public TaskRepository testRepo(){
        return new TaskRepository() {
            private Map<Integer, Task> taskMap = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return taskMap.values().stream().toList();
            }

            @Override
            public Optional<Task> findById(Integer id) {
                return Optional.ofNullable(taskMap.get(id));
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public List<Task> findByDoneIs(Boolean done) {
                return taskMap.values().stream()
                        .filter(task -> task.isDone().equals(done))
                        .collect(Collectors.toList());
            }

            @Override
            public boolean existsById(Integer id) {
                return taskMap.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroupId(Integer groupId) {
                return taskMap.values().stream()
                        .anyMatch(task -> task.isDone() && task.getGroup().getId()==(groupId));
            }

            @Override
            public Task save(Task entity) {
                return taskMap.put(taskMap.size() + 1, entity);
            }
        };
    }
}

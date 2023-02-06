package com.emzio.todo_api.controller;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integrationTmp")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTasks(){
        //given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));
        //when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
        //then
        assertThat(result).hasSize(initial+2);
    }

    @Test
    void httpGet_returnsTaskForId(){
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        //when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);
        //then
        assertThat(result).isInstanceOf(Task.class);
        assertThat(result).matches(task-> task.getDescription().equals("foo"));
    }

    @Test
    void httpPost_returnsSavedTask(){
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        String reqJson =
                "    \"done\": true," +
                "    \"description\": \"test json\"," +
                "    \"deadline\": \"2023-10-12T23:59:59.999\"";
        RequestEntity<String> reqBody = RequestEntity.post("http://localhost:" + port + "/tasks").body(reqJson);

        //when
//        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);
            Task result = restTemplate.postForObject("http://localhost:" + port + "/tasks", reqBody, Task.class);
        //then
        assertThat(result).isInstanceOf(Task.class);
        assertThat(result).hasFieldOrProperty("description");
//        assertThat(result).matches(task-> task.getDescription().equals("foo"));
    }
}
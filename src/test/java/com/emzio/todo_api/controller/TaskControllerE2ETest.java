package com.emzio.todo_api.controller;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO delete profile integrationTmp
@ActiveProfiles("integrationTmp")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
class TaskControllerE2ETest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
// TODO put check status.ok
//    @Autowired
//    private MockMvc mockMvc;

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
        //TODO JsonNode method
//        //given
//        String url = "http://localhost:" + port + "/tasks";
//        //and
//        var taskJsonObject = getJsonObject(false, "JsonTest", LocalDateTime.now());
//        // and
//        HttpEntity<String> httpEntity = getHttpEntity(taskJsonObject);
//        // when
//        String result = restTemplate.postForObject(url, httpEntity, String.class);
//        try {
//            JsonNode root = objectMapper.readTree(result);
//            //        assertThat(result).isInstanceOf(Task.class);
////            assertThat(root).flatExtracting("description").isEqualTo("JsonTest");
//            assertThat(result).contains("description");
//            assertThat(result).contains("JsonTest");
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        //given
        String url = "http://localhost:" + port + "/tasks";
        //and
        var taskJsonObject = getJsonObject(false, "JsonTest", LocalDateTime.now());
        // and
        HttpEntity<String> httpEntity = getHttpEntity(taskJsonObject);
        // when
        Task result = restTemplate.postForObject(url, httpEntity, Task.class);
        // then
        assertThat(result).isInstanceOf(Task.class);
        assertThat(result.getDescription()).isEqualTo("JsonTest");
    }

    @Test
    void httpPut_returnsNoContent(){
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        // and
        String url = "http://localhost:" + port + "/tasks/" + id;
        //and
        JSONObject taskJsonObject = getJsonObject(true, "Put", LocalDateTime.now());
        HttpEntity<String> httpEntity = getHttpEntity(taskJsonObject);
        //when
        restTemplate.put(url,httpEntity);
        Task result = repo.findById(id).orElse(null);
        //then
        assertThat(result).isNotNull();
        assertThat(result.isDone()).isTrue();
        assertThat(result.getDescription()).isEqualTo("Put");
    }

//    @Test
//    void httpPut_returnsStatusOk(){
//        //given
//        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
//        // and
//        String url = "http://localhost:" + port + "/tasks/" + id;
//        //and
//        JSONObject taskJsonObject = getJsonObject(true, "Put", LocalDateTime.now());
//        HttpEntity<String> httpEntity = getHttpEntity(taskJsonObject);
//        //when
//        try {
//            mockMvc.perform(put(url, httpEntity).contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        restTemplate.put(url,httpEntity);
//        Task result = repo.findById(id).orElse(null);
//        //then
//        assertThat(result).isNotNull();
//        assertThat(result.isDone()).isTrue();
//        assertThat(result.getDescription()).isEqualTo("Put");
//given
//Task task = new Task("Will you save me?", LocalDateTime.now());
//
//    //when
//    RequestEntity<Task> request = RequestEntity
//            .post("http://localhost:" + localServerPort + "/tasks/create")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(task);
//
//    ResponseEntity<Task> response = testRestTemplate.exchange(request, Task.class);
//
//    //then
//    Task taskBody = response.getBody();
//
//    assertTrue(response.getStatusCode().is2xxSuccessful());
//    assertEquals("Will you save me?", taskBody.getDesc());
//    }


    private static JSONObject getJsonObject(boolean done, String description, LocalDateTime dateTime) {
        var taskJsonObject = new JSONObject();
        try {
            taskJsonObject.put("done", done);
            taskJsonObject.put("description", description);
            taskJsonObject.put("deadline", dateTime);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return taskJsonObject;
    }

    private static HttpEntity<String> getHttpEntity(JSONObject taskJsonObject) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(taskJsonObject.toString(), headers);
    }
}
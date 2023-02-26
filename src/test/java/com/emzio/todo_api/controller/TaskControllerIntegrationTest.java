package com.emzio.todo_api.controller;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;
import org.hibernate.validator.internal.engine.MethodValidationConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnsGivenTask(){
        // given
        int id = taskRepository.save(new Task("etwas", LocalDateTime.now())).getId();
        // when + then
        try {
            mockMvc.perform(get("/tasks/" + id))
                    .andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

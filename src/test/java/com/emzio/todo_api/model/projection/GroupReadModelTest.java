package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskGroups;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline(){
        //given
        var source = new TaskGroups();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));
        // when
        var result = new GroupReadModel(source);

        // then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}
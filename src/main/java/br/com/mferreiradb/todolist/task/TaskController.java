package br.com.mferreiradb.todolist.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository _taskRepository;

    @PostMapping
    public ResponseEntity<TaskModel> create(@RequestBody TaskModel body, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        body.setUserId((UUID) userId);
        var createdTask = this._taskRepository.save(body);

        return ResponseEntity.status(201).body(createdTask);
    }
}

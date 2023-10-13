package br.com.mferreiradb.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository _taskRepository;

    @PostMapping
    public ResponseEntity<TaskModel> create(@RequestBody TaskModel body) {
        System.out.println("Controller");
        var createdTask = this._taskRepository.save(body);

        return ResponseEntity.status(201).body(createdTask);
    }
}

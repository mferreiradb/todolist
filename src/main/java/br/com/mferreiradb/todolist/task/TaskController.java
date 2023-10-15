package br.com.mferreiradb.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mferreiradb.todolist.Utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository _taskRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel body, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        body.setUserId((UUID) userId);
        
        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(body.getStartedAt()) || currentDate.isAfter(body.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The start and end dates must be greater than or equals to the current data.");
        }
        if(body.getStartedAt().isAfter(body.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The start date cannot be greater than the end date.");
        }
        

        var createdTask = this._taskRepository.save(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAll(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var response = this._taskRepository.findByUserId((UUID) userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity udpate(HttpServletRequest request, @RequestBody TaskModel body, @PathVariable UUID taskId) {        
        var task = this._taskRepository.findTaskById(taskId);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task does not exists.");
        }

        var userId = request.getAttribute("userId");

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Utils.copyNonNullProperties(body, task);

        return ResponseEntity.status(HttpStatus.CREATED).body(this._taskRepository.save(task));
    }
}

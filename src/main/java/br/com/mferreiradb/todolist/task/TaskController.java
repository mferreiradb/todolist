package br.com.mferreiradb.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity create(@RequestBody TaskModel body, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        body.setUserId((UUID) userId);
        
        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(body.getStartedAt()) || currentDate.isAfter(body.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("As datas de inicio e fim devem ser maiores ou iguais à data atual.");
        }
        if(body.getStartedAt().isAfter(body.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio não pode ser superior à data de fim");
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
}

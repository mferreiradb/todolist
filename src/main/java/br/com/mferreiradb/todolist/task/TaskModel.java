package br.com.mferreiradb.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tbl_tasks")
public class TaskModel {
    
    @Id
    @GeneratedValue(generator = "UUID")

    private UUID id;

    private UUID userId;

    private String description;
    
    @Column(length = 50)
    private String title;
    
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private String priority;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {
        if(title.length() > 50) {
            throw new Exception("Title must be at least 50 characters.");
        }
        this.title = title;
    }

}

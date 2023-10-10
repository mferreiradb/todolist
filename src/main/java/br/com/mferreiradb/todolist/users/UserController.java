package br.com.mferreiradb.todolist.users;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping
    public void create(@RequestBody UserModel body) {
        System.out.println("Body: " + body.getName());
        System.out.println("Body: " + body.getUsername());
        System.out.println("Body: " + body.getPassword());
    }
}

package br.com.mferreiradb.todolist.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;
    @PostMapping
    public ResponseEntity create(@RequestBody UserModel body) {
        var userAlreadyExists = this.userRepository.findByUsername(body.getUsername());

        if(userAlreadyExists != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
        }

        var hashPassword = BCrypt.withDefaults().hashToString(12, body.getPassword().toCharArray());

        body.setPassword(hashPassword);

        var userCreted = this.userRepository.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreted);
    }
}

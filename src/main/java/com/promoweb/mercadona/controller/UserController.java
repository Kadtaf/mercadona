package com.promoweb.mercadona.controller;



import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){

        this.userService = userService;

    }

    //Read
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            throw new EntityNotFoundException("L'utilisateur avec l'id : " +id + " n'existe pas");
        }

    }

    //Create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Update
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updateUser = userService.updateUser(id, user);
        if (updateUser !=null) {
            return ResponseEntity.ok(updateUser);
        } else {
            throw new EntityNotFoundException("L'administrateur avec l'id : " + id + " n'existe pas");
        }
    }

    //Delete
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }





}
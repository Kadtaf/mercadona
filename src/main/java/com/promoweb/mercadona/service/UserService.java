package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        // Ajoutez ici la logique de validation ou de traitement avant d'enregistrer dans la base de données
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            // Ajoutez ici la logique de validation ou de traitement avant d'enregistrer dans la base de données
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("L'Administrateur avec l'id : " +id + " n'existe pas");
        }
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }  else {
            throw new EntityNotFoundException("L'utilisateur avec l'id : " +id + " n'existe pas");
        }

    }
}
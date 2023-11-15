package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        validateUser(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user){
       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
       userRepository.save(user);
    }

    public void deleteUser(Long id) throws EntityNotFoundException {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("L'utilisateur avec l'id : " + id + " n'existe pas");
        }
    }

    private void validateUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Nom d'utilisateur et mot de passe sont obligatoires");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Le nom d'utilisateur est déjà pris");
        }
    }

    public Page<User> findUsersWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findByLastnameContains(kw, pageable);
        }
    }

    public Long getIdUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user.getId();
    }

    public void setStatusUser(User user) {
        userRepository.save(user);
    }
}
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
        // Validation de traitement avant d'enregistrer dans la base de données
        validateUser(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            // Validation de traitement avant d'enregistrer dans la base de données
           // validateUser(user);
            // Mettez à jour d'autres champs si nécessaire
            existingUser.setUsername(user.getUsername());
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            //Hashé le mot de passe s'il est fournit
            if (user.getPassword() != null) {
                existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("L'Administrateur avec l'id : " + id + " n'existe pas");
        }

    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("L'utilisateur avec l'id : " + id + " n'existe pas");
        }

    }

    //Méthode de validation du formulaire
    private void validateUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Nom d'utilisateur et mot de passe sont obligatoires");
        }
        //Vériier l'unicité du nom d'utilisateur
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Le nom d'utilisateur est déjà pris");
        }
    }

    public Page<User> findUsersWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {
            // Si le mot-clé est vide, récupérez tous les utilisateurs avec pagination
            return userRepository.findAll(pageable);
        } else {
            // Si un mot-clé est fourni, recherchez les utilisateurs par mot-clé avec pagination
            return userRepository.findByLastnameContains(kw, pageable);
        }
    }

}
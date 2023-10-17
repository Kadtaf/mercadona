package com.promoweb.mercadona.repository;

import com.promoweb.mercadona.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Page<User> findByLastnameContains(String kw, Pageable pageable);

    User findByUsername(String username);
}

package com.promoweb.mercadona.repository;

import com.promoweb.mercadona.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByLabelContains(String kw, Pageable pageable);

    List<Category> findByStatus(Boolean status);
}

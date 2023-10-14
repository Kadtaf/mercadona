package com.promoweb.mercadona.repository;

import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPromotionIsNotNull();

    List<Product> findByUserId(Long admin_id);

    List<Product> findByCategoryId(Long category_id);
}

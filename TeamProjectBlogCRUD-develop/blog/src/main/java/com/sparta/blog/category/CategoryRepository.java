package com.sparta.blog.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findCategoriesByLayer(int layer);
    List<Category> findAllByOrderByNameAsc();

}
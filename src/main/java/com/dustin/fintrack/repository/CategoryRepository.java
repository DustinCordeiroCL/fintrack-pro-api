package com.dustin.fintrack.repository;

import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByUser(User user);

    List<Category> findAllByUserAndNameContainingIgnoreCase(User user, String name);

    Optional<Category> findByIdAndUser(Long id, User user);
}
package org.example.repositories;

import org.example.entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    void deleteByBookId(Long book_id);
}
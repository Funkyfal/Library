package org.example.repositories;

import org.example.entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    void deleteByBookId(Long book_id);
    Optional<Shelf> findByBookId(Long book_id);
    List<Shelf> findAllByAvailability(boolean availability);
}
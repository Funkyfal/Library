package org.example.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "book_id")
    private Long bookId;
    private boolean availability;
    private LocalDateTime takenAt;
    private LocalDateTime returnBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long book_id) {
        this.bookId = book_id;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public LocalDateTime getReturnBy() {
        return returnBy;
    }

    public void setReturnBy(LocalDateTime returnBy) {
        this.returnBy = returnBy;
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "id=" + id +
                ", book_id=" + bookId +
                ", status=" + availability +
                ", takenAt=" + takenAt +
                ", returnBy=" + returnBy +
                '}';
    }
}
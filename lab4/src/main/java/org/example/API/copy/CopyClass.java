package org.example.API.copy;

import jakarta.persistence.*;
import org.example.API.bookedition.BookEditionClass;

@Entity
@Table(name = "copies")
public class CopyClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_edition_id", nullable = false)
    private BookEditionClass bookEdition;

    @Transient
    private Long bookEditionId;

    @Column(nullable = false, unique = true)
    private String inventoryNumber;

    @Column(nullable = false)
    private String status;

    public CopyClass() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BookEditionClass getBookEdition() { return bookEdition; }
    public void setBookEdition(BookEditionClass bookEdition) {
        this.bookEdition = bookEdition;
        if (bookEdition != null) this.bookEditionId = bookEdition.getId();
    }

    public Long getBookEditionId() {
        return bookEdition != null ? bookEdition.getId() : bookEditionId;
    }
    public void setBookEditionId(Long bookEditionId) { this.bookEditionId = bookEditionId; }

    public String getInventoryNumber() { return inventoryNumber; }
    public void setInventoryNumber(String inventoryNumber) { this.inventoryNumber = inventoryNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
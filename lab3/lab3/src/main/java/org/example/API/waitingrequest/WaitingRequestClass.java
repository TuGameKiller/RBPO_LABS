package org.example.API.waitingrequest;

import jakarta.persistence.*;
import org.example.API.bookedition.BookEditionClass;
import org.example.API.user.UserClass;

@Entity
@Table(name = "waiting_requests")
public class WaitingRequestClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_edition_id", nullable = false)
    private BookEditionClass bookEdition;

    @Transient
    private Long bookEditionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserClass user;

    @Transient
    private Long userId;

    @Column(nullable = false)
    private String requestDate;

    @Column(nullable = false)
    private String status;

    public WaitingRequestClass() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BookEditionClass getBookEdition() { return bookEdition; }
    public void setBookEdition(BookEditionClass bookEdition) {
        this.bookEdition = bookEdition;
        if (bookEdition != null) this.bookEditionId = bookEdition.getId();
    }
    public Long getBookEditionId() { return bookEdition != null ? bookEdition.getId() : bookEditionId; }
    public void setBookEditionId(Long bookEditionId) { this.bookEditionId = bookEditionId; }

    public UserClass getUser() { return user; }
    public void setUser(UserClass user) {
        this.user = user;
        if (user != null) this.userId = user.getId();
    }
    public Long getUserId() { return user != null ? user.getId() : userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
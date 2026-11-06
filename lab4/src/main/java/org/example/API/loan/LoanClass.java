package org.example.API.loan;

import jakarta.persistence.*;
import org.example.API.copy.CopyClass;
import org.example.API.user.UserClass;

@Entity
@Table(name = "loans")
public class LoanClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "copy_id", nullable = false)
    private CopyClass copy;

    @Transient
    private Long copyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserClass user;

    @Transient
    private Long userId;

    @Column(nullable = false)
    private String loanDate;

    @Column
    private String returnDate;


    public LoanClass() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CopyClass getCopy() { return copy; }
    public void setCopy(CopyClass copy) {
        this.copy = copy;
        if (copy != null) this.copyId = copy.getId();
    }
    public Long getCopyId() { return copy != null ? copy.getId() : copyId; }
    public void setCopyId(Long copyId) { this.copyId = copyId; }

    public UserClass getUser() { return user; }
    public void setUser(UserClass user) {
        this.user = user;
        if (user != null) this.userId = user.getId();
    }
    public Long getUserId() { return user != null ? user.getId() : userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLoanDate() { return loanDate; }
    public void setLoanDate(String loanDate) { this.loanDate = loanDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
}
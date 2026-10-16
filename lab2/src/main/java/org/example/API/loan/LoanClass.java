package org.example.API.loan;

public class LoanClass {
    private Long id;
    private Long copyId;     // ID экземпляра книги
    private Long userId;     // ID читателя (User)
    private String loanDate; // Дата выдачи (генерируется сервером)
    private String returnDate; // Дата возврата

    public LoanClass() {}

    public LoanClass(Long id, Long copyId, Long userId, String loanDate, String returnDate) {
        this.id = id;
        this.copyId = copyId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCopyId() { return copyId; }
    public void setCopyId(Long copyId) { this.copyId = copyId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLoanDate() { return loanDate; }
    public void setLoanDate(String loanDate) { this.loanDate = loanDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
}
package org.example.API.waitingrequest;

public class WaitingRequestClass {
    private Long id;
    private Long bookEditionId; // ID книжного издания
    private Long userId;     // ID читателя
    private String requestDate; // Дата постановки (генерируется сервером)
    private String status;   // Статус заявки (например, "PENDING")

    public WaitingRequestClass() {}

    public WaitingRequestClass(Long id, Long bookEditionId, Long userId, String requestDate, String status) {
        this.id = id;
        this.bookEditionId = bookEditionId;
        this.userId = userId;
        this.requestDate = requestDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookEditionId() { return bookEditionId; }
    public void setBookEditionId(Long bookEditionId) { this.bookEditionId = bookEditionId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
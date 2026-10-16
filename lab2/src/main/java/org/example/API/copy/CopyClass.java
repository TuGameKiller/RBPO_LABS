package org.example.API.copy;

public class CopyClass {
    private Long id;
    private Long bookEditionId;
    private String inventoryNumber;
    private String status;

    public CopyClass() {}

    public CopyClass(Long id, Long bookEditionId, String inventoryNumber, String status) {
        this.id = id;
        this.bookEditionId = bookEditionId;
        this.inventoryNumber = inventoryNumber;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookEditionId() { return bookEditionId; }
    public void setBookEditionId(Long bookEditionId) { this.bookEditionId = bookEditionId; }

    public String getInventoryNumber() { return inventoryNumber; }
    public void setInventoryNumber(String inventoryNumber) { this.inventoryNumber = inventoryNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
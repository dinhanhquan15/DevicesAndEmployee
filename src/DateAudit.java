import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAudit {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime handoverDate;
    private LocalDateTime evictionDate;

    public DateAudit() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public LocalDateTime getHandoverDate() {return handoverDate;}
    public LocalDateTime getEvictionDate() {return evictionDate;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
    public void setHandoverDate(LocalDateTime handoverDate) {this.handoverDate = handoverDate;}
    public void setEvictionDate(LocalDateTime evictionDate) {this.evictionDate = evictionDate;}

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("CreatedAt=%s, UpdatedAt=%s, HandoverDate=%s, EvictionDate=%s",
                createdAt.format(formatter),
                updatedAt != null ? updatedAt.format(formatter) : "N/A",
                handoverDate != null ? handoverDate.format(formatter) : "N/A",
                evictionDate != null ? evictionDate.format(formatter) : "N/A");
    }
}

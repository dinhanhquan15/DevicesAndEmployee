import java.util.ArrayList;
import java.util.List;

public class Borrowing {
    private String idBorrowing;
    private DateAudit dateAudit;
    private double totalPrice;
    private Employee employee;
    private List<Device> devices;

    public Borrowing(String idBorrowing, Employee employee, List<Device> devices) {
        this.idBorrowing = idBorrowing;
        this.dateAudit = new DateAudit();
        this.employee = employee;
        this.devices = devices != null ? devices : new ArrayList<>();
        this.totalPrice = calculateTotalPrice();

    }

    private double calculateTotalPrice() {
        return devices.stream().mapToDouble(devices -> devices.getOriginPrice() * devices.getRateType().getRate()).sum();
    }

    public String getIdBorrowing() { return idBorrowing; }
    public DateAudit getDateAudit() { return dateAudit; }
    public double getTotalPrice() { return totalPrice; }
    public Employee getEmployee() { return employee; }
    public List<Device> getDevices() { return devices; }

    public void addDevice(Device device) {
        devices.add(device);
        totalPrice = calculateTotalPrice();
    }

    public void removeDevice(Device device) {
        devices.remove(device);
        totalPrice = calculateTotalPrice();
    }

    @Override
    public String toString() {
        return String.format("Borrowing{borrowingId=%s, TotalPrice=%.2f, Employee=%s, Devices=%d, %s}",
                idBorrowing, totalPrice, employee.getFullName(), devices.size(), dateAudit);
    }
}

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static DataManager instance;
    private List<Device> devices = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Borrowing> borrowings = new ArrayList<>();

    private DataManager() {
        initSampleData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initSampleData() {
        employees.add(new Employee("EMP-001", "Nguyen Van A", "Hanoi", "0123456789", 1000.0));
        employees.add(new Employee("EMP-002", "Tran Thi B", "HCM", "0987654321", 2000.0));

        devices.add(new Device("DEV-001", "Mouse", 50.0, RateType.NEW, "Branch1", "Mouse A", "1.0", 50.0));
        devices.add(new Device("DEV-002", "Keyboard", 100.0, RateType.LIKE_NEW, "Branch2", "KB B", "2.0", 120.0));

        List<Device> borrowingDevices = new ArrayList<>();
        borrowingDevices.add(devices.get(0));
        borrowings.add(new Borrowing("BOR-001", employees.get(0), borrowingDevices));
    }

    // Thêm Employee
    public void createEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null!");
        }
        if (employees.stream().anyMatch(e -> e.getIdEmployee().equals(employee.getIdEmployee()))) {
            throw new IllegalArgumentException("Employee ID " + employee.getIdEmployee() + " already exists!");
        }
        employees.add(employee);
        saveToFile("employees.txt", employees);
    }

    // Hiển thị danh sách Employee
    public List<Employee> viewEmployees() {
        return new ArrayList<>(employees);
    }

    public void createDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Device cannot be null!");
        }
        if (devices.stream().anyMatch(d -> d.getIdDevice().equals(device.getIdDevice()))) {
            throw new IllegalArgumentException("Device ID " + device.getIdDevice() + " already exists!");
        }
        devices.add(device);
        saveToFile("devices.txt", devices);
    }

    public List<Device> viewDevices() { return new ArrayList<>(devices); }

    public void editDevice(String deviceId, Device updatedDevice) {
        Device device = devices.stream().filter(d -> d.getIdDevice().equals(deviceId)).findFirst().orElse(null);
        if (device != null) {
            devices.remove(device);
            devices.add(updatedDevice);
            updatedDevice.getDateAudit().setUpdatedAt(LocalDateTime.now());
            saveToFile("devices.txt", devices);
        }
    }

    public void deleteDevice(String deviceId) {
        devices.removeIf(d -> d.getIdDevice().equals(deviceId));
        saveToFile("devices.txt", devices);
    }

    public List<Device> sortDevicesByPrice() {
        return devices.stream()
                .sorted(Comparator.comparingDouble(Device::getUntiPrice))
                .collect(Collectors.toList());
    }

    public List<Device> sortDevicesByCreatedAt() {
        return devices.stream()
                .sorted(Comparator.comparing(d -> d.getDateAudit().getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<Device> searchDevices(String keyword, String type, RateType rateType, LocalDateTime date) {
        return devices.stream()
                .filter(d -> (keyword == null || d.getNameDevice().contains(keyword)) &&
                        (type == null || d.getType().equals(type)) &&
                        (rateType == null || d.getRateType() == rateType) &&
                        (date == null || d.getDateAudit().getCreatedAt().toLocalDate().equals(date.toLocalDate())))
                .collect(Collectors.toList());
    }

    public void createBorrowing(Borrowing borrowing) {
        if (borrowings.stream().anyMatch(b -> b.getIdBorrowing().equals(borrowing.getIdBorrowing()))) {
            throw new IllegalArgumentException("Borrowing ID already exists!");
        }
        borrowings.add(borrowing);
        saveToFile("borrowings.txt", borrowings);
    }

    public List<Borrowing> viewBorrowings() { return new ArrayList<>(borrowings); }

    public void deleteBorrowing(String borrowingId) {
        borrowings.removeIf(b -> b.getIdBorrowing().equals(borrowingId));
        saveToFile("borrowings.txt", borrowings);
    }

    public List<Borrowing> sortBorrowingsByPrice() {
        return borrowings.stream()
                .sorted(Comparator.comparingDouble(Borrowing::getTotalPrice))
                .collect(Collectors.toList());
    }

    public List<Borrowing> sortBorrowingsByHandoverDate() {
        return borrowings.stream()
                .sorted(Comparator.comparing(b -> b.getDateAudit().getHandoverDate(), Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public List<Borrowing> searchBorrowings(String keyword, LocalDateTime date, double price) {
        return borrowings.stream()
                .filter(b -> (keyword == null || b.getEmployee().getFullName().contains(keyword)) &&
                        (date == null || (b.getDateAudit().getHandoverDate() != null && b.getDateAudit().getHandoverDate().toLocalDate().equals(date.toLocalDate()))) &&
                        (price == 0 || b.getTotalPrice() == price))
                .collect(Collectors.toList());
    }

    public void exportReport() {
        try (PrintWriter writer = new PrintWriter("report.txt")) {
            writer.println("Total Devices: " + devices.size());
            writer.println("Total Borrowings: " + borrowings.size());
            writer.println("Total Device Price: " + devices.stream().mapToDouble(Device::getUntiPrice).sum());
            writer.println("Total Borrowing Price: " + borrowings.stream().mapToDouble(Borrowing::getTotalPrice).sum());

            writer.println("\n5 Borrowings from 15 days ago:");
            LocalDateTime fifteenDaysAgo = LocalDateTime.now().minusDays(15);
            borrowings.stream()
                    .filter(b -> b.getDateAudit().getHandoverDate() != null && b.getDateAudit().getHandoverDate().isAfter(fifteenDaysAgo))
                    .limit(5)
                    .forEach(b -> writer.println(b));

            System.out.println("Report exported to report.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void evictAndMoveDevice(String borrowingId, String deviceId, String newBorrowingId) {
        Borrowing oldBorrowing = borrowings.stream().filter(b -> b.getIdBorrowing().equals(borrowingId)).findFirst().orElse(null);
        Borrowing newBorrowing = borrowings.stream().filter(b -> b.getIdBorrowing().equals(newBorrowingId)).findFirst().orElse(null);
        Device device = devices.stream().filter(d -> d.getIdDevice().equals(deviceId)).findFirst().orElse(null);

        if (oldBorrowing != null && newBorrowing != null && device != null && oldBorrowing.getDevices().contains(device)) {
            oldBorrowing.removeDevice(device);
            oldBorrowing.getDateAudit().setEvictionDate(LocalDateTime.now());
            newBorrowing.addDevice(device);
            newBorrowing.getDateAudit().setHandoverDate(LocalDateTime.now());
            saveToFile("borrowings.txt", borrowings);
        }
    }

    private <T> void saveToFile(String filename, List<T> data) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            data.forEach(writer::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
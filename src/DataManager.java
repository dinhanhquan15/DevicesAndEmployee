import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static DataManager instance;
    private List<Device> devices = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Borrowing> borrowings = new ArrayList<>();

    private DataManager() {
        initSampleData();
        loadDataFromFiles();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void loadDataFromFiles() {
        loadEmployeesFromFile("employees.txt");
        loadDeviceFromFile("devices.tst");
        loadBorrowingFromFile("borrowings.txt");

        if(employees.isEmpty() && devices.isEmpty() && borrowings.isEmpty()) {
            initSampleData();
            saveAllToFiles();
        }
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

    public void saveAllToFiles() {
        saveToFile("employees.txt", employees);
        saveToFile("devices.txt", devices);
        saveToFile("borrowings.txt", borrowings);
    }

    private void loadEmployeesFromFile(String filename) {
        try(BufferedReader rD = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = rD.readLine()) != null){
                String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String employeeId = parts[0].split("=")[1];
                        String fullName = parts[1].split("=")[1];
                        String address = parts[2].split("=")[1];
                        String phoneNumber = parts[3].split("=")[1];
                        double accountBalance = Double.parseDouble(parts[4].split("=")[1].replace("}", ""));
                        employees.add(new Employee(employeeId, fullName, address, phoneNumber, accountBalance));
                    }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Không tìm thấy file " + filename + ", sẽ dùng dữ liệu mặc định.");
        }
    }

    private void loadDeviceFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 7) {
                    String deviceId = parts[0].split("=")[1];
                    String type = parts[1].split("=")[1];
                    double unitPrice = Double.parseDouble(parts[2].split("=")[1]);
                    RateType rateType = RateType.valueOf(parts[3].split("=")[1]);
                    String itemName = parts[4].split("=")[1];
                    double originPrice = Double.parseDouble(parts[5].split("=")[1]);
                    // DateAudit sẽ được khởi tạo mặc định
                    Device device = new Device(deviceId, type, unitPrice, rateType, "BranchDefault", itemName, "1.0", originPrice);
                    devices.add(device);
                }
            }
        } catch (IOException e) {
            System.out.println("Không tìm thấy file " + filename + ", sẽ dùng dữ liệu mặc định.");
        }
    }

    private void loadBorrowingFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 4) {
                    String borrowingId = parts[0].split("=")[1];
                    double totalPrice = Double.parseDouble(parts[1].split("=")[1]);
                    String employeeName = parts[2].split("=")[1];
                    int deviceCount = Integer.parseInt(parts[3].split("=")[1]);
                    Employee employee = employees.stream()
                            .filter(e -> e.getFullName().equals(employeeName))
                            .findFirst()
                            .orElse(employees.get(0));
                    List<Device> borrowingDevices = new ArrayList<>();
                    borrowings.add(new Borrowing(borrowingId, employee, borrowingDevices));
                }
            }
        } catch (IOException e) {
            System.out.println("Không tìm thấy file " + filename + ", sẽ dùng dữ liệu mặc định.");
        }
    }

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
                .toList();
    }

    public List<Device> searchDevices(String keyword, String type, RateType rateType, LocalDateTime date) {
        return devices.stream()
                .filter(d -> (keyword == null || d.getNameDevice().contains(keyword)) &&
                        (type == null || d.getType().equals(type)) &&
                        (rateType == null || d.getRateType() == rateType) &&
                        (date == null || d.getDateAudit().getCreatedAt().toLocalDate().equals(date.toLocalDate())))
                .toList();
    }

    public void createBorrowing(Borrowing borrowing) {
        if (borrowings.stream().anyMatch(b -> b.getIdBorrowing().equals(borrowing.getIdBorrowing()))) {
            throw new IllegalArgumentException("Borrowing ID already exists!");
        }
        borrowings.add(borrowing);
        saveToFile("borrowings.txt", borrowings);
    }

    public List<Borrowing> viewBorrowings() { return new ArrayList<>(borrowings); }

    public List<Borrowing> sortBorrowingsByPrice() {
        return borrowings.stream()
                .sorted(Comparator.comparingDouble(Borrowing::getTotalPrice))
                .toList();
    }

    public List<Borrowing> searchBorrowings(String keyword, LocalDateTime date, double price) {
        return borrowings.stream()
                .filter(b -> (keyword == null || b.getEmployee().getFullName().contains(keyword)) &&
                        (date == null || (b.getDateAudit().getHandoverDate() != null && b.getDateAudit().getHandoverDate().toLocalDate().equals(date.toLocalDate()))) &&
                        (price == 0 || b.getTotalPrice() == price))
                .toList();
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
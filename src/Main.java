import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final Scanner sc = new Scanner(System.in);
    public static final DataManager dm = DataManager.getInstance();
    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            if (choice == 11) {
                System.out.println("Kết thúc chương trình");
                sc.close();
                return;
            }
            progressChoice(choice);
        }

    }
    private static void displayMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Thêm Employee");
        System.out.println("2. Thêm Device");
        System.out.println("3. Hiển thị danh sách");
        System.out.println("4. Sắp xếp thiết bị theo giá");
        System.out.println("5. Sắp xếp khoản mượn theo giá");
        System.out.println("6. Tổng thiết bị và tổng số tiền mượn");
        System.out.println("7. Tổng giá thiết bị và tổng giá khoản mượn");
        System.out.println("8. Liệt kê 5 khoản mượn trong 15 ngày trước");
        System.out.println("9. Xóa thiết bị khỏi khoản mượn và thêm vào khoản mượn khác");
        System.out.println("10. Tìm kiếm theo tên");
        System.out.println("11. Thoát chương trình");
        System.out.print("Chọn chức năng (1-11): ");
    }
    private static int getUserChoice() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số hợp lệ!");
            return -1;
        }
    }
    private static void progressChoice(int choice) {
        switch (choice) {
            case 1 -> addEmployee();
            case 2 -> addDevice();
            case 3 -> displayList();
            case 4 -> SortedDevice();
            case 5 -> SortedBorrowing();
            case 6 -> totalPrice();
            case 7 -> totalPriceBorrowing();
            case 8 -> recentBorrowing();
            case 9 -> deleteBorrowing();
            case 11 -> {}
            default ->  System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");

        }
    }

    private static void addEmployee() {
        System.out.print("Nhập Employee ID (EMP-XXX): ");
        String employeeId = sc.nextLine();
        System.out.print("Nhập họ tên: ");
        String fullName = sc.nextLine();
        System.out.print("Nhập địa chỉ: ");
        String address = sc.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String phoneNumber = sc.nextLine();
        System.out.print("Nhập số dư tài khoản: ");
        double accountBalance;
        try {
            accountBalance = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Số dư không hợp lệ, đặt mặc định là 0!");
            accountBalance = 0.0;
        }
        Employee newEmployee = new Employee(employeeId, fullName, address, phoneNumber, accountBalance);
        try {
            dm.createEmployee(newEmployee);
            System.out.println("Đã thêm Employee: " + newEmployee);
        } catch ( Exception e ) {
            System.out.println("lỗi: " + e.getMessage());
        }
    }

    private static void addDevice() {
        System.out.print("Nhập Device ID (DEV-XXX): ");
        String deviceId = sc.nextLine();

        System.out.print("Nhập loại thiết bị (Mouse/Keyboard/Display/...): ");
        String type = sc.nextLine();

        System.out.print("Nhập giá đơn vị: ");
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Giá không hợp lệ, đặt mặc định là 0!");
            unitPrice = 0.0;
        }

        System.out.print("Nhập RateType (NEW/LIKE_NEW/SECONDHAND/ERROR): ");
        RateType rateType;
        try {
            rateType = RateType.valueOf(sc.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("RateType không hợp lệ, đặt mặc định là NEW!");
            rateType = RateType.NEW;
        }
        System.out.print("Nhập tên chi nhánh: ");
        String branchName = sc.nextLine();

        System.out.print("Nhập tên thiết bị: ");
        String itemName = sc.nextLine();

        System.out.print("Nhập phiên bản: ");
        String version = sc.nextLine();

        System.out.print("Nhập giá gốc: ");
        double originPrice;
        try {
            originPrice = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Giá gốc không hợp lệ, đặt mặc định là 0!");
            originPrice = 0.0;
        }

        Device device = new Device(deviceId, type, unitPrice, rateType, branchName, itemName, version, originPrice);
        try {
            dm.createDevice(device);
            System.out.println("Đã thêm Device: " + device);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void displayList(){
        System.out.println("\nDanh sách Employee:");
        List<Employee> employees = dm.viewEmployees();
        if (employees.isEmpty()) {
            System.out.println("Chưa có Employee nào!");
        } else {
            employees.forEach(System.out::println);
        }
        System.out.println("\nDanh sách Device:");
        List<Device> devices = dm.viewDevices();
        if (devices.isEmpty()) {
            System.out.println("Chưa có Device nào!");
        } else {
            devices.forEach(System.out::println);
        }
        System.out.println("\n danh sách đơn mượn: ");
        List<Borrowing> borrowingroes = dm.viewBorrowings();
        if (borrowingroes.isEmpty()) {
            System.out.println("Chưa có Device nào!");
        } else {
            borrowingroes.forEach(System.out::println);
        }
    }

    private static void SortedDevice() {
        System.out.println("\n Danh sách Device sắp xếp theo giá:" );
        List<Device> sortedByPrice = dm.sortDevicesByPrice();
        if (sortedByPrice.isEmpty()) {
            System.out.println("Chưa có thiết bị nào!");
        } else {
            sortedByPrice.forEach(System.out::println);
        }
    }

    private static void SortedBorrowing() {
        System.out.println("\n Danh sách khoản mượn theo giá: ");
        List<Borrowing> sortedBorrowingsByPrice = dm.sortBorrowingsByPrice();
        if (sortedBorrowingsByPrice.isEmpty()) {
            System.out.println("Chưa có đơn mượn nào!");
        } else {
            sortedBorrowingsByPrice.forEach(System.out::println);
        }
    }

    private static void totalPrice() {
        System.out.println("Tổng số thiết bị: " + dm.viewEmployees().size());
        System.out.println("Tổng số tiền mượn thiết bị: " + dm.viewBorrowings().stream().mapToDouble(Borrowing::getTotalPrice).sum());
    }

    private static void totalPriceBorrowing() {
        System.out.println("Tổng giá của thiết bị: " + dm.viewDevices().stream().mapToDouble(Device::getUntiPrice).sum());
        System.out.println("Tổng giá của khoản mượn: " + dm.viewBorrowings().stream().mapToDouble(Borrowing::getTotalPrice).sum());
    }

    private static void recentBorrowing() {
        System.out.println("\n khoản mượn trong 15 ngày trước: ");
        LocalDateTime fifteenDaysAgo = LocalDateTime.now().minusDays(15);
        List<Borrowing> recentBorrowings = dm.viewBorrowings().stream()
                .filter(b ->b.getDateAudit().getHandoverDate() != null && b.getDateAudit().getHandoverDate().isAfter(fifteenDaysAgo))
                .limit(5)
                .toList();
        if (recentBorrowings.isEmpty()) {
            System.out.println("Không có khoản mượng nào trong 15 ngày! ");
        } else {
            recentBorrowings.forEach(System.out::println);
        }
    }

    private static void deleteBorrowing() {
        System.out.println("Nhập Borrowing ID hiện tại (BOR-XXX): ");
        String currentBorrowingId = sc.nextLine();

        System.out.println("Nhập ID thiết bị cần xóa(DEV-XXX): ");
        String deleteDevice = sc.nextLine();

        System.out.println("Nhập ID khoản mượn mới (BOR-XXX): ");
        String newBorrowingId = sc.nextLine();

        try{
            dm.evictAndMoveDevice(currentBorrowingId, deleteDevice, newBorrowingId);
            System.out.println("Đã xóa thiết bị mã " + deleteDevice + " khỏi " + currentBorrowingId + " và thêm vào "+ newBorrowingId);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void search() {
        System.out.println("\n=== TÌM KIẾM ===");
        System.out.println("1. Tìm kiếm Employee");
        System.out.println("2. Tìm kiếm Device");
        int searchType;
        try {
            searchType = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số hợp lệ!");
            return;
        }
        System.out.print("Nhập tên để tìm kiếm: ");
        String name = sc.nextLine();

        if (searchType == 1) {
            List<Employee> employees = dm.viewEmployees(); // dm là DataManager object
            List<Employee> result = employees.stream()
                    .filter(e -> e.getFullName().toLowerCase().contains(name.toLowerCase()))
                    .toList();

            System.out.println("\nKết quả tìm kiếm Employee:");
            if (result.isEmpty()) {
                System.out.println("Không tìm thấy Employee nào!");
            } else {
                result.forEach(System.out::println);
            }
        } else if (searchType == 2) {
            List<Device> searchDevices = dm.viewDevices(); // dm là DataManager object

            System.out.println("\nKết quả tìm kiếm Device:");
            if (searchDevices.isEmpty()) {
                System.out.println("Không tìm thấy Device nào!");
            } else {
                searchDevices.forEach(System.out::println);
            }
        } else {
            System.out.println("Loại tìm kiếm không hợp lệ!");
        }
    }
}
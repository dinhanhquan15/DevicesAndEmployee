import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        DataManager dm = DataManager.getInstance();
        Scanner cs = new Scanner(System.in);


        Device newDevice = new Device("DEV-003", "Display", 300.0, RateType.SECONDHAND, "Branch3", "Display C", "1.1", 400.0);
        dm.createDevice(newDevice);
        System.out.println("Tất cả các thiết bị:");
        dm.viewDevices().forEach(System.out::println);

        System.out.println("\nThiết bị được sắp xếp theo giá:");
        dm.sortDevicesByPrice().forEach(System.out::println);

        System.out.println("\nTìm kiếm thiết bị theo loại 'Mouse':");
        dm.searchDevices(null, "Mouse", null, null).forEach(System.out::println);


        List<Device> devicesForBorrowing = dm.viewDevices().subList(0, 2);
        Borrowing newBorrowing = new Borrowing("BOR-002", dm.viewBorrowings().get(0).getEmployee(), devicesForBorrowing);
        newBorrowing.getDateAudit().setHandoverDate(LocalDateTime.now());
        dm.createBorrowing(newBorrowing);

        System.out.println("\nTất cả các khoản vay:");
        dm.viewBorrowings().forEach(System.out::println);

        System.out.println("\nCác khoản vay được sắp xếp theo giá:");
        dm.sortBorrowingsByPrice().forEach(System.out::println);

        dm.exportReport();

        dm.evictAndMoveDevice("BOR-001", "DEV-001", "BOR-002");
        System.out.println("\nSau khi di chuyển thiết bị:");
        dm.viewBorrowings().forEach(System.out::println);

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Thêm Employee");
            System.out.println("2. Thêm Device");
            System.out.println("3. Hiển thị danh sách Employee");
            System.out.println("4. Hiển thị danh sách Device");
            System.out.println("5. Sắp xếp thiết bị theo giá");
            System.out.println("6. Sắp xếp khoản mượn theo giá");
            System.out.println("7. Tổng thiết bị và tổng số tiền mượn");
            System.out.println("8. HTổng giá thiết bị và tổng giá khoản mượn");
            System.out.println("9. Liệt kê 5 khoản mượn trong 15 ngày trước");
            System.out.println("10. Xóa thiết bị khỏi khoản mượn và thêm vào khoản mượn khác");
            System.out.println("11. Thoát chung trình");
            System.out.print("Chọn chức năng (1-11): ");

            int choice;
            try {
                choice = Integer.parseInt(cs.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Nhập Employee ID (EMP-XXX): ");
                    String employeeId = cs.nextLine();

                    System.out.print("Nhập họ tên: ");
                    String fullName = cs.nextLine();

                    System.out.print("Nhập địa chỉ: ");
                    String address = cs.nextLine();

                    System.out.print("Nhập số điện thoại: ");
                    String phoneNumber = cs.nextLine();

                    System.out.print("Nhập số dư tài khoản: ");
                    double accountBalance;
                    try {
                        accountBalance = Double.parseDouble(cs.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Số dư không hợp lệ, đặt mặc định là 0!");
                        accountBalance = 0.0;
                    }

                    Employee newEmployee = new Employee(employeeId, fullName, address, phoneNumber, accountBalance);
                    try {
                        dm.createEmployee(newEmployee);
                        System.out.println("Đã thêm Employee: " + newEmployee);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Lỗi: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Nhập Device ID (DEV-XXX): ");
                    String deviceId = cs.nextLine();

                    System.out.print("Nhập loại thiết bị (Mouse/Keyboard/Display/...): ");
                    String type = cs.nextLine();

                    System.out.print("Nhập giá đơn vị: ");
                    double unitPrice;
                    try {
                        unitPrice = Double.parseDouble(cs.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Giá không hợp lệ, đặt mặc định là 0!");
                        unitPrice = 0.0;
                    }

                    System.out.print("Nhập RateType (NEW/LIKE_NEW/SECONDHAND/ERROR): ");
                    RateType rateType;
                    try {
                        rateType = RateType.valueOf(cs.nextLine().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("RateType không hợp lệ, đặt mặc định là NEW!");
                        rateType = RateType.NEW;
                    }

                    System.out.print("Nhập tên chi nhánh: ");
                    String branchName = cs.nextLine();

                    System.out.print("Nhập tên thiết bị: ");
                    String itemName = cs.nextLine();

                    System.out.print("Nhập phiên bản: ");
                    String version = cs.nextLine();

                    System.out.print("Nhập giá gốc: ");
                    double originPrice;
                    try {
                        originPrice = Double.parseDouble(cs.nextLine());
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
                    break;

                case 3:
                    System.out.println("\nDanh sách Employee:");
                    List<Employee> employees = dm.viewEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("Chưa có Employee nào!");
                    } else {
                        employees.forEach(System.out::println);
                    }
                    break;

                case 4:
                    System.out.println("\nDanh sách Device:");
                    List<Device> devices = dm.viewDevices();
                    if (devices.isEmpty()) {
                        System.out.println("Chưa có Device nào!");
                    } else {
                        devices.forEach(System.out::println);
                    }
                    break;

                case 5:
                    System.out.println("\n Danh sách Device sắp xếp theo giá:" );
                    List<Device> sortedByPrice = dm.sortDevicesByPrice();
                    if (sortedByPrice.isEmpty()) {
                        System.out.println("Chưa có thiết bị nào!");
                    } else {
                        sortedByPrice.forEach(System.out::println);
                    }
                    break;

                case 6:
                    System.out.println("\n Danh sách khoản mượn theo giá: ");
                    List<Borrowing> sortedBorrowingsByPrice = dm.sortBorrowingsByPrice();
                    if (sortedBorrowingsByPrice.isEmpty()) {
                        System.out.println("Chưa có đơn mượn nào!");
                    } else {
                        sortedBorrowingsByPrice.forEach(System.out::println);
                    }
                    break;

                case 7:
                    System.out.println("Tổng số thiết bị: " + dm.viewEmployees().size());
                    System.out.println("Tổng số tiền mượn thiết bị: " + dm.viewBorrowings().stream().mapToDouble(Borrowing::getTotalPrice).sum());
                    break;

                case 8:
                    System.out.println("Tổng giá của thiết bị: " + dm.viewDevices().stream().mapToDouble(Device::getUntiPrice).sum());
                    System.out.println("Tổng giá của khoản mượn: " + dm.viewBorrowings().stream().mapToDouble(Borrowing::getTotalPrice).sum());

                case 9:
                    System.out.println("\n khoản mượn trong 15 ngày trước: ");
                    LocalDateTime fifteenDaysAgo = LocalDateTime.now().minusDays(15);
                    List<Borrowing> recentBorrowings = dm.viewBorrowings().stream()
                            .filter(b ->b.getDateAudit().getHandoverDate() != null && b.getDateAudit().getHandoverDate().isAfter(fifteenDaysAgo))
                            .limit(5)
                            .collect(Collectors.toList());
                    if (recentBorrowings.isEmpty()) {
                        System.out.println("Không có khoản mượng nào trong 15 ngày! ");
                    } else {
                        recentBorrowings.forEach(System.out::println);
                    }
                    break;

                case 10:
                    System.out.println("Nhập Borrowing ID hiện tại (BOR-XXX): ");
                    String currentBorrowingId = cs.nextLine();

                    System.out.println("Nhập ID thiết bị cần xóa(DEV-XXX): ");
                    String deleteDevice = cs.nextLine();

                    System.out.println("Nhập ID khoản mượn mới (BOR-XXX): ");
                    String newBorrowingId = cs.nextLine();

                    try{
                        dm.evictAndMoveDevice(currentBorrowingId, deleteDevice, newBorrowingId);
                        System.out.println("Đã xóa thiết bị mã " + deleteDevice + " khỏi " + currentBorrowingId + " và thêm vào "+ newBorrowingId);
                    } catch (Exception e) {
                        System.out.println("Lỗi: " + e.getMessage());
                    }
                    break;

                case 11:
                    System.out.println("Kết thúc chương trình !");
                    cs.close();
                    return;

                default:
                    System.out.println("Chức năng không hợp lệ, vui lòng chọn lại!");
            }
        }
    }
}
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataManager dm = DataManager.getInstance();
        Device newDevice = new Device("DEV-003", "Display", 300.0, RateType.SECONDHAND, "Branch3", "Display C", "1.1", 400.0);
        dm.createDevices(newDevice);
        System.out.println("Tất cả các thiết bị:");
        dm.viewDevices().forEach(System.out::println);

        System.out.println("\nThiết bị được sắp xếp theo giá:");
        dm.sortDevicesByPrice().forEach(System.out::println);

        System.out.println("\nTìm kiếm thiết bị theo loại 'Mouse':");
        dm.searchDevices(null, "Mouse", null, null).forEach(System.out::println);

        // Test Borrowing Functions
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

    }
}
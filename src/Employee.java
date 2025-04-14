public class Employee {
    private String idEmployee;
    private String fullName;
    private String address;
    private String phone;
    private double accountBalance;

    public Employee(String idEmployee, String fullName, String address, String phone, double accountBlance) {
        this.idEmployee = idEmployee;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.accountBalance = accountBalance;
    }
    public String getIdEmployee() { return idEmployee; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public double getAccountBalance() { return accountBalance; }

    @Override
    public String toString() {
        return String.format("Employee{ID=%s, FullName=%s, Phone=%s, Balance=%.2f}", idEmployee, fullName, phone, accountBalance);
    }
}

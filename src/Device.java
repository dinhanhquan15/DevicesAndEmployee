public class Device {
    private String idDevice;
    private String type;
    private double untiPrice;
    private RateType rateType;
    private DateAudit dateAudit;
    private String branchName;
    private String nameDevice;
    private String version;
    private double originPrice;

    public Device(String idDevice, String type, double untiPrice, RateType rateType,  String branchName, String nameDevice, String version, double originPrice ) {
        this.idDevice = idDevice;
        this.type = type;
        this.untiPrice = untiPrice;
        this.rateType = rateType;
        this.dateAudit = dateAudit;
        this.branchName = branchName;
        this.nameDevice = nameDevice;
        this.version = version;
        this.originPrice = originPrice;
    }

    public String getIdDevice() { return idDevice; }
    public String getType() { return type; }
    public double getUntiPrice() { return untiPrice; }
    public RateType getRateType() { return rateType; }
    public DateAudit getDateAudit() { return dateAudit; }
    public String getBranchName() { return branchName; }
    public String getNameDevice() { return nameDevice; }
    public String getVersion() { return version; }
    public double getOriginPrice() { return originPrice; }

    @Override
    public String toString() {
        return String.format("Device{ID=%s, Type=%s, UnitPrice=%.2f, RateType=%s,NameDevice=%s, OriginPrice=%.2f, DateAudit=%s}",
                idDevice, type, untiPrice, rateType, nameDevice, originPrice, dateAudit);
    }
}

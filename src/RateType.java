public enum RateType {
    NEW(1.0), LIKE_NEW(0.8), SECONDHAND(0.5), ERROR(0.1);
    private final double rate;
    RateType(double rate) {
        this.rate = rate;
    }
    public double getRate() { return rate; }
}
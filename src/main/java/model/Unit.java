package main.java.model;

public enum Unit {
    KG("Килограммы", 1.0),
    POUND("Фунты", 0.453592);

    private final String displayName;
    private final double kgFactor; // коэффициент перевода в кг

    Unit(String displayName, double kgFactor) {
        this.displayName = displayName;
        this.kgFactor = kgFactor;
    }

    public String getDisplayName() { return displayName; }
    public double getKgFactor() { return kgFactor; }
}
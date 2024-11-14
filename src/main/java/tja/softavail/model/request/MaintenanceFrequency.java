package tja.softavail.model.request;

public enum MaintenanceFrequency {
    VERY_LOW("very_low"),
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String displayName;

    MaintenanceFrequency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

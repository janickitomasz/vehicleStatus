package tja.softavail.model.response;

public enum MaintenanceScoreType {
    POOR("poor"),
    AVERAGE("average"),
    GOOD("good");

    final String displayName;

    MaintenanceScoreType(String displayName) {
        this.displayName = displayName;
    }
}

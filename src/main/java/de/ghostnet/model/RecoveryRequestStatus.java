package de.ghostnet.model;

public enum RecoveryRequestStatus {

    PENDING("Offen"),
    ACCEPTED("Angenommen"),
    REJECTED("Abgelehnt");

    private final String label;

    RecoveryRequestStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
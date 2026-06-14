package de.ghostnet.model;

public enum GhostNetStatus {

    REPORTED("Gemeldet"),
    RECOVERY_PENDING("Bergung bevorstehend"),
    RECOVERED("Geborgen"),
    LOST("Verschollen");

    private final String label;

    GhostNetStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
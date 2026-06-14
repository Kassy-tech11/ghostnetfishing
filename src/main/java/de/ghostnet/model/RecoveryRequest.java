package de.ghostnet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "recovery_request")
public class RecoveryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GhostNet ghostNet;

    @ManyToOne
    private Person requester;

    @Enumerated(EnumType.STRING)
    private RecoveryRequestStatus status;

    private LocalDateTime createdAt;

    public RecoveryRequest() {
    }

    public RecoveryRequest(GhostNet ghostNet, Person requester) {
        this.ghostNet = ghostNet;
        this.requester = requester;
        this.status = RecoveryRequestStatus.PENDING;
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = RecoveryRequestStatus.PENDING;
        }
    }

    public Long getId() {
        return id;
    }

    public GhostNet getGhostNet() {
        return ghostNet;
    }

    public void setGhostNet(GhostNet ghostNet) {
        this.ghostNet = ghostNet;
    }

    public Person getRequester() {
        return requester;
    }

    public void setRequester(Person requester) {
        this.requester = requester;
    }

    public RecoveryRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
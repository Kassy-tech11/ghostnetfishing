package de.ghostnet.service;

import de.ghostnet.model.GhostNet;
import de.ghostnet.model.GhostNetStatus;
import de.ghostnet.model.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import de.ghostnet.model.RecoveryRequest;
import de.ghostnet.model.RecoveryRequestStatus;

import java.util.List;

@ApplicationScoped
public class GhostNetService {

    @PersistenceContext(unitName = "ghostNetPU")
    private EntityManager entityManager;

    @Transactional
    public void reportGhostNet(GhostNet ghostNet, String reporterName, String reporterPhoneNumber) {
        if (reporterName != null && !reporterName.isBlank()
                && reporterPhoneNumber != null && !reporterPhoneNumber.isBlank()) {

            Person reporter = new Person(reporterName, reporterPhoneNumber);
            entityManager.persist(reporter);
            ghostNet.setReporter(reporter);
        }

        entityManager.persist(ghostNet);
    }

    public List<GhostNet> findOpenGhostNets() {
        return entityManager
                .createQuery("""
                        SELECT g
                        FROM GhostNet g
                        WHERE g.status IN :statuses
                        ORDER BY g.createdAt DESC
                        """, GhostNet.class)
                .setParameter("statuses", List.of(
                        GhostNetStatus.REPORTED,
                        GhostNetStatus.RECOVERY_PENDING
                ))
                .getResultList();
    }

    public GhostNet findGhostNetById(Long id) {
        return entityManager.find(GhostNet.class, id);
    }

    @Transactional
    public void claimGhostNet(Long ghostNetId, String recovererName, String phoneNumber) {
        GhostNet ghostNet = entityManager.find(GhostNet.class, ghostNetId);

        if (ghostNet == null) {
            throw new IllegalArgumentException("Das ausgewählte Geisternetz wurde nicht gefunden.");
        }

        if (ghostNet.getRecoverer() != null) {
            throw new IllegalStateException("Dieses Geisternetz ist bereits einer bergenden Person zugeordnet.");
        }

        Person recoverer = new Person(recovererName, phoneNumber);
        entityManager.persist(recoverer);

        ghostNet.setRecoverer(recoverer);
        ghostNet.setStatus(GhostNetStatus.RECOVERY_PENDING);
    }
    public List<GhostNet> findPendingRecoveries() {
        return entityManager
                .createQuery("""
                        SELECT g
                        FROM GhostNet g
                        WHERE g.status = :status
                        ORDER BY g.updatedAt DESC
                        """, GhostNet.class)
                .setParameter("status", GhostNetStatus.RECOVERY_PENDING)
                .getResultList();
    }
    @Transactional
    public void markAsRecovered(Long ghostNetId) {
        GhostNet ghostNet = entityManager.find(GhostNet.class, ghostNetId);

        if (ghostNet == null) {
            throw new IllegalArgumentException("Das ausgewählte Geisternetz wurde nicht gefunden.");
        }

        if (ghostNet.getRecoverer() == null) {
            throw new IllegalStateException("Dieses Geisternetz wurde noch keiner bergenden Person zugeordnet.");
        }

        ghostNet.setStatus(GhostNetStatus.RECOVERED);
    }
    public List<GhostNet> findAssignedGhostNets() {
        return entityManager
                .createQuery("""
                        SELECT g
                        FROM GhostNet g
                        WHERE g.recoverer IS NOT NULL
                        ORDER BY g.updatedAt DESC
                        """, GhostNet.class)
                .getResultList();
    }
    @Transactional
    public void requestRecoveryTakeover(Long ghostNetId, String requesterName, String phoneNumber) {
        GhostNet ghostNet = entityManager.find(GhostNet.class, ghostNetId);

        if (ghostNet == null) {
            throw new IllegalArgumentException("Das ausgewählte Geisternetz wurde nicht gefunden.");
        }

        if (!GhostNetStatus.RECOVERY_PENDING.equals(ghostNet.getStatus())) {
            throw new IllegalStateException("Eine Übernahmeanfrage ist nur für Netze mit bevorstehender Bergung möglich.");
        }

        if (ghostNet.getRecoverer() == null) {
            throw new IllegalStateException("Für dieses Geisternetz ist aktuell keine bergende Person eingetragen.");
        }

        Long existingRequests = entityManager
                .createQuery("""
                        SELECT COUNT(r)
                        FROM RecoveryRequest r
                        WHERE r.ghostNet = :ghostNet
                        AND r.status = :status
                        AND r.requester.phoneNumber = :phoneNumber
                        """, Long.class)
                .setParameter("ghostNet", ghostNet)
                .setParameter("status", RecoveryRequestStatus.PENDING)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult();

        if (existingRequests > 0) {
            throw new IllegalStateException("Für diese Telefonnummer existiert bereits eine offene Übernahmeanfrage.");
        }

        Person requester = new Person(requesterName, phoneNumber);
        entityManager.persist(requester);

        RecoveryRequest request = new RecoveryRequest(ghostNet, requester);
        entityManager.persist(request);
    }
    public List<RecoveryRequest> findPendingRecoveryRequests() {
        return entityManager
                .createQuery("""
                        SELECT r
                        FROM RecoveryRequest r
                        WHERE r.status = :status
                        ORDER BY r.createdAt DESC
                        """, RecoveryRequest.class)
                .setParameter("status", RecoveryRequestStatus.PENDING)
                .getResultList();
    }
    @Transactional
    public void acceptRecoveryRequest(Long requestId) {
        RecoveryRequest request = entityManager.find(RecoveryRequest.class, requestId);

        if (request == null) {
            throw new IllegalArgumentException("Die Übernahmeanfrage wurde nicht gefunden.");
        }

        if (!RecoveryRequestStatus.PENDING.equals(request.getStatus())) {
            throw new IllegalStateException("Diese Übernahmeanfrage ist nicht mehr offen.");
        }

        GhostNet ghostNet = request.getGhostNet();

        if (ghostNet == null) {
            throw new IllegalStateException("Zur Anfrage gehört kein gültiges Geisternetz.");
        }

        if (!GhostNetStatus.RECOVERY_PENDING.equals(ghostNet.getStatus())) {
            throw new IllegalStateException("Das Geisternetz befindet sich nicht mehr in einer laufenden Bergung.");
        }

        ghostNet.setRecoverer(request.getRequester());
        ghostNet.setStatus(GhostNetStatus.RECOVERY_PENDING);

        request.setStatus(RecoveryRequestStatus.ACCEPTED);

        List<RecoveryRequest> otherRequests = entityManager
                .createQuery("""
                        SELECT r
                        FROM RecoveryRequest r
                        WHERE r.ghostNet = :ghostNet
                        AND r.status = :status
                        AND r.id <> :acceptedRequestId
                        """, RecoveryRequest.class)
                .setParameter("ghostNet", ghostNet)
                .setParameter("status", RecoveryRequestStatus.PENDING)
                .setParameter("acceptedRequestId", request.getId())
                .getResultList();

        for (RecoveryRequest otherRequest : otherRequests) {
            otherRequest.setStatus(RecoveryRequestStatus.REJECTED);
        }
    }
    @Transactional
    public void rejectRecoveryRequest(Long requestId) {
        RecoveryRequest request = entityManager.find(RecoveryRequest.class, requestId);

        if (request == null) {
            throw new IllegalArgumentException("Die Übernahmeanfrage wurde nicht gefunden.");
        }

        if (!RecoveryRequestStatus.PENDING.equals(request.getStatus())) {
            throw new IllegalStateException("Diese Übernahmeanfrage ist nicht mehr offen.");
        }

        request.setStatus(RecoveryRequestStatus.REJECTED);
    }
}
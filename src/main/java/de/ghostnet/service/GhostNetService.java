package de.ghostnet.service;

import de.ghostnet.model.GhostNet;
import de.ghostnet.model.GhostNetStatus;
import de.ghostnet.model.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class GhostNetService {

    @PersistenceContext(unitName = "ghostNetPU")
    private EntityManager entityManager;

    @Transactional
    public void reportGhostNet(GhostNet ghostNet) {
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
}
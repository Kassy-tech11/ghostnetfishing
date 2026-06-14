package de.ghostnet.web;

import de.ghostnet.model.GhostNet;
import de.ghostnet.service.GhostNetService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class RecoveryOverviewBean {

    @Inject
    private GhostNetService ghostNetService;

    private List<GhostNet> pendingRecoveries;

    @PostConstruct
    public void init() {
        pendingRecoveries = ghostNetService.findPendingRecoveries();
    }

    public String markAsRecovered(Long ghostNetId) {
        try {
            ghostNetService.markAsRecovered(ghostNetId);
            return "recovery-overview.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException | IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null)
            );
            return null;
        }
    }

    public List<GhostNet> getPendingRecoveries() {
        return pendingRecoveries;
    }
}
package de.ghostnet.web;

import de.ghostnet.model.RecoveryRequest;
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
public class RecoveryRequestOverviewBean {

    @Inject
    private GhostNetService ghostNetService;

    private List<RecoveryRequest> pendingRequests;

    @PostConstruct
    public void init() {
        pendingRequests = ghostNetService.findPendingRecoveryRequests();
    }

    public String accept(Long requestId) {
        try {
            ghostNetService.acceptRecoveryRequest(requestId);
            return "recovery-requests.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException | IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null)
            );
            return null;
        }
    }

    public String reject(Long requestId) {
        try {
            ghostNetService.rejectRecoveryRequest(requestId);
            return "recovery-requests.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException | IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null)
            );
            return null;
        }
    }

    public List<RecoveryRequest> getPendingRequests() {
        return pendingRequests;
    }
}
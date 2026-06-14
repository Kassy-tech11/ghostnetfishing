package de.ghostnet.web;

import de.ghostnet.model.GhostNet;
import de.ghostnet.service.GhostNetService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class TakeoverRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private GhostNetService ghostNetService;

    private Long ghostNetId;
    private GhostNet ghostNet;

    private String requesterName;
    private String phoneNumber;

    public void loadGhostNet() {
        if (ghostNetId != null) {
            ghostNet = ghostNetService.findGhostNetById(ghostNetId);
        }
    }

    public String submitRequest() {
        try {
            ghostNetService.requestRecoveryTakeover(ghostNetId, requesterName, phoneNumber);
            return "recovery-requests.xhtml?faces-redirect=true";
        } catch (IllegalArgumentException | IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null)
            );
            loadGhostNet();
            return null;
        }
    }

    public Long getGhostNetId() {
        return ghostNetId;
    }

    public void setGhostNetId(Long ghostNetId) {
        this.ghostNetId = ghostNetId;
    }

    public GhostNet getGhostNet() {
        if (ghostNet == null && ghostNetId != null) {
            ghostNet = ghostNetService.findGhostNetById(ghostNetId);
        }
        return ghostNet;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}